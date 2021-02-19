package com.ooooo.hw1;

/**
 * @author leizhijie
 * @since 2021/2/19 11:08
 * 运行命令 javac Hello.java , javap -c -v Hello
 */
public class Hello {
	
	public static void main(String[] args) {
		int a = 3, b = 4;
		System.out.println(a + b);
		System.out.println(a - b);
		System.out.println(a * b);
		System.out.println(a / b);
		System.out.println(a % b);
		
		float c = 5.0f, d = 6.0f;
		System.out.println(c + d);
		System.out.println(c - d);
		System.out.println(c * d);
		System.out.println(c / d);
		
		double e = 7.0, f = 8.0;
		System.out.println(e + f);
		System.out.println(e - f);
		System.out.println(e * f);
		System.out.println(e / f);
		
		if (a < b) {
			System.out.println("a < b");
		} else {
			System.out.println("a > b");
		}
		
		int sum = 0;
		for (int i = 0; i < 10; i++) {
			sum += i;
		}
		System.out.println("sum =" + sum);
	}
}
