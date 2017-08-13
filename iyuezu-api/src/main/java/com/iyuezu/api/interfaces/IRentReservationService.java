package com.iyuezu.api.interfaces;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentReservation;
import com.iyuezu.mybatis.params.ReservationParams;

public interface IRentReservationService {

	public PageInfo<RentReservation> getRentReservations(ReservationParams params, int page, int row);
	
	public PageInfo<RentReservation> getRentReservationsWithRentInfo(ReservationParams params, int page, int row);
	
	public PageInfo<RentReservation> getRentReservationsWithHouse(ReservationParams params, int page, int row);

	public RentReservation getRentReservationByUuid(String uuid);

	public RentReservation createRentReservation(RentReservation rentReservation) throws Exception;

	public RentReservation editRentReservation(RentReservation rentReservation);
	
	public Integer updateReservationStatus(String reservationUuid, String userUuid, Integer status, String remark) throws Exception;
	
	public List<House> getReservationHouseList(String ownerUuid, List<String> statusList, int page, int row);
	
	public List<RentInfo> getReservationRentInfoList(String renterUuid, List<String> statusList, int page, int row);
	
	public Integer getReservationCount(ReservationParams params);

}
