package com.orange.clients.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

public class FtpOperation {
	FTPClient client = new FTPClient();
	private boolean isLogedIn;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Logger logger = Logger.getLogger(FtpOperation.class);
	
	public boolean login(String host,String userName,String userPasswprd,int serverPort){
		try {
			isLogedIn = true;
			client.connect(host, serverPort);
			client.login(userName, userPasswprd);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return isLogedIn;
	}
	
	public String getCurrentDirectory(){
		String currentDirectory = "/";
		try {
			currentDirectory =  client.printWorkingDirectory();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return currentDirectory;
	}
	
	public String[] listFtpDirectorys(String parent){
		String[] directorys = null;
		if(client.isConnected() && isLogedIn){
			try{
				FTPFile[] fileList = client.listDirectories();
				if(fileList.length > 0){
					int index = 0;
					directorys = new String[fileList.length];
					for(FTPFile ftpFile : fileList){
						directorys[index] = ftpFile.getName();
						index += 1;
					}
				}
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return directorys;
	}
	
	private String formatTime(Date targetDate){
		return dateFormat.format(targetDate);
	}
	
	public RemoteFileInfo[] listFtpFiles(String parent){
		RemoteFileInfo[] files = null;
		if(client.isConnected() && isLogedIn){
			try {
				client.changeWorkingDirectory(parent);
				FTPFile[] fileList = client.listFiles();
				if(fileList.length > 0){
					int fileCount = 0;
					for(FTPFile ftpFile : fileList){
						if(ftpFile.isFile()){
							fileCount += 1;
						}
					}
					if(fileCount > 0){
						int index = 0;
						files = new RemoteFileInfo[fileCount];
						for(FTPFile ftpFile : fileList){
							if(ftpFile.isFile()){
								RemoteFileInfo fileInfo = new RemoteFileInfo();
								fileInfo.setAuth("");
								fileInfo.setFileName(ftpFile.getName());
								fileInfo.setFileSize(ftpFile.getSize());
								fileInfo.setFileType("");
								fileInfo.setModifyTime(formatTime(ftpFile.getTimestamp().getTime()));
								fileInfo.setUser_group(ftpFile.getUser()+"/"+ftpFile.getGroup());
								files[index] = fileInfo;
								index += 1;
							}
						}
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return files;
	}
	
	public boolean isLogedIn(){
		return isLogedIn;
	}
}
