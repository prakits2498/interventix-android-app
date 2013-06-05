
package com.federicocolantoni.projects.interventix.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.intervento.Intervento;

/* TODO questa Activity consente di visualizzare tutti i dati di un intervento,
 * e per farlo
 * verranno utilizzati i Fragment, i quali mostreranno le varie informazioni
 * relative all'intervento,
 * al cliente dell'intervento e ai dettagli dell'intervento (qualora ce ne
 * fossero). */

@SuppressLint("NewApi")
public class ViewInterventoActivity extends BaseActivity {

    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.view_intervento);

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Activity.MODE_PRIVATE);

	String nominativo = prefs.getString("USER_NOMINATIVO", null);

	getSupportActionBar().setTitle(nominativo);

	Bundle extras = getIntent().getExtras();

	Intervento interv = null;
	Cliente cliente = null;

	if (extras != null) {
	    interv = (Intervento) extras.getSerializable("INTERVENTO");
	    cliente = (Cliente) extras.getSerializable("CLIENTE");
	}

	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();

	OverViewIntervento overView = new OverViewIntervento();

	Bundle bundle = new Bundle();
	bundle.putSerializable("INTERVENTO", interv);
	bundle.putString("NOMINATIVO", nominativo);
	bundle.putSerializable("CLIENTE", cliente);

	overView.setArguments(bundle);

	transaction.replace(R.id.fragments_layout, overView).commit();
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

	optionsMenu = menu;

	final MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.view_intervento, menu);

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {

	    case android.R.id.home:
		SharedPreferences prefs = getSharedPreferences(
			Constants.PREFERENCES, Context.MODE_PRIVATE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		    prefs.edit().clear().apply();
		} else {
		    final Editor editor = prefs.edit();
		    editor.clear();

		    new Thread(new Runnable() {

			@Override
			public void run() {

			    editor.commit();
			}
		    }).start();
		}

		this.finish();
		return true;
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();
    }
}
