package com.example.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.annotation.CacheResult;
import com.example.dao.StudentDao;
import com.example.entity.Student;

@Service
@CacheResult(key="student-Service",cacheName="redis-Service")
public class StudentService {
	
	@Resource
	StudentDao studentDao;
	
	
	public String testStudent(){
		
		return "天气晴朗";
		
	}
	
	
	@CacheResult(key="#name",cacheName="redis-Method")
	public Student findStudent( String name){
		Student student = studentDao.findByName(name);
		System.out.println(student.toString());
		return student;
	}
	
	public void findStudentNoAnnotation( String name){
		System.out.println("业务逻辑 1  name:"+name+"     ");
		
		Student student = studentDao.findByName(name);
		System.out.println(student.toString());
		
		System.out.println("业务逻辑 2   name:"+name+"     ");
		
		
	}

}
