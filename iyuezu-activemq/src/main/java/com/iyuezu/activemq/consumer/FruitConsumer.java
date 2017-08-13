package com.iyuezu.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.stereotype.Component;

@Component("fruitConsumer")
public class FruitConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ObjectMessage om = (ObjectMessage) message;
		try {
			Object key = om.getObjectProperty("key");
			System.out.println("fruitMessage:" + key);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
