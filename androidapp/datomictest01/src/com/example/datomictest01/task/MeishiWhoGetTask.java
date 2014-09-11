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
	 * param params[0]:ユーザ番号/ 1～
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
		String datacol = "data";
		String pp1 = params[0];
		try {
			jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "meishi-who-p?pp1="+pp1,null);
			jo = new JSONObject(jsondata);
////このあたりはAPI変更して簡易になるようにしたいが．．．いったんこのまま
			ja = jo.getJSONArray(datacol);
			int count = ja.length();
			for (int i=0 ; i < count; i++) {
				UserDto wu = new UserDto();
				JSONArray wa = ja.getJSONArray(i);
				wu.id = wa.getInt(0);
				wu.name = wa.getString(1);
				MeishiDto wm = new MeishiDto();
				wm.id = wa.getInt(2);
				wm.name = wa.getString(3);
				wm.user.id = wa.getInt(4);
				wm.user.name = wa.getString(5);
				wu.hasMeishi.add(wm);
				users.add(wu);
			}
		} catch (Exception e) {
			Log.d("doInBackground/meishis", e.toString());
			return users; //e.toString();
		}

		flagEnd = true;
		return users;
	}

}
