package com.github.xcfyl.drpc.core.common;

import lombok.ToString;

import java.io.Serializable;

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
    private final short flag;
    /**
     * 本次传输的数据长度
     */
    private final int length;
    /**
     * 本次传输的数据内容
     */
    private final byte[] body;

    public RpcTransferProtocol(short flag, int length, byte[] body) {
        this.flag = flag;
        this.length = length;
        this.body = body;
    }

    public short getFlag() {
        return flag;
    }

    public byte[] getBody() {
        return body;
    }

    public int getLength() {
        return length;
    }
}
