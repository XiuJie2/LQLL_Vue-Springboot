package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${file.upload-dir:/app/files}")
    private String fileUploadDir;

    @PostMapping("/upload")
    @AutoLog("上傳文件")
    public Result upload(
            @RequestPart MultipartFile file,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String name) {

        // 確保目錄存在
        File dir = new File(fileUploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + fileExtension;
        String filePath = fileUploadDir + File.separator + fileName;

        try {
            // 寫入文件
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new CustomException("500", "文件上傳失敗: " + e.getMessage());
        }

        // 返回可訪問的 URL
        String fileUrl = String.format("%s/api/files/%s",
                System.getenv("API_BASE_URL"), fileName);

        Account account = new Account();
        account.setUsername(username);
        account.setName(name);
        account.setUrl(fileUrl);

        return Result.success(account);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(fileUploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new CustomException("404", "文件不存在或不可讀");
            }

            String contentType = determineContentType(filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(resource);
        } catch (Exception e) {
            throw new CustomException("500", "文件下載失敗: " + e.getMessage());
        }
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }
}