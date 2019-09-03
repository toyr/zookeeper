package com.example.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description
 * @create 2019-08-29
 */
public class Zookeeper_Constructor_Usage_With_SID_PASSWORD implements Watcher {
    private static CountDownLatch connectionSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new Zookeeper_Constructor_Usage_With_SID_PASSWORD());
        connectionSemaphore.await();
        long sessionId = zooKeeper.getSessionId();
        byte[] sessionPasswd = zooKeeper.getSessionPasswd();
        // 使用错误的sessionId 和 sessionPasswd
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new Zookeeper_Constructor_Usage_With_SID_PASSWORD(),
                1L,
                "test".getBytes());
        // 使用正确的sessionId 和 sessionPasswd
        zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                5000,
                new Zookeeper_Constructor_Usage_With_SID_PASSWORD(),
                sessionId,
                sessionPasswd);
        Thread.sleep(Integer.MAX_VALUE);

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:" + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectionSemaphore.countDown();
        }
    }
}
