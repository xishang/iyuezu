package com.iyuezu.mybatis.params;

import java.math.BigDecimal;

import com.iyuezu.mybatis.enums.SimpleEnumOrder;

public class HouseCommentParams {

	private String uuid;
	private String houseUuid; // 评论的房源uuid
	private String userUuid; // 评论人uuid
	private String replyUuid; // 回复的评论uuid
	private Integer minThumb; // 最少点赞数
	private Integer maxThumb; // 最多点赞数
	private Integer minReplyCount; // 最少回复数
	private Integer maxReplyCount; // 最多评论数
	private BigDecimal minCompScore; // 最小综合评分
	private BigDecimal maxCompScore; // 最大综合评分
	private Integer status; // 评论状态
	private Long minCreateTime; // 评论最早创建时间
	private Long maxCreateTime; // 评论最晚创建时间
	
	private SimpleEnumOrder order; // 排序字段
	private Integer desc; // 是否降序[0:否, 1:是]
	private Integer offset; // 开始索引值
	private Integer size; // 长度
	
	public HouseCommentParams() { // 默认排序方式：按时间倒序
		order = SimpleEnumOrder.create_time;
		desc = 1;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getHouseUuid() {
		return houseUuid;
	}

	public void setHouseUuid(String houseUuid) {
		this.houseUuid = houseUuid;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public String getReplyUuid() {
		return replyUuid;
	}

	public void setReplyUuid(String replyUuid) {
		this.replyUuid = replyUuid;
	}

	public Integer getMinThumb() {
		return minThumb;
	}

	public void setMinThumb(Integer minThumb) {
		this.minThumb = minThumb;
	}

	public Integer getMaxThumb() {
		return maxThumb;
	}

	public void setMaxThumb(Integer maxThumb) {
		this.maxThumb = maxThumb;
	}

	public Integer getMinReplyCount() {
		return minReplyCount;
	}

	public void setMinReplyCount(Integer minReplyCount) {
		this.minReplyCount = minReplyCount;
	}

	public Integer getMaxReplyCount() {
		return maxReplyCount;
	}

	public void setMaxReplyCount(Integer maxReplyCount) {
		this.maxReplyCount = maxReplyCount;
	}

	public BigDecimal getMinCompScore() {
		return minCompScore;
	}

	public void setMinCompScore(BigDecimal minCompScore) {
		this.minCompScore = minCompScore;
	}

	public BigDecimal getMaxCompScore() {
		return maxCompScore;
	}

	public void setMaxCompScore(BigDecimal maxCompScore) {
		this.maxCompScore = maxCompScore;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getMinCreateTime() {
		return minCreateTime;
	}

	public void setMinCreateTime(Long minCreateTime) {
		this.minCreateTime = minCreateTime;
	}

	public Long getMaxCreateTime() {
		return maxCreateTime;
	}

	public void setMaxCreateTime(Long maxCreateTime) {
		this.maxCreateTime = maxCreateTime;
	}

	public SimpleEnumOrder getOrder() {
		return order;
	}

	public void setOrder(SimpleEnumOrder order) {
		this.order = order;
	}

	public Integer getDesc() {
		return desc;
	}

	public void setDesc(Integer desc) {
		this.desc = desc;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

}
