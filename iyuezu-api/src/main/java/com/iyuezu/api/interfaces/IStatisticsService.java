package com.iyuezu.api.interfaces;

import java.util.Map;

public interface IStatisticsService {
	
	public Map<String, Integer> getHandleMap(String userUuid, Integer userType);

}
