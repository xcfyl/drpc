package com.github.xcfyl.drpc.core.common.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/17 17:35
 */
public class RetryUtils {
    private static final Logger logger = LoggerFactory.getLogger(RetryUtils.class);

    /**
     * 重试执行某个操作，直到RetryDo返回的结果通过了predicate
     * 或者超过最大重试次数
     *
     * @param retryTimes
     * @param retryInterval
     * @param retryDo
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T retry(int retryTimes, long retryInterval,
                              RetryDo<T> retryDo, Predicate<T> predicate) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("start retry, retry times {}, retry interval {}", retryTimes, retryInterval);
        }

        int curTime = 1;
        while (curTime <= retryTimes) {
            if (logger.isDebugEnabled()) {
                logger.debug("current retry time {}", curTime);
            }
            T res = retryDo.retry();
            if (predicate.test(res)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("retry success");
                }
                return res;
            }
            curTime++;
            TimeUnit.MILLISECONDS.sleep(retryInterval);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("retry failure");
        }
        return null;
    }
}
