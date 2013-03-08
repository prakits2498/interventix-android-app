
package com.federicocolantoni.projects.interventix;

import android.app.Application;

public class Interventix extends Application {

    @Override
    public void onCreate() {

	super.onCreate();
	loadAsyncTask();
    }

    private void loadAsyncTask() {

	try {
	    Class.forName("android.os.AsyncTask");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
