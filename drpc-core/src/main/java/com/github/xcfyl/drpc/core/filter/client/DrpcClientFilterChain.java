package com.github.xcfyl.drpc.core.filter.client;

import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:04
 */
public class DrpcClientFilterChain {
    private final List<DrpcClientFilter> filters = new ArrayList<>();
    private int curIndex = 0;

    public synchronized void addFilter(DrpcClientFilter filter) {
        filters.add(filter);
    }

    /**
     * doFilter方法可能被多个线程同时调用，因此需要将该方法加锁
     *
     * @param connectionWrappers
     * @param request
     */
    public synchronized void doFilter(List<DrpcConnectionWrapper> connectionWrappers, DrpcRequest request) {
        if (curIndex >= filters.size()) {
            return;
        }
        DrpcClientFilter filter = filters.get(curIndex++);
        filter.filter(this, connectionWrappers, request);
        curIndex--;
    }
}
