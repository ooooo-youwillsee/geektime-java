package com.ooooo.test;

import java.lang.reflect.Method;

/**
 * @author leizhijie
 * @since 2021/3/5 21:23
 */
public class Hello1 {
	
	public void hello() throws Exception {
		System.out.println("invoke hello1");
		Class<?> hello2Clazz = Class.forName("com.ooooo.test.Hello2");
		Object o = hello2Clazz.newInstance();
		Method helloMethod = hello2Clazz.getMethod("hello");
		helloMethod.setAccessible(true);
		helloMethod.invoke(o);
	}
}
