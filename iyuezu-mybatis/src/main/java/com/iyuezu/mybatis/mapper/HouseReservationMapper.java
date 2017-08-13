package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseReservation;
import com.iyuezu.common.beans.HouseReservationStatus;
import com.iyuezu.mybatis.params.ReservationParams;

public interface HouseReservationMapper {
	
	public Integer selectReservationCount(ReservationParams params);

	public List<HouseReservation> selectHouseReservations(ReservationParams params);

	public List<HouseReservation> selectHouseReservationsWithRenter(ReservationParams params);

	public List<HouseReservation> selectHouseReservationsWithHouse(ReservationParams params);
	
	public List<HouseReservation> selectHouseReservationsWithoutRenter(ReservationParams params);

	public HouseReservation selectHouseReservationByUuid(@Param("uuid") String uuid);

	public Integer insertHouseReservation(HouseReservation houseReservation);

	public Integer updateHouseReservation(HouseReservation houseReservation);

	public Integer disableStatusByHouseUuid(@Param("houseUuid") String houseUuid);

	public Integer insertReservationStatus(HouseReservationStatus reservationStatus);

	public List<House> selectReservationHouses(@Param("ownerUuid") String ownerUuid,
			@Param("statusList") List<String> statusList, @Param("offset") Integer offset, @Param("size") Integer size);

}
