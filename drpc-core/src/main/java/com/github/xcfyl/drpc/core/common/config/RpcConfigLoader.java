package com.github.xcfyl.drpc.core.common.config;

import com.github.xcfyl.drpc.core.client.RpcClientConfig;
import com.github.xcfyl.drpc.core.common.enums.*;
import com.github.xcfyl.drpc.core.server.RpcServerConfig;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.UUID;

/**
 * 加载指定路径下的Properties文件，解析配置
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 17:40
 */
public class RpcConfigLoader {
    private final Properties properties;

    public RpcConfigLoader(String configName) {
        properties = getProperties(configName);
    }

    private static Properties getProperties(String configFileName) {
        URL resource = RpcConfigLoader.class.getClassLoader().getResource(configFileName);
        if (resource == null) {
            throw new RuntimeException("未找到配置文件");
        }
        Properties properties = new Properties();
        Path path = Paths.get(resource.getPath());
        try (InputStream input = Files.newInputStream(path, StandardOpenOption.READ)) {
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 从配置文件中加载服务端的配置
     *
     * @return
     */
    public RpcServerConfig loadRpcServerConfig() {
        // 创建服务端配置对象
        RpcServerConfig serverConfig = new RpcServerConfig();
        // 获取服务端的应用名称
        String applicationName = getString(RpcServerConfigName.SERVER_APPLICATION_NAME.getDescription(),
                UUID.randomUUID().toString());
        // 获取最大的请求长度
        Integer maxRequestLength = getInteger(RpcServerConfigName.SERVER_MAX_REQUEST_LENGTH.getDescription(),
                1024 * 1024);
        // 获取注册中心类型
        String registryType = getString(RpcServerConfigName.SERVER_REGISTRY_TYPE.getDescription(),
                RpcRegistryType.ZK.getDescription());
        // 获取注册中心地址
        String registryAddr = getString(RpcServerConfigName.SERVER_REGISTRY_ADDR.getDescription(), "127.0.0.1:2181");
        // 获取序列化器类型
        String serializeType = getString(RpcServerConfigName.SERVER_SERIALIZE_TYPE.toString(), RpcSerializeType.FASTJSON.getDescription());
        // 获取服务端口号
        Integer port = getInteger(RpcServerConfigName.SERVER_PORT.getDescription(), 1998);
        // 设置服务器的配置
        serverConfig.setPort(port);
        serverConfig.setApplicationName(applicationName);
        serverConfig.setMaxRequestLength(maxRequestLength);
        serverConfig.setRegistryType(RpcRegistryType.fromDescription(registryType));
        serverConfig.setRegistryAddr(registryAddr);
        serverConfig.setSerializeType(RpcSerializeType.fromDescription(serializeType));
        return serverConfig;
    }

    /**
     * 从配置文件中加载客户端的配置
     *
     * @return
     */
    public RpcClientConfig loadRpcClientConfig() {
        RpcClientConfig clientConfig = new RpcClientConfig();
        // 获取应用名称
        String applicationName = getString(RpcClientConfigName.CLIENT_APPLICATION_NAME.getDescription(),
                UUID.randomUUID().toString());
        // 获取最大请求长度
        Integer maxRequestLength = getInteger(RpcClientConfigName.CLIENT_MAX_REQUEST_LENGTH.getDescription(),
                1024 * 1024);
        // 获取注册中心的类型字符串
        String registryType = getString(RpcClientConfigName.CLIENT_REGISTRY_TYPE.getDescription(),
                RpcRegistryType.ZK.getDescription());
        // 获取注册中心的地址
        String registryAddr = getString(RpcClientConfigName.CLIENT_REGISTRY_ADDR.getDescription(), "127.0.0.1:2181");
        // 获取序列化器的类型
        String serializeType = getString(RpcClientConfigName.CLIENT_SERIALIZE_TYPE.toString(), RpcSerializeType.FASTJSON.getDescription());
        // 获取请求时间
        Long requestTimeout = getLong(RpcClientConfigName.CLIENT_REQUEST_TIMEOUT.getDescription(), 3000L);
        // 获取代理类型
        String proxyType = getString(RpcClientConfigName.CLIENT_PROXY_TYPE.getDescription(), RpcProxyType.JDK.getDescription());
        // 获取路由类型
        String routerType = getString(RpcClientConfigName.CLIENT_ROUTER_TYPE.getDescription(), RpcRouterType.RANDOM.getDescription());
        // 设置客户端的配置
        clientConfig.setRequestTimeout(requestTimeout);
        clientConfig.setProxyType(RpcProxyType.fromDescription(proxyType));
        clientConfig.setRouterType(RpcRouterType.fromDescription(routerType));
        clientConfig.setApplicationName(applicationName);
        clientConfig.setMaxRequestLength(maxRequestLength);
        clientConfig.setRegistryType(RpcRegistryType.fromDescription(registryType));
        clientConfig.setRegistryAddr(registryAddr);
        clientConfig.setSerializeType(RpcSerializeType.fromDescription(serializeType));
        return clientConfig;
    }

    private String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private Integer getInteger(String key, Integer defaultValue) {
        String property = properties.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Integer.parseInt(property);
    }

    private Long getLong(String key, Long defaultValue) {
        String property = properties.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Long.parseLong(property);
    }
}
