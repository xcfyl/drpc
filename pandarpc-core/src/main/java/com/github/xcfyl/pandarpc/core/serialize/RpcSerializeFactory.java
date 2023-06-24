package com.github.xcfyl.pandarpc.core.serialize;

/**
 * rpc序列化工厂
 *
 * @author 西城风雨楼
 */
public interface RpcSerializeFactory<T> {
    /**
     * 将目标对象序列化为字节数组
     *
     * @param obj
     * @return
     */
    byte[] serialize(T obj) throws Exception;

    /**
     * 将字节数组反序列化为目标对象
     *
     * @param bytes
     * @param clazz
     * @return
     */
    T deserialize(byte[] bytes, Class<T> clazz) throws Exception;
}
