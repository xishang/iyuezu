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
import com.iyuezu.api.interfaces.IRentInfoService;
import com.iyuezu.api.interfaces.IRentReservationService;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentInfoDto;
import com.iyuezu.common.beans.RentReservation;
import com.iyuezu.common.beans.ResponseResult;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.ApplicationUtil;
import com.iyuezu.common.utils.DecoderUtil;
import com.iyuezu.mybatis.params.RentInfoParams;
import com.iyuezu.mybatis.params.ReservationParams;

@RestController
@RequestMapping("/rent")
public class RentInfoController {

	@Autowired
	private IRentInfoService rentInfoService;

	@Autowired
	private IRentReservationService reservationService;
	
	private static Logger logger = Logger.getLogger(RentInfoController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentInfo>> getRentInfoList(RentInfoParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<RentInfo> rentPage = rentInfoService.getRentInfos(params, page, row);
		return new ResponseResult<PageInfo<RentInfo>>("0", "获取求租信息列表成功", rentPage);
	}
	
	@RequestMapping(value = "/validList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentInfo>> getValidRentInfoList(RentInfoParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		params.setStatus(1);
		PageInfo<RentInfo> rentPage = rentInfoService.getRentInfos(params, page, row);
		return new ResponseResult<PageInfo<RentInfo>>("0", "获取求租信息列表成功", rentPage);
	}

	@RequestMapping(value = "/ownList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentInfo>> getRentInfoList(RentInfoParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<RentInfo>>("1", "请先登录");
		}
		params.setRenterUuid(user.getUuid());
		PageInfo<RentInfo> rentPage = rentInfoService.getRentInfos(params, page, row);
		return new ResponseResult<PageInfo<RentInfo>>("0", "获取求租信息列表成功", rentPage);
	}
	
	@RequestMapping(value = "/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<RentInfo> getRentInfoByUuid(@PathVariable("uuid") String uuid) {
		RentInfo rentInfo = rentInfoService.getRentInfoByUuid(uuid);
		if (rentInfo == null) {
			return new ResponseResult<RentInfo>("1", "获取求租信息失败");
		} else {
			return new ResponseResult<RentInfo>("0", "获取求租信息成功", rentInfo);
		}
	}

	@RequestMapping(value = "/dto/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<RentInfoDto> getRentInfoDtoByUuid(@PathVariable("uuid") String uuid) {
		RentInfoDto rentInfo = rentInfoService.getRentInfoDtoByUuid(uuid);
		if (rentInfo == null) {
			return new ResponseResult<RentInfoDto>("1", "获取求租信息失败");
		} else {
			return new ResponseResult<RentInfoDto>("0", "获取求租信息成功", rentInfo);
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<RentInfo> saveRentInfo(RentInfo rentInfo, HttpServletRequest request) {
		DecoderUtil.decode(rentInfo, "utf-8");
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			logger.error("保存求租信息：用户ip[" + ApplicationUtil.getIpAddr(request) + "], 结果[保存失败，未登录]");
			return new ResponseResult<RentInfo>("9", "请先登录");
		}
		if (rentInfo.getUuid() == null) {
			if (rentInfo.getRenter() == null || rentInfo.getRenter().getUuid() == null) {
				rentInfo.setRenter(user);
			}
			RentInfo resInfo = rentInfoService.createRentInfo(rentInfo);
			logger.info("创建求租信息：用户账号[" + user.getAccount() + "], 求租信息[联系人：" + rentInfo.getContactName()
					+ ", 手机号：" + rentInfo.getContactPhone() + ", 微信号：" + rentInfo.getContactWechat() + "], 结果[创建成功]");
			return new ResponseResult<RentInfo>("0", "创建求租信息成功", resInfo);
		} else {
			String baseLogInfo = "修改求租信息：用户账号[" + user.getAccount() + "], 求租信息[uuid：" + rentInfo.getUuid() + "联系人：" + rentInfo.getContactName()
				+ ", 手机号：" + rentInfo.getContactPhone() + ", 微信号：" + rentInfo.getContactWechat() + "]";
			RentInfo oriInfo = rentInfoService.getRentInfoByUuid(rentInfo.getUuid());
			if (user.getType() != 3 && !user.getUuid().equals(oriInfo.getRenter().getUuid())) {
				logger.error(baseLogInfo + ", 结果[修改失败，没有权限]");
				return new ResponseResult<RentInfo>("2", "你不是求租信息发布者，没有修改权限");
			}
			RentInfo resInfo = rentInfoService.editRentInfo(rentInfo);
			if (resInfo == null) {
				logger.error(baseLogInfo + ", 结果[修改失败]");
				return new ResponseResult<RentInfo>("1", "保存求租信息失败");
			} else {
				logger.info(baseLogInfo + ", 结果[修改成功]");
				return new ResponseResult<RentInfo>("0", "保存求租信息成功", resInfo);
			}
		}
	}

	@RequestMapping(value = "/reservation/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentReservation>> getReservationList(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		PageInfo<RentReservation> reservationPage = reservationService.getRentReservations(params, page, row);
		return new ResponseResult<PageInfo<RentReservation>>("0", "获取求租回应信息列表成功", reservationPage);
	}
	
	@RequestMapping(value = "/reservation/listByRentInfo", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentReservation>> getReservationListByRentInfo(
			@RequestParam(value = "rentUuid", required = true) String rentUuid,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		ReservationParams params = new ReservationParams();
		params.setRentUuid(rentUuid);
		PageInfo<RentReservation> reservationPage = reservationService.getRentReservationsWithHouse(params, page, row);
		return new ResponseResult<PageInfo<RentReservation>>("0", "获取求租回应信息列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/listByHouse", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentReservation>> getReservationListByHouse(
			@RequestParam(value = "houseUuid", required = true) String houseUuid,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row) {
		ReservationParams params = new ReservationParams();
		params.setHouseUuid(houseUuid);
		PageInfo<RentReservation> reservationPage = reservationService.getRentReservationsWithRentInfo(params, page, row);
		return new ResponseResult<PageInfo<RentReservation>>("0", "获取求租回应信息列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/ownListByRenter", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentReservation>> getOwnReservationListByRenter(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) { // 获取与租客有关的求租回应信息
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<RentReservation>>("1", "请先登录");
		}
		params.setRenterUuid(user.getUuid());
		PageInfo<RentReservation> reservationPage = reservationService.getRentReservations(params, page, row);
		return new ResponseResult<PageInfo<RentReservation>>("0", "获取求租回应信息列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/ownListByOwner", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentReservation>> getOwnReservationListByOwner(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) { // 获取与房东有关的求租回应信息
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<RentReservation>>("1", "请先登录");
		}
		params.setOwnerUuid(user.getUuid());
		PageInfo<RentReservation> reservationPage = reservationService.getRentReservations(params, page, row);
		return new ResponseResult<PageInfo<RentReservation>>("0", "获取求租回应信息列表成功", reservationPage);
	}
	
	@RequestMapping(value = "/reservation/ownList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<PageInfo<RentReservation>> getOwnReservationList(ReservationParams params,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) { // 获取与房东有关的求租回应信息
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<PageInfo<RentReservation>>("1", "请先登录");
		}
		if (user.getType() == 1) {
			params.setRenterUuid(user.getUuid());
		} else if (user.getType() == 2) {
			params.setOwnerUuid(user.getUuid());
		}
		PageInfo<RentReservation> reservationPage = reservationService.getRentReservations(params, page, row);
		return new ResponseResult<PageInfo<RentReservation>>("0", "获取求租回应信息列表成功", reservationPage);
	}

	@RequestMapping(value = "/reservation/{uuid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<RentReservation> getRservationByUuid(@PathVariable("uuid") String uuid) {
		RentReservation reservation = reservationService.getRentReservationByUuid(uuid);
		if (reservation == null) {
			return new ResponseResult<RentReservation>("1", "获取求租回应信息失败");
		} else {
			return new ResponseResult<RentReservation>("0", "获取求租回应信息成功", reservation);
		}
	}

	@RequestMapping(value = "/reservation/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<RentReservation> saveReservation(RentReservation reservation, HttpServletRequest request) {
		/* -- 回应求租信息要求必须登陆 -- */
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			logger.error("保存求租预约：用户ip[" + ApplicationUtil.getIpAddr(request) + "], 结果[保存失败，未登录]");
			return new ResponseResult<RentReservation>("9", "请先登录");
		}
		if (reservation.getUuid() == null) {
			
			if (reservation.getRentInfo() == null || StringUtil.isEmpty(reservation.getRentInfo().getUuid())) {
				logger.error("创建求租预约：用户账号[" + user.getAccount() + "], 结果[创建失败，未选择求租信息]");
				return new ResponseResult<RentReservation>("2", "创建失败，未选择求租信息");
			}
			if (reservation.getHouse() == null || StringUtil.isEmpty(reservation.getHouse().getUuid())) {
				logger.error("创建求租预约：用户账号[" + user.getAccount() + "], 结果[创建失败，未选择房源]");
				return new ResponseResult<RentReservation>("3", "创建失败，未选择房源");
			}
			
			if (StringUtil.isEmpty(reservation.getContactPhone())) { // 若未输入联系方式，则使用预约人手机号
				reservation.setContactPhone(user.getPhone());
			}
			String baseLogInfo = "创建求租预约：用户账号[" + user.getAccount() + "], 预约信息[求租uuid：" + reservation.getRentInfo().getUuid()
					+ ", 房源uuid：" + reservation.getHouse().getUuid() + ", 手机号：" + reservation.getContactPhone() + ", 备注信息："
					+ reservation.getRemark() + "]";
			try {
				RentReservation resReservation = reservationService.createRentReservation(reservation);
				if (resReservation == null) {
					logger.error(baseLogInfo + ", 结果[创建失败，选择的房源已回应过该求租信息，且正在处理中]");
					return new ResponseResult<RentReservation>("4", "您选择的房源已回应过该求租信息，且正在处理中，请尝试其他房源");
				} else {
					logger.info(baseLogInfo + ", 结果[创建成功]");
					return new ResponseResult<RentReservation>("0", "创建求租预约成功", resReservation);
				}
			} catch (Exception e) {
				logger.error(baseLogInfo + ", 结果[创建失败]");
				return new ResponseResult<RentReservation>("1", "创建求租预约失败");
			}
		} else {
			RentReservation resReservation = reservationService.editRentReservation(reservation);
			if (resReservation == null) {
				logger.error("修改求租预约：用户账号[" + user.getAccount() + "], 预约信息[uuid：" + reservation.getUuid() + "], 结果[修改失败]");
				return new ResponseResult<RentReservation>("1", "修改求租预约失败");
			} else {
				logger.info("修改求租预约：用户账号[" + user.getAccount() + "], 预约信息[uuid：" + reservation.getUuid() + "], 结果[修改成功]");
				return new ResponseResult<RentReservation>("0", "修改求租预约成功", resReservation);
			}
		}
	}
	
	@RequestMapping(value = "/reservation/updateStatus", method = RequestMethod.POST, produces = "application/json")
	public ResponseResult<Void> updateReservation(@RequestParam("reservationUuid") String reservationUuid,
			@RequestParam("status") Integer status,
			@RequestParam(value = "remark", required = false, defaultValue = "") String remark,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			logger.error("更新求租预约状态：用户ip[" + ApplicationUtil.getIpAddr(request) + "], 结果[更新失败，未登录]");
			return new ResponseResult<Void>("9", "请先登录");
		}
		String baseLogInfo = "更新求租预约状态：用户账号[" + user.getAccount() + "], 更新信息[预约uuid：" + reservationUuid + ", 状态：" + status + ", 备注信息：" + remark + "]";
		try {
			RentReservation reservation = reservationService.getRentReservationByUuid(reservationUuid);
			if (reservation == null) {
				logger.error(baseLogInfo + ", 结果[更新失败，预约信息不存在]");
				return new ResponseResult<Void>("2", "要更新状态的预约信息不存在");
			}
			if (user.getType() != 3 && !reservation.getHouse().getOwner().getUuid().equals(user.getUuid())
					&& !reservation.getRentInfo().getRenter().getUuid().equals(user.getUuid())) { // 不是管理员或预约相关用户，无权限
				logger.error(baseLogInfo + ", 结果[更新失败，用户没有权限]");
				return new ResponseResult<Void>("3", "你没有权限修改该预约信息");
			}
			if (isIllegal(reservation.getStatus(), status)) {
				logger.error(baseLogInfo + ", 结果[更新失败，预约状态已变更]");
				return new ResponseResult<Void>("4", "更新预约状态失败，请检查预约状态是否已变更");
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
			[1:已申请, 2:已接受, 3.已拒绝, 4:已确认, 5:已取消]
		状态流转[带*为需要验证]:
			1[已申请]-2[已接受*]-4[已确认*]
			1[已申请]-2[已接受*]-5[已取消]
			1[已申请]-3[已拒绝*]
			1[已申请]-5[已取消]
		状态流转中的操作:
			1[已申请]	renter:	接受, 拒绝[验证]
					owner:	取消
			2[已接受]	renter:	确认[验证]
					owner:	取消
			3[已拒绝]	renter:	--
					owner:	--
			4[已确认]	renter:	租房
					owner:	--
			5[已取消]	renter:	--
					owner:	--
	 */
	private boolean isIllegal(int oriStatus, int newStatus) {
		if (newStatus == 2 || newStatus == 3) {
			if (oriStatus != 1) {
				return true;
			}
		}
		if (newStatus == 4) {
			if (oriStatus != 2) {
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
	
	@RequestMapping(value = "/reservation/rentList", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<List<RentInfo>> getReservationRentInfoList(
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "row", required = false, defaultValue = "10") Integer row,
			HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<List<RentInfo>>("9", "请先登录");
		}
		List<RentInfo> rentList = reservationService.getReservationRentInfoList(user.getUuid(), null, page, row);
		return new ResponseResult<List<RentInfo>>("0", "获取房源列表成功", rentList);
	}
	
	@RequestMapping(value = "/reservation/count", method = RequestMethod.GET, produces = "application/json")
	public ResponseResult<Integer> getReservationCount(ReservationParams params, HttpServletRequest request) {
		User user = (User) request.getAttribute("user");
		if (user == null || user.getUuid() == null) {
			return new ResponseResult<Integer>("9", "请先登录");
		}
		int count = reservationService.getReservationCount(params);
		return new ResponseResult<Integer>("0", "获取求租信息预约数成功", count);
	}
	
}
