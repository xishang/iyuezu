package com.iyuezu.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IUserService;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.beans.UserDto;
import com.iyuezu.common.beans.UserMember;
import com.iyuezu.common.beans.UserRelation;
import com.iyuezu.common.exceptions.AccountAlreadyExistException;
import com.iyuezu.common.exceptions.ParameterIllegalException;
import com.iyuezu.common.exceptions.PasswordIncorrectException;
import com.iyuezu.common.exceptions.PhoneAlreadyExistException;
import com.iyuezu.common.exceptions.UserNotExistException;
import com.iyuezu.common.utils.RegexUtil;
import com.iyuezu.common.utils.StringUtil;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.UserMapper;
import com.iyuezu.redis.impl.UserRedisImpl;

@Service("userService")
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserRedisImpl userRedisImpl;
	
	@Override
	public User login(String account, String password) throws Exception { // 支持账号或手机号登录
		User user = userMapper.selectUserByAccountOrPhone(account);
		if (user == null) {
			throw new UserNotExistException();
		} else if (!password.equals(user.getPassword())) {
			throw new PasswordIncorrectException();
		}
		String token = UUID.randomUUID().toString();
		user.setToken(token);
		userRedisImpl.setUserByToken(token, user);
		return user;
	}

	@Override
	public boolean isAccountExist(String account) {
		int count = userMapper.selectUserCountByAccount(account);
		return count == 0 ? false : true;
	}

	@Override
	public boolean isPhoneExist(String phone) {
		int count = userMapper.selectUserCountByPhone(phone);
		return count == 0 ? false : true;
	}

	@Override
	public User register(User user) throws Exception {
		user.formatDefault();
		if (!checkParameters(user)) {
			throw new ParameterIllegalException();
		}
		int countByAccount = userMapper.selectUserCountByAccount(user.getAccount());
		if (countByAccount != 0) {
			throw new AccountAlreadyExistException();
		}
		int countByPhone = userMapper.selectUserCountByPhone(user.getPhone());
		if (countByPhone != 0) {
			throw new PhoneAlreadyExistException();
		}
		String uuid = UuidUtil.getUuidByTimestamp(5);
		user.setUuid(uuid);
		Long timestamp = new Date().getTime();
		user.setCreateTime(timestamp);
		user.setActiveTime(timestamp);
		String token = UUID.randomUUID().toString();
		user.setToken(token);
		int flag = userMapper.insertUser(user);
		if (flag == 0) {
			return null;
		}
		userRedisImpl.setUserByToken(token, user);
		return user;
	}

	private boolean checkParameters(User user) {
		return RegexUtil.checkAccount(user.getAccount()) && RegexUtil.checkPassword(user.getPassword())
				&& RegexUtil.checkPhone(user.getPhone());
	}

	@Override
	public int changePassword(String uuid, String password, String newPassword) {
		return userMapper.changePassword(uuid, password, newPassword);
	}

	@Override
	public void logout(String token) {
		userRedisImpl.removeAccessToken(token);
	}

	@Override
	public User editUser(User user) throws Exception {
		if (user.getAccount() != null) {
			User userByAccount = userMapper.selectUserByAccount(user.getAccount());
			if (userByAccount != null && !userByAccount.getUuid().equals(user.getUuid())) { // account已存在且不是当前用户
				throw new AccountAlreadyExistException();
			}
		}
		if (user.getPhone() != null) {
			User userByPhone = userMapper.selectUserByPhone(user.getPhone());
			if (userByPhone != null && !userByPhone.getUuid().equals(user.getUuid())) {
				throw new PhoneAlreadyExistException();
			}
		}
		int flag = userMapper.updateUser(user);
		if (flag == 0) {
			return null;
		}
		return getUserByUuid(user.getUuid());
	}

	@Override
	public User getUserByUuid(String uuid) {
		return userMapper.selectUserByUuid(uuid);
	}

	@Override
	public User getUserByAccount(String account) {
		return userMapper.selectUserByAccount(account);
	}

	@Override
	public User getUserByPhone(String phone) {
		return userMapper.selectUserByPhone(phone);
	}

	@Override
	public PageInfo<User> getUserList(String parentUuid, Integer type, Integer status, Integer page, Integer row) {
		PageHelper.startPage(page, row);
		List<User> userList = userMapper.selectUsers(parentUuid, type, status);
		return new PageInfo<User>(userList);
	}

	@Override
	public UserDto getUserDtoByUuid(String uuid) {
		return userMapper.selectUserDtoByUuid(uuid);
	}

	@Override
	public void addFriend(String userUuid, String friendUuid, String friendNick) {
		Integer relationId = userMapper.selectUserRelationId(userUuid, friendUuid);
		if (relationId == null) { // 好友关系不存在, 添加
			UserRelation relation = new UserRelation();
			relation.setUserUuid(userUuid);
			relation.setFriendUuid(friendUuid);
			relation.setFriendNick(friendNick);
			relation.setStatus(1); // 默认为1[1:正常, 2:拉黑, 3:删除]
			Date curTime = new Date();
			relation.setCreateTime(curTime.getTime());
			relation.setUpdateTime(curTime.getTime());
			userMapper.insertUserRelation(relation);
		} else { // 好友关系已存在
			UserRelation relation = new UserRelation();
			relation.setId(relationId);
			relation.setFriendNick(friendNick);
			relation.setStatus(1); // 默认为1[1:正常, 2:拉黑, 3:删除]
			Date curTime = new Date();
			relation.setUpdateTime(curTime.getTime());
			userMapper.updateUserRelation(relation);
		}
	}

	@Override
	public void setFriendNick(int id, String friendNick) {
		UserRelation relation = new UserRelation();
		relation.setId(id);
		relation.setFriendNick(friendNick);
		relation.setUpdateTime(new Date().getTime());
		userMapper.updateUserRelation(relation);
	}
	
	@Override
	public void blackFriend(int id) {
		UserRelation relation = new UserRelation();
		relation.setId(id);
		relation.setStatus(2); // [1:正常, 2:拉黑, 3:删除]
		relation.setUpdateTime(new Date().getTime());
		userMapper.updateUserRelation(relation);
	}

	@Override
	public void removeFriend(int id) {
		UserRelation relation = new UserRelation();
		relation.setId(id);
		relation.setStatus(3); // [1:正常, 2:拉黑, 3:删除]
		relation.setUpdateTime(new Date().getTime());
		userMapper.updateUserRelation(relation);
	}

	@Override
	public PageInfo<UserMember> getFriendList(String userUuid, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex, pageSize);
		List<UserMember> friendList = userMapper.selectFriends(userUuid);
		return new PageInfo<UserMember>(friendList);
	}

	@Override
	public List<User> getUsersByKeyword(String keyword) {
		if (StringUtil.isBlank(keyword)) {
			return null;
		}
		return userMapper.selectUsersByKeyword("%" + keyword + "%");
	}

}
