package com.federicocolantoni.projects.interventix.task;

import java.net.SocketTimeoutException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;

public class GetLogin extends AsyncTask<String, Void, Integer> {
    
    private Context mContext;
    private ProgressDialog progress;
    
    private SharedPreferences defaultPrefs;
    
    private String mUsername, mPassword;
    
    public GetLogin(Activity activity, String username, String password) {
	
	mContext = activity;
	
	defaultPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
	
	this.mUsername = username;
	this.mPassword = password;
	
	progress = new ProgressDialog(mContext);
	progress.setIndeterminate(true);
	progress.setTitle(mContext.getString(R.string.login_started_title));
	progress.setIcon(R.drawable.ic_launcher);
	progress.setMessage(mContext.getString(R.string.login_started_message));
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
	
	final String prefs_url = mContext.getResources().getString(string.prefs_key_url);
	final String prefs_auto_login = mContext.getResources().getString(string.prefs_key_auto_login);
	
	final String url = defaultPrefs.getString(prefs_url, null);
	
	System.out.println("URL: " + url);
	
	// checks if auto login's flag is false
	// in this case, make a connection to the server to make login
	if (!defaultPrefs.getBoolean(prefs_auto_login, false)) {
	    
	    System.out.println("auto-login false");
	    
	    try {
		
		JSONObject response = new JSONObject(Utils.connectionForURL(strings[0], url).toJSONString());
		
		if (response.get("response").toString().equalsIgnoreCase("success")) {
		    
		    JSONObject data = response.getJSONObject("data");
		    
		    ContentResolver cr = mContext.getContentResolver();
		    ContentValues values;
		    
		    String selection = Fields.TYPE + " = ? AND " + UtenteDB.Fields.USERNAME + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    UtenteDB.UTENTE_ITEM_TYPE, mUsername
		    };
		    
		    Cursor cursor = cr.query(UtenteDB.CONTENT_URI, null, selection, selectionArgs, null);
		    
		    if (cursor.getCount() > 0) {
			
			// Update user's informations
			
			Utente updateUser = new Utente();
			updateUser.setCancellato(data.getBoolean("cancellato"));
			updateUser.setCestinato(data.getBoolean("cestinato"));
			updateUser.setCognome(data.getString("cognome"));
			updateUser.setEmail(data.getString("email"));
			updateUser.setNome(data.getString("nome"));
			updateUser.setRevisione(data.getLong("revisione"));
			updateUser.setTipo(data.getString("tipo"));
			
			values = Utente.updateSQL(updateUser);
			
			String selectionUpdate = UtenteDB.Fields.ID_UTENTE + " = ?";
			
			String[] selectionUpdateArgs = new String[] {
				"" + data.get("idutente")
			};
			
			cr.update(UtenteDB.CONTENT_URI, values, selectionUpdate, selectionUpdateArgs);
			
			SharedPreferences localPrefs = mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			
			Editor editor = localPrefs.edit();
			editor.putLong(Constants.USER_ID, data.getLong("idutente")).commit();
			
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
			
			Utente newUser = new Utente();
			newUser.setIdUtente(data.getLong("idutente"));
			newUser.setCancellato(data.getBoolean("cancellato"));
			newUser.setCestinato(data.getBoolean("cestinato"));
			newUser.setCognome(data.getString("cognome"));
			newUser.setEmail(data.getString("email"));
			newUser.setNome(data.getString("nome"));
			newUser.setUserName(data.getString("username"));
			newUser.setRevisione(data.getLong("revisione"));
			newUser.setTipo(data.getString("tipo"));
			
			values = Utente.insertSQL(newUser);
			
			cr.insert(UtenteDB.CONTENT_URI, values);
			
			SharedPreferences localPrefs = mContext.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			
			localPrefs.edit().putLong(Constants.USER_ID, data.getLong("idutente")).putString(Constants.USERNAME, mUsername).putString(Constants.PASSWORD, mPassword).commit();
			
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
	    
	    mContext.startActivity(new Intent(mContext, com.federicocolantoni.projects.interventix.core.HomeActivity_.class));
	}
	else
	    InterventixToast.makeToast(mContext, mContext.getString(R.string.toast_login_error), Toast.LENGTH_LONG);
    }
}
