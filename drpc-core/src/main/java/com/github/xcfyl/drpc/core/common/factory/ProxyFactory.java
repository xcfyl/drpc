package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.ClientContext;
import com.github.xcfyl.drpc.core.common.enums.ProxyType;
import com.github.xcfyl.drpc.core.proxy.RpcProxy;
import com.github.xcfyl.drpc.core.proxy.jdk.JdkProxy;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:32
 */
public class ProxyFactory {
    public static RpcProxy createRpcProxy(ProxyType type, ClientContext context) {
        if (type == ProxyType.JDK) {
            return new JdkProxy(context);
        }
        throw new RuntimeException("暂不支持的代理类型");
    }
}
