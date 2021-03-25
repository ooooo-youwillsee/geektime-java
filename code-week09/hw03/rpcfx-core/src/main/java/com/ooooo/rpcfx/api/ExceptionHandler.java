package com.ooooo.rpcfx.api;

import java.lang.reflect.Method;

/**
 * @author leizhijie
 * @since 2021/3/25 22:22
 */
public interface ExceptionHandler {
	
	RpcfxResponse handle(RpcfxResponse response, Object proxy, Method method, Object[] args, RpcfxException exception);
}
