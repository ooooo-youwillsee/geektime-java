package com.ooooo.loader;

import com.ooooo.launcher.XarFile;

/**
 * @author leizhijie
 * @since 2021/3/5 15:56
 */
public class XarClassLoader extends ClassLoader {
	
	private final XarFile xarFile;
	
	public XarClassLoader(XarFile xarFile) {
		this.xarFile = xarFile;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("findClass: " + name);
		String xclassName = name.replaceAll("\\.", "/") + XarFile.XLASS_EXTENSION;
		
		if (xarFile.getXclassNames().contains(xclassName)) {
			byte[] bytes = xarFile.readXclass(xclassName);
			return defineClass(name, bytes, 0, bytes.length);
		}
		return super.findClass(name);
	}
}
