package com.orange.clients.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

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
	private Tree leftTree;
	private Tree rightTree;
	private Text hostText;
	private Text userNameText;
	private Text userPasswordText;
	private Text portText;
	private Table commandTable;
	private Table localDirectoryTable;
	private Table remoteDirectoryTable;
	private Table bottomTable;
	private Button connectButton;
	
	private int BUTTON_WIDTH_1;
	private int TEXT_WIDTH;
	private int LOCAL_DIRECTORY_TABLE_HEADER_WIDTH;
	private int REMOTE_DIRECTORY_TABLE_HEADER_WIDTH;
	private int TABLE_TREE_HEIGHT;
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
		userPasswordLabel.setText("Pass:");
		
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
		connectButton = new Button(shell, SWT.NONE);
		connectButton.setBounds(connectButtonLeft, 10, BUTTON_WIDTH_1, WindowConstant.BUTTON_HEIGHT);
		connectButton.setText("Connect");
		connectButton.setCursor(cursor);
		
		commandTable = new Table(shell, SWT.BORDER);
		commandTable.setHeaderVisible(false);
		commandTable.setLinesVisible(false);
		commandTable.setBounds(0, 45, SHEELW, WindowConstant.FTP_COMPOSITE_HEIGHT);
		
		int leftTreeTop = commandTable.getBounds().y + commandTable.getBounds().height + 5;
		leftTree = new Tree(shell, SWT.BORDER);
		leftTree.setBounds(0, leftTreeTop, SHEELW / 2 - 10, TABLE_TREE_HEIGHT);
		
		rightTree = new Tree(shell, SWT.BORDER);
		rightTree.setBounds(SHEELW / 2, leftTreeTop, SHEELW / 2 - 10, TABLE_TREE_HEIGHT);
		
		// 表格布局  
        GridData gridData = new org.eclipse.swt.layout.GridData();  
        gridData.horizontalAlignment = SWT.FILL;  
        gridData.grabExcessHorizontalSpace = true;  
        gridData.grabExcessVerticalSpace = true;  
        gridData.verticalAlignment = SWT.FILL;  
        
        int directoryTableTop = leftTree.getBounds().y + leftTree.getBounds().height + 5;
		localDirectoryTable = new Table(shell, SWT.BORDER|SWT.MULTI);
		localDirectoryTable.setBounds(0, directoryTableTop, SHEELW / 2 - 10, TABLE_TREE_HEIGHT);
		localDirectoryTable.setHeaderVisible(true);
		localDirectoryTable.setLinesVisible(false);
		localDirectoryTable.setLayoutData(gridData);// 设置表格布局  
		
		String[] localDirectoryTableHeaders = new String[]{"FileName","FileSize","FileType","LastModify"};
		for(int index = 0; index < localDirectoryTableHeaders.length ; index ++){
			String headerName = localDirectoryTableHeaders[index];
			TableColumn fileName = new TableColumn(localDirectoryTable, SWT.NONE);
			fileName.setText(headerName);
			fileName.setMoveable(true);
			fileName.setWidth(LOCAL_DIRECTORY_TABLE_HEADER_WIDTH);
		}
		
		remoteDirectoryTable = new Table(shell, SWT.BORDER);
		remoteDirectoryTable.setBounds(SHEELW / 2, directoryTableTop, SHEELW / 2 - 10, TABLE_TREE_HEIGHT);
		remoteDirectoryTable.setHeaderVisible(true);
		remoteDirectoryTable.setLinesVisible(false);
		
		String[] remoteDirectoryTableHeaders = new String[]{"FileName","FileSize","FileType","LastModify","Auth","Owner"};
		for(int index = 0; index < remoteDirectoryTableHeaders.length ; index ++){
			String headerName = remoteDirectoryTableHeaders[index];
			TableColumn fileName = new TableColumn(remoteDirectoryTable, SWT.NONE);
			fileName.setText(headerName);
			fileName.setMoveable(true);
			fileName.setWidth(REMOTE_DIRECTORY_TABLE_HEADER_WIDTH);
		}
		
		int bottomTableTop = remoteDirectoryTable.getBounds().y + remoteDirectoryTable.getBounds().height + 5;
		bottomTable = new Table(shell, SWT.BORDER);
		bottomTable.setBounds(0, bottomTableTop, SHEELW, WindowConstant.FTP_COMPOSITE_HEIGHT);
		bottomTable.setHeaderVisible(false);
		bottomTable.setLinesVisible(false);
		
		for (int i = 0; i < 3; i++)  
        {
            TreeItem temp = new TreeItem(leftTree, SWT.NONE);  
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
		LOCAL_DIRECTORY_TABLE_HEADER_WIDTH = (SHEELW / 2 - 20) / 4;
		REMOTE_DIRECTORY_TABLE_HEADER_WIDTH = (SHEELW / 2 - 20) / 6;
		
		TABLE_TREE_HEIGHT = (SHEELH - 50 - 2 * WindowConstant.FTP_COMPOSITE_HEIGHT) / 2;
		
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
