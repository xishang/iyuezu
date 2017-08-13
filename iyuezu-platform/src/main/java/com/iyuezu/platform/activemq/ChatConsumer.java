package com.iyuezu.platform.activemq;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iyuezu.common.beans.ChatContentDto;
import com.iyuezu.mybatis.mapper.ChatMapper;
import com.iyuezu.platform.websocket.WebSocketSessionHolder;

import net.sf.json.JSONObject;

@Component("chatConsumer")
public class ChatConsumer implements MessageListener {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ChatMapper chatMapper;

	@Override
	public void onMessage(Message message) {
		ObjectMessage om = (ObjectMessage) message;
		try {
			Object key = om.getObjectProperty("key");
			JSONObject json = JSONObject.fromObject(key);
			logger.info("ActiveMQ接收到Chat(Topic)消息[" + json.toString() + "]");
			ChatContentDto content = (ChatContentDto) JSONObject.toBean(json, ChatContentDto.class);
			int chatId = content.getChatId();
			List<String> userUuids = chatMapper.selectUserUuidsByChatId(chatId);
			// 向聊天信息[chatId]所有已连接的用户发送消息
			for (String userUuid : userUuids) {
				WebSocketSessionHolder.sendMessage(userUuid, json.toString());
			}
		} catch (JMSException e) {
			logger.error("ActiveMQ接收Chat(Topic)消息失败", e);
		}
	}

}
