
package com.federicocolantoni.projects.interventix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

@SuppressLint("NewApi")
public class BaseActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {

	    //*** Strict Mode - Start ***///

	    StrictMode.setThreadPolicy(new Builder().detectAll().penaltyLog()
		    .build());
	    StrictMode.setVmPolicy(new VmPolicy.Builder().detectAll()
		    .penaltyLog().build());

	    //*** Strict Mode - End ***///
	}

	super.onCreate(savedInstanceState);

	getSupportActionBar().setTitle(Constants.INTERVENTIX_TITLE);
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();
	if (BuildConfig.DEBUG) {
	    System.gc();
	}
    }

    public static class ReadDefaultPreferences extends
	    AsyncTask<Void, Void, SharedPreferences> {

	private Context context;

	public ReadDefaultPreferences(Context context) {

	    this.context = context;
	}

	@Override
	protected SharedPreferences doInBackground(Void... params) {

	    SharedPreferences prefs = PreferenceManager
		    .getDefaultSharedPreferences(context);
	    return prefs;
	}

	@Override
	protected void onPostExecute(SharedPreferences result) {

	    super.onPostExecute(result);
	}
    }
}
