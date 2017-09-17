package com.example.entity;

import org.springframework.stereotype.Repository;

@Repository
public class Student{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	
	private String name;
	
	private int age;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Student(String code, String name, int age) {
		super();
		this.code = code;
		this.name = name;
		this.age = age;
	}

	public Student() {
		super();
	}

	@Override
	public String toString() {
		return "Student [code=" + code + ", name=" + name + ", age=" + age + "]";
	}
	
	
}
