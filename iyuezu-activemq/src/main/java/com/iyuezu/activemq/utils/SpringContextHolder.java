package com.iyuezu.activemq.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextHolder {

	public static ApplicationContext context;

	public static synchronized void init() {
		if (context == null) {
			// 通过xml方式加载配置
			context = new ClassPathXmlApplicationContext("classpath:application-activemq.xml");
		}
	}

	public static <T> T getBean(Class<T> clazz) {
		if (context == null) {
			init();
		}
		return context.getBean(clazz);
	}

}
