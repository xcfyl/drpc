package com.github.xcfyl.drpc.core.protocol;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc传输协议
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:44
 */
public class DrpcTransferProtocol implements Serializable {
    private static final long serialVersionUID = -3657714509000090585L;
    /**
     * 协议标识，用于检测是否为rpc协议
     */
    private final static short MAGIC_NUMBER = 1998;
    /**
     * 本次传输的数据长度
     */
    private int length;
    /**
     * 本次传输的数据内容
     */
    private byte[] body;
    /**
     * 协议的附加属性
     */
    private final Map<String, Object> attrs;

    public DrpcTransferProtocol() {
        attrs = new HashMap<>();
    }

    public DrpcTransferProtocol(byte[] body) {
        this();
        this.length = body.length;
        this.body = body;
    }

    /**
     * 获取协议头部长度，协议头等于magicNumber + length
     *
     * @return 返回协议头部长度
     */
    public static int getHeaderLength() {
        return 6;
    }

    public static short getMagicNumber() {
        return MAGIC_NUMBER;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setAttr(String key, Object value) {
        attrs.put(key, value);
    }

    public Object get(String key) {
        return attrs.get(key);
    }

    @Override
    public String toString() {
        return "DrpcTransferProtocol{" +
                "length=" + length +
                ", body=" + Arrays.toString(body) +
                ", attrs=" + attrs +
                '}';
    }
}
