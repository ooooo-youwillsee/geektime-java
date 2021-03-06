package com.ooooo.launcher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import lombok.SneakyThrows;

/**
 * @author leizhijie
 * @since 2021/3/5 16:42
 */
@SuppressWarnings("DuplicatedCode")
public class XarFile {
	
	private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
	private static final String START_CLASS_ATTRIBUTE = "Start-Class";
	public static final String XLASS_EXTENSION = ".xlass";
	
	private final Set<String> xclassNames;
	private final JarFile rootFile;
	
	private Manifest manifest;
	
	public XarFile(File xarFile) throws Exception {
		this.rootFile = new JarFile(xarFile);
		this.xclassNames = new HashSet<>(64);
		read(new FileInputStream(xarFile), "", 0);
	}
	
	public void read(InputStream in, String parentDir, int depth) throws Exception {
		JarInputStream jarInputStream = new JarInputStream(in);
		while (true) {
			JarEntry entry = jarInputStream.getNextJarEntry();
			if (entry == null) break;
			if (isMacFile(entry)) continue;
			
			String name = entry.getName();
			println(depth, String.format("entry: [ name = %s ]", name));
			
			if (name.endsWith(XLASS_EXTENSION)) {
				xclassNames.add(name);
			} else if (name.equals(MANIFEST_NAME)) {
				this.manifest = new Manifest(jarInputStream);
				println(depth, START_CLASS_ATTRIBUTE + ": " + this.manifest.getMainAttributes().getValue(START_CLASS_ATTRIBUTE));
			} else if (name.endsWith(".xar")) {
				String scanXar = parentDir + name + "/";
				println(depth + 1, String.format("start scan: %s \n", scanXar));
				// 递归调用
				read(jarInputStream, scanXar, depth + 1);
				println(depth + 1, String.format("end scan: %s \n", scanXar));
			}
		}
		System.out.println();
		
		println(depth, "xclassNames: ");
		for (String xclassName : xclassNames) {
			println(depth, xclassName);
		}
		System.out.println();
	}
	
	// 过滤mac特有的文件，如__MACOSX
	private boolean isMacFile(JarEntry entry) {
		String name = entry.getName();
		if (name.contains("MACOSX") || name.contains(".DS_Store")) {
			return true;
		}
		// 过滤隐藏文件
		int i = name.lastIndexOf("/");
		if (i >= 0 && i + 1 < name.length()) {
			return name.charAt(i + 1) == '.';
		}
		return false;
	}
	
	public String getMainClassName() {
		return manifest.getMainAttributes().getValue(START_CLASS_ATTRIBUTE);
	}
	
	public Set<String> getXclassNames() {
		return xclassNames;
	}
	
	// name -> com/ooooo/test/Hello1.xlass
	@SneakyThrows
	public byte[] readXclass(String name) {
		JarEntry jarEntry = rootFile.getJarEntry(name);
		InputStream inputStream = rootFile.getInputStream(jarEntry);
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
	
	private void println(int depth, String message) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth * 2; i++) {
			sb.append(" ");
		}
		System.out.println(sb.toString() + message);
	}
}


