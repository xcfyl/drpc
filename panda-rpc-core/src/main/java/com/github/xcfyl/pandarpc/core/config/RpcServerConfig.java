package com.github.xcfyl.pandarpc.core.config;

import com.github.xcfyl.pandarpc.core.enums.RegistryType;
import lombok.Data;
import lombok.ToString;

/**
 * 存放和rpc服务端相关的配置信息
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:56
 */
@ToString
@Data
public class RpcServerConfig {
    /**
     * 服务端的监听端口号
     */
    private int port;
    /**
     * 最长请求限制
     */
    private int maxRequestLength;
    /**
     * 注册中心的类型
     */
    private RegistryType registryType;
}
