package com.iyuezu.common.beans;

import java.math.BigDecimal;
import java.util.List;

public class HouseComment {

	private String uuid;
	private House house; // 评论的房源
	private User user; // 评论人
	private String replyUuid; // 回复的评论uuid
	private Integer level; // 评论级别[回复的级别为2]
	private String content; // 评论内容
	private Integer thumb; // 点赞数
	private Integer replyCount; // 回复数
	private BigDecimal compScore; // 综合评价
	private BigDecimal authScore; // 房源可靠度
	private BigDecimal satisScore; // 房源满意度
	private BigDecimal servScore; // 服务满意度
	private Integer status; // 评论状态
	private Long createTime; // 创建时间

	List<HouseComment> replys; // 评论的所有回复

	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		replyUuid = replyUuid == null ? "" : replyUuid;
		level = level == null ? 1 : level;
		content = content == null ? "" : content;
		thumb = thumb == null ? 0 : thumb;
		replyCount = replyCount == null ? 0 : replyCount;
		compScore = compScore == null ? new BigDecimal(0.0) : compScore;
		authScore = authScore == null ? new BigDecimal(0.0) : authScore;
		satisScore = satisScore == null ? new BigDecimal(0.0) : satisScore;
		servScore = servScore == null ? new BigDecimal(0.0) : servScore;
		status = status == null ? 1 : status;
		createTime = createTime == null ? 0L : createTime;
		if (house == null || house.getUuid() == null) {
			house = new House();
			house.setUuid("");
		}
		if (user == null || user.getUuid() == null) {
			user = new User();
			user.setUuid("");
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getReplyUuid() {
		return replyUuid;
	}

	public void setReplyUuid(String replyUuid) {
		this.replyUuid = replyUuid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getThumb() {
		return thumb;
	}

	public void setThumb(Integer thumb) {
		this.thumb = thumb;
	}

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}

	public BigDecimal getCompScore() {
		return compScore;
	}

	public void setCompScore(BigDecimal compScore) {
		this.compScore = compScore;
	}

	public BigDecimal getAuthScore() {
		return authScore;
	}

	public void setAuthScore(BigDecimal authScore) {
		this.authScore = authScore;
	}

	public BigDecimal getSatisScore() {
		return satisScore;
	}

	public void setSatisScore(BigDecimal satisScore) {
		this.satisScore = satisScore;
	}

	public BigDecimal getServScore() {
		return servScore;
	}

	public void setServScore(BigDecimal servScore) {
		this.servScore = servScore;
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

	public List<HouseComment> getReplys() {
		return replys;
	}

	public void setReplys(List<HouseComment> replys) {
		this.replys = replys;
	}

}
