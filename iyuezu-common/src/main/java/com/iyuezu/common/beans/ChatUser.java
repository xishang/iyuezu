package com.iyuezu.common.beans;

/**
 * 聊天信息与聊天用户的关联关系
 */
public class ChatUser {

	private Integer id;
	private Integer chatId; // 对话id
	private String userUuid; // 用户uuid
	private String userNick; // 用户聊天信息昵称
	private Integer disturb; // 消息提醒[0:关闭, 1:打开]
	private Integer status; // 关联关系的状态
	private Long createTime; // 用户加入聊天信息的时间
	private Long updateTime; // 关联关系修改时间
	private Long activeTime; // 用户最后活动时间[作为未读信息依据]

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChatId() {
		return chatId;
	}

	public void setChatId(Integer chatId) {
		this.chatId = chatId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public Integer getDisturb() {
		return disturb;
	}

	public void setDisturb(Integer disturb) {
		this.disturb = disturb;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Long activeTime) {
		this.activeTime = activeTime;
	}

}
