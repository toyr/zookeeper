package com.example.zookeeper.curatortest;

import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingZooKeeperServer;

/**
 * @author unisk1123
 * @Description
 * @create 2019-09-03
 */
public class TestingCluster_Sample {
    public static void main(String[] args) throws Exception {
        TestingCluster cluster = new TestingCluster(3);
        cluster.start();
        Thread.sleep(2000);

        TestingZooKeeperServer leader = null;
        for (TestingZooKeeperServer zs : cluster.getServers()) {
            System.out.println(zs.getInstanceSpec().getServerId() + "-");
            System.out.println(zs.getQuorumPeer().getServerState() + "-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
            if (zs.getQuorumPeer().getServerState().endsWith("leading")) {
                leader = zs;
            }
        }
        leader.kill();

        System.out.println("-- After leader kill:");
        for (TestingZooKeeperServer zs : cluster.getServers()) {
            System.out.println(zs.getInstanceSpec().getServerId() + "-");
            System.out.println(zs.getQuorumPeer().getServerState() + "-");
            System.out.println(zs.getInstanceSpec().getDataDirectory().getAbsolutePath());
        }
        cluster.stop();
    }
}
