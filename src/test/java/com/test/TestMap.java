package com.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMap {
	
	static Map<String, List<String>> serverMap = new HashMap<String, List<String>>();
	
	public static void main(String[] args) {
		String[] arr = new String[10];
		List<String> list = Arrays.asList(arr);
		serverMap.put("env", list);
		serverMap.get("env").add("abc");
		System.out.println("serverMAp >> "+serverMap);
	}
}
