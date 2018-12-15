package com.amitapi.netty.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

class NettyHttpClientInitializer extends ChannelInitializer<SocketChannel> {
	private final int maxResponseSize;
	private final NettyHttpClientHandler handler;

	public NettyHttpClientInitializer(int maxResponseSize, NettyHttpClientHandler handler) {
		this.maxResponseSize = maxResponseSize;
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		p.addLast(new HttpClientCodec());
		p.addLast(new HttpContentDecompressor());
		p.addLast(new HttpObjectAggregator(maxResponseSize));
		//p.addLast(new LoggingHandler(LogLevel.INFO));
		p.addLast(handler); 
	}
}
