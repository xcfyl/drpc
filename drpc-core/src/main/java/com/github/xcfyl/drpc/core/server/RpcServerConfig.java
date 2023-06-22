package com.github.xcfyl.drpc.core.server;

import lombok.Data;

/**
 * 存放和rpc服务端相关的配置信息
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:56
 */
@Data
public class RpcServerConfig {
    /**
     * 最长的请求长度
     */
    private int maxRequestLength;
    /**
     * 服务端的监听端口号
     */
    private int port;
}
