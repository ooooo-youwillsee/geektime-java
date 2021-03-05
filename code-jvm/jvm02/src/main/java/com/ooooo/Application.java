package com.ooooo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author leizhijie
 * @since 2021/3/5 16:26
 * <p>
 * 应用程序入口
 */
public class Application {
	
	public static void main(String[] args) {
		try {
			System.out.println("开始执行application方法");
			
			Class<?> clazz = Class.forName("com.ooooo.test.Hello1");
			Object o = clazz.newInstance();
			Method helloMethod = clazz.getMethod("hello");
			helloMethod.invoke(o);
			
			System.out.println("结束执行application方法");
		} catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}
