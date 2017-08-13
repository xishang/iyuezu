package com.iyuezu.common.beans;

import java.math.BigDecimal;

public class OrderParams {

	private String renterUuid; // 租客uuid
	private String ownerUuid; // 房东uuid
	private String houseUuid; // 房源uuid
	private String code; // 订单号
	private BigDecimal minDiscount; // 最小续约折扣
	private BigDecimal maxDiscount; // 最大续约折扣
	private BigDecimal minBenefit; // 订单最小优惠金额
	private BigDecimal maxBenefit; // 订单最大优惠金额
	private BigDecimal minAmount; // 订单最小金额
	private BigDecimal maxAmount; // 订单最小金额
	private Integer status; // 订单状态[0:取消, 1:待确认, 2:完成]
	private Long minCreateTime; // 订单创建时间(最早)
	private Long maxCreateTime; // 订单创建时间(最晚)
	private Long currentTime; // 当前时间戳，设置该参数表示需要目前仍在生效的订单，即：beginTime < currentTime < endTime

	public String getRenterUuid() {
		return renterUuid;
	}

	public void setRenterUuid(String renterUuid) {
		this.renterUuid = renterUuid;
	}

	public String getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(String ownerUuid) {
		this.ownerUuid = ownerUuid;
	}

	public String getHouseUuid() {
		return houseUuid;
	}

	public void setHouseUuid(String houseUuid) {
		this.houseUuid = houseUuid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getMinDiscount() {
		return minDiscount;
	}

	public void setMinDiscount(BigDecimal minDiscount) {
		this.minDiscount = minDiscount;
	}

	public BigDecimal getMaxDiscount() {
		return maxDiscount;
	}

	public void setMaxDiscount(BigDecimal maxDiscount) {
		this.maxDiscount = maxDiscount;
	}

	public BigDecimal getMinBenefit() {
		return minBenefit;
	}

	public void setMinBenefit(BigDecimal minBenefit) {
		this.minBenefit = minBenefit;
	}

	public BigDecimal getMaxBenefit() {
		return maxBenefit;
	}

	public void setMaxBenefit(BigDecimal maxBenefit) {
		this.maxBenefit = maxBenefit;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
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

	public Long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Long currentTime) {
		this.currentTime = currentTime;
	}

}
