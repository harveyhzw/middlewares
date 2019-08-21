package com.harvey.zk;

import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

public class CuratorAclDemo {
    public static void main( String[] args ) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString("192.168.0.106:2181,192.168.0.107:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).
                namespace("curatorAclDemo").build();

        curatorFramework.start();

        List<ACL> acls = new ArrayList<>();

        ACL acl = new ACL(ZooDefs.Perms.READ,new Id("digest", DigestAuthenticationProvider.generateDigest("user:password")));

        acls.add(acl);

        curatorFramework.create().
                creatingParentsIfNeeded().
                withMode(CreateMode.PERSISTENT).
                withACL(acls,false).forPath("/auth","sc".getBytes());

        AuthInfo authInfo = new AuthInfo("digest","user:password".getBytes());
        List<AuthInfo> authInfos = new ArrayList<>();
        authInfos.add(authInfo);

        CuratorFramework curatorFramework2 = CuratorFrameworkFactory.builder().
                connectString("192.168.0.106:2181,192.168.0.107:2181").sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000,3)).
                authorization(authInfos).//访问授权节点
                namespace("curatorAclDemo").build();

        curatorFramework2.setACL().withACL(acls).forPath("/auth");
    }
}
