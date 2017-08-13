package com.iyuezu.api.interfaces;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseReservation;
import com.iyuezu.mybatis.params.ReservationParams;

public interface IHouseReservationService {
	
	public PageInfo<HouseReservation> getHouseReservations(ReservationParams params, int page, int row);
	
	public PageInfo<HouseReservation> getHouseReservationsWithRenter(ReservationParams params, int page, int row);
	
	public PageInfo<HouseReservation> getHouseReservationsWithHouse(ReservationParams params, int page, int row);
	
	public PageInfo<HouseReservation> getHouseReservationsWithoutRenter(ReservationParams params, int page, int row);
	
	public HouseReservation getHouseReservationByUuid(String uuid);
	
	public HouseReservation createHouseReservation(HouseReservation houseReservation) throws Exception;
	
	public HouseReservation editHouseReservation(HouseReservation houseReservation);
	
	public Integer updateReservationStatus(String reservationUuid, String userUuid, Integer status, String remark) throws Exception;
	
	public List<House> getReservationHouseList(String ownerUuid, List<String> statusList, int page, int row);
	
	public Integer getReservationCount(ReservationParams params);

}
