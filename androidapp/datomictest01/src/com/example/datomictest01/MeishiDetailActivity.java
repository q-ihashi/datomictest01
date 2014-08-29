package com.example.datomictest01;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.util.HttpUtil;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MeishiDetailActivity extends Activity {

	MeishiDto meishi;
	AQuery aq;
	List<String> hists = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meishi_detail);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		aq = new AQuery(this);
		meishi = (MeishiDto)getIntent().getParcelableExtra("MEISHIDETAIL");
		printDetail();

//		～履歴情報の取得をここで行う～
		(new Thread(new Runnable() {
			@Override
			public void run() {
				String jsondata = null;
				JSONObject jo = null;
				JSONArray ja = null;
				String ret = "";
				try {
					jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "meishi-hist-p?pp1="+meishi.id,null);
					jo = new JSONObject(jsondata);
					ja = jo.getJSONArray("txInstant");
					int count = ja.length();
					for (int i=0 ; i < count; i++) {
						ret = ret + "," + ja.getLong(i);
						hists.add(String.valueOf(ja.getLong(i)));
					}
				} catch (Exception e) {
					Log.d("onCreate", e.toString());
					return;
				}
				final String w = jsondata;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						TextView txtJSON = (TextView)findViewById(R.id.txtJSON);
						txtJSON.setText(w);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(MeishiDetailActivity.this, android.R.layout.simple_list_item_1);
						ListView listView = (ListView) findViewById(R.id.listView1);
						adapter.addAll(hists);
						listView.setAdapter(adapter);
						listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								ListView listView = (ListView) parent;
								final String item = (String) listView.getItemAtPosition(position);
//								Toast.makeText(getApplication(), item, Toast.LENGTH_LONG).show();
								(new Thread(new Runnable() {
									
									@Override
									public void run() {
										String jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + 
												"meishi-get-tp?pp1="+meishi.id+"&pp2="+item,null);
										JSONObject wjo = null;
										JSONArray wja = null;
										try {
											wjo = new JSONObject(jsondata);
			//								meishi.id = ja.getInt(i);
											wja = wjo.getJSONArray("title");
											if (wja != null && wja.length()>0) meishi.title = wja.getString(0); else meishi.title="";
											wja = wjo.getJSONArray("company");
											if (wja != null && wja.length()>0) meishi.company = wja.getString(0); else meishi.company="";
											wja = wjo.getJSONArray("name");
											if (wja != null && wja.length()>0) meishi.name = wja.getString(0); else meishi.name="";
											wja = wjo.getJSONArray("addr");
											if (wja != null && wja.length()>0) meishi.addr = wja.getString(0); else meishi.addr="";
											wja = wjo.getJSONArray("tel");
											if (wja != null && wja.length()>0) meishi.tel = wja.getString(0); else meishi.tel="";
											wja = wjo.getJSONArray("email");
											if (wja != null && wja.length()>0) meishi.email = wja.getString(0); else meishi.email="";
											printDetail();
										} catch (Exception e) {
											Log.d("setOnItemClickListener-meishi-get-tp", wja.toString(), e);
										}
									}
								})).start();

						}});
					}
				});
			}
		})).start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.meishi_detail, menu);
		return true;
	}

	/**
	 * メニュー選択時
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void printDetail() {
//		String tb = AppConfig.getImageBaseUrl() + "coupon_" + meishi.id + ".jpg";
//		aq.id(R.id.coupon_detail_image).image(tb, true, true, 0, R.drawable.missing, null, AQuery.FADE_IN_NETWORK, 1.0f);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				aq.id(R.id.txtMeishiTitle).text(meishi.title);
				aq.id(R.id.txtMeishiCompany).text(meishi.company);
				aq.id(R.id.txtMeishiName).text(meishi.name);
				aq.id(R.id.txtMeishiAddress).text(meishi.addr);
				aq.id(R.id.txtMeishiTel).text(meishi.tel);
				aq.id(R.id.txtMeishiEmail).text(meishi.email);
			}
		});

//		aq.id(R.id.coupon_detail_expire).text(
//			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY).format(coupon.expireDate)
//		);
	}


}
