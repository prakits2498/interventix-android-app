package com.federicocolantoni.projects.interventix.modules.login;

import java.net.SocketTimeoutException;
import java.util.HashMap;

import multiface.crypto.cr2.JsonCR2;

import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.core.HomeActivity;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.google.inject.Inject;

@SuppressLint("NewApi")
public class Login extends RoboFragment implements OnClickListener {
    
    @InjectView(R.id.field_password)
    EditText password;
    @InjectView(R.id.field_username)
    EditText username;
    
    @Inject
    ConnectivityManager connMgr;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	super.onCreateView(inflater, container, savedInstanceState);
	final View view = inflater.inflate(R.layout.login, container, false);
	
	view.findViewById(R.id.btn_login).setOnClickListener(this);
	
	return view;
    }
    
    @Override
    public void onClick(View v) {
	
	connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo info = connMgr.getActiveNetworkInfo();
	
	SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	if (username.getText().toString().length() == 0 || password.getText().toString().length() == 0)
	    InterventixToast.makeToast(getActivity(), getString(R.string.toast_username_and_password_required), Toast.LENGTH_SHORT);
	else {
	    
	    String json_req = new String();
	    
	    if (info != null)
		switch (info.getType()) {
		
		    case ConnectivityManager.TYPE_WIFI:
			
			if (info.isAvailable() && info.isConnected())
			    try {
				HashMap<String, String> parameters = new HashMap<String, String>();
				
				parameters.put("username", username.getText().toString());
				parameters.put("password", password.getText().toString());
				parameters.put("type", "TECNICO");
				
				json_req = JsonCR2.createRequest("users", "login", parameters, -1);
				
				new GetLogin(getActivity()).execute(json_req, username.getText().toString(), password.getText().toString());
			    }
			    catch (Exception e) {
				
				e.printStackTrace();
				BugSenseHandler.sendException(e);
			    }
			else {
			    
			    String usrnm = prefs.getString(Constants.USERNAME, null);
			    String psswrd = prefs.getString(Constants.PASSWORD, null);
			    
			    if (usrnm != null && psswrd != null)
				if (usrnm.equals(username.getText().toString()) && psswrd.equals(password.getText().toString())) {
				    
				    password.setText("");
				    
				    InterventixToast.makeToast(getActivity(), getString(R.string.toast_offline_access), Toast.LENGTH_LONG);
				    
				    startActivity(new Intent(getActivity(), HomeActivity.class));
				}
			}
			
			break;
		    
		    case ConnectivityManager.TYPE_MOBILE:
			
			if (info.isAvailable() && info.isConnected())
			    try {
				HashMap<String, String> parameters = new HashMap<String, String>();
				
				parameters.put("username", username.getText().toString());
				parameters.put("password", password.getText().toString());
				parameters.put("type", "TECNICO");
				
				json_req = JsonCR2.createRequest("users", "login", parameters, -1);
				
				new GetLogin(getActivity()).execute(json_req, username.getText().toString(), password.getText().toString());
			    }
			    catch (Exception e) {
				
				e.printStackTrace();
				BugSenseHandler.sendException(e);
			    }
			else {
			    
			    String usrnm = prefs.getString(Constants.USERNAME, null);
			    String psswrd = prefs.getString(Constants.PASSWORD, null);
			    
			    if (usrnm != null && psswrd != null)
				if (usrnm.equals(username.getText().toString()) && psswrd.equals(password.getText().toString())) {
				    
				    password.setText("");
				    
				    InterventixToast.makeToast(getActivity(), getString(R.string.toast_offline_access), Toast.LENGTH_LONG);
				    
				    startActivity(new Intent(getActivity(), HomeActivity.class));
				}
			}
			
			break;
		}
	    else {
		
		AlertDialog.Builder connUnavailable = new Builder(getActivity());
		
		connUnavailable.setTitle(getString(R.string.no_active_connection));
		connUnavailable.setMessage(R.string.conn_unavailable_text);
		connUnavailable.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
		    
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		    }
		});
		connUnavailable.setNeutralButton(R.string.enable_wifi, new DialogInterface.OnClickListener() {
		    
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			
			Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			dialog.dismiss();
		    }
		});
		connUnavailable.setPositiveButton(R.string.enable_mobile, new DialogInterface.OnClickListener() {
		    
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
			
			Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
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
    
    private class GetLogin extends AsyncTask<String, Void, Integer> {
	
	private final Context context;
	private final ProgressDialog progress;
	
	public GetLogin(Activity activity) {
	    
	    context = activity;
	    progress = new ProgressDialog(context);
	    progress.setIndeterminate(true);
	    progress.setTitle(getString(R.string.login_started_title));
	    progress.setIcon(R.drawable.ic_launcher);
	    progress.setMessage(getString(R.string.login_started_message));
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
	    
	    final SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
	    
	    final String prefs_url = getResources().getString(string.prefs_key_url);
	    final String prefs_auto_login = getResources().getString(string.prefs_key_auto_login);
	    
	    final String url = defaultPrefs.getString(prefs_url, null);
	    
	    System.out.println("URL: " + url);
	    
	    // checks if auto login's flag is false
	    // in this case, make a connection to the server to make login
	    if (!defaultPrefs.getBoolean(prefs_auto_login, false)) {
		
		System.out.println("auto-login false");
		
		try {
		    
		    JSONObject response = new JSONObject(Utils.connectionForURL(strings[0], url).toJSONString());
		    // JSONObject jsonResponse =
		    // Utils.connectionForURL(strings[0], url);
		    
		    if (response.get("response").toString().equalsIgnoreCase("success")) {
			
			JSONObject data = response.getJSONObject("data");
			
			ContentResolver cr = getActivity().getContentResolver();
			ContentValues values;
			
			String selection = Fields.TYPE + " = ? AND " + UtenteDB.Fields.USERNAME + " = ?";
			
			String[] selectionArgs = new String[] {
				UtenteDB.UTENTE_ITEM_TYPE, strings[1]
			};
			
			Cursor cursor = cr.query(UtenteDB.CONTENT_URI, null, selection, selectionArgs, null);
			
			if (cursor.getCount() > 0) {
			    
			    // Update user's informations
			    
			    values = new ContentValues();
			    values.put(UtenteDB.Fields.NOME, data.getString("nome"));
			    values.put(UtenteDB.Fields.COGNOME, data.getString("cognome"));
			    values.put(UtenteDB.Fields.USERNAME, data.getString("username"));
			    values.put(UtenteDB.Fields.CANCELLATO, data.getBoolean("cancellato"));
			    values.put(UtenteDB.Fields.REVISIONE, data.getLong("revisione"));
			    values.put(UtenteDB.Fields.EMAIL, data.getString("email"));
			    values.put(UtenteDB.Fields.TIPO, data.getString("tipo"));
			    values.put(UtenteDB.Fields.CESTINATO, data.getBoolean("cestinato"));
			    
			    String selectionUpdate = UtenteDB.Fields.ID_UTENTE + " = ?";
			    
			    String[] selectionUpdateArgs = new String[] {
				    "" + data.get("idutente")
			    };
			    
			    cr.update(UtenteDB.CONTENT_URI, values, selectionUpdate, selectionUpdateArgs);
			    
			    SharedPreferences localPrefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			    
			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
				localPrefs.edit().putLong(Constants.USER_ID, data.getLong("idutente")).apply();
			    else {
				Editor editor = localPrefs.edit();
				editor.putLong(Constants.USER_ID, data.getLong("idutente")).commit();
			    }
			    
			    System.out.println("UPDATE USER DONE");
			    
			    result = Activity.RESULT_OK;
			    
			    progress.dismiss();
			    
			    if (!cursor.isClosed())
				cursor.close();
			    else
				System.out.println("Cursor for GetLogin - UPDATE MODE is closed");
			}
			else {
			    
			    // Insert user's informations
			    
			    values = new ContentValues();
			    values.put(UtenteDB.Fields.ID_UTENTE, data.getLong("idutente"));
			    values.put(Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
			    values.put(UtenteDB.Fields.NOME, data.getString("nome"));
			    values.put(UtenteDB.Fields.COGNOME, data.getString("cognome"));
			    values.put(UtenteDB.Fields.USERNAME, data.getString("username"));
			    values.put(UtenteDB.Fields.CANCELLATO, data.getBoolean("cancellato"));
			    values.put(UtenteDB.Fields.REVISIONE, data.getLong("revisione"));
			    values.put(UtenteDB.Fields.EMAIL, data.getString("email"));
			    values.put(UtenteDB.Fields.TIPO, data.getString("tipo"));
			    values.put(UtenteDB.Fields.CESTINATO, data.getBoolean("cestinato"));
			    
			    cr.insert(UtenteDB.CONTENT_URI, values);
			    
			    SharedPreferences localPrefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			    
			    EditText username = (EditText) getActivity().findViewById(R.id.field_username);
			    EditText password = (EditText) getActivity().findViewById(R.id.field_password);
			    
			    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
				localPrefs.edit().putLong(Constants.USER_ID, data.getLong("idutente")).putString(Constants.USERNAME, username.getText().toString()).putString(Constants.PASSWORD, password.getText().toString()).apply();
			    else
				localPrefs.edit().putLong(Constants.USER_ID, data.getLong("idutente")).putString(Constants.USERNAME, username.getText().toString()).putString(Constants.PASSWORD, password.getText().toString()).commit();
			    
			    System.out.println("INSERT USER DONE");
			    
			    result = Activity.RESULT_OK;
			    
			    progress.dismiss();
			    
			    if (!cursor.isClosed())
				cursor.close();
			    else
				System.out.println("Cursor for GetLogin - INSERT MODE is closed");
			    
			}
		    }
		    else {
			result = Activity.RESULT_CANCELED;
			
			progress.dismiss();
		    }
		}
		catch (SocketTimeoutException e) {
		    
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		catch (Exception e) {
		    
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		finally {
		    progress.dismiss();
		}
	    }
	    
	    return result;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
	    
	    if (result == Activity.RESULT_OK) {
		
		EditText password = (EditText) getActivity().findViewById(R.id.field_password);
		
		password.setText("");
		
		startActivity(new Intent(context, HomeActivity.class));
	    }
	    else
		InterventixToast.makeToast(context, getString(R.string.toast_login_error), Toast.LENGTH_LONG);
	}
    }
}
