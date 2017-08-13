package com.iyuezu.api.interfaces;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.Order;
import com.iyuezu.common.beans.OrderParams;

public interface IOrderService {

	public Order createOrder(Order order) throws Exception;

	public Order editOrder(Order order) throws Exception;

	public Order getOrderByUuid(String uuid);

	public PageInfo<Order> getOrderList(OrderParams params, Integer page, Integer row);

}
