package com.github.xcfyl.drpc.core.pubsub;

/**
 * rpc事件接口
 *
 * @author 西城风雨楼
 */
public interface DrpcEvent<T> {
    T getData();
    void setData(T data);
    String getName();
}
