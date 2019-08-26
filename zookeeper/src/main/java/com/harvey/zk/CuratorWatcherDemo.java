package com.harvey.zk;

import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

public class CuratorWatcherDemo {
/*
* 三种监听机制
* 1. PathChildCache 针对叶子节点的创建，删除和更新
* 2. NodeCache 针对当前节点
* 3. TreeCache 以上两种都监听
* */
    public static void main( String[] args ) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString("192.168.0.106:2181,192.168.0.101:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

        curatorFramework.start();

        addListenerWithNode(curatorFramework);

        System.in.read();

    }

    //作为注册中心，动态感知
    private static void addListenerWithNode(CuratorFramework curatorFramework) throws Exception {
        //NodeCache nodeCache = new NodeCache(curatorFramework, "/curatorAclWatcherDemo", false);
        PathChildrenCache nodeCache = new PathChildrenCache(curatorFramework, "/watch", true);
        //NodeCacheListener nodeCacheListener = new NodeCacheListener() {
        PathChildrenCacheListener nodeCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                System.out.println(pathChildrenCacheEvent.getType() + "->" +
                        new String(pathChildrenCacheEvent.getData().getData()));

            }
            /*@Override
            public void nodeChanged() throws Exception {
                System.out.println("Receive Node Changed");
                System.out.println(nodeCache.getCurrentData().getPath() + "--"
                        + new String(nodeCache.getCurrentData().getData()));
            }*/
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start(PathChildrenCache.StartMode.NORMAL);
    }



}
