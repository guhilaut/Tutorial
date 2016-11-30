package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadWriteProps {
	
	public static void main(String[] args) {
		
		Properties inProps = new Properties();
		String location = getPropertiesPath();
		InputStream is = null;
		try{
			is = new FileInputStream(new File(location+"envGroups.properties"));
			inProps.load(is);
			is.close();
		} catch (Exception e) {
			throw new IllegalStateException("Bad things happend...", e);
		}
		try{
			FileOutputStream out = new FileOutputStream(new File(location+"envGroups.properties"));
			String value = (String)inProps.get("abc");
			if(value != null){
				inProps.setProperty("abc", value+",abc_prod");
				/*inProps.remove("abc");*/
				/*inProps.setProperty("ivr", "");*/
			}else{
				inProps.setProperty("abc", "");
			}
			inProps.store(out, null);
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String getPropertiesPath(){
		Properties properties = new Properties();
		String location = null;
		InputStream is = ReadWriteProps.class.getClassLoader().getResourceAsStream("application.properties");
		try {
			properties.load(is);
			location = properties.getProperty("app.props.location");
		} catch (IOException e) {
			throw new IllegalStateException("Not found location of properties file...", e);
		} catch (Exception e) {
			throw new IllegalStateException("Bad things happend...", e);
		}
		return location;
	}

}
