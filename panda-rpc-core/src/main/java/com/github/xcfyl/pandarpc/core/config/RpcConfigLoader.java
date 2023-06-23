package com.github.xcfyl.pandarpc.core.config;

import com.github.xcfyl.pandarpc.core.enums.*;

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
    private static final String CONFIG_FILE_NAME = "pandarpc.properties";
    private static final Properties PROPERTIES = new Properties();

    private static final RpcCommonConfig RPC_COMMON_CONFIG;

    static {
        URL resource = RpcConfigLoader.class.getClassLoader().getResource(CONFIG_FILE_NAME);
        if (resource == null) {
            throw new RuntimeException("未找到配置文件");
        }
        Path path = Paths.get(resource.getPath());
        try (InputStream input = Files.newInputStream(path, StandardOpenOption.READ)) {
            PROPERTIES.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RPC_COMMON_CONFIG = loadRpcCommonConfig();
    }

    /**
     * 从配置文件中加载服务端的配置
     *
     * @return
     */
    public static RpcServerConfig loadRpcServerConfig() {
        RpcServerConfig serverConfig = new RpcServerConfig();
        // rpc服务的端口
        Integer port = getInteger(RpcServerConfigName.SERVER_PORT.getDescription(), 1998);
        serverConfig.setPort(port);
        serverConfig.setCommonConfig(RPC_COMMON_CONFIG);
        return serverConfig;
    }

    /**
     * 加载rpc的公有配置
     *
     * @return
     */
    public static RpcCommonConfig loadRpcCommonConfig() {
        String applicationName = getString(RpcCommonConfigName.APPLICATION_NAME.getDescription(),
                UUID.randomUUID().toString());
        Integer maxRequestLength = getInteger(RpcCommonConfigName.MAX_REQUEST_LENGTH.getDescription(),
                1024 * 1024);
        String registryType = getString(RpcCommonConfigName.REGISTRY_TYPE.getDescription(),
                RegistryType.ZK.getDescription());
        String registryAddr = getString(RpcCommonConfigName.REGISTRY_ADDR.getDescription(), "127.0.0.1:2181");
        RpcCommonConfig rpcCommonConfig = new RpcCommonConfig();
        rpcCommonConfig.setApplicationName(applicationName);
        rpcCommonConfig.setMaxRequestLength(maxRequestLength);
        rpcCommonConfig.setRegistryType(RegistryType.fromDescription(registryType));
        rpcCommonConfig.setRegistryAddr(registryAddr);
        return rpcCommonConfig;
    }

    /**
     * 从配置文件中加载客户端的配置
     *
     * @return
     */
    public static RpcClientConfig loadRpcClientConfig() {
        RpcClientConfig clientConfig = new RpcClientConfig();
        Long requestTimeout = getLong(RpcClientConfigName.CLIENT_REQUEST_TIMEOUT.getDescription(), 3000L);
        String proxyType = getString(RpcClientConfigName.CLIENT_PROXY_TYPE.getDescription(), ProxyType.JDK.getDescription());
        clientConfig.setRequestTimeout(requestTimeout);
        clientConfig.setProxyType(ProxyType.fromDescription(proxyType));
        clientConfig.setCommonConfig(RPC_COMMON_CONFIG);
        return clientConfig;
    }

    private static String getString(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    private static Integer getInteger(String key, Integer defaultValue) {
        String property = PROPERTIES.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Integer.parseInt(property);
    }

    private static Long getLong(String key, Long defaultValue) {
        String property = PROPERTIES.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Long.parseLong(property);
    }

    public static void main(String[] args) {
        RpcServerConfig serverConfig = RpcConfigLoader.loadRpcServerConfig();
        RpcClientConfig clientConfig = RpcConfigLoader.loadRpcClientConfig();
        System.out.println(serverConfig);
        System.out.println(clientConfig);
    }
}
