package com.ooooo.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.ooooo.rpcfx.api.*;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;

public class RpcfxInvocationHandler implements InvocationHandler {
	
	private final Class<?> serviceClass;
	private final String url;
	private final Filter[] filters;
	private final List<ExceptionHandler> exceptionHandlers;
	private final ExecutorService executorService = Executors.newFixedThreadPool(8);
	
	public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url, List<ExceptionHandler> exceptionHandlers, Filter... filters) {
		this.serviceClass = serviceClass;
		this.url = url;
		this.exceptionHandlers = exceptionHandlers;
		this.filters = filters;
	}
	
	// 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
	// int byte char float double long bool
	// [], data class
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
		
		// 加filter地方之二
		// mock == true, new Student("hubao");
		
		RpcfxRequest request = new RpcfxRequest();
		request.setServiceClass(this.serviceClass.getName());
		request.setMethod(method.getName());
		request.setParams(params);
		
		if (null != filters) {
			for (Filter filter : filters) {
				if (!filter.filter(request)) {
					return null;
				}
			}
		}
		
		RpcfxResponse response = post(request, url);
		
		// 加filter地方之三
		// Student.setTeacher("cuijing");
		
		// 这里判断response.status，处理异常
		// 考虑封装一个全局的RpcfxException
		if (!response.isStatus()) {
			RpcfxException rpcfxException = new RpcfxException(response.getException().getMessage());
			for (ExceptionHandler exceptionHandler : exceptionHandlers) {
				response = exceptionHandler.handle(response, proxy, method, params, rpcfxException);
				if (response != null) {
					break;
				}
			}
			if (!response.isStatus()) {
				throw new RpcfxException(response.getException().getMessage());
			}
		}
		
		// response.getResult() --> JSONObject
		return JSON.parseObject(response.getResult().toString(), method.getReturnType());
	}
	
	// use okhttpclient
	
	
	// use netty client
	@SneakyThrows
	private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
		Holder<RpcfxResponse> holder = new Holder<>();
		//OKHttp3Client.post(req, url, holder);
		executorService.submit(() -> Netty4Client.post(req, url, holder));
		return holder.getData();
	}
	
}