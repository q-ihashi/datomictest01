package com.example.datomictest01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

public class MainSwipeActivity extends Activity implements ActionBar.TabListener {

	private List<MeishiDto> my_meishis = new ArrayList<MeishiDto>();
	private SharedPreferences sharedPref;
	private static String pref_userId = "";

	SectionsPagerAdapter mSectionsPagerAdapter;
	ActionBar actionBar = null;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_swipe);
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setOnPageChangeListener(
			new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					actionBar.setSelectedNavigationItem(position);
					//updateMainFragAct0(); //Newsfeedは毎回更新したいかも．
				}
		});
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		//設定情報読み込み
		AppConfig.getPreferenceData(this);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		pref_userId = sharedPref.getString("pref_userId", "1");

		AsyncTask t = new MeishiGetTask(new MeishiGetTaskCallback());
		t.execute(new String[] {pref_userId});
//		TextView tvMsg = null;
//		tvMsg = (TextView)findViewById(R.id.tvMsg);
//		tvMsg.setText("GET Start.");

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
//			NavUtils.navigateUpFromSameTask(this);
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
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mViewPager != null) mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}


	//################################
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_swipe1,
					container, false);

			return rootView;
		}
		
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
//			tvMsg.setText("END."+jsondata);
		}

		@Override
		public void onFailure(final String errmsg) {
//			tvMsg.setText("ERR."+errmsg);
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
