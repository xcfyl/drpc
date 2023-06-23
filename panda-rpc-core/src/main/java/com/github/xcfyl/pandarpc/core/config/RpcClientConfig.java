package com.github.xcfyl.pandarpc.core.config;

import com.github.xcfyl.pandarpc.core.enums.ProxyType;
import com.github.xcfyl.pandarpc.core.enums.RegistryType;
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
     * 最长请求限制
     */
    private Integer maxRequestLength;
    /**
     * 请求超时时间
     */
    private Long requestTimeout;
    /**
     * 代理类型
     */
    private ProxyType proxyType;
    /**
     * 注册中心的类型
     */
    private RegistryType registryType;
}
