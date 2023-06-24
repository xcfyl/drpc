package com.github.xcfyl.pandarpc.core.pubsub.listener;

import com.github.xcfyl.pandarpc.core.pubsub.event.RpcEvent;

/**
 * @author 西城风雨楼
 */
public interface RpcEventListener<T extends RpcEvent<?>> {
    void callback(T event);
}
