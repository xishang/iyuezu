package com.iyuezu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IRentReservationService;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentReservation;
import com.iyuezu.common.beans.RentReservationStatus;
import com.iyuezu.common.beans.User;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.RentInfoMapper;
import com.iyuezu.mybatis.mapper.RentReservationMapper;
import com.iyuezu.mybatis.params.ReservationParams;

@Service
public class RentReservationServiceImpl implements IRentReservationService {

	@Autowired
	private RentReservationMapper rentReservationMapper;

	@Autowired
	private RentInfoMapper rentInfoMapper;

	@Override
	public PageInfo<RentReservation> getRentReservations(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<RentReservation> reservations = rentReservationMapper.selectRentReservations(params);
		return new PageInfo<RentReservation>(reservations);
	}

	@Override
	public PageInfo<RentReservation> getRentReservationsWithRentInfo(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<RentReservation> reservations = rentReservationMapper.selectRentReservationsWithRentInfo(params);
		return new PageInfo<RentReservation>(reservations);
	}

	@Override
	public PageInfo<RentReservation> getRentReservationsWithHouse(ReservationParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<RentReservation> reservations = rentReservationMapper.selectRentReservationsWithHouse(params);
		return new PageInfo<RentReservation>(reservations);
	}

	@Override
	public RentReservation getRentReservationByUuid(String uuid) {
		return rentReservationMapper.selectRentReservationByUuid(uuid);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public RentReservation createRentReservation(RentReservation rentReservation) throws Exception {
		ReservationParams params = new ReservationParams();
		params.setRentUuid(rentReservation.getRentInfo().getUuid());
		params.setHouseUuid(rentReservation.getHouse().getUuid());
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1);
		statusList.add(2);
		params.setStatusList(statusList);
		int count = rentReservationMapper.selectReservationCount(params);
		if (count > 0) { // 存在同样的进行中的预约
			return null;
		}
		rentReservation.formatDefault();
		rentInfoMapper.raiseReservationCount(rentReservation.getRentInfo().getUuid());
		String reservationUuid = UuidUtil.getUuidByTimestamp(5);
		rentReservation.setUuid(reservationUuid);
		rentReservation.setStatus(1);
		Long timestamp = new Date().getTime();
		rentReservation.setCreateTime(timestamp);
		int flag = rentReservationMapper.insertRentReservation(rentReservation);
		if (flag == 0) {
			throw new Exception();
		}
		RentReservationStatus reservationStatus = new RentReservationStatus();
		reservationStatus.setUuid(UuidUtil.getUuidByTimestamp(5));
		reservationStatus.setReservation(rentReservation);
		reservationStatus.setUser(rentReservation.getHouse().getOwner());
		reservationStatus.setStatus(rentReservation.getStatus());
		reservationStatus.setRemark("");
		reservationStatus.setTimestamp(timestamp);
		int insertStatusFlag = rentReservationMapper.insertReservationStatus(reservationStatus);
		if (insertStatusFlag == 0) {
			throw new Exception();
		}
		return rentReservationMapper.selectRentReservationByUuid(reservationUuid);
	}

	@Override
	public RentReservation editRentReservation(RentReservation rentReservation) {
		int flag = rentReservationMapper.updateRentReservation(rentReservation);
		return flag == 0 ? null : rentReservationMapper.selectRentReservationByUuid(rentReservation.getUuid());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer updateReservationStatus(String reservationUuid, String userUuid, Integer status, String remark)
			throws Exception {
		RentReservation reservation = new RentReservation();
		reservation.setUuid(reservationUuid);
		reservation.setStatus(status);
		int flag = rentReservationMapper.updateRentReservation(reservation);
		if (flag == 0) {
			throw new Exception();
		}
		RentReservationStatus reservationStatus = new RentReservationStatus();
		String uuid = UuidUtil.getUuidByTimestamp(5);
		reservationStatus.setUuid(uuid);
		reservationStatus.setReservation(reservation);
		User user = new User();
		user.setUuid(userUuid);
		reservationStatus.setUser(user);
		reservationStatus.setStatus(status);
		reservationStatus.setRemark(remark);
		reservationStatus.setTimestamp(new Date().getTime());
		return rentReservationMapper.insertReservationStatus(reservationStatus);
	}
	
	@Override
	public List<House> getReservationHouseList(String ownerUuid, List<String> statusList, int page, int row) {
		int offset = (page - 1) * row;
		return rentReservationMapper.selectReservationHouses(ownerUuid, statusList, offset, row);
	}

	@Override
	public List<RentInfo> getReservationRentInfoList(String renterUuid, List<String> statusList, int page, int row) {
		int offset = (page - 1) * row;
		return rentReservationMapper.selectReservationRentInfos(renterUuid, statusList, offset, row);
	}

	@Override
	public Integer getReservationCount(ReservationParams params) {
		return rentReservationMapper.selectReservationCount(params);
	}

}
