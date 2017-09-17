package com.example.dao.impl;
	    

import org.springframework.stereotype.Repository;

import com.example.dao.StudentDao;
import com.example.entity.Student;


@Repository
public class StudentDaoImpl implements StudentDao {
	
	
	public Student findByName(String name){
		return new Student((int)(Math.random()*100)+"", name, ((int)(Math.random()*10)+18));
	}

}
