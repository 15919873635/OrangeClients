package com.orange.clients;

import com.orange.clients.ui.MainWindow;

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
