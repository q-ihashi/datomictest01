package com.example.datomictest01.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.example.datomictest01.AppConfig;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.dto.UserDto;
import com.example.datomictest01.util.HttpUtil;
import com.example.datomictest01.util.TaskCallback;

public class MeishiWhoGetTask extends AsyncTask<String, Integer,  List<UserDto>> {

	TaskCallback<UserDto> callback;
	boolean flagEnd = false;

	public MeishiWhoGetTask(TaskCallback<UserDto> callback) {
//	public MeishiGetTask() {
		super();
		this.callback = callback;
	}

	@Override
	protected void onPostExecute(List<UserDto> result) {
		if (flagEnd) {
//			setViewMeishiList(result);
			callback.onSuccess(result);
		} else {
//			callback.onFailure("error/onPostExecute,flagEnd=false");
		}
	}

	/**
	 * 名刺データ取得
	 * param params[0]:取得元指定/ "myMeishi" ユーザの名刺, "hasMeishi" 他からもらった名刺
	 * param params[1]:ユーザ番号/ 1～
	 */
	@Override
	protected List<UserDto> doInBackground(String... params) {
//		Map param = new HashMap<String, String>();
//		param.put("pp1", params[0]);
		List<UserDto> users = new ArrayList<UserDto>();
		String jsondata = null;
		JSONObject jo = null;
		JSONArray ja = null;
		String ret = "";
		String datacol = params[0];
		String pp1 = params[1];
//		try {
//			jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "user-get-p?pp1="+pp1,null);
//			jo = new JSONObject(jsondata);
////このあたりはAPI変更して簡易になるようにしたいが．．．いったんこのまま
//			ja = jo.getJSONArray(datacol);
//			int count = ja.length();
//			for (int i=0 ; i < count; i++) {
//				ret = ret + "," + ja.getInt(i);
//				jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "meishi-get-p?pp1="+ja.getInt(i),null);
//				jo = new JSONObject(jsondata);
//				UserDto ud = new UserDto();
//				ud.id = ja.getInt(i);
//				ud.title = jo.getJSONArray("title").getString(0);
//				ud.company = jo.getJSONArray("company").getString(0);
//				ud.name = jo.getJSONArray("name").getString(0);
//				ud.addr = jo.getJSONArray("addr").getString(0);
//				ud.tel = jo.getJSONArray("tel").getString(0);
//				ud.email = jo.getJSONArray("email").getString(0);
//				users.add(ud);
//			}
//		} catch (Exception e) {
//			Log.d("doInBackground", e.toString());
//			return meishis; //e.toString();
//		}

		flagEnd = true;
		return users;
	}

}
