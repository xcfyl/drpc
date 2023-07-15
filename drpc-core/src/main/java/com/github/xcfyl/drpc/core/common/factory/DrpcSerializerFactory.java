package com.github.xcfyl.drpc.core.common.factory;

import com.github.xcfyl.drpc.core.common.enums.DrpcSerializeType;
import com.github.xcfyl.drpc.core.serializer.DrpcSerializer;
import com.github.xcfyl.drpc.core.serializer.fastjson.DrpcFastJsonSerializer;
import com.github.xcfyl.drpc.core.serializer.jdk.DrpcJdkSerializer;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 16:28
 */
public class DrpcSerializerFactory {
    public static DrpcSerializer createRpcSerializer(DrpcSerializeType type) {
        if (type.getCode() == DrpcSerializeType.JDK.getCode()) {
            return new DrpcJdkSerializer();
        } else {
            return new DrpcFastJsonSerializer();
        }
    }
}
