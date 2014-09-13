package com.federicocolantoni.projects.interventix.fragments;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.CheckConnection;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.InterventixToast;
import com.federicocolantoni.projects.interventix.helpers.PasswordHash;
import com.federicocolantoni.projects.interventix.models.Utente;
import com.federicocolantoni.projects.interventix.models.UtenteController;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_login)
public class LoginFragment extends Fragment implements TextWatcher {

    @ViewById(R.id.field_password)
    EditText password;

    @ViewById(R.id.field_username)
    EditText username;

    @ViewById(R.id.btn_login)
    Button btnLogin;

    @StringRes(R.string.service_not_available)
    String serviceNotAvailable;

    @StringRes(R.string.prefs_key_url)
    String prefsUrl;

    @StringRes(R.string.formatted_url_string)
    String formattedURL;

    @StringRes(R.string.not_offline_access_title)
    String notOfflineAccessTitle;

    @StringRes(R.string.not_offline_access_message)
    String notOfflineAccessMessage;

    @StringRes(R.string.ok_btn)
    String btnOk;

    @StringRes(R.string.toast_login_error)
    String toastLoginError;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    RuntimeExceptionDao<Utente, Long> utenteDao;

    private SharedPreferences defaultPrefs;

    private AccountManager accountManager;

    private Menu optionsMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	defaultPrefs = PreferenceManager.getDefaultSharedPreferences(Interventix.getContext());
    }

    @Override
    public void onStart() {

	super.onStart();

	setHasOptionsMenu(true);

	username.addTextChangedListener(this);
	password.addTextChangedListener(this);
	btnLogin.setEnabled(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	inflater.inflate(R.menu.menu_main, menu);
	optionsMenu = menu;
    }

    private void setRefreshActionButtonState(final boolean refreshing) {

	if (optionsMenu != null) {

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		final MenuItem refreshItem = optionsMenu.findItem(R.id.menu_progress);

		if (refreshItem != null)
		    if (refreshing) {
			refreshItem.setVisible(refreshing);
			refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		    }
		    else {
			refreshItem.setVisible(refreshing);
			refreshItem.setActionView(null);
		    }
	    }
	    else {
		final MenuItemImpl refreshItem = (MenuItemImpl) optionsMenu.findItem(R.id.menu_progress);

		if (refreshItem != null) {
		    if (refreshing) {
			refreshItem.setVisible(refreshing);
			refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		    }
		    else {
			refreshItem.setVisible(refreshing);
			refreshItem.setActionView(null);
		    }
		}
	    }
	}
    }

    @Click(R.id.btn_login)
    void startLogin() {

	setRefreshActionButtonState(true);

	String jsonReq;

	if (CheckConnection.connectionIsAlive()) {

	    try {
		HashMap<String, String> parameters = new HashMap<String, String>();

		parameters.put(Constants.JSON_USERNAME, username.getText().toString());
		parameters.put(Constants.JSON_PASSWORD, password.getText().toString());
		parameters.put(Constants.JSON_TYPE, Constants.USER_TYPE.TECNICO.name());

		final String url = defaultPrefs.getString(prefsUrl, "");

		jsonReq = JsonCR2.createRequest(Constants.JSON_USER_SECTION, Constants.JSON_LOGIN_ACTION, parameters, -1);

		Ion.with(getActivity()).load(String.format(formattedURL, url)).setBodyParameter(Constants.DATA_POST_PARAMETER, jsonReq).asString().setCallback(new FutureCallback<String>() {

		    @Override
		    public void onCompleted(Exception exception, String response) {

			if (response != null) {
			    try {

				final JSONObject jsonResp = new JSONObject(JsonCR2.read(response.trim()).toJSONString());

				System.out.println(jsonResp.toString(2));

				parseResponse(jsonResp);

			    }
			    catch (ParseException e) {

				BugSenseHandler.sendException(e);
				e.printStackTrace();
			    }
			    catch (Exception e) {

				BugSenseHandler.sendException(e);
				e.printStackTrace();
			    }
			    finally {

				setRefreshActionButtonState(false);
			    }
			}
			else {
			    setRefreshActionButtonState(false);

			    InterventixToast.makeToast(serviceNotAvailable, Toast.LENGTH_LONG);
			}
		    }
		});
	    }
	    catch (Exception e) {

		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	}
	else {

	    // connessione in modalità offline

	    accountManager = AccountManager.get(Interventix.getContext());

	    assert accountManager != null;
	    Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

	    if (accounts.length == 0) {

		AlertDialog.Builder builder = new Builder(getActivity());

		builder.setIcon(R.drawable.ic_launcher);

		builder.setTitle(notOfflineAccessTitle);
		builder.setMessage(notOfflineAccessMessage).setPositiveButton(btnOk, new Dialog.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialogInterface, int i) {

			dialogInterface.dismiss();
		    }
		});

		builder.create().show();

		setRefreshActionButtonState(false);
	    }
	    else {

		for (Account account : accounts) {

		    if (account.name.equals(username.getText().toString())) {

			String encryptedPassword = null;

			try {
			    encryptedPassword = PasswordHash.createHash(password.getText().toString());
			}
			catch (NoSuchAlgorithmException e) {

			    e.printStackTrace();
			}
			catch (InvalidKeySpecException e) {

			    e.printStackTrace();
			}

			accountManager.setPassword(account, encryptedPassword);
			accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

			new AsyncTask<String, Void, Utente>() {

			    @Override
			    protected Utente doInBackground(String... params) {

				return utenteDao.queryForEq(Constants.ORMLITE_USERNAME, params[0]).get(0);
			    }

			    @Override
			    protected void onPostExecute(Utente utente) {

				UtenteController.tecnicoLoggato = utente;
			    }
			}.execute(username.getText().toString());

			break;
		    }
		}

		setRefreshActionButtonState(false);

		Intent intent = new Intent(Interventix.getContext(), com.federicocolantoni.projects.interventix.activities.HomeActivity_.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Interventix.getContext().startActivity(intent);
	    }
	}
    }

    private void parseResponse(JSONObject response) throws JsonSyntaxException, JSONException {

	if (response.get(Constants.JSON_RESPONSE).toString().equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {

	    JSONObject data = response.getJSONObject(Constants.JSON_DATA);

	    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

	    Utente utente = gson.fromJson(data.toString(), Utente.class);

	    new AsyncTask<Utente, Void, Void>() {

		@Override
		protected Void doInBackground(Utente... params) {

		    if (!utenteDao.idExists(params[0].idutente))
			utenteDao.create(params[0]);
		    else {

			Utente utExists = utenteDao.queryForId(params[0].idutente);

			if (!utExists.equals(params[0]))
			    utenteDao.update(params[0]);
		    }

		    UtenteController.tecnicoLoggato = params[0];

		    return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {

		    addAccountAndStartActivity();
		}
	    }.execute(utente);
	}
	else {

	    InterventixToast.makeToast(toastLoginError, Toast.LENGTH_LONG);
	}
    }

    @Override
    public void onResume() {

	super.onResume();

	setRefreshActionButtonState(false);
    }

    @Override
    public void onPause() {

	super.onPause();

	setRefreshActionButtonState(false);
    }

    private void addAccountAndStartActivity() {

	String encryptedPassword = null;

	try {
	    encryptedPassword = PasswordHash.createHash(password.getText().toString());
	}
	catch (NoSuchAlgorithmException e) {

	    e.printStackTrace();
	}
	catch (InvalidKeySpecException e) {

	    e.printStackTrace();
	}

	accountManager = AccountManager.get(Interventix.getContext());

	assert accountManager != null;
	Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

	if (accounts.length == 0) {

	    final Account account = new Account(username.getText().toString(), Constants.ACCOUNT_TYPE_INTERVENTIX);

	    accountManager.addAccountExplicitly(account, encryptedPassword, null);
	    accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

	    SharedPreferences globalPrefs = Interventix.getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

	    final Editor edit = globalPrefs.edit().putString(Constants.USERNAME, username.getText().toString());

	    edit.apply();
	}
	else {

	    for (Account account : accounts) {

		if (account.name.equals(username.getText().toString())) {

		    accountManager.setPassword(account, password.getText().toString());
		    accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

		    new AsyncTask<String, Void, Utente>() {

			@Override
			protected Utente doInBackground(String... params) {

			    return utenteDao.queryForEq(Constants.ORMLITE_USERNAME, params[0]).get(0);
			}

			@Override
			protected void onPostExecute(Utente utente) {

			    UtenteController.tecnicoLoggato = utente;
			}
		    }.execute(username.getText().toString());

		    break;
		}
	    }
	}

	Intent intent = new Intent(Interventix.getContext(), com.federicocolantoni.projects.interventix.activities.HomeActivity_.class);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	password.setText("");

	Interventix.getContext().startActivity(intent);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

	if (username.getText().length() != 0 && password.getText().length() != 0)
	    btnLogin.setEnabled(true);
	else
	    btnLogin.setEnabled(false);
    }
}
