package com.amitapi.netty.client;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

public abstract class NettyHttpClientHandler extends
		SimpleChannelInboundHandler<FullHttpResponse> {
}
