package com.github.xcfyl.drpc.core.registry.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Zookeeper客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 21:39
 */
public class ZookeeperClient {
    private CuratorFramework curator;
    private final Integer baseSleepTimes;
    private final Integer maxRetries;
    private final String zkAddress;

    public ZookeeperClient(String zkAddress) {
        this(zkAddress, 1000, 3);
    }

    public ZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetries) {
        this.zkAddress = zkAddress;
        this.baseSleepTimes = baseSleepTimes;
        this.maxRetries = maxRetries;
    }

    public void start() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimes, maxRetries);
        curator = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
        curator.start();
    }

    public void updateNodeData(String address, String data) throws Exception {
        curator.setData().forPath(address, data.getBytes());
    }

    public String getNodeData(String address) throws Exception {
        byte[] bytes = curator.getData().forPath(address);
        return new String(bytes);
    }

    public List<String> getChildrenPaths(String path) throws Exception {
        return curator.getChildren().forPath(path);
    }

    public void createPersistentData(String address, String data) throws Exception {
        curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(address, data.getBytes());
    }

    public void createPersistentWithSeqData(String address, String data) throws Exception {
        curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(address, data.getBytes());
    }

    public void createTemporarySeqData(String address, String data) throws Exception {
        curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(address, data.getBytes());
    }

    public void createTemporaryData(String address, String data) throws Exception {
        curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(address, data.getBytes());
    }

    public void setTemporaryData(String address, String data) throws Exception {
        curator.setData().forPath(address, data.getBytes());
    }

    public void destroy() {
        curator.close();
    }

    public List<String> listNode(String address) throws Exception {
        return curator.getChildren().forPath(address);
    }

    public void deleteNode(String address) throws Exception {
        curator.delete().forPath(address);
    }

    public boolean existNode(String address) {
        try {
            Stat stat = curator.checkExists().forPath(address);
            return stat != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void watchNodeData(String path, Watcher watcher) throws Exception {
        curator.getData().usingWatcher(watcher).forPath(path);
    }

    public void watchChildNodeData(String path, Watcher watcher) throws Exception {
        curator.getChildren().usingWatcher(watcher).forPath(path);
    }
}
