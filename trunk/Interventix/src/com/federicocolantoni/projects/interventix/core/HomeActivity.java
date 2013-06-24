
package com.federicocolantoni.projects.interventix.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

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
import com.federicocolantoni.projects.interventix.data.DBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.DBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.intervento.Cliente;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.utils.GetCliente;
import com.federicocolantoni.projects.interventix.view.ViewInterventoActivity;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.slezica.tools.async.ManagedAsyncTask;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

// import org.acra.ACRA;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity implements RefreshActionListener {

    private RefreshActionItem mRefreshActionItem;

    private int numOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.activity_home);

	String nominativo = null;

	try {
	    numberOfBackgroundOperations();
	} catch (InterruptedException e1) {
	    e1.printStackTrace();
	} catch (ExecutionException e1) {
	    e1.printStackTrace();
	}

	final SharedPreferences prefs = getSharedPreferences(
		Constants.PREFERENCES, Activity.MODE_PRIVATE);

	final Editor edit = prefs.edit();

	try {
	    nominativo = setNominativo();

	    edit.putString(Constants.USER_NOMINATIVO, nominativo);
	    getSupportActionBar().setTitle(nominativo);

	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		edit.apply();
	    } else {

		new Thread(new Runnable() {

		    @Override
		    public void run() {

			edit.commit();
		    }
		}).start();
	    }
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	//	retrieveInterventionsFromServer();

	List<Intervento> listInterventionsOpen = null;
	InterventionsAdapter adapter = null;

	try {
	    listInterventionsOpen = getUserInterventions();

	    adapter = new InterventionsAdapter(this, listInterventionsOpen);

	    adapter.notifyDataSetChanged();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	ListView listOpen = (ListView) findViewById(R.id.list_interv_open);

	TextView headerOpen = (TextView) findViewById(R.id.list_header_open);
	headerOpen.setText(R.string.interventi_aperti);

	listOpen.setAdapter(adapter);

	listOpen.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view,
		    int position, long id) {

		Intervento interv = (Intervento) adapter
			.getItemAtPosition(position);

		System.out.println("Modalità: " + interv.getmModalita());

		Cliente cliente = new Cliente();

		GetCliente cl = new GetCliente(HomeActivity.this);
		cl.execute(interv.getmIdCliente());

		try {
		    cliente = cl.get();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} catch (ExecutionException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		Toast.makeText(
			HomeActivity.this,
			"Hai selezionato l'intervento "
				+ interv.getmIdIntervento(), Toast.LENGTH_SHORT)
			.show();

		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.INTERVENTO, interv);
		bundle.putSerializable(Constants.CLIENTE, cliente);

		Intent intent = new Intent(HomeActivity.this,
			ViewInterventoActivity.class);

		intent.putExtras(bundle);

		startActivity(intent);
	    }
	});
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {

	    LogoutDialog logout = new LogoutDialog();
	    logout.show(getSupportFragmentManager(),
		    Constants.LOGOUT_DIALOG_FRAGMENT);
	    return true;
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

	LogoutDialog logout = new LogoutDialog();
	logout.show(getSupportFragmentManager(),
		Constants.LOGOUT_DIALOG_FRAGMENT);
    }

    public static class LogoutDialog extends SherlockDialogFragment implements
	    OnClickListener {

	public LogoutDialog() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder logout_dialog = new Builder(getActivity());

	    logout_dialog.setTitle(getResources().getString(
		    R.string.logout_title));
	    logout_dialog.setMessage(getResources().getString(
		    R.string.logout_message));

	    logout_dialog.setPositiveButton(
		    getResources().getString(R.string.logout_positive_btn),
		    this);
	    logout_dialog.setNegativeButton(
		    getResources().getString(R.string.logout_negative_btn),
		    this);

	    return logout_dialog.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    if (DialogInterface.BUTTON_POSITIVE == which) {
		SharedPreferences prefs = getActivity().getSharedPreferences(
			Constants.PREFERENCES, Context.MODE_PRIVATE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		    prefs.edit().clear().apply();
		} else {
		    final Editor editor = prefs.edit();
		    editor.clear();

		    new Thread(new Runnable() {

			@Override
			public void run() {

			    editor.commit();
			}
		    }).start();
		}

		dialog.dismiss();
		getActivity().finish();
	    } else {
		dialog.dismiss();
	    }
	}
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

	MenuItem item = menu.findItem(R.id.refresh_menu);

	if (item != null) {
	    //	    mRefreshActionItem = (RefreshActionItem) item.getActionView();
	    //	    mRefreshActionItem.setMenuItem(item);
	    //	    try {
	    //		mRefreshActionItem.setMax(numberOfBackgroundOperations());
	    //	    } catch (InterruptedException e) {
	    //		e.printStackTrace();
	    //		BugSenseHandler.sendException(e);
	    //	    } catch (ExecutionException e) {
	    //		e.printStackTrace();
	    //		BugSenseHandler.sendException(e);
	    //	    }
	    //	    mRefreshActionItem.setMax(numOps);
	    //	    mRefreshActionItem.setRefreshActionListener(this);
	    //	    mRefreshActionItem
	    //		    .setProgressIndicatorType(ProgressIndicatorType.PIE);
	}

	retrieveInterventionsFromServer();

	return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	getSupportMenuInflater().inflate(R.menu.activity_home, menu);

	return true;
    }

    private void loadData() {

	mRefreshActionItem.setMax(100);
	mRefreshActionItem.showProgress(true);

	new ManagedAsyncTask<Void, Void, Void>(HomeActivity.this) {

	    @Override
	    protected Void doInBackground(Void... params) {

		for (int i = 0; i <= 100; ++i) {
		    try {
			Thread.sleep(20);
			mRefreshActionItem.setProgress(i);
		    } catch (InterruptedException e) {
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		}

		return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {

		mRefreshActionItem.showProgress(false);
	    }
	}.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {

	    case android.R.id.home:
		//		SharedPreferences prefs = getSharedPreferences(
		//			Constants.PREFERENCES, Context.MODE_PRIVATE);
		//
		//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		//		    prefs.edit().clear().apply();
		//		} else {
		//		    final Editor editor = prefs.edit();
		//		    editor.clear();
		//
		//		    new Thread(new Runnable() {
		//
		//			@Override
		//			public void run() {
		//
		//			    editor.commit();
		//			}
		//		    }).start();
		//		}
		//
		//		this.finish();

		LogoutDialog logout = new LogoutDialog();
		logout.show(getSupportFragmentManager(),
			Constants.LOGOUT_DIALOG_FRAGMENT);
	}

	return super.onOptionsItemSelected(item);
    }

    private String setNominativo() throws InterruptedException,
	    ExecutionException {

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	ManagedAsyncTask<Long, Void, String> nominativo = new ManagedAsyncTask<Long, Void, String>(
		HomeActivity.this) {

	    @Override
	    protected String doInBackground(Long... params) {

		String res = null;

		ContentResolver cr = getContentResolver();

		String[] projection = new String[] { UtenteDB.Fields.NOME,
			UtenteDB.Fields.COGNOME };

		String selection = UtenteDB.Fields.TYPE + "='"
			+ UtenteDB.UTENTE_ITEM_TYPE + "' AND "
			+ UtenteDB.Fields.ID_UTENTE + "=" + params[0];

		Cursor cursor = cr.query(UtenteDB.CONTENT_URI, projection,
			selection, null, null);

		if (cursor.getCount() == 1) {
		    cursor.moveToFirst();

		    res = cursor.getString(cursor
			    .getColumnIndex(UtenteDB.Fields.NOME))
			    + " "
			    + cursor.getString(cursor
				    .getColumnIndex(UtenteDB.Fields.COGNOME));
		}

		if (!cursor.isClosed()) {
		    cursor.close();
		} else {
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

    private void retrieveInterventionsFromServer() {

	final SharedPreferences prefsLocal = getSharedPreferences(
		Constants.PREFERENCES, Context.MODE_PRIVATE);

	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {

	    //	    private ProgressDialog progress;

	    @Override
	    protected void onPreExecute() {

		//		progress = new ProgressDialog(HomeActivity.this);
		//		progress.setIndeterminate(true);
		//		progress.setMessage("Attendere prego. Setup dell'applicazione in corso");
		//		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		//
		//		progress.show();

		if (mRefreshActionItem != null) {
		    System.out
			    .println("RefreshActionItem is not null and progress is true");
		    mRefreshActionItem.showProgress(true);
		}
	    }

	    @Override
	    protected Integer doInBackground(Long... params) {

		String json_req = new String();

		ContentResolver cr = getContentResolver();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("revision",
			prefsLocal.getLong(Constants.REVISION, 0));

		int result = 0;

		long iduser = params[0];

		try {
		    json_req = JsonCR2.createRequest("interventions",
			    "mysyncro", parameters, (int) iduser);

		    final SharedPreferences prefsDefault = PreferenceManager
			    .getDefaultSharedPreferences(HomeActivity.this);

		    System.out.println("Request mysyncro interventi:\n"
			    + json_req);

		    final String prefs_url = getResources().getString(
			    string.prefs_key_url);

		    final String url = prefsDefault.getString(prefs_url, null);

		    final AndroidHttpClient request = new AndroidHttpClient(url);
		    request.setMaxRetries(5);

		    ParameterMap paramMap = new ParameterMap();
		    paramMap.add("DATA", json_req);

		    HttpResponse response;
		    response = request.post("", paramMap);

		    JSONObject json_resp = JsonCR2.read(response
			    .getBodyAsString());

		    if (json_resp.get("response").toString()
			    .equalsIgnoreCase("success")) {

			JSONObject data = (JSONObject) json_resp.get("data");

			System.out.println("REVISIONE " + data.get("revision"));

			final Editor editor = prefsLocal.edit();

			editor.putLong(Constants.REVISION,
				(Long) data.get("revision"));

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    editor.apply();
			} else {
			    new Thread(new Runnable() {

				@Override
				public void run() {

				    editor.commit();
				}
			    });
			}

			JSONArray interventions = (JSONArray) data
				.get("intervents");

			numOps += interventions.size();

			//*** getting informations from server for the k-th intervention ***\\
			for (int k = 0; k < interventions.size(); k++) {

			    Map<String, Object> parametersIntervention = new HashMap<String, Object>();
			    parametersIntervention.put("id",
				    interventions.get(k));

			    String json_req_single_interv = JsonCR2
				    .createRequest("interventions", "get",
					    parametersIntervention,
					    (int) iduser);

			    AndroidHttpClient requestSingleIntervention = new AndroidHttpClient(
				    url);
			    requestSingleIntervention.setMaxRetries(5);

			    ParameterMap paramMapSingleInterv = new ParameterMap();
			    paramMapSingleInterv.add("DATA",
				    json_req_single_interv);

			    HttpResponse responseSingleInterv = requestSingleIntervention
				    .post("", paramMapSingleInterv);

			    addSingleIntervention(responseSingleInterv);

			    if (mRefreshActionItem != null) {
				mRefreshActionItem.setProgress(k);
			    }
			}

			JSONArray intervDEL = (JSONArray) data.get("del");

			numOps += intervDEL.size();

			//*** deleting interventions that not belong anymore to the current responsible ***\\
			if (intervDEL.size() > 0) {
			    for (int i = 0; i < intervDEL.size(); i++) {

				long intervID = (Long) intervDEL.get(i);

				String where = InterventoDB.Fields.TYPE + "='"
					+ InterventoDB.INTERVENTO_ITEM_TYPE
					+ "' AND "
					+ InterventoDB.Fields.ID_INTERVENTO
					+ "=" + intervID;

				cr.delete(InterventoDB.CONTENT_URI, where, null);

				if (mRefreshActionItem != null) {
				    mRefreshActionItem.setProgress(i);
				}
			    }
			}

			JSONArray intervMOD = (JSONArray) data.get("mod");

			numOps += intervMOD.size();

			if (intervMOD.size() > 0) {
			    for (int i = 0; i < intervMOD.size(); i++) {

				if (mRefreshActionItem != null) {
				    mRefreshActionItem.setProgress(i);
				}
			    }
			}

			result = Activity.RESULT_OK;
			//			progress.dismiss();

		    } else {
			result = Activity.RESULT_CANCELED;
			//			progress.dismiss();
		    }

		} catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		return result;
	    }

	    private void addSingleIntervention(HttpResponse responseSingleInterv) {

		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();

		Cursor cursorCliente = null;
		Cursor cursorIntervento = null;

		try {
		    JSONObject json_resp = JsonCR2.read(responseSingleInterv
			    .getBodyAsString());

		    JSONArray interventions = (JSONArray) json_resp.get("data");

		    for (int i = 0; i < interventions.size(); i++) {

			JSONObject intervento = (JSONObject) interventions
				.get(i);

			String selection = InterventoDB.Fields.TYPE + "='"
				+ InterventoDB.INTERVENTO_ITEM_TYPE + "' AND "
				+ InterventoDB.Fields.ID_INTERVENTO + "="
				+ intervento.get("idintervento");

			cursorIntervento = cr.query(InterventoDB.CONTENT_URI,
				null, selection, null, null);

			if (cursorIntervento.getCount() == 0) {

			    JSONObject cliente = (JSONObject) intervento
				    .get("cliente");

			    //*** INSERT INTERVENTO ***\\

			    System.out.println("Inserimento intervento "
				    + intervento.get("idintervento"));

			    values.put(InterventoDB.Fields.TYPE,
				    InterventoDB.INTERVENTO_ITEM_TYPE);
			    values.put(InterventoDB.Fields.ID_INTERVENTO,
				    (Long) intervento.get("idintervento"));
			    values.put(InterventoDB.Fields.CANCELLATO,
				    (Boolean) intervento.get("cancellato"));
			    values.put(InterventoDB.Fields.COSTO_ACCESSORI,
				    (Double) intervento.get("costoaccessori"));
			    values.put(InterventoDB.Fields.COSTO_COMPONENTI,
				    (Double) intervento.get("costocomponenti"));
			    values.put(InterventoDB.Fields.COSTO_MANODOPERA,
				    (Double) intervento.get("costomanodopera"));
			    values.put(InterventoDB.Fields.DATA_ORA,
				    (Long) intervento.get("dataora"));
			    values.put(InterventoDB.Fields.FIRMA,
				    (String) intervento.get("firma"));
			    values.put(InterventoDB.Fields.CLIENTE,
				    (Long) cliente.get("idcliente"));
			    values.put(InterventoDB.Fields.IMPORTO,
				    (Double) intervento.get("importo"));
			    values.put(InterventoDB.Fields.IVA,
				    (Double) intervento.get("iva"));
			    values.put(InterventoDB.Fields.MODALITA,
				    (String) intervento.get("modalita"));
			    values.put(InterventoDB.Fields.MOTIVO,
				    (String) intervento.get("motivo"));
			    values.put(InterventoDB.Fields.NOMINATIVO,
				    (String) intervento.get("nominativo"));
			    values.put(InterventoDB.Fields.NOTE,
				    (String) intervento.get("note"));
			    values.put(InterventoDB.Fields.PRODOTTO,
				    (String) intervento.get("prodotto"));
			    values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA,
				    (String) intervento.get("riffattura"));
			    values.put(
				    InterventoDB.Fields.RIFERIMENTO_SCONTRINO,
				    (String) intervento.get("rifscontrino"));
			    values.put(InterventoDB.Fields.SALDATO,
				    (Boolean) intervento.get("saldato"));
			    values.put(InterventoDB.Fields.TIPOLOGIA,
				    (String) intervento.get("tipologia"));
			    values.put(InterventoDB.Fields.TOTALE,
				    (Double) intervento.get("totale"));

			    values.put(InterventoDB.Fields.CHIUSO,
				    (Boolean) intervento.get("chiuso"));
			    values.put(InterventoDB.Fields.TECNICO,
				    (Long) intervento.get("tecnico"));

			    cr.insert(InterventoDB.CONTENT_URI, values);

			    String selectionCliente = ClienteDB.Fields.TYPE
				    + "='" + ClienteDB.CLIENTE_ITEM_TYPE
				    + "' AND " + ClienteDB.Fields.ID_CLIENTE
				    + "=" + cliente.get("idcliente");

			    cursorCliente = cr.query(ClienteDB.CONTENT_URI,
				    null, selectionCliente, null, null);

			    if (cursorCliente.getCount() > 0) {
				cursorCliente.close();
			    } else {
				//*** INSERT CLIENTE ***\\\
				values = new ContentValues();

				values.put(ClienteDB.Fields.TYPE,
					ClienteDB.CLIENTE_ITEM_TYPE);
				values.put(ClienteDB.Fields.CANCELLATO,
					(Boolean) cliente.get("cancellato"));
				values.put(ClienteDB.Fields.CITTA,
					(String) cliente.get("citta"));
				values.put(ClienteDB.Fields.CODICE_FISCALE,
					(String) cliente.get("codicefiscale"));
				values.put(ClienteDB.Fields.EMAIL,
					(String) cliente.get("email"));
				values.put(ClienteDB.Fields.FAX,
					(String) cliente.get("fax"));
				values.put(ClienteDB.Fields.ID_CLIENTE,
					(Long) cliente.get("idcliente"));
				values.put(ClienteDB.Fields.INDIRIZZO,
					(String) cliente.get("indirizzo"));
				values.put(ClienteDB.Fields.INTERNO,
					(String) cliente.get("interno"));
				values.put(ClienteDB.Fields.NOMINATIVO,
					(String) cliente.get("nominativo"));
				values.put(ClienteDB.Fields.NOTE,
					(String) cliente.get("note"));
				values.put(ClienteDB.Fields.PARTITAIVA,
					(String) cliente.get("partitaiva"));
				values.put(ClienteDB.Fields.REFERENTE,
					(String) cliente.get("referente"));
				values.put(ClienteDB.Fields.REVISIONE,
					(Long) cliente.get("revisione"));
				values.put(ClienteDB.Fields.TELEFONO,
					(String) cliente.get("telefono"));
				values.put(ClienteDB.Fields.UFFICIO,
					(String) cliente.get("ufficio"));

				cr.insert(ClienteDB.CONTENT_URI, values);

				cursorCliente.close();
			    }
			}

			cursorIntervento.close();
		    }

		} catch (ParseException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}
	    }

	    @Override
	    protected void onPostExecute(Integer result) {

		if (result == Activity.RESULT_OK) {
		    //		    progress.dismiss();
		    //		    Toast.makeText(this.getActivity(), "Interventi scaricati",
		    //			    Toast.LENGTH_SHORT).show();
		    if (mRefreshActionItem != null) {
			System.out
				.println("RefreshActionItem is not null and progress is false");
			mRefreshActionItem.showProgress(false);
		    }
		} else {
		    //		    progress.dismiss();
		    Toast.makeText(
			    this.getActivity(),
			    "Si e' verificato un errore nel download degli interventi.",
			    Toast.LENGTH_SHORT).show();
		    if (mRefreshActionItem != null) {
			System.out
				.println("RefreshActionItem is not null and progress is false");
			mRefreshActionItem.showProgress(false);
		    }
		}
	    }
	}.execute(prefsLocal.getLong(Constants.USER_ID, 0l));
    }

    private int numberOfBackgroundOperations() throws InterruptedException,
	    ExecutionException {

	final SharedPreferences prefs = getSharedPreferences(
		Constants.PREFERENCES, Context.MODE_PRIVATE);

	ManagedAsyncTask<Long, Void, Integer> numBackGroundOperations = new ManagedAsyncTask<Long, Void, Integer>(
		HomeActivity.this) {

	    @Override
	    protected Integer doInBackground(Long... params) {

		int result = 0;

		int iduser = params[0].intValue();

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters
			.put("revision", prefs.getLong(Constants.REVISION, 0));

		String json_req = new String();

		try {
		    json_req = JsonCR2.createRequest("interventions",
			    "mysyncro", parameters, iduser);
		} catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		final SharedPreferences prefsDefault = PreferenceManager
			.getDefaultSharedPreferences(HomeActivity.this);

		System.out.println("Request mysyncro interventi:\n" + json_req);

		final String prefs_url = getResources().getString(
			string.prefs_key_url);

		final String url = prefsDefault.getString(prefs_url, null);

		AndroidHttpClient request = new AndroidHttpClient(url);
		request.setMaxRetries(5);

		ParameterMap paramMap = new ParameterMap();
		paramMap.add("DATA", json_req);

		HttpResponse response;
		response = request.post("", paramMap);

		JSONObject json_resp = new JSONObject();
		try {
		    json_resp = JsonCR2.read(response.getBodyAsString());

		    System.out.println("RESPONSE\n" + json_resp);
		} catch (ParseException e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		} catch (Exception e) {
		    e.printStackTrace();
		    BugSenseHandler.sendException(e);
		}

		//		if (json_resp.get("response").toString()
		//			.equalsIgnoreCase("success")) {
		//
		//		    JSONObject data = (JSONObject) json_resp.get("data");
		//
		//		    JSONArray intervents = (JSONArray) data.get("intervents");
		//		    JSONArray intervMOD = (JSONArray) data.get("mod");
		//		    JSONArray intervDEL = (JSONArray) data.get("del");
		//
		//		    result += intervents.size();
		//		    result += intervMOD.size();
		//		    result += intervDEL.size();
		//		}

		return result;
	    }

	    @Override
	    protected void onPostExecute(Integer result) {

	    }
	};

	numBackGroundOperations.execute(prefs.getLong(Constants.USER_ID, 0l));

	return numBackGroundOperations.get();
    }

    private List<Intervento> getUserInterventions()
	    throws InterruptedException, ExecutionException {

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	ManagedAsyncTask<Long, Void, List<Intervento>> listIntervento = new ManagedAsyncTask<Long, Void, List<Intervento>>(
		HomeActivity.this) {

	    @Override
	    protected List<Intervento> doInBackground(Long... params) {

		List<Intervento> list = new ArrayList<Intervento>();

		ContentResolver cr = getContentResolver();

		String selection = InterventoDB.Fields.TYPE + "='"
			+ InterventoDB.INTERVENTO_ITEM_TYPE + "' AND "
			/*+ InterventoDB.Fields.TECNICO
			+ "="
			+ params[0].intValue() + " AND "*/
			+ InterventoDB.Fields.CHIUSO + "=0";

		String orderBy = InterventoDB.Fields.DATA_ORA;

		Cursor cursor = cr.query(InterventoDB.CONTENT_URI, null,
			selection, null, orderBy);

		while (cursor.moveToNext()) {

		    Intervento interv = new Intervento();

		    interv.setmCancellato(Boolean.valueOf(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.CANCELLATO))));
		    interv.setmCostoAccessori(new BigDecimal(
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
		    interv.setmCostoComponenti(new BigDecimal(
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
		    interv.setmCostoManodopera(new BigDecimal(
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
		    interv.setmDataOra(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.DATA_ORA)));
		    interv.setmFirma(cursor.getString(
			    cursor.getColumnIndex(InterventoDB.Fields.FIRMA))
			    .getBytes());
		    interv.setmIdCliente(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.CLIENTE)));
		    interv.setmIdIntervento(cursor.getLong(cursor
			    .getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
		    interv.setmIdTecnico(params[0]);
		    interv.setmImporto(new BigDecimal(cursor.getDouble(cursor
			    .getColumnIndex(InterventoDB.Fields.IMPORTO))));
		    interv.setmIva(new BigDecimal(cursor.getDouble(cursor
			    .getColumnIndex(InterventoDB.Fields.IVA))));
		    interv.setmMotivo(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.MOTIVO)));
		    interv.setmNominativo(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
		    interv.setmNote(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.NOTE)));
		    interv.setmProdotto(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.PRODOTTO)));
		    interv.setmRifFattura(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.RIFERIMENTO_FATTURA)));
		    interv.setmRifScontrino(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.RIFERIMENTO_SCONTRINO)));
		    interv.setmSaldato(Boolean.valueOf(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.SALDATO))));
		    interv.setmTipologia(cursor.getString(cursor
			    .getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
		    interv.setmTotale(new BigDecimal(cursor.getDouble(cursor
			    .getColumnIndex(InterventoDB.Fields.TOTALE))));

		    list.add(interv);
		}

		cursor.close();

		return list;
	    }

	    @Override
	    protected void onPostExecute(java.util.List<Intervento> result) {

	    };
	}.execute(prefs.getLong(Constants.USER_ID, 0l));

	return listIntervento.get();
    }

    @Override
    public void onRefreshButtonClick(RefreshActionItem sender) {

	loadData();
    }
}
