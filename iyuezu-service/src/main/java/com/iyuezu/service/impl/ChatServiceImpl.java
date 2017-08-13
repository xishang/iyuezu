package com.iyuezu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyuezu.api.interfaces.IChatService;
import com.iyuezu.common.beans.Chat;
import com.iyuezu.common.beans.ChatContentDto;
import com.iyuezu.common.beans.ChatUser;
import com.iyuezu.common.beans.UserMember;
import com.iyuezu.mongo.entity.ChatContent;
import com.iyuezu.mongo.params.ChatContentParams;
import com.iyuezu.mongo.service.MongoChatService;
import com.iyuezu.mybatis.mapper.ChatMapper;

@Service
public class ChatServiceImpl implements IChatService {
	
	@Autowired
	private ChatMapper chatMapper;
	
	@Autowired
	private MongoChatService mongoChatService;

	@Override
	public Chat createChat(String name, String remark, List<String> userUuids) {
		Chat chat = new Chat();
		chat.setName(name);
		chat.setRemark(remark);
		chat.setStatus(1);
		Date curTime = new Date();
		chat.setCreateTime(curTime.getTime());
		chat.setUpdateTime(curTime.getTime());
		chat.setActiveTime(0l);
		chatMapper.insertChat(chat);
		int chatId = chat.getId();
		// 添加聊天与用户关系
		chatMapper.insertChatUserRelations(chatId, userUuids, curTime.getTime());
		return chat;
	}
	
	@Override
	public void editChat(int chatId, String name, String remark) {
		Chat chat = new Chat();
		chat.setId(chatId);
		chat.setName(name);
		chat.setRemark(remark);
		chat.setUpdateTime(new Date().getTime());
		chatMapper.updateChat(chat);
	}
	
	@Override
	public void updateChatStatus(int chatId, int status) {
		Chat chat = new Chat();
		chat.setId(chatId);
		chat.setStatus(status);
		chat.setUpdateTime(new Date().getTime());
		chatMapper.updateChat(chat);
	}
	
	@Override
	public void refreshChatActiveTime(int chatId) {
		Chat chat = new Chat();
		chat.setId(chatId);
		chat.setActiveTime(new Date().getTime());
		chatMapper.updateChat(chat);
	}
	
	@Override
	public Chat getChatById(int chatId) {
		return chatMapper.selectChatById(chatId);
	}
	
	@Override
	public List<Chat> getActiveChats(String userUuid, int count) {
		return chatMapper.selectActiveChats(userUuid, count);
	}
	
	@Override
	public ChatUser getChatUser(int chatId, String userUuid) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("chatId", chatId);
		paramMap.put("userUuid", userUuid);
		List<ChatUser> list = chatMapper.selectChatUsers(paramMap);
		return list.get(0);
	}
	
	@Override
	public void addChatUsers(int chatId, List<String> userUuids) {
		Date curTime = new Date();
		chatMapper.insertChatUserRelations(chatId, userUuids, curTime.getTime());
	}
	
	@Override
	public void setChatUserNick(int chatUserId, String userNick) {
		ChatUser chatUser = new ChatUser();
		chatUser.setId(chatUserId);
		chatUser.setUserNick(userNick);
		chatUser.setUpdateTime(new Date().getTime());
		chatMapper.updateChatUser(chatUser);
	}
	
	@Override
	public void setChatUserDisturb(int chatUserId, int disturb) {
		ChatUser chatUser = new ChatUser();
		chatUser.setId(chatUserId);
		chatUser.setDisturb(disturb);
		chatUser.setUpdateTime(new Date().getTime());
		chatMapper.updateChatUser(chatUser);
	}

	@Override
	public void setChatUserStatus(int chatUserId, int status) {
		ChatUser chatUser = new ChatUser();
		chatUser.setId(chatUserId);
		chatUser.setStatus(status);
		chatUser.setUpdateTime(new Date().getTime());
		chatMapper.updateChatUser(chatUser);
	}
	
	@Override
	public List<String> getUserUuidsByChatId(int chatId) {
		return chatMapper.selectUserUuidsByChatId(chatId);
	}
	
	@Override
	public List<UserMember> getUsersByChatId(int chatId) {
		return chatMapper.selectUsersByChatId(chatId);
	}
	
	@Override
	public void addChatContent(int chatId, String userUuid, int type, String content) {
		ChatContent chatContent = new ChatContent();
		chatContent.setChatId(chatId);
		chatContent.setUserUuid(userUuid);
		chatContent.setType(type);
		chatContent.setContent(content);
		chatContent.setStatus(1); // 默认为1[1:正常, 2:已撤回, 3:已删除]
		Date curTime = new Date();
		chatContent.setCreateTime(curTime.getTime());
		chatContent.setUpdateTime(curTime.getTime());
		mongoChatService.insertContent(chatContent);
	}
	
	@Override
	public void updateChatContentStatus(String contentId, Integer status) {
		mongoChatService.updateContentStatus(contentId, status);
	}

	@Override
	public List<ChatContentDto> getChatContents(Integer chatId, String userUuid, Integer type, Integer status,
			Long startTime, Long endTime, int pageIndex, int pageSize) {
		ChatContentParams params = new ChatContentParams();
		params.setChatId(chatId);
		params.setUserUuid(userUuid);
		params.setType(type);
		params.setStatus(status);
		params.setStartTime(startTime);
		params.setEndTime(endTime);
		params.setPageIndex(pageIndex);
		params.setPageSize(pageSize);
		List<ChatContent> contentList = mongoChatService.findContents(params);
		return convertToDto(contentList);
	}
	
	private List<ChatContentDto> convertToDto(List<ChatContent> contentList) {
		List<ChatContentDto> dtoList = new ArrayList<ChatContentDto>();
		for (ChatContent content : contentList) {
			ChatContentDto dto = new ChatContentDto();
			dto.setId(content.getId());
			dto.setChatId(content.getChatId());
			dto.setUserUuid(content.getUserUuid());
			dto.setType(content.getType());
			dto.setContent(content.getContent());
			dto.setStatus(content.getStatus());
			dto.setCreateTime(content.getCreateTime());
			dto.setUpdateTime(content.getUpdateTime());
			dtoList.add(dto);
		}
		return dtoList;
	}

}
