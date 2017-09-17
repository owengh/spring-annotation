package com.example.annotation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;


/**
 * 增加spring监听器
 * @author owang
 * ApplicationListener<ContextRefreshedEvent>  容器启动成功后，监听 ContextRefreshedEvent
 */
@Component
public class AnnotationContext implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
	
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 *  监听事件
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		Class<CacheResult> clazz = CacheResult.class;
		// 容器中拿到所有加了注释的bean
		Map<String, Object>  map = applicationContext.getBeansWithAnnotation(CacheResult.class);
		for(Entry<String, Object> entry:map.entrySet()){
			CacheResult cr = AnnotationUtils.findAnnotation(entry.getValue().getClass(), clazz);
			System.out.println("onApplicationEvent="+cr.key());
			System.out.println("onApplicationEvent="+cr.cacheName());
			
			for(Method method:entry.getValue().getClass().getMethods()){
				if(method.isAnnotationPresent(clazz)){
					CacheResult _cr= method.getAnnotation(clazz);
					System.out.println("method:"+_cr.key());
					System.out.println("method:"+_cr.cacheName());
				}
			}
		}
	}

}
