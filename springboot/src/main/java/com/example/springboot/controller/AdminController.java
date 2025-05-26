package com.example.springboot.controller;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Admin;
import com.example.springboot.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
//request.js(axios请求，创建统一的前端请求实例) -> Result.java(统一响应包装类) -> Admin.java(实体类-数据模型) -> Controller.java(http入口) -> Service.java(调用接口方法) -> mapper.java(数据访问接口) -> Mapper.xml(SQL映射文件)

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @GetMapping("/selectAll") //get: 查询全部的数据库中的内容操作
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}") //get: 使用动态的参数查询特定的数据内容
    public Result selectByfID(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    @GetMapping("/selectOne") //get: 使用参数查询特定的数据内容
    public Result selectOne(@RequestParam Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    @GetMapping("/selectPage") //get: 可使用页面参数和页面大小做个性化查询
    public Result selectPage(
            Admin admin,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Admin> pageInfo = adminService.selectPage(admin,  pageNum, pageSize);
        return Result.success(pageInfo);
    }

    //post: 新增操作
    @PostMapping("/add")
    @AutoLog("新增管理員")
    //@RequestBody可以把前端传来的JSON字符串映射为java的对象，或者数组
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success(null);
    }

    //put: 修改操作
    @PutMapping("/update")
    @AutoLog("更新管理員")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success(null);
    }

    //delete: 删除操作
    @DeleteMapping("/deleteById/{id}")
    @AutoLog("刪除管理員")
    public Result deleteById(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success(null);
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    @AutoLog("批量刪除管理員")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        adminService.deleteBatch(ids);
        return Result.success(null);
    }

    @GetMapping("/export/info")
    @AutoLog("導出管理員文件")
    public Result exportInfo() {
        Account account = new Account();
        return Result.success(account);
    }

    //导出数据
    @GetMapping("/export")
    @AutoLog("導出管理員文件")
    public void export(HttpServletResponse response) throws Exception {
        //1.拿到所有的员工数据
        List<Admin> adminList = adminService.selectAll(null);
        //2.构建 ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3.设置中文表头 以及输出流的头信息
        writer.addHeaderAlias("name", "名稱");
        writer.addHeaderAlias("username", "賬號");
        writer.setOnlyAlias(true); //只导出设定别名的字段
        //4.写出数据到writer
        writer.write(adminList, true);
        //5.设置输出文件的名称
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理員信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //6. 写出到输出流 并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
    }

    //导入
    @PostMapping("/import")
    @AutoLog("導入管理員文件")
    public Result importData(MultipartFile file,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String name) throws Exception{
        //1.拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader =  ExcelUtil.getReader(inputStream);
        //2. 读取Excel里面的数据
        reader.addHeaderAlias("名稱","name" );
        reader.addHeaderAlias("賬號","username");
        List<Admin> adminList = reader.readAll(Admin.class);
        //写入List数据到数据库
        for (Admin admin : adminList) {
            adminService.add(admin);
        }
        Account account = new Account();
        account.setUsername(username);
        account.setName(name);
        return Result.success(account);
    }

}
