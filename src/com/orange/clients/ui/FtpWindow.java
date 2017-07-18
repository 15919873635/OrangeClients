package com.orange.clients.ui;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.orange.clients.constant.WindowConstant;
import com.orange.clients.util.ClientUtil;

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
	private Text urlText;
	private Cursor cursor;
	
	private int SHEELH;
	private int SHEELW;
	private int BUTTON_WIDTH_1;
	private int URLTEXT_WIDTH;
	
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
		methodLabel.setBounds(methodLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		methodLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_METHOD);
		methodLabel.setBackground(mainColor);
		
		Label urlLabel = new Label(shell, SWT.NONE);
		urlLabel.setBounds(0, 48, WindowConstant.LABEL_WIDTH, 17);
		urlLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_URL);
		urlLabel.setBackground(mainColor);
		
		int urlTextLeft = urlLabel.getBounds().x + urlLabel.getBounds().width + 10;
		urlText = new Text(shell, SWT.BORDER);
		urlText.setBounds(urlTextLeft, 45, URLTEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		Label fileLabel = new Label(shell, SWT.NONE);
		fileLabel.setBounds(0, 80, WindowConstant.LABEL_WIDTH, 17);
		fileLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_FILES);
		fileLabel.setBackground(mainColor);
		
		int fileChooseButtonLeft = fileLabel.getBounds().x + fileLabel.getBounds().width + 10;
		Button fileChooseButton = new Button(shell, SWT.NONE);
		fileChooseButton.setBounds(fileChooseButtonLeft, 75, WindowConstant.FILE_CHOOSE_BUTTON_WIDTH, WindowConstant.BUTTON_HEIGHT);
		fileChooseButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_CHOOSE_FILE);
		fileChooseButton.setEnabled(false);
		
		FileDialog dialog = new FileDialog (shell, SWT.OPEN|SWT.MULTI);
		dialog.setFilterNames (new String [] {"All Files (*.*)"});
		dialog.setFilterExtensions (new String [] {"*.*"}); //Windows wild cards
		dialog.setFilterPath ("c:\\");
		
		urlText.setFocus();
		fileChooseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				dialog.open();
				String[] fileNames = dialog.getFileNames();
				String path = dialog.getFilterPath();
				String chooseFilePaths = "";
				if(ArrayUtils.isNotEmpty(fileNames)){
					for(String fileName : fileNames){
						chooseFilePaths += path + File.separator + fileName+";";
					}
				}
				if(StringUtils.isNotBlank(chooseFilePaths)){
					chooseFilePaths = chooseFilePaths.substring(0, chooseFilePaths.length() - 1);
				}
			}
		});
	}
	
	private void initWindow(){
		shell.setText(WindowConstant.MAIN_WINDOW_NAME);
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
