package com.iyuezu.mongo.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.iyuezu.mongo.config.SpringMongoConfig;

public class SpringContextHolder {

	public static ApplicationContext context;

	public static synchronized void init() {
		if (context == null) {
			// 通过注册方式加载配置
			context = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
			
			// 通过xml方式加载配置
			// context = new GenericXmlApplicationContext("classpath:application-mongo.xml");
			
			// 完成初始化工作
//			context.start();
		}
	}
	
	public static <T> T getBean(Class<T> clazz) {
		if (context == null) {
			init();
		}
		return context.getBean(clazz);
	}

}
