package com.iyuezu.platform.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.iyuezu.api.interfaces.IHouseCommentService;
import com.iyuezu.api.interfaces.IHouseReservationService;
import com.iyuezu.api.interfaces.IHouseService;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseComment;
import com.iyuezu.common.beans.HouseReservation;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.ResultPage;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.ApplicationUtil;
import com.iyuezu.common.utils.DecoderUtil;
import com.iyuezu.mybatis.params.HouseCommentParams;
import com.iyuezu.mybatis.params.HouseParams;
import com.iyuezu.mybatis.params.ReservationParams;

@RestController
@RequestMapping("/house")
public class HouseController {

	@Autowired
	private IHouseService houseService;

	@Autowired
	private IHouseReservationService reservationService;

	@Autowired
	private IHouseCommentService commentService;
	
	private static Logger logger = Logger.getLogger(HouseController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<House>> getHouseList(HouseParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<House> housePage = houseService.getHouses(params, page, row);
		return new ResponseResult<PageInfo<House>>("0", "获取房源列表成功", housePage);
	}
	
	@RequestMapping(value = "/validList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<House>> getValidHouseList(HouseParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		params.setStatus(1);
		PageInfo<House> housePage = houseService.getHouses(params, page, row);
		return new ResponseResult<PageInfo<House>>("0", "获取房源列表成功", housePage);
	}

	@RequestMapping(value = "/ownList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<House>> getOwnHouseList(HouseParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<House>>("1", "请先登录");
		}
		params.setOwnerUuid(user.getUuid());
		PageInfo<House> housePage = houseService.getHouses(params, page, row);
		return new ResponseResult<PageInfo<House>>("0", "获取房源列表成功", housePage);
	}

	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<House> getHouseByUuid(@PathVariable("uuid") String uuid) {
		House house = houseService.getHouseByUuid(uuid);
		if (house == null) {
			return new ResponseResult<House>("1", "获取房源失败");
		} else {
			return new ResponseResult<House>("0", "获取房源成功", house);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<House> saveHouse(House house, HttpServletRequest request) {
		DecoderUtil.decode(house, "utf-8");
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			logger.error("保存房源信息：用户ip[" + ApplicationUtil.getIpAddr(request) + "], 结果[保存失败，未登录]");
			return new ResponseResult<House>("9", "请先登录");
		}
		if (house.getUuid() == null) {
			if (house.getOwner() == null || house.getOwner().getUuid() == null) {
				house.setOwner(user);
			}
			House resHouse = houseService.createHouse(house);
			if (resHouse == null) {
				logger.error("创建房源：用户账号[" + user.getAccount() + "], 房源信息[标题：" + house.getTitle() + ", 联系人：" + house.getContactName()
					+ ", 手机号：" + house.getContactPhone() + ", 微信号：" + house.getContactWechat() + "], 结果[创建失败]");
				return new ResponseResult<House>("1", "创建房源失败");
			} else {
				logger.info("创建房源：用户账号[" + user.getAccount() + "], 房源信息[uuid：" + resHouse.getUuid() + ", 标题：" + house.getTitle()
					+ ", 联系人：" + house.getContactName() + ", 手机号：" + house.getContactPhone() + ", 微信号：" + house.getContactWechat()
					+ "], 结果[创建成功]");
				return new ResponseResult<House>("0", "创建房源成功", resHouse);
			}
		} else {
			House oriHouse = houseService.getHouseByUuid(house.getUuid());
			if (user.getType() != 3 && !user.getUuid().equals(oriHouse.getOwner().getUuid())) { // 请求者系统管理员，也不是房源所有者，禁止修改
				return new ResponseResult<House>("2", "你不是房源所有者，没有修改权限");
			}
			House resHouse = houseService.editHouse(house);
			if (resHouse == null) {
				logger.error("编辑房源：用户账号[" + user.getAccount() + "], 房源信息[uuid：" + house.getUuid() + ", 标题：" + house.getTitle() + "], 结果[编辑失败]");
				return new ResponseResult<House>("1", "编辑房源信息失败");
			} else {
				logger.info("编辑房源：用户账号[" + user.getAccount() + "], 房源信息[uuid：" + house.getUuid() + ", 标题：" + house.getTitle() + "], 结果[编辑成功]");
				return new ResponseResult<House>("0", "编辑房源信息成功", resHouse);
			}
		}
	}

	@RequestMapping(value = "/reservation/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<HouseReservation>> getReservationList(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<HouseReservation> reservationPage = reservationService.getHouseReservations(params, page, row);
		return new ResponseResult<PageInfo<HouseReservation>>("0", "获取房源预约列表成功", reservationPage);
	}
	
	@RequestMapping(value = "/reservation/listByHouse", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<HouseReservation>> getReservationListByHouse(
			@RequestParam(value = "houseUuid", required = true) String houseUuid,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		ReservationParams params = new ReservationParams();
		params.setHouseUuid(houseUuid);
		PageInfo<HouseReservation> reservationPage = reservationService.getHouseReservationsWithRenter(params, page, row);
		return new ResponseResult<PageInfo<HouseReservation>>("0", "获取房源预约列表成功", reservationPage);
	}
	
	@RequestMapping(value = "/reservation/listByPhone", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<HouseReservation>> getReservationListByPhone(
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) { // 根据手机号获取列表，提供给未登录时创建的预约使用
		ReservationParams params = new ReservationParams();
		params.setContactPhone(phone);
		PageInfo<HouseReservation> reservationPage = reservationService.getHouseReservationsWithoutRenter(params, page, row);
		return new ResponseResult<PageInfo<HouseReservation>>("0", "获取房源预约列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/ownListByRenter", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<HouseReservation>> getOwnReservationListByRenter(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) { // 获取与租客有关的房源预约信息
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<HouseReservation>>("1", "请先登录");
		}
		params.setRenterUuid(user.getUuid());
		PageInfo<HouseReservation> reservationPage = reservationService.getHouseReservationsWithHouse(params, page, row);
		return new ResponseResult<PageInfo<HouseReservation>>("0", "获取房源预约列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/ownListByOwner", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<HouseReservation>> getOwnReservationListByOwner(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) { // 获取与房东有关的房源预约信息
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<HouseReservation>>("1", "请先登录");
		}
		params.setOwnerUuid(user.getUuid());
		PageInfo<HouseReservation> reservationPage = reservationService.getHouseReservations(params, page, row);
		return new ResponseResult<PageInfo<HouseReservation>>("0", "获取房源预约列表成功", reservationPage);
	}
	
	@RequestMapping(value = "/reservation/ownList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<HouseReservation>> getOwnReservationList(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<HouseReservation>>("1", "请先登录");
		}
		if (user.getType() == 1) {
			params.setRenterUuid(user.getUuid());
		} else if (user.getType() == 2) {
			params.setOwnerUuid(user.getUuid());
		}
		PageInfo<HouseReservation> reservationPage = reservationService.getHouseReservations(params, page, row);
		return new ResponseResult<PageInfo<HouseReservation>>("0", "获取房源预约列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<HouseReservation> getRservationByUuid(@PathVariable("uuid") String uuid) {
		HouseReservation reservation = reservationService.getHouseReservationByUuid(uuid);
		if (reservation == null) {
			return new ResponseResult<HouseReservation>("1", "获取房源预约信息失败");
		} else {
			return new ResponseResult<HouseReservation>("0", "获取房源预约信息成功", reservation);
		}
	}

	@RequestMapping(value = "/reservation/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<HouseReservation> saveReservation(HouseReservation reservation, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		String userInfo;
		if (user == null || user.getUuid() == null) {
			userInfo = "用户ip[" +ApplicationUtil.getIpAddr(request) + "]";
		} else {
			userInfo = "用户账号[" + user.getAccount() + "]";
		}
		/* -- 允许只留手机号而不登陆 -- */
//		if (user == null || user.getUuid() == null) {
//			return new ResponseResult<HouseReservation>("9", "请先登录");
//		}
		if (reservation.getUuid() == null) {
			if (reservation.getHouse() == null || StringUtil.isEmpty(reservation.getHouse().getUuid())) {
				logger.error("创建求租预约：用户账号[" + user.getAccount() + "], 结果[创建失败，未选择房源]");
				return new ResponseResult<HouseReservation>("3", "创建失败，未选择房源");
			}
			if (user == null || user.getUuid() == null) {
				if (StringUtil.isEmpty(reservation.getContactPhone())) { // renter为空且没有联系方式，参数不合法
					logger.error("创建房源预约：" + userInfo + ", 预约信息[房源uuid：" + reservation.getHouse().getUuid() + ", 备注信息："
							+ reservation.getRemark() + "], 结果[创建失败，未登录且未提供手机号]");
					return new ResponseResult<HouseReservation>("2", "保存房源预约信息失败，请登录或输入联系方式");
				}
			} else {
				if (reservation.getRenter() == null || reservation.getRenter().getUuid() == null) {
					reservation.setRenter(user);
				}
				if (StringUtil.isEmpty(reservation.getContactPhone())) { // 若已登录且为未输入联系方式，则使用预约人的手机号
					reservation.setContactPhone(reservation.getRenter().getPhone());
				}
			}
			try {
				HouseReservation resReservation = reservationService.createHouseReservation(reservation);
				if (resReservation == null) {
					logger.error("创建房源预约：" + userInfo + ", 预约信息[房源uuid：" + reservation.getHouse().getUuid() + ", 手机号："
							+ reservation.getContactPhone() + ", 备注信息：" + reservation.getRemark() + "], 结果[创建失败，用户已预约过房源，且正在处理中]");
					return new ResponseResult<HouseReservation>("2", "您已预约过该房源，且正在处理中");
				} else {
					logger.info("创建房源预约：" + userInfo + ", 预约信息[uuid：" + resReservation.getUuid() + ", 房源uuid：" + reservation.getHouse().getUuid()
							+ ", 手机号：" + reservation.getContactPhone() + ", 备注信息：" + reservation.getRemark() + "], 结果[创建成功]");
					return new ResponseResult<HouseReservation>("0", "创建房源预约成功");
				}
			} catch (Exception e) {
				logger.error("创建房源预约：" + userInfo + ", 预约信息[房源uuid：" + reservation.getHouse().getUuid() + ", 手机号："
						+ reservation.getContactPhone() + ", 备注信息：" + reservation.getRemark() + "], 结果[创建失败，"
						+ (StringUtil.isEmpty(e.getMessage()) ? "服务器出错" : e.getMessage()) + "]");
				return new ResponseResult<HouseReservation>("1", "创建房源预约失败");
			}
		} else {
			/* -- 避免renter为null时验证出错 -- */
//			HouseReservation oriReservation = reservationService.getHouseReservationByUuid(reservation.getUuid());
//			if (user.getType() != 3 && !user.getUuid().equals(oriReservation.getRenter().getUuid())) {
//				return new ResponseResult<HouseReservation>("2", "你不是该预约信息发布者，没有修改权限");
//			}
			HouseReservation resReservation = reservationService.editHouseReservation(reservation);
			if (resReservation == null) {
				logger.error("修改房源预约：" + userInfo + ", 预约信息[uuid：" + reservation.getUuid() + "], 结果[修改失败]");
				return new ResponseResult<HouseReservation>("1", "修改房源预约失败");
			} else {
				logger.info("修改房源预约：" + userInfo + ", 预约信息[uuid：" + reservation.getUuid() + "], 结果[修改成功]");
				return new ResponseResult<HouseReservation>("0", "修改房源预约成功", resReservation);
			}
		}
	}
	
	@RequestMapping(value = "/reservation/updateStatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> updateReservation(@RequestParam("reservationUuid") String reservationUuid,
			@RequestParam("status") Integer status,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
//		if (user == null || user.getUuid() == null) {
//			return new ResponseResult<Void>("9", "请先登录");
//		}
		String userInfo;
		// 允许未登录更新
		if (user == null || user.getUuid() == null) {
			user = new User();
			user.setUuid("");
			userInfo = "用户账号[" + user.getAccount() + "]";
		} else {
			userInfo = "用户ip[" +ApplicationUtil.getIpAddr(request) + "]";
		}
		String baseLogInfo = "更新房源预约状态：" + userInfo + ", 更新信息[预约uuid：" + reservationUuid + ", 状态：" + status + ", 备注信息：" + remark + "]";
		try {
			HouseReservation reservation = reservationService.getHouseReservationByUuid(reservationUuid);
			if (reservation == null) {
				logger.error(baseLogInfo + ", 结果[更新失败，预约信息不存在]");
				return new ResponseResult<Void>("2", "要更新状态的预约信息不存在");
			}
			if (user.getType() != 3 && !reservation.getHouse().getOwner().getUuid().equals(user.getUuid())
					&& !reservation.getRenter().getUuid().equals(user.getUuid())) { // 不是管理员或预约相关用户，无权限
				logger.error(baseLogInfo + ", 结果[更新失败，用户没有权限]");
				return new ResponseResult<Void>("3", "你没有权限修改该预约信息");
			}
			if (isIllegal(reservation.getStatus(), status)) {
				logger.error(baseLogInfo + ", 结果[更新失败，预约状态已变更]");
				return new ResponseResult<Void>("3", "更新预约状态失败，请检查预约状态是否已变更");
			}
			reservationService.updateReservationStatus(reservationUuid, user.getUuid(), status, remark);
			logger.info(baseLogInfo + ", 结果[更新成功]");
			return new ResponseResult<Void>("0", "更新预约状态成功");
		} catch (Exception e) {
			logger.error(baseLogInfo + ", 结果[更新失败，服务器出错]");
			return new ResponseResult<Void>("1", "更新预约状态失败");
		}
	}
	
	/*
	 * 检查更新状态是否非法
	 * 
		HouseReservation状态:
			[1:已申请, 2:已接受, 3.已拒绝, 4:已确认, 5:已评价, 6:已取消]
		状态流转[带*为需要验证]:
			1[已申请]-2[已接受*]-4[已确认]-5[已评价]
			1[已申请]-2[已接受*]-6[已取消]
			1[已申请]-3[已拒绝*]
			1[已申请]-6[已取消]
		状态流转中的操作:
			1[已申请]	renter:	取消
					owner:	接受, 拒绝[由于同步问题, 可能预约已被取消, 服务端需验证当前状态为1]
			2[已接受]	renter:	取消, 确认
					owner:	--
			3[已拒绝]	renter:	--
					owner:	--
			4[已确认]	renter:	评价, 租房
					owner:	--
			5[已评价]	renter:	租房
					owner:	--
			6[已取消]	renter:	--
					owner:	--
	 */
	private boolean isIllegal(int oriStatus, int newStatus) {
		if (newStatus == 2 || newStatus == 3) {
			if (oriStatus != 1) {
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = "/reservation/houseList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<List<House>> getReservationHouseList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<List<House>>("9", "请先登录");
		}
		List<House> houseList = reservationService.getReservationHouseList(user.getUuid(), null, page, row);
		return new ResponseResult<List<House>>("0", "获取房源列表成功", houseList);
	}
	
	@RequestMapping(value = "/reservation/count", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Integer> getReservationCount(ReservationParams params, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<Integer>("9", "请先登录");
		}
		int count = reservationService.getReservationCount(params);
		return new ResponseResult<Integer>("0", "获取房源预约数成功", count);
	}

	@RequestMapping(value = "/comment/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<ResultPage<HouseComment>> getCommentList(HouseCommentParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		int count = commentService.getHouseCommentCount(params);
		int offset = (page - 1) * row;
		params.setOffset(offset);
		params.setSize(row);
		List<HouseComment> commentList = commentService.getHouseCommentsWithReplys(params);
		ResultPage<HouseComment> result = new ResultPage<HouseComment>(page, row, count, commentList);
		return new ResponseResult<ResultPage<HouseComment>>("0", "获取房源评价列表成功", result);
	}

	@RequestMapping(value = "/comment/ownList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<ResultPage<HouseComment>> getOwnCommentList(HouseCommentParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<ResultPage<HouseComment>>("1", "请先登录");
		}
		params.setUserUuid(user.getUuid());
		int count = commentService.getHouseCommentCount(params);
		int offset = (page - 1) * row;
		params.setOffset(offset);
		params.setSize(row);
		List<HouseComment> commentList = commentService.getCompleteHouseComments(params);
		ResultPage<HouseComment> result = new ResultPage<HouseComment>(page, row, count, commentList);
		return new ResponseResult<ResultPage<HouseComment>>("0", "获取房源评价列表成功", result);
	}

	@RequestMapping(value = "/comment/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<HouseComment> getCommentByUuid(@PathVariable("uuid") String uuid) {
		HouseComment comment = commentService.getHouseCommentByUuid(uuid);
		if (comment == null) {
			return new ResponseResult<HouseComment>("1", "获取房源评价信息失败");
		} else {
			return new ResponseResult<HouseComment>("0", "获取房源评价信息成功", comment);
		}
	}

	@RequestMapping(value = "/comment/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<HouseComment> saveComment(HouseComment comment, HttpServletRequest request) {
		DecoderUtil.decode(comment, "utf-8");
		User user = (User) request.getAttribute("user");
//		if (user == null || user.getUuid() == null) {
//			return new ResponseResult<HouseComment>("9", "请先登录");
//		}
		String userInfo;
		if (user == null || user.getUuid() == null) {
			userInfo = "用户账号[" + user.getAccount() + "]";
		} else {
			userInfo = "用户ip[" +ApplicationUtil.getIpAddr(request) + "]";
		}
		if (comment.getUuid() == null) {
			if (comment.getUser() == null || comment.getUser().getUuid() == null) {
				comment.setUser(user);
			}
			try {
				HouseComment resComment = commentService.createHouseComment(comment);
				logger.info("创建评论：" + userInfo + ", 评论信息[uuid：" + resComment.getUuid() + ", 房源uuid：" + comment.getHouse().getUuid()
					+ ", 回复uuid：" + comment.getReplyUuid() + ", 综合评分：" + comment.getCompScore() + ", 评论内容：" + comment.getContent()
					+ "], 结果[创建成功]");
				return new ResponseResult<HouseComment>("0", "创建房源评论成功", resComment);
			} catch (Exception e) {
				logger.error("创建评论：" + userInfo + ", 评论信息[房源uuid：" + comment.getHouse().getUuid() + ", 回复uuid：" + comment.getReplyUuid()
						+ ", 综合评分：" + comment.getCompScore() + ", 评论内容：" + comment.getContent() + "], 结果[创建失败，"
						+ (StringUtil.isEmpty(e.getMessage()) ? "服务器出错" : e.getMessage()) + "]");
				return new ResponseResult<HouseComment>("1", "创建房源评论失败");
			}
		} else {
			/* -- 避免user为null时验证出错 -- */
//			HouseComment oriComment = commentService.getHouseCommentByUuid(comment.getUuid());
//			if (user.getType() != 3 && !user.getUuid().equals(oriComment.getUser().getUuid())) {
//				return new ResponseResult<HouseComment>("2", "你不是该评论创建者，没有修改权限");
//			}
			HouseComment resComment = commentService.editHouseComment(comment);
			if (resComment == null) {
				logger.error("修改评论：" + userInfo + ", 评论信息[uuid：" + comment.getUuid() + "], 结果[修改成功]");
				return new ResponseResult<HouseComment>("1", "修改房源评论失败");
			} else {
				logger.info("修改评论：" + userInfo + ", 评论信息[uuid：" + comment.getUuid() + "], 结果[修改失败]");
				return new ResponseResult<HouseComment>("0", "修改房源评论成功", resComment);
			}
		}
	}
	
	@RequestMapping(value = "/comment/thumb", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Integer> thumbComment(@RequestParam("commentUuid") String commentUuid, HttpServletRequest request) {
		String userUuid = "";
		User user = (User) request.getAttribute("user");
		if (user != null && StringUtil.isNotEmpty(user.getUuid())) {
			userUuid = user.getUuid();
		}
		String userIp = ApplicationUtil.getIpAddr(request);
		try {
			int flag = commentService.thumbHouseComment(commentUuid, userUuid, userIp);
			String msg = flag == 1 ? "点赞成功" : "取消点赞成功";
			logger.info("用户点赞：用户uuid[" + userUuid + "], 用户ip[" + userIp + "], 点赞评论uuid[" + commentUuid + "], 结果[" + msg + "]");
			return new ResponseResult<Integer>("0", msg, flag);
		} catch (Exception e) {
			logger.error("用户点赞：用户uuid[" + userUuid + "], 用户ip[" + userIp + "], 点赞评论uuid[" + commentUuid + "], 结果[点赞失败，"
					+ (StringUtil.isEmpty(e.getMessage()) ? "服务器出错" : e.getMessage()) + "]");
			return new ResponseResult<Integer>("1", "点赞失败");
		}
	}
	
	@RequestMapping(value = "/comment/thumb/commentUuidList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<List<String>> getThumbCommentUuidList(HttpServletRequest request) {
		List<String> commentUuidList;
		User user = (User) request.getAttribute("user");
		if (user != null && StringUtil.isNotEmpty(user.getUuid())) {
			commentUuidList = commentService.getThumbCommentUuids(user.getUuid(), null);
		} else {
			String userIp = ApplicationUtil.getIpAddr(request);
			commentUuidList = commentService.getThumbCommentUuids("", userIp); // 过滤掉使用相同ip但是已经登录的用户的点赞
		}
		return new ResponseResult<List<String>>("0", "获取所有点赞评论uuid成功", commentUuidList);
	}

}
