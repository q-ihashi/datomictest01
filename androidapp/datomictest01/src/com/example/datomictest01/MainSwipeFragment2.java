/**
 * 
 */
package com.example.datomictest01;

import java.util.List;

import com.example.datomictest01.adapter.MeishiAdapter;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.dto.UserDto;
import com.example.datomictest01.task.MeishiGetTask;
import com.example.datomictest01.util.TaskCallback;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ihashi
 *
 */
public class MainSwipeFragment2 extends Fragment {

	View rootView =null;

	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_swipe2,
				container, false);
//		my_meishis.clear();
//		AsyncTask<String, Integer,  List<MeishiDto>> t = new MeishiGetTask(new MeishiGetTaskCallback());
//		t.execute(new String[] {""+MainSwipeActivity.userDto.id});

		return rootView;
	}
	public void updateView() {
		onResume();
	}
	//################################
	class MeishiGetTaskCallback implements TaskCallback<MeishiDto> {

		@Override
		public void onSuccess(List<MeishiDto> list) {
			setViewMeishiList(list);
		}

		@Override
		public void onSuccess(List<MeishiDto> list, boolean more) {}

		@Override
		public void onSuccess(final MeishiDto data) {
//			tvMsg.setText("END."+jsondata);
		}

		@Override
		public void onFailure(final String errmsg) {
//			tvMsg.setText("ERR."+errmsg);
		}
		
	}
	public void setViewMeishiList(List<MeishiDto> meishis) {
//		final View mViewPager = (ViewPager) .findViewById(R.id.pager);
		final Activity act = getActivity();
		final FrameLayout f = (FrameLayout)act.findViewById(R.id.fragment_main_swipe2_frame);
//		View meishiListView = new ListView(act);
		View meishiListView = LayoutInflater.from(f.getContext()).inflate(R.layout.list_mymeishi, f);
		final MeishiAdapter adapter = new MeishiAdapter(act, R.layout.list_mymeishi_item, meishis);
		final View v = meishiListView;
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				if (rootView == null) return;
				TextView tx = (TextView)v.findViewById(R.id.textView);
				if (tx != null) tx.setText(getString(R.string.title_head_section3));
				ListView listView = (ListView)v.findViewById(R.id.listView);
//				ListView listView = (ListView)v;
				if (listView == null) return;
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MeishiAdapter headerViewListAdapter = (MeishiAdapter) parent.getAdapter();
						MeishiDto meishi = (MeishiDto) headerViewListAdapter.getItem(position);
						//Coupon詳細画面へ
						Intent intent = new Intent(getActivity().getApplicationContext(), MeishiDetailActivity.class);
						intent.putExtra("MEISHIDETAIL", meishi);
						startActivity(intent);
					}
				});
			}
		});
	}
}
