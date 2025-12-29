package com.ding.spring_001.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    //主键id
    private Integer id;
    //用户id
    private Integer userid;
    //图片数据或路径
    private String img;
}