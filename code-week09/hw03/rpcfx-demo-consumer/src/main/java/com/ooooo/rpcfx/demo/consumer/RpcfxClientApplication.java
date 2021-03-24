package com.ooooo.rpcfx.demo.consumer;

import com.ooooo.rpcfx.api.Filter;
import com.ooooo.rpcfx.api.LoadBalancer;
import com.ooooo.rpcfx.api.Router;
import com.ooooo.rpcfx.api.RpcfxRequest;
import com.ooooo.rpcfx.client.Rpcfx;
import com.ooooo.rpcfx.demo.api.Order;
import com.ooooo.rpcfx.demo.api.OrderService;
import com.ooooo.rpcfx.demo.api.User;
import com.ooooo.rpcfx.demo.api.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RpcfxClientApplication {
	
	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//
	
	public static void main(String[] args) {
		
		// UserService service = new xxx();
		// service.findById
		
		UserService userService = Rpcfx.create(UserService.class, "http://localhost:8088/");
		User user = userService.findById(1);
		System.out.println("find user id=1 from server: " + user.getName());
		
		OrderService orderService = Rpcfx.create(OrderService.class, "http://localhost:8088/");
		Order order = orderService.findOrderById(1992129);
		System.out.printf("find order name=%s, amount=%f%n", order.getName(), order.getAmount());
		
		//
		//UserService userService2 = Rpcfx.createFromRegistry(UserService.class, "localhost:2181", new TagRouter(), new RandomLoadBalancer(), new CuicuiFilter());
		
		//		SpringApplication.run(RpcfxClientApplication.class, args);
	}
	
	private static class TagRouter implements Router {
		
		@Override
		public List<String> route(List<String> urls) {
			return urls;
		}
	}
	
	private static class RandomLoadBalancer implements LoadBalancer {
		
		@Override
		public String select(List<String> urls) {
			return urls.get(0);
		}
	}
	
	@Slf4j
	private static class CuicuiFilter implements Filter {
		
		@Override
		public boolean filter(RpcfxRequest request) {
			log.info("filter {} -> {}", this.getClass().getName(), request.toString());
			return true;
		}
	}
}



