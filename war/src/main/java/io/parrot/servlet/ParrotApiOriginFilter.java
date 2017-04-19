package io.parrot.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParrotApiOriginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		res.addHeader("Access-Control-Allow-Headers", "Content-Type");
		Class[] interfaces = request.getClass().getInterfaces();
		boolean isHttpServletRequest = false;
		for (Class interf: interfaces) {
			isHttpServletRequest = interf.getClass().getName().equals(HttpServletRequest.class.getName());
		}
		isHttpServletRequest = isHttpServletRequest || request instanceof HttpServletRequest;
		if (isHttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			ParrotHttpServletRequest galileoRequest = new ParrotHttpServletRequest(httpServletRequest);
			chain.doFilter(galileoRequest, res);
		} else {
			chain.doFilter(request, res);
		}
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
