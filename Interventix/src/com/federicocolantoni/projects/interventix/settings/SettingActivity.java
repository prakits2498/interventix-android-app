package com.federicocolantoni.projects.interventix.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by federico on 19/05/13.
 */
@SuppressLint("NewApi")
public class SettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
	}
}
