package com.hui.lockdemo.zklock;

import java.util.concurrent.atomic.AtomicBoolean;

public class FakeLimitedResource {
    private final AtomicBoolean inUse = new AtomicBoolean(false);
    public void use() throws Exception {
        //这个例子在使用锁的情况下不会抛出非法并发异常IllegalStateException
        //但是在无锁的情况下，由于sleep了一段时间，所以很容易抛出异常
        if(!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }
        try {
            Thread.sleep((long) (3 * Math.random()));
        } finally {
            inUse.set(false);
        }
    }
}
