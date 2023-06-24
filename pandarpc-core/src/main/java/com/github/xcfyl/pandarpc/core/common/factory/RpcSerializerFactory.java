package com.github.xcfyl.pandarpc.core.common.factory;

import com.github.xcfyl.pandarpc.core.client.RpcClientContext;
import com.github.xcfyl.pandarpc.core.common.config.RpcCommonConfig;
import com.github.xcfyl.pandarpc.core.common.enums.RpcSerializeType;
import com.github.xcfyl.pandarpc.core.exception.ConfigErrorException;
import com.github.xcfyl.pandarpc.core.serializer.RpcSerializer;
import com.github.xcfyl.pandarpc.core.serializer.fastjson.FastJsonRpcSerializer;
import com.github.xcfyl.pandarpc.core.serializer.jdk.JdkRpcSerializer;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:28
 */
public class RpcSerializerFactory {
    public static RpcSerializer createRpcSerializer(RpcCommonConfig config) {
        RpcSerializeType serializeType = config.getSerializeType();
        if (serializeType.getCode() == RpcSerializeType.JDK.getCode()) {
            return new JdkRpcSerializer();
        } else {
            return new FastJsonRpcSerializer();
        }
    }
}
