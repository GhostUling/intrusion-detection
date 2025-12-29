package com.ding.spring_001.controller;

import com.ding.spring_001.common.Result;
import com.ding.spring_001.entity.Book;
import com.ding.spring_001.entity.Params;
import com.ding.spring_001.service.BookService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;
    //查询图书信息
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Book> list=bookService.selectAll();
        return Result.success(list);
    }
   /* @GetMapping("/search")
    public Result findBySearch(Params params) {
        PageInfo<Book> bookPageInfo = bookService.findBySearch(params);
        return Result.success(bookPageInfo);
    }*/
   @GetMapping("/search")
   public Result findBySearch(Params params) {
       PageInfo<Book> bookPageInfo = bookService.findBySearch(params);
       return Result.success(bookPageInfo);
   }
    @PostMapping("/add")
    public Result save(@RequestBody Book book) {
        if(book.getId() == null){
            bookService.add(book);
        }
        else {
            bookService.update(book);
        }
        return Result.success();
    }

    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id) {
        bookService.del(id);
        return Result.success();
    }
}
