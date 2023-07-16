package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 15:54
 */
public class DrpcServerFilterChain {
    private static final Logger logger = LoggerFactory.getLogger(DrpcServerFilterChain.class);
    private final List<DrpcServerFilter> filters = new ArrayList<>();
    private int curIndex;

    public synchronized void addFilter(DrpcServerFilter filter) {
        filters.add(filter);
    }

    public synchronized void doFilter(DrpcRequest request) {
        if (curIndex >= filters.size()) {
            return;
        }
        DrpcServerFilter filter = filters.get(curIndex++);
        if (logger.isDebugEnabled()) {
            logger.debug("current server filter is {}", filter.getName());
        }
        filter.filter(this, request);
        curIndex--;
    }
}
