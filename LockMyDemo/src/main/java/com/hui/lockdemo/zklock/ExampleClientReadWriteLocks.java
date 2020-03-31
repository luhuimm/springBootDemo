package com.hui.lockdemo.zklock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

public class ExampleClientReadWriteLocks {
    private final InterProcessReadWriteLock readWriteLock;
    private final InterProcessMutex readLock;
    private final InterProcessMutex writeLock;
    private final FakeLimitedResource resource;
    private final String clientName;
    
    public ExampleClientReadWriteLocks(CuratorFramework client, String path, FakeLimitedResource resource, String clientName) {
        this.readWriteLock = new InterProcessReadWriteLock(client, path);
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
        this.resource = resource;
        this.clientName = clientName;
    }
    
    public void doWork(long time, TimeUnit unit) throws Exception {
        if(!writeLock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the writeLock!");
        }
        System.out.println(clientName + " has the writeLock");
        
        if(!readLock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the readLock!");
        }
        System.out.println(clientName + " has the readLock");
        
        try {
            resource.use();
        } finally {
            readLock.release();
            writeLock.release();
        }
    }
    
}
