
package com.federicocolantoni.projects.interventix;

import org.acra.ACRA;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

@SuppressLint("NewApi")
public class BaseActivity extends SherlockFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	try {
	    StrictModeWrapper.init(BaseActivity.this);
	} catch (Throwable e) {
	    ACRA.getErrorReporter().handleSilentException(e);
	}

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
		    .getDefaultSharedPreferences(context
			    .getApplicationContext());
	    return prefs;
	}

	@Override
	protected void onPostExecute(SharedPreferences result) {

	    super.onPostExecute(result);
	}
    }
}
