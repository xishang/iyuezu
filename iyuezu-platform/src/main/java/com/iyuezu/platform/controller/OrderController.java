package com.iyuezu.platform.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IOrderService;
import com.iyuezu.common.beans.Order;
import com.iyuezu.common.beans.OrderParams;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.User;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private IOrderService orderService;
	
	private static Logger logger = Logger.getLogger(OrderController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<Order>> getOrderList(OrderParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<Order> orderPage = orderService.getOrderList(params, page, row);
		return new ResponseResult<PageInfo<Order>>("0", "获取订单列表成功", orderPage);
	}
	
	@RequestMapping(value = "/ownList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<Order>> getOwnOrderList(OrderParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<Order>>("1", "请先登录");
		}
		if (user.getType() == 1) {
			params.setRenterUuid(user.getUuid());
		} else if (user.getType() == 2) {
			params.setOwnerUuid(user.getUuid());
		}
		PageInfo<Order> orderPage = orderService.getOrderList(params, page, row);
		return new ResponseResult<PageInfo<Order>>("0", "获取订单列表成功", orderPage);
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Order> getOrderByUuid(@PathVariable("uuid") String uuid) {
		Order order = orderService.getOrderByUuid(uuid);
		if (order == null) {
			return new ResponseResult<Order>("1", "获取订单失败");
		} else {
			return new ResponseResult<Order>("0", "获取订单成功", order);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Order> saveOrder(Order order, HttpServletRequest request) {
		try {
			Order resOrder;
			User user = (User) request.getAttribute("user");
			if (order.getUuid() == null) {
				if (order.getRenter() == null || order.getRenter().getUuid() == null) {
					order.setRenter(user);
				}
				OrderParams params = new OrderParams();
				params.setRenterUuid(order.getRenter().getUuid());
				params.setHouseUuid(order.getTarget().getUuid()); // 创建订单时先把关联的房源uuid存入order.target中
				params.setStatus(1);
				PageInfo<Order> orderPage = orderService.getOrderList(params, 1, 1);
				if (orderPage.getList().size() > 0) { // 已经创建过订单
					return new ResponseResult<Order>("2", "已经创建过订单，请勿重复提交", orderPage.getList().get(0));
				}
				resOrder = orderService.createOrder(order);
			} else {
				Order oriOrder = orderService.getOrderByUuid(order.getUuid());
				if (user.getType() != 3 && !user.getUuid().equals(oriOrder.getRenter().getUuid())
						&& !user.getUuid().equals(oriOrder.getTarget().getOwner().getUuid())) {
					return new ResponseResult<Order>("1", "你不是该订单所有者，没有编辑权限");
				}
				resOrder = orderService.editOrder(order);
			}
			if (resOrder == null) {
				return new ResponseResult<Order>("1", "编辑订单失败");
			} else {
				return new ResponseResult<Order>("0", "编辑订单成功", resOrder);
			}
		} catch (Exception e) {
			return new ResponseResult<Order>("1", "编辑订单失败");
		}
	}

}
