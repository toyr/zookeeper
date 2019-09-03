package com.example.zookeeper.curatortest;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;

import java.io.File;

/**
 * @author unisk1123
 * @Description
 * @create 2019-09-03
 */
public class TestingServer_Sample {

    static String path = "/";

    public static void main(String[] args) throws Exception {
        TestingServer server = new TestingServer(2184, new File("/home/admin/zk-book-data"));

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(server.getConnectString())
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        client.create().withMode(CreateMode.PERSISTENT).forPath(path + "zk-book", "init".getBytes());
        System.out.println(client.getChildren().forPath(path));
        server.close();
    }
}
