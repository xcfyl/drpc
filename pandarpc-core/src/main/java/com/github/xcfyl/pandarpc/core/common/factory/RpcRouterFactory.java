package com.github.xcfyl.pandarpc.core.common.factory;

import com.github.xcfyl.pandarpc.core.client.RpcClientContext;
import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.common.enums.RpcRouterType;
import com.github.xcfyl.pandarpc.core.exception.ConfigErrorException;
import com.github.xcfyl.pandarpc.core.router.RpcRandomRouter;
import com.github.xcfyl.pandarpc.core.router.RpcRoundRobinRouter;
import com.github.xcfyl.pandarpc.core.router.RpcRouter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:19
 */
@Slf4j
public class RpcRouterFactory {
    public static RpcRouter createRpcRouter(RpcClientConfig config) {
        if (Objects.requireNonNull(config.getRouterType()) == RpcRouterType.RANDOM) {
            return new RpcRandomRouter();
        }
        return new RpcRoundRobinRouter();
    }
}
