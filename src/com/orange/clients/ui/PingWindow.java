package com.orange.clients.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.orange.clients.constant.WindowConstant;
import com.orange.clients.util.ClientUtil;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Button;


public class PingWindow {
	protected Shell shell;
	private Display display;
	private Color mainColor;
	private Color whiteColor;
	private Color redColor;
	private Image image = new Image(display, "icons/http.png");
	private Cursor cursor;
	
	private Text pingAddressText;
	private Text portText;
	private Table table;
	private Button sendButton;
	private Button stopButton;
	private Button resetButton;
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
		mainColor = new Color(display, WindowConstant.MAIN_BACkGROUND_COLOR);
		whiteColor = new Color(display, WindowConstant.MAIN_WHITE_COLOR);
		redColor = new Color(display, WindowConstant.MAIN_RED_COLOR);
		cursor = new Cursor(display, SWT.CURSOR_HAND);
		initWindow();
		initWinContent();
	}
	
	private void initWinContent(){
		pingAddressText = new Text(shell, SWT.BORDER);
		pingAddressText.setBounds(77, 10, 247, 25);
		
		Combo pingOrTelnetCombo = new Combo(shell, SWT.NONE | SWT.DROP_DOWN | SWT.READ_ONLY);
		pingOrTelnetCombo.setBounds(10, 10, 60, 25);
		pingOrTelnetCombo.add(WindowConstant.PING);
		pingOrTelnetCombo.add(WindowConstant.TELNET);
		pingOrTelnetCombo.setCursor(cursor);
		
		portText = new Text(shell, SWT.BORDER);
		portText.setBounds(330, 10, 54, 25);
		
		pingOrTelnetCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String targetText = pingOrTelnetCombo.getText();
				if(targetText.equals(WindowConstant.PING)){
					portText.setEnabled(false);
				}else{
					portText.setEnabled(true);
				}
			}
		});
		pingOrTelnetCombo.select(0);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 55, 570, 305);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.H_SCROLL);
		table.setBounds(0, 0, 570, 305);
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		table.setBackground(whiteColor);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(545);
		
		sendButton = new Button(shell, SWT.NONE);
		sendButton.setBounds(390, 8, 60, 27);
		sendButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_SEND);
		sendButton.setCursor(cursor);
		
		stopButton = new Button(shell, SWT.NONE);
		stopButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_STOP);
		stopButton.setBounds(456, 8, 60, 27);
		stopButton.setCursor(cursor);
		
		resetButton = new Button(shell, SWT.NONE);
		resetButton.setText(WindowConstant.MAIN_WINDOW_BUTTON_RESET);
		resetButton.setBounds(522, 8, 60, 27);
		resetButton.setCursor(cursor);
		
		for (int i = 0; i < 100; i++){  
			addTableItem(i+"",false);
        } 
		
		sendButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String targetText = pingOrTelnetCombo.getText();
				if(targetText.equals(WindowConstant.PING)){
					portText.setEnabled(false);
				}else{
					portText.setEnabled(true);
				}
			}
		});
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
	
	private void addTableItem(String textContent,boolean succ){
		TableItem item = new TableItem(table, SWT.NONE);  
	 	// 第一列设置，创建TableEditor对象  
        final TableEditor editor = new TableEditor(table);  
        // 创建一个文本框，用于输入文字  
        final Label label = new Label(table, SWT.NONE); 
        label.setText(textContent);
        label.setBackground(whiteColor);
        if(!succ)
        	label.setForeground(redColor);
        // 设置编辑单元格水平填充  
        editor.grabHorizontal = true;  
        // 关键方法，将编辑单元格与文本框绑定到表格的第一列  
        editor.setEditor(label, item, 0); 
	}
	
	private void closeWindow(){
		shell.dispose();
		display.dispose();
	}
}
