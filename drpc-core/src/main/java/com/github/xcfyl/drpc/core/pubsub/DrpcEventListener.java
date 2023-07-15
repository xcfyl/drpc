package com.github.xcfyl.drpc.core.pubsub;

/**
 * @author 西城风雨楼
 */
public interface DrpcEventListener<T extends DrpcEvent<?>> {
    void callback(T event);
}
