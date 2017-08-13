package com.iyuezu.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IOrderService;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.Order;
import com.iyuezu.common.beans.OrderParams;
import com.iyuezu.common.beans.Target;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.HouseMapper;
import com.iyuezu.mybatis.mapper.HouseReservationMapper;
import com.iyuezu.mybatis.mapper.OrderMapper;
import com.iyuezu.mybatis.mapper.RentReservationMapper;
import com.iyuezu.mybatis.mapper.TargetMapper;

@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private HouseMapper houseMapper;

	@Autowired
	private TargetMapper targetMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private HouseReservationMapper houseReservationMapper;

	@Autowired
	private RentReservationMapper rentReservationMapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Order createOrder(Order order) throws Exception {
		House house = houseMapper.selectHouseByUuid(order.getTarget().getUuid());
		if (house == null) {
			throw new Exception();
		}
		Target target = house.convertToTarget();
		target.formatDefault();
		target.setUuid(UuidUtil.getUuidByTimestamp(5));
		target.setCreateTime(new Date().getTime());
		int insertTargetFlag = targetMapper.insertTarget(target);
		if (insertTargetFlag == 0) {
			throw new Exception();
		}
		order.setTarget(target);
		order.setUuid(UuidUtil.getUuidByTimestamp(5));
		order.setCode(UuidUtil.getDateFormatUuid(3));
		if (order.getDiscount() == null) {
			order.setDiscount(target.getDiscount());
		}
		BigDecimal amount = target.getPrice().multiply(order.getDiscount()).subtract(order.getBenefit());
		order.setAmount(amount);
		order.setStatus(1);
		Long timestamp = new Date().getTime();
		order.setCreateTime(timestamp);
		order.setUpdateTime(timestamp);
		int insertOrderFlag = orderMapper.insertOrder(order);
		if (insertOrderFlag == 0) {
			throw new Exception();
		} else {
			return orderMapper.selectOrderByUuid(order.getUuid());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Order editOrder(Order order) throws Exception {
		if (order.getStatus() != null && order.getStatus() == 2) { // 订单已确认，更新房源freeTime
			House house = new House();
			String houseUuid = order.getTarget().getHouse().getUuid();
			house.setUuid(houseUuid);
			house.setStatus(2); // 设置房源状态为已出租
			house.setFreeTime(order.getEndTime());
			int updateHouseFlag = houseMapper.updateHouse(house); // 更新房源到期时间
			if (updateHouseFlag == 0) {
				throw new Exception();
			}
			// 把所有与该房源有关的预约信息状态置为0
			houseReservationMapper.disableStatusByHouseUuid(houseUuid);
			rentReservationMapper.disableStatusByHouseUuid(houseUuid);
		}
		order.setUpdateTime(new Date().getTime());
		int updateOrderFlag = orderMapper.updateOrder(order);
		if (updateOrderFlag == 0) {
			throw new Exception();
		} else {
			return orderMapper.selectOrderByUuid(order.getUuid());
		}
	}

	@Override
	public Order getOrderByUuid(String uuid) {
		return orderMapper.selectOrderByUuid(uuid);
	}

	@Override
	public PageInfo<Order> getOrderList(OrderParams params, Integer page, Integer row) {
		PageHelper.startPage(page, row);
		List<Order> orderList = orderMapper.selectOrders(params); // 获取房东的订单列表
		return new PageInfo<Order>(orderList);
	}

}
