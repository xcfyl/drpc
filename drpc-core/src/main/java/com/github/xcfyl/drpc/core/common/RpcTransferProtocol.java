package com.github.xcfyl.drpc.core.common;

import com.github.xcfyl.drpc.core.common.constants.RpcConstants;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc传输协议
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:44
 */
@ToString
public class RpcTransferProtocol implements Serializable {
    private static final long serialVersionUID = -3657714509000090585L;
    /**
     * 协议标识，用于检测是否为rpc协议
     */
    private final short magicNumber = (short) RpcConstants.MAGIC_NUMBER.getCode();
    /**
     * 本次传输的数据长度
     */
    private final int length;
    /**
     * 本次传输的数据内容
     */
    private final byte[] body;
    /**
     * 协议的附加属性
     */
    private final Map<String, Object> attr;

    public RpcTransferProtocol(byte[] body) {
        this.length = body.length;
        this.body = body;
        attr = new HashMap<>();
    }

    public void putAttr(String key, Object value) {
        attr.put(key, value);
    }

    public Object getAttr(String key) {
        return attr.get(key);
    }

    /**
     * 获取协议头部长度，协议头等于magicNumber + length
     *
     * @return 返回协议头部长度
     */
    public int getHeaderLength() {
        return 6;
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public byte[] getBody() {
        return body;
    }

    public int getLength() {
        return length;
    }
}
