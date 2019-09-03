package com.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description Zookeeper API更新节点数据内容，使用同步接口
 * @create 2019-08-30
 */
public class Zookeeper_SetData_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new Zookeeper_SetData_API_Sync_Usage());
        connectedSemaphore.await();
        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        zk.getData(path, true, null);
        Stat stat = zk.setData(path, "456".getBytes(), -1);
        System.out.println(stat.getCzxid() + ", " +
                stat.getMzxid() + ", " +
                stat.getVersion());
        Stat stat2 = zk.setData(path, "456".getBytes(), stat.getVersion());
        System.out.println(stat2.getCzxid() + ", " +
                stat2.getMzxid() + ", " +
                stat2.getVersion());

        try {
            zk.setData(path, "456".getBytes(), stat.getVersion());
        } catch (KeeperException e) {
            System.out.println("Error:" + e.code() + ", " + e.getMessage());
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
