package com.federicocolantoni.projects.interventix.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment;

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
				Context.MODE_PRIVATE);

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
		transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			FragmentManager manager = getSupportFragmentManager();

			if (manager.getBackStackEntryCount() == 1) {
				finish();
			} else {
				manager.popBackStackImmediate();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {

		FragmentManager manager = getSupportFragmentManager();

		if (manager.getBackStackEntryCount() == 1) {
			finish();
		} else {
			manager.popBackStackImmediate();
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	//
	// final MenuInflater inflater = getSupportMenuInflater();
	// inflater.inflate(R.menu.view_intervento, menu);
	//
	// return super.onCreateOptionsMenu(menu);
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case android.R.id.home :

				finish();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}
