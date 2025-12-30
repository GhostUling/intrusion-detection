package com.ding.spring_001.config;

import com.ding.spring_001.common.Result;
import com.ding.spring_001.service.impl.SharedFolderUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Spring启动时加载配置类
 * 启用定时任务调度器
 * 根据配置的cron表达式注册定时任务
 * 到达指定时间时自动执行对应定时任务方法
 * 检查是否启用自动上传功能，如果启用则执行上传逻辑
 */
@Slf4j            //自动生成日志记录器
@Configuration    //声明当前类是配置类，项目启动时自动加载
@EnableScheduling //启用Spring的定时任务功能
public class AutoUploadConfig {

    @Autowired
    private SharedFolderUploadService uploadService;

    @Value("${auto.upload.enabled:true}")
    private boolean autoUploadEnabled;

    @Value("${auto.upload.cron:0 0/5 * * * ?}") // 每5分钟执行一次
    private String autoUploadCron;

    /**
     * 定时自动上传任务
     */
    @Scheduled(cron = "${auto.upload.cron}") //声明方法为定时任务
    public void autoUploadImages() {
        if (!autoUploadEnabled) {
            log.debug("自动上传任务已禁用");
            return;
        }

        log.info("开始执行自动上传任务");
        try {
            Result result = uploadService.uploadAllImages(null);
            log.info("自动上传完成: {}", result.getMsg());
        } catch (Exception e) {
            log.error("自动上传任务执行失败", e);
        }
    }
}

