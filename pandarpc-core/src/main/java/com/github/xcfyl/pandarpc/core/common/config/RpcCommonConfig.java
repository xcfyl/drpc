package com.github.xcfyl.pandarpc.core.common.config;

import com.github.xcfyl.pandarpc.core.common.enums.RpcRegistryType;
import com.github.xcfyl.pandarpc.core.common.enums.RpcSerializeType;
import lombok.Data;
import lombok.ToString;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 20:09
 */
@Data
@ToString
public class RpcCommonConfig {
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
    private RpcRegistryType registryType;
    /**
     * 注册中心的地址
     */
    private String registryAddr;
    /**
     * 序列化类型
     */
    private RpcSerializeType serializeType;
}
