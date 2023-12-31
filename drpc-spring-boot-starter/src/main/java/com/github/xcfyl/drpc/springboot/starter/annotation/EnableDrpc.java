package com.github.xcfyl.drpc.springboot.starter.annotation;

import com.github.xcfyl.drpc.springboot.starter.config.DrpcClientAutoConfiguration;
import com.github.xcfyl.drpc.springboot.starter.config.DrpcServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启Drpc的自动装配
 *
 * @author 西城风雨楼
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DrpcClientAutoConfiguration.class, DrpcServerAutoConfiguration.class})
public @interface EnableDrpc {
    /**
     * 配置扫描Drpc相关注解的路径
     *
     * @return
     */
    String[] scanPackages();
}
