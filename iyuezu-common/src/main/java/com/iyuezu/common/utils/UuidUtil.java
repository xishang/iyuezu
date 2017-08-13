package com.iyuezu.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class UuidUtil {

	public static String getUuidByTimestamp(int randSize) { // 时间戳+randSize位随机数
		Random r = new Random();
		int randomNum = r.nextInt((int) Math.pow(10, randSize));
		Long timestamp = new Date().getTime();
		return timestamp + formatNumberLength(randomNum, randSize);
	}

	public static String formatNumberLength(int num, int length) {
		String formatNumber = String.valueOf(num);
		if (formatNumber.length() < length) {
			int incSize = length - formatNumber.length();
			for (int i = 0; i < incSize; i++) {
				formatNumber = "0" + formatNumber;
			}
		}
		return formatNumber.substring(0, length);
	}

	public static String getDateFormatUuid(int randSize) {
		Random r = new Random();
		int randomNum = r.nextInt((int) Math.pow(10, randSize));
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String formatTime = formater.format(new Date());
		return formatTime + formatNumberLength(randomNum, randSize);
	}
	
}
