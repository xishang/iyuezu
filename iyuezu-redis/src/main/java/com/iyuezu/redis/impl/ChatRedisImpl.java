package com.iyuezu.redis.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.iyuezu.common.utils.StringUtil;
import com.iyuezu.redis.RedisDao;
import com.iyuezu.redis.SystemConstants;

@Repository
public class ChatRedisImpl {

	@Autowired
	private RedisDao redisDao;

	public boolean setChatUsers(int chatId, List<String> uuidList) {
		String uuids = "";
		for (String uuid : uuidList) {
			uuids += uuid + ",";
		}
		uuids = uuids.substring(0, uuids.length() - 1);
		return redisDao.setEx("chat_" + chatId, uuids, SystemConstants.CHAT_EXPIRE_TIME);
	}

	public List<String> getChatUsers(int chatId) {
		String redisKey = "chat_" + chatId;
		String uuids = (String) redisDao.get(redisKey);
		if (StringUtil.isEmpty(uuids)) {
			return null;
		} else {
			return Arrays.asList(uuids.split(","));
		}
	}

}
