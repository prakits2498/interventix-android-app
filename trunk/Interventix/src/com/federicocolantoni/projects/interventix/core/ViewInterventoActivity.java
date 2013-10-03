package com.federicocolantoni.projects.interventix.core;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.RipristinoInterventoDB;
import com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment;
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
public class ViewInterventoActivity extends BaseActivity {
    
    private static long id_intervento;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setContentView(R.layout.view_intervento);
	
	Bundle extras = getIntent().getExtras();
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	id_intervento = extras.getLong(Constants.ID_INTERVENTO);
	
	Intervento interv_old = null;
	ListDetailsIntervento listaDettagliIntervento_old = null;
	
	try {
	    interv_old = new GetInterventoAsyncTask(this).execute(id_intervento).get();
	    
	    listaDettagliIntervento_old = new GetDettagliInterventoAsyncTask(this).execute(id_intervento).get();
	    
	    JSONObject intervento = new JSONObject();
	    
	    intervento.put(InterventoDB.Fields.ID_INTERVENTO.toString(), interv_old.getmIdIntervento());
	    intervento.put(InterventoDB.Fields.CANCELLATO.toString(), interv_old.ismCancellato());
	    intervento.put(InterventoDB.Fields.CHIUSO.toString(), interv_old.ismChiuso());
	    intervento.put(InterventoDB.Fields.CLIENTE.toString(), interv_old.getmIdCliente());
	    intervento.put(InterventoDB.Fields.COSTO_ACCESSORI.toString(), interv_old.getmCostoAccessori().doubleValue());
	    intervento.put(InterventoDB.Fields.COSTO_COMPONENTI.toString(), interv_old.getmCostoComponenti().doubleValue());
	    intervento.put(InterventoDB.Fields.COSTO_MANODOPERA.toString(), interv_old.getmCostoManodopera().doubleValue());
	    intervento.put(InterventoDB.Fields.DATA_ORA.toString(), interv_old.getmDataOra());
	    intervento.put(InterventoDB.Fields.FIRMA.toString(), new String(interv_old.getmFirma()));
	    intervento.put(InterventoDB.Fields.IMPORTO.toString(), interv_old.getmImporto().doubleValue());
	    intervento.put(InterventoDB.Fields.IVA.toString(), interv_old.getmIva().doubleValue());
	    intervento.put(InterventoDB.Fields.MODALITA.toString(), interv_old.getmModalita());
	    intervento.put(InterventoDB.Fields.MODIFICATO.toString(), interv_old.getmModificato());
	    intervento.put(InterventoDB.Fields.MOTIVO.toString(), interv_old.getmMotivo());
	    intervento.put(InterventoDB.Fields.NOMINATIVO.toString(), interv_old.getmNominativo());
	    intervento.put(InterventoDB.Fields.NOTE.toString(), interv_old.getmNote());
	    intervento.put(InterventoDB.Fields.NUMERO_INTERVENTO.toString(), interv_old.getmNumeroIntervento());
	    intervento.put(InterventoDB.Fields.PRODOTTO.toString(), interv_old.getmProdotto());
	    intervento.put(InterventoDB.Fields.RIFERIMENTO_FATTURA.toString(), interv_old.getmRifFattura());
	    intervento.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO.toString(), interv_old.getmRifScontrino());
	    intervento.put(InterventoDB.Fields.SALDATO.toString(), interv_old.ismSaldato());
	    intervento.put(InterventoDB.Fields.TECNICO.toString(), interv_old.getmIdTecnico());
	    intervento.put(InterventoDB.Fields.TOTALE.toString(), interv_old.getmTotale().doubleValue());
	    intervento.put(InterventoDB.Fields.TIPOLOGIA.toString(), interv_old.getmTipologia());
	    
	    JSONArray arrayDettagli = new JSONArray();
	    
	    for (DettaglioIntervento dettaglio : listaDettagliIntervento_old.getListDetails()) {
		
		JSONObject dettaglioIntervento = new JSONObject();
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO.toString(), dettaglio.getmIdDettaglioIntervento());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.DESCRIZIONE.toString(), dettaglio.getmDescrizione());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.FINE.toString(), dettaglio.getmFine());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.INIZIO.toString(), dettaglio.getmInizio());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.INTERVENTO.toString(), dettaglio.getmIntervento());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.MODIFICATO.toString(), dettaglio.getmModificato());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.OGGETTO.toString(), dettaglio.getmOggetto());
		dettaglioIntervento.put(DettaglioInterventoDB.Fields.TIPO.toString(), dettaglio.getmTipo());
		
		arrayDettagli.put(dettaglioIntervento);
	    }
	    
	    intervento.put("arrayDettagli", arrayDettagli);
	    
	    new ManagedAsyncTask<JSONObject, Void, Integer>(this) {
		
		@Override
		protected Integer doInBackground(JSONObject... params) {
		    
		    int result = 0;
		    
		    ContentResolver cr = getContentResolver();
		    
		    ContentValues values = new ContentValues();
		    
		    values.put(RipristinoInterventoDB.Field.BACKUP_INTERVENTO, params[0].toString());
		    
		    Uri newRow = cr.insert(RipristinoInterventoDB.CONTENT_URI, values);
		    
		    if (newRow != null) {
			result = Activity.RESULT_OK;
		    }
		    else {
			result = RESULT_CANCELED;
		    }
		    
		    return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
		    
		    if (result == Activity.RESULT_OK) {
			System.out.println("Salvataggio dell'intervento di ripristino avvenuto con successo");
		    }
		    else {
			System.out.println("Errore nel salvataggio dell'intervento di ripristino");
		    }
		}
	    };
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (JSONException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	String nominativo = prefs.getString(Constants.USER_NOMINATIVO, null);
	
	getSupportActionBar().setTitle(nominativo);
	
	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();
	
	OverViewInterventoFragment overView = new OverViewInterventoFragment();
	
	Bundle bundle = new Bundle();
	bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	bundle.putAll(extras);
	
	overView.setArguments(bundle);
	
	transaction.add(R.id.fragments_layout, overView, Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.commit();
    }
    
    @Override
    protected void onResume() {
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	final Editor edit = prefs.edit();
	
	edit.putBoolean(Constants.INTERV_MODIFIED, false);
	edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
	
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
	
	super.onResume();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    
	    FragmentManager manager = getSupportFragmentManager();
	    
	    if (manager.getBackStackEntryCount() == 1) {
		
		SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
		boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
		boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
		
		if (!interv_modified && !dett_interv_modified) {
		    
		    finish();
		}
		else {
		    
		    new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		}
	    }
	    else {
		manager.popBackStackImmediate();
	    }
	    return true;
	}
	
	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() {
	
	FragmentManager manager = getSupportFragmentManager();
	
	if (manager.getBackStackEntryCount() == 1) {
	    
	    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
	    boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
	    
	    if (!interv_modified && !dett_interv_modified) {
		
		finish();
	    }
	    else {
		
		new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
	    }
	}
	else {
	    manager.popBackStackImmediate();
	}
    }
    
    public static class ExitIntervento extends SherlockDialogFragment implements OnClickListener {
	
	Cursor cursorReadDB;
	
	public ExitIntervento() {
	    
	    cursorReadDB = null;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder exit_dialog = new Builder(getSherlockActivity());
	    
	    exit_dialog.setTitle(R.string.interv_mod_title);
	    exit_dialog.setMessage(R.string.interv_mod_text);
	    
	    exit_dialog.setPositiveButton("SI", this);
	    exit_dialog.setNegativeButton("NO", this);
	    
	    return exit_dialog.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    switch (which) {
	    
		case DialogInterface.BUTTON_POSITIVE:
		    
		    dialog.dismiss();
		    
		    AsyncQueryHandler writeDB = new AsyncQueryHandler(getActivity().getContentResolver()) {
			
			@Override
			protected void onDeleteComplete(int token, Object cookie, int result) {
			    
			    switch (token) {
				case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				    
				    System.out.println("Cancellazione dell'intervento di ripristino avvenuta con successo");
				    
				    break;
				
				default:
				    break;
			    }
			}
			
			@Override
			protected void onInsertComplete(int token, Object cookie, Uri uri) {
			    super.onInsertComplete(token, cookie, uri);
			}
			
			@Override
			protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			    super.onQueryComplete(token, cookie, cursor);
			}
			
			@Override
			protected void onUpdateComplete(int token, Object cookie, int result) {
			    super.onUpdateComplete(token, cookie, result);
			}
		    };
		    
		    writeDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, null, null);
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
		    edit.putBoolean(Constants.INTERV_MODIFIED, false);
		    
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
		    getSherlockActivity().finish();
		    
		    break;
		
		case DialogInterface.BUTTON_NEGATIVE:
		    
		    dialog.dismiss();
		    
		    // TODO implementare il ripristino dell'intervento
		    
		    AsyncQueryHandler readDB = new AsyncQueryHandler(getActivity().getContentResolver()) {
			
			@Override
			protected void onDeleteComplete(int token, Object cookie, int result) {
			    super.onDeleteComplete(token, cookie, result);
			}
			
			@Override
			protected void onInsertComplete(int token, Object cookie, Uri uri) {
			    super.onInsertComplete(token, cookie, uri);
			}
			
			@Override
			protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			    
			    switch (token) {
				case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				    
				    System.out.println("Lettura dell'intervento di ripristino avvenuta con successo");
				    cursorReadDB = cursor;
				    
				    break;
				
				default:
				    break;
			    }
			}
			
			@Override
			protected void onUpdateComplete(int token, Object cookie, int result) {
			    super.onUpdateComplete(token, cookie, result);
			}
		    };
		    
		    readDB.startQuery(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, new String[] {
			    RipristinoInterventoDB.Field._ID, RipristinoInterventoDB.Field.BACKUP_INTERVENTO
		    }, null, null, null);
		    
		    boolean first = cursorReadDB.moveToFirst();
		    
		    if (first) {
			try {
			    JSONObject intervRipristino = new JSONObject(cursorReadDB.getString(cursorReadDB.getColumnIndex(RipristinoInterventoDB.Field.BACKUP_INTERVENTO)));
			    
			    ContentValues valuesIntervento = new ContentValues();
			    
			    valuesIntervento.put(InterventoDB.Fields.ID_INTERVENTO, intervRipristino.getLong(InterventoDB.Fields.ID_INTERVENTO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.CANCELLATO, intervRipristino.getBoolean(InterventoDB.Fields.CANCELLATO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.CHIUSO, intervRipristino.getBoolean(InterventoDB.Fields.CHIUSO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.CLIENTE, intervRipristino.getLong(InterventoDB.Fields.CLIENTE.toString()));
			    valuesIntervento.put(InterventoDB.Fields.COSTO_ACCESSORI, intervRipristino.getDouble(InterventoDB.Fields.COSTO_ACCESSORI.toString()));
			    valuesIntervento.put(InterventoDB.Fields.COSTO_COMPONENTI, intervRipristino.getDouble(InterventoDB.Fields.COSTO_COMPONENTI.toString()));
			    valuesIntervento.put(InterventoDB.Fields.COSTO_MANODOPERA, intervRipristino.getDouble(InterventoDB.Fields.COSTO_MANODOPERA.toString()));
			    valuesIntervento.put(InterventoDB.Fields.DATA_ORA, intervRipristino.getLong(InterventoDB.Fields.DATA_ORA.toString()));
			    valuesIntervento.put(InterventoDB.Fields.FIRMA, intervRipristino.getString(InterventoDB.Fields.FIRMA.toString()));
			    valuesIntervento.put(InterventoDB.Fields.IMPORTO, intervRipristino.getDouble(InterventoDB.Fields.IMPORTO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.IVA, intervRipristino.getDouble(InterventoDB.Fields.IVA.toString()));
			    valuesIntervento.put(InterventoDB.Fields.MODALITA, intervRipristino.getString(InterventoDB.Fields.MODALITA.toString()));
			    valuesIntervento.put(InterventoDB.Fields.MODIFICATO, intervRipristino.getString(InterventoDB.Fields.MODIFICATO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.MOTIVO, intervRipristino.getString(InterventoDB.Fields.MOTIVO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.NOMINATIVO, intervRipristino.getString(InterventoDB.Fields.NOMINATIVO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.NOTE, intervRipristino.getString(InterventoDB.Fields.NOTE.toString()));
			    valuesIntervento.put(InterventoDB.Fields.NUMERO_INTERVENTO, intervRipristino.getInt(InterventoDB.Fields.NUMERO_INTERVENTO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.PRODOTTO, intervRipristino.getString(InterventoDB.Fields.PRODOTTO.toString()));
			    valuesIntervento.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, intervRipristino.getString(InterventoDB.Fields.RIFERIMENTO_FATTURA.toString()));
			    
			    // readDB.startUpdate(token, cookie, uri, values,
			    // selection, selectionArgs);
			}
			catch (JSONException e) {
			    e.printStackTrace();
			    BugSenseHandler.sendException(e);
			}
		    }
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
		    edit.putBoolean(Constants.INTERV_MODIFIED, false);
		    
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
		    
		    getSherlockActivity().finish();
		    
		    break;
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	final MenuInflater inflater = getSupportMenuInflater();
	
	inflater.inflate(R.menu.view_intervento, menu);
	
	FragmentManager manager = getSupportFragmentManager();
	
	int cont = manager.getBackStackEntryCount();
	
	for (int i = 0; i < cont; i++) {
	    BackStackEntry entry = manager.getBackStackEntryAt(i);
	    
	    if (entry.getName().equals(Constants.DETAILS_INTERVENTO_FRAGMENT)) {
		
		MenuItem item = menu.findItem(R.id.add_detail_interv);
		
		item.setVisible(true);
	    }
	}
	
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		finish();
		
		break;
	    
	    case R.id.add_detail_interv:
		
		InterventixToast.makeToast(this, "Aggiungi dettaglio intervento", Toast.LENGTH_SHORT);
		break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
	
	super.onDestroy();
    }
}
