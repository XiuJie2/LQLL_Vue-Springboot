package com.example.springboot.controller;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Product;
import com.example.springboot.service.ProductService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
//request.js(axios请求，创建统一的前端请求实例) -> Result.java(统一响应包装类) -> Product.java(实体类-数据模型) -> Controller.java(http入口) -> Service.java(调用接口方法) -> mapper.java(数据访问接口) -> Mapper.xml(SQL映射文件)

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/selectAll") //get: 查询全部的数据库中的内容操作
    public Result selectAll(Product product) {
        List<Product> list = productService.selectAll(product);
        return Result.success(list);
    }
    @GetMapping("/selectById/{id}") //get: 使用动态的参数查询特定的数据内容
    public Result selectByfID(@PathVariable Integer id) {
        Product product = productService.selectById(id);
        return Result.success(product);
    }

    @GetMapping("/selectOne") //get: 使用参数查询特定的数据内容
    public Result selectOne(@RequestParam Integer id) {
        Product product = productService.selectById(id);
        return Result.success(product);
    }

    @GetMapping("/selectPage") //get: 可使用页面参数和页面大小做个性化查询
    public Result selectPage(
            Product product,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Product> pageInfo = productService.selectPage(product,  pageNum, pageSize);
        return Result.success(pageInfo);
    }


    //post: 新增操作
    @PostMapping("/add")
    @AutoLog("新增產品")
    //@RequestBody可以把前端传来的JSON字符串映射为java的对象，或者数组
    public Result add(@RequestBody Product product) {
        productService.add(product);
        return Result.success(null);
    }

    //put: 修改操作
    @PutMapping("/update")
    @AutoLog("更新產品")
    public Result update(@RequestBody Product product) {
        productService.update(product);
        return Result.success(null);
    }

    //delete: 删除操作
    @DeleteMapping("/deleteById/{id}")
    @AutoLog("刪除產品")
    public Result deleteById(@PathVariable Integer id) {
        productService.deleteById(id);
        return Result.success(null);
    }

    //批量删除数据
    @DeleteMapping("/deleteBatch")
    @AutoLog("批量刪除產品")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        productService.deleteBatch(ids);
        return Result.success(null);
    }

    @GetMapping("/export/info")
    @AutoLog("導出產品文件")
    public Result exportInfo() {
        return Result.success(null);
    }
    //导出数据
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //1.拿到所有的员工数据
        List<Product> productList = productService.selectAll(null);
        //2.构建 ExcelWriter
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //3.设置中文表头 以及输出流的头信息
        writer.addHeaderAlias("name", "名稱");
        writer.addHeaderAlias("categoryName", "類別");
        writer.addHeaderAlias("image", "賬號");
        writer.addHeaderAlias("price", "價格");
        writer.addHeaderAlias("description", "介紹");
        writer.setOnlyAlias(true); //只导出设定别名的字段
        //4.写出数据到writer
        writer.write(productList, true);
        //5.设置输出文件的名称
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("產品信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        //6. 写出到输出流 并关闭writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
    }

    //导入
    @PostMapping("/import")
    @AutoLog("導入產品文件")
    public Result importData(MultipartFile file,
                             @RequestParam(required = false) String username,
                             @RequestParam(required = false) String name) throws Exception{
        //1.拿到输入流 构建reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader =  ExcelUtil.getReader(inputStream);
        //2. 读取Excel里面的数据
        reader.addHeaderAlias("名稱","name" );
        reader.addHeaderAlias("類別", "categoryId");
        reader.addHeaderAlias("賬號","image");
        reader.addHeaderAlias("價格","price");
        reader.addHeaderAlias("介紹","description");
        List<Product> productList = reader.readAll(Product.class);
        //写入List数据到数据库
        for (Product product : productList) {
            productService.add(product);
        }
        Account account = new Account();
        account.setUsername(username);
        account.setName(name);
        return Result.success(account);
    }


}
