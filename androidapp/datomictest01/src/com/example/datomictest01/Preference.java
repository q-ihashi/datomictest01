package com.example.datomictest01;

import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class Preference extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
 
	@Override
	public void onStart(){
		super.onStart();
	}
 
	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preferences_header, target);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		String intentAction = intent.getAction();
		if (intentAction == null) return;
		if (intentAction.equals("Preference") && intent.getBooleanExtra("EXCR", false)) {
//			Toast.makeText(getApplicationContext(), "Preference#onNewIntent$Finish", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
}
