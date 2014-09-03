/**
 * 
 */
package com.example.datomictest01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.util.HttpUtil;
import com.example.datomictest01.util.TaskCallback;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author ihashi
 *
 */
public class MainSwipeFragment0 extends Fragment {

	private List<MeishiDto> my_meishis = new ArrayList<MeishiDto>();
//	private List<MeishiDto> has_meishis = new ArrayList<MeishiDto>();
//	private List<MeishiDto> who_meishis = new ArrayList<MeishiDto>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_swipe1,
				container, false);
		AsyncTask t = new MeishiGetTask(new MeishiGetTaskCallback());
		t.execute(new String[] {MainSwipeActivity.pref_userId});

		return rootView;
	}
	@Override
	public void onResume() {
		super.onResume();
		final Activity act = getActivity();
		if (my_meishis.size() != 0) {
			AsyncTask t = new MeishiGetTask(new MeishiGetTaskCallback());
			t.execute(new String[] {MainSwipeActivity.pref_userId});
		}
	}
	public void updateView() {
		onResume();
	}
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

			flagEnd = true;
			return ret;
		}

	}
	//################################
	class MeishiGetTaskCallback implements TaskCallback<String> {

		@Override
		public void onSuccess(List<String> list) {
		}

		@Override
		public void onSuccess(List<String> list, boolean more) {}

		@Override
		public void onSuccess(final String jsondata) {
//			tvMsg.setText("END."+jsondata);
			setViewMeishiList(my_meishis);
		}

		@Override
		public void onFailure(final String errmsg) {
//			tvMsg.setText("ERR."+errmsg);
		}
		
	}
	public void setViewMeishiList(List<MeishiDto> meishis) {
		final Activity act = getActivity();
		final MeishiAdapter adapter = new MeishiAdapter(act.getApplication(),R.layout.meishilist,meishis);
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView tx = (TextView)act.findViewById(R.id.textView1);
				if (tx != null) tx.setText(tx.getText().toString());
				ListView listView = (ListView)act.findViewById(R.id.listView1);
				if (listView == null) return;
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MeishiAdapter headerViewListAdapter = (MeishiAdapter) parent.getAdapter();
						MeishiDto meishi = (MeishiDto) headerViewListAdapter.getItem(position);
						//Coupon詳細画面へ
						Intent intent = new Intent(act.getApplicationContext(), MeishiDetailActivity.class);
						intent.putExtra("MEISHIDETAIL", meishi);
						startActivity(intent);
					}
				});
			}
		});
	}
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
