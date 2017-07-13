package com.orange.clients.util;

import java.awt.Toolkit;

public class ClientUtil {
	public static int getScreenWidth(){
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	public static int getScreenHeight(){
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	
	public static boolean isJSONArray(String targetUri){
		return targetUri.startsWith("{") && targetUri.endsWith("}");
	}
	
	public static boolean isJSONObject(String targetUri){
		return targetUri.startsWith("[") && targetUri.endsWith("]");
	}
}
