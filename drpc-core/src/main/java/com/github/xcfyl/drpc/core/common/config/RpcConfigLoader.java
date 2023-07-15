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
     * 加载rpc的公有配置
     *
     * @return
     */
    public RpcCommonConfig loadRpcCommonConfig() {
        String applicationName = getString(RpcCommonConfigName.APPLICATION_NAME.getDescription(),
                UUID.randomUUID().toString());
        Integer maxRequestLength = getInteger(RpcCommonConfigName.MAX_REQUEST_LENGTH.getDescription(),
                1024 * 1024);
        String registryType = getString(RpcCommonConfigName.REGISTRY_TYPE.getDescription(),
                RpcRegistryType.ZK.getDescription());
        String registryAddr = getString(RpcCommonConfigName.REGISTRY_ADDR.getDescription(), "127.0.0.1:2181");
        String serializeType = getString(RpcCommonConfigName.SERIALIZE_TYPE.toString(), RpcSerializeType.FASTJSON.getDescription());
        RpcCommonConfig rpcCommonConfig = new RpcCommonConfig();
        rpcCommonConfig.setApplicationName(applicationName);
        rpcCommonConfig.setMaxRequestLength(maxRequestLength);
        rpcCommonConfig.setRegistryType(RpcRegistryType.fromDescription(registryType));
        rpcCommonConfig.setRegistryAddr(registryAddr);
        rpcCommonConfig.setSerializeType(RpcSerializeType.fromDescription(serializeType));
        return rpcCommonConfig;
    }

    /**
     * 从配置文件中加载服务端的配置
     *
     * @return
     */
    public RpcServerConfig loadRpcServerConfig() {
        RpcServerConfig serverConfig = new RpcServerConfig();
        // rpc服务的端口
        Integer port = getInteger(RpcServerConfigName.SERVER_PORT.getDescription(), 1998);
        serverConfig.setPort(port);
        serverConfig.setCommonConfig(loadRpcCommonConfig());
        return serverConfig;
    }

    /**
     * 从配置文件中加载客户端的配置
     *
     * @return
     */
    public RpcClientConfig loadRpcClientConfig() {
        RpcClientConfig clientConfig = new RpcClientConfig();
        Long requestTimeout = getLong(RpcClientConfigName.CLIENT_REQUEST_TIMEOUT.getDescription(), 3000L);
        String proxyType = getString(RpcClientConfigName.CLIENT_PROXY_TYPE.getDescription(), RpcProxyType.JDK.getDescription());
        String routerType = getString(RpcClientConfigName.CLIENT_ROUTER_TYPE.getDescription(), RpcRouterType.RANDOM.getDescription());
        clientConfig.setCommonConfig(loadRpcCommonConfig());
        clientConfig.setRequestTimeout(requestTimeout);
        clientConfig.setProxyType(RpcProxyType.fromDescription(proxyType));
        clientConfig.setRouterType(RpcRouterType.fromDescription(routerType));
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
