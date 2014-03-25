package com.federicocolantoni.projects.interventix.application;

import org.androidannotations.annotations.EApplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.j256.ormlite.android.apptools.OpenHelperManager;

@EApplication
public class Interventix extends Application {

    private static Interventix instance;

    // private static InterventixDBHelper dbHelper;

    public static Interventix getInstance() {

	return instance;
    }

    public static Context getContext() {

	return instance.getApplicationContext();
    }

    /**
     * Restituisce un'istanza singleton della classe InterventixDBHelper.
     * 
     * @return un oggetto di tipo InterventixDBHelper.
     */
    public static InterventixDBHelper getDbHelper() {

	// if (dbHelper == null)
	// dbHelper = OpenHelperManager.getHelper(instance, InterventixDBHelper.class);

	return InterventixDBHelperHolder.INSTANCE;
    }

    private static class InterventixDBHelperHolder {

	public static final InterventixDBHelper INSTANCE = OpenHelperManager.getHelper(instance, InterventixDBHelper.class);
    }

    protected InterventixDBHelper readResolve() {

	return getDbHelper();
    }

    /**
     * Rilascia l'helper del database decrementato di 1 il contatore di uso. L'helper verrà chiuso se il contatore è 0.
     */
    // public static void releaseDbHelper() {
    //
    // OpenHelperManager.releaseHelper();
    // dbHelper = null;
    // }

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
	}
	catch (ClassNotFoundException e) {

	    BugSenseHandler.sendException(e);

	    e.printStackTrace();
	}
    }
}
