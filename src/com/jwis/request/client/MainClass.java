package com.jwis.request.client;

import com.jwis.request.client.window.MainWindow;

public class MainClass {

	public static void main(String[] args) {
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
