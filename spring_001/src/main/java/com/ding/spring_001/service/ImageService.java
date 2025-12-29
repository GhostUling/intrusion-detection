package com.ding.spring_001.service;

import com.ding.spring_001.entity.Image;
import com.ding.spring_001.entity.Params;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ImageService {
    List<Image> selectAll();

    List<Image> findById(Integer userid) ;
    public void add(Image Image);
    public void update(Image Image);
    public void del(Integer id);
}
