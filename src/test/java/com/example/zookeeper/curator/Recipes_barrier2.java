package com.example.zookeeper.curator;

import com.example.zookeeper.ConnectConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author unisk1123
 * @Description
 * @create 2019-09-02
 */
public class Recipes_barrier2 {
    static String barrier_path = "/curator_recipes_barrier_path";

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        CuratorFramework client = CuratorFrameworkFactory.builder()
                                .connectString(ConnectConstant.connectString)
                                .sessionTimeoutMs(5000)
                                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                                .build();
                        client.start();
                        DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(client, barrier_path, 5);
                        Thread.sleep(Math.round(Math.random() * 3000));
                        System.out.println(Thread.currentThread().getName() + "号进入barrier");
                        barrier.enter();
                        System.out.println("启动。。。");
                        Thread.sleep(Math.round(Math.random() * 3000));
                        barrier.leave();
                        System.out.println("退出。。。");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
