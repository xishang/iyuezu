package com.iyuezu.common.beans;

public class RentReservationStatus {

	private String uuid;
	private RentReservation reservation;
	private User user;
	private Integer status;
	private String remark;
	private Long timestamp;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public RentReservation getReservation() {
		return reservation;
	}

	public void setReservation(RentReservation reservation) {
		this.reservation = reservation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
