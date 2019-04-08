package com.fenbushi.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author YCKJ1423
 * @Date 2019/4/6 15:30
 * Version 1.0
 **/
public class CreateSession implements Watcher{
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String CONNECTEDS ="10.128.122.108:2181,10.128.122.11:2181," +
            "10.128.122.166:2181";
    private static ZooKeeper zooKeeper = null;
    private static Stat stat = new Stat();
    public static void main(String[] args) throws IOException {
         zooKeeper = new ZooKeeper(CONNECTEDS, 5000, new CreateSession());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("zookeeper+status:"+zooKeeper.getState());
        try {
            String node ="/zhangsan";
            String value ="";
            String createResult = zooKeeper.create(node,"111".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            value = new String(zooKeeper.getData(node,new CreateSession(),stat));
            //修改数据
            zooKeeper.setData(createResult,"22".getBytes(),-1);
            TimeUnit.SECONDS.sleep(2);
//            zooKeeper.setData(createResult,"33".getBytes(),-1);
//            TimeUnit.SECONDS.sleep(2);
            //创建子节点
            String node1= "/test4";
            if(null != zooKeeper.exists(node1,true)){
                if(null == zooKeeper.exists(node1+"/child1",true)){
                    zooKeeper.create(node1+"/child1","22".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    TimeUnit.SECONDS.sleep(1);
                }
            }
            else{
                zooKeeper.create(node1,"23234".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                if(null == zooKeeper.exists(node1+"/child1",true)){
                    zooKeeper.create(node1+"/child1","22".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    TimeUnit.SECONDS.sleep(1);
                }
            }

            zooKeeper.setData(node1+"/child1","232".getBytes(),-1);
            TimeUnit.SECONDS.sleep(2);

            //删除子节点
            zooKeeper.delete(node1+"/child1",-1);
            zooKeeper.delete(node1,-1);

            TimeUnit.SECONDS.sleep(1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
            if(watchedEvent.getType()==Event.EventType.None&&watchedEvent.getPath()==null){
                countDownLatch.countDown();
            }
            else if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                try {
                    System.out.println("node data change:"+watchedEvent.getPath()+" value:"+new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println("nodeChild change:"+watchedEvent.getPath()+" value:"+new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if(watchedEvent.getType() == Event.EventType.NodeCreated){
                try {
                    System.out.println("node create :"+watchedEvent.getPath()+" value:"+new String(zooKeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(watchedEvent.getType() == Event.EventType.NodeDeleted){
                try {
                    System.out.println("node delete :"+watchedEvent.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            System.out.println(watchedEvent.getPath());

        }

    }
}
