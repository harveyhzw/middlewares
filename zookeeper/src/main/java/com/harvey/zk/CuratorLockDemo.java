package com.harvey.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;


public class CuratorLockDemo {

    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString("192.168.6.126:2181,192.168.6.127:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                namespace("curatorLockDemo").build();

        curatorFramework.start();

        final InterProcessMutex lock = new InterProcessMutex(curatorFramework, "/locks");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "->try get the lock");
                try {
                    lock.acquire();
                    System.out.println(Thread.currentThread().getName() + "->success get the lock");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }
        System.in.read();
    }
}
