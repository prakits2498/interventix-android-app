package com.federicocolantoni.projects.interventix.core;

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
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDBTemp;
import com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.utils.GetIntervento;

@SuppressLint("NewApi")
public class ViewInterventoActivity extends BaseActivity {

    private static final int WRITE_INTERV_TOKEN = 0;
    private static final int CHECK_INTERV_MODIFIED = 1;
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
	    interv_temp = new GetIntervento(this).execute(id_intervento).get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}

	WriteIntervTemp writeIntervTemp = new WriteIntervTemp(this);

	ContentValues values = new ContentValues();

	values.put(InterventoDBTemp.Fields.TYPE,
		InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE);
	values.put(InterventoDBTemp.Fields.COSTO_ACCESSORI, interv_temp
		.getmCostoAccessori().doubleValue());
	values.put(InterventoDBTemp.Fields.CANCELLATO,
		interv_temp.ismCancellato());
	values.put(InterventoDBTemp.Fields.CHIUSO, interv_temp.ismChiuso());
	values.put(InterventoDBTemp.Fields.CLIENTE, interv_temp.getmIdCliente());
	values.put(InterventoDBTemp.Fields.COSTO_COMPONENTI, interv_temp
		.getmCostoComponenti().doubleValue());
	values.put(InterventoDBTemp.Fields.COSTO_MANODOPERA, interv_temp
		.getmCostoManodopera().doubleValue());
	values.put(InterventoDBTemp.Fields.DATA_ORA, interv_temp.getmDataOra());
	values.put(InterventoDBTemp.Fields.FIRMA, interv_temp.getmFirma());
	values.put(InterventoDBTemp.Fields.ID_INTERVENTO,
		interv_temp.getmIdIntervento());
	values.put(InterventoDBTemp.Fields.IMPORTO, interv_temp.getmImporto()
		.doubleValue());
	values.put(InterventoDBTemp.Fields.IVA, interv_temp.getmIva()
		.doubleValue());
	values.put(InterventoDBTemp.Fields.MODALITA, interv_temp.getmModalita());
	values.put(InterventoDBTemp.Fields.MODIFICATO,
		interv_temp.isModificato());
	values.put(InterventoDBTemp.Fields.MOTIVO, interv_temp.getmMotivo());
	values.put(InterventoDBTemp.Fields.NOMINATIVO,
		interv_temp.getmNominativo());
	values.put(InterventoDBTemp.Fields.NOTE, interv_temp.getmNote());
	values.put(InterventoDBTemp.Fields.NUMERO_INTERVENTO,
		interv_temp.getmNumeroIntervento());
	values.put(InterventoDBTemp.Fields.PRODOTTO, interv_temp.getmProdotto());
	values.put(InterventoDBTemp.Fields.RIFERIMENTO_FATTURA,
		interv_temp.getmRifFattura());
	values.put(InterventoDBTemp.Fields.RIFERIMENTO_SCONTRINO,
		interv_temp.getmRifScontrino());
	values.put(InterventoDBTemp.Fields.SALDATO, interv_temp.ismSaldato());
	values.put(InterventoDBTemp.Fields.TECNICO, interv_temp.getmIdTecnico());
	values.put(InterventoDBTemp.Fields.TIPOLOGIA,
		interv_temp.getmTipologia());
	values.put(InterventoDBTemp.Fields.TOTALE, interv_temp.getmTotale()
		.doubleValue());

	writeIntervTemp.startInsert(0, null, InterventoDBTemp.CONTENT_URI,
		values);

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES,
		Context.MODE_PRIVATE);

	final Editor edit = prefs.edit();

	edit.putBoolean(Constants.INTERV_MODIFIED, false);

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

	String nominativo = prefs.getString(Constants.USER_NOMINATIVO, null);

	getSupportActionBar().setTitle(nominativo);

	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();

	OverViewInterventoFragment overView = new OverViewInterventoFragment();

	Bundle bundle = new Bundle();
	bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	bundle.putAll(extras);

	overView.setArguments(bundle);

	transaction.add(R.id.fragments_layout, overView,
		Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {

	    FragmentManager manager = getSupportFragmentManager();

	    if (manager.getBackStackEntryCount() == 1) {

		CheckModifiedInterv check_mod_interv = new CheckModifiedInterv(
			this);

		String[] projection = new String[] { InterventoDB.Fields._ID,
			InterventoDB.Fields.MODIFICATO };

		String selection = InterventoDB.Fields.TYPE + "=? AND "
			+ InterventoDB.Fields.ID_INTERVENTO + "=? AND "
			+ InterventoDB.Fields.MODIFICATO + "=?";

		String[] selectionArgs = new String[] {
			InterventoDB.INTERVENTO_ITEM_TYPE, "" + id_intervento,
			"M" };

		check_mod_interv.startQuery(CHECK_INTERV_MODIFIED, null,
			InterventoDB.CONTENT_URI, projection, selection,
			selectionArgs, null);
	    } else {
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
	    CheckModifiedInterv check_mod_interv = new CheckModifiedInterv(this);

	    String[] projection = new String[] { InterventoDB.Fields._ID,
		    InterventoDB.Fields.MODIFICATO };

	    String selection = InterventoDB.Fields.TYPE + "=? AND "
		    + InterventoDB.Fields.ID_INTERVENTO + "=? AND "
		    + InterventoDB.Fields.MODIFICATO + "=?";

	    String[] selectionArgs = new String[] {
		    InterventoDB.INTERVENTO_ITEM_TYPE, "" + id_intervento, "M" };

	    check_mod_interv.startQuery(CHECK_INTERV_MODIFIED, null,
		    InterventoDB.CONTENT_URI, projection, selection,
		    selectionArgs, null);
	} else {
	    manager.popBackStackImmediate();
	}
    }

    public static class ExitIntervento extends SherlockDialogFragment implements
	    OnClickListener {

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

		String selection = InterventoDBTemp.Fields.TYPE + "=?";

		String[] selectionArgs = new String[] { InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE };

		new WriteIntervTemp(getSherlockActivity()).startDelete(0, null,
			InterventoDBTemp.CONTENT_URI, selection, selectionArgs);

		getSherlockActivity().finish();

		break;

	    case DialogInterface.BUTTON_NEGATIVE:

		dialog.dismiss();

		String[] projection = new String[] {
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
			InterventoDBTemp.Fields.TOTALE };

		String selectionQuery = InterventoDBTemp.Fields.TYPE
			+ "=? AND " + InterventoDBTemp.Fields.ID_INTERVENTO
			+ "=?";
		String[] selectionArgsQuery = new String[] {
			InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE,
			"" + id_intervento };

		WriteIntervTemp wrInterv = new WriteIntervTemp(
			getSherlockActivity());
		wrInterv.startQuery(WRITE_INTERV_TOKEN, null,
			InterventoDBTemp.CONTENT_URI, projection,
			selectionQuery, selectionArgsQuery, null);

		getSherlockActivity().finish();

		break;
	    }
	}
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    //
    // final MenuInflater inflater = getSupportMenuInflater();
    // inflater.inflate(R.menu.view_intervento, menu);
    //
    // return super.onCreateOptionsMenu(menu);
    // }

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

		SharedPreferences prefs = context.getSharedPreferences(
			Constants.PREFERENCES, Context.MODE_PRIVATE);

		boolean interv_modified = prefs.getBoolean(
			Constants.INTERV_MODIFIED, false);

		if (cursor.moveToFirst()) {
		    if (cursor
			    .getString(
				    cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO))
			    .equals("M")
			    && interv_modified) {

			new ExitIntervento().show(
				context.getSupportFragmentManager(),
				Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		    } else {
			context.finish();
		    }
		} else {
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
	    super.onDeleteComplete(token, cookie, result);
	}

	@Override
	protected void onInsertComplete(int token, Object cookie, Uri uri) {
	    switch (token) {
	    case 0:

		System.out.println("Dati inseriti in " + uri.toString());
		break;
	    }
	}

	@Override
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
	    switch (token) {
	    case WRITE_INTERV_TOKEN:

		ContentValues values = new ContentValues();

		if (cursor.moveToFirst()) {
		    values.put(
			    InterventoDB.Fields.CANCELLATO,
			    cursor.getInt(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.CANCELLATO)));
		    values.put(InterventoDB.Fields.CHIUSO, cursor.getInt(cursor
			    .getColumnIndex(InterventoDBTemp.Fields.CHIUSO)));
		    values.put(
			    InterventoDB.Fields.CLIENTE,
			    cursor.getInt(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.CLIENTE)));
		    values.put(
			    InterventoDB.Fields.COSTO_ACCESSORI,
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.COSTO_ACCESSORI)));
		    values.put(
			    InterventoDB.Fields.COSTO_COMPONENTI,
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.COSTO_COMPONENTI)));
		    values.put(
			    InterventoDB.Fields.COSTO_MANODOPERA,
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.COSTO_MANODOPERA)));
		    values.put(
			    InterventoDB.Fields.DATA_ORA,
			    cursor.getLong(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.DATA_ORA)));
		    values.put(InterventoDB.Fields.FIRMA, cursor.getBlob(cursor
			    .getColumnIndex(InterventoDBTemp.Fields.FIRMA)));
		    values.put(
			    InterventoDB.Fields.IMPORTO,
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.IMPORTO)));
		    values.put(InterventoDB.Fields.IVA, cursor.getDouble(cursor
			    .getColumnIndex(InterventoDBTemp.Fields.IVA)));
		    values.put(
			    InterventoDB.Fields.MODALITA,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.MODALITA)));
		    values.put(
			    InterventoDB.Fields.MODIFICATO,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.MODIFICATO)));
		    values.put(
			    InterventoDB.Fields.MOTIVO,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.MOTIVO)));
		    values.put(
			    InterventoDB.Fields.NOTE,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.NOTE)));
		    values.put(
			    InterventoDB.Fields.NUMERO_INTERVENTO,
			    cursor.getInt(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.NUMERO_INTERVENTO)));
		    values.put(
			    InterventoDB.Fields.PRODOTTO,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.PRODOTTO)));
		    values.put(
			    InterventoDB.Fields.RIFERIMENTO_FATTURA,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.RIFERIMENTO_FATTURA)));
		    values.put(
			    InterventoDB.Fields.RIFERIMENTO_SCONTRINO,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.RIFERIMENTO_SCONTRINO)));
		    values.put(
			    InterventoDB.Fields.SALDATO,
			    cursor.getInt(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.SALDATO)));
		    values.put(
			    InterventoDB.Fields.TECNICO,
			    cursor.getInt(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.TECNICO)));
		    values.put(
			    InterventoDB.Fields.TIPOLOGIA,
			    cursor.getString(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.TIPOLOGIA)));
		    values.put(
			    InterventoDB.Fields.TOTALE,
			    cursor.getDouble(cursor
				    .getColumnIndex(InterventoDBTemp.Fields.TOTALE)));

		    ContentResolver cr = context.getContentResolver();

		    String where = InterventoDB.Fields.TYPE + "=? AND "
			    + InterventoDB.Fields.ID_INTERVENTO + "=?";

		    String[] selectionArgs = new String[] {
			    InterventoDB.INTERVENTO_ITEM_TYPE,
			    ""
				    + cursor.getLong(cursor
					    .getColumnIndex(InterventoDBTemp.Fields.ID_INTERVENTO)) };

		    cr.update(InterventoDB.CONTENT_URI, values, where,
			    selectionArgs);

		    cr.delete(
			    InterventoDBTemp.CONTENT_URI,
			    InterventoDBTemp.Fields.TYPE + "=?",
			    new String[] { InterventoDBTemp.INTERVENTO_TEMP_ITEM_TYPE });
		}

		break;
	    }
	}

	@Override
	protected void onUpdateComplete(int token, Object cookie, int result) {
	    super.onUpdateComplete(token, cookie, result);
	}
    }
}
