package com.example.zookeeper;

/**
 * @author unisk1123
 * @Description
 * @create 2019-09-02
 */
public interface ConnectConstant {

    String connectString = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    Integer sessionTimeoutMs = 5000;
    Integer connectionTimeoutMs = 3000;
}
