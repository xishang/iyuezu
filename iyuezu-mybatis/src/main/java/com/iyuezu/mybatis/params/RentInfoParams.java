package com.iyuezu.mybatis.params;

import java.math.BigDecimal;

import com.iyuezu.mybatis.enums.SimpleEnumOrder;

public class RentInfoParams {

	private String uuid;
	private String renterUuid; // 租客uuid
	private Integer districtId; // 求租地区id
	private BigDecimal minSize; // 最小面积
	private BigDecimal maxSize; // 最大面积
	private BigDecimal minPrice; // 最小价格
	private BigDecimal maxPrice; // 最大价格
	private Integer minRoomCount; // 最小房间[室]数
	private Integer maxRoomCount; // 最大房间[室]数
	private Integer type; // 求租房间类型
	private Integer status; // 状态
	private Long minCreateTime; // 求租信息最早创建时间
	private Long maxCreateTime; // 求租信息最晚创建时间
	private Long minUpdateTime; // 求租信息最早更新时间
	private Long maxUpdateTime; // 求租信息最晚更新时间
	private SimpleEnumOrder order; // 排序字段[使用枚举类型以防止SQL注入]
	private Integer isDesc; // 是否倒序[1:倒序, 0:正序]
	
	public RentInfoParams() { // 默认以更新时间倒序排列
		order = SimpleEnumOrder.update_time;
		isDesc = 1;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getRenterUuid() {
		return renterUuid;
	}

	public void setRenterUuid(String renterUuid) {
		this.renterUuid = renterUuid;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public BigDecimal getMinSize() {
		return minSize;
	}

	public void setMinSize(BigDecimal minSize) {
		this.minSize = minSize;
	}

	public BigDecimal getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(BigDecimal maxSize) {
		this.maxSize = maxSize;
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

	public Integer getMinRoomCount() {
		return minRoomCount;
	}

	public void setMinRoomCount(Integer minRoomCount) {
		this.minRoomCount = minRoomCount;
	}

	public Integer getMaxRoomCount() {
		return maxRoomCount;
	}

	public void setMaxRoomCount(Integer maxRoomCount) {
		this.maxRoomCount = maxRoomCount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public Long getMinUpdateTime() {
		return minUpdateTime;
	}

	public void setMinUpdateTime(Long minUpdateTime) {
		this.minUpdateTime = minUpdateTime;
	}

	public Long getMaxUpdateTime() {
		return maxUpdateTime;
	}

	public void setMaxUpdateTime(Long maxUpdateTime) {
		this.maxUpdateTime = maxUpdateTime;
	}
	
	public SimpleEnumOrder getOrder() {
		return order;
	}

	public void setOrder(SimpleEnumOrder order) {
		this.order = order;
	}

	public Integer getIsDesc() {
		return isDesc;
	}

	public void setIsDesc(Integer isDesc) {
		this.isDesc = isDesc;
	}

}
