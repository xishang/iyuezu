package com.iyuezu.api.interfaces;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.District;

public interface IDistrictService {
	
	public District getDistrictById(int id);
	
	public PageInfo<District> getDistricts(District district, int page, int row);
	
	public List<District> getDistricts(District district);
	
	public List<District> getAllDistricts();
	
	public List<District> getValidDistricts();
	
	public int createDistrict(District district);
	
	public int editDistrict(District district);

}
