package com.example.springboot.mapper;

import com.example.springboot.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {
    List<Category> selectAll(Category category);

    //sql查询有两种形式 第一种是在xml文件里面书写，另一种是用注解的形式直接在当前的页面书写
    @Select("select * from `category` where id = #{id}")
    Category selectById(Integer id);

    void insert(Category category);

    void updateById(Category category);

    @Delete("delete from `category` where id = #{id}")
    void deleteById(Integer id);

}

