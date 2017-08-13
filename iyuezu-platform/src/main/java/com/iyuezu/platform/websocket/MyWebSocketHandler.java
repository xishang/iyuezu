package com.iyuezu.platform.websocket;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.iyuezu.api.interfaces.IChatService;
import com.iyuezu.common.beans.ChatContentDto;
import com.iyuezu.platform.activemq.ChatProducer;

import net.sf.json.JSONObject;

/**
 * WebSocket处理器, 3种方式
 * 1.继承TextWebSocketHandler
 * 2.继承BinaryWebSocketHandler
 * 3.实现WebSocketHandler
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ChatProducer chatProducer;
	
	@Autowired
	private IChatService chatService;

	/**
	 * 建立连接
	 * HandshakeInterceptor.beforeHandshake()方法中传入的attributes参数会存入session
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String uuid = (String) session.getAttributes().get("uuid");
		if (uuid != null) {
			logger.info("WebSocket:用户[" + uuid + "]建立连接, 保存session");
			WebSocketSessionHolder.add(uuid, session);
		}
	}

	/**
	 * 收到客户端消息
	 */
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (message.getPayloadLength() == 0) {
			return;
		}
		String uuid = (String) session.getAttributes().get("uuid");
		String content = message.getPayload().toString();
		logger.info("WebSocket:接收到用户[" + uuid + "]的消息, 内容[" + content + "]");
		// 上传到activeMQ, 以便所有服务接收
		JSONObject json = JSONObject.fromObject(content);
		ChatContentDto chatContent = (ChatContentDto) JSONObject.toBean(json, ChatContentDto.class);
		chatContent.setUserUuid(uuid);
		chatContent.setStatus(1);
		Date curTime = new Date();
		chatContent.setCreateTime(curTime.getTime());
		chatContent.setUpdateTime(curTime.getTime());
		// 刷新chat活动时间
		chatService.refreshChatActiveTime(chatContent.getChatId());
		// 存入mongodb, 无法返回id导致直接返回推送消息时不能回滚
		chatService.addChatContent(chatContent.getChatId(), uuid, chatContent.getType(), chatContent.getContent());
		chatProducer.sendMessage(chatContent);
	}

	/**
	 * 消息传输错误处理
	 */
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (session.isOpen()) {
			session.close();
		}
		String uuid = (String) session.getAttributes().get("uuid");
		logger.info("WebSocket:用户[" + uuid + "]消息传输错误, 移除session");
		WebSocketSessionHolder.remove(uuid);
	}

	/**
	 * 关闭连接后
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		String uuid = (String) session.getAttributes().get("uuid");
		logger.info("WebSocket:用户[" + uuid + "]连接已关闭, 移除session");
		WebSocketSessionHolder.remove(uuid);
	}

	/**
	 * 是否分段发送消息
	 */
	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}