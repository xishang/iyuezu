package com.iyuezu.activemq.config;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import net.sf.json.JSONObject;

public class ObjectMessageConverter implements MessageConverter {

	@Override
	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		ObjectMessage objectMessage = (ObjectMessage) message;
		return objectMessage.getObjectProperty("key");
	}

	/**
	 * 发送消息时调用
	 */
	@Override
	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		ObjectMessage objectMessage = session.createObjectMessage();
		objectMessage.setStringProperty("key", JSONObject.fromObject(object).toString());
		return objectMessage;
	}

}
