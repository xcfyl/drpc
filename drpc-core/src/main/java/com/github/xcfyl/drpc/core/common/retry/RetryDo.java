package com.github.xcfyl.drpc.core.common.retry;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/17 17:35
 */
public interface RetryDo<T> {
    T retry() throws Exception;
}
