package com.iyuezu.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentReservation;
import com.iyuezu.common.beans.RentReservationStatus;
import com.iyuezu.mybatis.params.ReservationParams;

public interface RentReservationMapper {
	
	public Integer selectReservationCount(ReservationParams params);

	public List<RentReservation> selectRentReservations(ReservationParams params);

	public List<RentReservation> selectRentReservationsWithRentInfo(ReservationParams params);

	public List<RentReservation> selectRentReservationsWithHouse(ReservationParams params);

	public RentReservation selectRentReservationByUuid(@Param("uuid") String uuid);

	public int insertRentReservation(RentReservation rentReservation);

	public int updateRentReservation(RentReservation rentReservation);

	public Integer disableStatusByHouseUuid(@Param("houseUuid") String houseUuid);

	public Integer disableStatusByRentUuid(@Param("rentUuid") String rentUuid);

	public Integer insertReservationStatus(RentReservationStatus reservationStatus);

	public List<House> selectReservationHouses(@Param("ownerUuid") String ownerUuid,
			@Param("statusList") List<String> statusList, @Param("offset") Integer offset, @Param("size") Integer size);
	
	public List<RentInfo> selectReservationRentInfos(@Param("renterUuid") String renterUuid,
			@Param("statusList") List<String> statusList, @Param("offset") Integer offset, @Param("size") Integer size);

}
