package com.iyuezu.api.interfaces;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.beans.UserDto;
import com.iyuezu.common.beans.UserMember;

public interface IUserService {

	public User login(String account, String password) throws Exception;

	public boolean isAccountExist(String account);
	
	public boolean isPhoneExist(String phone);

	public User register(User user) throws Exception;

	public User editUser(User user) throws Exception;
	
	public int changePassword(String uuid, String password, String newPassword);
	
	public void logout(String token);

	public User getUserByUuid(String uuid);
	
	public User getUserByAccount(String account);
	
	public User getUserByPhone(String phone);

	public PageInfo<User> getUserList(String parentUuid, Integer type, Integer status, Integer page, Integer row);
	
	public UserDto getUserDtoByUuid(String uuid);
	
	/**
	 * 添加好友
	 * 
	 * @param userUuid		发起好友请求的用户uuid
	 * @param friendUuid	添加的好友uuid
	 * @param friendNick	为好友添加的备注（昵称）
	 */
	public void addFriend(String userUuid, String friendUuid, String friendNick);
	
	/**
	 * 更改好友昵称
	 * 
	 * @param id			好友关系ID
	 * @param friendNick	为好友添加的备注（昵称）
	 */
	public void setFriendNick(int id, String friendNick);
	
	/**
	 * 拉黑好友
	 * 
	 * @param id	好友关系ID
	 */
	public void blackFriend(int id);
	
	/**
	 * 删除好友
	 * 
	 * @param id	好友关系ID
	 */
	public void removeFriend(int id);
	
	/**
	 * 获取好友列表
	 * 
	 * @param userUuid		用户uuid
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PageInfo<UserMember> getFriendList(String userUuid, int pageIndex, int pageSize);
	
	/**
	 * 根据关键字查找用户[account或username]
	 * 
	 * @param keyword
	 * @return
	 */
	public List<User> getUsersByKeyword(String keyword);

}
