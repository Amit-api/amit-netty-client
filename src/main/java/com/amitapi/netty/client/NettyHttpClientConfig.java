package com.amitapi.netty.client;

import java.net.URI;

public class NettyHttpClientConfig {
	private URI uri;
	private int maxResponseSize = 100000;
	
	public NettyHttpClientConfig(URI uri) {
		this.uri = uri;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public int getMaxResponseSize() {
		return maxResponseSize;
	}
}
