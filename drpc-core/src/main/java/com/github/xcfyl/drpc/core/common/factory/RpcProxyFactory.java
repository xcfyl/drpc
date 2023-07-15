package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.RpcClientConfig;
import com.github.xcfyl.drpc.core.client.RpcClientContext;
import com.github.xcfyl.drpc.core.proxy.RpcProxy;
import com.github.xcfyl.drpc.core.proxy.jdk.RpcJdkProxy;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:32
 */
public class RpcProxyFactory {
    public static RpcProxy createRpcProxy(RpcClientContext context) {
        return new RpcJdkProxy(context);
    }
}
