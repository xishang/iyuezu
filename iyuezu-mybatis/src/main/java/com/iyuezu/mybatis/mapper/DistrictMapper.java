package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.iyuezu.common.beans.District;

public interface DistrictMapper {

	public Integer insertDistrict(District district);

	public Integer updateDistrict(District district);

	@Select("select * from districts where id = #{id}")
	public District selectDistrictById(@Param("id") Integer id);

	public List<District> selectDistricts(District district);

}
