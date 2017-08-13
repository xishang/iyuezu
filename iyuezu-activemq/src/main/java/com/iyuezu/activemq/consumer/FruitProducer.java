package com.iyuezu.activemq.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.iyuezu.common.beans.ChatContentDto;

@Component
public class FruitProducer {

	@Autowired
	@Qualifier("queueJmsTemplate")
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(ChatContentDto content) {
		jmsTemplate.convertAndSend("Fruit.Shop", content);
	}

}
