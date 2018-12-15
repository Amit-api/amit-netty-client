package com.amitapi.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class NettyHttpClient {
	private static EventLoopGroup group;

	static {
		group = new NioEventLoopGroup();
	}

	private NettyHttpClientConfig config;

	public NettyHttpClient(NettyHttpClientConfig config) {
		this.config = config;
	}

	protected void postJson(String path, String json,
			NettyHttpClientHandler handler) throws InterruptedException {
		ByteBuf buffer = Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
		HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
				HttpMethod.POST, path, buffer);

		request.headers().set(HttpHeaderNames.HOST, config.getUri().getHost());
		request.headers().set(HttpHeaderNames.CONNECTION,
				HttpHeaderValues.CLOSE);
		request.headers().set(HttpHeaderNames.ACCEPT_ENCODING,
				HttpHeaderValues.GZIP);
		request.headers().set(HttpHeaderNames.CONTENT_TYPE,
				HttpHeaderValues.APPLICATION_JSON);
		request.headers().set(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());

		send(request, handler);
	}

	protected void send(HttpRequest request, NettyHttpClientHandler handler)
			throws InterruptedException {
		Bootstrap b = new Bootstrap();
		b.group(group)
				.channel(NioSocketChannel.class)
				.handler(
						new NettyHttpClientInitializer(config
								.getMaxResponseSize(), handler));

		String host = config.getUri().getHost();
		if (host == null || host.isEmpty()) {
			host = "127.0.0.1";
		}

		int port = config.getUri().getPort();
		if (port < 0) {
			port = 80;
		}

		Channel ch = b.connect(host, port).sync().channel();
		ch.writeAndFlush(request);
	}
}
