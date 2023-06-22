package com.github.xcfyl.drpc.core.event;

import com.github.xcfyl.drpc.core.event.data.ServiceUpdateEventData;
import com.github.xcfyl.drpc.core.event.listener.RpcServiceUpdateEventListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * rpc事件发布器
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:22
 */
public class RpcEventPublisher {
    private final List<RpcEventListener<?>> eventListeners = new ArrayList<>();
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 3, 1000, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));


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
    public synchronized void publishEvent(RpcEvent<?> event) {
        for (RpcEventListener listener : eventListeners) {
            Class eventType = parseEventType(listener);
            if (eventType == event.getClass()) {
                threadPoolExecutor.submit(() -> listener.callback(event));
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

    public static void main(String[] args) {
        RpcServiceUpdateEvent updateEvent = new RpcServiceUpdateEvent();
        ServiceUpdateEventData serviceChangeInfo = new ServiceUpdateEventData();
        serviceChangeInfo.setServiceName("service1");
        serviceChangeInfo.setNewServiceList(new ArrayList<>());
        updateEvent.setData(serviceChangeInfo);
        RpcEventPublisher eventPublisher = new RpcEventPublisher();
        RpcServiceUpdateEventListener updateEventListener = new RpcServiceUpdateEventListener();
        eventPublisher.addEventListener(updateEventListener);
        eventPublisher.publishEvent(updateEvent);
        System.out.println(eventPublisher.parseEventType(updateEventListener));
    }
}
