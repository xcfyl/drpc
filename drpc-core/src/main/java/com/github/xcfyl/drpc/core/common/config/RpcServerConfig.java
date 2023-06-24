package com.github.xcfyl.drpc.core.common.config;

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
     * 公用配置
     */
    private RpcCommonConfig commonConfig;
    /**
     * 服务端的监听端口号
     */
    private int port;
}
