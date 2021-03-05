package com.ooooo.main;

import java.lang.reflect.Method;

/**
 * @author leizhijie
 * @since 2021/3/5 16:17
 *
 */
public class MainMethodRunner {
	
	private final String mainClassName;
	private final String[] args;
	
	public MainMethodRunner(String mainClassName, String[] args) {
		this.mainClassName = mainClassName;
		this.args = args;
	}
	
	public void run() {
		try {
			Class<?> mainClass = Class.forName(mainClassName, false, Thread.currentThread().getContextClassLoader());
			Method mainMethod = mainClass.getMethod("main", String[].class);
			mainMethod.setAccessible(true);
			mainMethod.invoke(this, (Object) args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
