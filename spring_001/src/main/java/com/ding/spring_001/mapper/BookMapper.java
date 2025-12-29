package com.ding.spring_001.mapper;

import com.ding.spring_001.entity.Book;
import com.ding.spring_001.entity.Params;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookMapper{
    List<Book> selectAll();
    List<Book> findBySearch(@Param("params") Params params);
}
