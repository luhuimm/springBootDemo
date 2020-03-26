package com.hui.timerstudy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ScheduledTask {
    
    /**
     * 自动扫描，启动时间点之后2秒执行一次
     */
    @Scheduled(fixedRate=2000)
    public void getCurrentDate() {
        log.info("Scheduled定时任务执行：" + new Date());
    }
    
    /**
     * 自动扫描，启动时间点之后2秒执行一次
     */
    @Async("scheduledPoolTaskExecutor")
    @Scheduled(fixedRate=2000)
    public void getCurrentDate2() {
        log.info("Scheduled定时任务执行111111：" + new Date());
    }
}
