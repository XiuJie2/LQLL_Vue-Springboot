package com.example.springboot.mapper;

import com.example.springboot.entity.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DepartmentMapper {
    List<Department> selectAll(Department department);

    //sql查询有两种形式 第一种是在xml文件里面书写，另一种是用注解的形式直接在当前的页面书写
    @Select("select * from `department` where id = #{id}")
    Department selectById(Integer id);

    void insert(Department department);

    void updateById(Department department);

    @Delete("delete from `department` where id = #{id}")
    void deleteById(Integer id);

}

