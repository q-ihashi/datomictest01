package com.example.datomictest01.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class HttpUtil {
	
	/**
	 * get JSON string by sending json request.
	 * 
	 * @param url API url
	 * @param requestParams request parameter by map
	 * @param charset encoding char of response.
	 * @return response json(null if connection error)
	 */
	private static String getContentBySendJson(String url, Map<String, Object> requestParams, String charset) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 1000);
		HttpConnectionParams.setSoTimeout(params, 6000);
//		HttpPost httpRequest = new HttpPost(url);
		HttpGet httpRequest = new HttpGet(url);
		httpRequest.setHeader("Accept", "application/json");
		httpRequest.setHeader("Content-type", "application/json");
		
		String content = null;
//		try {
			Map<String, Object> requestMap = new HashMap<String, Object>();
			if (requestParams != null) requestMap.put("request", requestParams);
			content = map2Json(requestMap).toString();
//			httpRequest.setEntity(new StringEntity(content, charset));
//		} catch (UnsupportedEncodingException e) {
//			Log.e("HttpUtil", e.getMessage(), e);
//		}
		
		int j = 0;
		HttpResponse httpResponse = null;
		while(httpResponse == null){
			try {
				httpResponse = httpClient.execute(httpRequest);
			} catch (ClientProtocolException e) {
				Log.e("HttpUtil", e.getMessage(), e);
			} catch (Exception e){
				Log.e("HttpUtil", e.getMessage(), e);
			}
			if(j > 5){
				return null;
			}else{
				Log.d("HttpUtil", j+": Getting from "+url+" with "+content);
				j++;
			}
		}
		
		String result = null;
		if (httpResponse != null && httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
			HttpEntity httpEntity = httpResponse.getEntity();
			try {
				result = EntityUtils.toString(httpEntity);
			} catch (ParseException e) {
				Log.d("HttpUtil", e.toString());
			} catch (IOException e) {
				Log.d("HttpUtil", e.toString());
			} finally {
				try {
					httpEntity.consumeContent();
				} catch (IOException e) {
					Log.d("HttpUtil", e.toString());
				}
			}
		}else{
			Log.d("HttpUtil", "cannot make result string");
		}
		httpClient.getConnectionManager().shutdown();
		
		if(result == null || result.equals("[]")){ // TODO
			Log.d("HttpUtil", "content:"+content);
			return null;
		}
		
		return result;
	}
	
	/**
	 * get JSON string by sending json request.
	 * 
	 * @param url API url
	 * @param requestParams request parameter by map
	 * @return response json(null if connection error)
	 */
	public static String getContentBySendJson(String url, Map<String, Object> requestParams) {
		return getContentBySendJson(url, requestParams, "UTF-8");
	}

	/**
	 * change map to json string.
	 * 
	 * @param maps map
	 * @return json
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject map2Json(Map<String, Object> maps) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : maps.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				value = map2Json((Map<String, Object>)entry.getValue());
			} else if (value instanceof List<?>) {
				if (((List<?>)value).get(0) instanceof Map<?, ?>) {
					List<Map<?, ?>> list = (List<Map<?, ?>>)value;
					JSONArray array = new JSONArray();
					for (Map<?, ?> m : list) {
						array.put(new JSONObject(m));
					}
					value = array;
				} else {
					value = new JSONArray((List<String>)value);
				}
			}
			newMap.put(entry.getKey(), value);
		}
		return new JSONObject(newMap);
	}
	
}
