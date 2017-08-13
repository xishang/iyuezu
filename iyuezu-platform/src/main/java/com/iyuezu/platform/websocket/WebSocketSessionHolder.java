package com.iyuezu.platform.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class WebSocketSessionHolder {

	private static Logger logger = LoggerFactory.getLogger(WebSocketSessionHolder.class);

	private static ExecutorService threadPool = Executors.newCachedThreadPool();

	// 以<uuid, session>格式保存
	private static Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();

	/**
	 * 保存一个连接
	 */
	public static void add(String uuid, WebSocketSession session) {
		clients.put(uuid, session);
	}

	/**
	 * 获取一个连接
	 */
	public static WebSocketSession get(String uuid) {
		return clients.get(uuid);
	}

	/**
	 * 移除一个连接
	 */
	public static void remove(String uuid) {
		clients.remove(uuid);
	}

	/**
	 * 是否连接
	 */
	public static boolean hasConnection(String uuid) {
		return clients.containsKey(uuid);
	}

	/**
	 * 获取连接数的数量
	 */
	public static int getSize() {
		return clients.size();
	}

	/**
	 * 向指定用户发送消息
	 * 
	 * @param uuid
	 * @param message
	 */
	public static void sendMessage(String uuid, String message) {
		if (!hasConnection(uuid)) {
			logger.info("WebSocket:用户[" + uuid + "]暂未连接, 发送信息[" + message + "]失败");
			return;
		}
		WebSocketSession session = get(uuid);
		sendMessage(uuid, session, message);
	}

	/**
	 * 发送广播信息
	 * 
	 * @param message
	 */
	public static void broadcast(String message) {
		logger.info("WebSocket:发送广播信息[" + message + "]");
		for (Entry<String, WebSocketSession> entry : clients.entrySet()) {
			String uuid = entry.getKey();
			WebSocketSession session = entry.getValue();
			sendMessage(uuid, session, message);
		}
	}

	public static void sendMessage(final String uuid, final WebSocketSession session, final String message) {
		logger.info("WebSocket:向用户[" + uuid + "]发送信息[" + message + "]");
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					session.sendMessage(new TextMessage(message));
				} catch (IOException e) {
					// 发送失败, 移除有问题的连接
					remove(uuid);
					logger.error("WebSocket:向用户[" + uuid + "]发送信息[" + message + "]失败", e);
				}
			}
		});
	}

}
