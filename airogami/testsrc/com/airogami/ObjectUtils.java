package com.airogami;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class ObjectUtils {

	private static final XStream jsonXStream = 
		    new XStream(new JsonHierarchicalStreamDriver());
	
	public static  void printObject(Object obj){
		System.out.println(jsonXStream.toXML(obj));
	}
}
