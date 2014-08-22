package com.example.datomictest01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.util.HttpUtil;
import com.example.datomictest01.util.TaskCallback;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	private List<MeishiDto> my_meishis = new ArrayList<MeishiDto>();
	private TextView tvMsg = null;
	private SharedPreferences sharedPref;
	private String pref_userId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvMsg = (TextView)findViewById(R.id.tvMsg);

		//設定情報読み込み
		AppConfig.getPreferenceData(this);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		pref_userId = sharedPref.getString("pref_userId", "1");

		AQuery aq = new AQuery(this);
		AsyncTask t = new MeishiGetTask(new MeishiGetTaskCallback());
		t.execute(new String[] {pref_userId});
		tvMsg.setText("GET Start.");

//		List<Map<String,String>> datas = createViewData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * メニュー選択時
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings: //設定画面へ
			Intent intent = new Intent();
			intent.setClassName(
				"com.example.datomictest01",
				"com.example.datomictest01.Preference");
			startActivity(intent);
			return true;
		case R.id.action_exit: //終了
			finish();
		}
		return super.onOptionsItemSelected(item);
	}



	//################################
	class MeishiGetTask extends AsyncTask<String, Integer,  String> {

		TaskCallback<String> callback;
		boolean flagEnd = false;

		public MeishiGetTask(TaskCallback<String> callback) {
			super();
			this.callback = callback;
		}

		@Override
		protected void onPostExecute(String result) {
			if (flagEnd) {
				callback.onSuccess(result);
			} else {
				callback.onFailure(result);
			}
		}

		@Override
		protected String doInBackground(String... params) {
//			Map param = new HashMap<String, String>();
//			param.put("pp1", params[0]);
			String jsondata = null;
			JSONObject jo = null;
			JSONArray ja = null;
			String ret = "";
			try {
				jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "user-get-p?pp1="+params[0],null);
				jo = new JSONObject(jsondata);
//このあたりはAPI変更して簡易になるようにしたいが．．．いったんこのまま
				ja = jo.getJSONArray("myMeishi");
				int count = ja.length();
				for (int i=0 ; i < count; i++) {
					ret = ret + "," + ja.getInt(i);
					jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "meishi-get-p?pp1="+ja.getInt(i),null);
					jo = new JSONObject(jsondata);
					MeishiDto md = new MeishiDto();
					md.id = ja.getInt(i);
					md.title = jo.getJSONArray("title").getString(0);
					md.company = jo.getJSONArray("company").getString(0);
					md.name = jo.getJSONArray("name").getString(0);
					md.addr = jo.getJSONArray("addr").getString(0);
					md.tel = jo.getJSONArray("tel").getString(0);
					md.email = jo.getJSONArray("email").getString(0);
					my_meishis.add(md);
				}
			} catch (Exception e) {
				Log.d("doInBackground", e.toString());
				return e.toString();
			}

			// Adapterの作成
			final MeishiAdapter adapter = new MeishiAdapter(getApplication(),R.layout.meishilist,my_meishis);
			// ListViewにAdapterを関連付ける
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ListView listView = (ListView)findViewById(R.id.listView1);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							MeishiAdapter headerViewListAdapter = (MeishiAdapter) parent.getAdapter();
							MeishiDto meishi = (MeishiDto) headerViewListAdapter.getItem(position);
							//Coupon詳細画面へ
							Intent intent = new Intent(getApplicationContext(), MeishiDetailActivity.class);
							intent.putExtra("MEISHIDETAIL", meishi);
							startActivity(intent);
						}
					});
				}
			});
			flagEnd = true;
			return ret;
		}

	}
	//################################
	class MeishiGetTaskCallback implements TaskCallback<String> {

		@Override
		public void onSuccess(List<String> list) {}

		@Override
		public void onSuccess(List<String> list, boolean more) {}

		@Override
		public void onSuccess(final String jsondata) {
			tvMsg.setText("END."+jsondata);
		}

		@Override
		public void onFailure(final String errmsg) {
			tvMsg.setText("ERR."+errmsg);
		}
		
	}
	//################################
	public class MeishiAdapter extends ArrayAdapter<MeishiDto> {

		private List<MeishiDto> items;
		private LayoutInflater inflater;
		
		public MeishiAdapter(Context context, int resourceId, List<MeishiDto> items) {
			super(context, resourceId, items);
			this.items = items;
			this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MeishiDto meishiDto = items.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.meishilist, null);
			}
			AQuery aq = new AQuery(convertView);
			aq.id(R.id.txtMeishiTitle).text(meishiDto.title);
			aq.id(R.id.txtMeishiCompany).text(meishiDto.company);
			aq.id(R.id.txtMeishiName).text(meishiDto.name);
			aq.id(R.id.txtMeishiAddress).text(meishiDto.addr);
			aq.id(R.id.txtMeishiTel).text(meishiDto.tel);
			aq.id(R.id.txtMeishiEmail).text(meishiDto.email);
//			aq.id(R.id.list_newsfeed_time).text(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY).format(newsfeed.time));

//			String tb = AppConfig.getImageNewsfeedBaseUrl() + newsfeed.user.id + ".jpg";
//			aq.id(R.id.list_newsfeed_image).image(tb, true, true, 0, R.drawable.missing, null, AQuery.FADE_IN_NETWORK, 1.0f);

			return convertView;
		}
	}
}
