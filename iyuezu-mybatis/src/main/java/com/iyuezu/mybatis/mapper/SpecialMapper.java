package com.iyuezu.mybatis.mapper;

import org.apache.ibatis.annotations.Param;

public interface SpecialMapper {

	public Integer raiseHouseReservationCount(@Param("minCreateTime") Long minCreateTime,
			@Param("maxCreateTime") Long maxCreateTime, @Param("count") Integer count);

	public Integer raiseRentInfoReservationCount(@Param("minCreateTime") Long minCreateTime,
			@Param("maxCreateTime") Long maxCreateTime, @Param("count") Integer count);

}
