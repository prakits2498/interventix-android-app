package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.intervento.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.task.GetDettaglioInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.SaveChangesDettaglioInterventoAsyncQueryHandler;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.metova.roboguice.appcompat.RoboActionBarActivity;

@SuppressLint("NewApi")
public class DetailInterventoFragment extends RoboFragment {
    
    private static long sId_Dettaglio_Intervento, sId_Intervento;
    
    private static String sNuovo_Dettaglio;
    
    private static final String DESCRIZIONE_NUOVO_DETTAGLIO = "descrizione";
    private static final String OGGETTO_NUOVO_DETTAGLIO = "oggetto";
    private static final String TIPO_NUOVO_DETTAGLIO = "tipo";
    private static final String FINE_NUOVO_DETTAGLIO = "fine";
    private static final String INTERVENTO_NUOVO_DETTAGLIO = "intervento";
    private static final String INIZIO_NUOVO_DETTAGLIO = "inizio";
    private static final String ID_NUOVO_DETTAGLIO = "iddettagliointervento";
    private static final String TECNICI_DETTAGLIO = "tecnici";
    
    private static JSONObject sNewDetail;
    
    @InjectView(R.id.tv_dett_interv)
    TextView tv_dett_interv;
    
    @InjectView(R.id.row_oggetto_dettaglio)
    View row_oggetto_dett;
    @InjectView(R.id.tv_row_oggetto_dettaglio)
    TextView tv_row_oggetto_dett;
    
    private ActionBar actionbar;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	BugSenseHandler.initAndStartSession(getActivity(), Constants.API_KEY);
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	final View view = inflater.inflate(R.layout.detail_dett_intervento_fragment, container, false);
	
	actionbar = ((RoboActionBarActivity) getActivity()).getSupportActionBar();
	
	actionbar.setHomeButtonEnabled(true);
	actionbar.setDisplayHomeAsUpEnabled(true);
	
	if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN || Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)
	    setHasOptionsMenu(true);
	
	Bundle bundle = getArguments();
	
	sId_Dettaglio_Intervento = bundle.getLong(Constants.ID_DETTAGLIO_INTERVENTO);
	sNuovo_Dettaglio = bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO);
	sId_Intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
	System.out.println("ID dettaglio = " + sId_Dettaglio_Intervento + "\n"
		+ "ID intervento = " + sId_Intervento + "\n"
		+ "Nuovo dettaglio = " + sNuovo_Dettaglio);
	
	return setupViewsDetailIntervernto(view);
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	
	super.onActivityCreated(savedInstanceState);
	
	if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
	    setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	
	super.onCreateOptionsMenu(menu, inflater);
	
	if (sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO))
	    inflater.inflate(R.menu.menu_new_detail, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	    case R.id.menu_save_detail:
		
		try {
		    System.out.println(sNewDetail.toString(2));
		    
		    AsyncQueryHandler saveNewDetail = new AsyncQueryHandler(getActivity().getContentResolver()) {
			
			@Override
			protected void onInsertComplete(int token, Object cookie, Uri uri) {
			    
			    InterventixToast.makeToast(getActivity(), "Nuovo dettaglio inserito", Toast.LENGTH_LONG);
			    
			    SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			    
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
		    };
		    
		    ContentValues values = new ContentValues();
		    
		    values.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
		    values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, sNewDetail.getString(DESCRIZIONE_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.FINE, sNewDetail.getLong(FINE_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, sNewDetail.getLong(ID_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.INIZIO, sNewDetail.getLong(INIZIO_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.MODIFICATO, "N");
		    values.put(DettaglioInterventoDB.Fields.INTERVENTO, sNewDetail.getLong(INTERVENTO_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.OGGETTO, sNewDetail.getString(OGGETTO_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.TIPO, sNewDetail.getString(TIPO_NUOVO_DETTAGLIO));
		    values.put(DettaglioInterventoDB.Fields.TECNICI, new JSONArray().toString());
		    
		    saveNewDetail.startInsert(0, null, DettaglioInterventoDB.CONTENT_URI, values);
		}
		catch (JSONException e) {
		    
		    e.printStackTrace();
		}
		
		sNewDetail = null;
		
		getActivity().getSupportFragmentManager().popBackStackImmediate();
		
		break;
	    
	    case R.id.menu_cancel_detail:
		
		InterventixToast.makeToast(getActivity(),
			"Dettaglio intervento cancellato", Toast.LENGTH_SHORT);
		
		sNewDetail = null;
		
		getActivity().getSupportFragmentManager().popBackStackImmediate();
		
		break;
	}
	
	return true;
    }
    
    private View setupViewsDetailIntervernto(final View view) {
	if (!sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO)) {
	    DettaglioIntervento dettInterv = null;
	    
	    try {
		dettInterv = new GetDettaglioInterventoAsyncTask(getActivity()).execute(sId_Dettaglio_Intervento).get();
	    }
	    catch (InterruptedException e) {
		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	    catch (ExecutionException e) {
		e.printStackTrace();
		BugSenseHandler.sendException(e);
	    }
	    
	    tv_dett_interv = (TextView) view.findViewById(R.id.tv_dett_interv);
	    
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
	    
	    row_oggetto_dett = view.findViewById(R.id.row_oggetto_dettaglio);
	    
	    row_oggetto_dett.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    
		    new SetOggetto().show(getFragmentManager(), Constants.OGGETTO_DETTAGLIO_DIALOG_FRAGMENT);
		}
	    });
	    
	    tv_row_oggetto_dett = (TextView) row_oggetto_dett.findViewById(R.id.tv_row_oggetto_dettaglio);
	    tv_row_oggetto_dett.setText(dettInterv.getmOggetto());
	    
	    View row_tecnici_dett = view.findViewById(R.id.row_tecnici_dettaglio);
	    
	    TextView tv_row_tecnici_dett = (TextView) row_tecnici_dett.findViewById(R.id.tv_row_tecnici_dettaglio);
	    tv_row_tecnici_dett.setText("" + dettInterv.getmTecnici().length());
	    
	    final JSONArray tecnici = dettInterv.getmTecnici();
	    
	    System.out.println(this.getClass().getName() + " - Array tecnici: " + tecnici.toString());
	    
	    row_tecnici_dett.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		    
		    // inserire una dialog con la lista di tutti i tecnici già
		    // associati al dettaglio e che dia la possibilità di
		    // aggiungere o togliere i tecnici. Un tasto "fatto" chiude
		    // la dialog.
		    
		    SetTecnici diag_Tecnici = new SetTecnici();
		    
		    Bundle bundle = new Bundle();
		    bundle.putString(TECNICI_DETTAGLIO, tecnici.toString());
		    
		    diag_Tecnici.setArguments(bundle);
		    
		    diag_Tecnici.show(getFragmentManager(), Constants.TECHNICIANS_DETAIL_FRAGMENT);
		}
	    });
	    
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
		    
		    final Dialog dateTimeDialog = new Dialog(getActivity());
		    
		    final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		    
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
			    
			    TextView tv_dett_ore_tot = (TextView) getActivity().findViewById(R.id.tv_row_tot_ore_dettaglio);
			    TextView tv_dett_fine = (TextView) getActivity().findViewById(R.id.tv_row_fine_dettaglio);
			    
			    DateTime dt_fine = null;
			    
			    DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			    fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			    
			    dt_fine = fmt.parseDateTime(tv_dett_fine.getText().toString());
			    
			    if (dt_fine.toDate().getTime() < dt_inizio.toDate().getTime()) {
				InterventixToast.makeToast(getActivity(), "Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
			    }
			    else {
				
				tv_row_inizio_dett.setText(dt_inizio.toString("dd/MM/yyyy HH:mm"));
				
				DateTime dt_tot_ore = dt_fine.minus(dt_inizio.toDate().getTime());
				
				tv_dett_ore_tot.setText(dt_tot_ore.toString("HH:mm"));
				
				SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
				
				ContentValues values = new ContentValues();
				values.put(DettaglioInterventoDB.Fields.INIZIO, dt_inizio.toDate().getTime());
				values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
				
				String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
				
				String[] selectionArgs = new String[] {
					DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + sId_Dettaglio_Intervento
				};
				
				saveChanges.startUpdate(Constants.TOKEN_ORA_INIZIO_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
				
				SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
				
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
		    
		    final Dialog dateTimeDialog = new Dialog(getActivity());
		    
		    final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		    
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
			    
			    TextView tv_dett_ore_tot = (TextView) getActivity().findViewById(R.id.tv_row_tot_ore_dettaglio);
			    TextView tv_dett_inizio = (TextView) getActivity().findViewById(R.id.tv_row_inizio_dettaglio);
			    
			    DateTime dt_inizio = null;
			    
			    DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			    fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			    
			    dt_inizio = fmt.parseDateTime(tv_dett_inizio.getText().toString());
			    
			    if (dt_fine.toDate().getTime() < dt_inizio.toDate().getTime()) {
				InterventixToast.makeToast(getActivity(), "Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
			    }
			    else {
				
				tv_row_fine_dett.setText(dt_fine.toString("dd/MM/yyyy HH:mm"));
				
				DateTime dt_tot_ore = dt_fine.minus(dt_inizio.toDate().getTime());
				
				tv_dett_ore_tot.setText(dt_tot_ore.toString("HH:mm"));
				
				SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
				
				ContentValues values = new ContentValues();
				values.put(DettaglioInterventoDB.Fields.FINE, dt_fine.toDate().getTime());
				values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
				
				String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
				
				String[] selectionArgs = new String[] {
					DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + sId_Dettaglio_Intervento
				};
				
				saveChanges.startUpdate(Constants.TOKEN_ORA_FINE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
				
				SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
				
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
	}
	else {
	    
	    addNewDetail(view);
	}
	
	return view;
    }
    
    private void addNewDetail(final View view) {
	
	sNewDetail = new JSONObject();
	try {
	    sNewDetail.put(ID_NUOVO_DETTAGLIO, sId_Dettaglio_Intervento);
	    sNewDetail.put(INTERVENTO_NUOVO_DETTAGLIO, sId_Intervento);
	}
	catch (JSONException e) {
	    
	    e.printStackTrace();
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
	tv_row_tipo_dett.setText("");
	
	View row_oggetto_dett = view.findViewById(R.id.row_oggetto_dettaglio);
	
	row_oggetto_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetOggetto().show(getFragmentManager(), Constants.OGGETTO_DETTAGLIO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_oggetto_dett = (TextView) row_oggetto_dett.findViewById(R.id.tv_row_oggetto_dettaglio);
	tv_row_oggetto_dett.setText("");
	
	View row_tecnici_dett = view.findViewById(R.id.row_tecnici_dettaglio);
	
	TextView tv_row_tecnici_dett = (TextView) row_tecnici_dett.findViewById(R.id.tv_row_tecnici_dettaglio);
	tv_row_tecnici_dett.setText("");
	
	row_tecnici_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		SetTecnici diag_Tecnici = new SetTecnici();
		
		Bundle bundle = new Bundle();
		
		diag_Tecnici.setArguments(bundle);
		
		diag_Tecnici.show(getFragmentManager(), Constants.TECHNICIANS_DETAIL_FRAGMENT);
	    }
	});
	
	View row_descrizione_dett = view.findViewById(R.id.row_descrizione_dettaglio);
	
	row_descrizione_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetDecrizione().show(getFragmentManager(), Constants.DESCRIZIONE_DETTAGLIO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_descrizione_dett = (TextView) row_descrizione_dett.findViewById(R.id.tv_row_descrizione_dettaglio);
	tv_row_descrizione_dett.setText("");
	
	final View row_inizio_dett = view.findViewById(R.id.row_inizio_dettaglio);
	
	row_inizio_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		final TextView tv_row_inizio_dett = (TextView) row_inizio_dett.findViewById(R.id.tv_row_inizio_dettaglio);
		
		final Dialog dateTimeDialog = new Dialog(getActivity());
		
		final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		
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
			
			TextView tv_dett_ore_tot = (TextView) getActivity().findViewById(R.id.tv_row_tot_ore_dettaglio);
			TextView tv_dett_fine = (TextView) getActivity().findViewById(R.id.tv_row_fine_dettaglio);
			
			DateTime dt_fine = null;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			
			dt_fine = fmt.parseDateTime(tv_dett_fine.getText().toString());
			
			if (dt_fine.toDate().getTime() < dt_inizio.toDate().getTime()) {
			    InterventixToast.makeToast(getActivity(), "Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
			}
			else {
			    
			    tv_row_inizio_dett.setText(dt_inizio.toString("dd/MM/yyyy HH:mm"));
			    
			    DateTime dt_tot_ore = dt_fine.minus(dt_inizio.toDate().getTime());
			    
			    tv_dett_ore_tot.setText(dt_tot_ore.toString("HH:mm"));
			    
			    if (!sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO)) {
				SaveChangesDettaglioInterventoAsyncQueryHandler
				saveChanges = new
					SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
				
				ContentValues values = new ContentValues();
				values.put(DettaglioInterventoDB.Fields.INIZIO,
					dt_inizio.toDate().getTime());
				values.put(DettaglioInterventoDB.Fields.MODIFICATO,
					"M");
				
				String selection =
					DettaglioInterventoDB.Fields.TYPE + " = ? AND " +
						DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO
						+ " = ?";
				
				String[] selectionArgs = new String[] {
					DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
					"" + sId_Dettaglio_Intervento
				};
				
				saveChanges.startUpdate(Constants.TOKEN_ORA_INIZIO_DETTAGLIO,
					null, DettaglioInterventoDB.CONTENT_URI, values,
					selection, selectionArgs);
				
				SharedPreferences prefs =
					getActivity().getSharedPreferences(Constants.PREFERENCES,
						Context.MODE_PRIVATE);
				
				final Editor edit = prefs.edit();
				
				edit.putBoolean(Constants.DETT_INTERV_MODIFIED,
					true);
				
				if (Build.VERSION.SDK_INT >=
				Build.VERSION_CODES.GINGERBREAD) {
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
			    else {
				
				try {
				    sNewDetail.put(INIZIO_NUOVO_DETTAGLIO, dt_inizio.toDate().getTime());
				}
				catch (JSONException e) {
				    
				    e.printStackTrace();
				}
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
	
	DateTime dt_inizio = new DateTime(DateTime.now(), DateTimeZone.forID("Europe/Rome"));
	
	tv_row_inizio_dett.setText(dt_inizio.toString("dd/MM/yyyy HH:mm", Locale.ITALY));
	try {
	    sNewDetail.put(INIZIO_NUOVO_DETTAGLIO, dt_inizio.getMillis());
	}
	catch (JSONException e1) {
	    e1.printStackTrace();
	}
	
	final View row_fine_dett = view.findViewById(R.id.row_fine_dettaglio);
	
	row_fine_dett.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		final TextView tv_row_fine_dett = (TextView) row_fine_dett.findViewById(R.id.tv_row_fine_dettaglio);
		
		final Dialog dateTimeDialog = new Dialog(getActivity());
		
		final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		
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
			
			TextView tv_dett_ore_tot = (TextView) getActivity().findViewById(R.id.tv_row_tot_ore_dettaglio);
			TextView tv_dett_inizio = (TextView) getActivity().findViewById(R.id.tv_row_inizio_dettaglio);
			
			DateTime dt_inizio = null;
			
			DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
			fmt.withZone(DateTimeZone.forID("Europe/Rome"));
			
			dt_inizio = fmt.parseDateTime(tv_dett_inizio.getText().toString());
			
			if (dt_fine.toDate().getTime() < dt_inizio.toDate().getTime()) {
			    InterventixToast.makeToast(getActivity(), "Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
			}
			else {
			    
			    tv_row_fine_dett.setText(dt_fine.toString("dd/MM/yyyy HH:mm"));
			    
			    DateTime dt_tot_ore = dt_fine.minus(dt_inizio.toDate().getTime());
			    
			    tv_dett_ore_tot.setText(dt_tot_ore.toString("HH:mm"));
			    
			    if (!sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO)) {
				SaveChangesDettaglioInterventoAsyncQueryHandler
				saveChanges = new
					SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
				
				ContentValues values = new ContentValues();
				values.put(DettaglioInterventoDB.Fields.FINE,
					dt_fine.toDate().getTime());
				values.put(DettaglioInterventoDB.Fields.MODIFICATO,
					"M");
				
				String selection =
					DettaglioInterventoDB.Fields.TYPE + " = ? AND " +
						DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO
						+ " = ?";
				
				String[] selectionArgs = new String[] {
					DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
					"" + sId_Dettaglio_Intervento
				};
				
				saveChanges.startUpdate(Constants.TOKEN_ORA_FINE_DETTAGLIO,
					null, DettaglioInterventoDB.CONTENT_URI, values,
					selection, selectionArgs);
				
				SharedPreferences prefs =
					getActivity().getSharedPreferences(Constants.PREFERENCES,
						Context.MODE_PRIVATE);
				
				final Editor edit = prefs.edit();
				
				edit.putBoolean(Constants.DETT_INTERV_MODIFIED,
					true);
				
				if (Build.VERSION.SDK_INT >=
				Build.VERSION_CODES.GINGERBREAD) {
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
			    else {
				
				try {
				    sNewDetail.put(FINE_NUOVO_DETTAGLIO, dt_fine.toDate().getTime());
				}
				catch (JSONException e) {
				    
				    e.printStackTrace();
				}
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
	
	DateTime dt_fine = new DateTime(DateTime.now(), DateTimeZone.forID("Europe/Rome"));
	
	tv_row_fine_dett.setText(dt_fine.toString("dd/MM/yyyy HH:mm", Locale.ITALY));
	try {
	    sNewDetail.put(FINE_NUOVO_DETTAGLIO, dt_fine.getMillis());
	}
	catch (JSONException e1) {
	    e1.printStackTrace();
	}
	
	View row_tot_ore_dett = view.findViewById(R.id.row_tot_ore_dettaglio);
	
	TextView tv_row_tot_ore_dett = (TextView) row_tot_ore_dett.findViewById(R.id.tv_row_tot_ore_dettaglio);
	
	DateTime dt_tot_ore = new DateTime(dt_fine.toDate().getTime() - dt_inizio.toDate().getTime(), DateTimeZone.forID("Europe/Rome"));
	
	tv_row_tot_ore_dett.setText(dt_tot_ore.toString(DateTimeFormat.forPattern("HH:mm")));
    }
    
    public static class SetTipo extends DialogFragment implements DialogInterface.OnClickListener {
	
	private String mTipologiaChanged;
	
	public SetTipo() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder tipo_dett = new Builder(getActivity());
	    
	    tipo_dett.setTitle(R.string.tipo_dett_title);
	    
	    final String[] tipos = getResources().getStringArray(R.array.tipo_dettaglio_intervento);
	    
	    tipo_dett.setSingleChoiceItems(tipos, -1, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
		    TextView tv_tipology = (TextView) getActivity().findViewById(R.id.tv_row_tipo_dettaglio);
		    tv_tipology.setText(tipos[which]);
		    mTipologiaChanged = tipos[which];
		}
	    });
	    tipo_dett.setCancelable(false);
	    tipo_dett.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return tipo_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    if (!sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO)) {
		SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
		
		ContentValues values = new ContentValues();
		values.put(DettaglioInterventoDB.Fields.TIPO, mTipologiaChanged);
		values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
		
		String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		
		String[] selectionArgs = new String[] {
			DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + sId_Dettaglio_Intervento
		};
		
		saveChanges.startUpdate(Constants.TOKEN_TIPO_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
		
		SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
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
	    else {
		
		try {
		    sNewDetail.put(TIPO_NUOVO_DETTAGLIO, mTipologiaChanged);
		}
		catch (JSONException e) {
		    
		    e.printStackTrace();
		}
	    }
	    
	    dialog.dismiss();
	}
    }
    
    public static class SetOggetto extends DialogFragment implements DialogInterface.OnClickListener {
	
	private EditText mEdit_oggetto_dett;
	
	public SetOggetto() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder oggetto_dett = new Builder(getActivity());
	    
	    oggetto_dett.setTitle(R.string.oggetto_dett_title);
	    
	    TextView tv_oggetto_dett = (TextView) getActivity().findViewById(R.id.tv_row_oggetto_dettaglio);
	    
	    mEdit_oggetto_dett = new EditText(getActivity());
	    mEdit_oggetto_dett.setText(tv_oggetto_dett.getText());
	    
	    oggetto_dett.setView(mEdit_oggetto_dett);
	    oggetto_dett.setCancelable(false);
	    oggetto_dett.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return oggetto_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    if (!sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO)) {
		TextView tv_oggetto_dett = (TextView) getActivity().findViewById(R.id.tv_row_oggetto_dettaglio);
		tv_oggetto_dett.setText(mEdit_oggetto_dett.getText());
		
		SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
		
		ContentValues values = new ContentValues();
		values.put(DettaglioInterventoDB.Fields.OGGETTO, mEdit_oggetto_dett.getText().toString());
		values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
		
		String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		
		String[] selectionArgs = new String[] {
			DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + sId_Dettaglio_Intervento
		};
		
		saveChanges.startUpdate(Constants.TOKEN_OGGETTO_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
		
		SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
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
	    else {
		
		TextView tv_oggetto_dett = (TextView) getActivity().findViewById(R.id.tv_row_oggetto_dettaglio);
		tv_oggetto_dett.setText(mEdit_oggetto_dett.getText());
		
		try {
		    sNewDetail.put(OGGETTO_NUOVO_DETTAGLIO, mEdit_oggetto_dett.getText().toString());
		}
		catch (JSONException e) {
		    
		    e.printStackTrace();
		}
	    }
	    
	    dialog.dismiss();
	}
    }
    
    public static class SetDecrizione extends DialogFragment implements DialogInterface.OnClickListener {
	
	private EditText mEdit_descrizione_dett;
	
	public SetDecrizione() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder descrizione_dett = new Builder(getActivity());
	    
	    descrizione_dett.setTitle(R.string.oggetto_dett_title);
	    
	    TextView tv_descrizione_dett = (TextView) getActivity().findViewById(R.id.tv_row_descrizione_dettaglio);
	    
	    mEdit_descrizione_dett = new EditText(getActivity());
	    mEdit_descrizione_dett.setText(tv_descrizione_dett.getText());
	    
	    descrizione_dett.setView(mEdit_descrizione_dett);
	    descrizione_dett.setCancelable(false);
	    descrizione_dett.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return descrizione_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    if (!sNuovo_Dettaglio.equals(Constants.NUOVO_DETTAGLIO_INTERVENTO)) {
		TextView tv_descrizione_dett = (TextView) getActivity().findViewById(R.id.tv_row_descrizione_dettaglio);
		tv_descrizione_dett.setText(mEdit_descrizione_dett.getText());
		
		SaveChangesDettaglioInterventoAsyncQueryHandler saveChanges = new SaveChangesDettaglioInterventoAsyncQueryHandler(getActivity());
		
		ContentValues values = new ContentValues();
		values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, mEdit_descrizione_dett.getText().toString());
		values.put(DettaglioInterventoDB.Fields.MODIFICATO, "M");
		
		String selection = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + " = ?";
		
		String[] selectionArgs = new String[] {
			DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + sId_Dettaglio_Intervento
		};
		
		saveChanges.startUpdate(Constants.TOKEN_DESCRIZIONE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, values, selection, selectionArgs);
		
		SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
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
	    else {
		
		TextView tv_descrizione_dett = (TextView) getActivity().findViewById(R.id.tv_row_descrizione_dettaglio);
		tv_descrizione_dett.setText(mEdit_descrizione_dett.getText());
		
		try {
		    sNewDetail.put(DESCRIZIONE_NUOVO_DETTAGLIO, mEdit_descrizione_dett.getText().toString());
		}
		catch (JSONException e) {
		    
		    e.printStackTrace();
		}
	    }
	    
	    dialog.dismiss();
	}
    }
    
    public static class SetTecnici extends DialogFragment implements DialogInterface.OnClickListener {
	
	private String mArrayTecnici;
	
	public SetTecnici() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    Bundle bundle = getArguments();
	    
	    mArrayTecnici = bundle.getString(TECNICI_DETTAGLIO);
	    
	    JSONArray tecnici = null;
	    
	    try {
		
		if (mArrayTecnici != null)
		    tecnici = new JSONArray(mArrayTecnici);
		else
		    tecnici = new JSONArray();
	    }
	    catch (JSONException e) {
		e.printStackTrace();
	    }
	    
	    String[] tuttiTecnici = null;
	    
	    try {
		tuttiTecnici = getAllTecnici();
	    }
	    catch (InterruptedException e) {
		
		e.printStackTrace();
	    }
	    catch (ExecutionException e) {
		
		e.printStackTrace();
	    }
	    
	    final boolean[] tecniciChecked = new boolean[tuttiTecnici.length];
	    
	    for (int i = 0; i < tuttiTecnici.length; i++) {
		
		try {
		    if (tuttiTecnici[i].equals(tecnici.get(i)))
			tecniciChecked[i] = true;
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
	    }
	    
	    AlertDialog.Builder tecnici_dett = new Builder(getActivity());
	    
	    tecnici_dett.setTitle(R.string.choose_tecnici_title);
	    
	    tecnici_dett.setMultiChoiceItems(tuttiTecnici, tecniciChecked, new OnMultiChoiceClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		    
		    if (isChecked)
			tecniciChecked[which] = false;
		    else
			tecniciChecked[which] = true;
		}
	    });
	    
	    return tecnici_dett.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    dialog.dismiss();
	}
	
	private String[] getAllTecnici() throws InterruptedException, ExecutionException {
	    
	    // restituisce un array degli ID di tutti i tecnici
	    
	    AsyncTask<Void, Void, String[]> tuttiTecnici = new AsyncTask<Void, Void, String[]>() {
		
		@Override
		protected String[] doInBackground(Void... params) {
		    
		    ContentResolver cr = getActivity().getContentResolver();
		    
		    String[] projection = new String[] {
			    UtenteDB.Fields._ID, UtenteDB.Fields.ID_UTENTE
		    };
		    
		    String selection = UtenteDB.Fields.TYPE + "=?";
		    
		    String[] selectionArgs = new String[] {
			    UtenteDB.UTENTE_ITEM_TYPE
		    };
		    
		    String sortOrder = UtenteDB.Fields.COGNOME + " asc";
		    
		    Cursor cursor = cr.query(UtenteDB.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
		    
		    ArrayList<String> arrayTecnici = new ArrayList<String>();
		    
		    while (cursor.moveToNext()) {
			
			arrayTecnici.add("" + cursor.getLong(cursor.getColumnIndex(UtenteDB.Fields.ID_UTENTE)));
		    }
		    
		    System.out.println("Tutti i tecnici: " + arrayTecnici.toString());
		    
		    if (!cursor.isClosed())
			cursor.close();
		    
		    String[] tecnici = new String[arrayTecnici.size()];
		    
		    return arrayTecnici.toArray(tecnici);
		}
		
		@Override
		protected void onPostExecute(String[] result) {
		    
		}
	    };
	    
	    tuttiTecnici.execute();
	    
	    return tuttiTecnici.get();
	}
    }
}
