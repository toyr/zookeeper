package com.example.zookeeper.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author unisk1123
 * @Description 时间戳并发问题
 * @create 2019-09-02
 */
public class Recipes_Nolock {

    public static void main(String[] args) {
        final CountDownLatch down = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        down.await();
                    } catch (InterruptedException e) {

                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                    String orderNo = sdf.format(new Date());
                    System.err.println("生成的订单号是：" + orderNo);
                }
            }).start();
        }
        down.countDown();
    }
}
