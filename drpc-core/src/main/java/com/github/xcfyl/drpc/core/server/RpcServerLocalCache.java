package com.github.xcfyl.drpc.core.server;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc服务端的本地缓存
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:29
 */
public class RpcServerLocalCache {
    /**
     * 存放服务名称和服务提供者class的映射
     */
    public static final Map<String, Object> SERVICE_PROVIDER_MAP = new HashMap<>();
}
