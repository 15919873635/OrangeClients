package com.orange.clients.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.alibaba.fastjson.JSONObject;
import com.orange.clients.constant.WindowConstant;
import com.orange.clients.service.FtpOperation;
import com.orange.clients.service.RemoteFileInfo;
import com.orange.clients.util.BaseClientUtil;
import com.orange.clients.util.HttpClientUtil;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class ClientWindow {
	/**
	 * 全局相关
	 */
	protected Shell shell;
	private Display display;
	private Tray tray;
	private Color mainColor;
	private Image image = new Image(display, WindowConstant.ICON_HTTP);
	private Image showView = new Image(display, WindowConstant.ICON_SHOWVIEW);
	private Image hide = new Image(display, WindowConstant.ICON_HIDE);
	private Image exit = new Image(display, WindowConstant.ICON_EXIT);
	private int SHEELH;
	private int SHEELW;
	private Cursor cursor;
	/**
	 * 菜单栏相关
	 */
	private Menu menuBar;
	private MenuItem menuHttp;
	private MenuItem menuFtp;
	private MenuItem menuSvn;
	
	/**
	 * TabFolder相关
	 */
	private TabFolder tabFolder;
	private TabItem tabHttpItem;
	private TabItem tabFtpItem;
	private TabItem tabSvnItem;
	private Composite httpComposite;
	private Composite ftpComposite;
	private Composite svnComposite;
	
	/**
	 * HTTP Tab相关
	 */
	private int BUTTON_WIDTH_1;
	private int COMBOBOX_WIDTH;
	private int URLTEXT_WIDTH;
	private int FILE_PATH_WIDTH;
	private int HEADER_OR_BODY_TEXT_WIDTH;
	private int HEADER_OR_BODY_TEXT_HEIGHT;
	private int RESPONSE_TEXT_WIDTH;
	private int RESPONSE_TEXT_HEIGHT;
	private Combo methodCombo;
	private Combo protocolCombo;
	private Text headerText;
	private Text bodyText;
	private Text responseText;
	private Text urlText;
	
	/**
	 * FTP Tab相关
	 */
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
	private int TEXT_WIDTH;
	private int LOCAL_DIRECTORY_TABLE_HEADER_WIDTH;
	private int REMOTE_DIRECTORY_TABLE_HEADER_WIDTH;
	private int TABLE_TREE_HEIGHT;
	private String currentDirectory;
	private FtpOperation ftpOperation;
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
	
	private void closeWindow(){
		shell.dispose();
		tray.dispose();
		image.dispose();
		showView.dispose();
		hide.dispose();
		exit.dispose();
		display.dispose();
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
	}
	
	/**
	 * 初始化基础配置
	 */
	private void initShell(){
		shell.setText(WindowConstant.MAIN_CLIENT_WINDOW);
		shell.setBackground(mainColor);
		shell.setImage(image);
		int screenH = (int)BaseClientUtil.getScreenHeight();
		int screenW = (int)BaseClientUtil.getScreenWidth();
		
		SHEELH = screenH * 4 / 5;
		SHEELW = screenW * 4 / 5;
		SHEELH = (SHEELH >= WindowConstant.MIN_WINDOW_HEIGHT) ? SHEELH : WindowConstant.MIN_WINDOW_HEIGHT;
		SHEELW = (SHEELW >= WindowConstant.MIN_WINDOW_WIDTH) ? SHEELW : WindowConstant.MIN_WINDOW_WIDTH;
		
        shell.setSize(SHEELW, SHEELH);
        shell.setLocation((screenW - SHEELW) / 2, (screenH - SHEELH) / 2);
	}
	
	protected void initMenuBar(){
		menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		
		menuHttp = new MenuItem(menuBar, SWT.NONE);
		menuHttp.setText(WindowConstant.MAIN_CLIENT_MENU_HTTP);
		
		menuFtp = new MenuItem(menuBar, SWT.NONE);
		menuFtp.setText(WindowConstant.MAIN_CLIENT_MENU_FTP);
		
		menuSvn = new MenuItem(menuBar, SWT.NONE);
		menuSvn.setText(WindowConstant.MAIN_CLIENT_MENU_SVN);
	}
	
	protected void initTabFolder(){
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 5, SHEELW, SHEELH - 50);
		
		tabHttpItem = new TabItem(tabFolder, SWT.NONE);
		tabHttpItem.setText(WindowConstant.MAIN_CLIENT_MENU_HTTP);
		
		httpComposite = new Composite(tabFolder, SWT.NONE);
		httpComposite.setBackground(mainColor);
		tabHttpItem.setControl(httpComposite);
		
		tabFtpItem = new TabItem(tabFolder, SWT.NONE);
		tabFtpItem.setText(WindowConstant.MAIN_CLIENT_MENU_FTP);
		
		ftpComposite = new Composite(tabFolder, SWT.NONE);
		ftpComposite.setBackground(mainColor);
		tabFtpItem.setControl(ftpComposite);
		
		tabSvnItem = new TabItem(tabFolder, SWT.NONE);
		tabSvnItem.setText(WindowConstant.MAIN_CLIENT_MENU_SVN);
		
		svnComposite = new Composite(tabFolder, SWT.NONE);
		svnComposite.setBackground(mainColor);
		tabSvnItem.setControl(svnComposite);
	}
	
	protected void initHttpTab(){
		BUTTON_WIDTH_1 = ((SHEELW * 8 / 100) > WindowConstant.MIN_BUTTON_WIDTH_1) ? (SHEELW * 8 / 100) : WindowConstant.MIN_BUTTON_WIDTH_1;
		COMBOBOX_WIDTH = (SHEELW - WindowConstant.LABEL_WIDTH *2 - BUTTON_WIDTH_1 * 2 - 70) / 2;
		URLTEXT_WIDTH = SHEELW - WindowConstant.LABEL_WIDTH - BUTTON_WIDTH_1 * 2 - 50 ;
		FILE_PATH_WIDTH = URLTEXT_WIDTH - WindowConstant.FILE_CHOOSE_BUTTON_WIDTH -10;
		HEADER_OR_BODY_TEXT_WIDTH = (SHEELW - WindowConstant.LABEL_WIDTH * 2 - 40) / 2;
		HEADER_OR_BODY_TEXT_HEIGHT = (SHEELH - 250) * 4 / 10;
		RESPONSE_TEXT_WIDTH = SHEELW - WindowConstant.LABEL_WIDTH - 30;
		RESPONSE_TEXT_HEIGHT = (SHEELH - 250) * 6 / 10;
		
		Label protocolLabel = new Label(httpComposite, SWT.NONE);
		protocolLabel.setBounds(0, 13, WindowConstant.LABEL_WIDTH, 17);
		protocolLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_PROTOCOL);
		protocolLabel.setBackground(mainColor);

		int protocolComboLeft = protocolLabel.getBounds().x + protocolLabel.getBounds().width + 10;
		protocolCombo = new Combo(httpComposite, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
		protocolCombo.setBounds(protocolComboLeft, 10, COMBOBOX_WIDTH, WindowConstant.BUTTON_HEIGHT);
		protocolCombo.add(WindowConstant.REQUEST_HTTP);
		protocolCombo.add(WindowConstant.REQUEST_HTTPS);
		protocolCombo.setText(WindowConstant.REQUEST_HTTP);
		protocolCombo.setCursor(cursor);
		
		int methodLabelLeft = protocolCombo.getBounds().x + protocolCombo.getBounds().width + 10;
		Label methodLabel = new Label(httpComposite, SWT.NONE);
		methodLabel.setBounds(methodLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		methodLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_METHOD);
		methodLabel.setBackground(mainColor);
		
		int methodComboLeft = methodLabel.getBounds().x + methodLabel.getBounds().width + 10;
		methodCombo = new Combo(httpComposite, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
		methodCombo.setBounds(methodComboLeft, 10, COMBOBOX_WIDTH, WindowConstant.BUTTON_HEIGHT);
		methodCombo.add(WindowConstant.REQUEST_OPTIONS);
		methodCombo.add(WindowConstant.REQUEST_GET);
		methodCombo.add(WindowConstant.REQUEST_POST);
		methodCombo.add(WindowConstant.REQUEST_PUT);
		methodCombo.setCursor(cursor);
		
		int sendButtonLeft = methodCombo.getBounds().x + methodCombo.getBounds().width + 10;
		Button sendButton = new Button(httpComposite,SWT.NONE);
		sendButton.setBounds(sendButtonLeft, 10, BUTTON_WIDTH_1, WindowConstant.BUTTON_HEIGHT);
		sendButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_SEND);
		sendButton.setCursor(cursor);
		
		int resetButtonLeft = sendButton.getBounds().x + sendButton.getBounds().width + 10;
		Button resetButton = new Button(httpComposite, SWT.NONE);
		resetButton.setBounds(resetButtonLeft, 10, BUTTON_WIDTH_1, WindowConstant.BUTTON_HEIGHT);
		resetButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_RESET);
		resetButton.setCursor(cursor);
		
		Label urlLabel = new Label(httpComposite, SWT.NONE);
		urlLabel.setBounds(0, 48, WindowConstant.LABEL_WIDTH, 17);
		urlLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_URL);
		urlLabel.setBackground(mainColor);
		
		int urlTextLeft = urlLabel.getBounds().x + urlLabel.getBounds().width + 10;
		urlText = new Text(httpComposite, SWT.BORDER);
		urlText.setBounds(urlTextLeft, 45, URLTEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		Label fileLabel = new Label(httpComposite, SWT.NONE);
		fileLabel.setBounds(0, 80, WindowConstant.LABEL_WIDTH, 17);
		fileLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_FILES);
		fileLabel.setBackground(mainColor);
		
		int fileChooseButtonLeft = fileLabel.getBounds().x + fileLabel.getBounds().width + 10;
		Button fileChooseButton = new Button(httpComposite, SWT.NONE);
		fileChooseButton.setBounds(fileChooseButtonLeft, 75, WindowConstant.FILE_CHOOSE_BUTTON_WIDTH, WindowConstant.BUTTON_HEIGHT);
		fileChooseButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_CHOOSE_FILE);
		fileChooseButton.setEnabled(false);
		
		int filePathLabelLeft = fileChooseButton.getBounds().x + fileChooseButton.getBounds().width + 10;
		Label filePathLabel = new Label(httpComposite, SWT.NONE);
		filePathLabel.setBounds(filePathLabelLeft, 80, FILE_PATH_WIDTH, 17);
		filePathLabel.setBackground(mainColor);
		
		int textTop = HEADER_OR_BODY_TEXT_HEIGHT / 2 + filePathLabel.getBounds().y + 20;
		Label headersLabel = new Label(httpComposite, SWT.NONE);
		headersLabel.setBounds(0, textTop, WindowConstant.LABEL_WIDTH, 17);
		headersLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_HEADERS);
		headersLabel.setBackground(mainColor);
		
		int headerTextLeft = headersLabel.getBounds().x + headersLabel.getBounds().width + 10;
		headerText = new Text(httpComposite, SWT.BORDER|SWT.MULTI|SWT.WRAP);
		headerText.setBounds(headerTextLeft, 110, HEADER_OR_BODY_TEXT_WIDTH, HEADER_OR_BODY_TEXT_HEIGHT);
		
		int bodyLabelLeft = headerText.getBounds().x + headerText.getBounds().width + 10;
		Label bodyLabel = new Label(httpComposite, SWT.NONE);
		bodyLabel.setBounds(bodyLabelLeft, textTop, WindowConstant.LABEL_WIDTH, 17);
		bodyLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_BODY);
		bodyLabel.setBackground(mainColor);
		
		int bodyTextLeft = bodyLabel.getBounds().x + bodyLabel.getBounds().width;
		bodyText = new Text(httpComposite, SWT.BORDER|SWT.MULTI|SWT.WRAP);
		bodyText.setBounds(bodyTextLeft, 110, HEADER_OR_BODY_TEXT_WIDTH, HEADER_OR_BODY_TEXT_HEIGHT);
		bodyText.setEnabled(false);
		
		int responseLabelTop = bodyText.getBounds().y + bodyText.getBounds().height + RESPONSE_TEXT_HEIGHT / 2 + 10;
		Label responseLabel = new Label(httpComposite, SWT.NONE);
		responseLabel.setBounds(0, responseLabelTop, WindowConstant.LABEL_WIDTH_2, 17);
		responseLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_RESPONSE);
		responseLabel.setBackground(mainColor);
		
		FileDialog dialog = new FileDialog (shell, SWT.OPEN|SWT.MULTI);
		dialog.setFilterNames (new String [] {"All Files (*.*)"});
		dialog.setFilterExtensions (new String [] {"*.*"}); //Windows wild cards
		dialog.setFilterPath ("c:\\"); //Windows path
		
		int responseTextLeft = responseLabel.getBounds().x + responseLabel.getBounds().width + 5;
		int responseTextTop = bodyText.getBounds().y + bodyText.getBounds().height + 10;
		responseText = new Text(httpComposite, SWT.BORDER  | SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		responseText.setBounds(responseTextLeft, responseTextTop, RESPONSE_TEXT_WIDTH, RESPONSE_TEXT_HEIGHT);
		
		int copyResponseTextLeft = (SHEELW - WindowConstant.COPY_RESPONSE_BUTTON_WIDTH) / 2;
		int copyResponseTextTop = SHEELH - WindowConstant.BUTTON_HEIGHT - 90;
		Button copyResponseText2Clipboard = new Button(httpComposite, SWT.NONE);
		copyResponseText2Clipboard.setBounds(copyResponseTextLeft, copyResponseTextTop, WindowConstant.COPY_RESPONSE_BUTTON_WIDTH, WindowConstant.BUTTON_HEIGHT);
		copyResponseText2Clipboard.setText(WindowConstant.MAIN_WINDOW_BUTTON_COPY_RESPONSE);
		copyResponseText2Clipboard.setEnabled(false);
		copyResponseText2Clipboard.setCursor(cursor);
		
		urlText.setFocus();
		
		methodCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String targetText = methodCombo.getText();
				if(targetText.equals(WindowConstant.REQUEST_POST)
						|| targetText.equals(WindowConstant.REQUEST_PUT)){
					bodyText.setEnabled(true);
					fileChooseButton.setEnabled(true);
				}else{
					bodyText.setEnabled(false);
					fileChooseButton.setEnabled(false);
				}
			}
		});
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				headerText.setText("");
				bodyText.setText("");
				urlText.setText("");
				responseText.setText("");
				filePathLabel.setText("");
				dialog.setFileName("");
				copyResponseText2Clipboard.setEnabled(false);
				protocolCombo.select(0);
				methodCombo.select(1);
			}
		});
		sendButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent paramSelectionEvent) {
				Map<String,String> headerMapper = null;
				String headertext = headerText.getText().trim();
				String bodytext = bodyText.getText().trim();
				String protocol = StringUtils.isBlank(protocolCombo.getText()) ? WindowConstant.REQUEST_HTTP : protocolCombo.getText();
				String method = StringUtils.isBlank(methodCombo.getText()) ? WindowConstant.REQUEST_GET : methodCombo.getText();
				String urltext = urlText.getText();
				String response = "";
				String filePaths = filePathLabel.getText();
				if(StringUtils.isNotBlank(urltext) && protocol.equals(WindowConstant.REQUEST_HTTP)){	
					if(StringUtils.isNotBlank(headertext) && BaseClientUtil.isJSONArray(headertext)){
						headerMapper = new HashMap<String,String>();
						JSONObject headerJSON = JSONObject.parseObject(headertext);
						if(!headerJSON.isEmpty()){
							Set<String> headers = headerJSON.keySet();
							for(String headerKey : headers){
								String headerVal = headerJSON.getString(headerKey);
								if(StringUtils.isNotBlank(headerKey) && StringUtils.isNotBlank(headerVal))
									headerMapper.put(headerKey, headerVal);
							}
						}
					}
					if(!urltext.startsWith(WindowConstant.REQUEST_HTTP) &&  !urltext.startsWith(WindowConstant.REQUEST_HTTP.toLowerCase()))
						urltext = WindowConstant.REQUEST_HTTP + "://" + urltext;
					if(method.equals(WindowConstant.REQUEST_OPTIONS))
						response = HttpClientUtil.sendOptionsRequest(urltext, headerMapper);
					else if(method.equals(WindowConstant.REQUEST_GET))
						response = HttpClientUtil.sendGetRequest(urltext, headerMapper);
					else if(method.equals(WindowConstant.REQUEST_POST)){
						Map<String,Object> bodyMap = new HashMap<String,Object>();
						if(StringUtils.isNotBlank(bodytext)){
							bodyMap.put("body", bodytext);
						}
						if(StringUtils.isNotBlank(filePaths)){
							String[] chooseFilePaths = filePaths.split(";");
							if(ArrayUtils.isNotEmpty(chooseFilePaths)){
								int fileCount = 0;
								for(String chooseFile : chooseFilePaths){
									File file = new File(chooseFile);
									if(file.exists()){
										bodyMap.put("filename_"+fileCount, file);
										fileCount += 1;
									}
								}
							}
						}
						response = HttpClientUtil.sendPostRequest(urltext, bodyMap, headerMapper);
					}else if(method.equals(WindowConstant.REQUEST_PUT)){
						Map<String,Object> bodyMap = new HashMap<String,Object>();
						if(StringUtils.isNotBlank(bodytext)){
							bodyMap.put("body", bodytext);
						}
						if(StringUtils.isNotBlank(filePaths)){
							String[] chooseFilePaths = filePaths.split(";");
							if(ArrayUtils.isNotEmpty(chooseFilePaths)){
								int fileCount = 0;
								for(String chooseFile : chooseFilePaths){
									File file = new File(chooseFile);
									if(file.exists()){
										bodyMap.put("filename_"+fileCount, file);
										fileCount += 1;
									}
								}
							}
						}
						response = HttpClientUtil.sendPutRequest(urltext, bodyMap, headerMapper);
					}
					responseText.setText(response.trim());
					if(!response.trim().equals("") && response.trim().length() > 0)
						copyResponseText2Clipboard.setEnabled(true);
				}
			}
		});
		copyResponseText2Clipboard.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String responseTextContent = responseText.getText();
				Clipboard clipboard = new Clipboard(display);
				TextTransfer textTransfer = TextTransfer.getInstance(); 
                clipboard.setContents(new String[]{responseTextContent}, new Transfer[]{textTransfer}); 
			}
		});
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
					filePathLabel.setText(chooseFilePaths);
					filePathLabel.setToolTipText(chooseFilePaths);
				}
			}
		});
		methodCombo.select(1);
	}
	
	protected void initFtpTab() {
		TEXT_WIDTH = ((SHEELW * 80 / 100) - 4 * WindowConstant.LABEL_WIDTH) / 4;
		LOCAL_DIRECTORY_TABLE_HEADER_WIDTH = (SHEELW / 2 - 20) / 4;
		REMOTE_DIRECTORY_TABLE_HEADER_WIDTH = (SHEELW / 2 - 20) / 6;
		TABLE_TREE_HEIGHT = (SHEELH - 50 - 2 * WindowConstant.FTP_COMPOSITE_HEIGHT) / 2;
		
		Label hostLabel = new Label(ftpComposite, SWT.NONE);
		hostLabel.setBounds(5, 13, WindowConstant.LABEL_WIDTH, 17);
		hostLabel.setText("Host:");
		hostLabel.setBackground(mainColor);
		
		int hostTextLeft = hostLabel.getBounds().x + hostLabel.getBounds().width + 10;
		hostText = new Text(ftpComposite, SWT.BORDER);
		hostText.setBounds(hostTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int userNameLabelLeft = hostText.getBounds().x + hostText.getBounds().width + 10;
		Label userNameLabel = new Label(ftpComposite, SWT.NONE);
		userNameLabel.setBounds(userNameLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		userNameLabel.setText("User:");
		
		int userNameTextLeft = userNameLabel.getBounds().x + userNameLabel.getBounds().width + 10;
		userNameText = new Text(ftpComposite, SWT.BORDER);
		userNameText.setBounds(userNameTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int userPasswordLabelLeft = userNameText.getBounds().x + userNameText.getBounds().width + 10;
		Label userPasswordLabel = new Label(ftpComposite, SWT.NONE);
		userPasswordLabel.setBounds(userPasswordLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		userPasswordLabel.setText("Pass:");
		
		int userPasswordTextLeft = userPasswordLabel.getBounds().x + userPasswordLabel.getBounds().width + 10;
		userPasswordText = new Text(ftpComposite, SWT.BORDER);
		userPasswordText.setBounds(userPasswordTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int portLabelLeft = userPasswordText.getBounds().x + userPasswordText.getBounds().width + 10;
		Label portLabel = new Label(ftpComposite, SWT.NONE);
		portLabel.setBounds(portLabelLeft, 13, WindowConstant.LABEL_WIDTH, 17);
		portLabel.setText("Port:");
		
		int portTextLeft = portLabel.getBounds().x + portLabel.getBounds().width + 10;
		portText = new Text(ftpComposite, SWT.BORDER);
		portText.setBounds(portTextLeft, 10, TEXT_WIDTH, WindowConstant.BUTTON_HEIGHT);
		
		int connectButtonLeft = portText.getBounds().x + portText.getBounds().width + 10;
		connectButton = new Button(ftpComposite, SWT.NONE);
		connectButton.setBounds(connectButtonLeft, 10, BUTTON_WIDTH_1, WindowConstant.BUTTON_HEIGHT);
		connectButton.setText("Connect");
		connectButton.setCursor(cursor);
		
		commandTable = new Table(ftpComposite, SWT.BORDER);
		commandTable.setHeaderVisible(false);
		commandTable.setLinesVisible(false);
		commandTable.setBounds(0, 45, SHEELW, WindowConstant.FTP_COMPOSITE_HEIGHT);
		
		int leftTreeTop = commandTable.getBounds().y + commandTable.getBounds().height + 5;
		leftTree = new Tree(ftpComposite, SWT.BORDER);
		leftTree.setBounds(0, leftTreeTop, SHEELW / 2 - 10, TABLE_TREE_HEIGHT);
		
		rightTree = new Tree(ftpComposite, SWT.BORDER);
		rightTree.setBounds(SHEELW / 2, leftTreeTop, SHEELW / 2 - 10, TABLE_TREE_HEIGHT);
		rightTree.setCursor(cursor);
		
		// 表格布局  
        GridData gridData = new org.eclipse.swt.layout.GridData();  
        gridData.horizontalAlignment = SWT.FILL;  
        gridData.grabExcessHorizontalSpace = true;  
        gridData.grabExcessVerticalSpace = true;  
        gridData.verticalAlignment = SWT.FILL;  
        
        int directoryTableTop = leftTree.getBounds().y + leftTree.getBounds().height + 5;
		localDirectoryTable = new Table(ftpComposite, SWT.BORDER|SWT.MULTI);
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
		
		remoteDirectoryTable = new Table(ftpComposite, SWT.BORDER);
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
		
		DragSource ds = new DragSource(localDirectoryTable, DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		ds.addDragListener(new DragSourceAdapter() {
			public void dragSetData(DragSourceEvent event) {
				System.out.println(rightTree.getSelection()[0].getText());
			}
		});
		
		DropTarget dt = new DropTarget(remoteDirectoryTable, DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		dt.addDropListener(new DropTargetAdapter() {
			public void drop(DropTargetEvent event) {
				System.out.println((String)event.data);
			}
		});
		   
		int bottomTableTop = remoteDirectoryTable.getBounds().y + remoteDirectoryTable.getBounds().height + 5;
		bottomTable = new Table(ftpComposite, SWT.BORDER);
		bottomTable.setBounds(0, bottomTableTop, SHEELW, WindowConstant.FTP_COMPOSITE_HEIGHT);
		bottomTable.setHeaderVisible(false);
		bottomTable.setLinesVisible(false);
		
		connectButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent paramSelectionEvent) {
				String ftpServerHost = hostText.getText();
				String userName = userNameText.getText();
				String userPass = userPasswordText.getText();
				String serverPort = portText.getText();
				
				boolean userLogin = ftpOperation.login(ftpServerHost, userName, userPass, Integer.parseInt(serverPort));
				if(userLogin){
					currentDirectory = ftpOperation.getCurrentDirectory();
					TreeItem rootDirectory = new TreeItem(rightTree, SWT.NONE);
					rootDirectory.setText(currentDirectory);
					rootDirectory.setExpanded(true);  
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {}
		});
		
		rightTree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if(event.item instanceof TreeItem){
	                TreeItem item = (TreeItem)event.item;  
	                if(item!=null){
	                	if(remoteDirectoryTable.getItemCount() > 0){
	                		remoteDirectoryTable.removeAll();
	                	}
	                	RemoteFileInfo[] ftpFileList = ftpOperation.listFtpFiles(item.getText());
	                	if(ftpFileList != null && ftpFileList.length > 0){
	                		for(RemoteFileInfo fileInfo : ftpFileList){
	                			TableItem tableItem = new TableItem(remoteDirectoryTable, SWT.NONE);
	                			tableItem.setText(new String[]{
                					fileInfo.getFileName(), 
                					Long.toString(fileInfo.getFileSize()), 
                					fileInfo.getFileType(), 
                					fileInfo.getModifyTime(),
                					fileInfo.getAuth(),
                					fileInfo.getUser_group()
            					});  
	                		}
	                	}
	                }
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {}
		});
		rightTree.addListener(SWT.MouseDoubleClick, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(event.button != 1) { 
		            return;  
		        } 
				Point point = new Point(event.x,event.y);  
                TreeItem item = rightTree.getItem(point);  
                if(item!=null){
                	if(item.getItemCount() > 0){
                		item.removeAll();
                	}
            		String[] directorys = ftpOperation.listFtpDirectorys(item.getText());
            		if(directorys != null){
            			for(String dir : directorys){
            				TreeItem rootDirectory = new TreeItem(item, SWT.NONE);
        					rootDirectory.setText(dir);
        					rootDirectory.setExpanded(true);
            			}
            		}
            		item.setExpanded(true);
                }
			}
		});
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void initWindow() {
		//初始化基础配置
		initShell();
		//初始化窗体的菜单
		initMenuBar();
		//初始化tab folder
		initTabFolder();
		//初始化 HTTP Tab
		initHttpTab();
		//初始化FTP Tab
		initFtpTab();
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
}
