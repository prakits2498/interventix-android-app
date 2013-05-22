
package com.federicocolantoni.projects.interventix;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;

public class Interventix extends Application {

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {

	//*** Strict Mode - Start ***///

	StrictMode.setThreadPolicy(new Builder().detectAll().penaltyLog()
		.build());
	StrictMode.setVmPolicy(new VmPolicy.Builder().detectAll().penaltyLog()
		.build());

	//*** Strict Mode - End ***///

	super.onCreate();
	loadAsyncTask();
	createInterventixDB();
    }

    private void createInterventixDB() {

    }

    private void loadAsyncTask() {

	try {
	    Class.forName("android.os.AsyncTask");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
