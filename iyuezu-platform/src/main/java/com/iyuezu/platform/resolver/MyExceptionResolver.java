package com.iyuezu.platform.resolver;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MyExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write("{\"code\":\"999\", \"msg\":\"服务器出错\"}");
			writer.close();
		} catch (IOException e) {

		}
		return null;
	}

}
