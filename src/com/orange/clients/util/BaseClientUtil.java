package com.orange.clients.util;

import java.awt.Toolkit;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class BaseClientUtil {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String getCurrentDate(){
		return dateFormat.format(new Date());
	}
	
	public static int getScreenWidth(){
		return Toolkit.getDefaultToolkit().getScreenSize().width;
	}
	
	public static int getScreenHeight(){
		return Toolkit.getDefaultToolkit().getScreenSize().height;
	}
	
	public static boolean isJSONArray(String targetUri){
		return targetUri.startsWith("[") && targetUri.endsWith("]");
	}
	
	public static boolean isJSONObject(String targetUri){
		return targetUri.startsWith("{") && targetUri.endsWith("}");
	}
	

	public static List<String> dirHierarchy(String directoryStructure){
		List<String> dirList = new ArrayList<String>();
		String[] dirArray = directoryStructure.split("/");
		for(String dir : dirArray){
			if(StringUtils.isNotBlank(dir))
				dirList.add(dir);
		}
		return dirList;
	}
	
	public static String getUriPath(String targetUri){
		String uriPath = "";
		if(targetUri.contains("?"))
			uriPath = targetUri.split("[?]")[0];
		else
			uriPath = targetUri;
		return uriPath;
	}
	
	public static String getgetResourceNameAndSuffix(String uri){
		String nameAndSuffix = null;
		if(StringUtils.isNotBlank(uri)){
			String suffixFile = getUriPath(uri);
			String[] arrangements = suffixFile.split("/");
			nameAndSuffix = arrangements[arrangements.length - 1];
		}
		return nameAndSuffix;
	}
	
	public static String getResourceName(String uri){
		String name = null;
		if(StringUtils.isNotBlank(uri)){
			String lastArrangements = getgetResourceNameAndSuffix(uri);
			if(StringUtils.isNotBlank(lastArrangements) && lastArrangements.contains(".")){
				String[] suffixes = lastArrangements.split("[.]");
				name = lastArrangements.substring(0, lastArrangements.length() - suffixes[suffixes.length - 1].length());
			}
		}
		return name;
	}
	
	public static String getResourceSuffix(String uri){
		String suffix = null;
		if(StringUtils.isNotBlank(uri)){
			String lastArrangements = getgetResourceNameAndSuffix(uri);
			if(StringUtils.isNotBlank(lastArrangements) && lastArrangements.contains(".")){
				String[] suffixes = lastArrangements.split("[.]");
				suffix = suffixes[suffixes.length - 1];
			}
		}
		return suffix.toLowerCase();
	}
	
	public static String getAbsolutePath(){
		File f = new File(".");
		String absolutePath = f.getAbsolutePath();
		if(absolutePath.endsWith("."))
			return absolutePath.substring(0, absolutePath.length() - 1);
		else
			return absolutePath;
	}
}
