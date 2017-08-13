package com.iyuezu.common.beans;

import java.math.BigDecimal;

public class RentInfo {

	private String uuid; // 求租信息唯一标识
	private User renter; // 发布求租信息的租客
	private District district; // 求租地区
	private Integer room; // 室
	private Integer hall; // 厅
	private Integer kitchen; // 厨
	private Integer defend; // 卫
	private BigDecimal size; // 面积
	private BigDecimal minPrice; // 心理价位[最低]
	private BigDecimal maxPrice; // 心理价位[最高]
	private String contactName; // 联系人
	private String contactPhone; // 联系人号码
	private String contactWechat; // 联系人微信
	private Integer reservationCount; // 回应求租信息的次数
	private Integer type; // 类型
	private String detail; // 详细描述
	private Integer status; // 求租信息的状态[0:无效, 1:有效]
	private Long createTime; // 创建时间
	private Long updateTime; // 更新时间

	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		room = room == null ? 0 : room;
		hall = hall == null ? 0 : hall;
		kitchen = kitchen == null ? 0 : kitchen;
		defend = defend == null ? 0 : defend;
		size = size == null ? new BigDecimal(0.00) : size;
		minPrice = minPrice == null ? new BigDecimal(0.00) : minPrice;
		maxPrice = maxPrice == null ? new BigDecimal(0.00) : maxPrice;
		contactName = contactName == null ? "" : contactName;
		contactPhone = contactPhone == null ? "" : contactPhone;
		contactWechat = contactWechat == null ? "" : contactWechat;
		reservationCount = reservationCount == null ? 0 : reservationCount;
		type = type == null ? 0 : type;
		detail = detail == null ? "" : detail;
		status = status == null ? 1 : status;
		createTime = createTime == null ? 0L : createTime;
		updateTime = updateTime == null ? 0L : updateTime;
		if (renter == null || renter.getUuid() == null) {
			renter = new User();
			renter.setUuid("");
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

	public User getRenter() {
		return renter;
	}

	public void setRenter(User renter) {
		this.renter = renter;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
		this.district = district;
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

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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
