package com.example.datomictest01;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppConfig {

	private static String apiBaseUrl = null;
	private static final String API_BASE_URL = 
			"http://192.168.0.79:3000/";
	private static String imageBaseUrl = null;
	private static final String IMAGE_BASE_URL = 
			"http://192.168.0.79:3000/images/";

	/**
	 * Prefrenceから各BASEURLを取得する．取得できないときはAPI_BASE_URL/IMAGE_BASE_URL定義値を使用する．
	 * @param context
	 */
	public static void getPreferenceData(Context context) {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		apiBaseUrl = sharedPref.getString("pref_apibaseurl", API_BASE_URL);
		imageBaseUrl = sharedPref.getString("pref_imgbaseurl", IMAGE_BASE_URL);
	}

	public static String getApiBaseUrl() {
		if (apiBaseUrl == null || apiBaseUrl.isEmpty()) return API_BASE_URL;

		return apiBaseUrl;
	}
	public static String getImageBaseUrl() {
		if (imageBaseUrl == null || imageBaseUrl.isEmpty()) return IMAGE_BASE_URL;

		return imageBaseUrl;
	}

}
