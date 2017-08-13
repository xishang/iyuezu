package com.iyuezu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IHouseService;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseDto;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.HouseMapper;
import com.iyuezu.mybatis.mapper.HouseReservationMapper;
import com.iyuezu.mybatis.mapper.RentReservationMapper;
import com.iyuezu.mybatis.params.HouseParams;

@Service
public class HouseServiceImpl implements IHouseService {

	@Autowired
	private HouseMapper houseMapper;
	
	@Autowired
	private HouseReservationMapper houseReservationMapper;
	
	@Autowired
	private RentReservationMapper rentReservationMapper;
	
	@Override
	public PageInfo<House> getHouses(HouseParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<House> houses = houseMapper.selectHouses(params);
		return new PageInfo<House>(houses);
	}

	@Override
	public House getHouseByUuid(String uuid) {
		return houseMapper.selectHouseByUuid(uuid);
	}

	@Override
	public HouseDto getHouseWithCommentByUuid(String uuid) {
		return houseMapper.selectHouseWithCommentsByUuid(uuid);
	}

	@Override
	public HouseDto getHouseDtoByUuid(String uuid) {
		return houseMapper.selectHouseDtoByUuid(uuid);
	}

	@Override
	public House createHouse(House house) {
		house.formatDefault();
//		允许隐私保护
//		String contactName = house.getContactName();
//		if (StringUtil.isEmpty(contactName) && StringUtil.isNotEmpty(house.getOwner().getUsername())) {
//			house.setContactName(house.getOwner().getUsername());
//		}
//		String contactPhone = house.getContactPhone();
//		if (StringUtil.isEmpty(contactPhone) && StringUtil.isNotEmpty(house.getOwner().getPhone())) {
//			house.setContactPhone(house.getOwner().getPhone());
//		}
		String uuid = UuidUtil.getUuidByTimestamp(5); // uuid为时间戳+5位随机数
		house.setUuid(uuid);
		Long timestamp = new Date().getTime();
		house.setCreateTime(timestamp);
		house.setUpdateTime(timestamp);
		house.setFreeTime(timestamp);
		int flag = houseMapper.insertHouse(house);
		return flag == 0 ? null : houseMapper.selectHouseByUuid(house.getUuid());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public House editHouse(House house) {
		if (house.getStatus() != null && house.getStatus() == 0) {
			// 把所有与该房源有关的预约信息状态置为0
			houseReservationMapper.disableStatusByHouseUuid(house.getUuid());
			rentReservationMapper.disableStatusByHouseUuid(house.getUuid());
		}
		house.setUpdateTime(new Date().getTime());
		int flag = houseMapper.updateHouse(house);
		return flag == 0 ? null : houseMapper.selectHouseByUuid(house.getUuid());
	}

}
