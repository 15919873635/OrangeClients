package com.orange.clients;

import org.apache.log4j.Logger;

import com.orange.clients.ui.FtpWindow;

public class MainFtpClass {
	
	public static Logger logger = Logger.getLogger(MainHttpClass.class);
	
	public static void main(String[] args) {
		try {
			FtpWindow window = new FtpWindow();
			window.open();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
