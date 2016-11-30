package com.cox.util;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SessionUtils {
	
	public static void logout(HttpServletRequest request) {
		HttpSession hs = request.getSession();
		Enumeration<String> e = hs.getAttributeNames();
		while (e.hasMoreElements()) {
			String attr = e.nextElement();
			hs.setAttribute(attr, null);
		}
		removeCookies(request);
		hs.invalidate();
	}

	public static void removeCookies(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
			}
		}
	}
	
	public static void setHeaderForCors(HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
	}
}
