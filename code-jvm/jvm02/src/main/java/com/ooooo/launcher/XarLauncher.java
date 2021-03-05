package com.ooooo.launcher;

import com.ooooo.loader.XarClassLoader;
import com.ooooo.main.MainMethodRunner;
import java.io.File;
import java.net.URI;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author leizhijie
 * @since 2021/3/5 15:55
 */
public class XarLauncher {
	
	public static void main(String[] args) throws Exception {
		XarFile xarFile = createXarFile();
		XarClassLoader xarClassLoader = new XarClassLoader(xarFile);
		// 设置当前线程的classloader, 非常重要！！！
		Thread.currentThread().setContextClassLoader(xarClassLoader);
		String mainClassName = xarFile.getMainClassName();
		new MainMethodRunner(mainClassName, args).run();
	}
	
	private static XarFile createXarFile() throws Exception {
		ProtectionDomain protectionDomain = XarLauncher.class.getProtectionDomain();
		CodeSource codeSource = protectionDomain.getCodeSource();
		URI location = (codeSource != null) ? codeSource.getLocation().toURI() : null;
		String path = (location != null) ? location.getSchemeSpecificPart() : null;
		if (path == null) {
			throw new IllegalStateException("Unable to determine code source archive");
		}
		File root = new File(path);
		if (!root.exists()) {
			throw new IllegalStateException("Unable to determine code source archive from " + root);
		}
		// root就是这个xar文件
		System.out.println("file:" + root);
		System.out.println();
		return new XarFile(root);
	}
}
