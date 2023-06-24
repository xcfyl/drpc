package com.github.xcfyl.pandarpc.core.filter.client;

import com.github.xcfyl.pandarpc.core.client.ConnectionWrapper;
import com.github.xcfyl.pandarpc.core.protocol.RpcRequest;

import java.util.List;

/**
 * @author 西城风雨楼
 */
public interface RpcClientFilter {
    void filter(RpcClientFilterChain chain, List<ConnectionWrapper> connectionWrappers, RpcRequest request);
}
