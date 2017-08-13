package com.iyuezu.platform.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.iyuezu.common.beans.User;
import com.iyuezu.redis.impl.UserRedisImpl;

public class AccessFilter implements Filter {

	private UserRedisImpl userRedis;

	private final String[] WHITE_URL = { "/user/.*", "/house/.*", "/rent/.*", "/district/.*", ".*\\..*" };

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String url = request.getRequestURI().toString();
		String token = request.getParameter("access_token");
		User user = getUserByToken(token);
		if (user == null) {
			if (isWhiteUrl(url)) {
				chain.doFilter(request, response); 
				return;
			}
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write("{\"code\":\"9998\", \"msg\":\"token invalid\"}");
			writer.flush();
			writer.close();
			return;
		}
		request.setAttribute("user", user);
		chain.doFilter(request, response);
		return;
	}

	private boolean isWhiteUrl(String url) {
		return checkUrl(WHITE_URL, url);
	}

	private boolean checkUrl(String[] regexs, String url) {
		for (String regex : regexs) {
			if (url.matches(regex)) {
				return true;
			}
		}
		return false;
	}

	private User getUserByToken(String accessToken) {
		if (accessToken == null) {
			return null;
		}
		User user = userRedis.getUserByToken(accessToken);
		if (user == null) {
			return null;
		}
		userRedis.refreshTokenExpireTime(accessToken);
		return user;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(context);
		this.userRedis = (UserRedisImpl) wac.getBean("userRedisImpl");
	}

}
