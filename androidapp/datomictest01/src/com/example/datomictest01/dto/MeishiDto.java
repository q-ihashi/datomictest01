package com.example.datomictest01.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class MeishiDto implements Parcelable {
	public MeishiDto() {}
	
	public Integer id = new Integer(0);
	public String title;
	public String company;
	public String name;
	public String addr;
	public String tel;
	public String email;
	public UserDto user = new UserDto();

	public static final Parcelable.Creator<MeishiDto> CREATOR =
			new Parcelable.Creator<MeishiDto>() {
				@Override
				public MeishiDto createFromParcel(Parcel source) {
					return new MeishiDto(source);
				}
				@Override
				public MeishiDto[] newArray(int size) {
					return new MeishiDto[size];
				}
				
			};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(id);
		if (title != null) arg0.writeString(title); else arg0.writeString("");
		if (company != null) arg0.writeString(company); else arg0.writeString("");
		if (name != null) arg0.writeString(name); else arg0.writeString("");
		if (addr != null) arg0.writeString(addr); else arg0.writeString("");
		if (tel != null) arg0.writeString(tel); else arg0.writeString("");
		if (email != null) arg0.writeString(email); else arg0.writeString("");
		arg0.writeValue(user);
	}
	
	private MeishiDto(Parcel in) {
		id      = in.readInt();
		title   = in.readString();
		company = in.readString();
		name    = in.readString();
		addr    = in.readString();
		tel     = in.readString();
		email   = in.readString();
		user = (UserDto)in.readValue(this.getClass().getClassLoader());
	}

}
