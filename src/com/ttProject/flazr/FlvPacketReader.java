package com.ttProject.flazr;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffers;

import com.flazr.io.flv.FlvAtom;
import com.flazr.util.Utils;

/**
 * byteBufferの中身を確認するReader
 * @author taktod
 */
public class FlvPacketReader {
	private boolean firstChecked = false;
	private ByteBuffer buffer = null;
	public List<FlvAtom> getPackets(ByteBuffer data) {
		if(buffer != null) {
			int length = buffer.remaining() + data.remaining();
			ByteBuffer newBuffer = ByteBuffer.allocate(length);
			newBuffer.put(buffer);
			buffer = newBuffer;
			buffer.put(data);
			buffer.flip();
			System.out.println(buffer.remaining());
		}
		else {
			buffer = data;
		}
		List<FlvAtom> result = new ArrayList<FlvAtom>();
		while(buffer.remaining() > 0) {
			FlvAtom flvAtom = analizeData(buffer);
			if(flvAtom != null) {
				result.add(flvAtom);
			}
			else {
				break;
			}
		}
		return result;
	}
	/**
	 * 内容を解析します
	 * @return
	 */
	private FlvAtom analizeData(ByteBuffer buffer) {
		if(!firstChecked) {
			if(buffer.remaining() < 13) {
				// データがたりないので、次のデータ待ち
				return null;
			}
			byte[] flvHeader = new byte[13];
			buffer.get(flvHeader);
			System.out.println(Utils.toHex(flvHeader, true));
			// 内容を解析する。46 4C 56 01 05 00 00 00 09 00 00 00 00 
			if(flvHeader[0] == 0x46
			&& flvHeader[1] == 0x4C
			&& flvHeader[2] == 0x56
			&& flvHeader[3] == 0x01
//			&& flvHeader[4] == 0x05
			&& flvHeader[5] == 0x00
			&& flvHeader[6] == 0x00
			&& flvHeader[7] == 0x00
			&& flvHeader[8] == 0x09
			&& flvHeader[9] == 0x00
			&& flvHeader[10] == 0x00
			&& flvHeader[11] == 0x00
			&& flvHeader[12] == 0x00) {
				firstChecked = true;
			}
			else {
				throw new RuntimeException("flvのヘッダ情報解析がうまくいきませんでした。");
			}
		}
		if(buffer.remaining() < 11) {
			return null;
		}
		// データの続きを解析する。
		int position = buffer.position();
		System.out.println(position);
		// 11バイト読み込んでデータを調べ上げる。
		byte[] packetHeader = new byte[11];
		buffer.get(packetHeader);
		System.out.println(Utils.toHex(packetHeader, true));
		int size = getSizeFromHeader(packetHeader);
		System.out.println(11+ size + 4);
		if(buffer.remaining() < size + 4) {
			buffer.position(position);
			return null;
		}
		byte[] data = new byte[11 + size + 4];
		buffer.position(position);
		buffer.get(data);
		System.out.println(buffer.position());
		return new FlvAtom(ChannelBuffers.copiedBuffer(data));
	}
	private int getSizeFromHeader(byte[] header) {
		return (((header[1] & 0xFF) << 16) + ((header[2] & 0xFF) << 8) + (header[3] & 0xFF));
	}
}
