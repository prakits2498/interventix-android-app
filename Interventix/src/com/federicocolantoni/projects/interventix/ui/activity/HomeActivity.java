package com.federicocolantoni.projects.interventix.ui.activity;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.internal.view.menu.MenuItemImpl;
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
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.Constants.BUFFER_TYPE;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListInterventiAdapter;
import com.federicocolantoni.projects.interventix.controller.ClienteController;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.controller.InterventoSingleton;
import com.federicocolantoni.projects.interventix.controller.UtenteController;
import com.federicocolantoni.projects.interventix.core.BufferInterventix;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.utils.BigDecimalTypeAdapter;
import com.federicocolantoni.projects.interventix.utils.CheckConnection;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.ManagedAsyncTask;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity {

    private ListInterventiAdapter adapter;
    private SwingBottomInAnimationAdapter animationAdapter;

    private Menu optionsMenu;

    private SharedPreferences prefsLocal;

    @ViewById(R.id.list_interv_open)
    ListView listOpen;

    @StringRes(R.string.toast_error_syncro)
    String toastErrorSyncro;

    private BufferInterventix buffer;

    BroadcastReceiver receiverBufferFinish = new BroadcastReceiver() {

	@Override
	public void onReceive(Context context, Intent intent) {

	    if (intent.getAction().equals(Constants.ACTION_FINISH_BUFFER)) {

		try {
		    getUsersSyncro();
		}
		catch (Exception e) {

		    e.printStackTrace();
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
    }

    @Override
    protected void onStart() {

	super.onStart();

	getSupportActionBar().setTitle(UtenteController.tecnicoLoggato.nome + " " + UtenteController.tecnicoLoggato.cognome);

	InterventoController.controller = null;

	registerReceiver(receiverBufferFinish, new IntentFilter(Constants.ACTION_FINISH_BUFFER));

	try {
	    getUsersSyncro();
	}
	catch (Exception e) {

	    e.printStackTrace();
	}

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

	RuntimeExceptionDao<Intervento, Long> interventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeInterventoDao();

	QueryBuilder<Intervento, Long> qb = interventoDao.queryBuilder();

	qb.selectColumns(new String[] {
		"numero", "dataora", "cliente", "conflitto", "modificato", "nuovo"
	});

	try {
	    qb.where().eq("tecnico", UtenteController.tecnicoLoggato.idutente).and().eq("chiuso", false);
	    List<Intervento> listaInterventiAperti = interventoDao.query(qb.prepare());

	    adapter = new ListInterventiAdapter(listaInterventiAperti);

	    animationAdapter = new SwingBottomInAnimationAdapter(adapter, 150, 1500);
	    animationAdapter.setAbsListView(listOpen);

	    listOpen.setAdapter(animationAdapter);

	    animationAdapter.notifyDataSetChanged();
	}
	catch (SQLException e) {

	    e.printStackTrace();
	}

	com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {

	    // finish();
	}

	return true;
    }

    @Override
    public void onBackPressed() {

	// finish();
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
	parameters.put("revision", UtenteController.revisioneUtenti);

	String jsonReq = JsonCR2.createRequest("users", "syncro", parameters, UtenteController.tecnicoLoggato.idutente.intValue());

	SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

	String prefs_url = getResources().getString(R.string.prefs_key_url);

	String url_string = prefsDefault.getString(prefs_url, null);

	RequestQueue requestQueue = Volley.newRequestQueue(this);

	StringRequest jsonRequest = new StringRequest(Method.POST, String.format(getString(R.string.formatted_url_string), url_string, jsonReq), new Listener<String>() {

	    @Override
	    public void onResponse(String response) {

		try {

		    JSONObject jsonResp = new JSONObject(JsonCR2.read(response.trim()).toJSONString());

		    runAsyncTaskUserSyncro(jsonResp);
		}
		catch (ParseException e) {

		    e.printStackTrace();
		}
		catch (Exception e) {

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

		InterventixToast.makeToast("Errore di connessione", Toast.LENGTH_LONG);
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

		    if (response != null && response.getString("response").equalsIgnoreCase("success")) {
			JSONObject data = response.getJSONObject("data");

			UtenteController.revisioneUtenti = data.getLong("revision");

			JSONArray usersMOD = data.getJSONArray("mod");
			JSONArray usersDEL = data.getJSONArray("del");

			Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();

			RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

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
		    getClientsSyncro();
		else {

		    InterventixToast.makeToast(toastErrorSyncro, Toast.LENGTH_LONG);

		    com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

		    setRefreshActionButtonState(false);
		}
	    }
	}.execute();
    }

    private void getClientsSyncro() {

	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {

	    @Override
	    protected void onPreExecute() {

	    }

	    @Override
	    protected Integer doInBackground(Long... params) {

		String json_req = new String();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("revision", ClienteController.revisioneClienti);

		int result = 0;

		try {

		    if (CheckConnection.connectionIsAlive()) {
			json_req = JsonCR2.createRequest("clients", "syncro", parameters, params[0].intValue());

			SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

			String prefs_url = getResources().getString(R.string.prefs_key_url);

			String url_string = prefsDefault.getString(prefs_url, null);

			JSONObject response = new JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());

			if (response != null && response.getString("response").equalsIgnoreCase("success")) {
			    JSONObject data = response.getJSONObject("data");

			    ClienteController.revisioneClienti = data.getLong("revision");

			    RuntimeExceptionDao<Cliente, Long> clienteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeClienteDao();

			    JSONArray clientsMOD = data.getJSONArray("mod");
			    JSONArray clientsDEL = data.getJSONArray("del");

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
		    else {
			result = Constants.ERRORE_NO_CONNECTION;
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
		    getInterventionsSyncro();
		else {
		    if (result == RESULT_CANCELED) {

			InterventixToast.makeToast(toastErrorSyncro, Toast.LENGTH_LONG);

			com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
		    }
		    else if (result == Constants.ERRORE_NO_CONNECTION)
			InterventixToast.makeToast(getString(R.string.toast_no_connection_available), Toast.LENGTH_LONG);

		    setRefreshActionButtonState(false);
		}
	    }
	}.execute(UtenteController.tecnicoLoggato.idutente);
    }

    private void getInterventionsSyncro() {

	prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {

	    @Override
	    protected Integer doInBackground(Long... params) {

		String json_req = new String();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("revision", prefsLocal.getLong(Constants.REVISION_INTERVENTI, 0));

		int result = 0;

		long iduser = params[0];

		try {

		    if (CheckConnection.connectionIsAlive()) {
			json_req = JsonCR2.createRequest("interventions", "mysyncro", parameters, (int) iduser);

			SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);

			String prefs_url = getResources().getString(R.string.prefs_key_url);

			String url_string = prefsDefault.getString(prefs_url, null);

			JSONObject response = new JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());

			if (response != null && response.getString("response").equalsIgnoreCase("success")) {

			    JSONObject data = response.getJSONObject("data");

			    InterventoController.revisioneInterventi = data.getLong("revision");

			    RuntimeExceptionDao<Intervento, Long> interventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeInterventoDao();

			    JSONArray intervMOD = data.getJSONArray("mod");
			    JSONArray intervDEL = data.getJSONArray("del");
			    JSONArray interventions = data.getJSONArray("intervents");

			    if (intervMOD.length() > 0)
				for (int i = 0; i < intervMOD.length(); ++i) {
				    addInterventions((JSONObject) intervMOD.get(i), interventoDao);
				}

			    if (intervDEL.length() > 0)
				for (int i = 0; i < intervDEL.length(); ++i)
				    interventoDao.deleteById(intervDEL.getLong(i));

			    if (interventions.length() > 0)
				for (int k = 0; k < interventions.length(); ++k) {

				}

			    result = RESULT_OK;
			}
			else {
			    result = RESULT_CANCELED;
			}
		    }
		    else {
			result = Constants.ERRORE_NO_CONNECTION;
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

		    RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioInterventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeDettaglioInterventoDao();

		    JSONArray dettagli_intervento = responseIntervs.getJSONArray("dettagliintervento");

		    Gson gsonDettagli = new GsonBuilder().serializeNulls().setExclusionStrategies(new ExclusionStrategy() {

			@Override
			public boolean shouldSkipField(FieldAttributes f) {

			    if (f.getName().equals("tecniciintervento"))
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

			JsonArray tecnici = jo.getAsJsonArray("tecniciintervento");

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
				System.out.println("Il dettaglio " + dettExists.iddettagliointervento + " non necessita di nessuna sincronizzazione");
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

		    if (result == RESULT_CANCELED) {

			InterventixToast.makeToast(toastErrorSyncro, Toast.LENGTH_LONG);

			com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
		    }
		    else if (result == Constants.ERRORE_NO_CONNECTION)
			InterventixToast.makeToast(getString(R.string.toast_no_connection_available), Toast.LENGTH_LONG);

		    setRefreshActionButtonState(false);
		}
		else {

		    setRefreshActionButtonState(false);

		    new ReadListInterventions().execute();

		    // RuntimeExceptionDao<Intervento, Long> interventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeInterventoDao();
		    //
		    // QueryBuilder<Intervento, Long> qb = interventoDao.queryBuilder();
		    //
		    // qb.selectColumns(new String[] {
		    // "numero", "dataora", "cliente", "conflitto", "modificato", "nuovo"
		    // });
		    //
		    // try {
		    // qb.where().eq("tecnico", UtenteController.tecnicoLoggato.idutente).and().eq("chiuso", false);
		    // List<Intervento> listaInterventiAperti = interventoDao.query(qb.prepare());
		    //
		    // adapter = new ListInterventiAdapter(listaInterventiAperti);
		    //
		    // animationAdapter = new SwingBottomInAnimationAdapter(adapter, 150, 1500);
		    // animationAdapter.setAbsListView(listOpen);
		    //
		    // listOpen.setAdapter(animationAdapter);
		    //
		    // animationAdapter.notifyDataSetChanged();
		    // }
		    // catch (SQLException e) {
		    //
		    // e.printStackTrace();
		    // }
		    //
		    // com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
		}
	    }

	}.execute(UtenteController.tecnicoLoggato.idutente);
    }

    private class ReadListInterventions extends AsyncTask<Void, Void, List<Intervento>> {

	private RuntimeExceptionDao<Intervento, Long> interventoDao;
	private QueryBuilder<Intervento, Long> qb;

	public ReadListInterventions() {

	    interventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeInterventoDao();
	    qb = interventoDao.queryBuilder();
	}

	@Override
	protected void onPreExecute() {

	    qb.selectColumns(new String[] {
		    "numero", "dataora", "cliente", "conflitto", "modificato", "nuovo"
	    });

	    try {
		qb.where().eq("tecnico", UtenteController.tecnicoLoggato.idutente).and().eq("chiuso", false);
	    }
	    catch (SQLException e) {

		e.printStackTrace();
	    }
	}

	@Override
	protected List<Intervento> doInBackground(Void... params) {

	    List<Intervento> listaInterventiAperti = null;
	    try {
		listaInterventiAperti = interventoDao.query(qb.prepare());
	    }
	    catch (SQLException e) {

		e.printStackTrace();
	    }

	    return listaInterventiAperti;
	}

	@Override
	protected void onPostExecute(List<Intervento> result) {

	    adapter = new ListInterventiAdapter(result);

	    animationAdapter = new SwingBottomInAnimationAdapter(adapter, 150, 1500);
	    animationAdapter.setAbsListView(listOpen);

	    listOpen.setAdapter(animationAdapter);

	    animationAdapter.notifyDataSetChanged();

	    com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
	}
    }
}
