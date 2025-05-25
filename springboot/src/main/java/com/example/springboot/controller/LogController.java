package com.example.springboot.controller;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Log;
import com.example.springboot.service.LogService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
//request.js(axios请求，创建统一的前端请求实例) -> Result.java(统一响应包装类) -> Log.java(实体类-数据模型) -> Controller.java(http入口) -> Service.java(调用接口方法) -> mapper.java(数据访问接口) -> Mapper.xml(SQL映射文件)

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private LogService logService;

    @GetMapping("/selectAll") //get: 查询全部的数据库中的内容操作
    public Result selectAll(Log log) {
        List<Log> list = logService.selectAll(log);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}") //get: 使用动态的参数查询特定的数据内容
    public Result selectByfID(@PathVariable Integer id) {
        Log log = logService.selectById(id);
        return Result.success(log);
    }

    @GetMapping("/selectOne") //get: 使用参数查询特定的数据内容
    public Result selectOne(@RequestParam Integer id) {
        Log log = logService.selectById(id);
        return Result.success(log);
    }

    @GetMapping("/selectPage") //get: 可使用页面参数和页面大小做个性化查询
    public Result selectPage(
            Log log,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Log> pageInfo = logService.selectPage(log,  pageNum, pageSize);
        return Result.success(pageInfo);
    }



    //delete: 删除操作
    @DeleteMapping("/deleteById/{id}")
    @AutoLog("刪除日誌")
    public Result deleteById(@PathVariable Integer id) {
        logService.deleteById(id);
        return Result.success(null);
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    @AutoLog("批量刪除日誌")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        logService.deleteBatch(ids);
        return Result.success(null);
    }
    //导出数据
    @GetMapping("/export")
    @AutoLog("導出日誌文件")
    public Result export(HttpServletResponse response,
                       @RequestParam(required = false) String username,
                       @RequestParam(required = false) String name) throws Exception {
        //1.拿到所有的员工数据
        List<Log> logList = logService.selectAll(null);
        //2.构建 ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3.设置中文表头 以及输出流的头信息
        writer.addHeaderAlias("time", "時間");
        writer.addHeaderAlias("name", "操作人");
        writer.addHeaderAlias("username", "操作賬號");
        writer.addHeaderAlias("type", "操作內容");
        writer.addHeaderAlias("ip", "ip地址");
        writer.setOnlyAlias(true); //只导出设定别名的字段
        //4.写出数据到writer
        writer.write(logList, true);
        //5.设置输出文件的名称
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("產品信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //6. 写出到输出流 并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        Account account = new Account();
        account.setUsername(username);
        account.setName(name);
        return Result.success(account);
    }


}
