package com.federicocolantoni.projects.interventix;

import org.androidannotations.annotations.EApplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.bugsense.trace.BugSenseHandler;

@EApplication
public class Interventix extends Application {

	private static Interventix instance;

	public static Interventix getInstance() {

		return instance;
	}

	public static Context getContext() {

		return instance.getApplicationContext();
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {

		BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

		instance = this;

		super.onCreate();
		loadAsyncTask();
	}

	private void loadAsyncTask() {

		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {

			BugSenseHandler.sendException(e);

			e.printStackTrace();
		}
	}
}
