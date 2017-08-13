package com.iyuezu.common.beans;

public class RentReservation {

	private String uuid;
	private RentInfo rentInfo;
	private House house;
	private String contactPhone;
	private String remark;
	private Integer status; // [1:待发布者确认, 2:已确认, 3:已取消, 0:已废弃]
	private Long visitTime;
	private Long createTime;
	
	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		contactPhone = contactPhone == null ? "" : contactPhone;
		remark = remark == null ? "" : remark;
		status = status == null ? 1 : status;
		visitTime = visitTime == null ? 0L : visitTime;
		createTime = createTime == null ? 0L : createTime;
		if (house == null || house.getUuid() == null) {
			house = new House();
			house.setUuid("");
		}
		if (rentInfo == null || rentInfo.getUuid() == null) {
			rentInfo = new RentInfo();
			rentInfo.setUuid("");
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public RentInfo getRentInfo() {
		return rentInfo;
	}

	public void setRentInfo(RentInfo rentInfo) {
		this.rentInfo = rentInfo;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Long visitTime) {
		this.visitTime = visitTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

}
