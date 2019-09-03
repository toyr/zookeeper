package com.example.zookeeper.zkclient;

import org.I0Itec.zkclient.ZkClient;

/**
 * @author unisk1123
 * @Description
 * @create 2019-08-30
 */
public class ZkClient_Create_Session_Sample {

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 5000);
        System.out.println("Zookeeper session established");
    }
}
