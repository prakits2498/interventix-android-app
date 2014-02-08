package com.federicocolantoni.projects.interventix.settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.federicocolantoni.projects.interventix.R;

public class SettingSupportActivity extends PreferenceActivity {
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.activity_support_preferences_options);
    }
}
