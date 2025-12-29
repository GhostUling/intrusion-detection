package com.ding.spring_001.service;

import com.ding.spring_001.entity.Image;
import com.ding.spring_001.entity.Params;
import com.ding.spring_001.mapper.ImageMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageService {
    List<Image> selectAll();

    PageInfo<Image> findBySearch(Params params) ;
    public void add(Image Image);
    public void update(Image Image);
    public void del(Integer id);
}
