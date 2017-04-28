package com.orange.clients;

import com.orange.clients.ui.MainWindow;
import com.orange.clients.ui.PingWindow;

public class MainClass {

	public static void main(String[] args) {
		try {
//			MainWindow window = new MainWindow();
			PingWindow window = new PingWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
