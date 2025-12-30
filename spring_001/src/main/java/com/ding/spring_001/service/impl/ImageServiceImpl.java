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
    public List<Image> findById(Integer userid) {
//        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        List<Image> Images = ImageMapper.findById(userid);
        return Images;
//        return new PageInfo<>(Images);
    }

    public void add(Image Image) {
        ImageMapper.insert(Image);
    }

    public void update(Image Image) {
        ImageMapper.update(Image);
    }

    public void del(Integer id) {
        ImageMapper.deleteById(id);
    }
}
