package com.cox.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cox.service.model.User;

public class AuthFilter implements Filter{

	FilterConfig filterConfig;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		HttpSession session = httpRequest.getSession();
		User user = (User)session.getAttribute("user");
		if(!session.isNew() && user == null){
			throwForbidden(response);
		}else{
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	private void throwForbidden(ServletResponse res) throws IOException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.reset();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
