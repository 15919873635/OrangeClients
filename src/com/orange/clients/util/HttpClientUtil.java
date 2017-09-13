package com.orange.clients.util;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {
	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);
	private static final Map<String,String> baseHeaderMap = new HashMap<String,String>();
	private static RequestConfig requestConfig;
	static{
		baseHeaderMap.put("accept","*/*");
		baseHeaderMap.put("content-type","application/json");
		baseHeaderMap.put("accept-encoding","UTF-8");
		baseHeaderMap.put("user-agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
		requestConfig = RequestConfig.custom()  
								     .setSocketTimeout(30000)  
								     .setConnectTimeout(30000)
								     .setConnectionRequestTimeout(30000)
								     .build();
	}
	
	public static String sendOptionsRequest(String postUrl,Map<String,String> customHeaderMap){
		String respContent = "";
		HttpOptions httpOptions = new HttpOptions(postUrl);
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		httpClient = HttpClients.createDefault();
		try{
			Map<String,String> newHeaderMapper = new HashMap<String,String>();
			newHeaderMapper.putAll(baseHeaderMap);
			if(customHeaderMap != null && customHeaderMap.size() > 0){
				newHeaderMapper.putAll(customHeaderMap);
			}
			Set<String> keys = newHeaderMapper.keySet();
			for(String key : keys){
				httpOptions.setHeader(key, newHeaderMapper.get(key));
			}
			httpOptions.setConfig(requestConfig); 
			response = httpClient.execute(httpOptions);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, "UTF-8");
		}catch (Exception e) {
			logger.error("send Http Request to:"+postUrl+" ,has errors:"+e.getMessage());
		}
		return respContent;
	}
	
	/**
	 * 发送HTTP GET请求
	 * 所使用的java库为 HttpClient
	 * 
	 */
	public static String sendGetRequest(String postUrl,Map<String,String> customHeaderMap){
		String respContent = "";
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
				httpPost.setHeader(key, URLEncoder.encode(newHeaderMapper.get(key),"UTF-8"));
			}
			HttpEntity requestEntity = null;
			if(bodyContent != null && bodyContent.size() > 0){
				int fileCount = 0;
				for(Object obj : bodyContent.values()){
					if(obj instanceof File)
						fileCount += 1;
				}
				if(fileCount == 0){
					String body = (String)bodyContent.get("body");
					if(body != null){
						if(ClientUtil.isJSONObject(body)){
				            JSONObject bodyJson = JSONObject.parseObject(body);
				            if(!bodyJson.isEmpty()){
				            	Set<String> jsonArray = bodyJson.keySet();
				            	if(!jsonArray.isEmpty()){
				            		List<NameValuePair> params = new ArrayList<NameValuePair>();
				            		for(String indexName : jsonArray){
				            			params.add(new BasicNameValuePair(indexName,bodyJson.getString(indexName))); 
				            		}
				            		requestEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
				            	}
				            }
						}else{
							requestEntity = new StringEntity(body, Consts.UTF_8);
						}
					}
				}else{
					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					builder.setCharset(Consts.UTF_8);
					String body = (String)bodyContent.get("body");
					if(body != null && body.length() > 0){
						if(ClientUtil.isJSONObject(body)){
				            JSONObject bodyJson = JSONObject.parseObject(body);
				            if(!bodyJson.isEmpty()){
				            	Set<String> jsonArray = bodyJson.keySet();
				            	if(!jsonArray.isEmpty()){
				            		for(String indexName : jsonArray){
				            			builder.addTextBody(indexName, bodyJson.getString(indexName),ContentType.APPLICATION_JSON);
				            		}
				            	}
				            }
						}else{
							builder.addTextBody("body", body,ContentType.APPLICATION_JSON);
						}
					}
					for(String key : bodyContent.keySet()){
						Object obj = bodyContent.get(key);
						if(obj instanceof File){
							File file = (File)obj;
							ContentType contentType = ContentType.create("application/octet-stream", Consts.UTF_8);
							builder.addBinaryBody(key, file, contentType, file.getName());// 文件流
						}
					}
					requestEntity = builder.build();
				}
			}
			if(requestEntity != null){
				httpPost.setEntity(requestEntity);
				httpPost.setHeader("content-type",requestEntity.getContentType().getValue());
			}
			httpPost.setConfig(requestConfig);  
			httpClient = HttpClients.createDefault(); 
			response = httpClient.execute(httpPost);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, Consts.UTF_8);
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
				int fileCount = 0;
				for(Object obj : bodyContent.values()){
					if(obj instanceof File)
						fileCount += 1;
				}
				if(fileCount == 0){
					String body = (String)bodyContent.get("body");
					if(body != null){
						if(ClientUtil.isJSONObject(body)){
				            JSONObject bodyJson = JSONObject.parseObject(body);
				            if(!bodyJson.isEmpty()){
				            	Set<String> jsonArray = bodyJson.keySet();
				            	if(!jsonArray.isEmpty()){
				            		List<NameValuePair> params = new ArrayList<NameValuePair>();
				            		for(String indexName : jsonArray){
				            			params.add(new BasicNameValuePair(indexName,bodyJson.getString(indexName))); 
				            		}
				            		requestEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
				            	}
				            }
						}else{
							requestEntity = new StringEntity(body, Consts.UTF_8);
						}
					}
				}else{
					MultipartEntityBuilder builder = MultipartEntityBuilder.create();
					builder.setCharset(Consts.UTF_8);
					String body = (String)bodyContent.get("body");
					if(body != null && body.length() > 0){
						if(ClientUtil.isJSONObject(body)){
				            JSONObject bodyJson = JSONObject.parseObject(body);
				            if(!bodyJson.isEmpty()){
				            	Set<String> jsonArray = bodyJson.keySet();
				            	if(!jsonArray.isEmpty()){
				            		for(String indexName : jsonArray){
				            			ContentType contentType = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);
				            			builder.addTextBody(indexName, bodyJson.getString(indexName),contentType);
				            		}
				            	}
				            }
						}else{
							builder.addTextBody("body", body,ContentType.APPLICATION_JSON);
						}
					}
					for(String key : bodyContent.keySet()){
						Object obj = bodyContent.get(key);
						if(obj instanceof File){
							File file = (File)obj;
							ContentType contentType = ContentType.create("application/octet-stream", Consts.UTF_8);
							builder.addBinaryBody(key, file, contentType, file.getName());// 文件流
						}
					}
					requestEntity = builder.build();
				}
			}
			if(requestEntity != null){
				httpPut.setEntity(requestEntity);
				httpPut.setHeader("content-type",requestEntity.getContentType().getValue());
			}
			httpPut.setConfig(requestConfig);  
			httpClient = HttpClients.createDefault(); 
			response = httpClient.execute(httpPut);  
			entity = response.getEntity();
			respContent = EntityUtils.toString(entity, Consts.UTF_8);
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
