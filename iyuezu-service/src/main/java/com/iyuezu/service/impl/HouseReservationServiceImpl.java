package com.iyuezu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IHouseReservationService;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseReservation;
import com.iyuezu.common.beans.HouseReservationStatus;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.HouseMapper;
import com.iyuezu.mybatis.mapper.HouseReservationMapper;
import com.iyuezu.mybatis.params.ReservationParams;

@Service
public class HouseReservationServiceImpl implements IHouseReservationService {

	@Autowired
	private HouseReservationMapper houseReservationMapper;

	@Autowired
	private HouseMapper houseMapper;

	@Override
	public PageInfo<HouseReservation> getHouseReservations(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<HouseReservation> reservations = houseReservationMapper.selectHouseReservations(params);
		return new PageInfo<HouseReservation>(reservations);
	}

	@Override
	public PageInfo<HouseReservation> getHouseReservationsWithRenter(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<HouseReservation> reservations = houseReservationMapper.selectHouseReservationsWithRenter(params);
		return new PageInfo<HouseReservation>(reservations);
	}

	@Override
	public PageInfo<HouseReservation> getHouseReservationsWithHouse(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<HouseReservation> reservations = houseReservationMapper.selectHouseReservationsWithHouse(params);
		return new PageInfo<HouseReservation>(reservations);
	}
	
	@Override
	public PageInfo<HouseReservation> getHouseReservationsWithoutRenter(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<HouseReservation> reservations = houseReservationMapper.selectHouseReservationsWithoutRenter(params);
		return new PageInfo<HouseReservation>(reservations);
	}

	@Override
	public HouseReservation getHouseReservationByUuid(String uuid) {
		return houseReservationMapper.selectHouseReservationByUuid(uuid);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public HouseReservation createHouseReservation(HouseReservation houseReservation) throws Exception {
		ReservationParams params = new ReservationParams();
		params.setHouseUuid(houseReservation.getHouse().getUuid());
		if (houseReservation.getRenter() == null || houseReservation.getRenter().getUuid() == null) {
			params.setContactPhone(houseReservation.getContactPhone());
		} else {
			params.setRenterUuid(houseReservation.getRenter().getUuid());
		}
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1);
		statusList.add(2);
		statusList.add(4);
		params.setStatusList(statusList);
		int count = houseReservationMapper.selectReservationCount(params);
		if (count > 0) { // 存在同样的进行中的预约
			return null;
		}
		houseReservation.formatDefault();
		houseMapper.raiseReservationCount(houseReservation.getHouse().getUuid()); // 相关房源的预约数+1
		String reservationUuid = UuidUtil.getUuidByTimestamp(5);
		houseReservation.setUuid(reservationUuid);
		houseReservation.setStatus(1);
		Long timestamp = new Date().getTime();
		houseReservation.setCreateTime(timestamp);
		int flag = houseReservationMapper.insertHouseReservation(houseReservation);
		if (flag == 0) {
			throw new Exception("插入HouseReservation失败");
		}
		// 创建预约时插入第一条状态流转信息
		HouseReservationStatus reservationStatus = new HouseReservationStatus();
		reservationStatus.setUuid(UuidUtil.getUuidByTimestamp(5));
		reservationStatus.setReservation(houseReservation);
		reservationStatus.setUser(houseReservation.getRenter());
		reservationStatus.setStatus(houseReservation.getStatus());
		reservationStatus.setRemark("");
		reservationStatus.setTimestamp(timestamp);
		int insertStatusFlag = houseReservationMapper.insertReservationStatus(reservationStatus);
		if (insertStatusFlag == 0) {
			throw new Exception("插入HouseReservationStatus失败");
		}
		return houseReservationMapper.selectHouseReservationByUuid(reservationUuid);
	}

	@Override
	public HouseReservation editHouseReservation(HouseReservation houseReservation) {
		int flag = houseReservationMapper.updateHouseReservation(houseReservation);
		return flag == 0 ? null : houseReservationMapper.selectHouseReservationByUuid(houseReservation.getUuid());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer updateReservationStatus(String reservationUuid, String userUuid, Integer status, String remark)
			throws Exception {
		HouseReservation reservation = new HouseReservation();
		reservation.setUuid(reservationUuid);
		reservation.setStatus(status);
		int flag = houseReservationMapper.updateHouseReservation(reservation);
		if (flag == 0) {
			throw new Exception("更新HouseReservation失败");
		}
		HouseReservationStatus reservationStatus = new HouseReservationStatus();
		String uuid = UuidUtil.getUuidByTimestamp(5);
		reservationStatus.setUuid(uuid);
		reservationStatus.setReservation(reservation);
		User user = new User();
		user.setUuid(userUuid);
		reservationStatus.setUser(user);
		reservationStatus.setStatus(status);
		reservationStatus.setRemark(remark);
		reservationStatus.setTimestamp(new Date().getTime());
		return houseReservationMapper.insertReservationStatus(reservationStatus);
	}
	
	@Override
	public List<House> getReservationHouseList(String ownerUuid, List<String> statusList, int page, int row) {
		int offset = (page - 1) * row;
		return houseReservationMapper.selectReservationHouses(ownerUuid, statusList, offset, row);
	}

	@Override
	public Integer getReservationCount(ReservationParams params) {
		return houseReservationMapper.selectReservationCount(params);
	}

}
