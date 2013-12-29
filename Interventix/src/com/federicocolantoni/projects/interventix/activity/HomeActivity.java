package com.federicocolantoni.projects.interventix.activity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import multiface.crypto.cr2.JsonCR2;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.adapter.ListInterventiAdapter;
import com.federicocolantoni.projects.interventix.core.BufferInvioCliente;
import com.federicocolantoni.projects.interventix.core.BufferInvioIntervento;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.task.GetNominativoUtenteAsyncTask;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_home)
public class HomeActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {
    
    private final static int MESSAGE_LOADER = 1;
    
    static final String[] PROJECTION = new String[] {
	    InterventoDB.Fields._ID, InterventoDB.Fields.NUMERO_INTERVENTO, InterventoDB.Fields.CLIENTE, InterventoDB.Fields.ID_INTERVENTO, InterventoDB.Fields.DATA_ORA
    };
    
    static final String SELECTION = InterventoDB.Fields.TYPE + " =? AND " + InterventoDB.Fields.CHIUSO + " =?";
    
    static final String[] SELECTION_ARGS = new String[] {
	    InterventoDB.INTERVENTO_ITEM_TYPE, "0"
    };
    
    private ListInterventiAdapter mAdapter;
    
    private Menu optionsMenu;
    
    private SharedPreferences prefsLocal;
    
    @ViewById(R.id.list_interv_open)
    ListView listOpen;
    
    @StringRes(R.string.toast_error_syncro_users)
    String toast_error_syncro_users;
    
    @StringRes(R.string.toast_error_syncro_clients)
    String toast_error_syncro_clients;
    
    @StringRes(R.string.toast_error_syncro_interventions)
    String toast_error_syncro_interventions;
    
    // buffer per gli interventi e i clienti
    BufferInvioCliente bufferCliente;
    BufferInvioIntervento bufferIntervento;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	bufferIntervento = new BufferInvioIntervento(this);
	bufferCliente = new BufferInvioCliente(this);
    }
    
    @Override
    protected void onStart() {
	
	super.onStart();
	
	getUsersSyncro();
	
	listOpen = (ListView) findViewById(R.id.list_interv_open);
	
	mAdapter = new ListInterventiAdapter(this, null);
	
	listOpen.setAdapter(mAdapter);
	
	getSupportLoaderManager().initLoader(MESSAGE_LOADER, null, this);
	
	listOpen.setOnItemClickListener(new OnItemClickListener() {
	    
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		
		Bundle bundle = new Bundle();
		
		Cursor cur = (Cursor) adapter.getItemAtPosition(position);
		
		bundle.putLong(Constants.ID_INTERVENTO, cur.getLong(cur.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
		bundle.putLong(Constants.NUMERO_INTERVENTO, cur.getLong(cur.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO)));
		
		Intent intent = new Intent(HomeActivity.this, com.federicocolantoni.projects.interventix.activity.ViewInterventoActivity_.class);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
	    }
	});
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    
	    finish();
	}
	
	return true;
    }
    
    @Override
    public void onBackPressed() {
	
	finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	optionsMenu = menu;
	getMenuInflater().inflate(R.menu.menu_home, menu);
	
	setRefreshActionButtonState(true);
	
	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		finish();
		
		break;
	    
	    case R.id.refresh_menu:
		
		getUsersSyncro();
		setRefreshActionButtonState(true);
		
		break;
	    
	    case R.id.add_menu:
		
		Bundle bundle = new Bundle();
		
		bundle.putLong(Constants.ID_INTERVENTO, Constants.sIdInterventoTemp);
		bundle.putLong(Constants.NUMERO_INTERVENTO, Constants.sIdInterventoTemp);
		
		Intent intent = new Intent(HomeActivity.this, com.federicocolantoni.projects.interventix.activity.ViewInterventoActivity_.class);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
		
		// Constants.sIdInterventoTemp -= 1;
		
		break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onPause() {
	
	super.onPause();
	
	bufferIntervento.stopTimerInterventi();
	bufferCliente.stopTimerClienti();
    }
    
    @Override
    protected void onResume() {
	
	super.onResume();
	
	bufferIntervento.startTimerInterventi();
	bufferCliente.startTimerClienti();
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
    
    private String setNominativo() throws InterruptedException, ExecutionException {
	
	prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	GetNominativoUtenteAsyncTask nominativo = new GetNominativoUtenteAsyncTask(this);
	
	nominativo.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
	
	Utente utente = nominativo.get();
	
	return utente.getNome() + " " + utente.getCognome();
    }
    
    private void getUsersSyncro() {
	
	prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {
	    
	    @Override
	    protected void onPreExecute() {
		
		setRefreshActionButtonState(true);
	    };
	    
	    @Override
	    protected Integer doInBackground(Long... params) {
		String json_req = new String();
		
		ContentResolver cr = getContentResolver();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("revision", prefsLocal.getLong(Constants.REVISION_USERS, 0));
		
		int result = 0;
		
		try {
		    
		    json_req = JsonCR2.createRequest("users", "syncro", parameters, params[0].intValue());
		    
		    System.out.println("REQUEST SYNCRO USERS:\n" + json_req);
		    
		    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
		    
		    final String prefs_url = getResources().getString(string.prefs_key_url);
		    
		    final String url_string = prefsDefault.getString(prefs_url, null);
		    
		    JSONObject response = new org.json.JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());
		    
		    System.out.println("RESPONSE SYNCRO USERS:\n" + response.toString());
		    
		    if (response != null && response.getString("response").equalsIgnoreCase("success")) {
			JSONObject data = response.getJSONObject("data");
			
			System.out.println("REVISIONE UTENTI " + data.getLong("revision"));
			
			final Editor editor = prefsLocal.edit();
			
			editor.putLong(Constants.REVISION_USERS, data.getLong("revision"));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    }).start();
			}
			
			JSONArray usersMOD = data.getJSONArray("mod");
			JSONArray usersDEL = data.getJSONArray("del");
			
			ContentValues values = new ContentValues();
			
			for (int i = 0; i < usersMOD.length(); i++) {
			    JSONObject obj = usersMOD.getJSONObject(i);
			    
			    System.out.println("INSERT MOD USERS");
			    
			    if (obj.getLong("idutente") != params[0]) {
				
				Utente newUser = new Utente();
				newUser.setIdUtente(obj.getLong("idutente"));
				newUser.setCancellato(obj.getBoolean("cancellato"));
				newUser.setCestinato(obj.getBoolean("cestinato"));
				newUser.setCognome(obj.getString("cognome"));
				newUser.setEmail(obj.getString("email"));
				newUser.setNome(obj.getString("nome"));
				newUser.setUserName(obj.getString("username"));
				newUser.setRevisione(obj.getLong("revisione"));
				newUser.setTipo(obj.getString("tipo"));
				
				values = Utente.insertSQL(newUser);
				
				cr.insert(UtenteDB.CONTENT_URI, values);
			    }
			}
			
			if (usersDEL.length() > 0) {
			    for (int k = 0; k < usersDEL.length(); k++) {
				
				String where = UtenteDB.Fields.ID_UTENTE + " = ? AND " + Fields.TYPE + " = ?";
				
				String[] selectionArgs = new String[] {
					"" + usersDEL.getLong(k), UtenteDB.UTENTE_ITEM_TYPE
				};
				
				cr.delete(UtenteDB.CONTENT_URI, where, selectionArgs);
			    }
			}
			else {
			    System.out.println("DEL USERS EMPTY");
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
		
		if (result == RESULT_OK) {
		    getClientsSyncro();
		}
		else {
		    
		    InterventixToast.makeToast(HomeActivity.this, toast_error_syncro_users, Toast.LENGTH_LONG);
		    
		    setRefreshActionButtonState(false);
		}
	    }
	    
	}.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
    }
    
    private void getClientsSyncro() {
	
	prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {
	    
	    @Override
	    protected void onPreExecute() {
		
	    }
	    
	    @Override
	    protected Integer doInBackground(Long... params) {
		
		String json_req = new String();
		
		ContentResolver cr = getContentResolver();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("revision", prefsLocal.getLong(Constants.REVISION_CLIENTS, 0));
		
		int result = 0;
		
		try {
		    
		    json_req = JsonCR2.createRequest("clients", "syncro", parameters, params[0].intValue());
		    
		    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
		    
		    final String prefs_url = getResources().getString(string.prefs_key_url);
		    
		    final String url_string = prefsDefault.getString(prefs_url, null);
		    
		    JSONObject response = new JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());
		    
		    if (response != null && response.getString("response").equalsIgnoreCase("success")) {
			JSONObject data = response.getJSONObject("data");
			
			System.out.println("REVISIONE CLIENTI " + data.getLong("revision"));
			
			final Editor editor = prefsLocal.edit();
			
			editor.putLong(Constants.REVISION_CLIENTS, data.getLong("revision"));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    }).start();
			}
			
			JSONArray clientsMOD = data.getJSONArray("mod");
			JSONArray clientsDEL = data.getJSONArray("del");
			
			Cursor cursorCliente = null;
			
			for (int i = 0; i < clientsMOD.length(); i++) {
			    
			    JSONObject cliente = clientsMOD.getJSONObject(i);
			    
			    String selectionCliente = ClienteDB.Fields.TYPE + " = ? AND " + ClienteDB.Fields.ID_CLIENTE + " = ?";
			    
			    String[] selectionClienteArgs = new String[] {
				    ClienteDB.CLIENTE_ITEM_TYPE, "" + cliente.getLong("idcliente")
			    };
			    
			    cursorCliente = cr.query(ClienteDB.CONTENT_URI, null, selectionCliente, selectionClienteArgs, null);
			    
			    ContentValues values = new ContentValues();
			    
			    if (cursorCliente.getCount() > 0) {
				
				// *** UPDATE CLIENTE ***\\
				
				Cliente updateCliente = new Cliente();
				updateCliente.setCitta(cliente.getString("citta"));
				updateCliente.setCodiceFiscale(cliente.getString("codicefiscale"));
				updateCliente.setEmail(cliente.getString("email"));
				updateCliente.setFax(cliente.getString("fax"));
				updateCliente.setIndirizzo(cliente.getString("indirizzo"));
				updateCliente.setInterno(cliente.getString("interno"));
				updateCliente.setNominativo(cliente.getString("nominativo"));
				updateCliente.setNote(cliente.getString("note"));
				updateCliente.setPartitaIVA(cliente.getString("partitaiva"));
				updateCliente.setReferente(cliente.getString("referente"));
				updateCliente.setRevisione(cliente.getLong("revisione"));
				updateCliente.setTelefono(cliente.getString("telefono"));
				updateCliente.setUfficio(cliente.getString("ufficio"));
				
				values = Cliente.updateSQL(updateCliente);
				
				cr.update(ClienteDB.CONTENT_URI, values,
					ClienteDB.Fields.TYPE + "=? AND " + ClienteDB.Fields.ID_CLIENTE + "=?", new String[] {
						ClienteDB.CLIENTE_ITEM_TYPE, "" + cliente.getLong("idcliente")
					});
				
				cursorCliente.close();
			    }
			    else {
				
				// *** INSERT CLIENTE ***\\
				
				Cliente newCliente = new Cliente();
				newCliente.setIdCliente(cliente.getLong("idcliente"));
				newCliente.setCitta(cliente.getString("citta"));
				newCliente.setCodiceFiscale(cliente.getString("codicefiscale"));
				newCliente.setEmail(cliente.getString("email"));
				newCliente.setFax(cliente.getString("fax"));
				newCliente.setIndirizzo(cliente.getString("indirizzo"));
				newCliente.setInterno(cliente.getString("interno"));
				newCliente.setNominativo(cliente.getString("nominativo"));
				newCliente.setNote(cliente.getString("note"));
				newCliente.setPartitaIVA(cliente.getString("partitaiva"));
				newCliente.setReferente(cliente.getString("referente"));
				newCliente.setRevisione(cliente.getLong("revisione"));
				newCliente.setTelefono(cliente.getString("telefono"));
				newCliente.setUfficio(cliente.getString("ufficio"));
				
				values = Cliente.insertSQL(newCliente);
				
				cr.insert(ClienteDB.CONTENT_URI, values);
				
				cursorCliente.close();
				
			    }
			}
			
			for (int k = 0; k < clientsDEL.length(); k++) {
			    
			    String where = ClienteDB.Fields.ID_CLIENTE + " = ? AND " + ClienteDB.Fields.TYPE + " = ?";
			    
			    String[] selectionArgs = new String[] {
				    "" + clientsDEL.getLong(k), ClienteDB.CLIENTE_ITEM_TYPE
			    };
			    
			    cr.delete(ClienteDB.CONTENT_URI, where, selectionArgs);
			    
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
		if (result == RESULT_OK) {
		    getInterventionsSyncro();
		}
		else {
		    
		    InterventixToast.makeToast(HomeActivity.this, toast_error_syncro_clients, Toast.LENGTH_LONG);
		    
		    setRefreshActionButtonState(false);
		}
	    }
	}.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
    }
    
    private void getInterventionsSyncro() {
	
	prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {
	    
	    @Override
	    protected void onPreExecute() {
		
	    };
	    
	    @Override
	    protected Integer doInBackground(Long... params) {
		
		String json_req = new String();
		
		ContentResolver cr = getContentResolver();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("revision", prefsLocal.getLong(Constants.REVISION_INTERVENTIONS, 0));
		
		int result = 0;
		
		long iduser = params[0];
		
		try {
		    json_req = JsonCR2.createRequest("interventions", "mysyncro", parameters, (int) iduser);
		    
		    System.out.println("Request mysyncro interventi\n" + json_req);
		    
		    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
		    
		    final String prefs_url = getResources().getString(string.prefs_key_url);
		    
		    final String url_string = prefsDefault.getString(prefs_url, null);
		    
		    JSONObject response = new JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());
		    
		    if (response != null && response.getString("response").equalsIgnoreCase("success")) {
			
			JSONObject data = response.getJSONObject("data");
			
			System.out.println("REVISIONE INTERVENTI " + data.getLong("revision"));
			
			final Editor editor = prefsLocal.edit();
			
			editor.putLong(Constants.REVISION_INTERVENTIONS, data.getLong("revision"));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    }).start();
			}
			
			JSONArray intervMOD = data.getJSONArray("mod");
			JSONArray intervDEL = data.getJSONArray("del");
			JSONArray interventions = data.getJSONArray("intervents");
			
			int cont = 0;
			
			// *** adding interventions that belong to
			// the current responsible ***\\
			if (intervMOD.length() > 0)
			    for (int i = 0; i < intervMOD.length(); ++i) {
				addInterventions((JSONObject) intervMOD.get(i), cont);
			    }
			
			// *** deleting interventions that not belong anymore to
			// the current responsible ***\\
			if (intervDEL.length() > 0)
			    for (int i = 0; i < intervDEL.length(); ++i) {
				
				long intervID = intervDEL.getLong(i);
				
				String where = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
				
				String[] selectionArgs = new String[] {
					InterventoDB.INTERVENTO_ITEM_TYPE, "" + intervID
				};
				
				cr.delete(InterventoDB.CONTENT_URI, where, selectionArgs);
				
				System.out.println("Eliminato l'intervento " + intervID);
				
			    }
			
			if (interventions.length() > 0)
			    for (int k = 0; k < interventions.length(); ++k) {
				
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
	    
	    private void addInterventions(JSONObject responseIntervs, int contProg) {
		
		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		
		Cursor cursorIntervento = null;
		
		try {
		    
		    String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    InterventoDB.INTERVENTO_ITEM_TYPE, "" + responseIntervs.getLong("idintervento")
		    };
		    
		    cursorIntervento = cr.query(InterventoDB.CONTENT_URI, null, selection, selectionArgs, null);
		    
		    if (cursorIntervento.getCount() == 0) {
			
			// *** INSERT INTERVENTO ***\\
			
			System.out.println("Inserimento intervento " + responseIntervs.getLong("idintervento"));
			
			Intervento newInterv = new Intervento();
			newInterv.setIdIntervento(responseIntervs.getLong("idintervento"));
			newInterv.setCostoAccessori(new BigDecimal(responseIntervs.getDouble("costoaccessori")));
			newInterv.setCostoComponenti(new BigDecimal(responseIntervs.getDouble("costocomponenti")));
			newInterv.setCostoManodopera(new BigDecimal(responseIntervs.getDouble("costomanodopera")));
			newInterv.setDataOra(responseIntervs.getLong("dataora"));
			newInterv.setFirma(responseIntervs.getString("firma"));
			newInterv.setIdCliente(responseIntervs.getLong("cliente"));
			newInterv.setIdTecnico(responseIntervs.getLong("tecnico"));
			newInterv.setImporto(new BigDecimal(responseIntervs.getDouble("importo")));
			newInterv.setIva(new BigDecimal(responseIntervs.getDouble("iva")));
			newInterv.setModalita(responseIntervs.getString("modalita"));
			newInterv.setMotivo(responseIntervs.getString("motivo"));
			newInterv.setNominativo(responseIntervs.getString("nominativo"));
			newInterv.setNote(responseIntervs.getString("note"));
			newInterv.setNumeroIntervento(responseIntervs.getLong("numero"));
			newInterv.setProdotto(responseIntervs.getString("prodotto"));
			newInterv.setRifFattura(responseIntervs.getString("riffattura"));
			newInterv.setRifScontrino(responseIntervs.getString("rifscontrino"));
			newInterv.setTipologia(responseIntervs.getString("tipologia"));
			newInterv.setTotale(new BigDecimal(responseIntervs.getDouble("totale")));
			newInterv.setSaldato(responseIntervs.getBoolean("saldato"));
			newInterv.setChiuso(responseIntervs.getBoolean("chiuso"));
			newInterv.setCancellato(responseIntervs.getBoolean("cancellato"));
			newInterv.setModificato(Constants.NUOVO_INTERVENTO);
			
			values = Intervento.insertSQL(newInterv);
			
			cr.insert(InterventoDB.CONTENT_URI, values);
			
			JSONArray dettagli_intervento = responseIntervs.getJSONArray("dettagliintervento");
			
			for (int cont = 0; cont < dettagli_intervento.length(); cont++) {
			    
			    JSONObject newDettInterv = dettagli_intervento.getJSONObject(cont);
			    
			    // *** INSERT DETTAGLIO INTERVENTO *** \\
			    
			    DettaglioIntervento dtInt = new DettaglioIntervento();
			    dtInt.setIdDettaglioIntervento(newDettInterv.getLong("iddettagliointervento"));
			    dtInt.setDescrizione(newDettInterv.getString("descrizione"));
			    dtInt.setIntervento(responseIntervs.getLong("idintervento"));
			    dtInt.setModificato(Constants.NUOVO_DETT_INTERVENTO);
			    dtInt.setOggetto(newDettInterv.getString("oggetto"));
			    dtInt.setTipo(newDettInterv.getString("tipo"));
			    dtInt.setInizio(newDettInterv.getLong("inizio"));
			    dtInt.setFine(newDettInterv.getLong("fine"));
			    dtInt.setTecnici(newDettInterv.getJSONArray("tecniciintervento"));
			    
			    values = DettaglioIntervento.insertSQL(dtInt);
			    
			    cr.insert(DettaglioInterventoDB.CONTENT_URI, values);
			}
		    }
		    else {
			
			// *** UPDATE INTERVENTO *** \\
			
			Intervento updateInterv = new Intervento();
			updateInterv.setCostoAccessori(new BigDecimal(responseIntervs.getDouble("costoaccessori")));
			updateInterv.setCostoComponenti(new BigDecimal(responseIntervs.getDouble("costocomponenti")));
			updateInterv.setCostoManodopera(new BigDecimal(responseIntervs.getDouble("costomanodopera")));
			updateInterv.setDataOra(responseIntervs.getLong("dataora"));
			updateInterv.setFirma(responseIntervs.getString("firma"));
			updateInterv.setIdCliente(responseIntervs.getLong("cliente"));
			updateInterv.setIdTecnico(responseIntervs.getLong("tecnico"));
			updateInterv.setImporto(new BigDecimal(responseIntervs.getDouble("importo")));
			updateInterv.setIva(new BigDecimal(responseIntervs.getDouble("iva")));
			updateInterv.setModalita(responseIntervs.getString("modalita"));
			updateInterv.setMotivo(responseIntervs.getString("motivo"));
			updateInterv.setNominativo(responseIntervs.getString("nominativo"));
			updateInterv.setNote(responseIntervs.getString("note"));
			updateInterv.setNumeroIntervento(responseIntervs.getLong("numero"));
			updateInterv.setProdotto(responseIntervs.getString("prodotto"));
			updateInterv.setRifFattura(responseIntervs.getString("riffattura"));
			updateInterv.setRifScontrino(responseIntervs.getString("rifscontrino"));
			updateInterv.setTipologia(responseIntervs.getString("tipologia"));
			updateInterv.setTotale(new BigDecimal(responseIntervs.getDouble("totale")));
			updateInterv.setSaldato(responseIntervs.getBoolean("saldato"));
			updateInterv.setChiuso(responseIntervs.getBoolean("chiuso"));
			updateInterv.setCancellato(responseIntervs.getBoolean("cancellato"));
			updateInterv.setModificato(Constants.NUOVO_INTERVENTO);
			
			values = Intervento.updateSQL(updateInterv);
			
			String where = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
			
			cr.update(InterventoDB.CONTENT_URI, values, where, selectionArgs);
			
			JSONArray dettagli_intervento = responseIntervs.getJSONArray("dettagliintervento");
			
			for (int cont = 0; cont < dettagli_intervento.length(); cont++) {
			    
			    JSONObject dettInterv = dettagli_intervento.getJSONObject(cont);
			    
			    // *** UPDATE DETTAGLIO INTERVENTO *** \\
			    
			    DettaglioIntervento dtInt = new DettaglioIntervento();
			    dtInt.setDescrizione(dettInterv.getString("descrizione"));
			    dtInt.setIntervento(responseIntervs.getLong("idintervento"));
			    dtInt.setModificato(Constants.NUOVO_DETT_INTERVENTO);
			    dtInt.setOggetto(dettInterv.getString("oggetto"));
			    dtInt.setTipo(dettInterv.getString("tipo"));
			    dtInt.setInizio(dettInterv.getLong("inizio"));
			    dtInt.setFine(dettInterv.getLong("fine"));
			    dtInt.setTecnici(dettInterv.getJSONArray("tecniciintervento"));
			    
			    values = DettaglioIntervento.updateSQL(dtInt);
			    
			    String selectionDettaglioIntervento = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
			    
			    String[] selectionDettIntervArgs = new String[] {
				    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + dettInterv.getLong("iddettagliointervento")
			    };
			    
			    cr.update(DettaglioInterventoDB.CONTENT_URI, values, selectionDettaglioIntervento, selectionDettIntervArgs);
			    
			}
		    }
		    
		    cursorIntervento.close();
		    
		}
		catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
	    }
	    
	    @Override
	    protected void onPostExecute(Integer result) {
		
		if (result == RESULT_OK) {
		    
		    String nominativo = null;
		    
		    prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor editor = prefsLocal.edit();
		    
		    try {
			nominativo = setNominativo();
			
			editor.putString(Constants.USER_NOMINATIVO, nominativo);
			getSupportActionBar().setTitle(nominativo);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    }).start();
			}
			
			setRefreshActionButtonState(false);
		    }
		    catch (InterruptedException e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		    catch (ExecutionException e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		}
		else {
		    
		    InterventixToast.makeToast(HomeActivity.this, toast_error_syncro_interventions, Toast.LENGTH_LONG);
		    
		    setRefreshActionButtonState(false);
		}
	    }
	    
	}.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
	
	String sortOrder = InterventoDB.Fields.DATA_ORA + " desc";
	
	Loader<Cursor> loader = new CursorLoader(this, InterventoDB.CONTENT_URI, HomeActivity.PROJECTION, HomeActivity.SELECTION, HomeActivity.SELECTION_ARGS, sortOrder);
	
	return loader;
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	
	mAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
	
	mAdapter.swapCursor(null);
    }
}
