package com.github.xcfyl.pandarpc.core.event.listener;

import com.github.xcfyl.pandarpc.core.event.RpcEventListener;
import com.github.xcfyl.pandarpc.core.event.ServerShutdownEvent;
import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import com.github.xcfyl.pandarpc.core.registry.RpcRegistry;
import com.github.xcfyl.pandarpc.core.server.RpcServerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *
 *
 * @author 西城风雨楼
 * @date create at 2023/6/24 13:33
 */
@Slf4j
public class ServerShutdownEventListener implements RpcEventListener<ServerShutdownEvent> {
    @Override
    public void callback(ServerShutdownEvent event) {
        // 如果rpc服务器被正常关闭，那么需要取消注册所有已经注册的服务
        RpcRegistry registry = event.getData();
        Map<String, RegistryData> registryDataCache = RpcServerContext.getRegistryDataCache();
        for (RegistryData registryData : registryDataCache.values()) {
            try {
                registry.unregister(registryData);
            } catch (Exception e) {
                log.error("close rpc server, unregister service #{} failure! exception is #{}", registryData, e.getMessage());
            }
        }
    }
}
