package com.iyuezu.platform.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iyuezu.api.interfaces.IChatService;
import com.iyuezu.common.beans.Chat;
import com.iyuezu.common.beans.ChatContentDto;
import com.iyuezu.common.beans.ChatUser;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.beans.UserMember;

@RestController
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	IChatService chatService;

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Chat> createChat(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "remark", required = false) String remark,
			@RequestParam(value = "userUuids", required = true) String userUuids, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		String userUuid = user.getUuid();
		List<String> uuids = new ArrayList<String>();
		uuids.addAll(Arrays.asList(userUuids.split(",")));
		uuids.add(userUuid);
		Chat chat = chatService.createChat(name, remark, uuids);
		return new ResponseResult<Chat>("0", "创建聊天信息成功", chat);
	}
	
	@RequestMapping(value = "/createWithFriend", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Chat> createChatWithFriend(@RequestParam(value = "friendUuid", required = true) String friendUuid,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		String userUuid = user.getUuid();
		List<String> uuids = new ArrayList<String>();
		uuids.add(userUuid);
		uuids.add(friendUuid);
		Chat chat = chatService.createChat("聊天信息" + System.currentTimeMillis(), "", uuids);
		return new ResponseResult<Chat>("0", "创建聊天信息成功", chat);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> editChat(@RequestParam(value = "chatId", required = true) Integer chatId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "remark", required = false) String remark) {
		chatService.editChat(chatId, name, remark);
		return new ResponseResult<Void>("0", "编辑聊天信息成功");
	}

	@RequestMapping(value = "/remove", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> removeChat(@RequestParam(value = "chatId", required = true) Integer chatId) {
		chatService.updateChatStatus(chatId, 0);
		return new ResponseResult<Void>("0", "删除聊天信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Chat> getChat(@PathVariable("id") Integer id) {
		Chat chat = chatService.getChatById(id);
		return new ResponseResult<Chat>("0", "获取聊天信息成功", chat);
	}
	
	@RequestMapping(value = "/activeChats", method = RequestMethod.GET, produces = "application/json")
	public List<Chat> getActiveChats(
			@RequestParam(value = "count", required = false, defaultValue = "10") Integer count,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		return chatService.getActiveChats(user.getUuid(), count);
	}
	
	@RequestMapping(value = "/ownChatUser", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<ChatUser> getOwnChatUser(
			@RequestParam(value = "chatId", required = true) Integer chatId,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		ChatUser chatUser = chatService.getChatUser(chatId, user.getUuid());
		return new ResponseResult<ChatUser>("0", "获取群聊用户信息成功", chatUser);
	}
	
	@RequestMapping(value = "/addUser", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> addChatUsers(
			@RequestParam(value = "chatId", required = true) Integer chatId,
			@RequestParam(value = "userUuids", required = true) String userUuids,
			HttpServletRequest request) {
		chatService.addChatUsers(chatId, Arrays.asList(userUuids.split(",")));
		return new ResponseResult<Void>("0", "添加聊天成员成功");
	}

	@RequestMapping(value = "/setUserNick", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> setUserNick(@RequestParam(value = "chatUserId", required = true) Integer chatUserId,
			@RequestParam(value = "userNick", required = true) String userNick) {
		chatService.setChatUserNick(chatUserId, userNick);
		return new ResponseResult<Void>("0", "设置聊天昵称成功");
	}

	@RequestMapping(value = "/setDisturb", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> setDisturb(@RequestParam(value = "chatUserId", required = true) Integer chatUserId,
			@RequestParam(value = "disturb", required = true) Integer disturb) {
		chatService.setChatUserDisturb(chatUserId, disturb);
		return new ResponseResult<Void>("0", disturb == 0 ? "设置消息免打扰成功" : "取消消息免打扰成功");
	}

	@RequestMapping(value = "/exit", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> exitChat(@RequestParam(value = "chatUserId", required = true) Integer chatUserId) {
		chatService.setChatUserStatus(chatUserId, 0);
		return new ResponseResult<Void>("0", "退出聊天成功");
	}

	@RequestMapping(value = "/userMember", method = RequestMethod.GET, produces = "application/json")
	public List<UserMember> getChatUserMember(@RequestParam(value = "chatId", required = true) Integer chatId) {
		return chatService.getUsersByChatId(chatId);
	}

	@RequestMapping(value = "/addContent", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> addContent(@RequestParam(value = "chatId", required = true) Integer chatId,
			@RequestParam(value = "type", required = true) Integer type,
			@RequestParam(value = "content", required = true) String content, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		chatService.addChatContent(chatId, user.getUuid(), type, content);
		return new ResponseResult<Void>("0", "发送聊天内容成功");
	}

	@RequestMapping(value = "/withdrawContent", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> withdrawContent(@RequestParam(value = "contentId", required = true) String contentId) {
		chatService.updateChatContentStatus(contentId, 2);
		return new ResponseResult<Void>("0", "撤回聊天内容成功");
	}

	@RequestMapping(value = "/contentList", method = RequestMethod.GET, produces = "application/json")
	public List<ChatContentDto> getContentList(@RequestParam(value = "chatId", required = true) Integer chatId,
			@RequestParam(value = "userUuid", required = false) String userUuid,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "status", required = false) Integer status,
			@RequestParam(value = "startTime", required = false) Long startTime,
			@RequestParam(value = "endTime", required = false) Long endTime,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		return chatService.getChatContents(chatId, userUuid, type, status, startTime, endTime, page, row);
	}

}
