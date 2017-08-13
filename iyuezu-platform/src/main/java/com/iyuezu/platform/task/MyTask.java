package com.iyuezu.platform.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component("task")
public class MyTask {

	public static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 10);
	
	/**
	 * 定时任务(10min/次)
	 */
	@Scheduled(cron = "0/600 * * * * ?")
	@Transactional
	public void executeTask() {
		//task
	}

}
