package com.github.xcfyl.drpc.core.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DrpcEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(DrpcEventPublisher.class);

    private final List<DrpcEventListener<?>> eventListeners = new ArrayList<>();

    private static class RpcEventPublisherHolder {
        static final DrpcEventPublisher EVENT_PUBLISHER = new DrpcEventPublisher();
    }

    public static DrpcEventPublisher getInstance() {
        return RpcEventPublisherHolder.EVENT_PUBLISHER;
    }

    /**
     * 添加一个新的事件监听器
     *
     * @param eventListener
     */
    public synchronized void addEventListener(DrpcEventListener<?> eventListener) {
        eventListeners.add(eventListener);
        if (logger.isDebugEnabled()) {
            logger.debug("add a new event listener {}", eventListener);
        }
    }

    /**
     * 发布事件
     *
     * @param event
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public synchronized void publishEvent(DrpcEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("publish a new event {}", event);
        }
        for (DrpcEventListener listener : eventListeners) {
            Class eventType = parseEventType(listener);
            if (eventType == event.getClass()) {
                listener.callback(event);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private Class parseEventType(DrpcEventListener listener) {
        Type[] types = listener.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }
}
