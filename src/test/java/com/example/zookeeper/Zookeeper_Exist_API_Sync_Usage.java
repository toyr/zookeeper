package com.example.zookeeper;

import org.apache.zookeeper.*;

import javax.swing.event.HyperlinkEvent;
import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description
 * @create 2019-08-30
 */
public class Zookeeper_Exist_API_Sync_Usage implements Watcher {

    private static CountDownLatch connectionSemaphore = new CountDownLatch(1);

    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new Zookeeper_Exist_API_Sync_Usage());
        connectionSemaphore.await();
        zk.exists(path, true);
        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.setData(path, "123".getBytes(), -1);
        zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.delete(path + "/c1", -1);
        zk.delete(path, -1);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    connectionSemaphore.countDown();
                } else if (Event.EventType.NodeCreated == watchedEvent.getType()) {
                    System.out.println(watchedEvent.getPath() + " created");
                    zk.exists(watchedEvent.getPath(), true);
                } else if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
                    System.out.println(watchedEvent.getPath() + " delete");
                    zk.exists(watchedEvent.getPath(), true);
                } else if (Event.EventType.NodeDataChanged == watchedEvent.getType()) {
                    System.out.println(watchedEvent.getPath() + " DataChanged");
                    zk.exists(watchedEvent.getPath(), true);
                }
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
