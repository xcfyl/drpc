package com.github.xcfyl.drpc.core.common.retry;

import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     */
    public static <T> T retry(String retryName, int retryTimes, long retryInterval,
                              RetryDo<T> retryDo, Predicate<T> predicate) throws Exception {
        return retry(retryName, retryTimes, retryInterval, Long.MAX_VALUE, retryDo, predicate);
    }

    /**
     * 在指定时间内尽可能多的重试
     *
     * @param retryTimes
     * @param retryInterval
     * @param timeout
     * @param retryDo
     * @param predicate
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> T retry(String retryName, int retryTimes, long retryInterval, long timeout,
                              RetryDo<T> retryDo, Predicate<T> predicate) throws Exception {
        if (logger.isDebugEnabled()) {
            if (StringUtil.isNullOrEmpty(retryName)) {
                retryName = "";
            }
            logger.debug("start retry {}, retry times {}, retry interval {}", retryName, retryTimes, retryInterval);
        }

        long now = System.currentTimeMillis();
        int curTime = 1;
        while (curTime <= retryTimes && now <= timeout) {
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
            now = System.currentTimeMillis();
        }
        if (logger.isDebugEnabled()) {
            logger.debug("retry failure");
        }
        return null;
    }
}
