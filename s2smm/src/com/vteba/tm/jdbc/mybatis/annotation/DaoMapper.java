package com.vteba.tm.jdbc.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * mybatis mapper 注解，spring扫描被该注解标注的类
 * @author yinlei
 * date 2012-9-30 上午10:11:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface DaoMapper {

}
