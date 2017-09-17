package com.example.annotation;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import com.example.dao.StudentDao;
import com.example.utils.SpelParser;

/**
 *  手动增加切面
 * @author owang
 *
 */
@Aspect
@Component
public class CacheAspect {
	
	@Resource
	StudentDao studentDao;

	
	@Around("@annotation(cr)")
	public void doAround(ProceedingJoinPoint pjp,CacheResult cr) throws Throwable{
		// alt + shift +L 快捷键
		String key = getKey(cr.key(),pjp);// 读取EL表达式
		String cacheName = cr.cacheName();
		
		System.out.println("业务逻辑 1  name:"+key+"     cacheName:"+cacheName);
		
		pjp.proceed();
		
		System.out.println("业务逻辑 2   name:"+key+"     cacheName:"+cacheName);
		
		
	}
	
	// 使用spring el表达式获取函数的入参 
	private String getKey(String key,ProceedingJoinPoint pjp){
		
		Object[] args = pjp.getArgs();
		Signature signature = pjp.getSignature();
		MethodSignature ms = (MethodSignature) signature;
		Method method = ms.getMethod();
		
		// 形参名称
		String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
		
		return SpelParser.getKey(key, parameterNames, args);
		
	}

}