package com.iyuezu.mybatis.enums;

public enum EnumOrder {
	
	SIZE("size"),
	PRICE("price"),
	MIN_PRICE("min_price"),
	RESERVATION_COUNT("reservation_count"),
	COMMENT_COUNT("comment_count"),
	DISCOUNT("discount"),
	CREATE_TIME("create_time"),
	UPDATE_TIME("update_time");
	
	
	private EnumOrder(String order) {
		this.order = order;
	}
	
	private String order;
	
	public String getValue() {
		return order;
	}

}
