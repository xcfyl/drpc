package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.DrpcRequest;

/**
 * rpc的过滤器
 *
 * @author 西城风雨楼
 */
public interface DrpcServerFilter {
    void filter(DrpcServerFilterChain chain, DrpcRequest request);
    String getName();
}
