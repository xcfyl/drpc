package com.github.xcfyl.pandarpc.core.common.factory;

import com.github.xcfyl.pandarpc.core.common.config.RpcClientConfig;
import com.github.xcfyl.pandarpc.core.proxy.RpcProxy;
import com.github.xcfyl.pandarpc.core.proxy.jdk.RpcJdkProxy;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:32
 */
public class RpcProxyFactory {
    public static RpcProxy createRpcProxy(RpcClientConfig config) {
        return new RpcJdkProxy();
    }
}
