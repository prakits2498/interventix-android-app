package com.federicocolantoni.projects.interventix.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.adapter.InterventionsAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity implements
					      LoaderCallbacks<Cursor> {
    
    private final static int MESSAGE_LOADER = 1;
    
    static final String[] PROJECTION = new String[] {
	    InterventoDB.Fields._ID, InterventoDB.Fields.NUMERO_INTERVENTO,
	    InterventoDB.Fields.CLIENTE, InterventoDB.Fields.ID_INTERVENTO,
	    InterventoDB.Fields.DATA_ORA
    };
    
    static final String SELECTION = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.CHIUSO + " = ?";
    
    static final String[] SELECTION_ARGS = new String[] {
	    InterventoDB.INTERVENTO_ITEM_TYPE, "0"
    };
    
    private InterventionsAdapter mAdapter;
    
    private Menu optionsMenu;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	BugSenseHandler.initAndStartSession(HomeActivity.this, Constants.API_KEY);
	
	setContentView(R.layout.activity_home);
	
	getUsersSyncro();
	
	ListView listOpen = (ListView) findViewById(R.id.list_interv_open);
	
	mAdapter = new InterventionsAdapter(this, null);
	
	listOpen.setAdapter(mAdapter);
	
	getSupportLoaderManager().initLoader(HomeActivity.MESSAGE_LOADER, null, this);
	
	TextView headerOpen = (TextView) findViewById(R.id.list_header_open);
	headerOpen.setText(R.string.interventi_aperti);
	
	listOpen.setOnItemClickListener(new OnItemClickListener() {
	    
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view,
				    int position, long id) {
		
		Bundle bundle = new Bundle();
		
		Cursor cur = (Cursor) adapter.getItemAtPosition(position);
		
		bundle.putLong(Constants.ID_INTERVENTO, cur.getLong(cur.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
		bundle.putLong(Constants.NUMERO_INTERVENTO, cur.getLong(cur.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO)));
		
		Intent intent = new Intent(HomeActivity.this, ViewInterventoActivity.class);
		
		intent.putExtras(bundle);
		
		startActivity(intent);
	    }
	});
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    
	    LogoutDialog logout = new LogoutDialog();
	    logout.show(getSupportFragmentManager(), Constants.LOGOUT_DIALOG_FRAGMENT);
	    return true;
	}
	
	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() {
	
	LogoutDialog logout = new LogoutDialog();
	logout.show(getSupportFragmentManager(), Constants.LOGOUT_DIALOG_FRAGMENT);
    }
    
    public static class LogoutDialog extends SherlockDialogFragment implements
								   OnClickListener {
	
	public LogoutDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder logout_dialog = new Builder(getSherlockActivity());
	    
	    logout_dialog.setTitle(getResources().getString(R.string.logout_title));
	    logout_dialog.setMessage(getResources().getString(R.string.logout_message));
	    logout_dialog.setIcon(R.drawable.ic_launcher);
	    
	    logout_dialog.setPositiveButton(getResources().getString(R.string.logout_positive_btn), this);
	    logout_dialog.setNegativeButton(getResources().getString(R.string.btn_cancel), this);
	    
	    return logout_dialog.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    if (DialogInterface.BUTTON_POSITIVE == which) {
		
		dialog.dismiss();
		getSherlockActivity().finish();
	    }
	    else {
		dialog.dismiss();
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	optionsMenu = menu;
	getSupportMenuInflater().inflate(R.menu.activity_home, menu);
	
	setRefreshActionButtonState(true);
	
	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		LogoutDialog logout = new LogoutDialog();
		logout.show(getSupportFragmentManager(), Constants.LOGOUT_DIALOG_FRAGMENT);
		break;
	    
	    case R.id.refresh_menu:
		
		getUsersSyncro();
		setRefreshActionButtonState(true);
		
		break;
	    
	    case R.id.add_menu:
		
		break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    private void setRefreshActionButtonState(final boolean refreshing) {
	if (optionsMenu != null) {
	    final MenuItem refreshItem = optionsMenu.findItem(R.id.refresh_menu);
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
    
    private String setNominativo() throws InterruptedException,
				  ExecutionException {
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	ManagedAsyncTask<Long, Void, String> nominativo = new ManagedAsyncTask<Long, Void, String>(HomeActivity.this) {
	    
	    @Override
	    protected String doInBackground(Long... params) {
		
		String res = null;
		
		ContentResolver cr = getContentResolver();
		
		String[] projection = new String[] {
			UtenteDB.Fields._ID, UtenteDB.Fields.NOME,
			UtenteDB.Fields.COGNOME
		};
		
		String selection = UtenteDB.Fields.TYPE + " = ? AND " + UtenteDB.Fields.ID_UTENTE + " = ?";
		
		String[] selectionArgs = new String[] {
			UtenteDB.UTENTE_ITEM_TYPE, "" + params[0]
		};
		
		Cursor cursor = cr.query(UtenteDB.CONTENT_URI, projection, selection, selectionArgs, null);
		
		if (cursor.getCount() == 1) {
		    cursor.moveToFirst();
		    
		    res = cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.NOME)) + " " + cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.COGNOME));
		}
		
		if (!cursor.isClosed()) {
		    cursor.close();
		}
		else {
		    System.out.println("Cursor for setNominativo is closed");
		}
		
		return res;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
		
	    };
	}.execute(prefs.getLong(Constants.USER_ID, 0l));
	
	return nominativo.get();
    }
    
    private void getUsersSyncro() {
	
	final SharedPreferences prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
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
		    
		    JSONObject json_resp = Utils.connectionForURL(json_req, url_string);
		    
		    System.out.println("RESPONSE SYNCRO USERS:\n" + json_resp.toJSONString());
		    
		    if (json_resp != null && json_resp.get("response").toString().equalsIgnoreCase("success")) {
			JSONObject data = (JSONObject) json_resp.get("data");
			
			System.out.println("REVISIONE UTENTI " + data.get("revision"));
			
			final Editor editor = prefsLocal.edit();
			
			editor.putLong(Constants.REVISION_USERS, (Long) data.get("revision"));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    });
			}
			
			JSONArray usersMOD = (JSONArray) data.get("mod");
			JSONArray usersDEL = (JSONArray) data.get("del");
			
			ContentValues values = new ContentValues();
			
			for (int i = 0; i < usersMOD.size(); i++) {
			    JSONObject obj = (JSONObject) usersMOD.get(i);
			    
			    System.out.println("INSERT MOD USERS");
			    
			    if ((Long) obj.get("idutente") != params[0]) {
				
				values.put(UtenteDB.Fields.ID_UTENTE, (Long) obj.get("idutente"));
				values.put(Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
				values.put(UtenteDB.Fields.NOME, (String) obj.get("nome"));
				values.put(UtenteDB.Fields.COGNOME, (String) obj.get("cognome"));
				values.put(UtenteDB.Fields.USERNAME, (String) obj.get("username"));
				values.put(UtenteDB.Fields.CANCELLATO, (Boolean) obj.get("cancellato"));
				values.put(UtenteDB.Fields.REVISIONE, (Long) obj.get("revisione"));
				values.put(UtenteDB.Fields.EMAIL, (String) obj.get("email"));
				values.put(UtenteDB.Fields.TIPO, (String) obj.get("tipo"));
				values.put(UtenteDB.Fields.CESTINATO, (Boolean) obj.get("cestinato"));
				
				cr.insert(UtenteDB.CONTENT_URI, values);
			    }
			}
			
			if (usersDEL.size() > 0) {
			    for (int k = 0; k < usersDEL.size(); k++) {
				
				String where = UtenteDB.Fields.ID_UTENTE + " = ? AND " + Fields.TYPE + " = ?";
				
				String[] selectionArgs = new String[] {
					"" + usersDEL.get(k),
					UtenteDB.UTENTE_ITEM_TYPE
				};
				
				cr.delete(UtenteDB.CONTENT_URI, where, selectionArgs);
				
			    }
			}
			else {
			    System.out.println("DEL USERS EMPTY");
			}
			
			result = Activity.RESULT_OK;
		    }
		    else {
			result = Activity.RESULT_CANCELED;
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
		
		if (result == Activity.RESULT_OK) {
		    
		    getClientsSyncro();
		}
		else {
		    
		    InterventixToast.makeToast(HomeActivity.this, "Si è verificato un errore nella sincronizzazione degli utenti.", Toast.LENGTH_LONG);
		    
		    setRefreshActionButtonState(false);
		}
	    }
	    
	}.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
    }
    
    private void getClientsSyncro() {
	
	final SharedPreferences prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
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
		    
		    // System.out.println("Request syncro clients\n" +
		    // json_req);
		    
		    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
		    
		    final String prefs_url = getResources().getString(string.prefs_key_url);
		    
		    final String url_string = prefsDefault.getString(prefs_url, null);
		    
		    JSONObject json_resp = Utils.connectionForURL(json_req, url_string);
		    
		    // System.out.println("Response syncro clients: \n"
		    // + json_resp.toJSONString());
		    
		    if (json_resp != null && json_resp.get("response").toString().equalsIgnoreCase("success")) {
			JSONObject data = (JSONObject) json_resp.get("data");
			
			System.out.println("REVISIONE CLIENTI " + data.get("revision"));
			
			final Editor editor = prefsLocal.edit();
			
			editor.putLong(Constants.REVISION_CLIENTS, (Long) data.get("revision"));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    });
			}
			
			JSONArray clientsMOD = (JSONArray) data.get("mod");
			JSONArray clientsDEL = (JSONArray) data.get("del");
			
			Cursor cursorCliente = null;
			
			for (int i = 0; i < clientsMOD.size(); i++) {
			    
			    JSONObject cliente = (JSONObject) clientsMOD.get(i);
			    
			    String selectionCliente = ClienteDB.Fields.TYPE + " = ? AND " + ClienteDB.Fields.ID_CLIENTE + " = ?";
			    
			    String[] selectionClienteArgs = new String[] {
				    ClienteDB.CLIENTE_ITEM_TYPE,
				    "" + cliente.get("idcliente")
			    };
			    
			    cursorCliente = cr.query(ClienteDB.CONTENT_URI, null, selectionCliente, selectionClienteArgs, null);
			    
			    if (cursorCliente.getCount() > 0) {
				cursorCliente.close();
				
			    }
			    else {
				
				// *** INSERT CLIENTE ***\\\
				ContentValues values = new ContentValues();
				
				values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
				values.put(ClienteDB.Fields.CANCELLATO, (Boolean) cliente.get("cancellato"));
				values.put(ClienteDB.Fields.CITTA, (String) cliente.get("citta"));
				values.put(ClienteDB.Fields.CODICE_FISCALE, (String) cliente.get("codicefiscale"));
				values.put(ClienteDB.Fields.EMAIL, (String) cliente.get("email"));
				values.put(ClienteDB.Fields.FAX, (String) cliente.get("fax"));
				values.put(ClienteDB.Fields.ID_CLIENTE, (Long) cliente.get("idcliente"));
				values.put(ClienteDB.Fields.INDIRIZZO, (String) cliente.get("indirizzo"));
				values.put(ClienteDB.Fields.INTERNO, (String) cliente.get("interno"));
				values.put(ClienteDB.Fields.NOMINATIVO, (String) cliente.get("nominativo"));
				values.put(ClienteDB.Fields.NOTE, (String) cliente.get("note"));
				values.put(ClienteDB.Fields.PARTITAIVA, (String) cliente.get("partitaiva"));
				values.put(ClienteDB.Fields.REFERENTE, (String) cliente.get("referente"));
				values.put(ClienteDB.Fields.REVISIONE, (Long) cliente.get("revisione"));
				values.put(ClienteDB.Fields.TELEFONO, (String) cliente.get("telefono"));
				values.put(ClienteDB.Fields.UFFICIO, (String) cliente.get("ufficio"));
				
				cr.insert(ClienteDB.CONTENT_URI, values);
				
				cursorCliente.close();
				
			    }
			}
			
			for (int k = 0; k < clientsDEL.size(); k++) {
			    
			    String where = ClienteDB.Fields.ID_CLIENTE + " = ? AND " + ClienteDB.Fields.TYPE + " = ?";
			    
			    String[] selectionArgs = new String[] {
				    "" + clientsDEL.get(k),
				    ClienteDB.CLIENTE_ITEM_TYPE
			    };
			    
			    cr.delete(ClienteDB.CONTENT_URI, where, selectionArgs);
			    
			}
			
			result = Activity.RESULT_OK;
		    }
		    else {
			result = Activity.RESULT_CANCELED;
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
		if (result == Activity.RESULT_OK) {
		    
		    getInterventionsSyncro();
		}
		else {
		    
		    InterventixToast.makeToast(HomeActivity.this, "Si è verificato un errore nella sincronizzazione dei clienti.", Toast.LENGTH_LONG);
		    
		    setRefreshActionButtonState(false);
		}
	    }
	}.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
    }
    
    private void getInterventionsSyncro() {
	
	final SharedPreferences prefsLocal = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
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
		    
		    JSONObject json_resp = Utils.connectionForURL(json_req, url_string);
		    
		    if (json_resp != null && json_resp.get("response").toString().equalsIgnoreCase("success")) {
			
			JSONObject data = (JSONObject) json_resp.get("data");
			
			System.out.println("REVISIONE INTERVENTI " + data.get("revision"));
			
			final Editor editor = prefsLocal.edit();
			
			editor.putLong(Constants.REVISION_INTERVENTIONS, (Long) data.get("revision"));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.commit();
				}
			    });
			}
			
			JSONArray intervMOD = (JSONArray) data.get("mod");
			JSONArray intervDEL = (JSONArray) data.get("del");
			JSONArray interventions = (JSONArray) data.get("intervents");
			
			int cont = 0;
			
			if (intervMOD.size() > 0) {
			    for (int i = 0; i < intervMOD.size(); ++i) {
				
				addInterventions((JSONObject) intervMOD.get(i), cont);
			    }
			}
			
			// *** deleting interventions that not belong anymore to
			// the current responsible ***\\
			if (intervDEL.size() > 0) {
			    for (int i = 0; i < intervDEL.size(); ++i) {
				
				long intervID = (Long) intervDEL.get(i);
				
				String where = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
				
				String[] selectionArgs = new String[] {
					InterventoDB.INTERVENTO_ITEM_TYPE,
					"" + intervID
				};
				
				cr.delete(InterventoDB.CONTENT_URI, where, selectionArgs);
				
				System.out.println("Eliminato l'intervento " + intervID);
				
			    }
			}
			
			for (int k = 0; k < interventions.size(); ++k) {
			    
			}
			
			result = Activity.RESULT_OK;
		    }
		    else {
			
			result = Activity.RESULT_CANCELED;
		    }
		    
		}
		catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
		
		return result;
	    }
	    
	    private void addInterventions(JSONObject responseIntervs,
					  int contProg) {
		
		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		
		Cursor cursorIntervento = null;
		Cursor cursorDettaglioIntervento = null;
		
		try {
		    
		    String selection = Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
		    
		    String[] selectionArgs = new String[] {
			    InterventoDB.INTERVENTO_ITEM_TYPE,
			    "" + responseIntervs.get("idintervento")
		    };
		    
		    cursorIntervento = cr.query(InterventoDB.CONTENT_URI, null, selection, selectionArgs, null);
		    
		    if (cursorIntervento.getCount() == 0) {
			
			JSONArray dettagli_intervento = (JSONArray) responseIntervs.get("dettagliintervento");
			
			// *** INSERT INTERVENTO ***\\
			
			System.out.println("Inserimento intervento " + responseIntervs.get("idintervento"));
			
			values.put(Fields.TYPE, InterventoDB.INTERVENTO_ITEM_TYPE);
			values.put(InterventoDB.Fields.ID_INTERVENTO, (Long) responseIntervs.get("idintervento"));
			values.put(InterventoDB.Fields.CANCELLATO, (Boolean) responseIntervs.get("cancellato"));
			values.put(InterventoDB.Fields.COSTO_ACCESSORI, (Double) responseIntervs.get("costoaccessori"));
			values.put(InterventoDB.Fields.COSTO_COMPONENTI, (Double) responseIntervs.get("costocomponenti"));
			values.put(InterventoDB.Fields.COSTO_MANODOPERA, (Double) responseIntervs.get("costomanodopera"));
			values.put(InterventoDB.Fields.DATA_ORA, (Long) responseIntervs.get("dataora"));
			values.put(InterventoDB.Fields.FIRMA, (String) responseIntervs.get("firma"));
			values.put(InterventoDB.Fields.CLIENTE, (Long) responseIntervs.get("cliente"));
			values.put(InterventoDB.Fields.IMPORTO, (Double) responseIntervs.get("importo"));
			values.put(InterventoDB.Fields.IVA, (Double) responseIntervs.get("iva"));
			values.put(InterventoDB.Fields.MODALITA, (String) responseIntervs.get("modalita"));
			values.put(InterventoDB.Fields.MODIFICATO, "");
			values.put(InterventoDB.Fields.MOTIVO, (String) responseIntervs.get("motivo"));
			values.put(InterventoDB.Fields.NOMINATIVO, (String) responseIntervs.get("nominativo"));
			values.put(InterventoDB.Fields.NOTE, (String) responseIntervs.get("note"));
			values.put(InterventoDB.Fields.NUMERO_INTERVENTO, (Long) responseIntervs.get("numero"));
			values.put(InterventoDB.Fields.PRODOTTO, (String) responseIntervs.get("prodotto"));
			values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, (String) responseIntervs.get("riffattura"));
			values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, (String) responseIntervs.get("rifscontrino"));
			values.put(InterventoDB.Fields.SALDATO, (Boolean) responseIntervs.get("saldato"));
			values.put(InterventoDB.Fields.TIPOLOGIA, (String) responseIntervs.get("tipologia"));
			values.put(InterventoDB.Fields.TOTALE, (Double) responseIntervs.get("totale"));
			values.put(InterventoDB.Fields.CHIUSO, (Boolean) responseIntervs.get("chiuso"));
			values.put(InterventoDB.Fields.TECNICO, (Long) responseIntervs.get("tecnico"));
			
			cr.insert(InterventoDB.CONTENT_URI, values);
			
			for (int cont = 0; cont < dettagli_intervento.size(); cont++) {
			    
			    JSONObject dettInterv = (JSONObject) dettagli_intervento.get(cont);
			    
			    String selectionDettaglioIntervento = Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
			    
			    String[] selectionDettIntervArgs = new String[] {
				    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
				    "" + dettInterv.get("iddettagliointervento")
			    };
			    
			    cursorDettaglioIntervento = cr.query(DettaglioInterventoDB.CONTENT_URI, null, selectionDettaglioIntervento, selectionDettIntervArgs, null);
			    
			    if (cursorDettaglioIntervento.getCount() == 0) {
				
				// *** INSERT DETTAGLIO INTERVENTO ***\\\
				
				values = new ContentValues();
				
				// System.out.println("Dettaglio intervento n° "
				// + cont + " inserito");
				
				values.put(Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
				values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, (String) dettInterv.get("descrizione"));
				values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, (Long) dettInterv.get("iddettagliointervento"));
				values.put(DettaglioInterventoDB.Fields.INTERVENTO, (Long) responseIntervs.get("idintervento"));
				values.put(DettaglioInterventoDB.Fields.MODIFICATO, "");
				values.put(DettaglioInterventoDB.Fields.OGGETTO, (String) dettInterv.get("oggetto"));
				values.put(DettaglioInterventoDB.Fields.TIPO, (String) dettInterv.get("tipo"));
				values.put(DettaglioInterventoDB.Fields.INIZIO, (Long) dettInterv.get("inizio"));
				values.put(DettaglioInterventoDB.Fields.FINE, (Long) dettInterv.get("fine"));
				
				JSONArray tecnici = (JSONArray) dettInterv.get("tecniciintervento");
				
				String tecniciInterv = new String();
				
				for (int i = 0; i < tecnici.size(); i++) {
				    tecniciInterv += "" + tecnici.get(i);
				    
				    if (i < tecnici.size()) {
					tecniciInterv += ",";
				    }
				}
				
				values.put(DettaglioInterventoDB.Fields.TECNICI, tecniciInterv);
				
				cr.insert(DettaglioInterventoDB.CONTENT_URI, values);
				
				cursorDettaglioIntervento.close();
			    }
			}
		    }
		    else {
			
			JSONArray dettagli_intervento = (JSONArray) responseIntervs.get("dettagliintervento");
			
			// *** UPDATE INTERVENTO ***\\
			
			values.put(InterventoDB.Fields.CANCELLATO, (Boolean) responseIntervs.get("cancellato"));
			values.put(InterventoDB.Fields.COSTO_ACCESSORI, (Double) responseIntervs.get("costoaccessori"));
			values.put(InterventoDB.Fields.COSTO_COMPONENTI, (Double) responseIntervs.get("costocomponenti"));
			values.put(InterventoDB.Fields.COSTO_MANODOPERA, (Double) responseIntervs.get("costomanodopera"));
			values.put(InterventoDB.Fields.DATA_ORA, (Long) responseIntervs.get("dataora"));
			values.put(InterventoDB.Fields.FIRMA, (String) responseIntervs.get("firma"));
			values.put(InterventoDB.Fields.CLIENTE, (Long) responseIntervs.get("cliente"));
			values.put(InterventoDB.Fields.IMPORTO, (Double) responseIntervs.get("importo"));
			values.put(InterventoDB.Fields.IVA, (Double) responseIntervs.get("iva"));
			values.put(InterventoDB.Fields.MODALITA, (String) responseIntervs.get("modalita"));
			values.put(InterventoDB.Fields.MOTIVO, (String) responseIntervs.get("motivo"));
			values.put(InterventoDB.Fields.NOMINATIVO, (String) responseIntervs.get("nominativo"));
			values.put(InterventoDB.Fields.NOTE, (String) responseIntervs.get("note"));
			values.put(InterventoDB.Fields.NUMERO_INTERVENTO, (Long) responseIntervs.get("numero"));
			values.put(InterventoDB.Fields.PRODOTTO, (String) responseIntervs.get("prodotto"));
			values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, (String) responseIntervs.get("riffattura"));
			values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, (String) responseIntervs.get("rifscontrino"));
			values.put(InterventoDB.Fields.SALDATO, (Boolean) responseIntervs.get("saldato"));
			values.put(InterventoDB.Fields.TIPOLOGIA, (String) responseIntervs.get("tipologia"));
			values.put(InterventoDB.Fields.TOTALE, (Double) responseIntervs.get("totale"));
			values.put(InterventoDB.Fields.CHIUSO, (Boolean) responseIntervs.get("chiuso"));
			values.put(InterventoDB.Fields.TECNICO, (Long) responseIntervs.get("tecnico"));
			
			String where = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
			
			cr.update(InterventoDB.CONTENT_URI, values, where, selectionArgs);
			
			for (int cont = 0; cont < dettagli_intervento.size(); cont++) {
			    
			    JSONObject dettInterv = (JSONObject) dettagli_intervento.get(cont);
			    
			    String selectionDettaglioIntervento = Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
			    
			    String[] selectionDettIntervArgs = new String[] {
				    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
				    "" + dettInterv.get("iddettagliointervento")
			    };
			    
			    // *** UPDATE DETTAGLIO INTERVENTO ***\\\
			    
			    values = new ContentValues();
			    
			    System.out.println("Dettaglio intervento n° " + cont + " inserito");
			    
			    values.put(Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
			    values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, (String) dettInterv.get("descrizione"));
			    values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, (Long) dettInterv.get("iddettagliointervento"));
			    values.put(DettaglioInterventoDB.Fields.INTERVENTO, (Long) responseIntervs.get("idintervento"));
			    values.put(DettaglioInterventoDB.Fields.OGGETTO, (String) dettInterv.get("oggetto"));
			    values.put(DettaglioInterventoDB.Fields.TIPO, (String) dettInterv.get("tipo"));
			    values.put(DettaglioInterventoDB.Fields.INIZIO, (Long) dettInterv.get("inizio"));
			    values.put(DettaglioInterventoDB.Fields.FINE, (Long) dettInterv.get("fine"));
			    
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
		
		if (result == Activity.RESULT_OK) {
		    
		    String nominativo = null;
		    
		    final SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    try {
			nominativo = setNominativo();
			
			edit.putString(Constants.USER_NOMINATIVO, nominativo);
			getSupportActionBar().setTitle(nominativo);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    edit.apply();
			}
			else {
			    
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    edit.commit();
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
		    
		    InterventixToast.makeToast(HomeActivity.this, "Si è verificato un errore nella sincronizzazione degli interventi.", Toast.LENGTH_LONG);
		    
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
