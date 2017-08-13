package com.iyuezu.redis;

public class SystemConstants {

	public static final String DEFAULT_CHARSET = "utf-8";

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final long CHAT_EXPIRE_TIME = 86400l; // 过期时间为1day

	public static final long TOKEN_EXPIRE_TIME = 600l; // accessToken过期时间为10min

	public static final long VERIFY_EXPIRE_TIME = 300l; // verifyCode过期时间为5min

}
