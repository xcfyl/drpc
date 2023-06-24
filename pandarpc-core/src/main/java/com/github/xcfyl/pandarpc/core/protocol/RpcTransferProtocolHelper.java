package com.github.xcfyl.pandarpc.core.protocol;

import com.alibaba.fastjson.JSON;

/**
 * 协议处理的辅助工具类
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:21
 */
public class RpcTransferProtocolHelper {
    /**
     * 从协议中解析出rpc请求对象
     *
     * @param protocol
     * @return
     */
    public static RpcRequest parseRpcRequest(RpcTransferProtocol protocol) {
        String json = new String(protocol.getBody());
        return JSON.parseObject(json, RpcRequest.class);
    }

    /**
     * 从协议中解析出rpc响应对象
     *
     * @param protocol
     * @return
     */
    public static RpcResponse parseRpcResponse(RpcTransferProtocol protocol) {
        String json = new String(protocol.getBody());
        return JSON.parseObject(json, RpcResponse.class);
    }

    /**
     * 验证协议的magicNumber的合法性
     *
     * @param magicNumber
     * @return
     */
    public static boolean checkMagicNumber(short magicNumber) {
        return magicNumber == RpcTransferProtocol.getMagicNumber();
    }
}
