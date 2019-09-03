package com.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description Zookeeper API 获取节点数据内容，使用同步接口
 * @create 2019-08-29
 */
public class Zookeeper_GetData_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectionSemaphore = new CountDownLatch(1);

    private static ZooKeeper zk = null;

    private static Stat stat = new Stat();

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new Zookeeper_GetData_API_Sync_Usage());
        connectionSemaphore.await();
        zk.create(path, "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData(path, true, stat)));
        System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
        zk.setData(path, "456".getBytes(), -1);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                connectionSemaphore.countDown();
            } else if (Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                try {
                    System.out.println(new String(zk.getData(watchedEvent.getPath(), true, stat)));
                    System.out.println(stat.getCzxid() + ", " + stat.getMzxid() + ", " + stat.getVersion());
                } catch (Exception e) {
                }
            }
        }
    }
}