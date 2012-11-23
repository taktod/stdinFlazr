package com.ttProject.flazr.io.flv;

import com.flazr.rtmp.RtmpMessage;
import com.flazr.rtmp.RtmpReader;
import com.flazr.rtmp.message.Metadata;

public class FlvLiveReader implements RtmpReader {

	@Override
	public Metadata getMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RtmpMessage[] getStartMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAggregateDuration(int targetDuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getTimePosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long seek(long timePosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RtmpMessage next() {
		// TODO Auto-generated method stub
		return null;
	}

}
