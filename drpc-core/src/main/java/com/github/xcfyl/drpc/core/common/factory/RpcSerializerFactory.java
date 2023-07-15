package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.common.enums.RpcSerializeType;
import com.github.xcfyl.drpc.core.serializer.RpcSerializer;
import com.github.xcfyl.drpc.core.serializer.fastjson.FastJsonRpcSerializer;
import com.github.xcfyl.drpc.core.serializer.jdk.JdkRpcSerializer;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:28
 */
public class RpcSerializerFactory {
    public static RpcSerializer createRpcSerializer(RpcSerializeType type) {
        if (type.getCode() == RpcSerializeType.JDK.getCode()) {
            return new JdkRpcSerializer();
        } else {
            return new FastJsonRpcSerializer();
        }
    }
}
