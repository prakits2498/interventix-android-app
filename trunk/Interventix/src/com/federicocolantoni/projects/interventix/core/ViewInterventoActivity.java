package com.federicocolantoni.projects.interventix.core;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDBTemp;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDBTemp;
import com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment;
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;

@SuppressLint("NewApi")
public class ViewInterventoActivity extends BaseActivity {
    
    private static final int WRITE_INTERV_TOKEN = 0;
    private static final int WRITE_DETT_INTERV_TOKEN = 1;
    private static final int CHECK_INTERV_MODIFIED = 0;
    private static final int CHECK_DETTS_INTERV_MODIFIED = 1;
    
    private static long id_intervento;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setContentView(R.layout.view_intervento);
	
	Bundle extras = getIntent().getExtras();
	
	id_intervento = extras.getLong(Constants.ID_INTERVENTO);
	
	Intervento interv_temp = null;
	
	try {
	    interv_temp = new GetInterventoAsyncTask(this).execute(id_intervento).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	}
	
	SharedPreferences prefs = writeIntervAndIntervDetailsTemp(interv_temp);
	
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
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
	    edit.apply();
	else
	    new Thread(new Runnable() {
		
		@Override
		public void run() {
		    edit.commit();
		}
	    }).start();
	
	super.onResume();
    }
    
    private SharedPreferences writeIntervAndIntervDetailsTemp(Intervento interv_temp) {
	
	WriteIntervTemp writeIntervTemp = new WriteIntervTemp(this);
	
	ContentValues values = new ContentValues();
	
	values.put(InterventoDBTemp.Fields.TYPE, InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE);
	values.put(InterventoDBTemp.Fields.COSTO_ACCESSORI, interv_temp.getmCostoAccessori().doubleValue());
	values.put(InterventoDBTemp.Fields.CANCELLATO, interv_temp.ismCancellato());
	values.put(InterventoDBTemp.Fields.CHIUSO, interv_temp.ismChiuso());
	values.put(InterventoDBTemp.Fields.CLIENTE, interv_temp.getmIdCliente());
	values.put(InterventoDBTemp.Fields.COSTO_COMPONENTI, interv_temp.getmCostoComponenti().doubleValue());
	values.put(InterventoDBTemp.Fields.COSTO_MANODOPERA, interv_temp.getmCostoManodopera().doubleValue());
	values.put(InterventoDBTemp.Fields.DATA_ORA, interv_temp.getmDataOra());
	values.put(InterventoDBTemp.Fields.FIRMA, interv_temp.getmFirma());
	values.put(InterventoDBTemp.Fields.ID_INTERVENTO, interv_temp.getmIdIntervento());
	values.put(InterventoDBTemp.Fields.IMPORTO, interv_temp.getmImporto().doubleValue());
	values.put(InterventoDBTemp.Fields.IVA, interv_temp.getmIva().doubleValue());
	values.put(InterventoDBTemp.Fields.MODALITA, interv_temp.getmModalita());
	values.put(InterventoDBTemp.Fields.MODIFICATO, interv_temp.isModificato());
	values.put(InterventoDBTemp.Fields.MOTIVO, interv_temp.getmMotivo());
	values.put(InterventoDBTemp.Fields.NOMINATIVO, interv_temp.getmNominativo());
	values.put(InterventoDBTemp.Fields.NOTE, interv_temp.getmNote());
	values.put(InterventoDBTemp.Fields.NUMERO_INTERVENTO, interv_temp.getmNumeroIntervento());
	values.put(InterventoDBTemp.Fields.PRODOTTO, interv_temp.getmProdotto());
	values.put(InterventoDBTemp.Fields.RIFERIMENTO_FATTURA, interv_temp.getmRifFattura());
	values.put(InterventoDBTemp.Fields.RIFERIMENTO_SCONTRINO, interv_temp.getmRifScontrino());
	values.put(InterventoDBTemp.Fields.SALDATO, interv_temp.ismSaldato());
	values.put(InterventoDBTemp.Fields.TECNICO, interv_temp.getmIdTecnico());
	values.put(InterventoDBTemp.Fields.TIPOLOGIA, interv_temp.getmTipologia());
	values.put(InterventoDBTemp.Fields.TOTALE, interv_temp.getmTotale().doubleValue());
	
	writeIntervTemp.startInsert(WRITE_INTERV_TOKEN, null, InterventoDBTemp.CONTENT_URI, values);
	
	WriteDettIntervTemp writeDettsInterv = new WriteDettIntervTemp(this);
	
	ListDetailsIntervento listDetailsInterv = null;
	
	try {
	    listDetailsInterv = new GetDettagliInterventoAsyncTask(this).execute(interv_temp.getmIdIntervento()).get();
	}
	catch (InterruptedException e) {
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	
	values = new ContentValues();
	
	for (DettaglioIntervento dettInterv : listDetailsInterv.getListDetails()) {
	    
	    values.put(DettaglioInterventoDBTemp.Fields.TYPE, DettaglioInterventoDBTemp.DETTAGLIO_INTERVENTO_TEMP_ITEM_TYPE);
	    values.put(DettaglioInterventoDBTemp.Fields.DESCRIZIONE, dettInterv.getmDescrizione());
	    values.put(DettaglioInterventoDBTemp.Fields.FINE, dettInterv.getmFine());
	    values.put(DettaglioInterventoDBTemp.Fields.INIZIO, dettInterv.getmInizio());
	    values.put(DettaglioInterventoDBTemp.Fields.ID_DETTAGLIO_INTERVENTO, dettInterv.getmIdDettaglioIntervento());
	    values.put(DettaglioInterventoDBTemp.Fields.INTERVENTO, dettInterv.getmIntervento());
	    values.put(DettaglioInterventoDBTemp.Fields.OGGETTO, dettInterv.getmOggetto());
	    values.put(DettaglioInterventoDBTemp.Fields.TIPO, dettInterv.getmTipo());
	    values.put(DettaglioInterventoDBTemp.Fields.MODIFICATO, dettInterv.ismModificato());
	    
	    String tecnici = "";
	    
	    List<Integer> listTecnici = dettInterv.getmTecnici();
	    
	    for (int cont = 0; cont < listTecnici.size(); cont++) {
		tecnici += listTecnici.get(cont);
		
		if (cont < listTecnici.size())
		    tecnici += ",";
	    }
	    
	    values.put(DettaglioInterventoDBTemp.Fields.TECNICI, tecnici);
	    
	    writeDettsInterv.startInsert(WRITE_DETT_INTERV_TOKEN, null, DettaglioInterventoDBTemp.CONTENT_URI, values);
	}
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	final Editor edit = prefs.edit();
	
	edit.putBoolean(Constants.INTERV_MODIFIED, false);
	edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
	    edit.apply();
	else
	    new Thread(new Runnable() {
		
		@Override
		public void run() {
		    edit.commit();
		}
	    }).start();
	
	return prefs;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    
	    FragmentManager manager = getSupportFragmentManager();
	    
	    if (manager.getBackStackEntryCount() == 1) {
		
		CheckModifiedInterv check_mod_interv = new CheckModifiedInterv(this);
		
		String[] projection = new String[] {
			InterventoDB.Fields._ID, InterventoDB.Fields.MODIFICATO
		};
		
		String selection = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=? AND " + InterventoDB.Fields.MODIFICATO + "=?";
		
		String[] selectionArgs = new String[] {
			InterventoDB.INTERVENTO_ITEM_TYPE, "" + id_intervento,
			"M"
		};
		
		check_mod_interv.startQuery(CHECK_INTERV_MODIFIED, null, InterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
	    }
	    else
		manager.popBackStackImmediate();
	    return true;
	}
	
	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() {
	
	FragmentManager manager = getSupportFragmentManager();
	
	if (manager.getBackStackEntryCount() == 1) {
	    CheckModifiedInterv check_mod_interv = new CheckModifiedInterv(this);
	    
	    String[] projection = new String[] {
		    InterventoDB.Fields._ID, InterventoDB.Fields.MODIFICATO
	    };
	    
	    String selection = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=? AND " + InterventoDB.Fields.MODIFICATO + "=?";
	    
	    String[] selectionArgs = new String[] {
		    InterventoDB.INTERVENTO_ITEM_TYPE, "" + id_intervento, "M"
	    };
	    
	    check_mod_interv.startQuery(CHECK_INTERV_MODIFIED, null, InterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
	}
	else
	    manager.popBackStackImmediate();
    }
    
    public static class ExitIntervento extends SherlockDialogFragment implements OnClickListener {
	
	public ExitIntervento() {
	    
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
	    
	    switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
		    
		    dialog.dismiss();
		    
		    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    final Editor edit = prefs.edit();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
		    edit.putBoolean(Constants.INTERV_MODIFIED, false);
		    
		    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			edit.apply();
		    else
			new Thread(new Runnable() {
			    
			    @Override
			    public void run() {
				edit.commit();
			    }
			}).start();
		    
		    String selectionIntervTemp = InterventoDBTemp.Fields.TYPE + "=?";
		    String[] selectionArgsIntervTemp = new String[] {
			InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE
		    };
		    
		    String selectionDettIntervTemp = DettaglioInterventoDBTemp.Fields.TYPE + "=?";
		    String[] selectionArgsDettIntervTemp = new String[] {
			DettaglioInterventoDBTemp.DETTAGLIO_INTERVENTO_TEMP_ITEM_TYPE
		    };
		    
		    new WriteIntervTemp(getSherlockActivity()).startDelete(WRITE_INTERV_TOKEN, null, InterventoDBTemp.CONTENT_URI, selectionIntervTemp, selectionArgsIntervTemp);
		    new WriteDettIntervTemp(getSherlockActivity()).startDelete(WRITE_DETT_INTERV_TOKEN, null, DettaglioInterventoDBTemp.CONTENT_URI, selectionDettIntervTemp, selectionArgsDettIntervTemp);
		    
		    getSherlockActivity().finish();
		    
		    break;
		
		case DialogInterface.BUTTON_NEGATIVE:
		    
		    dialog.dismiss();
		    
		    String[] projectionInterv = new String[] {
			    InterventoDBTemp.Fields._ID,
			    InterventoDBTemp.Fields.CANCELLATO,
			    InterventoDBTemp.Fields.CHIUSO,
			    InterventoDBTemp.Fields.CLIENTE,
			    InterventoDBTemp.Fields.COSTO_ACCESSORI,
			    InterventoDBTemp.Fields.COSTO_COMPONENTI,
			    InterventoDBTemp.Fields.COSTO_MANODOPERA,
			    InterventoDBTemp.Fields.DATA_ORA,
			    InterventoDBTemp.Fields.FIRMA,
			    InterventoDBTemp.Fields.ID_INTERVENTO,
			    InterventoDBTemp.Fields.IMPORTO,
			    InterventoDBTemp.Fields.IVA,
			    InterventoDBTemp.Fields.MODALITA,
			    InterventoDBTemp.Fields.MODIFICATO,
			    InterventoDBTemp.Fields.MOTIVO,
			    InterventoDBTemp.Fields.NOMINATIVO,
			    InterventoDBTemp.Fields.NOTE,
			    InterventoDBTemp.Fields.NUMERO_INTERVENTO,
			    InterventoDBTemp.Fields.PRODOTTO,
			    InterventoDBTemp.Fields.RIFERIMENTO_FATTURA,
			    InterventoDBTemp.Fields.RIFERIMENTO_SCONTRINO,
			    InterventoDBTemp.Fields.SALDATO,
			    InterventoDBTemp.Fields.TECNICO,
			    InterventoDBTemp.Fields.TIPOLOGIA,
			    InterventoDBTemp.Fields.TOTALE
		    };
		    
		    String selectionQueryInterv = InterventoDBTemp.Fields.TYPE + "=? AND " + InterventoDBTemp.Fields.ID_INTERVENTO + "=?";
		    
		    String[] selectionArgsQueryInterv = new String[] {
			    InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE,
			    "" + id_intervento
		    };
		    
		    WriteIntervTemp wrInterv = new WriteIntervTemp(getSherlockActivity());
		    
		    wrInterv.startQuery(WRITE_INTERV_TOKEN, null, InterventoDBTemp.CONTENT_URI, projectionInterv, selectionQueryInterv, selectionArgsQueryInterv, null);
		    
		    // String[] projectionDettInterv = new String[] {
		    // DettaglioInterventoDBTemp.Fields._ID,
		    // DettaglioInterventoDBTemp.Fields.DESCRIZIONE,
		    // DettaglioInterventoDBTemp.Fields.FINE,
		    // DettaglioInterventoDBTemp.Fields.ID_DETTAGLIO_INTERVENTO,
		    // DettaglioInterventoDBTemp.Fields.INIZIO,
		    // DettaglioInterventoDBTemp.Fields.INTERVENTO,
		    // DettaglioInterventoDBTemp.Fields.MODIFICATO,
		    // DettaglioInterventoDBTemp.Fields.OGGETTO,
		    // DettaglioInterventoDBTemp.Fields.TECNICI,
		    // DettaglioInterventoDBTemp.Fields.TIPO
		    // };
		    //
		    // String selectionQueryDettInterv =
		    // DettaglioInterventoDBTemp.Fields.TYPE + "=? AND " +
		    // DettaglioInterventoDBTemp.Fields.ID_DETTAGLIO_INTERVENTO
		    // + "=?";
		    //
		    // long id_dett_intervento = 0;
		    //
		    // WriteDettIntervTemp wrDettsInterv = new
		    // WriteDettIntervTemp(getSherlockActivity());
		    //
		    // ListDetailsIntervento listDetailsInterv = null;
		    //
		    // try {
		    // listDetailsInterv = new
		    // GetDettagliInterventoAsyncTask(getSherlockActivity()).execute(id_intervento).get();
		    // }
		    // catch (InterruptedException e) {
		    // BugSenseHandler.sendException(e);
		    // e.printStackTrace();
		    // }
		    // catch (ExecutionException e) {
		    // BugSenseHandler.sendException(e);
		    // e.printStackTrace();
		    // }
		    //
		    // for (DettaglioIntervento dettInterv :
		    // listDetailsInterv.getListDetails()) {
		    //
		    // id_dett_intervento =
		    // dettInterv.getmIdDettaglioIntervento();
		    //
		    // String[] selectionArgsQueryDettInterv = new String[] {
		    // DettaglioInterventoDBTemp.DETTAGLIO_INTERVENTO_TEMP_ITEM_TYPE,
		    // "" + id_dett_intervento
		    // };
		    //
		    // wrDettsInterv.startQuery(WRITE_DETT_INTERV_TOKEN, null,
		    // DettaglioInterventoDBTemp.CONTENT_URI,
		    // projectionDettInterv, selectionQueryDettInterv,
		    // selectionArgsQueryDettInterv, null);
		    // }
		    
		    getSherlockActivity().finish();
		    
		    break;
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	final MenuInflater inflater = getSupportMenuInflater();
	
	FragmentManager manager = getSupportFragmentManager();
	
	int cont = manager.getBackStackEntryCount();
	
	for (int i = 0; i < cont; i++) {
	    BackStackEntry entry = manager.getBackStackEntryAt(i);
	    
	    if (entry.getName().equals(Constants.DETAILS_INTERVENTO_FRAGMENT))
		inflater.inflate(R.menu.details_interve_menu, menu);
	    else
		inflater.inflate(R.menu.view_intervento, menu);
	}
	
	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		finish();
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
	
	super.onDestroy();
    }
    
    private static class CheckModifiedInterv extends AsyncQueryHandler {
	
	private final BaseActivity context;
	
	public CheckModifiedInterv(BaseActivity context) {
	    super(context.getContentResolver());
	    this.context = context;
	}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	    
	    switch (token) {
		case CHECK_INTERV_MODIFIED:
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    CheckModifiedDettsInterv check_dett_interv = new CheckModifiedDettsInterv(context);
		    
		    String[] projection = new String[] {
			    DettaglioInterventoDB.Fields._ID,
			    DettaglioInterventoDB.Fields.MODIFICATO
		    };
		    
		    String selection = DettaglioInterventoDB.Fields.TYPE + "=? AND " + DettaglioInterventoDB.Fields.INTERVENTO + "=? AND " + DettaglioInterventoDB.Fields.MODIFICATO + "=?";
		    
		    String[] selectionArgs = new String[] {
			    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
			    "" + id_intervento, "M"
		    };
		    
		    boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
		    
		    if (cursor.moveToFirst()) {
			
			if (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO)).equals("M") && !interv_modified)
			    check_dett_interv.startQuery(CHECK_DETTS_INTERV_MODIFIED, true, DettaglioInterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
			else if (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO)).equals("M") && interv_modified)
			    check_dett_interv.startQuery(CHECK_DETTS_INTERV_MODIFIED, true, DettaglioInterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
		    }
		    else
			check_dett_interv.startQuery(CHECK_DETTS_INTERV_MODIFIED, false, DettaglioInterventoDB.CONTENT_URI, projection, selection, selectionArgs, null);
		    
		    break;
	    }
	}
    }
    
    private static class CheckModifiedDettsInterv extends AsyncQueryHandler {
	
	private final BaseActivity context;
	
	public CheckModifiedDettsInterv(BaseActivity context) {
	    super(context.getContentResolver());
	    
	    this.context = context;
	}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	    
	    boolean interv_modified = (Boolean) cookie;
	    
	    switch (token) {
	    
		case CHECK_DETTS_INTERV_MODIFIED:
		    
		    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		    
		    boolean detts_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
		    // boolean interv_modified_prefs =
		    // prefs.getBoolean(Constants.INTERV_MODIFIED, false);
		    
		    boolean modified = false;
		    
		    while (cursor.moveToNext())
			if (cursor.getString(cursor.getColumnIndex(DettaglioInterventoDB.Fields.MODIFICATO)).equals("M")) {
			    
			    modified = true;
			    break;
			}
		    
		    // intervento modificato; nessun dettaglio
		    // modificato
		    if (interv_modified && !modified && !detts_interv_modified) {
			
			System.out.println("intervento modificato; nessun dettaglio modificato");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // intervento modificato; nessun dettaglio
		    // modificato
		    else if (interv_modified && !modified && !detts_interv_modified) {
			
			System.out.println("intervento modificato; nessun dettaglio modificato");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // intervento modificato; uno dei suoi
		    // dettagli modificato la prima volta
		    else if (interv_modified && modified && !detts_interv_modified) {
			
			System.out.println("intervento modificato la prima volta; uno sei suoi dettagli modificato la prima volta");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // intervento modificato; uno dei
		    // suoi dettagli modificato la prima volta
		    else if (interv_modified && modified && !detts_interv_modified) {
			
			System.out.println("intervento modificato più di una volta; uno dei suoi dettagli modificato la prima volta");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // intervento modificato; uno dei
		    // suoi dettagli modificato più di una volta
		    else if (interv_modified && modified && detts_interv_modified) {
			
			System.out.println("intervento modificato più di una volta; uno o più dei suoi dettagli modificato più di una volta");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // intervento non modificato; uno dei suoi dettagli
		    // modificato la prima volta
		    else if (!interv_modified && modified && !detts_interv_modified) {
			
			System.out.println("intervento non modificato; uno dei suoi dettagli modificato la prima volta");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // intervento non modificato; uno dei suoi dettagli
		    // modificato più di una volta
		    else if (!interv_modified && modified && detts_interv_modified) {
			
			System.out.println("intervento non modificato; uno dei suoi dettagli modificato più di una volta");
			new ExitIntervento().show(context.getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    }
		    
		    // nessuna modifica all'intervento né ai suoi dettagli
		    else {
			
			System.out.println("nessuna modifica all'intervento né ai suoi dettagli");
			context.finish();
		    }
		    
		    break;
	    }
	}
    }
    
    private static class WriteIntervTemp extends AsyncQueryHandler {
	
	private final Context context;
	
	public WriteIntervTemp(Context context) {
	    super(context.getContentResolver());
	    this.context = context;
	}
	
	@Override
	protected void onDeleteComplete(int token, Object cookie, int result) {
	    
	    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.INTERV_MODIFIED, false);
	    
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		edit.apply();
	    else
		edit.commit();
	}
	
	@Override
	protected void onInsertComplete(int token, Object cookie, Uri uri) {
	    switch (token) {
		case WRITE_INTERV_TOKEN:
		    
		    System.out.println("Dati intervento inseriti in " + uri.toString());
		    break;
	    }
	}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	    switch (token) {
		case WRITE_INTERV_TOKEN:
		    
		    ContentValues values = new ContentValues();
		    
		    if (cursor.moveToFirst()) {
			values.put(InterventoDB.Fields.CANCELLATO, cursor.getInt(cursor.getColumnIndex(InterventoDBTemp.Fields.CANCELLATO)));
			values.put(InterventoDB.Fields.CHIUSO, cursor.getInt(cursor.getColumnIndex(InterventoDBTemp.Fields.CHIUSO)));
			values.put(InterventoDB.Fields.CLIENTE, cursor.getInt(cursor.getColumnIndex(InterventoDBTemp.Fields.CLIENTE)));
			values.put(InterventoDB.Fields.COSTO_ACCESSORI, cursor.getDouble(cursor.getColumnIndex(InterventoDBTemp.Fields.COSTO_ACCESSORI)));
			values.put(InterventoDB.Fields.COSTO_COMPONENTI, cursor.getDouble(cursor.getColumnIndex(InterventoDBTemp.Fields.COSTO_COMPONENTI)));
			values.put(InterventoDB.Fields.COSTO_MANODOPERA, cursor.getDouble(cursor.getColumnIndex(InterventoDBTemp.Fields.COSTO_MANODOPERA)));
			values.put(InterventoDB.Fields.DATA_ORA, cursor.getLong(cursor.getColumnIndex(InterventoDBTemp.Fields.DATA_ORA)));
			values.put(InterventoDB.Fields.FIRMA, cursor.getBlob(cursor.getColumnIndex(InterventoDBTemp.Fields.FIRMA)));
			values.put(InterventoDB.Fields.IMPORTO, cursor.getDouble(cursor.getColumnIndex(InterventoDBTemp.Fields.IMPORTO)));
			values.put(InterventoDB.Fields.IVA, cursor.getDouble(cursor.getColumnIndex(InterventoDBTemp.Fields.IVA)));
			values.put(InterventoDB.Fields.MODALITA, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.MODALITA)));
			values.put(InterventoDB.Fields.MODIFICATO, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.MODIFICATO)));
			values.put(InterventoDB.Fields.MOTIVO, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.MOTIVO)));
			values.put(InterventoDB.Fields.NOTE, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.NOTE)));
			values.put(InterventoDB.Fields.NUMERO_INTERVENTO, cursor.getInt(cursor.getColumnIndex(InterventoDBTemp.Fields.NUMERO_INTERVENTO)));
			values.put(InterventoDB.Fields.PRODOTTO, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.PRODOTTO)));
			values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.RIFERIMENTO_FATTURA)));
			values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.RIFERIMENTO_SCONTRINO)));
			values.put(InterventoDB.Fields.SALDATO, cursor.getInt(cursor.getColumnIndex(InterventoDBTemp.Fields.SALDATO)));
			values.put(InterventoDB.Fields.TECNICO, cursor.getInt(cursor.getColumnIndex(InterventoDBTemp.Fields.TECNICO)));
			values.put(InterventoDB.Fields.TIPOLOGIA, cursor.getString(cursor.getColumnIndex(InterventoDBTemp.Fields.TIPOLOGIA)));
			values.put(InterventoDB.Fields.TOTALE, cursor.getDouble(cursor.getColumnIndex(InterventoDBTemp.Fields.TOTALE)));
			
			ContentResolver cr = context.getContentResolver();
			
			String where = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?";
			
			String[] selectionArgs = new String[] {
				InterventoDB.INTERVENTO_ITEM_TYPE,
				"" + cursor.getLong(cursor.getColumnIndex(InterventoDBTemp.Fields.ID_INTERVENTO))
			};
			
			cr.update(InterventoDB.CONTENT_URI, values, where, selectionArgs);
			
			cr.delete(InterventoDBTemp.CONTENT_URI, InterventoDBTemp.Fields.TYPE + "=?", new String[] {
			    InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE
			});
		    }
		    
		    break;
	    }
	}
	
	@Override
	protected void onUpdateComplete(int token, Object cookie, int result) {
	    super.onUpdateComplete(token, cookie, result);
	}
    }
    
    private static class WriteDettIntervTemp extends AsyncQueryHandler {
	
	private final Context context;
	
	public WriteDettIntervTemp(Context context) {
	    super(context.getContentResolver());
	    this.context = context;
	}
	
	@Override
	protected void onInsertComplete(int token, Object cookie, Uri uri) {
	    switch (token) {
		case WRITE_DETT_INTERV_TOKEN:
		    
		    System.out.println("Dati dettagli intervento inseriti in " + uri.toString());
		    
		    break;
	    }
	}
	
	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	    switch (token) {
		case WRITE_DETT_INTERV_TOKEN:
		    
		    ContentValues values = new ContentValues();
		    
		    if (cursor.moveToFirst()) {
			
			ContentResolver cr = context.getContentResolver();
			
			values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, cursor.getString(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.DESCRIZIONE)));
			values.put(DettaglioInterventoDB.Fields.FINE, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.FINE)));
			values.put(DettaglioInterventoDB.Fields.INIZIO, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.INIZIO)));
			values.put(DettaglioInterventoDB.Fields.INTERVENTO, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.INTERVENTO)));
			values.put(DettaglioInterventoDB.Fields.MODIFICATO, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.MODIFICATO)));
			values.put(DettaglioInterventoDB.Fields.OGGETTO, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.OGGETTO)));
			values.put(DettaglioInterventoDB.Fields.TIPO, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.TIPO)));
			values.put(DettaglioInterventoDB.Fields.TECNICI, cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.TECNICI)));
			
			String where = DettaglioInterventoDB.Fields.TYPE + "=? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "=?";
			
			String[] selectionArgs = new String[] {
				DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
				"" + cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDBTemp.Fields.ID_DETTAGLIO_INTERVENTO))
			};
			
			cr.update(DettaglioInterventoDB.CONTENT_URI, values, where, selectionArgs);
			
			cr.delete(DettaglioInterventoDBTemp.CONTENT_URI, DettaglioInterventoDBTemp.Fields.TYPE + "=?", new String[] {
			    DettaglioInterventoDBTemp.DETTAGLIO_INTERVENTO_TEMP_ITEM_TYPE
			});
		    }
		    
		    break;
	    }
	}
	
	@Override
	protected void onDeleteComplete(int token, Object cookie, int result) {
	    
	    SharedPreferences prefs = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
	    
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		edit.apply();
	    else
		edit.commit();
	}
	
	@Override
	protected void onUpdateComplete(int token, Object cookie, int result) {
	    super.onUpdateComplete(token, cookie, result);
	}
    }
}
