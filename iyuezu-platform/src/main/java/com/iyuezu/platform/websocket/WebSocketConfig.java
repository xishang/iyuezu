package com.iyuezu.platform.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * WebScoket配置处理器
 */
@Configuration
@EnableWebMvc
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

	@Autowired
	MyWebSocketHandler handler;

	/**
	 * 系统启动时注册
	 */
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// 注册原生ws协议
		registry.addHandler(handler, "/ws").addInterceptors(new WebSocketHandShake()).setAllowedOrigins("*");
		
		// 注册SockJs支持, 若浏览器不支持ws协议, 则使用sockjs发送
		registry.addHandler(handler, "/ws/sockjs").addInterceptors(new WebSocketHandShake()).withSockJS();
	}
}