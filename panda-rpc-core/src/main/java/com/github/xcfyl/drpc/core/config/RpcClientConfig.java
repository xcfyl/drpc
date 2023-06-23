package com.github.xcfyl.drpc.core.config;

import lombok.ToString;

/**
 * 存放和rpc客户端相关的配置信息
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:56
 */
@ToString
public class RpcClientConfig {
    /**
     * rpc服务地址
     */
    private String serverAddr;
    /**
     * 最长请求限制
     */
    private Integer maxRequestLength;
    /**
     * 请求超时时间
     */
    private Long requestTimeout;

    public void setRequestTimeout(Long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public Long getRequestTimeout() {
        return requestTimeout;
    }

    public String getServerIp() {
        return serverAddr.split(":")[0];
    }

    public Integer getServerPort() {
        return Integer.parseInt(serverAddr.split(":")[1]);
    }

    public void setMaxRequestLength(Integer maxRequestLength) {
        this.maxRequestLength = maxRequestLength;
    }

    public Integer getMaxRequestLength() {
        return maxRequestLength;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
}
