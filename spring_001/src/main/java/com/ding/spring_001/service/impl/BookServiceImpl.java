package com.ding.spring_001.service.impl;

import com.ding.spring_001.entity.Book;
import com.ding.spring_001.entity.Params;
import com.ding.spring_001.mapper.BookMapper;
import com.ding.spring_001.service.BookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;
    @Override
    public List<Book> selectAll() {
        return bookMapper.selectAll();
    }
/*
    @Override
    public PageInfo<Book> findBySearch(Params params) {
        //return bookMapper.findBySearch(params);
        return PageInfo.of(params);
    }*/
    @Override
    public PageInfo<Book> findBySearch(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        List<Book> books = bookMapper.findBySearch(params);
        return new PageInfo<>(books);
    }

    public void add(Book book) {
    }

    public void update(Book book) {
    }

    public void del(Integer id) {
    }
}
