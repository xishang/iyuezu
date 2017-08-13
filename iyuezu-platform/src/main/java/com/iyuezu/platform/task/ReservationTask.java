package com.iyuezu.platform.task;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.iyuezu.mybatis.mapper.SpecialMapper;

//@Component("reservationTask")
public class ReservationTask {
	
	private final int HOUSE_COUNT_1 = 5;
    private final int HOUSE_COUNT_7 = 3;
    private final int HOUSE_COUNT_30 = 2;
    private final int HOUSE_COUNT_L30 = 1;
    private final int RENT_COUNT_1 = 3;
    private final int RENT_COUNT_7 = 2;
    private final int RENT_COUNT_L7 = 1;
    
    private final long dayTs = 1000 * 60 * 60 * 24;
    
    long curTimestamp = new Date().getTime();
    long timestamp_1_Ago = curTimestamp - dayTs;
    long timestamp_7_Ago = curTimestamp - dayTs * 7;
    long timestamp_30_Ago = curTimestamp - dayTs * 30;

	public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
	
	@Autowired
	private SpecialMapper specialMapper;
	
	/**
	 * 定时任务(6hours/次): 增加预约数
	 */
	@Scheduled(cron = "* * 0/6 * * ?")
	@Transactional
	public void executeReservationTask() {
		/*--增加reservation count--*/
		specialMapper.raiseHouseReservationCount(timestamp_1_Ago, null, HOUSE_COUNT_1);
		specialMapper.raiseHouseReservationCount(timestamp_7_Ago, timestamp_1_Ago, HOUSE_COUNT_7);
		specialMapper.raiseHouseReservationCount(timestamp_30_Ago, timestamp_7_Ago, HOUSE_COUNT_30);
		specialMapper.raiseHouseReservationCount(null, timestamp_30_Ago, HOUSE_COUNT_L30);
		
		specialMapper.raiseRentInfoReservationCount(timestamp_1_Ago, null, RENT_COUNT_1);
		specialMapper.raiseRentInfoReservationCount(timestamp_7_Ago, timestamp_1_Ago, RENT_COUNT_7);
		specialMapper.raiseRentInfoReservationCount(null, timestamp_1_Ago, RENT_COUNT_L7);
	}

}
