package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.DrpcRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 15:54
 */
public class DrpcServerFilterChain {
    private final List<DrpcServerFilter> filters = new ArrayList<>();
    private int curIndex;

    public void addFilter(DrpcServerFilter filter) {
        filters.add(filter);
    }

    public void doFilter(DrpcRequest request) {
        if (curIndex >= filters.size()) {
            return;
        }

        DrpcServerFilter filter = filters.get(curIndex++);
        filter.filter(this, request);
        curIndex--;
    }
}
