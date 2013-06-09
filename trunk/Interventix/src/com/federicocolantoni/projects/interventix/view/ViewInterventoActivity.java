
package com.federicocolantoni.projects.interventix.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
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
	bundle.putString("NOMINATIVO", nominativo);
	bundle.putAll(extras);

	overView.setArguments(bundle);

	transaction.add(R.id.fragments_layout, overView,
		Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.VIEW_INTERVENTO_TRANSACTION);
	transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    return true;
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

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
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();
    }
}
