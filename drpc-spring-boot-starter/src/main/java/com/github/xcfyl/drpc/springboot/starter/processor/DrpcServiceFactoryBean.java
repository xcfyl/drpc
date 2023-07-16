package com.github.xcfyl.drpc.springboot.starter.processor;

import com.github.xcfyl.drpc.core.server.DrpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartFactoryBean;

import javax.annotation.Resource;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 20:24
 */
public class DrpcServiceFactoryBean<T> implements SmartFactoryBean<T> {
    private static final Logger logger = LoggerFactory.getLogger(DrpcServiceFactoryBean.class);
    private final Class<T> clazz;
    @Resource
    private DrpcServer drpcServer;

    public DrpcServiceFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public T getObject() throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        drpcServer.registerService(instance);
        return instance;
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }

    @Override
    public boolean isPrototype() {
        return false;
    }

    @Override
    public boolean isEagerInit() {
        return true;
    }
}
