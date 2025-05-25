package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileController {
    private static final String filePath = System.getProperty("user.dir") + "/files/";
    @PostMapping("/upload") //文件上传接口
    @AutoLog("上傳文件")
    public Result upload(MultipartFile file){ // 文件流的形式接收前端发过来的文件
        String originalFilename = file.getOriginalFilename();
        if (!FileUtil.isDirectory(filePath)) { //如果目录不存在
            FileUtil.mkdir(filePath); //创建一个目录
        }
        //提供文件存储的完整的路径
        //给文件名一个唯一的标识
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName; //完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(), realPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件上传失败");
        }

        //返回一个网络连接
        //http://localhost:9090/api/files/download/xxx.jpg
        String url = "https://black.ntubbirc.ggff.net/api/files/download/" + fileName;
        return Result.success(url);
    }

    //文件下载接口
    @GetMapping("/download/{fileName}")
    @AutoLog("下載文件")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            OutputStream os = response.getOutputStream(); //获取到文件的字节数组
            os.write(FileUtil.readBytes(filePath + fileName));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500", "文件下载失败");
        }
    }
}
