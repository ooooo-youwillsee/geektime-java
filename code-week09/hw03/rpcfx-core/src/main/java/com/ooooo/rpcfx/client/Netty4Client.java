package com.ooooo.rpcfx.client;

import com.alibaba.fastjson.JSON;
import com.ooooo.rpcfx.api.RpcfxRequest;
import com.ooooo.rpcfx.api.RpcfxResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ClientCookieEncoder;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.util.CharsetUtil;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author leizhijie
 * @since 2021/3/27 17:07
 */
@Slf4j
public class Netty4Client {
	
	
	@SneakyThrows
	public static void post(RpcfxRequest req, String url, Holder<RpcfxResponse> holder) {
		byte[] bytes = JSON.toJSONString(req).getBytes(StandardCharsets.UTF_8);
		URL u = new URL(url);
		
		EventLoopGroup group = new NioEventLoopGroup(1);
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioSocketChannel.class)
			 .handler(new ChannelInitializer<SocketChannel>() {
				 @Override
				 protected void initChannel(SocketChannel ch) throws Exception {
					 ChannelPipeline p = ch.pipeline();
					 p.addLast(new HttpClientCodec());
					 p.addLast(new HttpContentDecompressor());
					 p.addLast(new SimpleChannelInboundHandler<HttpObject>() {
						 @Override
						 protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
							 if (msg instanceof HttpContent) {
								 HttpContent content = (HttpContent) msg;
								 if (content instanceof LastHttpContent) {
									 System.out.println("==========finish request===========");
									 ctx.close();
									 return;
								 }
								 String respJson = content.content().toString(CharsetUtil.UTF_8);
								 log.info("respJson = {}", respJson);
								 holder.setData(JSON.parseObject(respJson, RpcfxResponse.class));
							 }
						 }
					 });
				 }
			 });
			
			// Make the connection attempt.
			Channel ch = b.connect(u.getHost(), u.getPort()).sync().channel();
			
			// Prepare the HTTP request.
			HttpRequest request = new DefaultFullHttpRequest(
					HttpVersion.HTTP_1_1, HttpMethod.POST, u.getPath(), Unpooled.wrappedBuffer(bytes));
			request.headers().set(HttpHeaderNames.HOST, u.getHost());
			request.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
			request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
			request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
			request.headers().set(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
			
			// Set some example cookies.
			request.headers().set(
					HttpHeaderNames.COOKIE,
					ClientCookieEncoder.STRICT.encode(
							new DefaultCookie("my-cookie", "foo"),
							new DefaultCookie("another-cookie", "bar")));
			
			// Send the HTTP request.
			ch.writeAndFlush(request);
			
			// Wait for the server to close the connection.
			ch.closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// Shut down executor threads to exit.
			group.shutdownGracefully();
		}
	}
}
