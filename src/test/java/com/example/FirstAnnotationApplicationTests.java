package com.example;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.annotation.CacheResult;
import com.example.service.StudentService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FirstAnnotationApplicationTests {
	
	@Resource
	StudentService studentService;

	@Test
	public void test(){
		studentService.findStudent("洋洋");
	}	
	
	@Test
	public void testNoAnnotation(){
		studentService.findStudentNoAnnotation("zhangsan");
	}
	
	
	
	@Test
	public void testAnnotation(){
		
		Class<StudentService> clazz = StudentService.class;
		Class<CacheResult> annotationClazz = CacheResult.class;
		// 判断类别(类)是否存在注释
		if(clazz .isAnnotationPresent(annotationClazz)){
			CacheResult cr = (CacheResult) clazz.getAnnotation(annotationClazz);
			System.out.println(cr.key());
			System.out.println(cr.cacheName());
		}
		
		Method[] methods = clazz.getMethods();
		for(Method method : methods){
			// 判断 方法 是否存在注释
			if(method.isAnnotationPresent(annotationClazz)){
				CacheResult cr =method.getAnnotation(annotationClazz);
				System.out.println(cr.key());
				System.out.println(cr.cacheName());
			}
		}
	}
}