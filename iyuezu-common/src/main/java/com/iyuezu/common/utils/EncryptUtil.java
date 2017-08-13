package com.iyuezu.common.utils;

import java.security.MessageDigest;

import org.springframework.util.DigestUtils;

public class EncryptUtil {

	private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f" };

	public static String byteArrayToString(byte[] b) {
		StringBuffer bths = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			bths.append(byteToHexString(b[i]));
		}
		return bths.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String ToMD5(String origin) {
		String result = null;
		try {
			result = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byteArrayToString(md.digest(result.getBytes()));
		} catch (Exception e) {
		}
		return result;
	}

	public static String ToMD5(String origin, String charset) {
		String result = null;
		try {
			result = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byteArrayToString(md.digest(result.getBytes(charset)));
		} catch (Exception e) {
		}
		return result;
	}

	public static String md5Hex(String origin) {
		String result = null;
		try {
			return DigestUtils.md5DigestAsHex(origin.getBytes());
		} catch (Exception e) {
		}
		return result;
	}

	public static String md5Hex(String origin, String charset) {
		String result = null;
		try {
			return DigestUtils.md5DigestAsHex(origin.getBytes(charset));
		} catch (Exception e) {
		}
		return result;
	}

}
