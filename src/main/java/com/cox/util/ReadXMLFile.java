package com.cox.util;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadXMLFile {
	
	public static void  main(String[] args) {
		List<String> data = marshal("simple", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <route customId=\"true\" id=\"Assess-Financial-Risk-Services-Route\" xmlns=\"http://camel.apache.org/schema/spring\"> <from uri=\"cxf:bean:AssessFinancialRiskServicesEndPoint\"/> <interceptFrom uri=\"cxf*\" id=\"interceptFrom10\"> <setHeader headerName=\"CamelInterceptedEndpoint\" id=\"setHeader33\"> <expressionDefinition></expressionDefinition> </setHeader> <process id=\"process34\"/> </interceptFrom> <doTry id=\"doTry21\"> <choice id=\"choice54\"> <when id=\"when65\"> <simple>${in.header.operationName} == 'getPastDueWriteOff'</simple> <to uri=\"direct-vm:GetPastDueWriteOffEndPoint\" id=\"to363\"/> </when> <when id=\"when66\"> <simple>${in.header.operationName} == 'getCreditAndDeposit'</simple> <to uri=\"direct-vm:GetCreditAndDepositEndPoint\" id=\"to364\"/> </when> <otherwise id=\"otherwise19\"> <log message=\"Endpoint not found\" id=\"log9\"/> </otherwise> </choice> </doTry> </route>");
		for(String operationName : data){
			System.out.println(operationName);
			
		}
	}
	public static List<String> marshal(final String tagName,String inputXml) {
		final List<String> rawOperation = new ArrayList<String>();
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean simple = false;
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					if (qName.equalsIgnoreCase(tagName)) {
						simple = true;
					}


				}
				public void characters(char ch[], int start, int length)
						throws SAXException {
					if (simple) {
						rawOperation.add(new String(ch, start, length));
						simple = false;
					}
				}
			};

			saxParser.parse( new ByteArrayInputStream(inputXml.getBytes(StandardCharsets.UTF_8)), handler);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rawOperation;
	}
	
	public static List<String> getRawOperation(List<String> rawOperation) {
		List<String> operations	=	new ArrayList<String>();
		for(String strOperation:rawOperation){
			if(strOperation.contains("==")){
				strOperation = strOperation.split("==")[1];
				operations.add(strOperation.replace("'", "").trim());
			}
		}
		return operations;
	}

}