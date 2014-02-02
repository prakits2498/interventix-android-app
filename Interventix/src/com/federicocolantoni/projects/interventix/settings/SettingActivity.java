package com.federicocolantoni.projects.interventix.settings;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.federicocolantoni.projects.interventix.R;

/**
 * Created by federico on 19/05/13.
 */
@SuppressLint("NewApi")
public class SettingActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
		else
			addPreferencesFromResource(R.xml.activity_support_preferences_options);
	}
}
