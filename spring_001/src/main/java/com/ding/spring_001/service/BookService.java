package com.ding.spring_001.service;

import com.ding.spring_001.entity.Book;
import com.ding.spring_001.entity.Params;
import com.ding.spring_001.mapper.BookMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookService {
    List<Book> selectAll();

    PageInfo<Book> findBySearch(Params params) ;
    public void add(Book book);
    public void update(Book book);
    public void del(Integer id);
}
