package com.github.xcfyl.drpc.core.config;

import lombok.ToString;

/**
 * 存放和rpc服务端相关的配置信息
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:56
 */
@ToString
public class RpcServerConfig {
    /**
     * 服务端的监听端口号
     */
    private int port;

    /**
     * 最长请求限制
     */
    private int maxRequestLength;

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setMaxRequestLength(int maxRequestLength) {
        this.maxRequestLength = maxRequestLength;
    }

    public int getMaxRequestLength() {
        return maxRequestLength;
    }
}
