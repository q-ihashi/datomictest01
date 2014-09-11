package com.example.datomictest01.adapter;

import java.util.List;

import com.androidquery.AQuery;
import com.example.datomictest01.R;
import com.example.datomictest01.dto.MeishiDto;
import com.example.datomictest01.dto.UserDto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class UserMeishiAdapter extends ArrayAdapter<UserDto> {

	private List<UserDto> items;
	private LayoutInflater inflater;
	
	public UserMeishiAdapter(Context context, int resourceId, List<UserDto> items) {
		super(context, resourceId, items);
		this.items = items;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserDto userDto = items.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_usermeishi_item, null);
		}
		AQuery aq = new AQuery(convertView);
		aq.id(R.id.txtMeishiName).text(userDto.hasMeishi.get(0).name);
		aq.id(R.id.txtUserName).text(userDto.name);
//		aq.id(R.id.list_newsfeed_time).text(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY).format(newsfeed.time));

//		String tb = AppConfig.getImageNewsfeedBaseUrl() + newsfeed.user.id + ".jpg";
//		aq.id(R.id.list_newsfeed_image).image(tb, true, true, 0, R.drawable.missing, null, AQuery.FADE_IN_NETWORK, 1.0f);

		return convertView;
	}

}
