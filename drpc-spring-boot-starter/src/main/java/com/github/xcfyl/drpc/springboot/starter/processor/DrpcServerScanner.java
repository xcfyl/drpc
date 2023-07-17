package com.github.xcfyl.drpc.springboot.starter.processor;

import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcService;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.Set;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 17:54
 */
public class DrpcServerScanner extends ClassPathBeanDefinitionScanner {
    public DrpcServerScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @NonNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
        return super.doScan(basePackages);
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        return metadataReader.getAnnotationMetadata().hasAnnotation(DrpcService.class.getName());
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return  beanDefinition.getMetadata().hasAnnotation(DrpcService.class.getName());
    }
}
