package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.common.JwtTokenUtils;
import com.example.springboot.entity.Admin;
import com.example.springboot.entity.Account;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import  java.util.List;
@Service
public class AdminService {
    
    @Resource
    private AdminMapper adminMapper;

    public void add(Admin admin) {
        String username = admin.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if (dbAdmin != null) { //账号已存在 无法新增
            throw new CustomException("500", "账号已存在，请更换账号！");
        }
        if (StrUtil.isBlank(admin.getPassword())){ //密码没填
            admin.setPassword("adminpassword"); //默认密码 adminpassword
        }
        if (StrUtil.isBlank(admin.getName())){ //名字没设
            admin.setName(admin.getUsername()); //用账号代替
        }
        // 获取当前最大no值+1
        Integer maxNo = adminMapper.selectMaxNo();
        admin.setNo(maxNo != null ? maxNo + 1 : 1);

        admin.setRole("Admin"); //管理员角色
        adminMapper.insert(admin);
    }

    public void update(Admin admin) {
        adminMapper.updateById(admin);
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    public PageInfo<Admin> selectPage(Admin admin, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }


    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public Admin login(Account account) {
        String username = account.getUsername();
        Admin dbAdmin = adminMapper.selectByUsername(username);
        if(dbAdmin == null) { //没有查询到任何用户 说明没有这个账号
            throw new CustomException("500", "账号不存在");
        }
        String password = account.getPassword();
        if(!dbAdmin.getPassword().equals(password)){
            throw new CustomException("500", "账号或密码错误"); //用户输入的密码跟数据库账号的密码不匹配
        }
        //生成當前用戶對應的token，然後返回到前端
        String token = JwtTokenUtils.genToken(dbAdmin.getId().toString(), dbAdmin.getRole(), dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Integer id = account.getId();
        Admin admin = this.selectById(id);
        if (!admin.getPassword().equals(account.getPassword())) { //页面转来的原密码跟数据库密码不匹配的话 报错
            throw new CustomException("500", "原密码错误，无法修改密码");
        }
        admin.setPassword(account.getNewPassword()); //设置新密码
        this.update(admin);
    }
}
