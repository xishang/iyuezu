package com.iyuezu.api.interfaces;

import com.github.pagehelper.PageInfo;
import com.iyuezu.common.beans.RentInfo;
import com.iyuezu.common.beans.RentInfoDto;
import com.iyuezu.mybatis.params.RentInfoParams;

public interface IRentInfoService {
	
	public PageInfo<RentInfo> getRentInfos(RentInfoParams params, int page, int row);
	
	public RentInfo getRentInfoByUuid(String uuid);
	
	public RentInfoDto getRentInfoDtoByUuid(String uuid);
	
	public RentInfo createRentInfo(RentInfo rentInfo);
	
	public RentInfo editRentInfo(RentInfo rentInfo);

}
