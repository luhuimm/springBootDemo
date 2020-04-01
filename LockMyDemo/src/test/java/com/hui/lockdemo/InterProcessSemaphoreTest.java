package com.hui.lockdemo;

import com.hui.lockdemo.zklock.FakeLimitedResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreV2;
import org.apache.curator.framework.recipes.locks.Lease;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class InterProcessSemaphoreTest {
    
    private static final int MAX_LEASE = 10;
    
    private static final String PATH = "/examples/locks";
    
    @Autowired
    private CuratorFramework client;
    
    @Test
    public void SemaphoreTest() throws Exception {
        FakeLimitedResource resource = new FakeLimitedResource();
        InterProcessSemaphoreV2 semaphore = new InterProcessSemaphoreV2(client, PATH, MAX_LEASE);
        Collection<Lease> leases = semaphore.acquire(5);
        System.out.println("get " + leases.size() + " leases");
        Lease lease = semaphore.acquire();
        System.out.println("get another lease");
        resource.use();
    
        Collection<Lease> leases2 = semaphore.acquire(5, 10, TimeUnit.SECONDS);
        System.out.println("Should timeout and acquire return " + leases2);
    
        System.out.println("return one lease");
        semaphore.returnLease(lease);
        System.out.println("return another 5 leases");
        semaphore.returnAll(leases);
    }
    
}
