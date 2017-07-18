package com.orange.clients;

import org.apache.log4j.Logger;

import com.orange.clients.ui.HttpWindow;

public class MainHttpClass {

	public static Logger logger = Logger.getLogger(MainHttpClass.class);
	
	public static void main(String[] args) {
		try {
			HttpWindow window = new HttpWindow();
			window.open();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
