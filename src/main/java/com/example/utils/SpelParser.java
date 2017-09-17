package com.example.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;


/**
 * 
 * @author owang
 * Spring 实现读取EL表达式
 */
@Component
public class SpelParser {
	
	private static ExpressionParser parser = new SpelExpressionParser();
	
	
	/**
	 * Spring 实现读取EL表达式
	 * @param key
	 * @param paramsName
	 * @param args
	 * @return
	 */
	public static String getKey(String key,String[] paramsName,Object[] args){
		Expression exp = parser.parseExpression(key);
		EvaluationContext context = new StandardEvaluationContext();
		if( args.length <= 0 ){
			return null;
		}
		
		for(int i=0;i<args.length;i++){
			context.setVariable(paramsName[i], args[i]);
		}
		
		return exp.getValue(context,String.class);
		
	}
	
	public static void main(String[] args) {
		SpelParser parser = new SpelParser();
		String key = "#user.ID+'||'+#userCode";
		Object[] _args = new Object[2];
		TUser  user = new TUser();
		user.setID("001");
		_args[0]=user;
		_args[1]="zhangsan";
		
		String[] paramsName = {"user","userCode"};
		System.out.println(parser.getKey(key, paramsName, _args));
	}
}

class TUser{
	
	private String ID;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
}
