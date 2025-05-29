package com.example.springboot.controller;

import com.example.springboot.common.AutoLog;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.service.AdminService;
import com.example.springboot.service.EmployeeService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController {
    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

    @Resource
    private EmployeeService employeeService;

    @Resource
    private AdminService adminService;


    @PostMapping("/login") //依角色做账号登录
    @AutoLog("用戶登入")
    public Result login(@RequestBody Account account) {
        Account result = null;
        if("Admin".equals(account.getRole())){
            result = adminService.login(account);
        } else if ("User".equals(account.getRole())){
            result = employeeService.login(account);
        } else {
            throw new CustomException("500", "非法输入");
        }
        return Result.success(result);
    }

    @GetMapping("/logout")
    @AutoLog("退出登錄")
    public Result exportInfo() {
        return Result.success(null);
    }

    @PostMapping("/register") //员工注册
    @AutoLog("用戶註冊")
    public Result register(@RequestBody Employee employee) {
        employeeService.register(employee);
        Account account = employee;
        return Result.success(account);
    }

    @PutMapping("/updatePassword")
    @AutoLog("修改密碼")
    public Result updatePassword(@RequestBody Account account) {
        Account result = null;
        if("Admin".equals(account.getRole())){
            adminService.updatePassword(account);
        } else if ("User".equals(account.getRole())){
            employeeService.updatePassword(account);
        } else {
            throw new CustomException("500", "非法输入");
        }
        return Result.success("成功");
    }

//    @GetMapping("/hello")
//    public Result hello() {
//        return Result.success("Hello");
//    }
//
//    //自定义抛出错误 code: 400
//    @GetMapping("/count")
//    public Result count() {
//        throw new CustomException("400", "禁止请求");
//    }
//
//    //系统报错 code: 500
//    @GetMapping("/counts")
//    public Result counts() {
//        int a = 1/0;
//        return Result.success(10);
//    }
//
//    @GetMapping("/weather")
//    public Result weather(){
//        return Result.success("今天天气很好");
//    }
//
//    @GetMapping("/map")
//    public Result map(){
//        Map<String,String> map = new HashMap<>();
//        map.put("name","仕傑");
//        map.put("age","18");
//        return Result.success(map);
//    }
}
