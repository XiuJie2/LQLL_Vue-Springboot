package com.example.springboot.service;

import com.example.springboot.entity.Category;
import com.example.springboot.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    public void add(Category category) {
        categoryMapper.insert(category);
    }

    public void update(Category category) {
        categoryMapper.updateById(category);
    }

    public List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }

    public PageInfo<Category> selectPage(Category category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }


    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
}
