package com.example.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description
 * @create 2019-08-29
 */
public class Zookeeper_Constructor_Usage_Simple implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183",
                    5000,
                    new Zookeeper_Constructor_Usage_Simple());
            System.out.println(zooKeeper.getState());
            connectedSemaphore.await();
            System.out.println("Zookeeper session established");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event: " + watchedEvent);
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            connectedSemaphore.countDown();
        }
    }
}
