package com.example.springboot.controller;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Employee;
import com.example.springboot.service.EmployeeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
//request.js(axios请求，创建统一的前端请求实例) -> Result.java(统一响应包装类) -> Employee.java(实体类-数据模型) -> Controller.java(http入口) -> Service.java(调用接口方法) -> mapper.java(数据访问接口) -> Mapper.xml(SQL映射文件)

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    @GetMapping("/selectAll") //get: 查询全部的数据库中的内容操作
    public Result selectAll(Employee employee) {
        List<Employee> list = employeeService.selectAll(employee);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}") //get: 使用动态的参数查询特定的数据内容
    public Result selectByfID(@PathVariable Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    @GetMapping("/selectOne") //get: 使用参数查询特定的数据内容
    public Result selectOne(@RequestParam Integer id) {
        Employee employee = employeeService.selectById(id);
        return Result.success(employee);
    }

    @GetMapping("/selectPage") //get: 可使用页面参数和页面大小做个性化查询
    public Result selectPage(
            Employee employee,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employee> pageInfo = employeeService.selectPage(employee,  pageNum, pageSize);
        return Result.success(pageInfo);
    }


    //post: 新增操作
    @PostMapping("/add")
    @AutoLog("新增用戶")
    //@RequestBody可以把前端传来的JSON字符串映射为java的对象，或者数组
    public Result add(@RequestBody Employee employee) {
        employeeService.add(employee);
        return Result.success(null);
    }

    //put: 修改操作
    @PutMapping("/update")
    @AutoLog("更新用戶")
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success(null);
    }

    //delete: 删除操作
    @DeleteMapping("/deleteById/{id}")
    @AutoLog("刪除用戶")
    public Result deleteById(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return Result.success(null);
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    @AutoLog("批量刪除用戶")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        employeeService.deleteBatch(ids);
        return Result.success(null);
    }
    //导出数据
    @GetMapping("/export")
    @AutoLog("導出用戶文件")
    public void export(HttpServletResponse response) throws Exception {
        //1.拿到所有的员工数据
        List<Employee> employeeList = employeeService.selectAll(null);
        //2.构建 ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3.设置中文表头 以及输出流的头信息
        writer.addHeaderAlias("name", "名稱");
        writer.addHeaderAlias("username", "賬號");
        writer.addHeaderAlias("age", "年齡");
        writer.addHeaderAlias("sex", "性別");
        writer.addHeaderAlias("departmentName", "部門");
        writer.addHeaderAlias("no", "工號");
        writer.addHeaderAlias("description", "個人簡介");
        writer.setOnlyAlias(true); //只导出设定别名的字段
        //4.写出数据到writer
        writer.write(employeeList, true);
        //5.设置输出文件的名称
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("员工信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //6. 写出到输出流 并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
    }

    //导入
    @PostMapping("/import")
    @AutoLog("導入用戶文件")
    public Result importData(MultipartFile file) throws Exception{
        //1.拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader =  ExcelUtil.getReader(inputStream);
        //2. 读取Excel里面的数据
        reader.addHeaderAlias("名稱","name" );
        reader.addHeaderAlias("賬號","username");
        reader.addHeaderAlias( "年齡", "age");
        reader.addHeaderAlias("性別","sex");
        reader.addHeaderAlias("部門", "departmentId");
        reader.addHeaderAlias("工號","no");
        reader.addHeaderAlias("個人簡介","description");
        List<Employee> employeeList = reader.readAll(Employee.class);
        //写入List数据到数据库
        for (Employee employee : employeeList) {
            employeeService.add(employee);
        }
        return Result.success(null);
    }


}
