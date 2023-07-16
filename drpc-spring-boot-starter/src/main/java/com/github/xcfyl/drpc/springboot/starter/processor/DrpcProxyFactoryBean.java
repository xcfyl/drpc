package com.github.xcfyl.drpc.springboot.starter.processor;

import com.github.xcfyl.drpc.core.client.DrpcClient;
import com.github.xcfyl.drpc.core.client.DrpcRemoteReference;
import com.github.xcfyl.drpc.core.client.DrpcServiceWrapper;
import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.Resource;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 19:24
 */
public class DrpcProxyFactoryBean<T> implements FactoryBean<T> {
    private static final Logger logger = LoggerFactory.getLogger(DrpcProxyFactoryBean.class);
    private final Class<T> clazz;
    @Resource
    private DrpcClient drpcClient;

    @Resource
    private DrpcRemoteReference remoteReference;

    public DrpcProxyFactoryBean(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T getObject() throws Exception {
        // 如果当前bean被DrpcReference标注了
        DrpcServiceWrapper<T> serviceWrapper = new DrpcServiceWrapper<>();
        DrpcReference reference = clazz.getAnnotation(DrpcReference.class);
        long timeout = reference.timeout();
        int retryTimes = reference.retryTimes();
        boolean sync = reference.isSync();
        serviceWrapper.setSync(sync);
        serviceWrapper.setTimeout(timeout);
        serviceWrapper.setServiceClass(clazz);
        serviceWrapper.setRetryTimes(retryTimes);
        try {
            drpcClient.subscribeService(clazz.getName());
            return remoteReference.get(serviceWrapper);
        } catch (Exception e) {
            throw new BeanCreationException("create drpc proxy failure");
        }
    }

    @Override
    public Class<?> getObjectType() {
        return clazz;
    }
}
