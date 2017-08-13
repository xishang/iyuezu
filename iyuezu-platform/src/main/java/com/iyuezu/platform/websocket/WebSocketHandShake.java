package com.iyuezu.platform.websocket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.iyuezu.common.beans.User;

/**
 * websocket连接的拦截器, 2种方式
 * 1.实现接口HandshakeInterceptor, 实现beforeHandshake和afterHandshake函数
 * 2.继承HttpSessionHandshakeInterceptor, 重载beforeHandshake和afterHandshake函数
 */
public class WebSocketHandShake implements HandshakeInterceptor {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 客户端建立websocket连接时调用
	 * 
	 * @param attributes	数据传递容器
	 * 
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
			Map<String, Object> attributes) throws Exception {
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			Object obj = servletRequest.getServletRequest().getAttribute("user");
			String accessToken = servletRequest.getServletRequest().getParameter("access_token");
			if (obj == null) { // 未登录用户不允许建立websocket连接
				return false;
			} else {
				User user = (User) obj;
				attributes.put("uuid", user.getUuid());
				logger.info("WebSocket:用户[uuid:" + user.getUuid() + ", username:" + user.getUsername() + "]建立websocket连接");
			}
		}
		return true;
	}

	/**
	 * websocket连接断开时调用
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler,
			Exception exception) {
	}

}