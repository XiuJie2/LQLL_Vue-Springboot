package com.example.springboot.mapper;

import com.example.springboot.entity.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    //sql查询有两种形式 第一种是在xml文件里面书写，另一种是用注解的形式直接在当前的页面书写
    @Select("select * from `admin` where id = #{id}")
    Admin selectById(Integer id);

    void insert(Admin admin);

    void updateById(Admin admin);

    @Delete("delete from `admin` where id = #{id}")
    void deleteById(Integer id);

    /**
     * 根据账号查询员工信息
     * @param username 员工账号
     * @return 员工实体
     */
    @Select("select * from `admin` where username = #{username}")
    Admin selectByUsername(String username);

    /**
     * 查询当前最大的员工编号
     * @return 最大的no值，如果没有记录则返回null
     */
    @Select("SELECT MAX(no) FROM `admin`")
    Integer selectMaxNo();
}

