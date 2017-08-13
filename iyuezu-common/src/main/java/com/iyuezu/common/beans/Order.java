package com.iyuezu.common.beans;

import java.math.BigDecimal;

public class Order {

	private String uuid; // 订单唯一标识
	private String code; // 订单号
	private User renter; // 租客
	private Target target; // 房源标的
	private BigDecimal discount; // 续约折扣
	private BigDecimal benefit; // 订单优惠金额
	private BigDecimal amount; // 订单金额
	private Integer status; // 订单状态[0:取消, 1:待确认, 2:完成]
	private Long beginTime; // 订单生效时间
	private Long endTime; // 订单到期时间
	private Long createTime; // 订单创建时间
	private Long updateTime; // 订单更新时间

	public void formatDefault() {
		uuid = uuid == null ? "" : uuid;
		code = code == null ? "" : code;
		discount = discount == null ? new BigDecimal(1.00) : discount;
		benefit = benefit == null ? new BigDecimal(0.00) : benefit;
		amount = amount == null ? new BigDecimal(0.00) : amount;
		status = status == null ? 1 : status;
		beginTime = beginTime == null ? 0L : beginTime;
		endTime = endTime == null ? 0L : endTime;
		createTime = createTime == null ? 0L : createTime;
		updateTime = updateTime == null ? 0L : updateTime;
		if (renter == null || renter.getUuid() == null) {
			renter = new User();
			renter.setUuid("");
		}
		if (target == null || target.getUuid() == null) {
			target = new Target();
			target.setUuid("");
		}
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public User getRenter() {
		return renter;
	}

	public void setRenter(User renter) {
		this.renter = renter;
	}

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getBenefit() {
		return benefit;
	}

	public void setBenefit(BigDecimal benefit) {
		this.benefit = benefit;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
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
