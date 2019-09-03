package com.example.zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description ZooKeeper API创建节点，使用同步（sync）接口
 * @create 2019-08-29
 */
public class ZooKeeper_Create_API_ASync_Usage implements Watcher {

    private static CountDownLatch connectionSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new ZooKeeper_Create_API_ASync_Usage());
        connectionSemaphore.await();
        zooKeeper.create("/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                new IStringCallback(),
                "I am context");

        zooKeeper.create("/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL,
                new IStringCallback(),
                "I am context");

        zooKeeper.create("/zk-test-ephemeral-",
                "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallback(),
                "I am context");

        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectionSemaphore.countDown();
        }
    }



}

class IStringCallback implements AsyncCallback.StringCallback {

    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("Create path result: [" + rc + "," + path + ","
                + ctx + ", real path name: " + name);
    }


}