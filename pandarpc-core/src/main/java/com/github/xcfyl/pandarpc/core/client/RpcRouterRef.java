package com.github.xcfyl.pandarpc.core.client;

import com.github.xcfyl.pandarpc.core.router.RpcRouter;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 09:27
 */
public class RpcRouterRef {
    private static RpcRouter rpcRouter;

    public static void setRpcRouter(RpcRouter rpcRouter) {
        RpcRouterRef.rpcRouter = rpcRouter;
    }

    public static RpcRouter getRpcRouter() {
        return rpcRouter;
    }
}
