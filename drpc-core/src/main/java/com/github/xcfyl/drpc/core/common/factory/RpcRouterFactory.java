package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.client.RpcClientConfig;
import com.github.xcfyl.drpc.core.common.enums.RpcRouterType;
import com.github.xcfyl.drpc.core.router.RpcRandomRouter;
import com.github.xcfyl.drpc.core.router.RpcRoundRobinRouter;
import com.github.xcfyl.drpc.core.router.RpcRouter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:19
 */
@Slf4j
public class RpcRouterFactory {
    public static RpcRouter createRpcRouter(RpcClientConfig config, ConnectionManager connectionManager) {
        if (Objects.requireNonNull(config.getRouterType()) == RpcRouterType.RANDOM) {
            return new RpcRandomRouter(connectionManager);
        }
        return new RpcRoundRobinRouter(connectionManager);
    }
}
