package com.github.xcfyl.drpc.core.router;

import com.github.xcfyl.drpc.core.client.ConnectionWrapper;

/**
 * rpc的路由层
 *
 * @author 西城风雨楼
 */
public interface Router {
    /**
     * 选择指定service下面一个可用的连接对象，作为本次rpc调用的服务提供者
     *
     * @param serviceName
     * @return
     */
    ConnectionWrapper select(String serviceName) throws Exception;

    /**
     * 刷新路由，当本地的服务提供者列表发生变化的时候
     * 调用该方法，可以刷新当前路由层的数据
     */
    void refresh(String serviceName);
}
