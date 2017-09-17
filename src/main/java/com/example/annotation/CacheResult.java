package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  增加注释
 * @author owang
 * Retention
 *    source 表示在编译时这个注释会被移除，不会包含在编译后产品的class文件中，例如override 
 *    class  表示这个注释会被包含在class文件中，但在运行时会被移除
 *    runtime 表示这个注释会被留到运行时，在运行时可以被JVM访问到，我们可以在运行时通过反射解析这个注释
 *    
 * Target
 *    ElementType.?
 *    {ElementType.?,ElementType.?}
 *    
 *    ElementType.TYPE 加类上， method 加方法体上
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface CacheResult {
	
	
	String key();
	
	String cacheName();
	
	String backUpKey() default "";
	
	boolean needBloodFilter() default false;

}
