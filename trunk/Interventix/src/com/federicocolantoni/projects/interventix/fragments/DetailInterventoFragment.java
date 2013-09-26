package com.federicocolantoni.projects.interventix.fragments;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.task.GetDettaglioInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.SaveChangesDettaglioInterventoAsyncQueryHandler;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
public class DetailInterventoFragment extends SherlockFragment {
    
    private static long sId_Dettaglio_Intervento;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	
	BugSenseHandler.initAndStartSession(getSherlockActivity(), Constants.API_KEY);
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	final View view = inflater.inflate(R.layout.detail_dett_intervento_fragment, container, false);
	
	Bundle bundle = getArguments();
	
	sId_Dettaglio_Intervento = bundle.getLong(Constants.ID_DETTAGLIO_INTERVENTO);
	
	DettaglioIntervento dettInterv = null;
	
	try {
	    dettInterv = new GetDettaglioInterventoAsyncTask(getSherlockActivity()).execute(sId_Dettaglio_Intervento).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	TextView tv_dett_interv = (TextView) view.findViewById(R.id.tv_dett_interv);
	
	tv_dett_interv.setText("Dettaglio " + sId_Dettaglio_Intervento);
	
	View row_tipo_dett = view.findViewById(R.id.row_tipo_dettaglio);
	
	row_tipo_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetTipo().show(getFragmentManager(), Constants.TIPO_DETTAGLIO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_tipo_dett = (TextView) row_tipo_dett.findViewById(R.id.tv_row_tipo_dettaglio);
	tv_row_tipo_dett.setText(dettInterv.getmTipo());
	
	View row_oggetto_dett = view.findViewById(R.id.row_oggetto_dettaglio);
	
	row_oggetto_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetOggetto().show(getFragmentManager(), Constants.OGGETTO_DETTAGLIO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_oggetto_dett = (TextView) row_oggetto_dett.findViewById(R.id.tv_row_oggetto_dettaglio);
	tv_row_oggetto_dett.setText(dettInterv.getmOggetto());
	
	// View row_tecnici_dett =
	// view.findViewById(R.id.row_tecnici_dettaglio);
	
	// TextView tv_row_tecnici_dett = (TextView)
	// row_tecnici_dett.findViewById(R.id.tv_row_tecnici_dettaglio);
	// tv_row_tecnici_dett.setText("" + dettInterv.getmTecnici().size());
	
	View row_descrizione_dett = view.findViewById(R.id.row_descrizione_dettaglio);
	
	row_descrizione_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetDecrizione().show(getFragmentManager(), Constants.DESCRIZIONE_DETTAGLIO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_descrizione_dett = (TextView) row_descrizione_dett.findViewById(R.id.tv_row_descrizione_dettaglio);
	tv_row_descrizione_dett.setText(dettInterv.getmDescrizione());
	
	final View row_inizio_dett = view.findViewById(R.id.row_inizio_dettaglio);
	
	row_inizio_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		final TextView tv_row_inizio_dett = (TextView) row_inizio_dett.findViewById(R.id.tv_row_inizio_dettaglio);
		
		final Dialog dateTimeDialog = new Dialog(getSherlockActivity());
		
		final RelativeLayout dateTimeDialogView = (RelativeLayout) getSherlockActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		
		final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);
		
		DateTime dt = null;
		
		dt = DateTime.parse(tv_row_inizio_dett.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
		
		dateTimePicker.setDateTime(dt);
		
		dateTimePicker.setDateChangedListener(new DateWatcher() {
		    
		    @Override
		    public void onDateChanged(Calendar c) {
			
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			dateTimePicker.clearFocus();
			
			DateTime dt_inizio = new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(), DateTimeZone.forID("Europe/Rome"));
			
			TextView tv_dett_ore_tot = (TextView) getSherlockActivity().findViewById(R.id.tv_row_tot_ore_dettaglio);
			TextView tv_dett_fine = (TextView) getSherlockActivity().findViewById(R.id.tv_row_fine_dettaglio);
			
			DateTime dt_fine = null;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			
			dt_fine = fmt.parseDateTime(tv_dett_fine.getText().toString());
			
			if (dt_fine.toDate().getTime() < dt_inizio.toDate().getTime()) {
			    InterventixToast.makeToast(getSherlockActivity(), "Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
			}
			else {
			    
			    tv_row_inizio_dett.setText(dt_inizio.toString("dd/MM/yyyy HH:mm"));
			    
			    DateTime dt_tot_ore = new DateTime(dt_fine.toDate().getTime() - dt_inizio.toDate().getTime(), DateTimeZone.forID("Europe/Rome"));
			    
			    tv_dett_ore_tot.setText(dt_tot_ore.toString("HH:mm"));
			    
			    SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getSherlockActivity());
			    
			    ContentValues values = new ContentValues();
			    values.put(DettaglioInterventoDB.Fields.INIZIO, dt_inizio.toDate().getTime());
			    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
			    
			    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
			    
			    String[] selectionArgs = new String[] {
				    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
				    "" + sId_Dettaglio_Intervento
			    };
			    
			    saveChanges.startUpdate(Constants.TOKEN_ORA_INIZIO_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
			    
			    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			    
			    final Editor edit = prefs.edit();
			    
			    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
			    
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
			}
			
			dateTimeDialog.dismiss();
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			dateTimeDialog.cancel();
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			DateTime dt = null;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			
			dt = fmt.parseDateTime(tv_row_inizio_dett.getText().toString());
			
			dateTimePicker.setDateTime(dt);
		    }
		});
		
		dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialog.setContentView(dateTimeDialogView);
		dateTimeDialog.setCancelable(false);
		dateTimeDialog.show();
	    }
	});
	
	TextView tv_row_inizio_dett = (TextView) row_inizio_dett.findViewById(R.id.tv_row_inizio_dettaglio);
	
	DateTime dt_inizio = new DateTime(dettInterv.getmInizio(), DateTimeZone.forID("Europe/Rome"));
	
	tv_row_inizio_dett.setText(dt_inizio.toString("dd/MM/yyyy HH:mm", Locale.ITALY));
	
	final View row_fine_dett = view.findViewById(R.id.row_fine_dettaglio);
	
	row_fine_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		final TextView tv_row_fine_dett = (TextView) row_fine_dett.findViewById(R.id.tv_row_fine_dettaglio);
		
		final Dialog dateTimeDialog = new Dialog(getSherlockActivity());
		
		final RelativeLayout dateTimeDialogView = (RelativeLayout) getSherlockActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		
		final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);
		
		DateTime dt = null;
		
		dt = DateTime.parse(tv_row_fine_dett.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
		
		dateTimePicker.setDateTime(dt);
		
		dateTimePicker.setDateChangedListener(new DateWatcher() {
		    
		    @Override
		    public void onDateChanged(Calendar c) {
			
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			dateTimePicker.clearFocus();
			
			DateTime dt_fine = new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(), DateTimeZone.forID("Europe/Rome"));
			
			TextView tv_dett_ore_tot = (TextView) getSherlockActivity().findViewById(R.id.tv_row_tot_ore_dettaglio);
			TextView tv_dett_inizio = (TextView) getSherlockActivity().findViewById(R.id.tv_row_inizio_dettaglio);
			
			DateTime dt_inizio = null;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			
			dt_inizio = fmt.parseDateTime(tv_dett_inizio.getText().toString());
			
			if (dt_fine.toDate().getTime() < dt_inizio.toDate().getTime()) {
			    InterventixToast.makeToast(getSherlockActivity(), "Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
			}
			else {
			    
			    tv_row_fine_dett.setText(dt_fine.toString("dd/MM/yyyy HH:mm"));
			    
			    DateTime dt_tot_ore = new DateTime(dt_fine.toDate().getTime() - dt_inizio.toDate().getTime(), DateTimeZone.forID("Europe/Rome"));
			    
			    tv_dett_ore_tot.setText(dt_tot_ore.toString("HH:mm"));
			    
			    SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getSherlockActivity());
			    
			    ContentValues values = new ContentValues();
			    values.put(DettaglioInterventoDB.Fields.FINE, dt_fine.toDate().getTime());
			    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
			    
			    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
			    
			    String[] selectionArgs = new String[] {
				    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
				    "" + sId_Dettaglio_Intervento
			    };
			    
			    saveChanges.startUpdate(Constants.TOKEN_ORA_FINE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
			    
			    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			    
			    final Editor edit = prefs.edit();
			    
			    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
			    
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
			}
			
			dateTimeDialog.dismiss();
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			dateTimeDialog.cancel();
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			DateTime dt = null;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			
			dt = fmt.parseDateTime(tv_row_fine_dett.getText().toString());
			
			dateTimePicker.setDateTime(dt);
		    }
		});
		
		dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialog.setContentView(dateTimeDialogView);
		dateTimeDialog.setCancelable(false);
		dateTimeDialog.show();
	    }
	});
	
	TextView tv_row_fine_dett = (TextView) row_fine_dett.findViewById(R.id.tv_row_fine_dettaglio);
	
	DateTime dt_fine = new DateTime(dettInterv.getmFine(), DateTimeZone.forID("Europe/Rome"));
	
	tv_row_fine_dett.setText(dt_fine.toString("dd/MM/yyyy HH:mm", Locale.ITALY));
	
	View row_tot_ore_dett = view.findViewById(R.id.row_tot_ore_dettaglio);
	
	TextView tv_row_tot_ore_dett = (TextView) row_tot_ore_dett.findViewById(R.id.tv_row_tot_ore_dettaglio);
	
	DateTime dt_tot_ore = new DateTime(dt_fine.toDate().getTime() - dt_inizio.toDate().getTime(), DateTimeZone.forID("Europe/Rome"));
	
	tv_row_tot_ore_dett.setText(dt_tot_ore.toString(DateTimeFormat.forPattern("HH:mm")));
	
	return view;
    }
    
    public static class SetTipo extends SherlockDialogFragment
							      implements
							      DialogInterface.OnClickListener {
	
	private EditText mEdit_tipo_dett;
	
	public SetTipo() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder tipo_dett = new Builder(getSherlockActivity());
	    
	    tipo_dett.setTitle(R.string.tipo_dett_title);
	    
	    TextView tv_tipo_dett = (TextView) getSherlockActivity().findViewById(R.id.tv_row_tipo_dettaglio);
	    
	    mEdit_tipo_dett = new EditText(getSherlockActivity());
	    mEdit_tipo_dett.setText(tv_tipo_dett.getText());
	    
	    tipo_dett.setView(mEdit_tipo_dett);
	    
	    tipo_dett.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return tipo_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    TextView tv_tipo_dett = (TextView) getSherlockActivity().findViewById(R.id.tv_row_tipo_dettaglio);
	    tv_tipo_dett.setText(mEdit_tipo_dett.getText());
	    
	    SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(DettaglioInterventoDB.Fields.TIPO, mEdit_tipo_dett.getText().toString());
	    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		    "" + sId_Dettaglio_Intervento
	    };
	    
	    saveChanges.startUpdate(Constants.TOKEN_TIPO_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
	    
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
	    
	    dialog.dismiss();
	}
    }
    
    public static class SetOggetto extends SherlockDialogFragment
								 implements
								 DialogInterface.OnClickListener {
	
	private EditText mEdit_oggetto_dett;
	
	public SetOggetto() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder oggetto_dett = new Builder(getSherlockActivity());
	    
	    oggetto_dett.setTitle(R.string.oggetto_dett_title);
	    
	    TextView tv_oggetto_dett = (TextView) getSherlockActivity().findViewById(R.id.tv_row_oggetto_dettaglio);
	    
	    mEdit_oggetto_dett = new EditText(getSherlockActivity());
	    mEdit_oggetto_dett.setText(tv_oggetto_dett.getText());
	    
	    oggetto_dett.setView(mEdit_oggetto_dett);
	    
	    oggetto_dett.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return oggetto_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    TextView tv_oggetto_dett = (TextView) getSherlockActivity().findViewById(R.id.tv_row_oggetto_dettaglio);
	    tv_oggetto_dett.setText(mEdit_oggetto_dett.getText());
	    
	    SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(DettaglioInterventoDB.Fields.OGGETTO, mEdit_oggetto_dett.getText().toString());
	    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		    "" + sId_Dettaglio_Intervento
	    };
	    
	    saveChanges.startUpdate(Constants.TOKEN_OGGETTO_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
	    
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
	    
	    dialog.dismiss();
	}
    }
    
    public static class SetDecrizione extends SherlockDialogFragment
								    implements
								    DialogInterface.OnClickListener {
	
	private EditText mEdit_descrizione_dett;
	
	public SetDecrizione() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder decrizione_dett = new Builder(getSherlockActivity());
	    
	    decrizione_dett.setTitle(R.string.oggetto_dett_title);
	    
	    TextView tv_descrizione_dett = (TextView) getSherlockActivity().findViewById(R.id.tv_row_descrizione_dettaglio);
	    
	    mEdit_descrizione_dett = new EditText(getSherlockActivity());
	    mEdit_descrizione_dett.setText(tv_descrizione_dett.getText());
	    
	    decrizione_dett.setView(mEdit_descrizione_dett);
	    
	    decrizione_dett.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return decrizione_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    TextView tv_descrizione_dett = (TextView) getSherlockActivity().findViewById(R.id.tv_row_descrizione_dettaglio);
	    tv_descrizione_dett.setText(mEdit_descrizione_dett.getText());
	    
	    SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, mEdit_descrizione_dett.getText().toString());
	    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		    "" + sId_Dettaglio_Intervento
	    };
	    
	    saveChanges.startUpdate(Constants.TOKEN_DESCRIZIONE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, true);
	    
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
	    
	    dialog.dismiss();
	}
    }
}
