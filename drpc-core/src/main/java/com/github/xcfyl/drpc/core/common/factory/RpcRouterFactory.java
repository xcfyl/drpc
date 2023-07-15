package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.ConnectionManager;
import com.github.xcfyl.drpc.core.common.enums.RpcRouterType;
import com.github.xcfyl.drpc.core.router.RpcRandomRouter;
import com.github.xcfyl.drpc.core.router.RpcRoundRobinRouter;
import com.github.xcfyl.drpc.core.router.RpcRouter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:19
 */
@Slf4j
public class RpcRouterFactory {
    public static RpcRouter createRpcRouter(RpcRouterType type, ConnectionManager connectionManager) {
        if (type == RpcRouterType.RANDOM) {
            return new RpcRandomRouter(connectionManager);
        } else if (type == RpcRouterType.ROUND_ROBIN) {
            return new RpcRoundRobinRouter(connectionManager);
        }
        throw new RuntimeException("暂不支持的路由类型");
    }
}
