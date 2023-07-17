package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.protocol.DrpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/17 13:57
 */
public class DrpcResponseGuardedObject {
    private static final Logger logger = LoggerFactory.getLogger(DrpcResponseGuardedObject.class);

    private int length;
    private final Lock[] locks;
    private final Condition[] conditions;
    private final ConcurrentHashMap<String, DrpcResponse>[] responses;
    /**
     * 标识当前客户端还在等待请求
     */
    private static final DrpcResponse STILL_WAIT = new DrpcResponse("-1", null);

    @SuppressWarnings("unchecked")
    public DrpcResponseGuardedObject(int length) {
        this.length = length;
        locks = new Lock[length];
        conditions = new Condition[length];
        responses = new ConcurrentHashMap[length];
        for (int i = 0; i < length; i++) {
            locks[i] = new ReentrantLock();
            conditions[i] = locks[i].newCondition();
            responses[i] = new ConcurrentHashMap<>();
        }
    }

    public DrpcResponseGuardedObject() {
        this(64);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public DrpcResponse getDrpcResponse(String requestId, long timeout) {
        int index = getIndex(requestId);
        locks[index].lock();
        // 先添加一个标记，标识当前自己还在等待结果
        responses[index].put(requestId, STILL_WAIT);
        if (logger.isDebugEnabled()) {
            logger.debug("locks {} locked, new response {} query from guarded", index, requestId);
        }
        long now = System.currentTimeMillis();
        long futureTime = now + timeout;
        try {
            // 跳出该循环有两种可能，一种是response被添加到了response中，此时requestId的数据就不是STILL_WAIT状态了
            // 或者是超时了
            while (responses[index].get(requestId) == STILL_WAIT && now <= futureTime) {
                boolean res = conditions[index].await(timeout, TimeUnit.MILLISECONDS);
                if (!res) {
                    break;
                }
                now = System.currentTimeMillis();
            }
            if (responses[index].get(requestId) != STILL_WAIT) {
                // 如果不是因为超时结束等待，那么获取response
                DrpcResponse drpcResponse = responses[index].get(requestId);
                // 将该请求从缓存中移除，否则将导致内存泄漏
                responses[index].remove(requestId);
                return drpcResponse;
            }
        } catch (InterruptedException e) {
            logger.debug("wait response interrupted {}", e.getMessage());
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("locks {} unlocked", index);
            }
            // 当要退出get方法时，不论是否获取成功，都需要将response[index]对应位置的数据进行清除
            responses[index].remove(requestId);
            locks[index].unlock();
        }
        return null;
    }

    public void setDrpcResponse(DrpcResponse response) {
        int index = getIndex(response.getId());
        locks[index].lock();
        try {
            if (responses[index].get(response.getId()) != STILL_WAIT) {
                if (logger.isDebugEnabled()) {
                    logger.debug("locks {} locked, response {} has canceled", index, response.getId());
                }
                return;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("locks {} locked, new response {} added in guarded", index, response.getId());
            }
            responses[index].put(response.getId(), response);
            conditions[index].signalAll();
        } finally {
            if (logger.isDebugEnabled()) {
                logger.debug("locks {} unlocked", index);
            }
            locks[index].unlock();
        }
    }

    private int getIndex(String requestId) {
        int hash = Objects.hash(requestId);
        return hash & (length - 1);
    }

    public static void main(String[] args) {
        DrpcResponseGuardedObject guardedObject = new DrpcResponseGuardedObject();

        for (int i = 0; i < 1000; i++) {
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(guardedObject.getDrpcResponse(String.valueOf(j), 3000));
                }
            }).start();
        }

        for (int i = 0; i < 1000; i++) {
            final int j = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DrpcResponse response = new DrpcResponse();
                    response.setId(String.valueOf(j));
                    guardedObject.setDrpcResponse(response);
                }
            }).start();
        }
    }
}
