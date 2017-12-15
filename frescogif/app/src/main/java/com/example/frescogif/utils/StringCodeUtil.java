package com.example.frescogif.utils;


public class StringCodeUtil {
	
	/**
	 * �� hex ��ʾ���ַ���ת��Ϊ  byte ����
	 * @param hexString
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexString)  {
		byte[] result = new byte[hexString.length() / 2];
		for (int i = 0; i < result.length; i++) {
			try {
				result[i] = (byte) ((hexToByte(hexString.charAt(i * 2)) << 4) + hexToByte(hexString.charAt(i * 2 + 1)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static int hexToByte(char ch) throws Exception {
		if (ch >= '0' && ch <= '9'){
			return ch - '0';
		}
		if (ch >= 'a' && ch <= 'f' ) {
			return ch - 'a' + 10;
		} 
		if (ch >= 'A' && ch <= 'F') {
			return ch - 'A' + 10;
		}
		throw new Exception("hex to byte Error: " + ch);
	}
	
	public static String bytesToHexString(byte[] bs) {
		StringBuilder buf = new StringBuilder(bs.length * 2);
		for (byte b : bs) {
			if ((b & 0xff) < 16) {
				buf.append("0");
			}
			buf.append(Long.toString(b & 0xff, 16));
		}
		return buf.toString();
	}
}
