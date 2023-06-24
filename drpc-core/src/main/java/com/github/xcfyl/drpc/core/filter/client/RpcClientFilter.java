package com.github.xcfyl.drpc.core.filter.client;

import com.github.xcfyl.drpc.core.client.ConnectionWrapper;
import com.github.xcfyl.drpc.core.protocol.RpcRequest;

import java.util.List;

/**
 * @author 西城风雨楼
 */
public interface RpcClientFilter {
    void filter(RpcClientFilterChain chain, List<ConnectionWrapper> connectionWrappers, RpcRequest request);
}
