package com.federicocolantoni.projects.interventix.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.federicocolantoni.projects.interventix.R;

/**
 * Created by federico on 19/05/13.
 */

@SuppressLint("NewApi")
public class SettingFragment extends PreferenceFragment {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.activity_preferences_options);
    }
}
