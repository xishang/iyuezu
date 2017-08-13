package com.iyuezu.common.beans;

public class HouseReservation {

	private String uuid;
	private House house;
	private User renter;
	private String contactPhone;
	private String remark;
	private Integer status; // [1:已申请, 2:已接受, 3:已确认, 4:已评价, 5:已取消]
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
		if (renter == null || renter.getUuid() == null) {
			renter = new User();
			renter.setUuid("");
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

	public User getRenter() {
		return renter;
	}

	public void setRenter(User renter) {
		this.renter = renter;
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
