package com.github.xcfyl.pandarpc.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.github.xcfyl.pandarpc.core.serialize.RpcSerializeFactory;

/**
 * 基于fastjson的序列化和反序列化工厂
 *
 * @author 西城风雨楼
 * @date create at 2023/6/24 10:08
 */
public class FastJsonRpcSerializeFactory implements RpcSerializeFactory {
    @Override
    public <T> byte[] serialize(T obj) {
        return JSON.toJSONString(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data), clazz);
    }
}
