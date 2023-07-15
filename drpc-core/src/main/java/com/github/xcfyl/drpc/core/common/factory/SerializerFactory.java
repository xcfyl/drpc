package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.common.enums.SerializeType;
import com.github.xcfyl.drpc.core.serializer.Serializer;
import com.github.xcfyl.drpc.core.serializer.fastjson.FastJsonSerializer;
import com.github.xcfyl.drpc.core.serializer.jdk.JdkSerializer;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:28
 */
public class SerializerFactory {
    public static Serializer createRpcSerializer(SerializeType type) {
        if (type.getCode() == SerializeType.JDK.getCode()) {
            return new JdkSerializer();
        } else {
            return new FastJsonSerializer();
        }
    }
}
