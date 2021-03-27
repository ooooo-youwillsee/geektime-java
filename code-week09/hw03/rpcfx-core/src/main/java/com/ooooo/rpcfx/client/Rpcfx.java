package com.ooooo.rpcfx.client;


import com.ooooo.rpcfx.api.ExceptionHandler;
import com.ooooo.rpcfx.api.Filter;
import com.ooooo.rpcfx.api.LoadBalancer;
import com.ooooo.rpcfx.api.Router;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Rpcfx {
	
	private static final List<ExceptionHandler> exceptionHandlers = new CopyOnWriteArrayList<>();
	
	public static void registerExceptionHandler(ExceptionHandler exceptionHandler) {
		exceptionHandlers.add(exceptionHandler);
	}
	
	public static <T, filters> T createFromRegistry(final Class<T> serviceClass, final String zkUrl, Router router, LoadBalancer loadBalance, Filter filter) {
		
		// 加filte之一
		
		// curator Provider list from zk
		List<String> invokers = new ArrayList<>();
		// 1. 简单：从zk拿到服务提供的列表
		// 2. 挑战：监听zk的临时节点，根据事件更新这个list（注意，需要做个全局map保持每个服务的提供者List）
		
		List<String> urls = router.route(invokers);
		
		String url = loadBalance.select(urls); // router, loadbalance
		
		return (T) create(serviceClass, url, filter);
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T create(final Class<T> serviceClass, final String url, Filter... filters) {
		
		// 0. 替换动态代理 -> AOP
		return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url, exceptionHandlers, filters));
		
	}

}
