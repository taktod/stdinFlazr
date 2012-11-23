package com.ttProject.flazr.io.flv;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flazr.io.flv.FlvAtom;
import com.flazr.io.flv.FlvReader;
import com.flazr.rtmp.RtmpMessage;
import com.flazr.rtmp.RtmpReader;
import com.flazr.rtmp.message.Metadata;
import com.flazr.rtmp.message.MetadataAmf0;
import com.ttProject.flazr.FlvPacketReader;

public class FlvLiveReader implements RtmpReader {
    private static final Logger logger = LoggerFactory.getLogger(FlvReader.class);
    private final LinkedBlockingQueue<FlvAtom> dataQueue = new LinkedBlockingQueue<FlvAtom>();
    
//    private final BufferReader in;
    private Metadata metadata;
    private int aggregateDuration;    
    private final Thread stdinThread;

    public FlvLiveReader(final String path) {
    	// FlvLiveReaderの作成の方がさきにくるので、metaデータはデフォルトとします。
    	// 標準入力を解析するThreadをつくっておく。
    	stdinThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FlvPacketReader reader = new FlvPacketReader();
//					ReadableByteChannel stdinChannel = Channels.newChannel(System.in);
					// dummy
					String targetFile = System.getProperty("user.home") + "/Sites/mario/mario.flv";
					FileChannel stdinChannel = new FileInputStream(targetFile).getChannel();
					// 実処理
					// データを確認する。
					while(true) {
						// オブジェクトを変更しないと、getPacketsがうまく動作しないみたいです。
						ByteBuffer buffer = ByteBuffer.allocate(65536);
						if(stdinChannel.read(buffer) < 0) {
							break;
						}
						buffer.flip();
						List<FlvAtom> data = reader.getPackets(buffer);
						dataQueue.addAll(data);
//						System.out.println(data);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    	stdinThread.start();
    	// threadをつくって、stdinのデータを取り込みqueueにいれていく動作をつくっておく。
    	logger.info("FlvLiveReader");
        metadata = new MetadataAmf0("onMetaData");
        logger.debug("flv file metadata: {}", metadata);
    }
    @Override
    public Metadata getMetadata() {
    	logger.info("getMetaData");
        return metadata;
    }

    @Override
    public RtmpMessage[] getStartMessages() {
    	logger.info("getStartMessages");
        return new RtmpMessage[] { metadata };
    }

    @Override
    public void setAggregateDuration(int targetDuration) {
    	logger.info("setAggregateDuration" + targetDuration);
        this.aggregateDuration = targetDuration;
    }

    @Override
    @Deprecated // ライブ動作なので、シーク禁止 時間の位置を計算する必要はないと思われます。(まぁあってもいいけど。)
    public long getTimePosition() {
    	throw new RuntimeException("getTimePositionが呼ばれました。");
/*        final int time;
        if(hasNext()) {
            time = next().getHeader().getTime();
            prev();
//        } else if(hasPrev()) {
//            time = prev().getHeader().getTime();
//            next();
        } else {
            throw new RuntimeException("not seekable");
        }
        return time; */
    }

    @Override
    @Deprecated // ライブ動作させるのでシークは禁止
    public long seek(final long time) {
    	throw new RuntimeException("seekが呼ばれました。");
    }

    @Override
    public boolean hasNext() {        
    	logger.info("hasNext");
    	return true; // 絶対にあることにしておく。
    }

    @Override
    public RtmpMessage next() {
    	logger.info("next");
        if(aggregateDuration <= 0) {
        	// aggregateDirationは0であることが見越されます。
        	// flvAtomを生成して、linkedBlockingQueueにいれておき、popして応答すればそれでいいと思う。
        	try {
        		return dataQueue.take();
        	}
        	catch (Exception e) {
        		logger.error("error", e);
        		throw new RuntimeException("takeに失敗しました。");
			}
        }
        else {
        	throw new RuntimeException("aggregateDurationが0よりおおきかった。");
        }
    }

    @Override
    public void close() {
    	logger.info("close");
    	stdinThread.interrupt();
    }

}
