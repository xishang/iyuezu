package com.iyuezu.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iyuezu.common.beans.User;
import com.iyuezu.redis.RedisDao;
import com.iyuezu.redis.SystemConstants;

@Repository
public class UserRedisImpl {

	@Autowired
	private RedisDao redisDao;

	public boolean setUserByToken(String accessToken, User user) {
		String redisKey = "token_" + accessToken;
		return redisDao.setEx(redisKey, user, SystemConstants.TOKEN_EXPIRE_TIME);
	}

	public User getUserByToken(String accessToken) {
		String redisKey = "token_" + accessToken;
		return (User) redisDao.get(redisKey);
	}

	public boolean refreshTokenExpireTime(String accessToken) {
		String redisKey = "token_" + accessToken;
		return redisDao.expire(redisKey, SystemConstants.TOKEN_EXPIRE_TIME);
	}

	public boolean removeAccessToken(String accessToken) {
		String redisKey = "token_" + accessToken;
		return redisDao.del(redisKey);
	}

	public boolean setVerifyCode(String uuid, String verifyCode) {
		String redisKey = "verify_" + uuid;
		return redisDao.setEx(redisKey, verifyCode, SystemConstants.VERIFY_EXPIRE_TIME);
	}

	public String getVerifyCode(String uuid) {
		String redisKey = "verify_" + uuid;
		return (String) redisDao.get(redisKey);
	}

}
