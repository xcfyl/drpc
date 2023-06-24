package com.github.xcfyl.pandarpc.core.filter.server;

import com.github.xcfyl.pandarpc.core.protocol.RpcRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 15:54
 */
public class RpcServerFilterChain {
    private final List<RpcServerFilter> filters = new ArrayList<>();
    private int curIndex;

    public void addFilter(RpcServerFilter filter) {
        filters.add(filter);
    }

    public void doFilter(RpcRequest request) {
        if (curIndex >= filters.size()) {
            return;
        }

        RpcServerFilter filter = filters.get(curIndex++);
        filter.filter(this, request);
        curIndex--;
    }
}
