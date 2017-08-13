package com.iyuezu.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iyuezu.api.interfaces.IStatisticsService;
import com.iyuezu.mybatis.mapper.HouseReservationMapper;
import com.iyuezu.mybatis.mapper.RentReservationMapper;
import com.iyuezu.mybatis.params.ReservationParams;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

	@Autowired
	private HouseReservationMapper houseReservationMapper;

	@Autowired
	private RentReservationMapper rentReservationMapper;

	@Override
	public Map<String, Integer> getHandleMap(String userUuid, Integer userType) {
		Map<String, Integer> handleMap = new HashMap<String, Integer>();
		ReservationParams reservationParams = new ReservationParams();
		if (userType == 1) {
			reservationParams.setRenterUuid(userUuid);
		} else if (userType == 2) {
			reservationParams.setOwnerUuid(userUuid);
		}
		List<Integer> statusList = new ArrayList<Integer>();
		statusList.add(1);
		statusList.add(2);
		reservationParams.setStatusList(statusList);
		int rentReservationCount = rentReservationMapper.selectReservationCount(reservationParams);
		handleMap.put("rentReservationCount", rentReservationCount);
		reservationParams.getStatusList().add(4);
		int houseReservationCount = houseReservationMapper.selectReservationCount(reservationParams);
		handleMap.put("houseReservationCount", houseReservationCount);
		return handleMap;
	}

}
