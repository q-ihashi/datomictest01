package com.example.datomictest01;

import java.util.List;
import java.util.Locale;

import com.example.datomictest01.dto.UserDto;
import com.example.datomictest01.task.UserGetTask;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainSwipeActivity extends Activity implements ActionBar.TabListener {

	private SharedPreferences sharedPref;
	static String pref_userId = "";
	static UserDto userDto = null;

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
		
		//設定情報読み込み
		AppConfig.getPreferenceData(this);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		pref_userId = sharedPref.getString("pref_userId", "1");

		AsyncTask<String, Integer,  UserDto> t = new UserGetTask(new UserGetTaskCallback());
		t.execute(new String[] {MainSwipeActivity.pref_userId});

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
		if (mViewPager != null) {
			mViewPager.setCurrentItem(tab.getPosition());
//			setViewMeishiList(tab.getPosition());
		}
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
			return PlaceholderFragment.newInstance(position);
		}

		@Override
		public int getCount() {
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
	public static class PlaceholderFragment {
		private static final String ARG_SECTION_NUMBER = "section_number";

		public static Fragment newInstance(int sectionNumber) {
			Fragment fragment = null;
			switch (sectionNumber) {
			case 0:
				fragment =new MainSwipeFragment0();
				break;
			case 1:
				fragment =new MainSwipeFragment1();
				break;
			case 2:
				fragment =new MainSwipeFragment2();
				break;
			}
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

	}
	//################################
	class UserGetTaskCallback implements TaskCallback<UserDto> {
		@Override
		public void onSuccess(final UserDto data) {
			userDto = data;
			// Create the adapter that will return a fragment for each of the three
			// primary sections of the activity.
			mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.pager_main);
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
						.setTabListener(MainSwipeActivity.this));
			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), data.name.toString(), Toast.LENGTH_LONG).show();
				}
			});

		}
		@Override
		public void onFailure(final String errmsg) {
//			tvMsg.setText("ERR."+errmsg);
		}
		@Override
		public void onSuccess(List<UserDto> list) {}
		@Override
		public void onSuccess(List<UserDto> list, boolean more) {}

	}

}
