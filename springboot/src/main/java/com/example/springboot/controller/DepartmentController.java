package com.example.springboot.controller;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Department;
import com.example.springboot.service.DepartmentService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//request.js(axios请求，创建统一的前端请求实例) -> Result.java(统一响应包装类) -> Department.java(实体类-数据模型) -> Controller.java(http入口) -> Service.java(调用接口方法) -> mapper.java(数据访问接口) -> Mapper.xml(SQL映射文件)

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Resource
    private DepartmentService departmentService;

    @GetMapping("/selectAll") //get: 查询全部的数据库中的内容操作
    public Result selectAll(Department department) {
        List<Department> list = departmentService.selectAll(department);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}") //get: 使用动态的参数查询特定的数据内容
    public Result selectByfID(@PathVariable Integer id) {
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }

    @GetMapping("/selectOne") //get: 使用参数查询特定的数据内容
    public Result selectOne(@RequestParam Integer id) {
        Department department = departmentService.selectById(id);
        return Result.success(department);
    }

    @GetMapping("/selectPage") //get: 可使用页面参数和页面大小做个性化查询
    public Result selectPage(
            Department department,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Department> pageInfo = departmentService.selectPage(department,  pageNum, pageSize);
        return Result.success(pageInfo);
    }


    //post: 新增操作
    @PostMapping("/add")
    @AutoLog("新增部門")
    //@RequestBody可以把前端传来的JSON字符串映射为java的对象，或者数组
    public Result add(@RequestBody Department department) {
        departmentService.add(department);
        return Result.success(null);
    }

    //put: 修改操作
    @PutMapping("/update")
    @AutoLog("更新部門")
    public Result update(@RequestBody Department department) {
        departmentService.update(department);
        return Result.success(null);
    }

    //delete: 删除操作
    @DeleteMapping("/deleteById/{id}")
    @AutoLog("刪除部門")
    public Result deleteById(@PathVariable Integer id) {
        departmentService.deleteById(id);
        return Result.success(null);
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    @AutoLog("批量刪除部門")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        departmentService.deleteBatch(ids);
        return Result.success(null);
    }

}
