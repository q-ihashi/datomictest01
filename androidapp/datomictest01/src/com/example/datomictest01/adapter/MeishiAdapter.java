package com.example.datomictest01.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.datomictest01.R;
import com.example.datomictest01.dto.MeishiDto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
			convertView = inflater.inflate(R.layout.list_mymeishi_item, null);
		}
		AQuery aq = new AQuery(convertView);
		aq.id(R.id.txtMeishiTitle).text(meishiDto.id+":" + meishiDto.title);
		aq.id(R.id.txtMeishiCompany).text(meishiDto.company);
		aq.id(R.id.txtMeishiName).text(meishiDto.name);
		aq.id(R.id.txtMeishiAddress).text(meishiDto.addr);
		aq.id(R.id.txtMeishiTel).text(meishiDto.tel);
		aq.id(R.id.txtMeishiEmail).text(meishiDto.email);
//		aq.id(R.id.list_newsfeed_time).text(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY).format(newsfeed.time));

//		String tb = AppConfig.getImageNewsfeedBaseUrl() + newsfeed.user.id + ".jpg";
//		aq.id(R.id.list_newsfeed_image).image(tb, true, true, 0, R.drawable.missing, null, AQuery.FADE_IN_NETWORK, 1.0f);

		return convertView;
	}

}
