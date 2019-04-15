package com.fenbushi.zk.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class DistributeWatcher implements Watcher {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getType() == Event.EventType.NodeDeleted ){
            countDownLatch.countDown();
        }

    }
}
