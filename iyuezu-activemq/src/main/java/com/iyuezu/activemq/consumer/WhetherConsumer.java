package com.iyuezu.activemq.consumer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.stereotype.Component;

@Component("whetherConsumer")
public class WhetherConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		ObjectMessage om = (ObjectMessage) message;
		try {
			Object key = om.getObjectProperty("key");
			System.out.println("whetherMessage:" + key);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
