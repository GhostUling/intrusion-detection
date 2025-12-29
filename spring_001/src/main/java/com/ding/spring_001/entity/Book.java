package com.ding.spring_001.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    //主键id
    private Integer id;
    //图书名称
    private String name;
    //图书价格
    private String price;
    //作者
    private String author;
    //出版社
    private String press;
    private String img;
}
