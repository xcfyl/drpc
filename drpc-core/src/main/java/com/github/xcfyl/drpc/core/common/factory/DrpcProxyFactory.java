package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.client.DrpcClientContext;
import com.github.xcfyl.drpc.core.common.enums.DrpcProxyType;
import com.github.xcfyl.drpc.core.proxy.DrpcProxy;
import com.github.xcfyl.drpc.core.proxy.jdk.DrpcJdkProxy;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:32
 */
public class DrpcProxyFactory {
    public static DrpcProxy createRpcProxy(DrpcProxyType type, DrpcClientContext context) {
        if (type == DrpcProxyType.JDK) {
            return new DrpcJdkProxy(context);
        }
        throw new RuntimeException("暂不支持的代理类型");
    }
}
