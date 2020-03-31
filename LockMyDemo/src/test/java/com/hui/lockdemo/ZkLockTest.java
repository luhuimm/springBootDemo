package com.hui.lockdemo;


import com.hui.lockdemo.zklock.ExampleClientThatLocks;
import com.hui.lockdemo.zklock.FakeLimitedResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class ZkLockTest {
    
    private static final int QTY = 5;
    private static final int REPETITIONS = 10;
    private static final String PATH = "/examples/locks";
    @Autowired
    private CuratorFramework client;
    
    @Test
    public void lockTest() throws InterruptedException {
        final FakeLimitedResource resource = new FakeLimitedResource();
        ExecutorService executor = Executors.newFixedThreadPool(300);
        for(int i=0; i < QTY; i++) {
            int index = i;
            Runnable task = () ->{
                final ExampleClientThatLocks example =
                        new ExampleClientThatLocks(client, PATH, resource, "Client " + index);
                for(int j=0; j < REPETITIONS; j++) {
                    try {
                        example.doWork(10, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            executor.submit(task);
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }
}
