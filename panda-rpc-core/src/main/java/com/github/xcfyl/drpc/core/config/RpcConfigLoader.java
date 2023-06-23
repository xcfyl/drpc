package com.github.xcfyl.drpc.core.config;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import static com.github.xcfyl.drpc.core.constants.RpcConfigName.*;

/**
 * 加载指定路径下的Properties文件，解析配置
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 17:40
 */
public class RpcConfigLoader {
    private static final String CONFIG_FILE_NAME = "drpc.properties";
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

    public static RpcServerConfig loadRpcServerConfig() {
        RpcServerConfig serverConfig = new RpcServerConfig();
        // 设置rpc服务端的端口
        serverConfig.setPort(getInteger(SERVER_PORT_CONFIG_PREFIX, 1998));
        // 设置服务端可以接受的最大请求的长度
        serverConfig.setMaxRequestLength(getInteger(MAX_REQUEST_LENGTH_PREFIX, 1024 * 1024));
        return serverConfig;
    }

    public static RpcClientConfig loadRpcClientConfig() {
        RpcClientConfig clientConfig = new RpcClientConfig();
        Integer serverPort = getInteger(SERVER_PORT_CONFIG_PREFIX, 1998);
        String serverIp = getString(SERVER_IP_CONFIG_PREFIX, "127.0.0.1");
        Integer maxRequestLength = getInteger(MAX_REQUEST_LENGTH_PREFIX, 1024 * 1024);
        Long requestTimeout = getLong(REQUEST_TIMEOUT_PREFIX, 3000L);
        clientConfig.setServerAddr(serverIp + ":" + serverPort);
        clientConfig.setRequestTimeout(requestTimeout);
        clientConfig.setMaxRequestLength(maxRequestLength);
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
