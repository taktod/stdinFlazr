package com.ttProject.flazr.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.flazr.rtmp.RtmpDecoder;
import com.flazr.rtmp.RtmpEncoder;
import com.flazr.rtmp.client.ClientHandshakeHandler;
import com.flazr.rtmp.client.ClientOptions;

public class ClientPipelineFactoryEx implements ChannelPipelineFactory {
	private final ClientOptions options;
	public ClientPipelineFactoryEx(final ClientOptions options) {
		this.options = options;
	}
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("handshaker", new ClientHandshakeHandler(options));
        pipeline.addLast("decoder", new RtmpDecoder());
        pipeline.addLast("encoder", new RtmpEncoder());
        
        // 独自のClientHandlerを代わりにいれておく。
        pipeline.addLast("handler", new ClientHandlerEx(options));
		return pipeline;
	}
}
