package com.github.xcfyl.pandarpc.core.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 通用的工具类
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 20:30
 */
@Slf4j
public class CommonUtils {
    /**
     * 获取本机ip地址
     *
     * @return 返回本机ip地址，如果获取失败，返回本机环路地址
     */
    public static String getCurrentMachineIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                } else {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {
                            return ip.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug("exception -> #{}", e.getMessage());
            log.debug("获取本机ip地址失败，使用127.0.0.1");
        }
        return "127.0.0.1";
    }
}
