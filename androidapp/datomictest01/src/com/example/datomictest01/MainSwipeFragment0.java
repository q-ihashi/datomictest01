/**
 * 
 */
package com.example.datomictest01;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.datomictest01.adapter.MeishiAdapter;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.task.MeishiGetTask;
import com.example.datomictest01.util.HttpUtil;
import com.example.datomictest01.util.TaskCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author ihashi
 *
 */
public class MainSwipeFragment0 extends Fragment implements OnClickListener {

	View rootView =null;

	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_swipe0,
				container, false);
//		my_meishis.clear();
		AsyncTask<String, Integer,  List<MeishiDto>> t = new MeishiGetTask(new MeishiGetTaskCallback());
		t.execute(new String[] {"myMeishi", ""+MainSwipeActivity.userDto.id});

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
		final FrameLayout f = (FrameLayout)act.findViewById(R.id.fragment_main_swipe0_frame);
//		View meishiListView = new ListView(act);
		View meishiListView = LayoutInflater.from(f.getContext()).inflate(R.layout.list_mymeishi, f);
		final MeishiAdapter adapter = new MeishiAdapter(act, R.layout.list_mymeishi_item, meishis);
		final View v = meishiListView;
		act.runOnUiThread(new Runnable() {
			@Override
			public void run() {
//				if (rootView == null) return;
				Button btnTop = (Button)v.findViewById(R.id.btnTop);
				btnTop.setOnClickListener(MainSwipeFragment0.this);
				btnTop.setText("Add..");
				btnTop.setVisibility(Button.VISIBLE);
				
				TextView tx = (TextView)v.findViewById(R.id.textView);
				if (tx != null) tx.setText(getString(R.string.title_head_section1));
				ListView listView = (ListView)v.findViewById(R.id.listView);
//				ListView listView = (ListView)v;
				if (listView == null) return;
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						MeishiAdapter headerViewListAdapter = (MeishiAdapter) parent.getAdapter();
						MeishiDto meishi = (MeishiDto) headerViewListAdapter.getItem(position);
						//Meishi詳細画面へ
						Intent intent = new Intent(getActivity().getApplicationContext(), MeishiDetailActivity.class);
						intent.putExtra("MEISHIDETAIL", meishi);
						startActivity(intent);
					}
				});
				listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						final MeishiDto item = (MeishiDto) parent.getItemAtPosition(position);
						//テキスト入力を受け付けるビューを作成します。
						final EditText editView = new EditText(getActivity());
						new AlertDialog.Builder(getActivity())
							.setIcon(android.R.drawable.ic_dialog_info)
							.setTitle(item.title+"を誰に渡しますか？")
							//setViewにてビューを設定します。
							.setView(editView)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									//入力した文字をトースト出力する
									Toast.makeText(getActivity(), 
											editView.getText().toString(), 
											Toast.LENGTH_LONG).show();
									if (editView.getText().toString().length()>0) {
										sendMeishi(item, Integer.parseInt(editView.getText().toString()));
									}
								}
							})
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
								}
							})
							.show();
						return true;
					}
				});
			}
		});
	}
	public boolean sendMeishi(MeishiDto meishi, int toUid) {
		Toast.makeText(getActivity(), 
				"SendMeishi!", 
				Toast.LENGTH_LONG).show();
//		String jsondata = null;
		JSONObject jo = null;
		JSONArray ja = null;
		String ret = "";
		final String pp1 = String.valueOf(toUid);
		final String pp2 = String.valueOf(meishi.id);
		(new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
//					jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "meishi-exist-p?pp1="+pp1+"&pp2="+pp2,null);
					String jsondata = HttpUtil.getContentBySendJson(AppConfig.getApiBaseUrl() + "hasmeishi-add-p?pp1="+pp1+"&pp2="+pp2,null);
				} catch (Exception e) {
					Log.d("sendMeishi", e.toString());
//					return false
				}
			}
		})).start();
		return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTop:
//			Toast.makeText(getActivity(), "New MEISHI", Toast.LENGTH_LONG).show();
			//名刺追加画面へ
			Intent intent = new Intent(getActivity().getApplicationContext(), MeishiNewActivity.class);
			startActivity(intent);
			break;
		default:
			Toast.makeText(getActivity(), "default:"+v.getId(), Toast.LENGTH_LONG).show();
			break;
		}
		
	}
}
