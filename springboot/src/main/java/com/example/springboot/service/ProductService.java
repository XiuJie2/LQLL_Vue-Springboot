package com.example.springboot.service;

import cn.hutool.core.util.StrUtil;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Product;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import  java.util.List;
@Service
public class ProductService {
    @Resource
    private ProductMapper productMapper;

    @Transactional
    public void add(Product product) {
        String name = product.getName();
        Product dbProduct = productMapper.selectByName(name);
        if (dbProduct != null) { //账号已存在 无法注册
            throw new CustomException("500", "產品名稱已存在，请更換名稱！");
        }
//        if (StrUtil.isBlank(product.getName())){ //密码没填
//            product.setName("product"); //默认密码 password
//        }
        // 获取当前最大no值+1
        Integer maxNo = productMapper.selectMaxNo();
        product.setNo(maxNo != null ? maxNo + 1 : 1);
        productMapper.insert(product);
    }

    public void update(Product product) {
        productMapper.updateById(product);
    }

    public List<Product> selectAll(Product product) {
        return productMapper.selectAll(product);
    }

    public Product selectById(Integer id) {
        return productMapper.selectById(id);
    }

    public PageInfo<Product> selectPage(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectAll(product);
        return PageInfo.of(list);
    }


    public void deleteById(Integer id) {
        productMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

}
