package com.iyuezu.common.beans;

import java.math.BigDecimal;

public class Target {

	private String uuid; // 唯一标识房源标的信息
	private House house; // 标的所属房源
	private User owner; // 标的所属房东
	private String title; // 房源标的标题
	private District district; // 地区
	private String community; // 房源标的所属小区
	private Integer room; // 室
	private Integer hall; // 厅
	private Integer kitchen; // 厨
	private Integer defend; // 卫
	private BigDecimal size; // 房源标的面积
	private BigDecimal price; // 房源标的价格
	private BigDecimal discount; // 房源标的折扣
	private String contactName; // 联系人
	private String contactPhone; // 联系人号码
	private String contactWechat; // 联系人微信
	private Integer reservationCount; // 预约次数
	private Integer commentCount; // 评论数
	private BigDecimal avgScore; // 综合评分平均值
	private Integer type; // 房源标的类型
	private Integer floor; // 房源标的所属楼层
	private Integer totalFloor; // 总楼层数
	private String furnitures; // 房源标的所有家具
	private String description; // 房源标的情况概述
	private String detail; // 房源标的详细描述
	private String pictures; // 房源标的图片信息
	private Integer status; // 房源标的状态
	private Long createTime; // 房源标的创建时间

	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		title = title == null ? "" : title;
		community = community == null ? "" : community;
		room = room == null ? 0 : room;
		hall = hall == null ? 0 : hall;
		kitchen = kitchen == null ? 0 : kitchen;
		defend = defend == null ? 0 : defend;
		size = size == null ? new BigDecimal(0.00) : size;
		price = price == null ? new BigDecimal(0.00) : price;
		discount = discount == null ? new BigDecimal(1.00) : discount;
		contactName = contactName == null ? "" : contactName;
		contactPhone = contactPhone == null ? "" : contactPhone;
		contactWechat = contactWechat == null ? "" : contactWechat;
		reservationCount = reservationCount == null ? 0 : reservationCount;
		commentCount = commentCount == null ? 0 : commentCount;
		avgScore = avgScore == null ? new BigDecimal(0.0) : avgScore;
		type = type == null ? 0 : type;
		floor = floor == null ? 0 : floor;
		totalFloor = totalFloor == null ? 0 : totalFloor;
		furnitures = furnitures == null ? "" : furnitures;
		description = description == null ? "" : description;
		detail = detail == null ? "" : detail;
		pictures = pictures == null ? "" : pictures;
		status = status == null ? 1 : status;
		createTime = createTime == null ? 0L : createTime;
		if (house == null || house.getUuid() == null) {
			house = new House();
			house.setUuid("");
		}
		if (owner == null || owner.getUuid() == null) {
			owner = new User();
			owner.setUuid("");
		}
		if (district == null || district.getId() == null) {
			district = new District();
			district.setId(0);
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

	public BigDecimal getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(BigDecimal avgScore) {
		this.avgScore = avgScore;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
