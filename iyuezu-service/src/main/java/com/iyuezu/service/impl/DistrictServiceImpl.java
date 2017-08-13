package com.iyuezu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iyuezu.api.interfaces.IDistrictService;
import com.iyuezu.common.beans.District;
import com.iyuezu.mybatis.mapper.DistrictMapper;

@Service
public class DistrictServiceImpl implements IDistrictService {
	
	@Autowired
	private DistrictMapper districtMapper;

	@Override
	public District getDistrictById(int id) {
		return districtMapper.selectDistrictById(id);
	}

	@Override
	public PageInfo<District> getDistricts(District district, int page, int row) {
		PageHelper.startPage(page, row);
		List<District> districts = districtMapper.selectDistricts(district);
		return new PageInfo<District>(districts);
	}
	
	@Override
	public List<District> getDistricts(District district) {
		return districtMapper.selectDistricts(district);
	}
	
	@Override
	public List<District> getAllDistricts() {
		return districtMapper.selectDistricts(null);
	}
	
	@Override
	public List<District> getValidDistricts() {
		District district = new District();
		district.setStatus(1);
		return districtMapper.selectDistricts(district);
	}

	@Override
	public int createDistrict(District district) {
		return districtMapper.insertDistrict(district);
	}

	@Override
	public int editDistrict(District district) {
		return districtMapper.updateDistrict(district);
	}

}
