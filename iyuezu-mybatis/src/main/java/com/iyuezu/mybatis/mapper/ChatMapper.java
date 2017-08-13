package com.iyuezu.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.Chat;
import com.iyuezu.common.beans.ChatUser;
import com.iyuezu.common.beans.UserMember;

public interface ChatMapper {

	public Integer insertChat(Chat chat);
	
	public Integer updateChat(Chat chat);
	
	public Chat selectChatById(@Param("chatId") Integer chatId);
	
	public List<Chat> selectActiveChats(@Param("userUuid") String userUuid, @Param("count") Integer count);
	
	public Integer insertChatUserRelations(@Param("chatId") Integer chatId, @Param("userUuids") List<String> userUuids,
			@Param("time") Long time);
	
	public Integer updateChatUser(ChatUser chatUser);
	
	public List<ChatUser> selectChatUsers(Map<String, Object> paramMap);
	
	public List<String> selectUserUuidsByChatId(@Param("chatId") Integer chatId);
	
	public List<UserMember> selectUsersByChatId(@Param("chatId") Integer chatId);

}
