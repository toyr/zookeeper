package com.example.zookeeper.curator;

import com.example.zookeeper.ConnectConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * @author unisk1123
 * @Description
 * @create 2019-09-02
 */
public class Set_Data_Sample {
    static String path = "/zk-book";
    static CuratorFramework client = CuratorFrameworkFactory.builder()
            .connectString(ConnectConstant.connectString)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();

    public static void main(String[] args) throws Exception {
        client.start();
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(path, "init".getBytes());
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println("success set node fro: " + path + ", new version: "
                + client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());

        try {
            client.setData().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            System.out.println("Fail set node due to " + e.getMessage());
        }
    }
}
