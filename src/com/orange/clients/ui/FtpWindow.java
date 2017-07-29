package com.orange.clients.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;

import com.orange.clients.constant.WindowConstant;
import com.orange.clients.util.ClientUtil;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Table;

public class FtpWindow {
	protected Shell shell;
	private Display display;
	private Tray tray;
	private Color mainColor;
	private Image image = new Image(display, WindowConstant.ICON_HTTP);
	private Image showView = new Image(display, WindowConstant.ICON_SHOWVIEW);
	private Image hide = new Image(display, WindowConstant.ICON_HIDE);
	private Image exit = new Image(display, WindowConstant.ICON_EXIT);
	private Cursor cursor;
	
	private int SHEELH;
	private int SHEELW;
	private Text hostText;
	private Text userNameText;
	private Text userPasswordText;
	private Text portText;
	private Table table;
	private Table table_1;
	
	private int BUTTON_WIDTH_1;
	private int TEXT_WIDTH;
	/**
	 * Open the window.
	 */
	public void open() {
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		closeWindow();
	}
	
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		display = Display.getDefault();
		shell = new Shell(SWT.MIN|SWT.CLOSE);
		tray = display.getSystemTray();
		mainColor = new Color(display, WindowConstant.MAIN_BACkGROUND_COLOR);
		cursor = new Cursor(display, SWT.CURSOR_HAND);
		initWindow();
		initWinContent();
	}
	
	private void initWinContent(){
		
		Label hostLabel = new Label(shell, SWT.NONE);
		hostLabel.setBounds(5, 13, WindowConstant.LABEL_WIDTH, 17);
		hostLabel.setText("Host:");
		hostLabel.setBackground(mainColor);
		
		int hostTextLeft = hostLabel.getBounds().x + hostLabel.getBounds().width + 10;
		hostText = new Text(shell, SWT.BORDER);
		hostText.setBounds(hostTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int userNameLabelLeft = hostText.getBounds().x + hostText.getBounds().width + 10;
		Label userNameLabel = new Label(shell, SWT.NONE);
		userNameLabel.setBounds(userNameLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		userNameLabel.setText("User:");
		
		int userNameTextLeft = userNameLabel.getBounds().x + userNameLabel.getBounds().width + 10;
		userNameText = new Text(shell, SWT.BORDER);
		userNameText.setBounds(userNameTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int userPasswordLabelLeft = userNameText.getBounds().x + userNameText.getBounds().width + 10;
		Label userPasswordLabel = new Label(shell, SWT.NONE);
		userPasswordLabel.setBounds(userPasswordLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		userPasswordLabel.setText("Password:");
		
		int userPasswordTextLeft = userPasswordLabel.getBounds().x + userPasswordLabel.getBounds().width + 10;
		userPasswordText = new Text(shell, SWT.BORDER);
		userPasswordText.setBounds(userPasswordTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int portLabelLeft = userPasswordText.getBounds().x + userPasswordText.getBounds().width + 10;
		Label portLabel = new Label(shell, SWT.NONE);
		portLabel.setBounds(portLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		portLabel.setText("Port:");
		
		int portTextLeft = portLabel.getBounds().x + portLabel.getBounds().width + 10;
		portText = new Text(shell, SWT.BORDER);
		portText.setBounds(portTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int connectButtonLeft = portText.getBounds().x + portText.getBounds().width + 10;
		Button connectButton = new Button(shell, SWT.NONE);
		connectButton.setBounds(connectButtonLeft, 10, BUTTON_WIDTH_1, WindowConstant.BUTTON_HEIGHT);
		connectButton.setText("Connect");
		connectButton.setCursor(cursor);
		
		Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(0, 45, 358, 201);
		
		Tree tree_1 = new Tree(shell, SWT.BORDER);
		tree_1.setBounds(396, 45, 367, 201);
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setBounds(0, 252, 358, 149);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		table_1 = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setBounds(396, 252, 367, 149);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		for (int i = 0; i < 3; i++)  
        {
            TreeItem temp = new TreeItem(tree, SWT.NONE);  
            temp.setText("root-" + (char) ('a' + i));  
            new TreeItem(temp, SWT.NONE).setText("sub of " + temp.getText());  
        }  
	}
	
	private void initWindow(){
		shell.setText(WindowConstant.MAIN_FTP_WINDOW);
		shell.setBackground(mainColor);
		shell.setImage(image);
		int screenH = (int)ClientUtil.getScreenHeight();
		int screenW = (int)ClientUtil.getScreenWidth();
		
		SHEELH = screenH * 4 / 5;
		SHEELW = screenW * 4 / 5;
		SHEELH = (SHEELH >= WindowConstant.MIN_WINDOW_HEIGHT) ? SHEELH : WindowConstant.MIN_WINDOW_HEIGHT;
		SHEELW = (SHEELW >= WindowConstant.MIN_WINDOW_WIDTH) ? SHEELW : WindowConstant.MIN_WINDOW_WIDTH;
		
		BUTTON_WIDTH_1 = ((SHEELW * 8 / 100) > WindowConstant.MIN_BUTTON_WIDTH_1) ? (SHEELW * 8 / 100) : WindowConstant.MIN_BUTTON_WIDTH_1;
		
		TEXT_WIDTH = ((SHEELW * 80 / 100) - 4 * WindowConstant.LABEL_WIDTH) / 4;
		
        shell.setSize(SHEELW, SHEELH);
        shell.setLocation((screenW - SHEELW) / 2, (screenH - SHEELH) / 2);
	}
	
	private void closeWindow(){
		shell.dispose();
		tray.dispose();
		image.dispose();
		showView.dispose();
		hide.dispose();
		exit.dispose();
		display.dispose();
	}
}
