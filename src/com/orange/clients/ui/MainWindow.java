package com.orange.clients.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.orange.clients.constant.WindowConstant;
import com.orange.clients.util.ClientUtil;
import com.orange.clients.util.HttpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;

public class MainWindow {
	protected Shell shell;
	private Display display;
	private Tray tray;
	private Color mainColor;
	private Image image = new Image(display, "icons/http.png");
	private Image showView = new Image(display, "icons/showview.png");
	private Image hide = new Image(display, "icons/hide.png");
	private Image exit = new Image(display, "icons/exit.png");
	private Combo methodCombo;
	private Combo protocolCombo;
	private Text headerText;
	private Text bodyText;
	private Text responseText;
	private Text urlText;
	private Cursor cursor;

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
		Label protocolLabel = new Label(shell, SWT.NONE);
		protocolLabel.setBounds(0, 13, 52, 17);
		protocolLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_PROTOCOL);
		protocolLabel.setBackground(mainColor);
		
		protocolCombo = new Combo(shell, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
		protocolCombo.setBounds(64, 10, 113, 25);
		protocolCombo.add(WindowConstant.REQUEST_HTTP);
		protocolCombo.add(WindowConstant.REQUEST_HTTPS);
		protocolCombo.setText(WindowConstant.REQUEST_HTTP);
		protocolCombo.setCursor(cursor);
		
		Label methodLabel = new Label(shell, SWT.NONE);
		methodLabel.setBounds(214, 13, 49, 17);
		methodLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_METHOD);
		methodLabel.setBackground(mainColor);
		
		methodCombo = new Combo(shell, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
		methodCombo.setBounds(269, 10, 113, 25);
		methodCombo.add(WindowConstant.REQUEST_GET);
		methodCombo.add(WindowConstant.REQUEST_POST);
		methodCombo.add(WindowConstant.REQUEST_PUT);
		methodCombo.setCursor(cursor);
		
		Label urlLabel = new Label(shell, SWT.NONE);
		urlLabel.setBounds(26, 48, 26, 17);
		urlLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_URL);
		urlLabel.setBackground(mainColor);
		
		urlText = new Text(shell, SWT.BORDER);
		urlText.setBounds(64, 45, 510, 23);
		
		Label fileLabel = new Label(shell, SWT.NONE);
		fileLabel.setBounds(23, 80, 30, 17);
		fileLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_FILES);
		fileLabel.setBackground(mainColor);
		
		FileDialog dialog = new FileDialog (shell, SWT.OPEN|SWT.MULTI);
		dialog.setFilterNames (new String [] {"All Files (*.*)"});
		dialog.setFilterExtensions (new String [] {"*.*"}); //Windows wild cards
		dialog.setFilterPath ("c:\\"); //Windows path
		
		Button fileChooseButton = new Button(shell, SWT.NONE);
		fileChooseButton.setBounds(64, 75, 105, 27);
		fileChooseButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_CHOOSE_FILE);
		fileChooseButton.setEnabled(false);
		
		Label filePathLabel = new Label(shell, SWT.NONE);
		filePathLabel.setBounds(175, 80, 399, 17);
		filePathLabel.setBackground(mainColor);
		
		Label headersLabel = new Label(shell, SWT.NONE);
		headersLabel.setBounds(0, 141, 52, 17);
		headersLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_HEADERS);
		headersLabel.setBackground(mainColor);
		
		headerText = new Text(shell, SWT.BORDER|SWT.MULTI|SWT.WRAP);
		headerText.setBounds(64, 110, 226, 84);
		
		Label bodyLabel = new Label(shell, SWT.NONE);
		bodyLabel.setBounds(316, 141, 33, 17);
		bodyLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_BODY);
		bodyLabel.setBackground(mainColor);
		
		bodyText = new Text(shell, SWT.BORDER|SWT.MULTI|SWT.WRAP);
		bodyText.setBounds(355, 110, 219, 84);
		bodyText.setEnabled(false);
		
		Button copyResponseText2Clipboard = new Button(shell, SWT.NONE);
		copyResponseText2Clipboard.setBounds(448, 335, 115, 25);
		copyResponseText2Clipboard.setText(WindowConstant.MAIN_WINDOW_BUTTON_COPY_RESPONSE);
		copyResponseText2Clipboard.setEnabled(false);
		copyResponseText2Clipboard.setCursor(cursor);
		
		Button sendButton = new Button(shell,SWT.NONE);
		sendButton.setBounds(448, 8, 52, 27);
		sendButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_SEND);
		sendButton.setCursor(cursor);
		
		Button resetButton = new Button(shell, SWT.NONE);
		resetButton.setBounds(513, 8, 49, 27);
		resetButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_RESET);
		resetButton.setCursor(cursor);
		
		Label responseLabel = new Label(shell, SWT.NONE);
		responseLabel.setBounds(0, 249, 60, 17);
		responseLabel.setText(WindowConstant.MAIN_WINDOW_LABEL_RESPONSE);
		responseLabel.setBackground(mainColor);
		
		responseText = new Text(shell, SWT.BORDER  | SWT.WRAP | SWT.MULTI | SWT.READ_ONLY);
		responseText.setBounds(63, 205, 511, 122);
		urlText.setFocus();
		
		methodCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String targetText = methodCombo.getText();
				if(targetText.equals(WindowConstant.REQUEST_GET)){
					bodyText.setEnabled(false);
					fileChooseButton.setEnabled(false);
				}else{
					bodyText.setEnabled(true);
					fileChooseButton.setEnabled(true);
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
				protocolCombo.setText(WindowConstant.REQUEST_HTTP);
				methodCombo.setText(WindowConstant.REQUEST_GET);
			}
		});
		sendButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent paramSelectionEvent) {
				Map<String,String> headerMapper = null;
				String headertext = headerText.getText().trim();
				String bodytext = bodyText.getText().trim();
				String protocol = StringUtils.isBlank(protocolCombo.getText()) ? "Http" : protocolCombo.getText();
				String method = StringUtils.isBlank(methodCombo.getText()) ? "GET" : methodCombo.getText();
				String urltext = urlText.getText();
				String response = "";
				String filePaths = filePathLabel.getText();
				if(StringUtils.isNotBlank(urltext) && protocol.equals(WindowConstant.REQUEST_HTTP)){	
					if(StringUtils.isNotBlank(headertext) && headertext.startsWith("{") && headertext.endsWith("}")){
						headerMapper = new HashMap<String,String>();
						JSONObject headerJSON = JSONObject.fromObject(headertext);
						if(!headerJSON.isNullObject() && !headerJSON.isEmpty()){
							JSONArray headers = headerJSON.names();
							for(int index = 0 ;index < headers.size() ; index ++){
								String headerKey = headers.getString(index);
								String headerVal = headerJSON.getString(headerKey);
								if(StringUtils.isNotBlank(headerKey) && StringUtils.isNotBlank(headerVal))
									headerMapper.put(headerKey, headerVal);
							}
						}
					}
					if(!urltext.startsWith(WindowConstant.REQUEST_HTTP) &&  !urltext.startsWith(WindowConstant.REQUEST_HTTP.toLowerCase()))
						urltext = WindowConstant.REQUEST_HTTP + "://" + urltext;
					if(method.equals(WindowConstant.REQUEST_GET))
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
		methodCombo.select(0);
	}
	
	private void initWindow(){
		shell.setSize(WindowConstant.MAIN_WINDOW_WIDTH, WindowConstant.MAIN_WINDOW_HEIGHT);
		shell.setText(WindowConstant.MAIN_WINDOW_NAME);
		shell.setBackground(mainColor);
		shell.setImage(image);
		int screenH = ClientUtil.getScreenHeight();
		int screenW = ClientUtil.getScreenWidth();
		
        int shellH = shell.getBounds().height;
        int shellW = shell.getBounds().width;
        if(shellH > screenH)
            shellH = screenH;
        if(shellW > screenW)
            shellW = screenW;
        
        shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));
	}
	
	private void initTray(){
		final Menu trayMenu = new Menu(shell,SWT.POP_UP);
		final MenuItem showOrHideItem = new MenuItem(trayMenu, SWT.PUSH);
		showOrHideItem.setText("Hide");
		showOrHideItem.setImage(hide);
		showOrHideItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(shell.isVisible()){
					showOrHideItem.setText("Show Home");
					showOrHideItem.setImage(showView);
					shell.setVisible(false);
				}else{
					showOrHideItem.setText("Hide");
					showOrHideItem.setImage(hide);
					shell.setVisible(true);
				}
			}
		});
		new MenuItem(trayMenu, SWT.SEPARATOR);
		final MenuItem exitItem = new MenuItem(trayMenu, SWT.PUSH);
		exitItem.setText("Exit");
		exitItem.setImage(exit);
		exitItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeWindow();
			}
		});
		TrayItem item = new TrayItem(tray, SWT.NONE);
		item.setImage(image);
		item.setToolTipText("This is a TrayItem");
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
