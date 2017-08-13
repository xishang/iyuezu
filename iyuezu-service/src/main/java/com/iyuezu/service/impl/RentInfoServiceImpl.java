package com.iyuezu.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IRentInfoService;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentInfoDto;
import com.iyuezu.common.utils.UuidUtil;
import com.iyuezu.mybatis.mapper.RentInfoMapper;
import com.iyuezu.mybatis.mapper.RentReservationMapper;
import com.iyuezu.mybatis.params.RentInfoParams;

@Service
public class RentInfoServiceImpl implements IRentInfoService {
	
	@Autowired
	private RentInfoMapper rentInfoMapper;
	
	@Autowired
	private RentReservationMapper rentReservationMapper;

	@Override
	public PageInfo<RentInfo> getRentInfos(RentInfoParams params, int page, int row) {
		PageHelper.startPage(page, row);
		List<RentInfo> rentInfos = rentInfoMapper.selectRentInfos(params);
		return new PageInfo<RentInfo>(rentInfos);
	}

	@Override
	public RentInfo getRentInfoByUuid(String uuid) {
		return rentInfoMapper.selectRentInfoByUuid(uuid);
	}

	@Override
	public RentInfoDto getRentInfoDtoByUuid(String uuid) {
		return rentInfoMapper.selectRentInfoDtoByUuid(uuid);
	}

	@Override
	public RentInfo createRentInfo(RentInfo rentInfo) {
		rentInfo.formatDefault();
//		允许租客设置隐私保护
//		String contactName = rentInfo.getContactName();
//		if (StringUtil.isEmpty(contactName) && StringUtil.isNotEmpty(rentInfo.getRenter().getUsername())) {
//			rentInfo.setContactName(rentInfo.getRenter().getUsername());
//		}
//		String contactPhone = rentInfo.getContactPhone();
//		if (StringUtil.isEmpty(contactPhone) && StringUtil.isNotEmpty(rentInfo.getRenter().getPhone())) {
//			rentInfo.setContactPhone(rentInfo.getRenter().getPhone());
//		}
		String uuid = UuidUtil.getUuidByTimestamp(5);
		rentInfo.setUuid(uuid);
		Long timestamp = new Date().getTime();
		rentInfo.setCreateTime(timestamp);
		rentInfo.setUpdateTime(timestamp);
		rentInfo.setStatus(1);
		int flag = rentInfoMapper.insertRentInfo(rentInfo);
		return flag == 0 ? null : rentInfoMapper.selectRentInfoByUuid(uuid);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public RentInfo editRentInfo(RentInfo rentInfo) {
		if (rentInfo.getStatus() != null && rentInfo.getStatus() == 0) { // 求租信息已经失效，将所有与该求租信息有关的预约状态置为0
			rentReservationMapper.disableStatusByRentUuid(rentInfo.getUuid());
		}
		rentInfo.setUpdateTime(new Date().getTime());
		int flag = rentInfoMapper.updateRentInfo(rentInfo);
		return flag == 0 ? null : rentInfoMapper.selectRentInfoByUuid(rentInfo.getUuid());
	}

}
