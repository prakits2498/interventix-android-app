
package com.federicocolantoni.projects.interventix.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.id;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.data.DBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.modules.login.Login.OnLoginListener;
import com.federicocolantoni.projects.interventix.settings.SettingActivity;
import com.federicocolantoni.projects.interventix.settings.SettingSupportActivity;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	/*
	 * TODO insert code to control if URL connection's preference is empty or is not empty.
	 * If is empty, then it'll open a popup that allows the user to insert a valid URL connection;
	 * this popup never dismiss util the URL connection will be empty.
	 * Otherwise, the popup will not be shown.
	 */

	SharedPreferences prefs = null;

	ReadDefaultPreferences readPref = new ReadDefaultPreferences(
		MainActivity.this);
	readPref.execute();

	try {
	    prefs = readPref.get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	    if (prefs.getString(
		    getResources().getString(R.string.prefs_key_url), "")
		    .isEmpty()) {

		FirstRunDialog dialog = new FirstRunDialog();
		dialog.show(getSupportFragmentManager(), "first_run");
	    } else {
		if (prefs.getString(
			getResources().getString(R.string.prefs_key_url), "")
			.length() == 0) {

		    FirstRunDialog dialog = new FirstRunDialog();
		    dialog.show(getSupportFragmentManager(), "first_run");
		}
	    }
	}

	//	Cursor cursor = getContentResolver().query(UtenteDB.CONTENT_URI, null,
	//		UtenteDB.Fields.TYPE + "='" + UtenteDB.UTENTE_ITEM_TYPE + "'",
	//		null, null);
	//
	//	while (cursor.moveToNext()) {
	//	    System.out.println("*************");
	//	    System.out.println("USERNAME: "
	//		    + cursor.getString(cursor
	//			    .getColumnIndex(UtenteDB.Fields.USERNAME)));
	//	    System.out.println("NOME: "
	//		    + cursor.getString(cursor
	//			    .getColumnIndex(UtenteDB.Fields.NOME)));
	//	    System.out.println("COGNOME: "
	//		    + cursor.getString(cursor
	//			    .getColumnIndex(UtenteDB.Fields.COGNOME)));
	//	    System.out.println("*************");
	//	}

	findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		try {
		    onLogin();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	});

    }

    @Override
    public void onLogin() throws InterruptedException, IOException {

	EditText mUsername = (EditText) findViewById(R.id.field_username);
	EditText mPassword = (EditText) findViewById(R.id.field_password);

	String json_req = new String();

	try {
	    HashMap<String, String> parameters = new HashMap<String, String>();

	    parameters.put("username", mUsername.getText().toString());
	    parameters.put("password", mPassword.getText().toString());
	    parameters.put("type", "TECNICO");

	    json_req = JsonCR2.createRequest("users", "login", parameters, -1);

	    //	    System.out.println("REQUEST: \n" + json_req);

	    new GetLogin(MainActivity.this).execute(json_req, mUsername
		    .getText().toString(), mPassword.getText().toString());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	super.onCreateOptionsMenu(menu);

	final MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.activity_main, menu);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	super.onOptionsItemSelected(item);

	switch (item.getItemId()) {
	    case id.menu_options:

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
		    startActivity(new Intent(this, SettingActivity.class));
		} else {
		    startActivity(new Intent(this, SettingSupportActivity.class));
		}
		return true;
	    default:
		if (super.onOptionsItemSelected(item)) {
		    return true;
		} else {
		    return false;
		}
	}
    }

    public static class FirstRunDialog extends SherlockDialogFragment implements
	    OnClickListener {

	private EditText input_url;
	private Button save_url;

	public FirstRunDialog() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder first_run_dialog = new Builder(getActivity());

	    first_run_dialog.setTitle(R.string.welcome_title);
	    first_run_dialog.setMessage(R.string.welcome_message);

	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View view = inflater.inflate(R.layout.first_run, null);

	    first_run_dialog.setView(view);

	    input_url = (EditText) view.findViewById(R.id.input_first_run);
	    save_url = (Button) view.findViewById(R.id.save_prefs_url);
	    save_url.setOnClickListener(this);

	    return first_run_dialog.create();
	}

	@Override
	public void onClick(View v) {

	    switch (v.getId()) {
		case R.id.save_prefs_url:

		    if (input_url.getText().toString().length() != 0) {
			SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getSherlockActivity());
			final Editor editor = prefs.edit();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.putString(
				    getResources().getString(
					    R.string.prefs_key_url),
				    input_url.getText().toString()).apply();
			} else {
			    new Thread(new Runnable() {

				@Override
				public void run() {

				    editor.putString(
					    getResources().getString(
						    R.string.prefs_key_url),
					    input_url.getText().toString())
					    .commit();
				}
			    }).start();
			}

			this.dismiss();
		    }
		    break;
	    }
	}
    }

    private class GetLogin extends AsyncTask<String, Void, Integer> {

	private Context context;
	private ProgressDialog progress;

	public GetLogin(Activity activity) {

	    context = activity;
	    progress = new ProgressDialog(context);
	    progress.setIndeterminate(true);
	    progress.setTitle("Login in corso");
	    progress.setMessage("Attendere prego...");
	    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	@Override
	protected void onPreExecute() {

	    progress.show();
	}

	@Override
	protected Integer doInBackground(String... strings) {

	    int result = 0;

	    final SharedPreferences prefs = PreferenceManager
		    .getDefaultSharedPreferences(context);

	    final String prefs_url = getResources().getString(
		    string.prefs_key_url);
	    final String prefs_auto_login = getResources().getString(
		    string.prefs_key_auto_login);

	    final String url = prefs.getString(prefs_url, null);

	    // checks if auto login's flag is false
	    // in this case, make a connection to the server to make login
	    if (!prefs.getBoolean(prefs_auto_login, false)) {

		System.out.println("auto-login false");

		final AndroidHttpClient request = new AndroidHttpClient(url);
		request.setMaxRetries(5);

		ParameterMap paramMap = new ParameterMap();
		paramMap.add("DATA", strings[0]);

		HttpResponse response;
		response = request.post("", paramMap);

		try {
		    JSONObject jsonResponse = JsonCR2.read(response
			    .getBodyAsString());

		    if (jsonResponse.get("response").toString()
			    .equalsIgnoreCase("success")) {

			JSONObject data = (JSONObject) jsonResponse.get("data");

			ContentResolver cr = getContentResolver();
			ContentValues values;

			String selection = UtenteDB.Fields.TYPE + "='"
				+ UtenteDB.UTENTE_ITEM_TYPE + "' AND "
				+ UtenteDB.Fields.USERNAME + "='" + strings[1]
				+ "'";

			Cursor cursor = cr.query(UtenteDB.CONTENT_URI, null,
				selection, null, null);

			if (cursor.getCount() > 0) {

			    //Update user's informations

			    values = new ContentValues();
			    values.put(UtenteDB.Fields.NOME,
				    (String) data.get("nome"));
			    values.put(UtenteDB.Fields.COGNOME,
				    (String) data.get("cognome"));
			    values.put(UtenteDB.Fields.USERNAME,
				    (String) data.get("username"));
			    values.put(UtenteDB.Fields.CANCELLATO,
				    (Boolean) data.get("cancellato"));
			    values.put(UtenteDB.Fields.REVISIONE,
				    (Long) data.get("revisione"));
			    values.put(UtenteDB.Fields.EMAIL,
				    (String) data.get("email"));
			    values.put(UtenteDB.Fields.TIPO,
				    (String) data.get("tipo"));
			    values.put(UtenteDB.Fields.CESTINATO,
				    (Boolean) data.get("cestinato"));

			    String selectionUpdate = UtenteDB.Fields.ID_UTENTE
				    + "='" + data.get("idutente") + "'";

			    cr.update(UtenteDB.CONTENT_URI, values,
				    selectionUpdate, null);

			    System.out.println("UPDATE USER DONE");

			    result = Activity.RESULT_OK;
			} else {

			    //Insert user's informations

			    values = new ContentValues();
			    values.put(UtenteDB.Fields.ID_UTENTE,
				    (Long) data.get("idutente"));
			    values.put(UtenteDB.Fields.TYPE,
				    UtenteDB.UTENTE_ITEM_TYPE);
			    values.put(UtenteDB.Fields.NOME,
				    (String) data.get("nome"));
			    values.put(UtenteDB.Fields.COGNOME,
				    (String) data.get("cognome"));
			    values.put(UtenteDB.Fields.USERNAME,
				    (String) data.get("username"));
			    values.put(UtenteDB.Fields.CANCELLATO,
				    (Boolean) data.get("cancellato"));
			    values.put(UtenteDB.Fields.REVISIONE,
				    (Long) data.get("revisione"));
			    values.put(UtenteDB.Fields.EMAIL,
				    (String) data.get("email"));
			    values.put(UtenteDB.Fields.TIPO,
				    (String) data.get("tipo"));
			    values.put(UtenteDB.Fields.CESTINATO,
				    (Boolean) data.get("cestinato"));

			    cr.insert(UtenteDB.CONTENT_URI, values);

			    System.out.println("INSERT USER DONE");

			    result = Activity.RESULT_OK;
			}

			cursor.close();
		    } else {
			result = Activity.RESULT_CANCELED;
		    }
		} catch (ParseException e) {
		    e.printStackTrace();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }

	    // in this case, auto login's flag is true
	    // check user's info directly on the DB
	    else {

		System.out.println("auto-login true");

		/*
		 * TODO make login checking that the username and password given by user
		 * are already on the DB:
		 * - if presents, make login;
		 * - otherwise, allow the user to choose to connect to the server.
		 */

		String username = strings[1];
		String password = strings[2];

		ContentResolver cr = getContentResolver();

		String[] projection = new String[] { "count(*)" };
		String selection = UtenteDB.Fields.USERNAME + "='" + username
			+ "' AND " + UtenteDB.Fields.PASSWORD + "='" + password
			+ "'";

		Cursor cursor = cr.query(UtenteDB.CONTENT_URI, projection,
			selection, null, null);

		if (cursor != null) {
		    if (!cursor.moveToFirst()) {
			result = Activity.RESULT_CANCELED;
		    } else {
			result = Activity.RESULT_OK;
		    }

		    cursor.close();
		}
	    }

	    return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

	    if (result == Activity.RESULT_OK) {
		progress.dismiss();
		Toast.makeText(context, Constants.ACCESS_ALLOWED,
			Toast.LENGTH_LONG).show();

		startActivity(new Intent(context, ControlPanelActivity.class));
	    } else {
		progress.dismiss();
		Toast.makeText(
			context,
			"Si e' verificato un errore nel login.\n"
				+ Constants.ACCESS_DINIED, Toast.LENGTH_LONG)
			.show();
	    }
	}
    }
}
