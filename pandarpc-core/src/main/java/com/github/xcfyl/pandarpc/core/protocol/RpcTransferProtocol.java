package com.github.xcfyl.pandarpc.core.protocol;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc传输协议
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 09:44
 */
@Data
@NoArgsConstructor
public class RpcTransferProtocol implements Serializable {
    private static final long serialVersionUID = -3657714509000090585L;
    /**
     * 协议标识，用于检测是否为rpc协议
     */
    private static short magicNumber = 1998;
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
    private Map<String, Object> attr;

    public RpcTransferProtocol(byte[] body) {
        this.length = body.length;
        this.body = body;
        attr = new HashMap<>();
    }

    /**
     * 获取协议头部长度，协议头等于magicNumber + length
     *
     * @return 返回协议头部长度
     */
    public int getHeaderLength() {
        return 6;
    }

    public static short getMagicNumber() {
        return magicNumber;
    }
}
