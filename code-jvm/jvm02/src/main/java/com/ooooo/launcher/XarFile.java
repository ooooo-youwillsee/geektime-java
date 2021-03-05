package com.ooooo.launcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import lombok.SneakyThrows;

/**
 * @author leizhijie
 * @since 2021/3/5 16:42
 */
public class XarFile {
	
	private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
	private static final String START_CLASS_ATTRIBUTE = "Start-Class";
	public static final String XLASS_EXTENSION = ".xlass";
	
	private final Set<String> xclassNames;
	private final JarFile jarFile;
	
	private Manifest manifest;
	
	public XarFile(File xarFile) throws Exception {
		xclassNames = new HashSet<>(64);
		this.jarFile = new JarFile(xarFile);
		
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String name = jarEntry.getName();
			System.out.printf("jarEntry: [ name = %s ] %n", name);
			if (name.endsWith(XLASS_EXTENSION)) {
				// 去掉.xlass后缀
				xclassNames.add(name);
			} else if (name.equals(MANIFEST_NAME)) {
				this.manifest = new Manifest(jarFile.getInputStream(jarEntry));
			}
		}
		System.out.println();
		
		System.out.println("xclassNames: ");
		for (String xclassName : xclassNames) {
			System.out.println(xclassName);
		}
		System.out.println();
	}
	
	public Manifest getManifest() {
		return manifest;
	}
	
	public String getMainClassName() {
		return manifest.getMainAttributes().getValue(START_CLASS_ATTRIBUTE);
	}
	
	public Set<String> getXclassNames() {
		return xclassNames;
	}
	
	// name -> com.ooooo.test.Hello1
	@SneakyThrows
	public byte[] readXclass(String name) {
		JarEntry jarEntry = jarFile.getJarEntry(name);
		InputStream inputStream = jarFile.getInputStream(jarEntry);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		while (true) {
			int read = inputStream.read(buf);
			if (read == -1) {
				break;
			}
			outputStream.write(buf, 0, read);
		}
		return outputStream.toByteArray();
	}
}


