package com.github.xcfyl.drpc.springboot.starter.annotation;

import com.github.xcfyl.drpc.springboot.starter.config.DrpcClientAutoConfiguration;
import com.github.xcfyl.drpc.springboot.starter.processor.DrpcClientImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启DrpcClient的自动装配
 *
 * @author 西城风雨楼
 * @date create at 2023/7/17 09:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DrpcClientAutoConfiguration.class, DrpcClientImportSelector.class})
public @interface EnableDrpcClient {
    String[] scanPackages();
}
