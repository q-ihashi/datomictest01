package com.example.datomictest01.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class MeishiDto implements Parcelable {
	public MeishiDto() {}
	
	public Integer id;
	public String title;
	public String company;
	public String name;
	public String addr;
	public String tel;
	public String email;

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
		arg0.writeString(title);
		arg0.writeString(company);
		arg0.writeString(name);
		arg0.writeString(addr);
		arg0.writeString(tel);
		arg0.writeString(email);
	}
	
	private MeishiDto(Parcel in) {
		id      = in.readInt();
		title   = in.readString();
		company = in.readString();
		name    = in.readString();
		addr    = in.readString();
		tel     = in.readString();
		email   = in.readString();
	}

}
