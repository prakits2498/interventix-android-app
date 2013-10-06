package com.federicocolantoni.projects.interventix.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import multiface.crypto.cr2.JsonCR2;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.federicocolantoni.projects.interventix.adapter.InterventiAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.Utils;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity implements LoaderCallbacks<Cursor> {
    
    private final static int MESSAGE_LOADER = 1;
    
    static final String[] PROJECTION = new String[] {
	    InterventoDB.Fields._ID, InterventoDB.Fields.NUMERO_INTERVENTO, InterventoDB.Fields.CLIENTE, InterventoDB.Fields.ID_INTERVENTO, InterventoDB.Fields.DATA_ORA
    };
    
    static final String SELECTION = InterventoDB.Fields.TYPE + " =? AND " + InterventoDB.Fields.CHIUSO + " =?";
    
    static final String[] SELECTION_ARGS = new String[] {
	    InterventoDB.INTERVENTO_ITEM_TYPE, "0"
    };
    
    private InterventiAdapter mAdapter;
    
    private Menu optionsMenu;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setContentView(R.layout.activity_home);
	
	getUsersSyncro();
	
	ListView listOpen = (ListView) findViewById(R.id.list_interv_open);
	
	mAdapter = new InterventiAdapter(this, null);
	
	listOpen.setAdapter(mAdapter);
	
	getSupportLoaderManager().initLoader(HomeActivity.MESSAGE_LOADER, null, this);
	
	TextView headerOpen = (TextView) findViewById(R.id.list_header_open);
	headerOpen.setText(R.string.interventi_aperti);
	
	listOpen.setOnItemClickListener(new OnItemClickListener() {
	    
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		
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
    
    public static class LogoutDialog extends SherlockDialogFragment implements OnClickListener {
	
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
	getSupportMenuInflater().inflate(R.menu.menu_home, menu);
	
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
	    if (refreshItem != null)
		if (refreshing) {
		    refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
		}
		else {
		    refreshItem.setActionView(null);
		}
	}
    }
    
    private String setNominativo() throws InterruptedException, ExecutionException {
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	ManagedAsyncTask<Long, Void, String> nominativo = new ManagedAsyncTask<Long, Void, String>(HomeActivity.this) {
	    
	    @Override
	    protected String doInBackground(Long... params) {
		
		String res = null;
		
		ContentResolver cr = getContentResolver();
		
		String[] projection = new String[] {
			UtenteDB.Fields._ID, UtenteDB.Fields.NOME, UtenteDB.Fields.COGNOME
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
		    
		    JSONObject response = new org.json.JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());
		    // JSONObject json_resp = Utils.connectionForURL(json_req,
		    // url_string);
		    
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
				
				values.put(UtenteDB.Fields.ID_UTENTE, obj.getLong("idutente"));
				values.put(Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
				values.put(UtenteDB.Fields.NOME, obj.getString("nome"));
				values.put(UtenteDB.Fields.COGNOME, obj.getString("cognome"));
				values.put(UtenteDB.Fields.USERNAME, obj.getString("username"));
				values.put(UtenteDB.Fields.CANCELLATO, obj.getBoolean("cancellato"));
				values.put(UtenteDB.Fields.REVISIONE, (Long) obj.getLong("revisione"));
				values.put(UtenteDB.Fields.EMAIL, obj.getString("email"));
				values.put(UtenteDB.Fields.TIPO, obj.getString("tipo"));
				values.put(UtenteDB.Fields.CESTINATO, obj.getBoolean("cestinato"));
				
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
		    
		    JSONObject response = new JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());
		    
		    // JSONObject json_resp = Utils.connectionForURL(json_req,
		    // url_string);
		    
		    // System.out.println("Response syncro clients: \n"
		    // + json_resp.toJSONString());
		    
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
			    
			    if (cursorCliente.getCount() > 0) {
				
				// *** UPDATE CLIENTE ***\\\
				ContentValues values = new ContentValues();
				
				values.put(ClienteDB.Fields.CITTA, cliente.getString("citta"));
				values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getString("codicefiscale"));
				values.put(ClienteDB.Fields.EMAIL, cliente.getString("email"));
				values.put(ClienteDB.Fields.FAX, cliente.getString("fax"));
				values.put(ClienteDB.Fields.INDIRIZZO, cliente.getString("indirizzo"));
				values.put(ClienteDB.Fields.INTERNO, cliente.getString("interno"));
				values.put(ClienteDB.Fields.NOMINATIVO, cliente.getString("nominativo"));
				values.put(ClienteDB.Fields.NOTE, cliente.getString("note"));
				values.put(ClienteDB.Fields.PARTITAIVA, cliente.getString("partitaiva"));
				values.put(ClienteDB.Fields.REFERENTE, cliente.getString("referente"));
				values.put(ClienteDB.Fields.REVISIONE, cliente.getLong("revisione"));
				values.put(ClienteDB.Fields.TELEFONO, cliente.getString("telefono"));
				values.put(ClienteDB.Fields.UFFICIO, cliente.getString("ufficio"));
				
				cr.update(ClienteDB.CONTENT_URI, values,
					ClienteDB.Fields.TYPE + "=? AND " + ClienteDB.Fields.ID_CLIENTE + "=?", new String[] {
						ClienteDB.CLIENTE_ITEM_TYPE, "" + cliente.getLong("idcliente")
					});
				
				cursorCliente.close();
			    }
			    else {
				
				// *** INSERT CLIENTE ***\\\
				ContentValues values = new ContentValues();
				
				values.put(ClienteDB.Fields.ID_CLIENTE, cliente.getLong("idcliente"));
				values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
				values.put(ClienteDB.Fields.CITTA, cliente.getString("citta"));
				values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getString("codicefiscale"));
				values.put(ClienteDB.Fields.EMAIL, cliente.getString("email"));
				values.put(ClienteDB.Fields.FAX, cliente.getString("fax"));
				values.put(ClienteDB.Fields.INDIRIZZO, cliente.getString("indirizzo"));
				values.put(ClienteDB.Fields.INTERNO, cliente.getString("interno"));
				values.put(ClienteDB.Fields.NOMINATIVO, cliente.getString("nominativo"));
				values.put(ClienteDB.Fields.NOTE, cliente.getString("note"));
				values.put(ClienteDB.Fields.PARTITAIVA, cliente.getString("partitaiva"));
				values.put(ClienteDB.Fields.REFERENTE, cliente.getString("referente"));
				values.put(ClienteDB.Fields.REVISIONE, cliente.getLong("revisione"));
				values.put(ClienteDB.Fields.TELEFONO, cliente.getString("telefono"));
				values.put(ClienteDB.Fields.UFFICIO, cliente.getString("ufficio"));
				
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
		    
		    JSONObject response = new JSONObject(Utils.connectionForURL(json_req, url_string).toJSONString());
		    
		    // JSONObject json_resp = Utils.connectionForURL(json_req,
		    // url_string);
		    
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
			
			values.put(InterventoDB.Fields.ID_INTERVENTO, responseIntervs.getLong("idintervento"));
			values.put(Fields.TYPE, InterventoDB.INTERVENTO_ITEM_TYPE);
			values.put(InterventoDB.Fields.CANCELLATO, responseIntervs.getBoolean("cancellato"));
			values.put(InterventoDB.Fields.COSTO_ACCESSORI, responseIntervs.getDouble("costoaccessori"));
			values.put(InterventoDB.Fields.COSTO_COMPONENTI, responseIntervs.getDouble("costocomponenti"));
			values.put(InterventoDB.Fields.COSTO_MANODOPERA, responseIntervs.getDouble("costomanodopera"));
			values.put(InterventoDB.Fields.DATA_ORA, (Long) responseIntervs.getLong("dataora"));
			values.put(InterventoDB.Fields.FIRMA, responseIntervs.getString("firma"));
			values.put(InterventoDB.Fields.CLIENTE, responseIntervs.getLong("cliente"));
			values.put(InterventoDB.Fields.IMPORTO, responseIntervs.getDouble("importo"));
			values.put(InterventoDB.Fields.IVA, responseIntervs.getDouble("iva"));
			values.put(InterventoDB.Fields.MODALITA, responseIntervs.getString("modalita"));
			values.put(InterventoDB.Fields.MODIFICATO, "N");
			values.put(InterventoDB.Fields.MOTIVO, responseIntervs.getString("motivo"));
			values.put(InterventoDB.Fields.NOMINATIVO, responseIntervs.getString("nominativo"));
			values.put(InterventoDB.Fields.NOTE, responseIntervs.getString("note"));
			values.put(InterventoDB.Fields.NUMERO_INTERVENTO, responseIntervs.getLong("numero"));
			values.put(InterventoDB.Fields.PRODOTTO, responseIntervs.getString("prodotto"));
			values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, responseIntervs.getString("riffattura"));
			values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, responseIntervs.getString("rifscontrino"));
			values.put(InterventoDB.Fields.SALDATO, responseIntervs.getBoolean("saldato"));
			values.put(InterventoDB.Fields.TIPOLOGIA, responseIntervs.getString("tipologia"));
			values.put(InterventoDB.Fields.TOTALE, responseIntervs.getDouble("totale"));
			values.put(InterventoDB.Fields.CHIUSO, responseIntervs.getBoolean("chiuso"));
			values.put(InterventoDB.Fields.TECNICO, responseIntervs.getLong("tecnico"));
			
			cr.insert(InterventoDB.CONTENT_URI, values);
			
			JSONArray dettagli_intervento = responseIntervs.getJSONArray("dettagliintervento");
			
			for (int cont = 0; cont < dettagli_intervento.length(); cont++) {
			    
			    JSONObject dettInterv = dettagli_intervento.getJSONObject(cont);
			    
			    // *** INSERT DETTAGLIO INTERVENTO *** \\
			    
			    values = new ContentValues();
			    
			    // System.out.println("Dettaglio intervento n° "
			    // + cont + " inserito");
			    
			    values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, dettInterv.getLong("iddettagliointervento"));
			    values.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
			    values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettInterv.getString("descrizione"));
			    values.put(DettaglioInterventoDB.Fields.INTERVENTO, responseIntervs.getLong("idintervento"));
			    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "N");
			    values.put(DettaglioInterventoDB.Fields.OGGETTO, dettInterv.getString("oggetto"));
			    values.put(DettaglioInterventoDB.Fields.TIPO, dettInterv.getString("tipo"));
			    values.put(DettaglioInterventoDB.Fields.INIZIO, dettInterv.getLong("inizio"));
			    values.put(DettaglioInterventoDB.Fields.FINE, dettInterv.getLong("fine"));
			    values.put(DettaglioInterventoDB.Fields.TECNICI, dettInterv.getJSONArray("tecniciintervento").toString());
			    
			    cr.insert(DettaglioInterventoDB.CONTENT_URI, values);
			}
		    }
		    else {
			
			// *** UPDATE INTERVENTO *** \\
			
			values.put(InterventoDB.Fields.CANCELLATO, responseIntervs.getBoolean("cancellato"));
			values.put(InterventoDB.Fields.COSTO_ACCESSORI, responseIntervs.getDouble("costoaccessori"));
			values.put(InterventoDB.Fields.COSTO_COMPONENTI, responseIntervs.getDouble("costocomponenti"));
			values.put(InterventoDB.Fields.COSTO_MANODOPERA, responseIntervs.getDouble("costomanodopera"));
			values.put(InterventoDB.Fields.DATA_ORA, responseIntervs.getLong("dataora"));
			values.put(InterventoDB.Fields.FIRMA, responseIntervs.getString("firma"));
			values.put(InterventoDB.Fields.CLIENTE, responseIntervs.getLong("cliente"));
			values.put(InterventoDB.Fields.IMPORTO, responseIntervs.getDouble("importo"));
			values.put(InterventoDB.Fields.IVA, responseIntervs.getDouble("iva"));
			values.put(InterventoDB.Fields.MODALITA, responseIntervs.getString("modalita"));
			values.put(InterventoDB.Fields.MODIFICATO, "N");
			values.put(InterventoDB.Fields.MOTIVO, responseIntervs.getString("motivo"));
			values.put(InterventoDB.Fields.NOMINATIVO, responseIntervs.getString("nominativo"));
			values.put(InterventoDB.Fields.NOTE, responseIntervs.getString("note"));
			values.put(InterventoDB.Fields.NUMERO_INTERVENTO, responseIntervs.getLong("numero"));
			values.put(InterventoDB.Fields.PRODOTTO, responseIntervs.getString("prodotto"));
			values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, responseIntervs.getString("riffattura"));
			values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, responseIntervs.getString("rifscontrino"));
			values.put(InterventoDB.Fields.SALDATO, responseIntervs.getBoolean("saldato"));
			values.put(InterventoDB.Fields.TIPOLOGIA, responseIntervs.getString("tipologia"));
			values.put(InterventoDB.Fields.TOTALE, responseIntervs.getDouble("totale"));
			values.put(InterventoDB.Fields.CHIUSO, responseIntervs.getBoolean("chiuso"));
			values.put(InterventoDB.Fields.TECNICO, responseIntervs.getLong("tecnico"));
			
			String where = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
			
			cr.update(InterventoDB.CONTENT_URI, values, where, selectionArgs);
			
			JSONArray dettagli_intervento = responseIntervs.getJSONArray("dettagliintervento");
			
			for (int cont = 0; cont < dettagli_intervento.length(); cont++) {
			    
			    JSONObject dettInterv = dettagli_intervento.getJSONObject(cont);
			    
			    // *** UPDATE DETTAGLIO INTERVENTO *** \\
			    
			    values = new ContentValues();
			    
			    // System.out.println("Dettaglio intervento n° " +
			    // cont + " inserito");
			    
			    values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettInterv.getString("descrizione"));
			    values.put(DettaglioInterventoDB.Fields.INTERVENTO, responseIntervs.getLong("idintervento"));
			    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "N");
			    values.put(DettaglioInterventoDB.Fields.OGGETTO, dettInterv.getString("oggetto"));
			    values.put(DettaglioInterventoDB.Fields.TIPO, dettInterv.getString("tipo"));
			    values.put(DettaglioInterventoDB.Fields.INIZIO, dettInterv.getLong("inizio"));
			    values.put(DettaglioInterventoDB.Fields.FINE, dettInterv.getLong("fine"));
			    values.put(DettaglioInterventoDB.Fields.TECNICI, dettInterv.getJSONArray("tecniciintervento").toString());
			    
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
		
		if (result == Activity.RESULT_OK) {
		    
		    String nominativo = null;
		    
		    final SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor editor = prefs.edit();
		    
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
