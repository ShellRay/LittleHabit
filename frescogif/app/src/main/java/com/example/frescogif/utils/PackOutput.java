package com.example.frescogif.utils;

public class PackOutput {
	final byte[] bs;
	final int start;
	final boolean isBigEndian;
	/** �ڲ�ƫ���� */
	int innerOffset;
	
	/**
	 * ����һ��byte��
	 * @param bs Ҫ�������ֽ�����
	 * @param start ��ʼλ��
	 */
	public PackOutput(byte[] bs, int start, boolean isBigEndian) {
		this.bs = bs;
		this.start = start;
		this.isBigEndian = isBigEndian;
		this.innerOffset = 0;
	}
	
	public void writeByte(byte n) {
		bs[start + innerOffset++] = (byte)n;
	}
	
	public void writeLong(long n) {
		if (isBigEndian) {
			bs[start + innerOffset++] = (byte) ((n >> 56) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 48) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 40) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 32) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 24) & 0xFF);
	        bs[start + innerOffset++] = (byte) ((n >> 16) & 0xFF);
	        bs[start + innerOffset++] = (byte) ((n >> 8) & 0xFF);
	        bs[start + innerOffset++] = (byte) (n & 0xFF);
		}
		else {
			bs[start + innerOffset++] = (byte) (n & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 8) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 16) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 24) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 32) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 40) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 48) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 56) & 0xFF);
		}
	}
	
	public void writeUnsignedInt(long n) {
		if (isBigEndian) {
			bs[start + innerOffset++] = (byte) ((n >> 24) & 0xFF);
	        bs[start + innerOffset++] = (byte) ((n >> 16) & 0xFF);
	        bs[start + innerOffset++] = (byte) ((n >> 8) & 0xFF);
	        bs[start + innerOffset++] = (byte) (n & 0xFF);
		}
		else {
			bs[start + innerOffset++] = (byte) (n & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 8) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 16) & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 24) & 0xFF);
		};
	}
	
	public void writeUnsignedShort(int n) {
		if (isBigEndian) {
			bs[start + innerOffset++] = (byte) ((n >> 8) & 0xFF);
			bs[start + innerOffset++] = (byte) (n & 0xFF);
		}
		else {
			bs[start + innerOffset++] = (byte) (n & 0xFF);
			bs[start + innerOffset++] = (byte) ((n >> 8) & 0xFF);
		};
	}
	
	public void writeString(String str){
		final byte[] inBytes = str.getBytes();
		writeUnsignedShort(inBytes.length);
		System.arraycopy(inBytes, 0, bs, start + innerOffset, inBytes.length);
		innerOffset+= inBytes.length;
	}

	/**
	 * д��һ��byte����
	 * @param inBs Ҫд��� byte����
	 */
	public void writeBytes(byte[] inBs) {
		writeBytes(inBs, 0, inBs.length);
	}
	
	/**
	 * д��һ��byte����
	 * @param inBs Ҫд��� byte����
	 * @param offset ����ƫ����
	 * @param length Ҫд��ĳ���
	 */
	public void writeBytes(byte[] inBs, int offset, int length) {
		System.arraycopy(inBs, offset, bs, start + innerOffset, length);
		innerOffset += length;
	}

	public byte[] getBytes() {
		return bs;
	}
	
//	public void writeString(String str, int length){
//		writeBytes(str.getBytes(), length);
//	}
//	
//	public void writeBytes(byte[] inBytes, int length) {
//		System.arraycopy(inBytes, 0, bs, start + innerOffset, inBytes.length);
//		for(int i = inBytes.length; i < length; i++) {
//			bs[start + innerOffset + i] = 0;
//		}
//		innerOffset+=length;
//	}
}
