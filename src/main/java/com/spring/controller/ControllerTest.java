package com.spring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.annotion.Controller;
import com.spring.annotion.Qualifier;
import com.spring.annotion.RequestMapping;
import com.spring.service.StudentService;

@Controller("/ControllerTest")
public class ControllerTest {

	
	@Qualifier
	private StudentService studentService;
	
	
	@RequestMapping("insert")
	public String insert(HttpServletRequest request,HttpServletResponse response){
		studentService.insert();
		return "";
	}
	
}
