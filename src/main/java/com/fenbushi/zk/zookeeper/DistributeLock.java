package com.fenbushi.zk.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;

public class DistributeLock {
    private static final String lockPath ="lock";
    private static final String url = "10.128.122.108:2181,10.128.122.11:2181,10.128.122.166:2181";
    private CountDownLatch waitConnect = new CountDownLatch(1);
    public boolean getLock() throws KeeperException, InterruptedException, IOException {
        ZooKeeper zooKeeper = new ZooKeeper(url, 100000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState().equals(Event.KeeperState.SyncConnected)){
                    try {
                        waitConnect.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        waitConnect.countDown();
        String path = zooKeeper.create("/liuyang/lock/","123".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        List<String> childList = zooKeeper.getChildren("/liuyang/lock/",true);
        TreeSet<String> childSet = new TreeSet<String>(childList);
        if(path.equals(childSet.first())){
            return true;
        }
        else{
            SortedSet<String> set = childSet.headSet(path);
            String previewNode = set.last();
            Stat stat = zooKeeper.exists("/liuyang/lock/"+previewNode,new DistributeWatcher());

        }
        return false;
    }
}
