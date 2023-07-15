package com.github.xcfyl.drpc.core.filter.server;

import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:14
 */
@Slf4j
public class DrpcServerLogFilter implements DrpcServerFilter {
    @Override
    public void filter(DrpcServerFilterChain chain, DrpcRequest request) {
        log.debug("receive request #{}", request);
    }
}
