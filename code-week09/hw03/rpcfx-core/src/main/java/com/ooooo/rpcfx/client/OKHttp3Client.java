package com.ooooo.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.ooooo.rpcfx.api.RpcfxRequest;
import com.ooooo.rpcfx.api.RpcfxResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author leizhijie
 * @since 2021/3/27 17:08
 */
public class OKHttp3Client {
	
	public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");
	
	public static void post(RpcfxRequest req, String url, Holder<RpcfxResponse> holder) throws Throwable {
		String reqJson = JSON.toJSONString(req);
		System.out.println("req json: " + reqJson);
		
		// 1.可以复用client
		OkHttpClient client = new OkHttpClient();
		final Request request = new Request.Builder()
				.url(url)
				.post(RequestBody.create(JSONTYPE, reqJson))
				.build();
		String respJson = client.newCall(request).execute().body().string();
		System.out.println("resp json: " + respJson);
		holder.setData(JSON.parseObject(respJson, RpcfxResponse.class));
	}
}
