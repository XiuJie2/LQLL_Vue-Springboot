package com.example.springboot.service;

import com.example.springboot.entity.Log;
import com.example.springboot.entity.Product;
import com.example.springboot.exception.CustomException;
import com.example.springboot.mapper.LogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogService {
    @Resource
    private LogMapper logMapper;

    @Transactional
    public void add(Log log) {
        logMapper.insert(log);
    }
    public List<Log> selectAll(Log log) {
        return logMapper.selectAll(log);
    }

    public Log selectById(Integer id) {
        return logMapper.selectById(id);
    }

    public PageInfo<Log> selectPage(Log log, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Log> list = logMapper.selectAll(log);
        return PageInfo.of(list);
    }


    public void deleteById(Integer id) {
        logMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

}
