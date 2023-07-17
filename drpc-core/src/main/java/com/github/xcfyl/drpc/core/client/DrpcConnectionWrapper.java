package com.github.xcfyl.drpc.core.client;

import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 封装了连接对象
 *
 * @author 西城风雨楼
 * @date create at 2023/6/23 15:27
 */
public class DrpcConnectionWrapper {
    private static final Logger logger = LoggerFactory.getLogger(DrpcConnectionWrapper.class);

    /**
     * 代表连接的channelFuture对象
     */
    private ChannelFuture channelFuture;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 端口号
     */
    private Integer port;

    /**
     * 这些连接可能有权重
     */
    private BigDecimal weight;
    /**
     * 属于哪个服务的连接
     */
    private String serviceName;

    public DrpcConnectionWrapper() {

    }

    /**
     * 判断当前连接是否正常
     *
     * @return
     */
    public boolean isOk() {
        return channelFuture != null && channelFuture.channel() != null
                && (channelFuture.channel().isActive() || channelFuture.channel().isOpen());
    }

    /**
     * 发送数据
     *
     * @param data
     */
    public void writeAndFlush(Object data) {
        channelFuture.channel().writeAndFlush(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DrpcConnectionWrapper that = (DrpcConnectionWrapper) o;
        return Objects.equals(ip, that.ip) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "DrpcConnectionWrapper{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                ", serviceName='" + serviceName + '\'' +
                '}';
    }
}
