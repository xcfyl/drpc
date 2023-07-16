package com.github.xcfyl.springboot.starter.processor;

import com.github.xcfyl.drpc.core.filter.server.DrpcServerFilter;
import com.github.xcfyl.drpc.core.server.DrpcServer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartFactoryBean;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 20:24
 */
public class DrpcServiceFactoryBean<T> implements SmartFactoryBean<T> {
    private final Class<T> clazz;
    @Resource
    private DrpcServer drpcServer;

    @Resource
    private ObjectProvider<List<DrpcServerFilter>> drpcServerFilters;

    public DrpcServiceFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getObject() throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        drpcServer.registerService(instance);
        for (DrpcServerFilter drpcServerFilter : drpcServerFilters.getIfAvailable(ArrayList::new)) {
            drpcServer.addFilter(drpcServerFilter);
        }
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
