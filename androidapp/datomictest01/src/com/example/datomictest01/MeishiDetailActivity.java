package com.example.datomictest01;

import com.androidquery.AQuery;
import com.example.datomictest01.dto.MeishiDto;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class MeishiDetailActivity extends Activity {

	MeishiDto meishi;
	AQuery aq;

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.meishi_detail, menu);
		return true;
	}

	/**
	 * ÉÅÉjÉÖÅ[ëIëéû
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

		aq.id(R.id.txtMeishiTitle).text(meishi.title);
		aq.id(R.id.txtMeishiCompany).text(meishi.company);
		aq.id(R.id.txtMeishiName).text(meishi.name);
		aq.id(R.id.txtMeishiAddress).text(meishi.addr);
		aq.id(R.id.txtMeishiTel).text(meishi.tel);
		aq.id(R.id.txtMeishiEmail).text(meishi.email);

//		aq.id(R.id.coupon_detail_expire).text(
//			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY).format(coupon.expireDate)
//		);
	}


}
