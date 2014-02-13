package com.federicocolantoni.projects.interventix.task;

import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.UtenteController;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.utils.CheckConnection;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.PasswordHash;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@SuppressLint("NewApi")
public class GetLogin extends AsyncTask<String, Void, Integer> {

	private Context context;
	private ProgressDialog progress;

	private SharedPreferences defaultPrefs;

	private String username, password;

	private AccountManager accountManager;

	public GetLogin(Activity activity, String username, String password) {

		context = activity;

		defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context);

		this.username = username;
		this.password = password;

		accountManager = AccountManager.get(context);

		progress = new ProgressDialog(context);
		progress.setIndeterminate(true);
		progress.setTitle(context.getString(R.string.login_started_title));
		progress.setIcon(R.drawable.ic_launcher);
		progress.setMessage(context.getString(R.string.login_started_message));
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

		final String prefs_url = context.getResources().getString(R.string.prefs_key_url);

		final String url = defaultPrefs.getString(prefs_url, null);

		try {

			if (CheckConnection.connectionIsAlive()) {
				JSONObject response = new JSONObject(Utils.connectionForURL(strings[0], url).toJSONString());

				if (response.get("response").toString().equalsIgnoreCase("success")) {

					JSONObject data = response.getJSONObject("data");

					Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

					Utente utente = gson.fromJson(data.toString(), Utente.class);

					RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

					utenteDao.createIfNotExists(utente);

					utenteDao.update(utente);

					UtenteController.tecnicoLoggato = utente;

					result = Activity.RESULT_OK;

					com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
				} else {
					result = Constants.ERRORE_LOGIN;

					progress.dismiss();
				}
			} else {
				result = Constants.ERRORE_NO_CONNECTION;

				progress.dismiss();
			}
		} catch (SocketTimeoutException e) {

			e.printStackTrace();
			BugSenseHandler.sendException(e);
		} catch (Exception e) {

			e.printStackTrace();
			BugSenseHandler.sendException(e);
		} finally {
			progress.dismiss();
		}

		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {

		if (result == Activity.RESULT_OK) {

			String encryptedPassword = null;

			try {
				encryptedPassword = PasswordHash.createHash(password);
			} catch (NoSuchAlgorithmException e) {

				e.printStackTrace();
			} catch (InvalidKeySpecException e) {

				e.printStackTrace();
			}

			Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

			if (accounts.length == 0) {

				final Account account = new Account(username, Constants.ACCOUNT_TYPE_INTERVENTIX);

				accountManager.addAccountExplicitly(account, encryptedPassword, null);
				accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

				SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

				final Editor edit = prefs.edit().putString(Constants.USERNAME, username).putString(Constants.PASSWORD, encryptedPassword);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
					edit.apply();
				else {

					new Thread(new Runnable() {
						public void run() {

							edit.commit();
						}
					}).start();
				}
			} else {

				for (Account account : accounts) {

					if (account.name.equals(username)) {

						accountManager.setPassword(account, encryptedPassword);
						accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

						RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

						UtenteController.tecnicoLoggato = utenteDao.queryForEq("username", username).get(0);

						com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

						break;
					}
				}
			}

			context.startActivity(new Intent(context, com.federicocolantoni.projects.interventix.ui.activity.HomeActivity_.class));
		} else if (result == Constants.ERRORE_LOGIN)
			InterventixToast.makeToast(context.getString(R.string.toast_login_error), Toast.LENGTH_LONG);
		else if (result == Constants.ERRORE_NO_CONNECTION)
			InterventixToast.makeToast(context.getString(R.string.toast_no_connection_available), Toast.LENGTH_LONG);
	}
}
