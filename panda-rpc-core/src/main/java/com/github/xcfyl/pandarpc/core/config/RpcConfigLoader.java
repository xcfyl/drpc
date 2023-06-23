package com.github.xcfyl.pandarpc.core.config;

import com.github.xcfyl.pandarpc.core.enums.ProxyType;
import com.github.xcfyl.pandarpc.core.enums.RegistryType;
import com.github.xcfyl.pandarpc.core.enums.RpcClientConfigName;
import com.github.xcfyl.pandarpc.core.enums.RpcServerConfigName;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

/**
 * 加载指定路径下的Properties文件，解析配置
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 17:40
 */
public class RpcConfigLoader {
    private static final String CONFIG_FILE_NAME = "pandarpc.properties";
    private static final Properties PROPERTIES = new Properties();

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
        // 服务端可以接受的最大请求长度
        Integer maxRequestLength = getInteger(RpcServerConfigName.SERVER_MAX_REQUEST_LENGTH.getDescription(), 1024 * 1024);
        // 注册中心的类型
        String registryType = getString(RpcServerConfigName.SERVER_REGISTRY_TYPE.getDescription(), RegistryType.ZK.getDescription());
        serverConfig.setPort(port);
        serverConfig.setMaxRequestLength(maxRequestLength);
        serverConfig.setRegistryType(RegistryType.fromDescription(registryType));
        return serverConfig;
    }

    /**
     * 从配置文件中加载客户端的配置
     *
     * @return
     */
    public static RpcClientConfig loadRpcClientConfig() {
        RpcClientConfig clientConfig = new RpcClientConfig();
        // 最大请求内容的长度
        Integer maxRequestLength = getInteger(RpcClientConfigName.CLIENT_MAX_REQUEST_LENGTH.getDescription(), 1024 * 1024);
        // 请求超时时间
        Long requestTimeout = getLong(RpcClientConfigName.CLIENT_REQUEST_TIMEOUT.getDescription(), 3000L);
        // 代理类型
        String proxyType = getString(RpcClientConfigName.CLIENT_PROXY_TYPE.getDescription(), ProxyType.JDK.getDescription());
        // 注册中心类型
        String registryType = getString(RpcClientConfigName.CLIENT_REGISTRY_TYPE.getDescription(), RegistryType.ZK.getDescription());
        clientConfig.setMaxRequestLength(maxRequestLength);
        clientConfig.setRequestTimeout(requestTimeout);
        clientConfig.setProxyType(ProxyType.fromDescription(proxyType));
        clientConfig.setRegistryType(RegistryType.fromDescription(registryType));
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
