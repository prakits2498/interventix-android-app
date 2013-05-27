
package com.federicocolantoni.projects.interventix.core;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.DBContract.UtenteDB;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.activity_home);

	setNominativo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	super.onCreateOptionsMenu(menu);

	final MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.activity_index, menu);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	if (item.getItemId() == android.R.id.home) {
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

    private void setNominativo() {

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	new ManagedAsyncTask<Long, Void, String>(HomeActivity.this) {

	    @Override
	    protected String doInBackground(Long... params) {

		String res = null;

		ContentResolver cr = getContentResolver();

		String[] projection = new String[] { UtenteDB.Fields.NOME,
			UtenteDB.Fields.COGNOME };

		String selection = UtenteDB.Fields.TYPE + "='"
			+ UtenteDB.UTENTE_ITEM_TYPE + "' AND "
			+ UtenteDB.Fields.ID_UTENTE + "=" + params[0];

		Cursor cursor = cr.query(UtenteDB.CONTENT_URI, projection,
			selection, null, null);

		if (cursor.getCount() == 1) {
		    cursor.moveToFirst();

		    res = cursor.getString(cursor
			    .getColumnIndex(UtenteDB.Fields.NOME))
			    + " "
			    + cursor.getString(cursor
				    .getColumnIndex(UtenteDB.Fields.COGNOME));
		}

		cursor.close();

		return res;
	    }

	    @Override
	    protected void onPostExecute(String result) {

		TextView nominativo = (TextView) ((HomeActivity) getActivity())
			.findViewById(R.id.label_nominativo);
		nominativo.setText("Interventi (" + result + ")");
	    };
	}.execute(prefs.getLong(Constants.USER_ID, 0l));
    }
}
