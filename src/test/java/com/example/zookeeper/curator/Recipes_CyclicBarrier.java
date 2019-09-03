package com.example.zookeeper.curator;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author unisk1123
 * @Description 使用CyclicBarrier模拟一个赛跑比赛
 * @create 2019-09-02
 */
public class Recipes_CyclicBarrier {
    public static CyclicBarrier barrier = new CyclicBarrier(3);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(new Thread(new Runner("1号选手")));
        executor.submit(new Thread(new Runner("2号选手")));
        executor.submit(new Thread(new Runner("3号选手")));
        executor.shutdown();
    }
}

class Runner implements Runnable {
    private String name;

    public Runner(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + " 准备好了");
        try {
            Recipes_CyclicBarrier.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
