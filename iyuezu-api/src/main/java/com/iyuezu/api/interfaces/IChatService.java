package com.iyuezu.api.interfaces;

import java.util.List;

import com.iyuezu.common.beans.Chat;
import com.iyuezu.common.beans.ChatContentDto;
import com.iyuezu.common.beans.ChatUser;
import com.iyuezu.common.beans.UserMember;

public interface IChatService {

	/**
	 * 创建聊天信息
	 * 
	 * @param name		聊天信息名称
	 * @param remark	聊天信息备注
	 * @param userUuids	聊天用户
	 */
	public Chat createChat(String name, String remark, List<String> userUuids);

	/**
	 * 编辑聊天信息的名称和备注
	 * 
	 * @param chatId
	 * @param name
	 * @param remark
	 */
	public void editChat(int chatId, String name, String remark);
	
	/**
	 * 修改聊天信息状态
	 * 
	 * @param chatId
	 * @param status
	 */
	public void updateChatStatus(int chatId, int status);
	
	/**
	 * 更新聊天信息的活动时间
	 * 
	 * @param chatId
	 */
	public void refreshChatActiveTime(int chatId);

	/**
	 * 获取聊天信息
	 * 
	 * @param chatId
	 * @return
	 */
	public Chat getChatById(int chatId);
	
	/**
	 * 获取用户的活跃的聊天信息
	 * 
	 * @param userUuid	用户uuid
	 * @param count		聊天信息数量
	 * @return
	 */
	public List<Chat> getActiveChats(String userUuid, int count);
	
	/**
	 * 根据群聊ID和用户uuid获取群聊用户关系信息
	 * 
	 * @param chatId
	 * @param userUuid
	 * @return
	 */
	public ChatUser getChatUser(int chatId, String userUuid);
	
	/**
	 * 群聊添加成员
	 * 
	 * @param chatId		群聊ID
	 * @param userUuids		要添加的成员uuid
	 */
	public void addChatUsers(int chatId, List<String> userUuids);

	/**
	 * 设置聊天信息用户昵称
	 * 
	 * @param chatUserId	聊天信息-用户关联关系ID
	 * @param userNick		用户聊天信息昵称
	 */
	public void setChatUserNick(int chatUserId, String userNick);
	
	/**
	 * 设置聊天信息消息提醒模式
	 * 
	 * @param chatUserId	聊天信息-用户关联关系ID
	 * @param disturb		消息提醒[0:关闭, 1:打开]
	 */
	public void setChatUserDisturb(int chatUserId, int disturb);
	
	/**
	 * 设置用户与聊天信息的状态
	 * 
	 * @param chatUserId	聊天信息-用户关联关系ID
	 * @param status		聊天信息-用户关联关系状态
	 */
	public void setChatUserStatus(int chatUserId, int status);
	
	/**
	 * 获取聊天信息的所有用户uuid
	 * 
	 * @param chatId
	 * @return
	 */
	public List<String> getUserUuidsByChatId(int chatId);
	
	/**
	 * 获取聊天信息的所有用户
	 * 
	 * @param chatId
	 * @return
	 */
	public List<UserMember> getUsersByChatId(int chatId);

	/**
	 * 创建聊天内容
	 * 
	 * @param chatId	聊天信息ID
	 * @param userUuid	用户uuid
	 * @param type		内容类型[1:text, 2:picture, 3:audio, 4:video]
	 * @param content	聊天内容
	 */
	public void addChatContent(int chatId, String userUuid, int type, String content);

	/**
	 * 修改聊天内容状态
	 * 
	 * @param contentId		聊天内容ID
	 * @param status		聊天内容状态
	 */
	public void updateChatContentStatus(String contentId, Integer status);

	/**
	 * 获取聊天内容
	 * 
	 * @param chatId		聊天信息ID
	 * @param userUuid		聊天用户uuid
	 * @param type			内容类型[1:text, 2:picture, 3:audio, 4:video]
	 * @param status		内容状态[1:正常, 2:已撤回, 3:已删除]
	 * @param startTime		内容创建时间[开始]
	 * @param endTime		内容创建时间[结束]
	 * @param pageIndex		页码
	 * @param pageSize		每页条数
	 * @return
	 */
	public List<ChatContentDto> getChatContents(Integer chatId, String userUuid, Integer type, Integer status,
			Long startTime, Long endTime, int pageIndex, int pageSize);

}
