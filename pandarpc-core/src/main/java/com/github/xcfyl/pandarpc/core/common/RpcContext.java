package com.github.xcfyl.pandarpc.core.common;

/**
 * rpc客户端上下文
 *
 * @author 西城风雨楼
 * @date create at 2023/6/24 09:20
 */
public class RpcContext {
    /**
     * 最大请求长度
     */
    private static int maxRequestLength;

    /**
     * 请求超时时间
     */
    private static long requestTimeout;

    public static void setRequestTimeout(long requestTimeout) {
        RpcContext.requestTimeout = requestTimeout;
    }

    public static long getRequestTimeout() {
        return requestTimeout;
    }

    public static void setMaxRequestLength(int maxRequestLength) {
        RpcContext.maxRequestLength = maxRequestLength;
    }

    public static int getMaxRequestLength() {
        return maxRequestLength;
    }
}
