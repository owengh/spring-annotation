package com.example.dao;

import com.example.entity.Student;

public interface StudentDao {

	Student findByName(String name);
	
}
