package com.iyuezu.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	private static final String ACCOUNT_REGEX = "^\\w{3,25}$";
	private static final String PASSWORD_REGEX = "^\\w{3,25}$";
	private static final String PHONE_REGEX = "^1[3,5,8]\\d{9}$";
	private static final String EMAIL_REGEX = "^([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+(\\.[a-zA-Z]{2,3})+$";
	private static final String IDENTITY_REGEX = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

	public static boolean checkAccount(String account) {
		return checkRegex(account, ACCOUNT_REGEX);
	}

	public static boolean checkPassword(String password) {
		return checkRegex(password, PASSWORD_REGEX);
	}

	public static boolean checkPhone(String phone) {
		return checkRegex(phone, PHONE_REGEX);
	}

	public static boolean checkEmail(String email) {
		return checkRegex(email, EMAIL_REGEX);
	}

	public static boolean checkIdentity(String identity) {
		return checkRegex(identity, IDENTITY_REGEX);
	}

	private static boolean checkRegex(String content, String regex) {
		if (content == null) {
			return false;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}
	
}
