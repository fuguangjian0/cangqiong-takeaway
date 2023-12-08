package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解, 用于标识某个方法需要进行字段`自动填充`处理
 */
@Target(ElementType.METHOD) // 作用范围:方法
@Retention(RetentionPolicy.RUNTIME) // 信息保留在.class文件
public @interface AutoFill {
    //数据库操作类型enum : INSERT UPDATE
    OperationType value();
}
