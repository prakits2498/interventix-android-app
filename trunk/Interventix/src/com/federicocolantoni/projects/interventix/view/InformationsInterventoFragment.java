
package com.federicocolantoni.projects.interventix.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import com.federicocolantoni.projects.interventix.data.DBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;
import com.federicocolantoni.projects.interventix.utils.GetInformationsIntervento;
import com.federicocolantoni.projects.interventix.utils.SaveChangesIntervento;

@SuppressLint("NewApi")
public class InformationsInterventoFragment extends SherlockFragment {

    public static final int TOKEN_NOMINATIVO = 5;
    public static final int TOKEN_PRODOTTO = 4;
    public static final int TOKEN_MODALITA = 3;

    public static long id_intervento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	BugSenseHandler.initAndStartSession(getSherlockActivity(),
		Constants.API_KEY);

	super.onCreateView(inflater, container, savedInstanceState);

	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
		true);

	final View view = inflater.inflate(
		R.layout.information_intervento_fragment, container, false);

	Bundle bundle = getArguments();

	id_intervento = bundle.getLong(Constants.ID_INTERVENTO);

	Intervento interv = null;

	try {
	    interv = new GetInformationsIntervento(getSherlockActivity())
		    .execute(id_intervento).get();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	} catch (ExecutionException e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}

	TextView info_interv = (TextView) view
		.findViewById(R.id.tv_info_intervention);
	info_interv.setText("Informazioni Intervento " + id_intervento);

	View tipologia = view.findViewById(R.id.row_tipology);
	tipologia.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		new SetTipologia().show(getFragmentManager(),
			Constants.TIPOLOGIA_DIALOG_FRAGMENT);
	    }
	});
	//	tipologia.setOnTouchListener(new OnTouchListener() {
	//
	//	    @Override
	//	    public boolean onTouch(View v, MotionEvent event) {
	//
	//		switch (event.getAction()) {
	//		    case MotionEvent.ACTION_MOVE:
	//
	//			break;
	//		}
	//
	//		return true;
	//	    }
	//	});

	TextView tv_tipology = (TextView) tipologia
		.findViewById(R.id.tv_row_tipology);
	tv_tipology.setText(interv.getmTipologia());

	View mode = view.findViewById(R.id.row_mode);
	mode.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		new SetModalita().show(getFragmentManager(),
			Constants.MODALITA_DIALOG_FRAGMENT);
	    }
	});
	//	mode.setOnTouchListener(new OnTouchListener() {
	//
	//	    @Override
	//	    public boolean onTouch(View v, MotionEvent event) {
	//
	//		switch (event.getAction()) {
	//		    case MotionEvent.ACTION_MOVE:
	//
	//			break;
	//		}
	//
	//		return true;
	//	    }
	//	});

	TextView tv_mode = (TextView) mode.findViewById(R.id.tv_row_mode);
	tv_mode.setText(interv.getmModalita());

	View product = view.findViewById(R.id.row_product);
	product.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		new SetProdotto().show(getFragmentManager(),
			Constants.PRODOTTO_DIALOG_FRAGMENT);
	    }
	});
	//	product.setOnTouchListener(new OnTouchListener() {
	//
	//	    @Override
	//	    public boolean onTouch(View v, MotionEvent event) {
	//
	//		switch (event.getAction()) {
	//		    case MotionEvent.ACTION_MOVE:
	//
	//			break;
	//		}
	//
	//		return true;
	//	    }
	//	});

	TextView tv_product = (TextView) product
		.findViewById(R.id.tv_row_product);
	tv_product.setText(interv.getmProdotto());

	View motivation = view.findViewById(R.id.row_motivation);
	motivation.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

	    }
	});
	//	motivation.setOnTouchListener(new OnTouchListener() {
	//
	//	    @Override
	//	    public boolean onTouch(View v, MotionEvent event) {
	//
	//		switch (event.getAction()) {
	//		    case MotionEvent.ACTION_MOVE:
	//
	//			break;
	//		}
	//
	//		return true;
	//	    }
	//	});

	TextView tv_motivation = (TextView) motivation
		.findViewById(R.id.tv_row_motivation);
	tv_motivation.setText(interv.getmMotivo());

	View nominativo = view.findViewById(R.id.row_name);
	nominativo.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		new SetNominativo().show(getFragmentManager(),
			Constants.NOMINATIVO_DIALOG_FRAGMENT);
	    }
	});
	//	nominativo.setOnTouchListener(new OnTouchListener() {
	//
	//	    @Override
	//	    public boolean onTouch(View v, MotionEvent event) {
	//
	//		switch (event.getAction()) {
	//		    case MotionEvent.ACTION_MOVE:
	//
	//			break;
	//		}
	//
	//		return true;
	//	    }
	//	});

	TextView tv_nominativo = (TextView) nominativo
		.findViewById(R.id.tv_row_name);
	tv_nominativo.setText(interv.getmNominativo());

	final View date_interv = view.findViewById(R.id.row_date);

	date_interv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		//		new DateTimePicker().show(getFragmentManager(),
		//			Constants.DATAORA_DIALOG_FRAGMENT);

		final TextView tv_date_interv = (TextView) date_interv
			.findViewById(R.id.tv_row_date);

		final Dialog dateTimeDialog = new Dialog(getSherlockActivity());

		final RelativeLayout dateTimeDialogView = (RelativeLayout) getSherlockActivity()
			.getLayoutInflater().inflate(R.layout.date_time_dialog,
				null);

		final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView
			.findViewById(R.id.DateTimePicker);

		Date date = null;
		try {
		    date = new SimpleDateFormat("dd/MM/yyyy HH:mm",
			    Locale.ITALY).parse(tv_date_interv.getText()
			    .toString());
		} catch (ParseException e) {
		    e.printStackTrace();
		}

		dateTimePicker.setDateTime(date);

		dateTimePicker.setDateChangedListener(new DateWatcher() {

		    @Override
		    public void onDateChanged(Calendar c) {

		    }
		});

		((Button) dateTimeDialogView.findViewById(R.id.SetDateTime))
			.setOnClickListener(new View.OnClickListener() {

			    @Override
			    public void onClick(View v) {

				dateTimePicker.clearFocus();

				//				String result_string = String
				//					.valueOf(dateTimePicker.getDay())
				//					+ "/"
				//					+ dateTimePicker.getMonth()
				//					+ "/"
				//					+ String.valueOf(dateTimePicker
				//						.getYear())
				//					+ "  "
				//					+ String.valueOf(dateTimePicker
				//						.getHour())
				//					+ ":"
				//					+ String.valueOf(dateTimePicker
				//						.getMinute());
				//				if(mDateTimePicker.getHour() > 12) result_string = result_string + "PM";
				//				else result_string = result_string + "AM";

				//				String format = "dd/MMM/yyyy hh:mm";

				DateTime dt = new DateTime(dateTimePicker
					.getYear(), dateTimePicker.getMonth(),
					dateTimePicker.getDay(), dateTimePicker
						.getHour(), dateTimePicker
						.getMinute());

				tv_date_interv.setText(dt.toString(
					"dd/MM/yyyy HH:mm", Locale.ITALY));

				SaveChangesIntervento saveChange = new SaveChangesIntervento(
					getSherlockActivity());

				Date newDate = dt.toDate();

				ContentValues values = new ContentValues();
				values.put(InterventoDB.Fields.DATA_ORA,
					newDate.getTime());

				String selection = InterventoDB.Fields.TYPE
					+ " = '"
					+ InterventoDB.INTERVENTO_ITEM_TYPE
					+ "' AND "
					+ InterventoDB.Fields.ID_INTERVENTO
					+ " = ?";

				String[] selectionArgs = new String[] { ""
					+ id_intervento };

				saveChange.startUpdate(TOKEN_NOMINATIVO, null,
					InterventoDB.CONTENT_URI, values,
					selection, selectionArgs);
				dateTimeDialog.dismiss();
			    }
			});

		((Button) dateTimeDialogView.findViewById(R.id.CancelDialog))
			.setOnClickListener(new View.OnClickListener() {

			    @Override
			    public void onClick(View v) {

				dateTimeDialog.cancel();
			    }
			});

		((Button) dateTimeDialogView.findViewById(R.id.ResetDateTime))
			.setOnClickListener(new View.OnClickListener() {

			    @Override
			    public void onClick(View v) {

				Date date = null;
				try {
				    date = new SimpleDateFormat(
					    "dd/MM/yyyy HH:mm", Locale.ITALY)
					    .parse(tv_date_interv.getText()
						    .toString());
				} catch (ParseException e) {
				    e.printStackTrace();
				}

				//				dateTimePicker.reset();
				dateTimePicker.setDateTime(date);
			    }
			});

		dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialog.setContentView(dateTimeDialogView);
		dateTimeDialog.setCancelable(false);
		dateTimeDialog.show();
	    }
	});

	TextView tv_date_interv = (TextView) date_interv
		.findViewById(R.id.tv_row_date);

	DateTime dt = new DateTime(interv.getmDataOra());

	tv_date_interv.setText(dt.toString("dd/MM/yyyy HH:mm", Locale.ITALY));

	//	tv_date_interv.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm",
	//		Locale.ITALY).format(new Date(interv.getmDataOra())));

	return view;
    }

    @Override
    public void onPause() {

	super.onPause();

	System.out.println("InformationsInterventoFragment - paused");
    }

    @Override
    public void onResume() {

	super.onResume();

	System.out.println("InformationsInterventoFragment - resumed");
    }

    public static class SetTipologia extends SherlockDialogFragment implements
	    OnClickListener {

	private String tipologiaChanged;

	public SetTipologia() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder tipologia = new Builder(getSherlockActivity());

	    tipologia.setTitle(getResources().getString(
		    R.string.tipologia_title));
	    final String[] choices = getResources().getStringArray(
		    R.array.tipologia_choose);
	    tipologia.setSingleChoiceItems(choices, -1,
		    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			    //			    if (which == 0) {
			    //				TextView tv_tipology = (TextView) getSherlockActivity()
			    //					.findViewById(R.id.tv_row_tipology);
			    //				tv_tipology.setText(choices[which]);
			    //			    } else {
			    TextView tv_tipology = (TextView) getSherlockActivity()
				    .findViewById(R.id.tv_row_tipology);
			    tv_tipology.setText(choices[which]);
			    tipologiaChanged = choices[which];
			    //			    }
			}
		    });

	    tipologia.setPositiveButton(
		    getResources().getString(R.string.ok_btn), this);

	    return tipologia.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    SaveChangesIntervento saveChange = new SaveChangesIntervento(
		    getSherlockActivity());

	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.TIPOLOGIA, tipologiaChanged);

	    String selection = InterventoDB.Fields.TYPE + " = '"
		    + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND "
		    + InterventoDB.Fields.ID_INTERVENTO + " = ?";

	    String[] selectionArgs = new String[] { "" + id_intervento };

	    saveChange.startUpdate(TOKEN_MODALITA, null,
		    InterventoDB.CONTENT_URI, values, selection, selectionArgs);

	    dialog.dismiss();
	}
    }

    public static class SetModalita extends SherlockDialogFragment implements
	    OnClickListener {

	private String modalitaChanged;

	public SetModalita() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder modalita = new Builder(getSherlockActivity());

	    modalita.setTitle(getResources().getString(R.string.modalita_title));
	    final String[] choices = getResources().getStringArray(
		    R.array.modalita_choose);

	    modalita.setSingleChoiceItems(choices, -1,
		    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			    //			    if (which == 0) {
			    //				TextView tv_mode = (TextView) getSherlockActivity()
			    //					.findViewById(R.id.tv_row_mode);
			    //				tv_mode.setText(choices[which]);
			    //				modalitaChanged=choices[which];
			    //			    } else {
			    TextView tv_mode = (TextView) getSherlockActivity()
				    .findViewById(R.id.tv_row_mode);
			    tv_mode.setText(choices[which]);
			    modalitaChanged = choices[which];
			    //			    }
			}
		    });

	    modalita.setPositiveButton(getResources()
		    .getString(R.string.ok_btn), this);

	    return modalita.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    SaveChangesIntervento saveChange = new SaveChangesIntervento(
		    getSherlockActivity());

	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.MODALITA, modalitaChanged);

	    String selection = InterventoDB.Fields.TYPE + " = '"
		    + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND "
		    + InterventoDB.Fields.ID_INTERVENTO + " = ?";

	    String[] selectionArgs = new String[] { "" + id_intervento };

	    saveChange.startUpdate(TOKEN_MODALITA, null,
		    InterventoDB.CONTENT_URI, values, selection, selectionArgs);

	    dialog.dismiss();
	}
    }

    public static class SetProdotto extends SherlockDialogFragment implements
	    OnClickListener {

	private EditText edit_prodotto;

	public SetProdotto() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder prodotto = new Builder(getSherlockActivity());

	    prodotto.setTitle(R.string.prodotto_title);

	    TextView tv_prodotto = (TextView) getSherlockActivity()
		    .findViewById(R.id.tv_row_product);

	    edit_prodotto = new EditText(getSherlockActivity());
	    edit_prodotto.setText(tv_prodotto.getText());

	    prodotto.setView(edit_prodotto);

	    prodotto.setPositiveButton(getResources()
		    .getString(R.string.ok_btn), this);

	    return prodotto.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    TextView tv_product = (TextView) getSherlockActivity()
		    .findViewById(R.id.tv_row_product);
	    tv_product.setText(edit_prodotto.getText());

	    SaveChangesIntervento saveChange = new SaveChangesIntervento(
		    getSherlockActivity());

	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.PRODOTTO, edit_prodotto.getText()
		    .toString());

	    String selection = InterventoDB.Fields.TYPE + " = '"
		    + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND "
		    + InterventoDB.Fields.ID_INTERVENTO + " = ?";

	    String[] selectionArgs = new String[] { "" + id_intervento };

	    saveChange.startUpdate(TOKEN_PRODOTTO, null,
		    InterventoDB.CONTENT_URI, values, selection, selectionArgs);

	    dialog.dismiss();
	}
    }

    public static class SetNominativo extends SherlockDialogFragment implements
	    OnClickListener {

	private EditText edit_nominativo;

	public SetNominativo() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder nominativo = new Builder(getSherlockActivity());

	    nominativo.setTitle(R.string.nominativo_title);

	    TextView tv_nominativo = (TextView) getSherlockActivity()
		    .findViewById(R.id.tv_row_name);

	    edit_nominativo = new EditText(getSherlockActivity());
	    edit_nominativo.setText(tv_nominativo.getText());

	    nominativo.setView(edit_nominativo);

	    nominativo.setPositiveButton(
		    getResources().getString(R.string.ok_btn), this);

	    return nominativo.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    TextView tv_name = (TextView) getSherlockActivity().findViewById(
		    R.id.tv_row_name);
	    tv_name.setText(edit_nominativo.getText());

	    SaveChangesIntervento saveChange = new SaveChangesIntervento(
		    getSherlockActivity());

	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.NOMINATIVO, edit_nominativo
		    .getText().toString());

	    String selection = InterventoDB.Fields.TYPE + " = '"
		    + InterventoDB.INTERVENTO_ITEM_TYPE + "' AND "
		    + InterventoDB.Fields.ID_INTERVENTO + " = ?";

	    String[] selectionArgs = new String[] { "" + id_intervento };

	    saveChange.startUpdate(TOKEN_NOMINATIVO, null,
		    InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    dialog.dismiss();
	}
    }

    //    public static class DateTimePicker extends SherlockDialogFragment implements
    //	    OnTimeSetListener {
    //
    //	@Override
    //	public Dialog onCreateDialog(Bundle savedInstanceState) {
    //
    //	    // Use the current time as the default values for the picker
    //	    final Calendar c = Calendar.getInstance();
    //	    int hour = c.get(Calendar.HOUR_OF_DAY);
    //	    int minute = c.get(Calendar.MINUTE);
    //
    //	    // Create a new instance of TimePickerDialog and return it
    //	    return new TimePickerDialog(getSherlockActivity(), this, hour,
    //		    minute, DateFormat.is24HourFormat(getSherlockActivity()));
    //	}
    //
    //	@Override
    //	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    //
    //	}
    //    }
}
