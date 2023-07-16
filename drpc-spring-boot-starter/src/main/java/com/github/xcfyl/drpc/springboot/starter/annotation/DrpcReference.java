package com.github.xcfyl.drpc.springboot.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在接口上，表示为某个接口生成drpc的代理
 *
 * @author 西城风雨楼
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DrpcReference {
    /**
     * 当前调用是否是
     *
     * @return
     */
    boolean isSync() default true;
    /**
     * 如果本次调用失败了，重试的次数，重试次数只对同步方法起作用
     * 异步方法暂时不支持重试
     * @return
     */
    int retryTimes() default 1;

    long timeout() default 3000;
}
