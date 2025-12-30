package com.ding.spring_001.service.impl;

import cn.hutool.core.io.FileUtil;
import com.ding.spring_001.common.Result;
import com.ding.spring_001.controller.FileController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SharedFolderUploadService {


    public static final String filePath = System.getProperty("user.dir") + "/file/";

    //从yml中获取共享文件夹路径
    @Value("${shared.folder.path://172.25.192.1/share/}")
    private String sharedFolderPath;


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

    public Result uploadAllImages() {
        try {
            Path sharedPath = Paths.get(sharedFolderPath);
            if (!Files.exists(sharedPath) || !Files.isDirectory(sharedPath)) {
                return Result.error("共享文件夹不存在");
            }

            // 获取所有图片文件
            List<Path> imageFiles = new ArrayList<>();
            Files.walkFileTree(sharedPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    //调用方法判断文件是否是图片
                    if (isImageFile(file)) {
                        imageFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });


            if (imageFiles.isEmpty()) {
                return Result.error("没有图片");
            }

            // 按文件大小排序（小文件先上传）
            imageFiles.sort(Comparator.comparingLong(path -> {
                try {
                    return Files.size(path);
                } catch (IOException e) {
                    return Long.MAX_VALUE;
                }
            }));

            // 批量上传
            return batchUploadImages(imageFiles);

        } catch (Exception e) {
            return Result.error("扫描失败: " + e.getMessage());
        }
    }

    /**
     * 检查是否是图片文件
     */
    private boolean isImageFile(Path path) {
        if (path == null) return false;
        String filename = path.getFileName().toString().toLowerCase();
        return filename.endsWith(".jpg") ||
                filename.endsWith(".jpeg") ||
                filename.endsWith(".png") ||
                filename.endsWith(".gif") ||
                filename.endsWith(".bmp");
    }


    /**
     * 批量上传图片
     */
    private Result batchUploadImages(List<Path> imageFiles) {
        if (imageFiles.isEmpty()) {
            return Result.error("文件列表为空");
        }

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<String> successFiles = Collections.synchronizedList(new ArrayList<>());
        List<String> failFiles = Collections.synchronizedList(new ArrayList<>());

        // 一次全部上传（并行处理）
        imageFiles.parallelStream().forEach(file -> {
            Result result = uploadSingleFile(file);
        });

        return Result.success();
    }

    /**
     * 上传单个文件
     */
    private Result uploadSingleFile(Path filePath) {
        try {
            String filename = filePath.getFileName().toString();

            // 检查是否已上传过

            // 将File转换为MultipartFile
            MultipartFile multipartFile = convertToMultipartFile(filePath);

            if (multipartFile == null) {
                return Result.error("文件转换失败");
            }

            // 调用FileController的上传接口
            Result result = upLoad(multipartFile);

            if (result.getCode() .equals("200") ) {

                return Result.success("上传成功");
            } else {
                return Result.error("上传失败");
            }

        } catch (Exception e) {
            return Result.error("上传失败");
        }
    }

    /**
     * 将Path转换为MultipartFile
     */
    private MultipartFile convertToMultipartFile(Path filePath) throws IOException {
        String filename = filePath.getFileName().toString();
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        try (FileInputStream input = new FileInputStream(filePath.toFile())) {
            return new MockMultipartFile(
                    "file",           // 参数名，对应@RequestParam("file")
                    filename,         // 原始文件名
                    contentType,      // 内容类型
                    input             // 文件内容
            );
        }
    }

}
