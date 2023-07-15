package com.github.xcfyl.drpc.core.server;

import com.github.xcfyl.drpc.core.common.enums.RegistryType;
import com.github.xcfyl.drpc.core.common.enums.SerializeType;
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
public class ServerConfig {
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
    private RegistryType registryType;
    /**
     * 注册中心的地址
     */
    private String registryAddr;
    /**
     * 序列化类型
     */
    private SerializeType serializeType;
    /**
     * 服务端的监听端口号
     */
    private int port;
}
