package com.orange.clients;

import org.apache.log4j.Logger;

import com.orange.clients.ui.MainWindow;

public class MainClass {

	public static Logger logger = Logger.getLogger(MainClass.class);
	
	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
