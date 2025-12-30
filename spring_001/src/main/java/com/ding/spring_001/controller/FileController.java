package com.ding.spring_001.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.ding.spring_001.common.Result;
import com.ding.spring_001.service.impl.SharedFolderUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private SharedFolderUploadService sharedFolderUploadService;

    //文件上传存在什么位置
    public static final String filePath = System.getProperty("user.dir") + "/file/";

    @GetMapping("/uploadAll")
    public Result uploadAll(){
        return sharedFolderUploadService.uploadAllImages();
    }

    @PostMapping("/upload")
    public Result upLoad(MultipartFile file) {
        synchronized (FileController.class) {
            //生成一个当前时间戳
            String flag = System.currentTimeMillis() + "";
            //获取上传时文件本身的名字
            String fileName = file.getOriginalFilename();
            try {
                //判断在根目录是否有file文件夹
                if (!FileUtil.isDirectory(filePath))
                    //如果没有，则创建一个file文件夹
                    FileUtil.mkdir(filePath);
                //文件存储形式:时间戳-文件名
                FileUtil.writeBytes(file.getBytes(), filePath + flag + "+" + fileName);
                System.out.println(fileName + "-- 上传成功 ");
                Thread.sleep(1L);
            } catch (Exception e) {
                System.err.println(fileName + "-- 文件 上传失败 ");
            }
            return Result.success(flag);
        }
    }

    /**
     * 获取文件
     */
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        List<String> fileNames = FileUtil.listFileNames(filePath);
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content -Disposition", "attachment; filename=" + URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }
}
