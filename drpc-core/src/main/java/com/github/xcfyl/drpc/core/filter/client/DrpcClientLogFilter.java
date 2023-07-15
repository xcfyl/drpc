package com.github.xcfyl.drpc.core.filter.client;

import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:13
 */
@Slf4j
public class DrpcClientLogFilter implements DrpcClientFilter {
    @Override
    public void filter(DrpcClientFilterChain chain, List<DrpcConnectionWrapper> connectionWrappers, DrpcRequest request) {
        log.debug("send request #{}", request);
    }
}
