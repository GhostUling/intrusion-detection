package com.ding.spring_001.mapper;

import com.ding.spring_001.entity.Image;
import com.ding.spring_001.entity.Params;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImageMapper{
    List<Image> selectAll();
    List<Image> findBySearch(@Param("params") Params params);
}
