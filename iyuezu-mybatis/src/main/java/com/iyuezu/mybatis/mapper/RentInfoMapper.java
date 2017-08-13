package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentInfoDto;
import com.iyuezu.mybatis.params.RentInfoParams;

public interface RentInfoMapper {
	
	public List<RentInfo> selectRentInfos(RentInfoParams params);
	
	public RentInfo selectRentInfoByUuid(@Param("uuid") String uuid);
	
	public RentInfoDto selectRentInfoDtoByUuid(@Param("uuid") String uuid);
	
	public Integer insertRentInfo(RentInfo info);
	
	public Integer updateRentInfo(RentInfo info);
	
	public Integer raiseReservationCount(@Param("uuid") String uuid);
	
	public Integer reduceReservationCount(@Param("uuid") String uuid);

}
