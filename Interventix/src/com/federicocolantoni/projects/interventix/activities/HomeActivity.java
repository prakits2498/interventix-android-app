package com.federicocolantoni.projects.interventix.activities;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapters.ListInterventiAdapter;
import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.core.BufferInterventix;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.BigDecimalTypeAdapter;
import com.federicocolantoni.projects.interventix.helpers.CheckConnection;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.Constants.BUFFER_TYPE;
import com.federicocolantoni.projects.interventix.helpers.InterventixToast;
import com.federicocolantoni.projects.interventix.helpers.ManagedAsyncTask;
import com.federicocolantoni.projects.interventix.models.Cliente;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.Intervento;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.federicocolantoni.projects.interventix.models.InterventoSingleton;
import com.federicocolantoni.projects.interventix.models.Utente;
import com.federicocolantoni.projects.interventix.models.UtenteController;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity {

    private static final String REVISION = "revision";
    private ListInterventiAdapter adapter;
    private SwingBottomInAnimationAdapter animationAdapter;

    private Menu optionsMenu;

    @ViewById(R.id.list_interv_open)
    ListView listOpen;

    @StringRes(R.string.toast_error_syncro)
    String toastErrorSyncro;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    RuntimeExceptionDao<Utente, Long> utenteDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Cliente.class)
    RuntimeExceptionDao<Cliente, Long> clienteDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Intervento.class)
    RuntimeExceptionDao<Intervento, Long> interventoDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = DettaglioIntervento.class)
    RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioInterventoDao;

    private BufferInterventix buffer;

    SharedPreferences prefsDefault, globalPrefs;

    String prefsUrl;

    String urlString;

    BroadcastReceiver receiverBufferFinish = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {

	    if (intent.getAction().equals(Constants.ACTION_FINISH_BUFFER)) {

		try {
		    getUsersSyncro();
		}
		catch (Exception e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
	    }
	}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

	getSupportActionBar().setHomeButtonEnabled(false);
	getSupportActionBar().setDisplayHomeAsUpEnabled(false);

	buffer = BufferInterventix.getBufferInterventix();

	prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
	prefsUrl = getResources().getString(R.string.prefs_key_url);
	urlString = prefsDefault.getString(prefsUrl, null);

	globalPrefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

	UtenteController.tecnicoLoggato = utenteDao.queryForEq(Constants.ORMLITE_USERNAME, globalPrefs.getString(Constants.USERNAME, "")).get(0);
    }

    @Override
    protected void onStart() {

	super.onStart();

	SpannableStringBuilder spanStringBuilder = new SpannableStringBuilder(UtenteController.tecnicoLoggato.nome + " " + UtenteController.tecnicoLoggato.cognome);
	spanStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	spanStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, spanStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

	getSupportActionBar().setTitle(spanStringBuilder);
	getSupportActionBar().setSubtitle(R.string.incoming_interventions);

	InterventoController.controller = null;

	registerReceiver(receiverBufferFinish, new IntentFilter(Constants.ACTION_FINISH_BUFFER));

	listOpen = (ListView) findViewById(R.id.list_interv_open);

	listOpen.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

		Intervento intervento = (Intervento) adapter.getItemAtPosition(position);

		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.INTERVENTO, intervento);

		Intent intent = new Intent(HomeActivity.this, ViewInterventoActivity_.class);
		intent.putExtras(bundle);

		startActivity(intent);
	    }
	});

	if (CheckConnection.connectionIsAlive()) {
	    try {
		getUsersSyncro();
	    }
	    catch (Exception e) {

		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	}
	else {

	    QueryBuilder<Intervento, Long> qb = interventoDao.queryBuilder();

	    qb.selectColumns(new String[] {
		    Constants.ORMLITE_NUMERO, Constants.ORMLITE_DATAORA, Constants.ORMLITE_CLIENTE, Constants.ORMLITE_CONFLITTO, Constants.ORMLITE_MODIFICATO, Constants.ORMLITE_NUOVO
	    });

	    try {
		qb.where().eq(Constants.ORMLITE_TECNICO, UtenteController.tecnicoLoggato.idutente).and().eq(Constants.ORMLITE_CHIUSO, false);
		qb.orderBy(Constants.ORMLITE_DATAORA, false).orderBy(Constants.ORMLITE_NUMERO, false);

		List<Intervento> listaInterventiAperti = interventoDao.query(qb.prepare());

		adapter = new ListInterventiAdapter(listaInterventiAperti);

		animationAdapter = new SwingBottomInAnimationAdapter(adapter, 500, 1500);
		animationAdapter.setAbsListView(listOpen);

		listOpen.setAdapter(animationAdapter);

		animationAdapter.notifyDataSetChanged();
	    }
	    catch (SQLException e) {

		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }

	    setRefreshActionButtonState(false);
	}
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();

	Interventix.releaseDbHelper();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {

	    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {

		AlertDialog.Builder builder = new Builder(this);

		builder.setTitle("Logout");
		builder.setMessage("Effettuare il logout dall'app?");
		builder.setPositiveButton(getString(R.string.ok_btn), new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {

			dialog.dismiss();

			AccountManager accountManager = AccountManager.get(HomeActivity.this);

			Account account = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX)[0];

			accountManager.clearPassword(account);

			finish();
		    }
		});
		builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {

			dialog.dismiss();
		    }
		});

		builder.create().show();
	    }
	}

	return true;
    }

    @Override
    public void onBackPressed() {

	if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {

	    AlertDialog.Builder builder = new Builder(this);

	    builder.setTitle("Logout");
	    builder.setMessage("Effettuare il logout dall'app?");
	    builder.setPositiveButton(getString(R.string.ok_btn), new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		    dialog.dismiss();

		    AccountManager accountManager = AccountManager.get(HomeActivity.this);

		    Account account = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX)[0];

		    accountManager.clearPassword(account);

		    finish();
		}
	    });
	    builder.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		    dialog.dismiss();
		}
	    });

	    builder.create().show();
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	getMenuInflater().inflate(R.menu.menu_home, menu);
	optionsMenu = menu;

	setRefreshActionButtonState(true);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {

	    case R.id.refresh_menu:

		try {
		    getUsersSyncro();
		}
		catch (Exception e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		setRefreshActionButtonState(true);

		break;

	    case R.id.add_menu:

		Intent intent = new Intent(HomeActivity.this, ViewInterventoActivity_.class);

		startActivity(intent);

		break;

	    case R.id.logout:

		AccountManager accountManager = AccountManager.get(this);

		Account account = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX)[0];

		accountManager.clearPassword(account);

		finish();

		break;
	}

	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {

	super.onPause();

	buffer.stopTimer();

	unregisterReceiver(receiverBufferFinish);
    }

    @Override
    protected void onResume() {

	super.onResume();

	InterventoSingleton.reset();

	buffer.startTimer(BUFFER_TYPE.BUFFER_INTERVENTO);
	buffer.startTimer(BUFFER_TYPE.BUFFER_CLIENTE);

	utenteDao = Interventix.getDbHelper().getRuntimeUtenteDao();

	UtenteController.tecnicoLoggato = utenteDao.queryForEq(Constants.ORMLITE_USERNAME, globalPrefs.getString(Constants.USERNAME, "")).get(0);
    }

    private void setRefreshActionButtonState(final boolean refreshing) {

	if (optionsMenu != null) {

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		final MenuItem refreshItem = optionsMenu.findItem(R.id.refresh_menu);

		if (refreshItem != null)
		    if (refreshing) {
			refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		    }
		    else {
			refreshItem.setActionView(null);
		    }
	    }
	    else {
		final MenuItemImpl refreshItem = (MenuItemImpl) optionsMenu.findItem(R.id.refresh_menu);

		if (refreshItem != null) {
		    if (refreshing) {
			refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		    }
		    else {
			refreshItem.setActionView(null);
		    }
		}
	    }
	}
    }

    private void getUsersSyncro() throws Exception {

	setRefreshActionButtonState(true);

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put(REVISION, globalPrefs.getLong(Constants.REVISION_TECNICI, 0L));

	String jsonReq = JsonCR2.createRequest(Constants.JSON_USERS_SECTION, Constants.JSON_SYNCRO_USERS_ACTION, parameters, UtenteController.tecnicoLoggato.idutente.intValue());

	RequestQueue requestQueue = Volley.newRequestQueue(this);

	StringRequest jsonRequest = new StringRequest(Method.POST, String.format(getString(R.string.formatted_url_string), urlString, jsonReq), new Listener<String>() {

	    @Override
	    public void onResponse(String response) {

		try {

		    JSONObject jsonResp = new JSONObject(JsonCR2.read(response.trim()).toJSONString());

		    runAsyncTaskUserSyncro(jsonResp);
		}
		catch (ParseException e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		catch (Exception e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
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

    private void runAsyncTaskUserSyncro(final JSONObject jsonResp) {

	new ManagedAsyncTask<Void, Void, Integer>(HomeActivity.this) {

	    private JSONObject response = jsonResp;

	    @Override
	    protected Integer doInBackground(Void... params) {

		int result = 0;

		try {

		    if (response != null && response.getString(Constants.JSON_RESPONSE).equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {
			JSONObject data = response.getJSONObject(Constants.JSON_DATA);

			globalPrefs.edit().putLong(Constants.REVISION_TECNICI, data.getLong(REVISION)).commit();

			JSONArray usersMOD = data.getJSONArray(Constants.JSON_MOD);
			JSONArray usersDEL = data.getJSONArray(Constants.JSON_DEL);

			Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

			for (int i = 0; i < usersMOD.length(); i++) {

			    JSONObject obj = usersMOD.getJSONObject(i);

			    Utente utente = gson.fromJson(obj.toString(), Utente.class);

			    if (!utenteDao.idExists(utente.idutente))
				utenteDao.create(utente);
			    else {
				utenteDao.update(utente);
			    }
			}

			if (usersDEL.length() > 0) {

			    for (int k = 0; k < usersDEL.length(); k++) {

				utenteDao.deleteById(usersDEL.getLong(k));
			    }
			}

			result = RESULT_OK;
		    }
		    else {
			result = RESULT_CANCELED;
		    }
		}
		catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		return result;
	    }

	    @Override
	    protected void onPostExecute(Integer result) {

		if (result == RESULT_OK)
		    try {
			getClientsSyncro(null);
		    }
		    catch (Exception e) {

			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		else {

		    InterventixToast.makeToast(toastErrorSyncro, Toast.LENGTH_LONG);

		    setRefreshActionButtonState(false);
		}
	    }
	}.execute();
    }

    private void getClientsSyncro(JSONObject response) throws Exception {

	String jsonReq = new String();

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put(REVISION, globalPrefs.getLong(Constants.REVISION_CLIENTI, 0L));

	jsonReq = JsonCR2.createRequest(Constants.JSON_CLIENTS_SECTION, Constants.JSON_SYNCRO_CLIENTS_ACTION, parameters, UtenteController.tecnicoLoggato.idutente.intValue());

	RequestQueue requestQueue = Volley.newRequestQueue(this);

	StringRequest jsonRequest = new StringRequest(Method.POST, String.format(getString(R.string.formatted_url_string), urlString, jsonReq), new Listener<String>() {

	    @Override
	    public void onResponse(String response) {

		try {

		    JSONObject jsonResp = new JSONObject(JsonCR2.read(response.trim()).toJSONString());

		    runAsyncTaskClientSyncro(jsonResp);
		}
		catch (ParseException e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		catch (Exception e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		finally {

		    setRefreshActionButtonState(false);
		}
	    }
	}, new ErrorListener() {

	    @Override
	    public void onErrorResponse(VolleyError error) {

		setRefreshActionButtonState(false);

		InterventixToast.makeToast(error.networkResponse.data == null ? getString(R.string.service_not_available) : new String(error.getMessage()), Toast.LENGTH_LONG);
	    }
	});

	requestQueue.add(jsonRequest);

    }

    private void runAsyncTaskClientSyncro(final JSONObject jsonResp) {

	new ManagedAsyncTask<Void, Void, Integer>(HomeActivity.this) {

	    private JSONObject response = jsonResp;

	    @Override
	    protected Integer doInBackground(Void... params) {

		int result = 0;

		try {

		    if (response != null && response.getString(Constants.JSON_RESPONSE).equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {
			JSONObject data = response.getJSONObject(Constants.JSON_DATA);

			globalPrefs.edit().putLong(Constants.REVISION_CLIENTI, data.getLong(REVISION)).commit();

			JSONArray clientsMOD = data.getJSONArray(Constants.JSON_MOD);
			JSONArray clientsDEL = data.getJSONArray(Constants.JSON_DEL);

			Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

			for (int i = 0; i < clientsMOD.length(); i++) {

			    JSONObject clienteJSON = clientsMOD.getJSONObject(i);

			    Cliente cliente = gson.fromJson(clienteJSON.toString(), Cliente.class);

			    if (!clienteDao.idExists(cliente.idcliente))
				clienteDao.create(cliente);
			    else
				clienteDao.update(cliente);
			}

			for (int k = 0; k < clientsDEL.length(); k++) {

			    clienteDao.deleteById(clientsDEL.getLong(k));
			}

			result = RESULT_OK;
		    }
		    else {
			result = RESULT_CANCELED;
		    }
		}
		catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		return result;
	    }

	    @Override
	    protected void onPostExecute(Integer result) {

		if (result == RESULT_OK)
		    try {
			getInterventionsSyncro(null);
		    }
		    catch (Exception e) {

			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		else {

		    InterventixToast.makeToast(toastErrorSyncro, Toast.LENGTH_LONG);

		    setRefreshActionButtonState(false);
		}
	    }
	}.execute();
    }

    private void getInterventionsSyncro(JSONObject jsonResp) throws Exception {

	String jsonReq = new String();

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put(REVISION, globalPrefs.getLong(Constants.REVISION_INTERVENTI, 0L));

	jsonReq = JsonCR2.createRequest(Constants.JSON_INTERVENTIONS_SECTION, Constants.JSON_MYSYNCRO_INTERVENTIONS_ACTION, parameters, UtenteController.tecnicoLoggato.idutente.intValue());

	RequestQueue requestQueue = Volley.newRequestQueue(this);

	StringRequest jsonRequest = new StringRequest(Method.POST, String.format(getString(R.string.formatted_url_string), urlString, jsonReq), new Listener<String>() {

	    @Override
	    public void onResponse(String response) {

		try {

		    JSONObject jsonResp = new JSONObject(JsonCR2.read(response.trim()).toJSONString());

		    runAsyncTaskInterventionsSyncro(jsonResp);
		}
		catch (ParseException e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		catch (Exception e) {

		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		finally {

		    setRefreshActionButtonState(false);
		}
	    }
	}, new ErrorListener() {

	    @Override
	    public void onErrorResponse(VolleyError error) {

		setRefreshActionButtonState(false);

		InterventixToast.makeToast(error.networkResponse.data == null ? getString(R.string.service_not_available) : new String(error.getMessage()), Toast.LENGTH_LONG);
	    }
	});

	requestQueue.add(jsonRequest);
    }

    private void runAsyncTaskInterventionsSyncro(final JSONObject jsonResp) {

	new ManagedAsyncTask<Void, Void, Integer>(HomeActivity.this) {

	    private JSONObject response = jsonResp;

	    @Override
	    protected Integer doInBackground(Void... params) {

		int result = 0;

		try {

		    if (response != null && response.getString(Constants.JSON_RESPONSE).equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {

			JSONObject data = response.getJSONObject(Constants.JSON_DATA);

			globalPrefs.edit().putLong(Constants.REVISION_INTERVENTI, data.getLong(REVISION)).commit();

			JSONArray intervMOD = data.getJSONArray(Constants.JSON_MOD);
			JSONArray intervDEL = data.getJSONArray(Constants.JSON_DEL);
			JSONArray interventions = data.getJSONArray(Constants.JSON_INTERVENTS);

			if (intervMOD.length() > 0)
			    for (int i = 0; i < intervMOD.length(); ++i) {
				addInterventions((JSONObject) intervMOD.get(i), interventoDao);
			    }

			if (intervDEL.length() > 0)
			    for (int i = 0; i < intervDEL.length(); ++i)
				interventoDao.deleteById(intervDEL.getLong(i));

			if (interventions.length() > 0) {

			    interventoDao = Interventix.getDbHelper().getRuntimeInterventoDao();

			    ArrayList<Long> interventionsToDelete = new ArrayList<Long>();

			    for (int k = 0; k < interventions.length(); ++k) {

				long idIntervention = interventions.getLong(k);

				if (!interventoDao.idExists(idIntervention))
				    interventionsToDelete.add(idIntervention);
			    }

			    for (Long id : interventionsToDelete)
				interventoDao.deleteById(id);
			}

			result = RESULT_OK;
		    }
		    else {
			result = RESULT_CANCELED;
		    }
		}
		catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		return result;
	    }

	    private void addInterventions(JSONObject responseIntervs, RuntimeExceptionDao<Intervento, Long> interventoDao) {

		Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).registerTypeAdapter(new TypeToken<BigDecimal>() {
		}.getType(), new BigDecimalTypeAdapter()).create();

		try {

		    Intervento intervento = gson.fromJson(responseIntervs.toString(), Intervento.class);

		    if (!interventoDao.idExists(intervento.idintervento)) {

			intervento.modificato = Constants.INTERVENTO_SINCRONIZZATO;
			intervento.nuovo = false;
			interventoDao.create(intervento);
		    }
		    else {

			Intervento intervExists = interventoDao.queryForId(intervento.idintervento);

			if (intervExists.modificato.equals(Constants.INTERVENTO_MODIFICATO) || intervExists.modificato.equals(Constants.INTERVENTO_NUOVO)) {

			}
			else {
			    intervento.modificato = Constants.INTERVENTO_SINCRONIZZATO;
			    intervento.nuovo = false;

			    interventoDao.update(intervento);

			}
		    }

		    JSONArray dettagli_intervento = responseIntervs.getJSONArray(Constants.JSON_DETTAGLIINTERVENTO);

		    Gson gsonDettagli = new GsonBuilder().serializeNulls().setExclusionStrategies(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {

			    if (f.getName().equals(Constants.JSON_TECNICIINTERVENTO))
				return true;
			    else
				return false;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {

			    return false;
			}
		    }).setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

		    for (int cont = 0; cont < dettagli_intervento.length(); cont++) {

			JSONObject newDettInterv = dettagli_intervento.getJSONObject(cont);

			JsonParser parser = new JsonParser();

			JsonObject jo = (JsonObject) parser.parse(newDettInterv.toString());

			JsonArray tecnici = jo.getAsJsonArray(Constants.JSON_TECNICIINTERVENTO);

			DettaglioIntervento dtInt = gsonDettagli.fromJson(newDettInterv.toString(), DettaglioIntervento.class);

			if (!dettaglioInterventoDao.idExists(dtInt.iddettagliointervento)) {

			    dtInt.idintervento = (intervento.idintervento);
			    dtInt.tecniciintervento = (tecnici.toString());
			    dtInt.modificato = Constants.DETTAGLIO_SINCRONIZZATO;
			    dtInt.nuovo = false;

			    dettaglioInterventoDao.create(dtInt);
			}
			else {

			    DettaglioIntervento dettExists = dettaglioInterventoDao.queryForId(dtInt.iddettagliointervento);

			    if (dettExists.modificato.equals(Constants.DETTAGLIO_MODIFICATO) || dettExists.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
				// System.out.println("Il dettaglio " + dettExists.iddettagliointervento + " non necessita di nessuna sincronizzazione");
			    }
			    else {
				dtInt.idintervento = (intervento.idintervento);
				dtInt.tecniciintervento = (tecnici.toString());
				dtInt.modificato = Constants.DETTAGLIO_SINCRONIZZATO;
				dtInt.nuovo = false;

				dettaglioInterventoDao.update(dtInt);
			    }
			}
		    }
		}
		catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
	    }

	    @Override
	    protected void onPostExecute(Integer result) {

		if (result != RESULT_OK) {

		    InterventixToast.makeToast(toastErrorSyncro, Toast.LENGTH_LONG);

		    setRefreshActionButtonState(false);
		}
		else {

		    HomeActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {

			    readListInterventions();
			}
		    });
		}
	    }
	}.execute();
    }

    private void readListInterventions() {

	QueryBuilder<Intervento, Long> qb;

	qb = interventoDao.queryBuilder();

	qb.selectColumns(new String[] {
		Constants.ORMLITE_NUMERO, Constants.ORMLITE_DATAORA, Constants.ORMLITE_CLIENTE, Constants.ORMLITE_CONFLITTO, Constants.ORMLITE_MODIFICATO, Constants.ORMLITE_NUOVO
	});

	try {
	    qb.where().eq(Constants.ORMLITE_TECNICO, UtenteController.tecnicoLoggato.idutente).and().eq(Constants.ORMLITE_CHIUSO, false);
	    qb.orderBy(Constants.ORMLITE_DATAORA, false).orderBy(Constants.ORMLITE_NUMERO, false);
	}
	catch (SQLException e) {

	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	List<Intervento> listaInterventiAperti = null;

	try {
	    listaInterventiAperti = interventoDao.query(qb.prepare());
	}
	catch (SQLException e) {

	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	adapter = new ListInterventiAdapter(listaInterventiAperti);

	animationAdapter = new SwingBottomInAnimationAdapter(adapter, 500, 1500);
	animationAdapter.setAbsListView(listOpen);

	listOpen.setAdapter(animationAdapter);

	animationAdapter.notifyDataSetChanged();

	setRefreshActionButtonState(false);
    }
}