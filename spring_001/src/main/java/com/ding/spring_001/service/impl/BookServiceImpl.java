package com.ding.spring_001.service.impl;

import com.ding.spring_001.entity.Image;
import com.ding.spring_001.entity.Params;
import com.ding.spring_001.mapper.ImageMapper;
import com.ding.spring_001.service.ImageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageMapper ImageMapper;
    @Override
    public List<Image> selectAll() {
        return ImageMapper.selectAll();
    }
/*
    @Override
    public PageInfo<Image> findBySearch(Params params) {
        //return ImageMapper.findBySearch(params);
        return PageInfo.of(params);
    }*/
    @Override
    public PageInfo<Image> findBySearch(Params params) {
        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        List<Image> Images = ImageMapper.findBySearch(params);
        return new PageInfo<>(Images);
    }

    public void add(Image Image) {
    }

    public void update(Image Image) {
    }

    public void del(Integer id) {
    }
}
