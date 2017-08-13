package com.iyuezu.mongo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat")
/*@CompoundIndexes({ @CompoundIndex(name = "chat_idx", def = "{'chatId': 1, 'userUuid': 1, 'createTime': -1}") })*/
public class ChatContent implements Serializable {

	private static final long serialVersionUID = -5916077222279080192L;

	@Id
	private String id;
	// 普通索引, 该普通索引与@CompoundIndexes注解的联合索引都会被建立
	@Indexed
	private Integer chatId;
	@Indexed
	private String userUuid;
	private Integer type; // 1:text, 2:picture, 3:audio, 4:video
	private String content;
	private Integer status; // 1:正常, 2:已撤回, 3:已删除
	@Indexed
	private Long createTime;
	private Long updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
