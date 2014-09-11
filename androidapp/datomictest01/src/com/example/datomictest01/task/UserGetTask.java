package com.example.datomictest01.task;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.example.datomictest01.AppConfig;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.dto.UserDto;
import com.example.datomictest01.util.HttpUtil;
import com.example.datomictest01.util.TaskCallback;

public class UserGetTask extends AsyncTask<String, Integer,  UserDto> {

	TaskCallback<UserDto> callback;
	boolean flagEnd = false;

	public UserGetTask(TaskCallback<UserDto> callback) {
//	public MeishiGetTask() {
		super();
		this.callback = callback;
	}

	@Override
	protected void onPostExecute(UserDto result) {
		if (flagEnd) {
//			setViewMeishiList(result);
			callback.onSuccess(result);
		} else {
//			callback.onFailure("error/onPostExecute,flagEnd=false");
		}
	}

	/**
	 * 名刺データ取得
	 * param params[0]:ユーザ番号/ 1～
	 */
	@Override
	protected UserDto doInBackground(String... params) {
//		Map param = new HashMap<String, String>();
//		param.put("pp1", params[0]);
		UserDto userDto = new UserDto();
		String jsondata = null;
		JSONObject jo = null;
		JSONArray ja = null;
		String ret = "";
		String pp1 = params[0];
		try {
			jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "user-get-p?pp1="+pp1,null);
			jo = new JSONObject(jsondata);
			userDto.id = Integer.parseInt(pp1);
			userDto.name = jo.getJSONArray("uname").get(0).toString();
			ja = jo.getJSONArray("myMeishi");
			int count = ja.length();
			for (int i=0; i < count; i++) {
				MeishiDto wm = new MeishiDto();
				wm.id = ja.getInt(i);
				userDto.myMeishi.add(wm);
			}
			ja = jo.getJSONArray("hasMeishi");
			count = ja.length();
			for (int i=0; i < count; i++) {
				MeishiDto wm = new MeishiDto();
				wm.id = ja.getInt(i);
				userDto.hasMeishi.add(wm);
			}
					
		} catch (Exception e) {
			Log.d("doInBackground.err", e.toString());
			return null; //e.toString();
		}

		flagEnd = true;
		return userDto;
	}

}
