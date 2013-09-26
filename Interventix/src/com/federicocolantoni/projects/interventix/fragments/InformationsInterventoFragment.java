package com.federicocolantoni.projects.interventix.fragments;

import java.util.Calendar;
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
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragment;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetInformationsInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.SaveChangesInterventoAsyncQueryHandler;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;

@SuppressLint("NewApi")
public class InformationsInterventoFragment extends SherlockFragment {
    
    public static long sId_Intervento;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	
	BugSenseHandler.initAndStartSession(getSherlockActivity(), Constants.API_KEY);
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	final View view = inflater.inflate(R.layout.information_intervento_fragment, container, false);
	
	Bundle bundle = getArguments();
	
	sId_Intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
	Intervento interv = null;
	
	try {
	    interv = new GetInformationsInterventoAsyncTask(getSherlockActivity()).execute(sId_Intervento).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	TextView info_interv = (TextView) view.findViewById(R.id.tv_info_intervention);
	info_interv.setText("Informazioni Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	View tipologia = view.findViewById(R.id.row_tipology);
	tipologia.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetTipologiaDialog().show(getFragmentManager(), Constants.TIPOLOGIA_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_tipology = (TextView) tipologia.findViewById(R.id.tv_row_tipology);
	tv_tipology.setText(interv.getmTipologia());
	
	View mode = view.findViewById(R.id.row_mode);
	mode.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetModalitaDialog().show(getFragmentManager(), Constants.MODALITA_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_mode = (TextView) mode.findViewById(R.id.tv_row_mode);
	tv_mode.setText(interv.getmModalita());
	
	View product = view.findViewById(R.id.row_product);
	product.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetProdottoDialog().show(getFragmentManager(), Constants.PRODOTTO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_product = (TextView) product.findViewById(R.id.tv_row_product);
	tv_product.setText(interv.getmProdotto());
	
	View motivation = view.findViewById(R.id.row_motivation);
	motivation.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetMotivationDialog().show(getFragmentManager(), Constants.MOTIVO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_motivation = (TextView) motivation.findViewById(R.id.tv_row_motivation);
	tv_motivation.setText(interv.getmMotivo());
	
	View nominativo = view.findViewById(R.id.row_name);
	nominativo.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		new SetNominativoDialog().show(getFragmentManager(), Constants.NOMINATIVO_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_nominativo = (TextView) nominativo.findViewById(R.id.tv_row_name);
	tv_nominativo.setText(interv.getmNominativo());
	
	final View date_interv = view.findViewById(R.id.row_date);
	
	date_interv.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		final TextView tv_date_interv = (TextView) date_interv.findViewById(R.id.tv_row_date);
		
		final Dialog dateTimeDialog = new Dialog(getSherlockActivity());
		
		final RelativeLayout dateTimeDialogView = (RelativeLayout) getSherlockActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		
		final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);
		
		DateTime dt = null;
		
		dt = DateTime.parse(tv_date_interv.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
		
		dateTimePicker.setDateTime(dt);
		
		dateTimePicker.setDateChangedListener(new DateWatcher() {
		    
		    @Override
		    public void onDateChanged(Calendar c) {
			
		    }
		});
		
		((Button) dateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {
		    
		    @Override
		    public void onClick(View v) {
			
			dateTimePicker.clearFocus();
			
			DateTime dt = new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(), DateTimeZone.forID("Europe/Rome"));
			
			tv_date_interv.setText(dt.toString("dd/MM/yyyy HH:mm"));
			
			SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getSherlockActivity());
			
			ContentValues values = new ContentValues();
			values.put(InterventoDB.Fields.DATA_ORA, dt.toDate().getTime());
			values.put(InterventoDB.Fields.MODIFICATO, "M");
			
			String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
			
			String[] selectionArgs = new String[] {
				InterventoDB.INTERVENTO_ITEM_TYPE,
				"" + sId_Intervento
			};
			
			saveChange.startUpdate(Constants.TOKEN_INFO_DATA_ORA, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
			
			SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			
			final Editor edit = prefs.edit();
			
			edit.putBoolean(Constants.INTERV_MODIFIED, true);
			
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
			
			dt = fmt.parseDateTime(tv_date_interv.getText().toString());
			
			dateTimePicker.setDateTime(dt);
		    }
		});
		
		dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialog.setContentView(dateTimeDialogView);
		dateTimeDialog.setCancelable(false);
		dateTimeDialog.show();
	    }
	});
	
	TextView tv_date_interv = (TextView) date_interv.findViewById(R.id.tv_row_date);
	
	DateTime dt = new DateTime(interv.getmDataOra(), DateTimeZone.forID("Europe/Rome"));
	
	tv_date_interv.setText(dt.toString("dd/MM/yyyy HH:mm"));
	
	return view;
    }
    
    @Override
    public void onPause() {
	
	super.onPause();
    }
    
    @Override
    public void onResume() {
	
	super.onResume();
    }
    
    public static class SetTipologiaDialog extends SherlockDialogFragment
									 implements
									 OnClickListener {
	
	private String mTipologiaChanged;
	
	public SetTipologiaDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder tipologia = new Builder(getSherlockActivity());
	    
	    tipologia.setTitle(getResources().getString(R.string.tipologia_title));
	    final String[] choices = getResources().getStringArray(R.array.tipologia_choose);
	    tipologia.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
		    
		    TextView tv_tipology = (TextView) getSherlockActivity().findViewById(R.id.tv_row_tipology);
		    tv_tipology.setText(choices[which]);
		    mTipologiaChanged = choices[which];
		}
	    });
	    
	    tipologia.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return tipologia.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.TIPOLOGIA, mTipologiaChanged);
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		"" + sId_Intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_INFO_TIPOLOGIA, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.INTERV_MODIFIED, true);
	    
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
    
    public static class SetModalitaDialog extends SherlockDialogFragment
									implements
									OnClickListener {
	
	private String mModalitaChanged;
	
	public SetModalitaDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder modalita = new Builder(getSherlockActivity());
	    
	    modalita.setTitle(getResources().getString(R.string.modalita_title));
	    final String[] choices = getResources().getStringArray(R.array.modalita_choose);
	    
	    modalita.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
		    
		    TextView tv_mode = (TextView) getSherlockActivity().findViewById(R.id.tv_row_mode);
		    tv_mode.setText(choices[which]);
		    mModalitaChanged = choices[which];
		}
	    });
	    
	    modalita.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return modalita.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.MODALITA, mModalitaChanged);
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		"" + sId_Intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_INFO_MODALITA, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.INTERV_MODIFIED, true);
	    
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
    
    public static class SetProdottoDialog extends SherlockDialogFragment
									implements
									OnClickListener {
	
	private EditText mEdit_prodotto;
	
	public SetProdottoDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder prodotto = new Builder(getSherlockActivity());
	    
	    prodotto.setTitle(R.string.prodotto_title);
	    
	    TextView tv_product = (TextView) getSherlockActivity().findViewById(R.id.tv_row_product);
	    
	    mEdit_prodotto = new EditText(getSherlockActivity());
	    mEdit_prodotto.setText(tv_product.getText());
	    
	    prodotto.setView(mEdit_prodotto);
	    
	    prodotto.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return prodotto.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    TextView tv_product = (TextView) getSherlockActivity().findViewById(R.id.tv_row_product);
	    tv_product.setText(mEdit_prodotto.getText());
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.PRODOTTO, mEdit_prodotto.getText().toString());
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		"" + sId_Intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_INFO_PRODOTTO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.INTERV_MODIFIED, true);
	    
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
    
    public static class SetNominativoDialog extends SherlockDialogFragment
									  implements
									  OnClickListener {
	
	private EditText mEdit_nominativo;
	
	public SetNominativoDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder nominativo = new Builder(getSherlockActivity());
	    
	    nominativo.setTitle(R.string.nominativo_title);
	    
	    TextView tv_nominativo = (TextView) getSherlockActivity().findViewById(R.id.tv_row_name);
	    
	    mEdit_nominativo = new EditText(getSherlockActivity());
	    mEdit_nominativo.setText(tv_nominativo.getText());
	    
	    nominativo.setView(mEdit_nominativo);
	    
	    nominativo.setPositiveButton(getResources().getString(R.string.ok_btn), this);
	    
	    return nominativo.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    TextView tv_name = (TextView) getSherlockActivity().findViewById(R.id.tv_row_name);
	    tv_name.setText(mEdit_nominativo.getText());
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.NOMINATIVO, mEdit_nominativo.getText().toString());
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		"" + sId_Intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_INFO_NOMINATIVO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.INTERV_MODIFIED, true);
	    
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
    
    public static class SetMotivationDialog extends SherlockDialogFragment
									  implements
									  OnClickListener {
	
	private EditText mEdit_motivo;
	
	public SetMotivationDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder motivo = new Builder(getSherlockActivity());
	    
	    motivo.setTitle(R.string.motivation_title);
	    
	    TextView tv_motivo = (TextView) getSherlockActivity().findViewById(R.id.tv_row_motivation);
	    
	    mEdit_motivo = new EditText(getSherlockActivity());
	    mEdit_motivo.setText(tv_motivo.getText());
	    
	    motivo.setView(mEdit_motivo);
	    
	    motivo.setPositiveButton(R.string.ok_btn, this);
	    
	    return motivo.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    TextView tv_name = (TextView) getSherlockActivity().findViewById(R.id.tv_row_motivation);
	    tv_name.setText(mEdit_motivo.getText());
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getSherlockActivity());
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.MOTIVO, mEdit_motivo.getText().toString());
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = Fields.TYPE + " = '" + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		"" + sId_Intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_INFO_MOTIVO, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    edit.putBoolean(Constants.INTERV_MODIFIED, true);
	    
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
