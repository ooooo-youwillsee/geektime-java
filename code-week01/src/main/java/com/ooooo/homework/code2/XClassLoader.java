package com.ooooo.homework.code2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * @author leizhijie
 * @since 2021/1/18 22:01
 * 必做
 */
public class XClassLoader extends ClassLoader {
	
	public static void main(String[] args) throws Exception {
		XClassLoader xClassLoader = new XClassLoader();
		Class<?> clazz = xClassLoader.findClass("Hello");
		Object o = clazz.newInstance();
		Method helloMethod = clazz.getMethod("hello");
		helloMethod.invoke(o);
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// 系统类加载器，就是applicationClassLoader
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("Hello.xlass");
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		while (true) {
			try {
				int x = inputStream.read(buff);
				if (x == -1) break;
				byteArrayOutputStream.write(buff, 0, x);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		byte[] bytes = byteArrayOutputStream.toByteArray();
		byte[] srcBytes = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			srcBytes[i] = (byte) (255 - bytes[i]);
		}
		// 利用byte[]直接来定义
		Class<?> xClass = defineClass(name, srcBytes, 0, srcBytes.length);
		return xClass;
	}
}
