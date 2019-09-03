package com.example.zookeeper.curator;

import com.example.zookeeper.ConnectConstant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author unisk1123
 * @Description
 * @create 2019-09-02
 */
public class Create_Session_Sample {
    public static void main(String[] args) throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.newClient(ConnectConstant.connectString,
                        ConnectConstant.sessionTimeoutMs,
                        ConnectConstant.connectionTimeoutMs,
                        retryPolicy);
        client.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
}
