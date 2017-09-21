package com.spring.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.spring.annotion.Controller;
import com.spring.annotion.Qualifier;
import com.spring.annotion.RequestMapping;
import com.spring.annotion.Service;
import com.spring.controller.ControllerTest;



@WebServlet("/DispatcherServlet")
public class DispatcherServlet extends HttpServlet{
	// 这里放扫描出来的全包类名
	private List<String> packeNames  = new ArrayList<String>();
	
	//拿实例对象是根据注解配置参数做一个维护关系集合
	private Map<String, Object> instanceMaps = new HashMap<String, Object>();
	
	// 方法链 维护请求 和 请求处理对象
	private Map<String, Method> handlerMaps = new HashMap<String, Method>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//拿到 完整的请求路径
		String url = req.getRequestURI();
		String projectName = req.getContextPath();
		// projectName/classURL/method
		String requrl = url.replace(projectName, "");
		Method method = handlerMaps.get(requrl);
		PrintWriter out =resp.getWriter();
		if( method == null ){
			out.write("404");
		}
		// 看能否优化，通用代码 !!!!!!!!!!!!!!!!
		// localhost:8080/myspring/controllerTest/insert  
		String className = url.split("/")[2];
		ControllerTest controllerTest = (ControllerTest) instanceMaps.get(className);
		try {
			method.invoke(controllerTest, new Object[]{req,resp,null});
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

	@Override
	public void init() throws ServletException {
		String basePackageName = "com.spring.controller";
	/*	//我们加载配置中那些bean
		//我们需要将bean加载到容器
		basePackageName = Cofigutils.getBeanPackName("");
		System.out.println("读取springmvc的配置的基包有:"+basePackageName);
		*/
		
		try {
			// 通过基包来扫描我们的Bean 加载到spring容器里面去
			scanBean(basePackageName);//先扫描 后注入
			
			findBeansInstance();
			
			// 依赖注入 spriingIOC
			springIOC();
			
			//方法链的问题  handlerMaps(); hander/method 来处理请求，  例如  controllerTest/insert
			handlerMaps();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	//方法链处理 controllerTest/insert 来处理请求里面的方法链
	private void handlerMaps() throws Exception{
		if( instanceMaps.size() == 0 ){
			return;
		}
		for(Map.Entry<String, Object> entry: instanceMaps.entrySet()){
			// 判断字节码是否含有 Controller.class对象
			if(entry.getValue().getClass().isAnnotationPresent(Controller.class)){
				Controller controllerAnnotation = (Controller)entry.getValue().getClass().getAnnotation(Controller.class);
				// 拿到了 @Controller("controllerTest")的"controllerTest" 
				String classUrl = controllerAnnotation.value();
				for(Method method :entry.getValue().getClass().getMethods()){
					if(method.isAnnotationPresent(RequestMapping.class)){
						String methodValue = ((RequestMapping)method.getAnnotation(RequestMapping.class)).value();
						// spring 方法执行链
						handlerMaps.put("/"+classUrl+"/"+ methodValue,method);
					}
				}
			}
		}
	}

	private void springIOC() throws IllegalArgumentException, IllegalAccessException {
		if(instanceMaps.size() == 0 ){
			return ;
		}
		for(Map.Entry<String, Object> entry:instanceMaps.entrySet()){
			Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
			for(Field field : declaredFields){
				if(field.isAnnotationPresent(Qualifier.class)){
					String  qualifiervalue = ((Qualifier)field.getAnnotation(Qualifier.class)).value();
					//拿到私有属性
					field.setAccessible(true);
					field.set(entry.getValue(), instanceMaps.get(qualifiervalue));
				}
			}
		}
	}

	// 通过通过基包名称来扫描项目资源Bean名称加载
	// 通过基包扫描到的权限定类名，对应的创造实例
	private void findBeansInstance() throws Exception{
		if(packeNames.size() == 0 ){
			return ;
		}
		
		for(String className: packeNames){
			Class clazz = Class.forName(className.replace(".class", ""));
			// 只实例需要的那些实例 Service Controller
			if(clazz.isAnnotationPresent(Controller.class)){
				Object crollorInstance =  clazz.newInstance();
				// 需要拿到控制层字节对象上面的注释对象里面的参数
				Controller controller = (Controller)clazz.getAnnotation(Controller.class);
				String value = controller.value();
				instanceMaps.put(value, crollorInstance);
			}else if(clazz.isAnnotationPresent(Service.class)){
				Object servicerInstance =  clazz.newInstance();
				// 需要拿到Service字节对象上面的注释对象里面的参数
				Service servicer = (Service)clazz.getAnnotation(Service.class);
				String value = servicer.value();
				instanceMaps.put(value, servicerInstance);
			}
		}
	}

	// com.xxx...xx --->  com/xxx/xxx
	private void scanBean(String basePackageName) {
		URL url = this.getClass().getClassLoader().getResource("/"+replacePath(basePackageName));
		String path = url.getFile();
		File file = new File(path);
		String[] files = file.list();
		for(String pa : files){
			File eachFile = new File(path+pa);
			if(eachFile.isDirectory()){
				 // 
				 scanBean(basePackageName+"."+eachFile.getName());
			}else{
				packeNames.add(basePackageName+"."+eachFile.getName());
				System.out.println("Spring 容器全包类扫描文件有:"+basePackageName+"."+eachFile.getName());
			}
			
			
		}
	}

	private String replacePath(String basePackageName) {
		return basePackageName.replace("\\.", "/");
	}
}
