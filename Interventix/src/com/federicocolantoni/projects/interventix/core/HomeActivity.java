
package com.federicocolantoni.projects.interventix.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import multiface.crypto.cr2.JsonCR2;

import org.acra.ACRA;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.string;
import com.federicocolantoni.projects.interventix.data.DBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.DBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.slezica.tools.async.ManagedAsyncTask;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

@SuppressLint("NewApi")
public class HomeActivity extends BaseActivity {

    private Menu optionsMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.activity_home);

	try {
	    getSupportActionBar().setTitle(setNominativo());
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	retrieveInterventionsFromServer();

	List<Intervento> listInterventionsOpen = null;

	try {
	    listInterventionsOpen = getUserInterventions();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	ListView listOpen = (ListView) findViewById(R.id.list_interv_open);

	TextView headerOpen = (TextView) findViewById(R.id.list_header_open);
	headerOpen.setText(R.string.interventi_aperti);

	InterventionsAdapter adapter = new InterventionsAdapter(this,
		listInterventionsOpen);

	listOpen.setAdapter(adapter);

	listOpen.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view,
		    int position, long id) {

		Intervento interv = (Intervento) adapter
			.getItemAtPosition(position);

		Toast.makeText(
			HomeActivity.this,
			"Hai selezionato l'intervento "
				+ interv.getmIdIntervento(), Toast.LENGTH_SHORT)
			.show();
	    }
	});

	adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	optionsMenu = menu;

	final MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.activity_home, menu);

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
	    case R.id.menu_refresh:

		return true;

	    case android.R.id.home:
		SharedPreferences prefs = getSharedPreferences(
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

		this.finish();
		return true;
	}

	return super.onOptionsItemSelected(item);
    }

    private void setRefreshActionButtonState(final boolean refreshing) {

	if (optionsMenu != null) {
	    final MenuItem refreshItem = optionsMenu
		    .findItem(R.id.menu_refresh);
	    if (refreshItem != null) {
		if (refreshing) {
		    refreshItem
			    .setActionView(R.layout.actionbar_indeterminate_progress);
		} else {
		    refreshItem.setActionView(null);
		}
	    }
	}
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

		cursor.close();

		return res;
	    }

	    @Override
	    protected void onPostExecute(String result) {

	    };
	}.execute(prefs.getLong(Constants.USER_ID, 0l));

	return nominativo.get();
    }

    private void retrieveInterventionsFromServer() {

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	new ManagedAsyncTask<Long, Void, Integer>(HomeActivity.this) {

	    private ProgressDialog progress;

	    @Override
	    protected void onPreExecute() {

		progress = new ProgressDialog(HomeActivity.this);
		progress.setIndeterminate(true);
		progress.setMessage("Attendere prego. Setup dell'applicazione in corso");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

		progress.show();
	    }

	    @Override
	    protected Integer doInBackground(Long... params) {

		String json_req = new String();

		int result = 0;

		long iduser = params[0];

		try {
		    json_req = JsonCR2.createRequest("interventions", "get",
			    null, (int) iduser);

		    //		    System.out.println("REQUEST INTERVENTI:\n" + json_req);

		    final SharedPreferences prefs = PreferenceManager
			    .getDefaultSharedPreferences(HomeActivity.this);

		    final String prefs_url = getResources().getString(
			    string.prefs_key_url);

		    final String url = prefs.getString(prefs_url, null);

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

			JSONArray data = (JSONArray) json_resp.get("data");

			for (int i = 0; i < data.size(); i++) {
			    JSONObject intervento = (JSONObject) data.get(i);

			    JSONObject cliente = (JSONObject) intervento
				    .get("cliente");

			    ContentResolver cr = getContentResolver();

			    String selection = InterventoDB.Fields.TYPE + "='"
				    + InterventoDB.INTERVENTO_ITEM_TYPE
				    + "' AND "
				    + InterventoDB.Fields.ID_INTERVENTO + "="
				    + intervento.get("idintervento");

			    Cursor cursorIntervento = cr.query(
				    InterventoDB.CONTENT_URI, null, selection,
				    null, null);

			    ContentValues values = new ContentValues();

			    if (cursorIntervento.getCount() > 0) {
				cursorIntervento.close();
			    } else {
				//*** INSERT INTERVENTO ***\\

				values.put(InterventoDB.Fields.TYPE,
					InterventoDB.INTERVENTO_ITEM_TYPE);
				values.put(InterventoDB.Fields.ID_INTERVENTO,
					(Long) intervento.get("idintervento"));

				values.put(InterventoDB.Fields.CANCELLATO,
					(Boolean) intervento.get("cancellato"));
				values.put(InterventoDB.Fields.COSTO_ACCESSORI,
					(Double) intervento
						.get("costoaccessori"));
				values.put(
					InterventoDB.Fields.COSTO_COMPONENTI,
					(Double) intervento
						.get("costocomponenti"));
				values.put(
					InterventoDB.Fields.COSTO_MANODOPERA,
					(Double) intervento
						.get("costomanodopera"));
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
				values.put(InterventoDB.Fields.MOTIVO,
					(String) intervento.get("motivo"));
				values.put(InterventoDB.Fields.NOMINATIVO,
					(String) intervento.get("nominativo"));
				values.put(InterventoDB.Fields.NOTE,
					(String) intervento.get("note"));
				values.put(InterventoDB.Fields.PRODOTTO,
					(String) intervento.get("prodotto"));
				values.put(
					InterventoDB.Fields.RIFERIMENTO_FATTURA,
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

				cursorIntervento.close();
			    }

			    String selectionCliente = ClienteDB.Fields.TYPE
				    + "='" + ClienteDB.CLIENTE_ITEM_TYPE
				    + "' AND " + ClienteDB.Fields.ID_CLIENTE
				    + "=" + cliente.get("idcliente");

			    Cursor cursorCliente = cr.query(
				    ClienteDB.CONTENT_URI, null,
				    selectionCliente, null, null);

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

			result = Activity.RESULT_OK;
			progress.dismiss();
		    } else {
			result = Activity.RESULT_CANCELED;
			progress.dismiss();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    ACRA.getErrorReporter().handleSilentException(e);
		} finally {
		    progress.dismiss();
		}

		return result;
	    }

	    @Override
	    protected void onPostExecute(Integer result) {

		if (result == Activity.RESULT_OK) {
		    //		    progress.dismiss();
		    //		    Toast.makeText(this.getActivity(), "Interventi scaricati",
		    //			    Toast.LENGTH_SHORT).show();
		} else {
		    //		    progress.dismiss();
		    Toast.makeText(
			    this.getActivity(),
			    "Si e' verificato un errore nel download degli interventi.",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	}.execute(prefs.getLong(Constants.USER_ID, 0l));
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
			+ InterventoDB.Fields.TECNICO + "="
			+ params[0].intValue() + " AND "
			+ InterventoDB.Fields.CHIUSO + "=0";

		Cursor cursor = cr.query(InterventoDB.CONTENT_URI, null,
			selection, null, null);

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
}
