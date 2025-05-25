package com.example.springboot.controller;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Category;
import com.example.springboot.service.CategoryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//request.js(axios请求，创建统一的前端请求实例) -> Result.java(统一响应包装类) -> Category.java(实体类-数据模型) -> Controller.java(http入口) -> Service.java(调用接口方法) -> mapper.java(数据访问接口) -> Mapper.xml(SQL映射文件)

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping("/selectAll") //get: 查询全部的数据库中的内容操作
    public Result selectAll(Category category) {
        List<Category> list = categoryService.selectAll(category);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}") //get: 使用动态的参数查询特定的数据内容
    public Result selectByfID(@PathVariable Integer id) {
        Category category = categoryService.selectById(id);
        return Result.success(category);
    }

    @GetMapping("/selectOne") //get: 使用参数查询特定的数据内容
    public Result selectOne(@RequestParam Integer id) {
        Category category = categoryService.selectById(id);
        return Result.success(category);
    }

    @GetMapping("/selectPage") //get: 可使用页面参数和页面大小做个性化查询
    public Result selectPage(
            Category category,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Category> pageInfo = categoryService.selectPage(category,  pageNum, pageSize);
        return Result.success(pageInfo);
    }


    //post: 新增操作
    @PostMapping("/add")
    @AutoLog("新增產品類別")
    //@RequestBody可以把前端传来的JSON字符串映射为java的对象，或者数组
    public Result add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success(null);
    }

    //put: 修改操作
    @PutMapping("/update")
    @AutoLog("更新產品類別")
    public Result update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success(null);
    }

    //delete: 删除操作
    @DeleteMapping("/deleteById/{id}")
    @AutoLog("刪除產品類別")
    public Result deleteById(@PathVariable Integer id) {
        categoryService.deleteById(id);
        return Result.success(null);
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    @AutoLog("批量刪除產品類別")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        categoryService.deleteBatch(ids);
        return Result.success(null);
    }

}
