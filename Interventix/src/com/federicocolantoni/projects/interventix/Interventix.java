package com.federicocolantoni.projects.interventix;

import android.annotation.SuppressLint;
import android.app.Application;

import com.bugsense.trace.BugSenseHandler;

public class Interventix extends Application {
    
    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
	
	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);
	
	super.onCreate();
	loadAsyncTask();
    }
    
    private void loadAsyncTask() {
	
	try {
	    Class.forName("android.os.AsyncTask");
	}
	catch (ClassNotFoundException e) {
	    
	    BugSenseHandler.sendException(e);
	    
	    e.printStackTrace();
	}
    }
}
