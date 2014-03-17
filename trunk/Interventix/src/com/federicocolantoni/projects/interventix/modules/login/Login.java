package com.federicocolantoni.projects.interventix.modules.login;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.UtenteController;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.utils.CheckConnection;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.PasswordHash;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.qustom.dialog.QustomDialogBuilder;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_login)
public class Login extends Fragment {

    @ViewById(R.id.field_password)
    EditText password;

    @ViewById(R.id.field_username)
    EditText username;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    RuntimeExceptionDao<Utente, Long> utenteDao;

    private SharedPreferences defaultPrefs, globalPrefs;

    private AccountManager accountManager;

    private Menu optionsMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	defaultPrefs = PreferenceManager.getDefaultSharedPreferences(com.federicocolantoni.projects.interventix.Interventix_.getContext());
    }

    @Override
    public void onStart() {

	super.onStart();

	setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {

	super.onStop();

	com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
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

	if (username.getText().toString().length() == 0 || password.getText().toString().length() == 0)
	    InterventixToast.makeToast(getString(R.string.toast_username_and_password_required), Toast.LENGTH_SHORT);
	else {

	    setRefreshActionButtonState(true);

	    String jsonReq = new String();

	    if (CheckConnection.connectionIsAlive()) {

		try {
		    HashMap<String, String> parameters = new HashMap<String, String>();

		    parameters.put(Constants.JSON_USERNAME, username.getText().toString());
		    parameters.put(Constants.JSON_PASSWORD, password.getText().toString());
		    parameters.put(Constants.JSON_TYPE, Constants.USER_TYPE.TECNICO.name());

		    final String prefsUrl = com.federicocolantoni.projects.interventix.Interventix_.getContext().getResources().getString(R.string.prefs_key_url);

		    final String url = defaultPrefs.getString(prefsUrl, null);

		    jsonReq = JsonCR2.createRequest(Constants.JSON_USER_SECTION, Constants.JSON_LOGIN_ACTION, parameters, -1);

		    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

		    StringRequest jsonRequest = new StringRequest(Method.POST, String.format(getString(R.string.formatted_url_string), url, jsonReq), new Listener<String>() {

			@Override
			public void onResponse(String response) {

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
		    }, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			    setRefreshActionButtonState(false);

			    InterventixToast.makeToast(getString(R.string.service_not_available), Toast.LENGTH_LONG);
			}
		    });

		    requestQueue.add(jsonRequest);
		}
		catch (Exception e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
	    }
	    else {

		// connessione in modalità offline

		accountManager = AccountManager.get(com.federicocolantoni.projects.interventix.Interventix_.getContext());

		Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

		if (accounts.length == 0) {

		    QustomDialogBuilder builder = new QustomDialogBuilder(getActivity());

		    builder.setIcon(R.drawable.ic_launcher);
		    builder.setTitleColor(com.federicocolantoni.projects.interventix.Interventix_.getContext().getResources().getColor(R.color.interventix_color));
		    builder.setDividerColor(com.federicocolantoni.projects.interventix.Interventix_.getContext().getResources().getColor(R.color.interventix_color));

		    builder.setTitle(getString(R.string.not_offline_access_title));
		    builder.setMessage(getString(R.string.not_offline_access_message)).setPositiveButton(getString(R.string.ok_btn), new Dialog.OnClickListener() {
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

			    // RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

			    UtenteController.tecnicoLoggato = utenteDao.queryForEq(Constants.JSON_USERNAME, username.getText().toString()).get(0);

			    // com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

			    break;
			}
		    }

		    setRefreshActionButtonState(false);

		    Intent intent = new Intent(com.federicocolantoni.projects.interventix.Interventix_.getContext(), com.federicocolantoni.projects.interventix.ui.activity.HomeActivity_.class);
		    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		    com.federicocolantoni.projects.interventix.Interventix_.getContext().startActivity(intent);
		}
	    }
	}
    }

    private void parseResponse(JSONObject response) throws JsonSyntaxException, JSONException {

	if (response.get(Constants.JSON_RESPONSE).toString().equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {

	    JSONObject data = response.getJSONObject(Constants.JSON_DATA);

	    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

	    Utente utente = gson.fromJson(data.toString(), Utente.class);

	    // RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

	    if (!utenteDao.idExists(utente.idutente))
		utenteDao.create(utente);
	    else {

		Utente utExists = utenteDao.queryForId(utente.idutente);

		if (!utExists.equals(utente))
		    utenteDao.update(utente);
	    }

	    UtenteController.tecnicoLoggato = utente;

	    // com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

	    addAccountAndStartActivity();
	}
	else {

	    InterventixToast.makeToast(getString(R.string.toast_login_error), Toast.LENGTH_LONG);
	}
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

	accountManager = AccountManager.get(com.federicocolantoni.projects.interventix.Interventix_.getContext());

	Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

	if (accounts.length == 0) {

	    final Account account = new Account(username.getText().toString(), Constants.ACCOUNT_TYPE_INTERVENTIX);

	    accountManager.addAccountExplicitly(account, encryptedPassword, null);
	    accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

	    globalPrefs = com.federicocolantoni.projects.interventix.Interventix_.getContext().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

	    final Editor edit = globalPrefs.edit().putString(Constants.USERNAME, username.getText().toString()).putString(Constants.PASSWORD, encryptedPassword);

	    edit.apply();
	}
	else {

	    for (Account account : accounts) {

		if (account.name.equals(username.getText().toString())) {

		    accountManager.setPassword(account, password.getText().toString());
		    accountManager.setAuthToken(account, Constants.ACCOUNT_TYPE_INTERVENTIX, Constants.ACCOUNT_AUTH_TOKEN);

		    // RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

		    UtenteController.tecnicoLoggato = utenteDao.queryForEq(com.federicocolantoni.projects.interventix.ui.activity.MainActivity_.ORMLITE_USERNAME, username.getText().toString()).get(0);

		    // com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

		    break;
		}
	    }
	}

	Intent intent = new Intent(com.federicocolantoni.projects.interventix.Interventix_.getContext(), com.federicocolantoni.projects.interventix.ui.activity.HomeActivity_.class);
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	setRefreshActionButtonState(false);

	password.setText("");

	com.federicocolantoni.projects.interventix.Interventix_.getContext().startActivity(intent);
    }
}
