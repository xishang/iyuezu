package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.Order;
import com.iyuezu.common.beans.OrderParams;

public interface OrderMapper {

	public List<Order> selectOrders(OrderParams params);

	public Order selectOrderByUuid(@Param("uuid") String uuid);
	
	public Order selectOrder(Order order);
	
	public Integer insertOrder(Order order);

	public Integer updateOrder(Order order);
	
}
