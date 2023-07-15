package com.github.xcfyl.drpc.core.pubsub;

import com.github.xcfyl.drpc.core.pubsub.event.RpcEvent;
import com.github.xcfyl.drpc.core.pubsub.listener.RpcEventListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * rpc事件发布器
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:22
 */
public class RpcEventPublisher {
    private final List<RpcEventListener<?>> eventListeners = new ArrayList<>();

    private static class RpcEventPublisherHolder {
        static final RpcEventPublisher EVENT_PUBLISHER = new RpcEventPublisher();
    }

    public static RpcEventPublisher getInstance() {
        return RpcEventPublisherHolder.EVENT_PUBLISHER;
    }

    /**
     * 添加一个新的事件监听器
     *
     * @param eventListener
     */
    public synchronized void addEventListener(RpcEventListener<?> eventListener) {
        eventListeners.add(eventListener);
    }

    /**
     * 发布事件
     *
     * @param event
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public synchronized void publishEvent(RpcEvent event) {
        for (RpcEventListener listener : eventListeners) {
            Class eventType = parseEventType(listener);
            if (eventType == event.getClass()) {
                listener.callback(event);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private Class parseEventType(RpcEventListener listener) {
        Type[] types = listener.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }
}
