package com.github.xcfyl.drpc.core.event;

/**
 * rpc事件接口
 *
 * @author 西城风雨楼
 */
public interface RpcEvent<T> {
    T getData();
    void setData(T data);
    String getName();
}
