package com.ooooo.rpcfx.demo.provider;

import com.ooooo.rpcfx.api.RpcfxRequest;
import com.ooooo.rpcfx.api.RpcfxResolver;
import com.ooooo.rpcfx.api.RpcfxResponse;
import com.ooooo.rpcfx.api.ServiceProviderDesc;
import com.ooooo.rpcfx.demo.api.OrderService;
import com.ooooo.rpcfx.demo.api.UserService;
import com.ooooo.rpcfx.server.RpcfxInvoker;
import java.net.InetAddress;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RpcfxServerApplication {
	
	
	public static void main(String[] args) throws Exception {
		
		// start zk client
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181").namespace("rpcfx").retryPolicy(retryPolicy).build();
		client.start();
		
		// register service
		// xxx "io.kimmking.rpcfx.demo.api.UserService"
		
		String userService = "com.ooooo.rpcfx.demo.api.UserService";
		registerService(client, userService);
		String orderService = "com.ooooo.rpcfx.demo.api.OrderService";
		registerService(client, orderService);
		
		// 进一步的优化，是在spring加载完成后，从里面拿到特定注解的bean，自动注册到zk
		
		SpringApplication.run(RpcfxServerApplication.class, args);
	}
	
	private static void registerService(CuratorFramework client, String service) throws Exception {
		ServiceProviderDesc userServiceSesc = ServiceProviderDesc.builder()
		                                                         .host(InetAddress.getLocalHost().getHostAddress())
		                                                         .port(8088).serviceClass(service).build();
		// String userServiceSescJson = JSON.toJSONString(userServiceSesc);
		
		try {
			if (null == client.checkExists().forPath("/" + service)) {
				client.create().withMode(CreateMode.PERSISTENT).forPath("/" + service, "service".getBytes());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		String nodePath = "/" + service + "/" + userServiceSesc.getHost() + "_" + userServiceSesc.getPort();
		if (null == client.checkExists().forPath(nodePath)) {
			client.create().withMode(CreateMode.EPHEMERAL).forPath(nodePath, "provider".getBytes());
		}
	}
	
	@Autowired
	RpcfxInvoker invoker;
	
	@PostMapping("/")
	public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
		return invoker.invoke(request);
	}
	
	@Bean
	public RpcfxInvoker createInvoker(@Autowired RpcfxResolver resolver) {
		return new RpcfxInvoker(resolver);
	}
	
	@Bean
	public RpcfxResolver createResolver() {
		return new DemoResolver();
	}
	
	// 能否去掉name
	//
	
	// annotation
	
	
	@Bean
	public UserService createUserService() {
		return new UserServiceImpl();
	}
	
	@Bean
	public OrderService createOrderService() {
		return new OrderServiceImpl();
	}
	
}
