package com.github.xcfyl.drpc.core.filter.client;

import com.github.xcfyl.drpc.core.client.ConnectionWrapper;
import com.github.xcfyl.drpc.core.protocol.RpcRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:04
 */
public class RpcClientFilterChain {
    private final List<RpcClientFilter> filters = new ArrayList<>();
    private int curIndex = 0;

    public void addFilter(RpcClientFilter filter) {
        filters.add(filter);
    }

    public void doFilter(List<ConnectionWrapper> connectionWrappers, RpcRequest request) {
        if (curIndex >= filters.size()) {
            return;
        }
        RpcClientFilter filter = filters.get(curIndex++);
        filter.filter(this, connectionWrappers, request);
        curIndex--;
    }
}
