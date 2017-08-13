package com.iyuezu.common.beans;

import java.math.BigDecimal;
import java.util.List;

public class HouseDto {

	private String uuid; // 唯一标识房源信息
	private User owner; // 房源所属房东
	private String title; // 房源标题
	private District district; // 地区
	private String community; // 房源所属小区
	private Integer room; // 室
	private Integer hall; // 厅
	private Integer kitchen; // 厨
	private Integer defend; // 卫
	private BigDecimal size; // 房源面积
	private BigDecimal price; // 房源价格
	private BigDecimal discount; // 房源折扣
	private String contactName; // 联系人
	private String contactPhone; // 联系人号码
	private String contactWechat; // 联系人微信
	private Integer reservationCount; // 预约次数
	private Integer commentCount; // 评论数
	private Integer type; // 房源类型
	private Integer floor; // 房源所属楼层
	private Integer totalFloor; // 总楼层数
	private String furnitures; // 房源所有家具
	private String description; // 房源情况概述
	private String detail; // 房源详细描述
	private String pictures; // 房源图片信息
	private Integer status; // 房源状态[0:无效, 1:待出租, 2:已出租]
	private Long freeTime; // 房源到期时间
	private Long createTime; // 房源创建时间
	private Long updateTime; // 房源更新时间
	private List<HouseComment> comments; // 房源评论
	private List<HouseReservation> reservations; // 房源预约信息

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public Integer getRoom() {
		return room;
	}

	public void setRoom(Integer room) {
		this.room = room;
	}

	public Integer getHall() {
		return hall;
	}

	public void setHall(Integer hall) {
		this.hall = hall;
	}

	public Integer getKitchen() {
		return kitchen;
	}

	public void setKitchen(Integer kitchen) {
		this.kitchen = kitchen;
	}

	public Integer getDefend() {
		return defend;
	}

	public void setDefend(Integer defend) {
		this.defend = defend;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactWechat() {
		return contactWechat;
	}

	public void setContactWechat(String contactWechat) {
		this.contactWechat = contactWechat;
	}

	public Integer getReservationCount() {
		return reservationCount;
	}

	public void setReservationCount(Integer reservationCount) {
		this.reservationCount = reservationCount;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

	public Integer getTotalFloor() {
		return totalFloor;
	}

	public void setTotalFloor(Integer totalFloor) {
		this.totalFloor = totalFloor;
	}

	public String getFurnitures() {
		return furnitures;
	}

	public void setFurnitures(String furnitures) {
		this.furnitures = furnitures;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getFreeTime() {
		return freeTime;
	}

	public void setFreeTime(Long freeTime) {
		this.freeTime = freeTime;
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

	public List<HouseComment> getComments() {
		return comments;
	}

	public void setComments(List<HouseComment> comments) {
		this.comments = comments;
	}

	public List<HouseReservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<HouseReservation> reservations) {
		this.reservations = reservations;
	}

}
