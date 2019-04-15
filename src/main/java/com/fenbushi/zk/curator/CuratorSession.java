package com.fenbushi.zk.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorSession {
    private static final String client = "10.128.122.108:2181,10.128.122.11:2181,10.128.122.166:2181";
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(client, 10000, 10000, new ExponentialBackoffRetry(1000,3));
        curatorFramework.start();
        System.out.println(curatorFramework);
        byte[] d = curatorFramework.getData().forPath("/liuyang");
        System.out.println(new String(d));
    }
}
