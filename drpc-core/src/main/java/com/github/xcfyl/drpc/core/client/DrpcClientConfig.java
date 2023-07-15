package com.github.xcfyl.drpc.core.client;

import com.github.xcfyl.drpc.core.common.enums.DrpcProxyType;
import com.github.xcfyl.drpc.core.common.enums.DrpcRegistryType;
import com.github.xcfyl.drpc.core.common.enums.DrpcRouterType;
import com.github.xcfyl.drpc.core.common.enums.DrpcSerializeType;
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
public class DrpcClientConfig {
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 最大请求长度
     */
    private Integer maxRequestLength;
    /**
     * 注册中心类型
     */
    private DrpcRegistryType registryType;
    /**
     * 注册中心的地址
     */
    private String registryAddr;
    /**
     * 序列化类型
     */
    private DrpcSerializeType serializeType;
    /**
     * 请求超时时间
     */
    private Long requestTimeout;
    /**
     * 代理类型
     */
    private DrpcProxyType proxyType;
    /**
     * 内置的路由类型
     */
    private DrpcRouterType routerType;
}
