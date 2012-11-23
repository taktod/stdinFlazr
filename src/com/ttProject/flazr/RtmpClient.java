package com.ttProject.flazr;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flazr.rtmp.client.ClientOptions;
import com.flazr.util.Utils;
import com.ttProject.flazr.client.ClientPipelineFactoryEx;
import com.ttProject.flazr.io.flv.FlvLiveReader;

/**
 * rtmpの動作エントリー
 * @author taktod 
 */
public class RtmpClient {
	private static final Logger logger = LoggerFactory.getLogger(RtmpClient.class);
	
	/**
	 * メインエントリー
	 * @param args
	 */
	public static void main(String[] args) {
		final ClientOptions options = new ClientOptions();
		if(!options.parseCli(args)) {
			return;
		}
		Utils.printlnCopyrightNotice();
		final int count = options.getLoad();
		if(count == 1 && options.getClientOptionsList() == null) {
			// 単一動作のみ実行しておきます。
			if("-".equals(options.getFileToPublish())) {
				// publish fileが-の場合は標準入力データを配信するものとする。
				String targetFile = System.getProperty("user.home") + "/Sites/mario/mario.flv";
				logger.info(targetFile);
				options.setFileToPublish(null); // 切り替えるためには、nullをいれておく必要があるみたい。
				// 動作ターゲットを切り替えておく。
				options.setReaderToPublish(new FlvLiveReader(targetFile));
			}
			connect(options);
			return;
		}
		logger.error("単一プロセスのみ許可しています。");
	}
	/**
	 * 接続動作
	 * @param options
	 */
	public static void connect(final ClientOptions options) {  
		final ClientBootstrap bootstrap = getBootstrap(Executors.newCachedThreadPool(), options);
		final ChannelFuture future = bootstrap.connect(new InetSocketAddress(options.getHost(), options.getPort()));
		future.awaitUninterruptibly();
		if(!future.isSuccess()) {
			// future.getCause().printStackTrace();
			logger.error("error creating client connection: {}", future.getCause().getMessage());
		}
		future.getChannel().getCloseFuture().awaitUninterruptibly(); 
		bootstrap.getFactory().releaseExternalResources();
	}
	/**
	 * 起動作成
	 * @param executor
	 * @param options
	 * @return
	 */
	private static ClientBootstrap getBootstrap(final Executor executor, final ClientOptions options) {
		final ChannelFactory factory = new NioClientSocketChannelFactory(executor, executor);
		final ClientBootstrap bootstrap = new ClientBootstrap(factory);
		// clientPipelineFactoryをオーバーライドすることで、独自定義動作させます。
		bootstrap.setPipelineFactory(new ClientPipelineFactoryEx(options));
		bootstrap.setOption("tcpNoDelay" , true);
		bootstrap.setOption("keepAlive", true);
		return bootstrap;
	}
}
