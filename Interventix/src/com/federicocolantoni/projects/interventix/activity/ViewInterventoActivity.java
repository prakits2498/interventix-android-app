package com.federicocolantoni.projects.interventix.activity;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.RipristinoInterventoDB;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment;
import com.federicocolantoni.projects.interventix.task.GetInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetListaDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;
import com.googlecode.androidannotations.annotations.EActivity;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_view_intervento)
public class ViewInterventoActivity extends ActionBarActivity {
    
    private static long id_intervento;
    
    private SharedPreferences prefs;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    protected void onStart() {
	
	super.onStart();
	
	Bundle extras = getIntent().getExtras();
	
	prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	String nominativo = prefs.getString(Constants.USER_NOMINATIVO, null);
	
	getSupportActionBar().setTitle(nominativo);
	
	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();
	
	id_intervento = extras.getLong(Constants.ID_INTERVENTO);
	
	if (id_intervento > -1l) {
	    Intervento interv_old = null;
	    ListDetailsIntervento listaDettagliIntervento_old = null;
	    
	    try {
		interv_old = new GetInterventoAsyncTask(this).execute(id_intervento).get();
		
		listaDettagliIntervento_old = new GetListaDettagliInterventoAsyncTask(this).execute(id_intervento).get();
		
		JSONObject intervento = new JSONObject();
		
		intervento.put(InterventoDB.Fields.TYPE, InterventoDB.INTERVENTO_ITEM_TYPE);
		intervento.put(InterventoDB.Fields.ID_INTERVENTO.toString(), interv_old.getIdIntervento());
		intervento.put(InterventoDB.Fields.CANCELLATO.toString(), interv_old.isCancellato());
		intervento.put(InterventoDB.Fields.CHIUSO.toString(), interv_old.isChiuso());
		intervento.put(InterventoDB.Fields.CLIENTE.toString(), interv_old.getIdCliente());
		intervento.put(InterventoDB.Fields.COSTO_ACCESSORI.toString(), interv_old.getCostoAccessori().doubleValue());
		intervento.put(InterventoDB.Fields.COSTO_COMPONENTI.toString(), interv_old.getCostoComponenti().doubleValue());
		intervento.put(InterventoDB.Fields.COSTO_MANODOPERA.toString(), interv_old.getCostoManodopera().doubleValue());
		intervento.put(InterventoDB.Fields.DATA_ORA.toString(), interv_old.getDataOra());
		intervento.put(InterventoDB.Fields.FIRMA.toString(), new String(interv_old.getFirma()));
		intervento.put(InterventoDB.Fields.IMPORTO.toString(), interv_old.getImporto().doubleValue());
		intervento.put(InterventoDB.Fields.IVA.toString(), interv_old.getIva().doubleValue());
		intervento.put(InterventoDB.Fields.MODALITA.toString(), interv_old.getModalita());
		intervento.put(InterventoDB.Fields.MODIFICATO.toString(), interv_old.getModificato());
		intervento.put(InterventoDB.Fields.MOTIVO.toString(), interv_old.getMotivo());
		intervento.put(InterventoDB.Fields.NOMINATIVO.toString(), interv_old.getNominativo());
		intervento.put(InterventoDB.Fields.NOTE.toString(), interv_old.getNote());
		intervento.put(InterventoDB.Fields.NUMERO_INTERVENTO.toString(), interv_old.getNumeroIntervento());
		intervento.put(InterventoDB.Fields.PRODOTTO.toString(), interv_old.getProdotto());
		intervento.put(InterventoDB.Fields.RIFERIMENTO_FATTURA.toString(), interv_old.getRifFattura());
		intervento.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO.toString(), interv_old.getRifScontrino());
		intervento.put(InterventoDB.Fields.SALDATO.toString(), interv_old.isSaldato());
		intervento.put(InterventoDB.Fields.TECNICO.toString(), interv_old.getIdTecnico());
		intervento.put(InterventoDB.Fields.TOTALE.toString(), interv_old.getTotale().doubleValue());
		intervento.put(InterventoDB.Fields.TIPOLOGIA.toString(), interv_old.getTipologia());
		
		JSONArray arrayDettagli = new JSONArray();
		
		for (DettaglioIntervento dettaglio : listaDettagliIntervento_old.getListDetails()) {
		    
		    JSONObject dettaglioIntervento = new JSONObject();
		    
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO.toString(), dettaglio.getmIdDettaglioIntervento());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.DESCRIZIONE.toString(), dettaglio.getmDescrizione());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.FINE.toString(), dettaglio.getmFine());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.INIZIO.toString(), dettaglio.getmInizio());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.INTERVENTO.toString(), dettaglio.getmIntervento());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.MODIFICATO.toString(), dettaglio.getmModificato());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.OGGETTO.toString(), dettaglio.getmOggetto());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.TIPO.toString(), dettaglio.getmTipo());
		    dettaglioIntervento.put(DettaglioInterventoDB.Fields.TECNICI.toString(), dettaglio.getmTecnici());
		    
		    arrayDettagli.put(dettaglioIntervento);
		}
		
		intervento.put(Constants.ARRAY_DETTAGLI, arrayDettagli);
		
		new ManagedAsyncTask<JSONObject, Void, Integer>(this) {
		    
		    @Override
		    protected Integer doInBackground(JSONObject... params) {
			
			int result = 0;
			
			ContentResolver cr = getContentResolver();
			
			ContentValues values = new ContentValues();
			
			values.put(RipristinoInterventoDB.Field.TYPE, RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE);
			values.put(RipristinoInterventoDB.Field.BACKUP_INTERVENTO, params[0].toString());
			
			Uri newRow = cr.insert(RipristinoInterventoDB.CONTENT_URI, values);
			
			if (newRow != null) {
			    
			    System.out.println("Uri ripristino intervento: " + newRow.toString());
			    
			    result = RESULT_OK;
			}
			else {
			    result = RESULT_CANCELED;
			}
			
			return result;
		    }
		    
		    @Override
		    protected void onPostExecute(Integer result) {
			
			if (result == RESULT_OK) {
			    System.out.println("Salvataggio dell'intervento di ripristino avvenuto con successo");
			}
			else {
			    System.out.println("Errore nel salvataggio dell'intervento di ripristino");
			}
		    }
		}.execute(intervento);
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
	    
	    // OverViewInterventoFragment overView = new
	    // com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_();
	    
	    // Bundle bundle = new Bundle();
	    // bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	    // bundle.putAll(extras);
	    //
	    // overView.setArguments(bundle);
	    //
	    // transaction.add(R.id.fragments_layout, overView,
	    // Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	    // transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	    // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    // transaction.commit();
	}
	
	OverViewInterventoFragment overView = new com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_();
	
	Bundle bundle = new Bundle();
	bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	bundle.putAll(extras);
	
	overView.setArguments(bundle);
	
	transaction.add(R.id.fragments_layout, overView, Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.commit();
	
	// else {
	//
	// Bundle bundle = new Bundle();
	// bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	// bundle.putAll(extras);
	//
	// transaction.add(R.id.fragments_layout, overView,
	// Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	// transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	// transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	// transaction.commit();
	// }
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
	    
	    if (getSupportActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS)
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	    
	    final FragmentManager manager = getSupportFragmentManager();
	    
	    if (manager.getBackStackEntryCount() == 1) {
		
		SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
		boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
		boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
		
		if (!interv_modified && !dett_interv_modified) {
		    
		    AsyncQueryHandler writeDB = new AsyncQueryHandler(getContentResolver()) {
			
			@Override
			protected void onDeleteComplete(int token, Object cookie, int result) {
			    
			    switch (token) {
				case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				    
				    System.out.println("Delete Result: " + result + "\nCancellazione dell'intervento di ripristino avvenuta con successo");
				    
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
		    
		    writeDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
			    RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE
		    });
		    
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
	
	final FragmentManager manager = getSupportFragmentManager();
	
	if (manager.getBackStackEntryCount() == 1) {
	    
	    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
	    boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
	    
	    if (!interv_modified && !dett_interv_modified) {
		
		AsyncQueryHandler writeDB = new AsyncQueryHandler(getContentResolver()) {
		    
		    @Override
		    protected void onDeleteComplete(int token, Object cookie, int result) {
			
			switch (token) {
			    case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				
				System.out.println("Delete Result: " + result + "\nCancellazione dell'intervento di ripristino avvenuta con successo");
				
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
		
		writeDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
			RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE
		});
		
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
    
    public static class ExitIntervento extends DialogFragment implements OnClickListener {
	
	public ExitIntervento() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder exit_dialog = new Builder(getActivity());
	    
	    exit_dialog.setTitle(R.string.interv_mod_title);
	    exit_dialog.setMessage(R.string.interv_mod_text);
	    
	    exit_dialog.setPositiveButton(getString(R.string.yes_btn), this);
	    exit_dialog.setNegativeButton(getString(R.string.no_btn), this);
	    
	    return exit_dialog.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    switch (which) {
	    
		case DialogInterface.BUTTON_POSITIVE:
		    
		    dialog.dismiss();
		    
		    AsyncQueryHandler writeDB = new AsyncQueryHandler(getActivity().getContentResolver()) {
			
			@Override
			protected void onDeleteComplete(int token, Object cookie, int result) {
			    
			    switch (token) {
				case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				    
				    System.out.println("Delete Result: " + result + "\nCancellazione dell'intervento di ripristino avvenuta con successo");
				    
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
		    
		    writeDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
			    RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE
		    });
		    
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
		    
		    getActivity().finish();
		    
		    break;
		
		case DialogInterface.BUTTON_NEGATIVE:
		    
		    dialog.dismiss();
		    
		    Cursor cursorDB = null;
		    
		    AsyncQueryHandler queryHandlerDB = new AsyncQueryHandler(getActivity().getContentResolver()) {
			
			@Override
			protected void onDeleteComplete(int token, Object cookie, int result) {
			    
			    switch (token) {
				case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				    
				    System.out.println("Delete Result: " + result + "\nCancellazione dell'intervento di ripristino avvenuta con successo");
				    
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
		    
		    try {
			cursorDB = new ManagedAsyncTask<String, Void, Cursor>(getActivity()) {
			    
			    @Override
			    protected Cursor doInBackground(String... params) {
				
				Cursor cursor = null;
				
				ContentResolver cr = getActivity().getContentResolver();
				
				cursor = cr.query(RipristinoInterventoDB.CONTENT_URI, new String[] {
					RipristinoInterventoDB.Field._ID,
					RipristinoInterventoDB.Field.BACKUP_INTERVENTO
				}, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
					params[0]
				}, null);
				
				return cursor;
			    }
			    
			    @Override
			    protected void onPostExecute(Cursor result) {
				
				if (result != null) {
				    System.out.println("Lettura dell'intervento di ripristino avvenuta con successo");
				}
				else {
				    System.out.println("Errore nel recupero dell'intervento di ripristino");
				}
				
			    }
			}.execute(RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE).get();
		    }
		    catch (InterruptedException e) {
			
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		    catch (ExecutionException e) {
			
			e.printStackTrace();
			BugSenseHandler.sendException(e);
		    }
		    
		    if (cursorDB != null) {
			boolean first = cursorDB.moveToFirst();
			
			if (first) {
			    try {
				JSONObject intervRipristino = new JSONObject(cursorDB.getString(cursorDB.getColumnIndex(RipristinoInterventoDB.Field.BACKUP_INTERVENTO)));
				
				ContentValues valuesIntervento = new ContentValues();
				
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
				valuesIntervento.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, intervRipristino.getString(InterventoDB.Fields.RIFERIMENTO_SCONTRINO.toString()));
				valuesIntervento.put(InterventoDB.Fields.SALDATO, intervRipristino.getBoolean(InterventoDB.Fields.SALDATO.toString()));
				valuesIntervento.put(InterventoDB.Fields.TECNICO, intervRipristino.getLong(InterventoDB.Fields.TECNICO.toString()));
				valuesIntervento.put(InterventoDB.Fields.TIPOLOGIA, intervRipristino.getString(InterventoDB.Fields.TIPOLOGIA.toString()));
				valuesIntervento.put(InterventoDB.Fields.TOTALE, intervRipristino.getDouble(InterventoDB.Fields.TOTALE.toString()));
				
				queryHandlerDB.startUpdate(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, InterventoDB.CONTENT_URI, valuesIntervento,
					InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?", new String[] {
						InterventoDB.INTERVENTO_ITEM_TYPE, "" + intervRipristino.getLong(InterventoDB.Fields.ID_INTERVENTO.toString())
					});
				
				JSONArray dettagliIntervento = intervRipristino.getJSONArray(Constants.ARRAY_DETTAGLI);
				
				for (int i = 0; i < dettagliIntervento.length(); i++) {
				    
				    JSONObject dettaglio = dettagliIntervento.getJSONObject(i);
				    
				    ContentValues valuesDettaglio = new ContentValues();
				    
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettaglio.getString(DettaglioInterventoDB.Fields.DESCRIZIONE.toString()));
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.FINE, dettaglio.getLong(DettaglioInterventoDB.Fields.FINE.toString()));
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.INIZIO, dettaglio.getLong(DettaglioInterventoDB.Fields.INIZIO.toString()));
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.INTERVENTO, dettaglio.getLong(DettaglioInterventoDB.Fields.INTERVENTO.toString()));
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.MODIFICATO, dettaglio.getString(DettaglioInterventoDB.Fields.MODIFICATO.toString()));
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.OGGETTO, dettaglio.getString(DettaglioInterventoDB.Fields.OGGETTO.toString()));
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.TECNICI, dettaglio.getJSONArray(DettaglioInterventoDB.Fields.TECNICI.toString()).toString());
				    valuesDettaglio.put(DettaglioInterventoDB.Fields.TIPO, dettaglio.getString(DettaglioInterventoDB.Fields.TIPO.toString()));
				    
				    queryHandlerDB.startUpdate(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, DettaglioInterventoDB.CONTENT_URI, valuesDettaglio,
					    DettaglioInterventoDB.Fields.TYPE + "=? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "=?", new String[] {
						    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + dettaglio.getLong(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO.toString())
					    });
				}
				
				queryHandlerDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
					RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE
				});
				
				queryHandlerDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, DettaglioInterventoDB.CONTENT_URI, DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "<?", new String[] {
					"0"
				});
			    }
			    catch (JSONException e) {
				
				e.printStackTrace();
				BugSenseHandler.sendException(e);
			    }
			    finally {
				
				queryHandlerDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
					RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE
				});
			    }
			}
			
			cursorDB.close();
		    }
		    else {
			
			System.out.println("Errore nel recupero dell'intervento di ripristino");
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
		    
		    getActivity().finish();
		    
		    break;
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	super.onCreateOptionsMenu(menu);
	
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
		boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
		boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
		
		if (!interv_modified && !dett_interv_modified) {
		    
		    AsyncQueryHandler writeDB = new AsyncQueryHandler(getContentResolver()) {
			
			@Override
			protected void onDeleteComplete(int token, Object cookie, int result) {
			    
			    switch (token) {
				case Constants.TOKEN_RIPRISTINO_INTERVENTO:
				    
				    System.out.println("Delete Result: " + result + "\nCancellazione dell'intervento di ripristino avvenuta con successo");
				    
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
		    
		    writeDB.startDelete(Constants.TOKEN_RIPRISTINO_INTERVENTO, null, RipristinoInterventoDB.CONTENT_URI, RipristinoInterventoDB.Field.TYPE + "=?", new String[] {
			    RipristinoInterventoDB.RIPRISTINO_INTERVENTO_ITEM_TYPE
		    });
		    
		    finish();
		}
		else {
		    
		    new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		}
		
		break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
	
	super.onDestroy();
    }
}
