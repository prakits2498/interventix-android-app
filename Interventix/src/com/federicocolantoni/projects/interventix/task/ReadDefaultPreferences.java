package com.federicocolantoni.projects.interventix.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class ReadDefaultPreferences extends AsyncTask<Void, Void, SharedPreferences> {
    
    private final Context context;
    
    public ReadDefaultPreferences(Context context) {
    
	this.context = context;
    }
    
    @Override
    protected SharedPreferences doInBackground(Void... params) {
    
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	return prefs;
    }
    
    @Override
    protected void onPostExecute(SharedPreferences result) {
    
	super.onPostExecute(result);
    }
}
