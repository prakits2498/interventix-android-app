package com.federicocolantoni.projects.interventix.core;

import java.io.IOException;
import java.net.SocketTimeoutException;
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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.id;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.modules.login.Login.OnLoginListener;
import com.federicocolantoni.projects.interventix.settings.SettingActivity;
import com.federicocolantoni.projects.interventix.settings.SettingSupportActivity;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity implements OnLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	BugSenseHandler.initAndStartSession(MainActivity.this,
		Constants.API_KEY);

	setContentView(R.layout.activity_main);

	SharedPreferences prefs = null;

	ReadDefaultPreferences readPref = new ReadDefaultPreferences(
		MainActivity.this);
	readPref.execute();

	try {
	    prefs = readPref.get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
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
		    dialog.show(getSupportFragmentManager(),
			    Constants.FIRST_RUN_DIALOG_FRAGMENT);
		}
	    }
	}

	findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		try {
		    onLogin();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} catch (IOException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
	    }
	});
    }

    @Override
    public void onLogin() throws InterruptedException, IOException {

	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo info = connMgr.getActiveNetworkInfo();

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	EditText username = (EditText) findViewById(R.id.field_username);
	EditText password = (EditText) findViewById(R.id.field_password);

	if (username.getText().toString().length() == 0
		|| password.getText().toString().length() == 0) {
	    InterventixToast.makeToast(this,
		    "Devi riempire i campi \"Username\" e \"Password\"",
		    Toast.LENGTH_SHORT);
	} else {

	    String json_req = new String();

	    if (info != null) {
		switch (info.getType()) {

		case ConnectivityManager.TYPE_WIFI:

		    if (info.isAvailable() && info.isConnected()) {
			try {
			    HashMap<String, String> parameters = new HashMap<String, String>();

			    parameters.put("username", username.getText()
				    .toString());
			    parameters.put("password", password.getText()
				    .toString());
			    parameters.put("type", "TECNICO");

			    json_req = JsonCR2.createRequest("users", "login",
				    parameters, -1);

			    new GetLogin(MainActivity.this).execute(json_req,
				    username.getText().toString(), password
					    .getText().toString());
			} catch (Exception e) {
			    e.printStackTrace();
			    BugSenseHandler.sendException(e);
			}
		    } else {

			String usrnm = prefs
				.getString(Constants.USERNAME, null);
			String psswrd = prefs.getString(Constants.PASSWORD,
				null);

			if (usrnm != null && psswrd != null) {
			    if (usrnm.equals(username.getText().toString())
				    && psswrd.equals(password.getText()
					    .toString())) {

				password.setText("");

				InterventixToast.makeToast(this,
					"Accesso in modalità offline",
					Toast.LENGTH_LONG);

				startActivity(new Intent(MainActivity.this,
					HomeActivity.class));
			    }
			}
		    }

		    break;

		case ConnectivityManager.TYPE_MOBILE:

		    if (info.isAvailable() && info.isConnected()) {
			try {
			    HashMap<String, String> parameters = new HashMap<String, String>();

			    parameters.put("username", username.getText()
				    .toString());
			    parameters.put("password", password.getText()
				    .toString());
			    parameters.put("type", "TECNICO");

			    json_req = JsonCR2.createRequest("users", "login",
				    parameters, -1);

			    new GetLogin(MainActivity.this).execute(json_req,
				    username.getText().toString(), password
					    .getText().toString());
			} catch (Exception e) {
			    e.printStackTrace();
			    BugSenseHandler.sendException(e);
			}
		    } else {

			String usrnm = prefs
				.getString(Constants.USERNAME, null);
			String psswrd = prefs.getString(Constants.PASSWORD,
				null);

			if (usrnm != null && psswrd != null) {
			    if (usrnm.equals(username.getText().toString())
				    && psswrd.equals(password.getText()
					    .toString())) {

				password.setText("");

				InterventixToast.makeToast(this,
					"Accesso in modalità offline",
					Toast.LENGTH_LONG);

				startActivity(new Intent(MainActivity.this,
					HomeActivity.class));
			    }
			}
		    }

		    break;
		}
	    } else {

		AlertDialog.Builder connUnavailable = new Builder(this);

		connUnavailable.setTitle("Nessuna connessione attiva");
		connUnavailable.setMessage(R.string.conn_unavailable_text);
		connUnavailable.setNegativeButton(
			getString(R.string.btn_cancel),
			new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				dialog.dismiss();
			    }
			});
		connUnavailable.setNeutralButton(R.string.enable_wifi,
			new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {

				Intent intent = new Intent(
					android.provider.Settings.ACTION_WIFI_SETTINGS);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				dialog.dismiss();
			    }
			});
		connUnavailable.setPositiveButton(R.string.enable_mobile,
			new DialogInterface.OnClickListener() {

			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {

				Intent intent = new Intent(
					android.provider.Settings.ACTION_WIRELESS_SETTINGS);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				dialog.dismiss();
			    }
			});
		connUnavailable.setIcon(R.drawable.ic_launcher);

		connUnavailable.create().show();
	    }
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
	    if (super.onOptionsItemSelected(item))
		return true;
	    else
		return false;
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
				getResources()
					.getString(R.string.prefs_key_url),
				"http://" + input_url.getText().toString()
					+ "/Interventix/connector").apply();
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

		    dismiss();
		}
		break;
	    }
	}
    }

    private class GetLogin extends AsyncTask<String, Void, Integer> {

	private final Context context;
	private final ProgressDialog progress;

	public GetLogin(Activity activity) {

	    context = activity;
	    progress = new ProgressDialog(context);
	    progress.setIndeterminate(true);
	    progress.setTitle("Login in corso");
	    progress.setMessage("Attendere prego...");
	    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	    progress.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {

	    progress.show();
	}

	@Override
	protected Integer doInBackground(String... strings) {

	    int result = 0;

	    final SharedPreferences defaultPrefs = PreferenceManager
		    .getDefaultSharedPreferences(context
			    .getApplicationContext());

	    final String prefs_url = getResources().getString(
		    string.prefs_key_url);
	    final String prefs_auto_login = getResources().getString(
		    string.prefs_key_auto_login);

	    final String url = defaultPrefs.getString(prefs_url, null);

	    System.out.println("URL: " + url);

	    // checks if auto login's flag is false
	    // in this case, make a connection to the server to make login
	    if (!defaultPrefs.getBoolean(prefs_auto_login, false)) {

		System.out.println("auto-login false");

		try {

		    JSONObject jsonResponse = Utils.connectionForURL(
			    strings[0], url);

		    if (jsonResponse.get("response").toString()
			    .equalsIgnoreCase("success")) {

			JSONObject data = (JSONObject) jsonResponse.get("data");

			ContentResolver cr = getContentResolver();
			ContentValues values;

			String selection = Fields.TYPE + " = ? AND "
				+ UtenteDB.Fields.USERNAME + " = ?";

			String[] selectionArgs = new String[] {
				UtenteDB.UTENTE_ITEM_TYPE, strings[1] };

			Cursor cursor = cr.query(UtenteDB.CONTENT_URI, null,
				selection, selectionArgs, null);

			if (cursor.getCount() > 0) {

			    // Update user's informations

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
				    + " = ?";

			    String[] selectionUpdateArgs = new String[] { ""
				    + data.get("idutente") };

			    cr.update(UtenteDB.CONTENT_URI, values,
				    selectionUpdate, selectionUpdateArgs);

			    SharedPreferences localPrefs = getSharedPreferences(
				    Constants.PREFERENCES, Context.MODE_PRIVATE);

			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				localPrefs
					.edit()
					.putLong(Constants.USER_ID,
						(Long) data.get("idutente"))
					.apply();
			    } else {
				Editor editor = localPrefs.edit();
				editor.putLong(Constants.USER_ID,
					(Long) data.get("idutente")).commit();
			    }

			    System.out.println("UPDATE USER DONE");

			    result = Activity.RESULT_OK;

			    progress.dismiss();

			    if (!cursor.isClosed()) {
				cursor.close();
			    } else {
				System.out
					.println("Cursor for GetLogin - UPDATE MODE is closed");
			    }
			} else {

			    // Insert user's informations

			    values = new ContentValues();
			    values.put(UtenteDB.Fields.ID_UTENTE,
				    (Long) data.get("idutente"));
			    values.put(Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
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

			    SharedPreferences localPrefs = getSharedPreferences(
				    Constants.PREFERENCES, Context.MODE_PRIVATE);

			    EditText username = (EditText) findViewById(R.id.field_username);
			    EditText password = (EditText) findViewById(R.id.field_password);

			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				localPrefs
					.edit()
					.putLong(Constants.USER_ID,
						(Long) data.get("idutente"))
					.putString(Constants.USERNAME,
						username.getText().toString())
					.putString(Constants.PASSWORD,
						password.getText().toString())
					.apply();
			    } else {
				localPrefs
					.edit()
					.putLong(Constants.USER_ID,
						(Long) data.get("idutente"))
					.putString(Constants.USERNAME,
						username.getText().toString())
					.putString(Constants.PASSWORD,
						password.getText().toString())
					.commit();
			    }

			    System.out.println("INSERT USER DONE");

			    result = Activity.RESULT_OK;

			    progress.dismiss();

			    if (!cursor.isClosed()) {
				cursor.close();
			    } else {
				System.out
					.println("Cursor for GetLogin - INSERT MODE is closed");
			    }

			}
		    } else {
			result = Activity.RESULT_CANCELED;

			progress.dismiss();
		    }
		} catch (ParseException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} catch (SocketTimeoutException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} finally {
		    progress.dismiss();
		}
	    }

	    // in this case, auto login's flag is true
	    // check user's info directly on the DB
	    else {

		System.out.println("auto-login true");

		/*
		 * TODO make login checking that the username and password given
		 * by user are already on the DB: - if presents, make login; -
		 * otherwise, allow the user to choose to connect to the server.
		 */

	    }

	    return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

	    if (result == Activity.RESULT_OK) {

		EditText password = (EditText) findViewById(R.id.field_password);

		password.setText("");

		startActivity(new Intent(context, HomeActivity.class));
	    } else {

		InterventixToast.makeToast(context,
			"Si è verificato un errore nel login.\n"
				+ Constants.ACCESS_DINIED, Toast.LENGTH_SHORT);
	    }
	}
    }
}
