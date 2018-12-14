package com.example.frescogif.http;

import android.text.TextUtils;


import com.example.frescogif.utils.MD5;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignHelper {
	private static final String KEY = "LIVEMobile(2016)#$%&*";
	private static final String KEY_BILLING = "key=ux7YIWRVw0";
	
	public static void addSign(Map<String, String> sArray){
		Map<String, String> map = paraFilter(sArray);
		String str= createLinkString(map);
		String _sign=SHA1(str+"&"+KEY);
		sArray.put("livesign", _sign);
	}

	public static void addBillingSign(Map<String,String> params) {
		Map<String, String> map = paraFilter(params);
		String str = createLinkString(map);
		String md5 = MD5.generate(str + "&" + KEY_BILLING);
		params.put("sign", md5.toUpperCase());
	}
	
	 /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map<String, String> paraFilter(Map<String, String> sArray) {
    	HashMap<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) 
            return result;

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }
	
	
	/**
	 * 使用SAH1进行加密
	 *  @param str 需要加密的字符串
	 *  @return String 加密后的值
	 */
    private static String SHA1(String str){
    	if(TextUtils.isEmpty(str))
    		return "";
    	
		String value="";
		try {
    		MessageDigest md = MessageDigest.getInstance("SHA-1");
    		md.update(str.getBytes("UTF-8"));
    		byte[] result = md.digest();
    		StringBuffer sb = new StringBuffer();
    		for (byte b : result) {
    			int i = b & 0xff;
    			if (i < 0xf) {
    				sb.append(0);
    			}
    			sb.append(Integer.toHexString(i));
    		}
    		value=sb.toString().toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}
	
	 /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createLinkString(Map<String, String> params) {
    	if(params == null)
    		return null;
    	
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
}
