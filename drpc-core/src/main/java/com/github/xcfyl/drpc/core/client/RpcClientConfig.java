package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.config.RpcCommonConfig;
import com.github.xcfyl.drpc.core.common.enums.RpcProxyType;
import com.github.xcfyl.drpc.core.common.enums.RpcRouterType;
import lombok.Data;
import lombok.ToString;

/**
 * 存放和rpc客户端相关的配置信息
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:56
 */
@ToString
@Data
public class RpcClientConfig {
    /**
     * rpc的公有配置
     */
    private RpcCommonConfig commonConfig;
    /**
     * 请求超时时间
     */
    private Long requestTimeout;
    /**
     * 代理类型
     */
    private RpcProxyType proxyType;
    /**
     * 内置的路由类型
     */
    private RpcRouterType routerType;
}
