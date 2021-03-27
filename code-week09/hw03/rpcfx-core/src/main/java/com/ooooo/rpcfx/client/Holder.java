package com.ooooo.rpcfx.client;

import lombok.Data;

@Data
public class Holder<T> {
	
	private volatile T data;
	private final Object lock = new Object();
	
	// 可以添加timeout
	public T getData() {
		synchronized (lock) {
			if (data == null) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}
	
	public void setData(T data) {
		synchronized (lock) {
			this.data = data;
			lock.notifyAll();
		}
	}
}