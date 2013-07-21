package com.airogami.presentation.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AirogamiUtils {

	//not copy null values
	public static Map<String, Object> describe(Object obj){
		Method[] methods = obj.getClass().getDeclaredMethods();
		Map<String, Object> map = new HashMap<String, Object>(methods.length / 2);
		for(Method method : methods){			
			String name = method.getName();			
			if(name.startsWith("get")){
				String c = name.substring(3, 4).toLowerCase();
				try {
					Object value = method.invoke(obj);
					if(value != null){
						map.put(c + name.substring(4),value);
					}
					
				} catch (IllegalArgumentException e) {
					//e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				} catch (IllegalAccessException e) {
					//e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				} catch (InvocationTargetException e) {
					//e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
			}
			
		}
		return map;
	}
}
