package com.orange.clients.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

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
	private Combo protocolCombo;
	private Cursor cursor;
	
	private int SHEELH;
	private int SHEELW;
	private int BUTTON_WIDTH_1;
	private int URLTEXT_WIDTH;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Table table;
	private Table table_1;
	
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
		initTray();
		initWindow();
		initWinContent();
	}
	
	private void initWinContent(){
		protocolCombo = new Combo(shell, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
//		protocolCombo.setBounds(protocolComboLeft, 10, COMBOBOX_WIDTH, WindowConstant.BUTTON_HEIGHT);
		protocolCombo.add(WindowConstant.REQUEST_HTTP);
		protocolCombo.add(WindowConstant.REQUEST_HTTPS);
		protocolCombo.setText(WindowConstant.REQUEST_HTTP);
		protocolCombo.setCursor(cursor);
		
		int methodLabelLeft = protocolCombo.getBounds().x + protocolCombo.getBounds().width + 10;
		Label methodLabel = new Label(shell, SWT.NONE);
		methodLabel.setBounds(0, 10, WindowConstant.LABEL_WIDTH, 17);
		methodLabel.setText("Host:");
		methodLabel.setBackground(mainColor);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(61, 7, 112, 23);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(190, 10, 42, 17);
		lblNewLabel.setText("User");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(238, 7, 105, 23);
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBounds(354, 10, 61, 17);
		lblNewLabel_1.setText("Password");
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(421, 7, 96, 23);
		
		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setBounds(523, 10, 35, 17);
		lblNewLabel_2.setText("Port");
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(564, 7, 96, 23);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(683, 3, 80, 27);
		btnNewButton.setText("Connect");
		
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
		
		
		URLTEXT_WIDTH = SHEELW - WindowConstant.LABEL_WIDTH - BUTTON_WIDTH_1 * 2 - 50 ;
		
        shell.setSize(SHEELW, SHEELH);
        shell.setLocation((screenW - SHEELW) / 2, (screenH - SHEELH) / 2);
	}
	
	private void initTray(){
		final Menu trayMenu = new Menu(shell,SWT.POP_UP);
		final MenuItem showOrHideItem = new MenuItem(trayMenu, SWT.PUSH);
		showOrHideItem.setText(WindowConstant.MAIN_WINDOW_HIDE);
		showOrHideItem.setImage(hide);
		showOrHideItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(shell.isVisible()){
					showOrHideItem.setText(WindowConstant.MAIN_WINDOW_SHOW);
					showOrHideItem.setImage(showView);
					shell.setVisible(false);
				}else{
					showOrHideItem.setText(WindowConstant.MAIN_WINDOW_HIDE);
					showOrHideItem.setImage(hide);
					shell.setVisible(true);
				}
			}
		});
		new MenuItem(trayMenu, SWT.SEPARATOR);
		final MenuItem exitItem = new MenuItem(trayMenu, SWT.PUSH);
		exitItem.setText(WindowConstant.MAIN_WINDOW_EXIT);
		exitItem.setImage(exit);
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWindow();
			}
		});
		TrayItem item = new TrayItem(tray, SWT.NONE);
		item.setImage(image);
		item.setToolTipText(WindowConstant.MAIN_WINDOW_TRAY_TOOLTIP);
		item.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent arg0) {
				trayMenu.setVisible(true);
			}
		});
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
