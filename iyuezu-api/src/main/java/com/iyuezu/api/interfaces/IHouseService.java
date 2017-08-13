package com.iyuezu.api.interfaces;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.House;
import com.iyuezu.common.beans.HouseDto;
import com.iyuezu.mybatis.params.HouseParams;

public interface IHouseService {

	public PageInfo<House> getHouses(HouseParams params, int page, int row);

	public House getHouseByUuid(String uuid);

	public HouseDto getHouseWithCommentByUuid(String uuid);

	public HouseDto getHouseDtoByUuid(String uuid);

	public House createHouse(House house);

	public House editHouse(House house);

}
