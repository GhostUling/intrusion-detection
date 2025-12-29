package com.ding.spring_001.controller;

import com.ding.spring_001.common.Result;
import com.ding.spring_001.entity.Image;
import com.ding.spring_001.entity.Params;
import com.ding.spring_001.service.ImageService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Image")
public class ImageController {
    @Autowired
    ImageService ImageService;
    //查询图书信息
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Image> list=ImageService.selectAll();
        return Result.success(list);
    }
   /* @GetMapping("/search")
    public Result findBySearch(Params params) {
        PageInfo<Image> ImagePageInfo = ImageService.findBySearch(params);
        return Result.success(ImagePageInfo);
    }*/

    @GetMapping("/userid")
    public Result findById(Integer userid) {
        // 调用 ImageService 的 findBySearch 方法获取分页信息
        List<Image> images = ImageService.findById(userid);
        // 将分页信息封装到 Result 中并返回
        return Result.success(images);
    }

    @PostMapping("/add")
    public Result save(@RequestBody Image Image) {
        if(Image.getId() == null){
            ImageService.add(Image);
        }
        else {
            ImageService.update(Image);
        }
        return Result.success();
    }

    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Integer id) {
        ImageService.del(id);
        return Result.success();
    }
}
