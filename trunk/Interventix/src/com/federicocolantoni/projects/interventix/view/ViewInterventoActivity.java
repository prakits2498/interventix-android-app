
package com.federicocolantoni.projects.interventix.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;

@SuppressLint("NewApi")
public class ViewInterventoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.view_intervento);

	Bundle extras = getIntent().getExtras();

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Activity.MODE_PRIVATE);

	String nominativo = prefs.getString(Constants.USER_NOMINATIVO, null);

	getSupportActionBar().setTitle(nominativo);

	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();

	OverViewInterventoFragment overView = new OverViewInterventoFragment();

	Bundle bundle = new Bundle();
	bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	bundle.putAll(extras);

	overView.setArguments(bundle);

	transaction.add(R.id.fragments_layout, overView,
		Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.VIEW_INTERVENTO_TRANSACTION);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	if (!prefs.getBoolean(Constants.EDIT_MODE, false)) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {

		FragmentManager manager = getSupportFragmentManager();

		if (manager.getBackStackEntryCount() == 1) {
		    this.finish();
		} else {
		    manager.popBackStackImmediate();
		}
		return true;
	    }
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	if (!prefs.getBoolean(Constants.EDIT_MODE, false)) {
	    FragmentManager manager = getSupportFragmentManager();

	    if (manager.getBackStackEntryCount() == 1) {
		this.finish();
	    } else {
		manager.popBackStackImmediate();
	    }
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	final MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.view_intervento, menu);

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {

	    case android.R.id.home:

		this.finish();

	    case R.id.edit_mode:

		SharedPreferences prefs = getSharedPreferences(
			Constants.PREFERENCES, Activity.MODE_PRIVATE);

		if (!prefs.getBoolean(Constants.EDIT_MODE, false)) {

		    System.out.println("EDIT_MODE false");

		    final Editor editor = prefs.edit();

		    editor.putBoolean(Constants.EDIT_MODE, true);

		    System.out.println("EDIT_MODE set to true");

		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		    } else {
			new Thread(new Runnable() {

			    @Override
			    public void run() {

				editor.commit();
			    }
			});
		    }

		    sendBroadcast(new Intent(Constants.EDIT_MODE));
		} else {

		    System.out.println("EDIT_MODE true");

		    final Editor editor = prefs.edit();

		    editor.putBoolean(Constants.EDIT_MODE, false);

		    System.out.println("EDIT_MODE set to false");

		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		    } else {
			new Thread(new Runnable() {

			    @Override
			    public void run() {

				editor.commit();
			    }
			});
		    }

		    sendBroadcast(new Intent(Constants.EDIT_MODE));
		}

		break;
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();
    }
}
