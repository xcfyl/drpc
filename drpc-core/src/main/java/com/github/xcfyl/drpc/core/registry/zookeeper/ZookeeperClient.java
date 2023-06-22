package com.github.xcfyl.drpc.core.registry.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

/**
 * Zookeeper客户端
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 21:39
 */
public class ZookeeperClient {
    private final CuratorFramework curator;

    public ZookeeperClient(String zkAddress) {
        this(zkAddress, 1000, 3);
    }

    public ZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetries) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimes, maxRetries);
        curator = CuratorFrameworkFactory.newClient(zkAddress, retryPolicy);
    }

    public void updateNodeData(String address, String data) {
        try {
            curator.setData().forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNodeData(String address) {
        try {
            byte[] result = curator.getData().forPath(address);
            if (result != null) {
                return new String(result);
            }
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getChildrenData(String path) {
        try {
            return curator.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createPersistentData(String address, String data) {
        try {
            curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPersistentWithSeqData(String address, String data) {
        try {
            curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTemporarySeqData(String address, String data) {
        try {
            curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(address, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTemporaryData(String address, String data) {
        try {
            curator.create().creatingParentContainersIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(address, data.getBytes());
        } catch (KeeperException.NoChildrenForEphemeralsException e) {
            try {
                curator.setData().forPath(address, data.getBytes());
            } catch (Exception ex) {
                throw new IllegalStateException(ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    public void setTemporaryData(String address, String data) {
        try {
            curator.setData().forPath(address, data.getBytes());
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    public void destroy() {
        curator.close();
    }

    public List<String> listNode(String address) {
        try {
            return curator.getChildren().forPath(address);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public boolean deleteNode(String address) {
        try {
            curator.delete().forPath(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    public void watchNodeData(String path, Watcher watcher) {
        try {
            curator.getData().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void watchChildNodeData(String path, Watcher watcher) {
        try {
            curator.getChildren().usingWatcher(watcher).forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
