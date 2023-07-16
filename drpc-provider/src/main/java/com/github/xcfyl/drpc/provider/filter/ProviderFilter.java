package com.github.xcfyl.drpc.provider.filter;

import com.github.xcfyl.drpc.core.filter.server.DrpcAbstractServerFilter;
import com.github.xcfyl.drpc.core.filter.server.DrpcServerFilterChain;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcFilter;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 23:12
 */
@DrpcFilter
public class ProviderFilter extends DrpcAbstractServerFilter {
    @Override
    public void filter(DrpcServerFilterChain chain, DrpcRequest request) {
        System.out.println("这是测试filter");
        chain.doFilter(request);
    }
}
