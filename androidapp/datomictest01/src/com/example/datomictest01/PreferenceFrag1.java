package com.example.datomictest01;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenceFrag1 extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences_frag_1);
	}

}
