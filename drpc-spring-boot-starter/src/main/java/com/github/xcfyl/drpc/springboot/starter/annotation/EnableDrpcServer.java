package com.github.xcfyl.drpc.springboot.starter.annotation;

import com.github.xcfyl.drpc.springboot.starter.config.DrpcServerAutoConfiguration;
import com.github.xcfyl.drpc.springboot.starter.processor.DrpcServerImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启DrpcServer的自动装配
 *
 * @author 西城风雨楼
 * @date create at 2023/7/17 09:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DrpcServerAutoConfiguration.class, DrpcServerImportSelector.class})
public @interface EnableDrpcServer {
    String[] scanPackages();
}
