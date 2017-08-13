package com.iyuezu.redis;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public Long hIncrBy(final String hash, final String key, final Long delta, final Long maxValue) {
		Long result = redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriHash = SerializeUtil.serialize(hash);
				byte[] seriKey = SerializeUtil.serialize(key);
				Long value = connection.hIncrBy(seriHash, seriKey, delta);
				if (value > maxValue) { // value大于max,重新开始计数
					connection.hDel(seriHash, seriKey);
				}
				return value;
			}
		});
		return result;
	}
	
	public Serializable hGet(final String hash, final String key) {
		Serializable result = redisTemplate.execute(new RedisCallback<Serializable>() {
			public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriHash = SerializeUtil.serialize(hash);
				byte[] seriKey = SerializeUtil.serialize(key);
				byte[] seriValue = connection.hGet(seriHash, seriKey);
				return SerializeUtil.deserialize(seriValue);
			}
		});
		return result;
	}

	public boolean hSet(final String hash, final String key, final Serializable value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriHashName = SerializeUtil.serialize(hash);
				byte[] seriKey = SerializeUtil.serialize(key);
				byte[] seriValue = SerializeUtil.serialize(value);
				return connection.hSet(seriHashName, seriKey, seriValue);
			}
		});
		return result;
	}

	public boolean hDel(final String hash, final String key) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriHashName = SerializeUtil.serialize(hash);
				byte[] seriKey = SerializeUtil.serialize(key);
				Long delCount = connection.hDel(seriHashName, seriKey);
				return delCount == 1L;
			}
		});
		return result;
	}

	public boolean setEx(final String key, final Serializable value, final long seconds) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				byte[] seriValue = SerializeUtil.serialize(value);
				connection.setEx(seriKey, seconds, seriValue);
				return true;
			}
		});
		return result;
	}

	public Serializable get(final String key) {
		Serializable result = redisTemplate.execute(new RedisCallback<Serializable>() {
			public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				return SerializeUtil.deserialize(connection.get(seriKey));
			}
		});
		return result;
	}

	public boolean del(final String key) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				Long delCount = connection.del(seriKey);
				return delCount == 1L;
			}
		});
		return result;
	}

	public boolean expire(final String key, final long seconds) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				return connection.expire(seriKey, seconds);
			}
		});
		return result;
	}
	
	public boolean lPush(final String key, final Serializable value) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				byte[] seriValue = SerializeUtil.serialize(value);
				long count = connection.lPush(seriKey, seriValue);
				return count == 1;
			}
		});
		return result;
	}
	
	public boolean lPush(final String key, final List<? extends Serializable> list) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				byte[][] seriValues = new byte[list.size()][];
				for (int i = 0; i < list.size(); i++) {
					Serializable value = list.get(i);
					seriValues[i] = SerializeUtil.serialize(value);
				}
				long count = connection.lPush(seriKey, seriValues);
				return count == list.size();
			}
		});
		return result;
	}
	
	public Serializable rPop(final String key) {
		Serializable result = redisTemplate.execute(new RedisCallback<Serializable>() {
			public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] seriKey = SerializeUtil.serialize(key);
				return SerializeUtil.deserialize(connection.rPop(seriKey));
			}
		});
		return result;
	}
	
}
