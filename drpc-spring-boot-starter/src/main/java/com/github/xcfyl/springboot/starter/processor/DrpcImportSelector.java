package com.github.xcfyl.springboot.starter.processor;

import com.github.xcfyl.springboot.starter.annotation.DrpcReference;
import com.github.xcfyl.springboot.starter.annotation.DrpcService;
import com.github.xcfyl.springboot.starter.annotation.EnableDrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.Map;
import java.util.Set;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 17:54
 */
public class DrpcImportSelector implements ImportSelector, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(DrpcImportSelector.class);
    private BeanFactory beanFactory;
    public DrpcImportSelector() {
    }

    @NonNull
    @Override
    public String[] selectImports(@NonNull AnnotationMetadata metadata) {
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableDrpc.class.getName());
        if (attributes == null || attributes.size() == 0) {
            return new String[0];
        }

        java.lang.String[] scanPackages = (String[]) attributes.get("scanPackages");
        DrpcScanner scanner = new DrpcScanner((BeanDefinitionRegistry) beanFactory);
        Set<BeanDefinitionHolder> definitionHolders = scanner.doScan(scanPackages);
        for (BeanDefinitionHolder holder : definitionHolders) {
            BeanDefinition beanDefinition = holder.getBeanDefinition();
            String className = beanDefinition.getBeanClassName();
            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(DrpcReference.class)) {
                    ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
                    constructorArgumentValues.addGenericArgumentValue(clazz);
                    beanDefinition.setBeanClassName(DrpcProxyFactoryBean.class.getName());
                } else if (clazz.isAnnotationPresent(DrpcService.class)) {
                    ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
                    constructorArgumentValues.addGenericArgumentValue(clazz);
                    beanDefinition.setBeanClassName(DrpcServiceFactoryBean.class.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new String[0];
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
