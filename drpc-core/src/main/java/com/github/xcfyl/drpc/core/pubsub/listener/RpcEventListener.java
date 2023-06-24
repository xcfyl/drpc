package com.github.xcfyl.drpc.core.pubsub.listener;

import com.github.xcfyl.drpc.core.pubsub.event.RpcEvent;

/**
 * @author 西城风雨楼
 */
public interface RpcEventListener<T extends RpcEvent<?>> {
    void callback(T event);
}
