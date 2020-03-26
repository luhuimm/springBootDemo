package com.hui.timerstudy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class TimeController {
    
    @GetMapping("/time")
    public String doTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("Timer定时任务启动：" + new Date());
            }
        },1000, 1000); //延迟1秒启动，每1秒执行一次
        return "timer";
    }
    
    @GetMapping("/executor")
    public String ScheduledExecutorService() {
        ScheduledExecutorService service =
                Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(()->{
            log.info("ScheduledExecutorService定时任务执行：" + new Date());
        },1,1, TimeUnit.SECONDS);
        log.info("ScheduledExecutorService定时任务启动：" + new Date());
        return "ScheduledExecutorService!";
    }
    
    @Autowired
    TaskScheduler taskScheduler;
    
    @GetMapping("/poolTask")
    public String threadPoolTaskScheduler() {
        
        taskScheduler.schedule(new Runnable() {
            
            @Override
            public void run() {
                log.info("ThreadPoolTaskScheduler定时任务：" + new Date());
            }
        }, new CronTrigger("0/3 * * * * ?"));//每3秒执行一次
        return "ThreadPoolTaskScheduler!";
    }
    
}

