package com.example.datomictest01.dto;

import java.util.ArrayList;
import java.util.List;

import android.net.Credentials;
import android.os.Parcel;
import android.os.Parcelable;

public class UserDto implements Parcelable {
	public UserDto() {}

	public Integer id = new Integer(0);
	public String loginId;
	public String name;
	public List<MeishiDto> myMeishi = new ArrayList<MeishiDto>();
	public List<MeishiDto> hasMeishi = new ArrayList<MeishiDto>();

	public static final Parcelable.Creator<UserDto> CREATOR =
			new Parcelable.Creator<UserDto>() {
				@Override
				public UserDto createFromParcel(Parcel source) {
					return new UserDto(source);
				}
				@Override
				public UserDto[] newArray(int size) {
					return new UserDto[size];
				}
				
			};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		if (loginId != null) arg0.writeString(loginId); else arg0.writeString("");
		if (name != null) arg0.writeString(name); else arg0.writeString("");
		arg0.writeList(myMeishi);
		arg0.writeList(hasMeishi);
	}

	private UserDto(Parcel in) {
		id        = in.readInt();
		loginId   = in.readString();
		name      = in.readString();
		myMeishi.clear();
		in.readList(myMeishi,null);
		hasMeishi.clear();
		in.readList(hasMeishi,null);
	}

}
