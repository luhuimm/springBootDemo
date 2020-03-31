package com.hui.lockdemo.zklock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

public class ExampleClientThatLocks {
    
    private final InterProcessMutex lock;
    
    private final FakeLimitedResource resource;
    
    private final String clientName;
    
    
    public ExampleClientThatLocks(CuratorFramework framework, String path,
                                  FakeLimitedResource resource, String clientName) {
        this.lock = new InterProcessMutex(framework,path);
        this.resource = resource;
        this.clientName = clientName;
    }
    
    public void doWork(long time, TimeUnit timeUnit) throws Exception {
        if(!lock.acquire(time, timeUnit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock!");
        }
        System.out.println(clientName + " has the lock");
        
        try {
            resource.use();
        } finally {
            System.out.println(clientName + " releasing the lock");
            lock.release();
            //lock.release();
        }
    }
    
}
