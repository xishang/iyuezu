package com.iyuezu.activemq;

import com.iyuezu.activemq.consumer.FruitProducer;
import com.iyuezu.activemq.consumer.WhetherProducer;
import com.iyuezu.activemq.utils.SpringContextHolder;
import com.iyuezu.common.beans.ChatContentDto;

public class Main {

	public static void main(String[] args) {
		SpringContextHolder.init();
		try {
			FruitProducer producer1 = SpringContextHolder.getBean(FruitProducer.class);
			ChatContentDto content1 = new ChatContentDto();
			content1.setId("aaaaaaaa");
			content1.setChatId(1);
			content1.setContent("这是第1条Queue内容");
			producer1.sendMessage(content1);
			ChatContentDto content2 = new ChatContentDto();
			content2.setId("bbbbbb");
			content2.setChatId(1);
			content2.setContent("这是第2条Queue内容");
			producer1.sendMessage(content2);
			Thread.sleep(10 * 1000);
			WhetherProducer producer2 = SpringContextHolder.getBean(WhetherProducer.class);
			ChatContentDto content3 = new ChatContentDto();
			content3.setId("cccccccc");
			content3.setChatId(2);
			content3.setContent("这是第1条Topic内容");
			producer2.sendMessage(content3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(1);
	}

}
