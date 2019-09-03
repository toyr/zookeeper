package com.example.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description
 * @create 2019-08-29
 */
public class ZooKeeper_GetChildren_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectionSemaphore = new CountDownLatch(1);

    private static ZooKeeper zk = null;

    public static void main(String[] args) throws Exception {
        String path = "/zk-book";
        zk = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new ZooKeeper_GetChildren_API_ASync_Usage());
        connectionSemaphore.await();
        zk.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        zk.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        zk.getChildren(path, true, new IChildren2Callback(), null);

        zk.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                connectionSemaphore.countDown();
            } else if (Event.EventType.NodeChildrenChanged == watchedEvent.getType()) {
                try {
                    System.out.println("ReGet Child:" + zk.getChildren(watchedEvent.getPath(), true));
                } catch (Exception e) {

                }
            }
        }
    }
}

class IChildren2Callback implements AsyncCallback.Children2Callback {
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        System.out.println("Get Children zonde result: [response code: " + rc +
                ", param path: " + path + " ,ctx: " + ctx + ", children list: " + children + ", stat: " + stat);
    }
}
