package com.orange.clients.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HttpClientUtil {
	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);
	private static final Map<String,String> baseHeaderMap = new HashMap<String,String>();
	static{
		baseHeaderMap.put("accept","*/*");
		baseHeaderMap.put("content-type","application/json");
		baseHeaderMap.put("accept-encoding","UTF-8");
		baseHeaderMap.put("accept-language","zh-CN,en;q=0.9");
		baseHeaderMap.put("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
	}
	
	/**
	 * 发送HTTP GET请求
	 * 所使用的java库为 HttpClient
	 * 
	 */
	public static String sendGetRequest(String postUrl,Map<String,String> customHeaderMap){
		String respContent = "";
		RequestConfig requestConfig = RequestConfig.custom()  
	            .setSocketTimeout(15000)  
	            .setConnectTimeout(15000)  
	            .setConnectionRequestTimeout(15000)  
	            .build(); 
		
		HttpGet httpGet = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try{
			Map<String,String> newHeaderMapper = new HashMap<String,String>();
			newHeaderMapper.putAll(baseHeaderMap);
			httpGet = new HttpGet(postUrl);
			if(customHeaderMap != null && customHeaderMap.size() > 0){
				newHeaderMapper.putAll(customHeaderMap);
			}
			Set<String> keys = newHeaderMapper.keySet();
			for(String key : keys){
				httpGet.setHeader(key, newHeaderMapper.get(key));
			}
			httpGet.setConfig(requestConfig);  
			httpClient = HttpClients.createDefault(); 
			response = httpClient.execute(httpGet);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, "UTF-8");
		}catch (Exception e) {
			logger.error("send Http Request to:"+postUrl+" ,has errors:"+e.getMessage());
		}finally {
			try{
				if(response != null)
					response.close();
				if(httpClient != null)
					httpClient.close();
			}catch (Exception e) {
				logger.error("close Response OR httpClient to:"+postUrl+" ,has errors:"+e.getMessage());
			}
		}
		return respContent;
	}
	
	/**
	 * 发送HTTP POST请求
	 * 所使用的java库为 HttpClient
	 * 
	 */
	public static String sendPostRequest(String postUrl,Map<String,Object> bodyContent,Map<String,String> customHeaderMap){
		String respContent = "";
		RequestConfig requestConfig = RequestConfig.custom()  
	            .setSocketTimeout(15000)  
	            .setConnectTimeout(15000)  
	            .setConnectionRequestTimeout(15000)  
	            .build(); 
		
		HttpPost httpPost = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try{
			Map<String,String> newHeaderMapper = new HashMap<String,String>();
			newHeaderMapper.putAll(baseHeaderMap);
			httpPost = new HttpPost(postUrl);
			if(customHeaderMap != null && customHeaderMap.size() > 0){
				newHeaderMapper.putAll(customHeaderMap);
			}
			Set<String> keys = newHeaderMapper.keySet();
			for(String key : keys){
				httpPost.setHeader(key, newHeaderMapper.get(key));
			}
			HttpEntity requestEntity = null;
			if(bodyContent != null && bodyContent.size() > 0){
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				int fileCount = 0;
				for(Object obj : bodyContent.values()){
					if(obj instanceof File)
						fileCount += 1;
				}
				if(fileCount == 0){
					String body = (String)bodyContent.get("body");
					if(body != null){
						if(body.startsWith("{") && body.endsWith("}")){
				            JSONObject bodyJson = JSONObject.fromObject(body);
				            if(!bodyJson.isNullObject() && !bodyJson.isEmpty()){
				            	JSONArray jsonArray = bodyJson.names();
				            	if(jsonArray.isArray() && !jsonArray.isEmpty()){
				            		for(int index = 0 ; index < jsonArray.size() ; index ++){
				            			String indexName = jsonArray.getString(index);
				            			builder.addTextBody(indexName, bodyJson.getString(indexName));
				            		}
				            	}
				            }
						}else{
							builder.addTextBody("body", body);
						}
					}
				}else{
					String body = (String)bodyContent.get("body");
					if(body != null && body.length() > 0){
						if(body.startsWith("{") && body.endsWith("}")){
				            JSONObject bodyJson = JSONObject.fromObject(body);
				            if(!bodyJson.isNullObject() && !bodyJson.isEmpty()){
				            	JSONArray jsonArray = bodyJson.names();
				            	if(jsonArray.isArray() && !jsonArray.isEmpty()){
				            		for(int index = 0 ; index < jsonArray.size() ; index ++){
				            			String indexName = jsonArray.getString(index);
				            			builder.addTextBody(indexName, bodyJson.getString(indexName));
				            		}
				            	}
				            }
						}else{
							builder.addTextBody("body", body);
						}
					}
					for(String key : bodyContent.keySet()){
						Object obj = bodyContent.get(key);
						if(obj instanceof File){
							File file = (File)obj;
							builder.addBinaryBody(key, file);
						}
					}
				}
				requestEntity = builder.build();
			}
			if(requestEntity != null){
				httpPost.setEntity(requestEntity);
				httpPost.setHeader("content-type",requestEntity.getContentType().getValue());
			}
			httpPost.setConfig(requestConfig);  
			httpClient = HttpClients.createDefault(); 
			response = httpClient.execute(httpPost);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, "UTF-8");
		}catch (Exception e) {
			logger.error("send Http Request to:"+postUrl+" ,has errors:"+e.getMessage());
		}finally {
			try{
				if(response != null)
					response.close();
				if(httpClient != null)
					httpClient.close();
			}catch (Exception e) {
				logger.error("close Response OR httpClient to:"+postUrl+" ,has errors:"+e.getMessage());
			}
		}
		return respContent;
	}
	
	/**
	 * 发送HTTP POST请求
	 * 所使用的java库为 HttpClient
	 * 
	 */
	public static String sendPutRequest(String postUrl,Map<String,Object> bodyContent,Map<String,String> customHeaderMap){
		String respContent = "";
		RequestConfig requestConfig = RequestConfig.custom()  
	            .setSocketTimeout(15000)  
	            .setConnectTimeout(15000)  
	            .setConnectionRequestTimeout(15000)  
	            .build(); 
		
		HttpPut httpPut = null;
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try{
			Map<String,String> newHeaderMapper = new HashMap<String,String>();
			newHeaderMapper.putAll(baseHeaderMap);
			httpPut = new HttpPut(postUrl);
			if(customHeaderMap != null && customHeaderMap.size() > 0){
				newHeaderMapper.putAll(customHeaderMap);
			}
			Set<String> keys = newHeaderMapper.keySet();
			for(String key : keys){
				httpPut.setHeader(key, newHeaderMapper.get(key));
			}
			HttpEntity requestEntity = null;
			if(bodyContent != null && bodyContent.size() > 0){
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				int fileCount = 0;
				for(Object obj : bodyContent.values()){
					if(obj instanceof File)
						fileCount += 1;
				}
				if(fileCount == 0){
					String body = (String)bodyContent.get("body");
					if(body != null){
						if(body.startsWith("{") && body.endsWith("}")){
							httpPut.setHeader("content-type", ContentType.APPLICATION_FORM_URLENCODED.toString());
				            JSONObject bodyJson = JSONObject.fromObject(body);
				            if(!bodyJson.isNullObject() && !bodyJson.isEmpty()){
				            	JSONArray jsonArray = bodyJson.names();
				            	if(jsonArray.isArray() && !jsonArray.isEmpty()){
				            		for(int index = 0 ; index < jsonArray.size() ; index ++){
				            			String indexName = jsonArray.getString(index);
				            			builder.addTextBody(indexName, bodyJson.getString(indexName));
				            		}
				            	}
				            }
						}else{
							builder.addTextBody("body", body);
						}
					}
				}else{
					String body = (String)bodyContent.get("body");
					if(body != null){
						if(body.startsWith("{") && body.endsWith("}")){
							newHeaderMapper.put("content-type", ContentType.APPLICATION_FORM_URLENCODED.toString());
				            JSONObject bodyJson = JSONObject.fromObject(body);
				            if(!bodyJson.isNullObject() && !bodyJson.isEmpty()){
				            	JSONArray jsonArray = bodyJson.names();
				            	if(jsonArray.isArray() && !jsonArray.isEmpty()){
				            		for(int index = 0 ; index < jsonArray.size() ; index ++){
				            			String indexName = jsonArray.getString(index);
				            			builder.addTextBody(indexName, bodyJson.getString(indexName));
				            		}
				            	}
				            }
						}else{
							builder.addTextBody("body", body);
						}
					}
					for(String key : bodyContent.keySet()){
						Object obj = bodyContent.get(key);
						if(obj instanceof File){
							File file = (File)obj;
							builder.addBinaryBody(key, file, ContentType.MULTIPART_FORM_DATA, file.getName());// 文件流
						}
					}
				}
				requestEntity = builder.build();
			}
			if(requestEntity != null){
				httpPut.setEntity(requestEntity);
				httpPut.setHeader("content-type",requestEntity.getContentType().getValue());
			}
			httpPut.setConfig(requestConfig);  
			httpClient = HttpClients.createDefault(); 
			response = httpClient.execute(httpPut);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, "UTF-8");
		}catch (Exception e) {
			logger.error("send Http Request to:"+postUrl+" ,has errors:"+e.getMessage());
		}finally {
			try{
				if(response != null)
					response.close();
				if(httpClient != null)
					httpClient.close();
			}catch (Exception e) {
				logger.error("close Response OR httpClient to:"+postUrl+" ,has errors:"+e.getMessage());
			}
		}
		return respContent;
	}
}
