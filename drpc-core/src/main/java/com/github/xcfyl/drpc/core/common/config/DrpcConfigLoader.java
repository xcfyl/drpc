package com.github.xcfyl.drpc.core.common.config;

import com.github.xcfyl.drpc.core.client.DrpcClientConfig;
import com.github.xcfyl.drpc.core.common.enums.*;
import com.github.xcfyl.drpc.core.exception.DrpcConfigException;
import com.github.xcfyl.drpc.core.server.DrpcServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DrpcConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(DrpcConfigLoader.class);
    private final String configFileName;

    public DrpcConfigLoader(String configFileName) {
        this.configFileName = configFileName;
    }

    private Properties getProperties() {
        URL resource = DrpcConfigLoader.class.getClassLoader().getResource(configFileName);
        if (resource == null) {
            return null;
        }
        Properties properties = new Properties();
        Path path = Paths.get(resource.getPath());
        try (InputStream input = Files.newInputStream(path, StandardOpenOption.READ)) {
            properties.load(input);
        } catch (Exception e) {
            logger.debug("load properties failure {}", e.getMessage());
            return null;
        }
        return properties;
    }

    /**
     * 从配置文件中加载服务端的配置
     *
     * @return
     */
    public DrpcServerConfig loadRpcServerConfig() throws Exception {
        Properties properties = getProperties();
        if (properties == null) {
            throw new DrpcConfigException("load rpc server config failure");
        }
        // 创建服务端配置对象
        DrpcServerConfig serverConfig = new DrpcServerConfig();
        // 获取服务端的应用名称
        String applicationName = getString(properties, DrpcServerConfigName.SERVER_APPLICATION_NAME.getDescription(),
                UUID.randomUUID().toString());
        // 获取最大的请求长度
        Integer maxRequestLength = getInteger(properties,
                DrpcServerConfigName.SERVER_MAX_REQUEST_LENGTH.getDescription(), 1024 * 1024);
        // 获取注册中心类型
        String registryType = getString(properties, DrpcServerConfigName.SERVER_REGISTRY_TYPE.getDescription(),
                DrpcRegistryType.ZK.getDescription());
        // 获取注册中心地址
        String registryAddr = getString(properties,
                DrpcServerConfigName.SERVER_REGISTRY_ADDR.getDescription(), "127.0.0.1:2181");
        // 获取序列化器类型
        String serializeType = getString(properties,
                DrpcServerConfigName.SERVER_SERIALIZE_TYPE.toString(), DrpcSerializeType.FASTJSON.getDescription());
        // 获取服务端口号
        Integer port = getInteger(properties, DrpcServerConfigName.SERVER_PORT.getDescription(), 1998);
        // 设置服务器的配置
        serverConfig.setPort(port);
        serverConfig.setApplicationName(applicationName);
        serverConfig.setMaxRequestLength(maxRequestLength);
        serverConfig.setRegistryType(DrpcRegistryType.fromDescription(registryType));
        serverConfig.setRegistryAddr(registryAddr);
        serverConfig.setSerializeType(DrpcSerializeType.fromDescription(serializeType));
        return serverConfig;
    }

    /**
     * 从配置文件中加载客户端的配置
     *
     * @return
     */
    public DrpcClientConfig loadRpcClientConfig() throws Exception {
        Properties properties = getProperties();
        if (properties == null) {
            throw new DrpcConfigException("load rpc client config failure");
        }
        DrpcClientConfig clientConfig = new DrpcClientConfig();
        // 获取应用名称
        String applicationName = getString(properties, DrpcClientConfigName.CLIENT_APPLICATION_NAME.getDescription(),
                UUID.randomUUID().toString());
        // 获取最大请求长度
        Integer maxRequestLength = getInteger(properties,
                DrpcClientConfigName.CLIENT_MAX_REQUEST_LENGTH.getDescription(), 1024 * 1024);
        // 获取注册中心的类型字符串
        String registryType = getString(properties,
                DrpcClientConfigName.CLIENT_REGISTRY_TYPE.getDescription(), DrpcRegistryType.ZK.getDescription());
        // 获取注册中心的地址
        String registryAddr = getString(properties,
                DrpcClientConfigName.CLIENT_REGISTRY_ADDR.getDescription(), "127.0.0.1:2181");
        // 获取序列化器的类型
        String serializeType = getString(properties,
                DrpcClientConfigName.CLIENT_SERIALIZE_TYPE.toString(), DrpcSerializeType.FASTJSON.getDescription());
        // 获取请求时间
        Long requestTimeout = getLong(properties,
                DrpcClientConfigName.CLIENT_REQUEST_TIMEOUT.getDescription(), 3000L);
        // 获取代理类型
        String proxyType = getString(properties,
                DrpcClientConfigName.CLIENT_PROXY_TYPE.getDescription(), DrpcProxyType.JDK.getDescription());
        // 获取路由类型
        String routerType = getString(properties,
                DrpcClientConfigName.CLIENT_ROUTER_TYPE.getDescription(), DrpcRouterType.RANDOM.getDescription());
        Integer subscribeRetryTimes = getInteger(properties,
                DrpcClientConfigName.CLIENT_SUBSCRIBE_RETRY_TIMES.getDescription(), 0);
        Long subscribeRetryInterval = getLong(properties,
                DrpcClientConfigName.CLIENT_SUBSCRIBE_RETRY_INTERVAL.getDescription(), 1000L);
        // 设置客户端的配置
        clientConfig.setRequestTimeout(requestTimeout);
        clientConfig.setProxyType(DrpcProxyType.fromDescription(proxyType));
        clientConfig.setRouterType(DrpcRouterType.fromDescription(routerType));
        clientConfig.setApplicationName(applicationName);
        clientConfig.setMaxRequestLength(maxRequestLength);
        clientConfig.setRegistryType(DrpcRegistryType.fromDescription(registryType));
        clientConfig.setRegistryAddr(registryAddr);
        clientConfig.setSerializeType(DrpcSerializeType.fromDescription(serializeType));
        clientConfig.setSubscribeRetryTimes(subscribeRetryTimes);
        clientConfig.setSubscribeRetryInterval(subscribeRetryInterval);
        return clientConfig;
    }

    private String getString(Properties properties, String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    private Integer getInteger(Properties properties, String key, Integer defaultValue) {
        String property = properties.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Integer.parseInt(property);
    }

    private Long getLong(Properties properties, String key, Long defaultValue) {
        String property = properties.getProperty(key);
        if (property == null) {
            return defaultValue;
        }
        return Long.parseLong(property);
    }
}
