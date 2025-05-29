package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Employee;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.EmployeeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import  java.util.List;
@Service
public class EmployeeService {
    @Resource
    private EmployeeMapper employeeMapper;

    public void add(Employee employee) {
        String username = employee.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if (dbEmployee != null) { //账号已存在 无法注册
            throw new CustomException("500", "账号已存在，请更换账号！");
        }
        if (StrUtil.isBlank(employee.getPassword())){ //密码没填
            employee.setPassword("password"); //默认密码 password
        }
        if (StrUtil.isBlank(employee.getName())){ //名字没设
            employee.setName(employee.getUsername()); //用账号代替
        }
        // 获取当前最大no值+1
        Integer maxNo = employeeMapper.selectMaxNo();
        employee.setNo(maxNo != null ? maxNo + 1 : 1);

        employee.setRole("User");
        employeeMapper.insert(employee);
    }

    public void update(Employee employee) {
        employeeMapper.updateById(employee);
    }

    public List<Employee> selectAll(Employee employee) {
        return employeeMapper.selectAll(employee);
    }

    public Employee selectById(Integer id) {
        return employeeMapper.selectById(id);
    }

    public PageInfo<Employee> selectPage(Employee employee, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employee> list = employeeMapper.selectAll(employee);
        return PageInfo.of(list);
    }


    public void deleteById(Integer id) {
        employeeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Employee login(Account account) {
        String username = account.getUsername();
        Employee dbEmployee = employeeMapper.selectByUsername(username);
        if(dbEmployee == null) {
            throw new CustomException("500", "賬號不存在");
        }
        if(!account.getPassword().equals(dbEmployee.getPassword())){
            throw new CustomException("500", "賬號或密碼錯誤");
        }
        //生成當前用戶對應的token，然後返回到前端
        String token = JwtTokenUtils.genToken(dbEmployee.getId().toString(), dbEmployee.getRole(), dbEmployee.getPassword());
        dbEmployee.setToken(token);
        return dbEmployee;
    }

    public void register(Employee employee) {
        this.add(employee);
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Employee employee = this.selectById(id);
        if (!employee.getPassword().equals(account.getPassword())) { //页面转来的原密码跟数据库密码不匹配的话 报错
            throw new CustomException("500", "原密碼錯誤, 無法修改密碼");
        }
        employee.setPassword(account.getNewPassword()); //设置新密码
        this.update(employee);
    }
}
