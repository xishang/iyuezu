package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseDto;
import com.iyuezu.mybatis.params.HouseParams;

public interface HouseMapper {

	public List<House> selectHouses(HouseParams params);

	public House selectHouseByUuid(@Param("uuid") String uuid);
	
	public HouseDto selectHouseWithCommentsByUuid(@Param("uuid") String uuid);
	
	public HouseDto selectHouseDtoByUuid(@Param("uuid") String uuid);

	public Integer insertHouse(House house);

	public Integer updateHouse(House house);
	
	public Integer raiseReservationCount(@Param("uuid") String uuid);
	
	public Integer reduceReservationCount(@Param("uuid") String uuid);
	
	public Integer raiseCommentCount(@Param("uuid") String uuid);
	
	public Integer reduceCommentCount(@Param("uuid") String uuid);
	
	public Integer updateHouseAvgScore(@Param("uuid") String uuid);

}
