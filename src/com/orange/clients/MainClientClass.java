package com.orange.clients;

import com.orange.clients.ui.ClientWindow;

public class MainClientClass {

	public static void main(String[] args) {
		try {
			ClientWindow window = new ClientWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
