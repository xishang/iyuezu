package com.iyuezu.common.beans;

/**
 * 好友关系
 */
public class UserRelation {

	private Integer id;
	private String userUuid; // 发起好友请求的用户uuid
	private String friendUuid; // 添加的好友uuid
	private String friendNick; // 为好友添加的备注（昵称）
	private Integer status; // 关联关系的状态[1:正常, 2:拉黑, 3:删除]
	private Long createTime; // 创建关系的时间
	private Long updateTime; // 修改关系的时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getFriendUuid() {
		return friendUuid;
	}

	public void setFriendUuid(String friendUuid) {
		this.friendUuid = friendUuid;
	}

	public String getFriendNick() {
		return friendNick;
	}

	public void setFriendNick(String friendNick) {
		this.friendNick = friendNick;
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

}
