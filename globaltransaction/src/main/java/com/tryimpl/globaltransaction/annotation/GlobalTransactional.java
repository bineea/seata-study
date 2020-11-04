package com.tryimpl.globaltransaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 全局事务注解
 * 全局事务涉及的所有方法均需要配置全局事务注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GlobalTransactional {

    boolean isStart() default false;
    boolean isEnd() default false;
}
