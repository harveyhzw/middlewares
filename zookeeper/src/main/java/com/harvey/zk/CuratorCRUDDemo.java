package com.harvey.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CuratorCRUDDemo {
    public static void main( String[] args ) throws Exception {
        /*
        * 重试策略：Curator内部实现的几种重试策略:
        * ExponentialBackoffRetry:重试指定的次数, 且每一次重试之 间停顿的时间逐渐增加.
        * RetryNTimes:指定最大重试次数的重试策略
        * RetryOneTime:仅重试一次
        * RetryUntilElapsed:一直重试直到达到规定的时间
        * */
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString("192.168.0.106:2181,192.168.0.107:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).
                namespace("curatorDemo").build();
        curatorFramework.start();

        //createData(curatorFramework);
        //setData(curatorFramework);
        deleteData(curatorFramework);
        //CRUD
        /*curatorFramework.create();
        curatorFramework.setData();
        curatorFramework.delete();
        curatorFramework.getData();*/
    }

    private static void createData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).
                forPath("/data/program","test".getBytes());
    }

    private static void setData(CuratorFramework curatorFramework) throws Exception {
        curatorFramework.setData().forPath("/data/program","up".getBytes());
    }

    private static void deleteData(CuratorFramework curatorFramework) throws Exception {

        Stat stat = new Stat();
        String value = new String(curatorFramework.getData().storingStatIn(stat).forPath("/data/program"));

        curatorFramework.delete().withVersion(stat.getVersion()).forPath("/data/program");
    }

}
