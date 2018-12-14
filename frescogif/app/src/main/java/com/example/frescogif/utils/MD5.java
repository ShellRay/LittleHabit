package com.example.frescogif.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description MD5加密类
 *
 * @author 薛文超
 * @modify
 * @version 1.0.0
 */
public class MD5 {
	public static final String TAG = "MD5";

	private static final String HASH_ALGORITHM = "MD5";
	private static final int RADIX = 16; // 16进制

	public static String generate(String val) {
		byte[] md5 = getMD5(val.getBytes());
		BigInteger bi = new BigInteger(1, md5).abs();
		return bi.toString(RADIX);
	}

	private static byte[] getMD5(byte[] data) {
		byte[] hash = null;
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(data);
			hash = digest.digest();
		}
		catch (NoSuchAlgorithmException e) {
//			LogUtils.printStackTrace(e);
		}
		return hash;
	}
	
	public static void main(String[] args) {
		String test = "uid=89187079&key=ux7YIWRVw0";
		String md5 = generate(test);
		System.out.println(md5);
	}

	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
