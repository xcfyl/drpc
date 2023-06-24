package com.github.xcfyl.pandarpc.core.serialize.jdk;

import com.github.xcfyl.pandarpc.core.serialize.RpcSerializeFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 基于jdk原生序列化机制的序列化和反序列化工厂
 *
 * @author 西城风雨楼
 * @date create at 2023/6/24 10:03
 */
public class JdkRpcSerializeFactory<T> implements RpcSerializeFactory<T> {

    @Override
    public byte[] serialize(T obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    @Override
    public T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return clazz.cast(ois.readObject());
    }
}
