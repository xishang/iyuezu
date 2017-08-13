package com.iyuezu.platform.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IStatisticsService;
import com.iyuezu.api.interfaces.IUserService;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.beans.UserDto;
import com.iyuezu.common.beans.UserMember;
import com.iyuezu.common.exceptions.AccountAlreadyExistException;
import com.iyuezu.common.exceptions.ParameterIllegalException;
import com.iyuezu.common.exceptions.PasswordIncorrectException;
import com.iyuezu.common.exceptions.PhoneAlreadyExistException;
import com.iyuezu.common.exceptions.UserNotExistException;
import com.iyuezu.common.utils.ApplicationUtil;
import com.iyuezu.common.utils.DecoderUtil;
import com.iyuezu.redis.impl.UserRedisImpl;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private IStatisticsService statisticsService;
	
	@Autowired
	private UserRedisImpl userRedis;

	private static Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<User> login(@RequestParam("account") String account,
			@RequestParam("password") String password, HttpServletRequest request) {
		String baseLogInfo = "用户登录：账号[" + account + "], 密码[" + password + "], 用户ip[" + ApplicationUtil.getIpAddr(request) + "]";
		try {
			User user = userService.login(account, password);
			logger.info(baseLogInfo + ", 结果[登录成功]");
			return new ResponseResult<User>("0", "登录成功", user);
		} catch (UserNotExistException une) {
			logger.error(baseLogInfo + ", 结果[登录失败，用户名不存在]");
			return new ResponseResult<User>("1", "用户不存在");
		} catch (PasswordIncorrectException pie) {
			logger.error(baseLogInfo + ", 结果[登录失败，密码错误]");
			return new ResponseResult<User>("2", "密码错误");
		} catch (Exception e) {
			logger.error(baseLogInfo + ", 结果[登录失败，更新token或服务器出错]");
			return new ResponseResult<User>("3", "登录出错");
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> logout(@RequestParam("access_token") String accessToken) {
		userService.logout(accessToken);
		return new ResponseResult<Void>("0", "注销成功");
	}

	@RequestMapping(value = "/isAccountExist", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Void> isAccountExist(@RequestParam("account") String account) {
		boolean flag = userService.isAccountExist(account);
		if (flag) {
			return new ResponseResult<Void>("1", "该账号已经注册，请使用其他账号");
		} else {
			return new ResponseResult<Void>("0", "账号可以使用");
		}
	}

	@RequestMapping(value = "/isPhoneExist", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Void> isPhoneExist(@RequestParam("phone") String phone) {
		boolean flag = userService.isPhoneExist(phone);
		if (flag) {
			return new ResponseResult<Void>("1", "该手机号已经注册，请使用其他手机号");
		} else {
			return new ResponseResult<Void>("0", "手机号可以使用");
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<User> register(User user, HttpServletRequest request) {
		DecoderUtil.decode(user, "utf-8");
		String baseLogInfo = "用户注册：账号[" + user.getAccount() + "], 用户名[" + user.getUsername() + "], 姓名[" + user.getName() + "], 手机号["
				+ user.getPhone() + "], 类型[" + user.getType() + "], 用户ip[" + ApplicationUtil.getIpAddr(request) + "]";
		try {
			User resUser = userService.register(user);
			if (resUser == null) {
				logger.error(baseLogInfo + ", 结果[注册失败，插入数据库出错]");
				return new ResponseResult<User>("3", "注册失败");
			} else {
				logger.info(baseLogInfo + ", 结果[注册成功]");
				return new ResponseResult<User>("0", "注册成功", resUser);
			}
		} catch (AccountAlreadyExistException ae) {
			logger.error(baseLogInfo + ", 结果[注册失败，账号已存在]");
			return new ResponseResult<User>("1", "该账号已经存在，请使用其他账号注册");
		} catch (PhoneAlreadyExistException pe) {
			logger.error(baseLogInfo + ", 结果[注册失败，手机号已注册]");
			return new ResponseResult<User>("2", "该手机号已经注册，请使用其他手机号");
		} catch (ParameterIllegalException pie) {
			logger.error(baseLogInfo + ", 结果[注册失败，参数不合法]");
			return new ResponseResult<User>("8", "参数不合法");
		} catch (Exception e) {
			logger.error(baseLogInfo + ", 结果[注册失败，服务器出错]");
			return new ResponseResult<User>("3", "注册失败");
		}
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> changePassword(@RequestParam("password") String password,
			@RequestParam("newPassword") String newPassword, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			String userIp = ApplicationUtil.getIpAddr(request);
			logger.error("修改密码：ip[" + userIp + "], 原密码[" + password + "], 新密码[" + newPassword + "], 结果[修改失败，未登录]");
			return new ResponseResult<Void>("1", "请先登录");
		}
		String baseLogInfo = "修改密码：账号[" + user.getAccount() + "], 原密码[" + password + "], 新密码[" + newPassword + "]";
		int flag = userService.changePassword(user.getUuid(), password, newPassword);
		if (flag == 0) {
			logger.error(baseLogInfo + ", 结果[修改失败，原密码错误]");
			return new ResponseResult<Void>("1", "原密码错误");
		} else {
			logger.info(baseLogInfo + ", 结果[修改成功]");
			return new ResponseResult<Void>("0", "修改密码成功");
		}
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<User> editUser(User user, String access_token, HttpServletRequest request) {
		DecoderUtil.decode(user, "utf-8");
		User rUser = (User) request.getAttribute("user");
		if (user == null || rUser.getUuid() == null) {
			logger.error("编辑用户信息：用户ip[" + ApplicationUtil.getIpAddr(request) + "], 修改信息[uuid：" + user.getUuid() + ", 账号：" + user.getAccount()
				+ ", 用户名：" + user.getUsername() + ", 姓名:" + user.getName() + ", 手机号："+ user.getPhone() + ", 邮箱：" + user.getEmail() + ", 身份证号："
				+ user.getIdentity() + "], 结果[编辑失败，未登录]");
			return new ResponseResult<User>("1", "请先登录");
		}
		String baseLogInfo = "编辑用户信息：操作人信息[uuid：" + rUser.getUuid() + ", 账号：" + rUser.getAccount() + ", 用户名：" + rUser.getUsername()
			+ ", 类型：" + rUser.getType() + "], 修改信息[uuid：" + user.getUuid() + ", 账号：" + user.getAccount() + ", 用户名：" + user.getUsername()
			+ ", 姓名:" + user.getName() + ", 手机号："+ user.getPhone() + ", 邮箱：" + user.getEmail() + ", 身份证号：" + user.getIdentity() + "]";
		if (rUser.getType() != 3 && !rUser.getUuid().equals(user.getUuid())) {
			logger.error(baseLogInfo + ", 结果[编辑失败，没有权限]");
			return new ResponseResult<User>("2", "没有修改该用户信息的权限");
		}
		try {
			User resUser = userService.editUser(user);
			if (resUser == null) {
				logger.error(baseLogInfo + ", 结果：[编辑失败，数据库更新用户信息失败]");
				return new ResponseResult<User>("6", "编辑用户信息失败");
			} else {
				userRedis.setUserByToken(access_token, resUser);
				logger.info(baseLogInfo + ", 结果：[编辑成功]");
				return new ResponseResult<User>("0", "编辑用户信息成功", resUser);
			}
		} catch (AccountAlreadyExistException ae) {
			logger.error(baseLogInfo + ", 结果：[编辑失败，账号已注册]");
			return new ResponseResult<User>("3", "该账号已经注册，请使用其他账号");
		} catch (PhoneAlreadyExistException pe) {
			logger.error(baseLogInfo + ", 结果：[编辑失败，手机号已注册]");
			return new ResponseResult<User>("4", "该手机号已经注册，请使用其他手机号");
		} catch (Exception e) {
			logger.error(baseLogInfo + ", [编辑失败，服务器出错]");
			return new ResponseResult<User>("5", "编辑用户信息失败");
		}
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<User> getUserByUuid(@PathVariable("uuid") String uuid) {
		User user = userService.getUserByUuid(uuid);
		if (user == null) {
			return new ResponseResult<User>("1", "获取用户信息失败");
		} else {
			return new ResponseResult<User>("0", "获取用户信息成功", user);
		}
	}

	@RequestMapping(value = "/own", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<User> getOwnUser(HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<User>("2", "您尚未登录，无法获取用户信息");
		} else {
			User userByUuid = userService.getUserByUuid(user.getUuid());
			if (userByUuid == null) {
				return new ResponseResult<User>("1", "获取用户信息失败");
			} else {
				return new ResponseResult<User>("0", "获取用户信息成功", userByUuid);
			}
		}
	}

	@RequestMapping(value = "/ownDto", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<UserDto> getOwnUserDto(HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<UserDto>("9", "请先登录");
		}
		UserDto userDto = userService.getUserDtoByUuid(user.getUuid());
		if (userDto == null) {
			return new ResponseResult<UserDto>("1", "获取用户信息失败");
		} else {
			return new ResponseResult<UserDto>("0", "获取用户信息成功", userDto);
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<User>> getUserList(
			@RequestParam(value = "parentUuid", required = false) String parentUuid,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<User> userPage = userService.getUserList(parentUuid, type, status, page, row);
		return new ResponseResult<PageInfo<User>>("0", "获取用户列表成功", userPage);
	}

	@RequestMapping(value = "/ownerList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<User>> getOwnerList(@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<User> userPage = userService.getUserList(null, 2, status, page, row);
		return new ResponseResult<PageInfo<User>>("0", "获取房东列表成功", userPage);
	}

	@RequestMapping(value = "/handleInfo", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Map<String, Integer>> getHandleInfo(HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<Map<String, Integer>>("1", "暂未登录");
		}
		Map<String, Integer> handleMap = statisticsService.getHandleMap(user.getUuid(), user.getType());
		return new ResponseResult<Map<String, Integer>>("0", "获取待处理信息成功", handleMap);
	}
	
	@RequestMapping(value = "/listByKeyword", method = RequestMethod.GET, produces = "application/json")
	public List<User> getUserListByKeyword(@RequestParam(value = "keyword", required = true) String keyword) {
		List<User> userList = userService.getUsersByKeyword(keyword);
		return userList;
	}
	
	@RequestMapping(value = "/addFriend", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> addFriend(@RequestParam(value = "friendUuid", required = true) String friendUuid,
			@RequestParam(value = "friendNick", required = false) String friendNick, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		userService.addFriend(user.getUuid(), friendUuid, friendNick);
		return new ResponseResult<Void>("0", "添加好友成功");
	}

	@RequestMapping(value = "/setFriendNick", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> setFriendNick(@RequestParam(value = "relationId", required = true) Integer relationId,
			@RequestParam(value = "friendNick", required = true) String friendNick) {
		userService.setFriendNick(relationId, friendNick);
		return new ResponseResult<Void>("0", "修改好友昵称成功");
	}

	@RequestMapping(value = "/blackFriend", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> blackFriend(@RequestParam(value = "relationId", required = true) Integer relationId) {
		userService.blackFriend(relationId);
		return new ResponseResult<Void>("0", "拉黑好友成功");
	}

	@RequestMapping(value = "/removeFriend", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> removeFriend(@RequestParam(value = "relationId", required = true) Integer relationId) {
		userService.removeFriend(relationId);
		return new ResponseResult<Void>("0", "删除好友成功");
	}

	@RequestMapping(value = "/friends", method = RequestMethod.GET, produces = "application/json")
	public PageInfo<UserMember> getFriends(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		return userService.getFriendList(user.getUuid(), page, row);
	}

}
