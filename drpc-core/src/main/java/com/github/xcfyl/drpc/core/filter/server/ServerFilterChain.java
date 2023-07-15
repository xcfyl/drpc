package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.RpcRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 15:54
 */
public class ServerFilterChain {
    private final List<ServerFilter> filters = new ArrayList<>();
    private int curIndex;

    public void addFilter(ServerFilter filter) {
        filters.add(filter);
    }

    public void doFilter(RpcRequest request) {
        if (curIndex >= filters.size()) {
            return;
        }

        ServerFilter filter = filters.get(curIndex++);
        filter.filter(this, request);
        curIndex--;
    }
}
