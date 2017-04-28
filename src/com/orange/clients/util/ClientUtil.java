package com.orange.clients.util;

import java.awt.Toolkit;

public class ClientUtil {
	public static int getScreenWidth(){
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	public static int getScreenHeight(){
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
}
