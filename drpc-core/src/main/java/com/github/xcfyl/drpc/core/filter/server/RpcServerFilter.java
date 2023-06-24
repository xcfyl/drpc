package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.RpcRequest;

/**
 * rpc的过滤器
 *
 * @author 西城风雨楼
 */
public interface RpcServerFilter {
    void filter(RpcServerFilterChain chain, RpcRequest request);
}
