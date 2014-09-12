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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MeishiNewActivity extends Activity {

	MeishiDto meishi;
	AQuery aq;
	List<String> hists = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meishi_new);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		aq = new AQuery(this);
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

	public void onClickSave(View v) {
		switch (v.getId()) {
		case R.id.btnSave:
			Toast.makeText(this, "Save MEISHI.", Toast.LENGTH_LONG).show();
			finish();
			//名刺保存処理
			saveMeishi();
			break;
		default:
			Toast.makeText(this, "default:"+v.getId(), Toast.LENGTH_LONG).show();
			break;
		}
		
	}

	private void saveMeishi() {
		
		//保存処理
	}

}
