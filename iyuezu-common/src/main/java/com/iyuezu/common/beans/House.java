package com.iyuezu.common.beans;

import java.math.BigDecimal;

public class House {

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
	private BigDecimal avgScore; // 综合评分平均值
	private Integer type; // 房源出租方式[1:整套, 2:单间, 3:床位]
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

	public void formatDefault() { // 把所有空的字段格式化为默认值，否则插入数据会出错
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
		freeTime = freeTime == null ? 0L : freeTime;
		createTime = createTime == null ? 0L : createTime;
		updateTime = updateTime == null ? 0L : updateTime;
		if (owner == null || owner.getUuid() == null) {
			owner = new User();
			owner.setUuid("");
		}
		if (district == null || district.getId() == null) {
			district = new District();
			district.setId(0);
		}
	}

	public Target convertToTarget() {
		Target target = new Target();
		target.setOwner(owner);
		target.setTitle(title);
		target.setDistrict(district);
		target.setCommunity(community);
		target.setRoom(room);
		target.setHall(hall);
		target.setKitchen(kitchen);
		target.setDefend(defend);
		target.setSize(size);
		target.setPrice(price);
		target.setDiscount(discount);
		target.setContactName(contactName);
		target.setContactPhone(contactPhone);
		target.setContactWechat(contactWechat);
		target.setReservationCount(reservationCount);
		target.setCommentCount(commentCount);
		target.setType(type);
		target.setFloor(floor);
		target.setTotalFloor(totalFloor);
		target.setFurnitures(furnitures);
		target.setDescription(description);
		target.setDetail(detail);
		target.setPictures(pictures);
		target.setStatus(status);
		target.setHouse(this);
		return target;
	}

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

}
