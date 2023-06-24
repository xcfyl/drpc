package com.github.xcfyl.pandarpc.core.event;

import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 13:34
 */
public class ServerShutdownEvent implements RpcEvent<RpcRegistry> {
    private RpcRegistry registry;

    @Override
    public RpcRegistry getData() {
        return registry;
    }

    @Override
    public void setData(RpcRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String getName() {
        return ServerShutdownEvent.class.getName();
    }
}
