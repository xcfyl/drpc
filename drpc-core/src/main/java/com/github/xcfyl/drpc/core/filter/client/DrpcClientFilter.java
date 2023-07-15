package com.github.xcfyl.drpc.core.filter.client;

import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;

import java.util.List;

/**
 * @author 西城风雨楼
 */
public interface DrpcClientFilter {
    void filter(DrpcClientFilterChain chain, List<DrpcConnectionWrapper> connectionWrappers, DrpcRequest request);
}
