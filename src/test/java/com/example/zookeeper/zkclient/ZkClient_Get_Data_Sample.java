package com.example.zookeeper.zkclient;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * @author unisk1123
 * @Description
 * @create 2019-08-30
 */
public class ZkClient_Get_Data_Sample {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 5000);
        String path = "/zk-book";
        zkClient.createEphemeral(path, "123");
        zkClient.subscribeDataChanges(path, new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("Node " + s +"changed, new data: " + o);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println("Node " + s + " deleted");
            }
        });
        System.out.println((String) zkClient.readData(path));
        zkClient.writeData(path, "456");
        Thread.sleep(1000);
        zkClient.delete(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
