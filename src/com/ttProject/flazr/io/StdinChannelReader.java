package com.ttProject.flazr.io;

import org.jboss.netty.buffer.ChannelBuffer;

import com.flazr.io.BufferReader;

public class StdinChannelReader implements BufferReader {
	// はじめのデータがflvHeaderであることを確認する必要がある。
	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long position() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void position(long position) {
		// TODO Auto-generated method stub

	}

	@Override
	public ChannelBuffer read(int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] readBytes(int size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int readInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long readUnsignedInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
