package com.iyuezu.mybatis.params;

import java.util.List;

import com.iyuezu.mybatis.enums.SimpleEnumOrder;

public class ReservationParams {

	private String uuid;
	private String houseUuid; // 房源uuid
	private String rentUuid; // 求租信息的uuid
	private String ownerUuid; // 房东uuid
	private String renterUuid; // 租客uuid
	private String contactPhone; // 联系方式
	private List<Integer> statusList; // 状态
	private Long minVisitTime; // 最早看房时间
	private Long maxVisitTime; // 最晚看房时间
	private Long minCreateTime; // 最早创建时间
	private Long maxCreateTime; // 最晚创建时间
	// [特别注意:ReservationParams作为整体参数传入，若此处使用"orderBy"字段，且已经赋值，则在sql语句中会自动加上order by，因此字段名改为order]
	private SimpleEnumOrder order; // 排序字段[使用枚举类型以防止SQL注入]
	private Integer isDesc; // 是否倒序[1:倒序, 0:正序]

	public ReservationParams() {
		this.order = SimpleEnumOrder.create_time;
		this.isDesc = 1;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getHouseUuid() {
		return houseUuid;
	}

	public void setHouseUuid(String houseUuid) {
		this.houseUuid = houseUuid;
	}

	public String getRentUuid() {
		return rentUuid;
	}

	public void setRentUuid(String rentUuid) {
		this.rentUuid = rentUuid;
	}

	public String getOwnerUuid() {
		return ownerUuid;
	}

	public void setOwnerUuid(String ownerUuid) {
		this.ownerUuid = ownerUuid;
	}

	public String getRenterUuid() {
		return renterUuid;
	}

	public void setRenterUuid(String renterUuid) {
		this.renterUuid = renterUuid;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public List<Integer> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}

	public Long getMinVisitTime() {
		return minVisitTime;
	}

	public void setMinVisitTime(Long minVisitTime) {
		this.minVisitTime = minVisitTime;
	}

	public Long getMaxVisitTime() {
		return maxVisitTime;
	}

	public void setMaxVisitTime(Long maxVisitTime) {
		this.maxVisitTime = maxVisitTime;
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
