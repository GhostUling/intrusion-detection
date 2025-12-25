package com.ding.spring_001.entity;  // 注意包名要对应

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //用户姓名
    @Column(name = "name")
    private String name;

    //账户
    @Column(name = "username")
    private String username;

    //密码
    @Column(name = "password")
    private String password;

    //邮箱
    @Column(name = "email")
    private String email;

    //简介
    @Column(name = "intro")
    private String intro;

    // 根据你的数据库表，还需要添加以下字段：
    @Column(name = "phone")
    private String phone;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "type")
    private String type;
}