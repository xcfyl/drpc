package com.github.xcfyl.drpc.core.event;

/**
 * @author 西城风雨楼
 */
public interface RpcEventListener<T extends RpcEvent<?>> {
    void callback(T event);
}
