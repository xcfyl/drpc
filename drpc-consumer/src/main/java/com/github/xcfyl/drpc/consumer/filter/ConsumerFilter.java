package com.github.xcfyl.drpc.consumer.filter;

import com.github.xcfyl.drpc.core.client.DrpcConnectionWrapper;
import com.github.xcfyl.drpc.core.filter.client.DrpcAbstractClientFilter;
import com.github.xcfyl.drpc.core.filter.client.DrpcClientFilterChain;
import com.github.xcfyl.drpc.core.protocol.DrpcRequest;
import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcFilter;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 23:11
 */
@DrpcFilter
public class ConsumerFilter extends DrpcAbstractClientFilter {
    @Override
    public void filter(DrpcClientFilterChain chain, List<DrpcConnectionWrapper> connectionWrappers, DrpcRequest request) {
        System.out.println("这是测试filter");
    }
}
