package com.federicocolantoni.projects.interventix.ui.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog.OnDateSetListener;
import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog.OnTimeSetListener;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.ManagedAsyncTask;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_detail)
public class DetailInterventoFragment extends Fragment implements OnDateSetListener, OnTimeSetListener {

    @ViewById(R.id.row_tipo_dettaglio)
    View rowTipo;

    @ViewById(R.id.tv_row_tipo_dettaglio)
    TextView tvRowTipo;

    @ViewById(R.id.row_oggetto_dettaglio)
    View rowOggetto;

    @ViewById(R.id.tv_row_oggetto_dettaglio)
    TextView tvRowOggetto;

    @ViewById(R.id.row_descrizione_dettaglio)
    View rowDescr;

    @ViewById(R.id.tv_row_descrizione_dettaglio)
    TextView tvRowDescr;

    @ViewById(R.id.row_tecnici_dettaglio)
    View rowTecnici;

    @ViewById(R.id.tv_row_tecnici_dettaglio)
    TextView tvRowTecnici;

    @ViewById(R.id.tv_row_date_inizio_dettaglio)
    TextView tvRowDateInizio;

    @ViewById(R.id.tv_row_time_inizio_dettaglio)
    TextView tvRowTimeInizio;

    @ViewById(R.id.tv_row_date_fine_dettaglio)
    TextView tvRowDateFine;

    @ViewById(R.id.tv_row_time_fine_dettaglio)
    TextView tvRowTimeFine;

    @ViewById(R.id.row_tot_ore_dettaglio)
    View rowTotOre;

    @ViewById(R.id.tv_row_tot_ore_dettaglio)
    TextView tvRowTotOre;

    @StringRes(R.string.ok_btn)
    static String ok_btn;

    @StringRes(R.string.btn_cancel)
    static String cancel_btn;

    @StringRes(R.string.tipo_dett_title)
    static String tipo_dett_title;

    @StringRes(R.string.choose_tecnici_title)
    static String choose_tecnici_title;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = DettaglioIntervento.class)
    RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    static RuntimeExceptionDao<Utente, Long> utenteDao;

    private DettaglioIntervento dettaglio;

    private MutableDateTime mutableDateTimeInizio, mutableDateTimeFine;

    private CalendarDatePickerDialog calendarDateInizioPickerDialog, calendarDateFinePickerDialog;
    private RadialTimePickerDialog timeInizioPickerDialog, timeFinePickerDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);

	Bundle bundle = getArguments();

	if (bundle != null) {
	    dettaglio = (DettaglioIntervento) bundle.getSerializable(Constants.DETTAGLIO_N_ESIMO);
	}

	mutableDateTimeInizio = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	mutableDateTimeFine = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
    }

    @Override
    public void onResume() {

	super.onResume();

	updateUI();
    }

    @Override
    public void onStop() {

	super.onStop();

	com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();
    }

    private void updateUI() {

	// *** nuova gestione - inizio ***\\

	Bundle bundle = getArguments();

	if (dettaglio != null) {

	    // da sistemare
	    if (bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO) != null)

		if (!InterventoController.controller.getIntervento().nuovo && !dettaglio.nuovo)
		    if (!bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO).equals(Constants.NUOVO_DETTAGLIO_INTERVENTO))
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
				getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.detail) + dettaglio.iddettagliointervento);
		    else
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
				getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.new_detail));
		else {
		    if (bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO).equals(Constants.NUOVO_DETTAGLIO_INTERVENTO))
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.new_detail));
		    else
			((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.detail) + dettaglio.iddettagliointervento);
		}

	    tvRowTipo.setText(dettaglio.tipo);

	    tvRowOggetto.setText(dettaglio.oggetto);

	    try {
		tvRowTecnici.setText("" + new JSONArray(dettaglio.tecniciintervento).length());
	    }
	    catch (JSONException e) {

		BugSenseHandler.sendException(e);
		e.printStackTrace();
	    }

	    tvRowDescr.setText(dettaglio.descrizione);

	    mutableDateTimeInizio.setDate(dettaglio.inizio);
	    tvRowDateInizio.setText(mutableDateTimeInizio.toString(Constants.DATE_PATTERN, Locale.ITALY));

	    mutableDateTimeInizio.setTime(dettaglio.inizio);
	    tvRowTimeInizio.setText(mutableDateTimeInizio.toString(Constants.TIME_PATTERN, Locale.ITALY));

	    mutableDateTimeFine.setDate(dettaglio.fine);
	    tvRowDateFine.setText(mutableDateTimeFine.toString(Constants.DATE_PATTERN, Locale.ITALY));

	    mutableDateTimeFine.setTime(dettaglio.fine);
	    tvRowTimeFine.setText(mutableDateTimeFine.toString(Constants.TIME_PATTERN, Locale.ITALY));

	    DateTime dtTotOre = new DateTime(mutableDateTimeFine.getMillis() - mutableDateTimeInizio.getMillis(), DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    tvRowTotOre.setText(dtTotOre.toString(DateTimeFormat.forPattern(Constants.TIME_PATTERN)));
	}
	else {

	    addNewDettaglio();
	}
	// *** nuova gestione - fine ***\\
    }

    private void addNewDettaglio() {

	long maxId = dettaglioDao.queryRawValue("select max(iddettagliointervento) from DettagliIntervento");

	try {
	    DettaglioIntervento foo = dettaglioDao.queryBuilder().where().eq("iddettagliointervento", maxId).queryForFirst();

	    dettaglio = new DettaglioIntervento();

	    dettaglio.iddettagliointervento = (foo.iddettagliointervento + Constants.contatoreIdNuovoDettaglio);

	    dettaglio.nuovo = (true);
	    dettaglio.modificato = (Constants.DETTAGLIO_NUOVO);
	    dettaglio.idintervento = (InterventoController.controller.getIntervento().idintervento);

	    mutableDateTimeInizio = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    tvRowDateInizio.setText(mutableDateTimeInizio.toString(Constants.DATE_PATTERN, Locale.ITALY));
	    tvRowTimeInizio.setText(mutableDateTimeInizio.toString(Constants.TIME_PATTERN, Locale.ITALY));

	    dettaglio.inizio = (mutableDateTimeInizio.getMillis());

	    mutableDateTimeFine = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    tvRowDateFine.setText(mutableDateTimeFine.toString(Constants.DATE_PATTERN, Locale.ITALY));
	    tvRowTimeFine.setText(mutableDateTimeFine.toString(Constants.TIME_PATTERN, Locale.ITALY));

	    dettaglio.fine = (mutableDateTimeFine.getMillis());

	    DateTime dtTotOre = new DateTime(mutableDateTimeFine.getMillis() - mutableDateTimeInizio.getMillis(), DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    tvRowTotOre.setText(dtTotOre.toString(DateTimeFormat.forPattern(Constants.TIME_PATTERN)));
	}
	catch (SQLException e) {

	    e.printStackTrace();
	}
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

	Bundle bundle = getArguments();

	if (bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO) != null)
	    if (bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO).equals(Constants.NUOVO_DETTAGLIO_INTERVENTO))
		inflater.inflate(R.menu.menu_new_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
	    case R.id.menu_save_detail:

		InterventoController.controller.getListaDettagli().add(dettaglio);

		Constants.contatoreIdNuovoDettaglio += 1;

		getActivity().getSupportFragmentManager().popBackStackImmediate();

		break;

	    case R.id.menu_cancel_detail:

		getActivity().getSupportFragmentManager().popBackStackImmediate();

		dettaglio = null;

		break;
	}

	return true;
    }

    @Click(R.id.tv_row_time_inizio_dettaglio)
    void showDialogTimeInizioDettaglio() {

	// aggiungere il nuovo picker per il tempo - start

	mutableDateTimeInizio =
		MutableDateTime.parse(
			!tvRowTimeInizio.getText().toString().isEmpty() ? tvRowTimeInizio.getText().toString() : new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME)).toString(),
			DateTimeFormat.forPattern(Constants.TIME_PATTERN));

	timeInizioPickerDialog =
		RadialTimePickerDialog.newInstance(DetailInterventoFragment.this, mutableDateTimeInizio.getHourOfDay(), mutableDateTimeInizio.getMinuteOfHour(),
			DateFormat.is24HourFormat(getActivity()));
	timeInizioPickerDialog.onCancel(new DialogInterface() {

	    @Override
	    public void dismiss() {

	    }

	    @Override
	    public void cancel() {

	    }
	});
	timeInizioPickerDialog.show(getActivity().getSupportFragmentManager(), Constants.TIMER_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT);
    }

    @Click(R.id.tv_row_time_fine_dettaglio)
    void showDialogTimeFineDettaglio() {

	// aggiungere il nuovo picker per il tempo - start

	mutableDateTimeFine =
		MutableDateTime.parse(
			!tvRowTimeFine.getText().toString().isEmpty() ? tvRowTimeFine.getText().toString() : new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME)).toString(),
			DateTimeFormat.forPattern(Constants.TIME_PATTERN));

	timeFinePickerDialog =
		RadialTimePickerDialog.newInstance(DetailInterventoFragment.this, mutableDateTimeFine.getHourOfDay(), mutableDateTimeFine.getMinuteOfHour(), DateFormat.is24HourFormat(getActivity()));
	timeFinePickerDialog.onCancel(new DialogInterface() {

	    @Override
	    public void dismiss() {

	    }

	    @Override
	    public void cancel() {

	    }
	});
	timeFinePickerDialog.show(getActivity().getSupportFragmentManager(), Constants.TIMER_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT);
    }

    @Click(R.id.tv_row_date_inizio_dettaglio)
    void showDialogDateInizioDettaglio() {

	// aggiungere il nuovo picker per la data - start

	mutableDateTimeInizio =
		MutableDateTime.parse(
			!tvRowDateInizio.getText().toString().isEmpty() ? tvRowDateInizio.getText().toString() : new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME)).toString(),
			DateTimeFormat.forPattern(Constants.DATE_PATTERN));

	calendarDateInizioPickerDialog =
		CalendarDatePickerDialog.newInstance(DetailInterventoFragment.this, mutableDateTimeInizio.getYear(), mutableDateTimeInizio.getMonthOfYear() - 1, mutableDateTimeInizio.getDayOfMonth());
	calendarDateInizioPickerDialog.setYearRange(1970, 3000);
	calendarDateInizioPickerDialog.onCancel(new DialogInterface() {

	    @Override
	    public void dismiss() {

	    }

	    @Override
	    public void cancel() {

	    }
	});
	calendarDateInizioPickerDialog.show(getActivity().getSupportFragmentManager(), Constants.CALENDAR_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT);

	// aggiungere il nuovo picker per la data - end

	// final Dialog dateTimeDialog = new Dialog(getActivity());
	//
	// final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_date_time, null);
	//
	// final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);
	//
	// DateTime dt = null;
	//
	// dt = DateTime.parse(tvRowDateInizio.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
	//
	// dateTimePicker.setDateTime(dt);
	//
	// dateTimePicker.setDateChangedListener(new DateWatcher() {
	//
	// @Override
	// public void onDateChanged(Calendar c) {
	//
	// }
	// });
	//
	// ((Button) dateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// dateTimePicker.clearFocus();
	//
	// DateTime dtInizio =
	// new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(), DateTimeZone
	// .forID(Constants.TIMEZONE_EUROPE_ROME));
	//
	// DateTime dtFine = null;
	//
	// DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
	// fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	//
	// dtFine = fmt.parseDateTime(tvRowDateFine.getText().toString());
	//
	// if (dtFine.toDate().getTime() < dtInizio.toDate().getTime()) {
	// InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
	// }
	// else {
	//
	// tvRowDateInizio.setText(dtInizio.toString("dd/MM/yyyy HH:mm"));
	//
	// DateTime dt_tot_ore = dtFine.minus(dtInizio.toDate().getTime());
	//
	// tvRowTotOre.setText(dt_tot_ore.toString(Constants.TIME_PATTERN));
	//
	// if (!dettaglio.nuovo) {
	// dettaglio.inizio = (dtInizio.toDate().getTime());
	// dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
	// }
	// else {
	// dettaglio.inizio = (dtInizio.toDate().getTime());
	//
	// if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
	// dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
	// }
	// }
	// }
	//
	// dateTimeDialog.dismiss();
	//
	// updateUI();
	// }
	// });
	//
	// ((Button) dateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// dateTimeDialog.cancel();
	// }
	// });
	//
	// ((Button) dateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// DateTime dt = null;
	//
	// DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
	// fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	//
	// dt = fmt.parseDateTime(tvRowDateInizio.getText().toString());
	//
	// dateTimePicker.setDateTime(dt);
	// }
	// });
	//
	// dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dateTimeDialog.setContentView(dateTimeDialogView);
	// dateTimeDialog.setCancelable(false);
	// dateTimeDialog.show();
    }

    @Click(R.id.row_fine_dettaglio)
    void showDialogDateFineDettaglio() {

	// aggiungere il nuovo picker per la data - start

	mutableDateTimeFine =
		MutableDateTime.parse(
			!tvRowDateFine.getText().toString().isEmpty() ? tvRowDateFine.getText().toString() : new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME)).toString(),
			DateTimeFormat.forPattern(Constants.DATE_PATTERN));

	calendarDateFinePickerDialog =
		CalendarDatePickerDialog.newInstance(DetailInterventoFragment.this, mutableDateTimeFine.getYear(), mutableDateTimeFine.getMonthOfYear() - 1, mutableDateTimeFine.getDayOfMonth());
	calendarDateFinePickerDialog.setYearRange(1970, 3000);
	calendarDateFinePickerDialog.onCancel(new DialogInterface() {

	    @Override
	    public void dismiss() {

	    }

	    @Override
	    public void cancel() {

	    }
	});
	calendarDateFinePickerDialog.show(getActivity().getSupportFragmentManager(), Constants.CALENDAR_PICKER_INFORMATIONS_INTERVENTO_FRAGMENT);

	// aggiungere il nuovo picker per la data - end

	// final Dialog dateTimeDialog = new Dialog(getActivity());
	//
	// final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_date_time, null);
	//
	// final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);
	//
	// DateTime dt = null;
	//
	// dt = DateTime.parse(tvRowDateFine.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));
	//
	// dateTimePicker.setDateTime(dt);
	//
	// dateTimePicker.setDateChangedListener(new DateWatcher() {
	//
	// @Override
	// public void onDateChanged(Calendar c) {
	//
	// }
	// });
	//
	// ((Button) dateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// dateTimePicker.clearFocus();
	//
	// DateTime dtFine =
	// new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(), DateTimeZone
	// .forID(Constants.TIMEZONE_EUROPE_ROME));
	//
	// DateTime dtInizio = null;
	//
	// DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
	// fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	//
	// dtInizio = fmt.parseDateTime(tvRowDateInizio.getText().toString());
	//
	// if (dtFine.toDate().getTime() < dtInizio.toDate().getTime()) {
	// InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
	// }
	// else {
	//
	// tvRowDateFine.setText(dtFine.toString("dd/MM/yyyy HH:mm"));
	//
	// DateTime dt_tot_ore = dtFine.minus(dtInizio.toDate().getTime());
	//
	// tvRowTotOre.setText(dt_tot_ore.toString(Constants.TIME_PATTERN));
	//
	// if (!dettaglio.nuovo) {
	// dettaglio.fine = (dtFine.toDate().getTime());
	// dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
	// }
	// else {
	// dettaglio.fine = (dtFine.toDate().getTime());
	//
	// if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
	// dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
	// }
	// }
	// }
	//
	// dateTimeDialog.dismiss();
	//
	// updateUI();
	// }
	// });
	//
	// ((Button) dateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// dateTimeDialog.cancel();
	// }
	// });
	//
	// ((Button) dateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// DateTime dt = null;
	//
	// DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
	// fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	//
	// dt = fmt.parseDateTime(tvRowDateFine.getText().toString());
	//
	// dateTimePicker.setDateTime(dt);
	// }
	// });
	//
	// dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dateTimeDialog.setContentView(dateTimeDialogView);
	// dateTimeDialog.setCancelable(false);
	// dateTimeDialog.show();
    }

    @Click(R.id.row_tipo_dettaglio)
    void showDialogTipo() {

	AlertDialog.Builder tipo_dett = new Builder(getActivity());

	tipo_dett.setTitle(tipo_dett_title);

	final String[] tipos = getResources().getStringArray(R.array.tipo_dettaglio_intervento);

	tipo_dett.setSingleChoiceItems(tipos, -1, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		if (!dettaglio.nuovo) {
		    dettaglio.tipo = (tipos[which]);
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.tipo = (tipos[which]);

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}
	    }
	});
	tipo_dett.setCancelable(false);
	tipo_dett.setPositiveButton(ok_btn, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();
		updateUI();
	    }
	});

	tipo_dett.create().show();
    }

    @Click(R.id.row_oggetto_dettaglio)
    void showDialogOggetto() {

	final EditText mEdit_oggetto_dett;

	AlertDialog.Builder oggetto_dett = new Builder(getActivity());

	oggetto_dett.setTitle(R.string.oggetto_dett_title);

	mEdit_oggetto_dett = new EditText(getActivity());
	mEdit_oggetto_dett.setText(tvRowOggetto.getText());

	oggetto_dett.setView(mEdit_oggetto_dett);
	oggetto_dett.setCancelable(false);
	oggetto_dett.setPositiveButton(ok_btn, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		if (!dettaglio.nuovo) {
		    dettaglio.oggetto = (mEdit_oggetto_dett.getText().toString());
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.oggetto = (mEdit_oggetto_dett.getText().toString());

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}

		updateUI();
	    }
	});

	oggetto_dett.create().show();
    }

    @Click(R.id.row_descrizione_dettaglio)
    void showDialogDescrizione() {

	final EditText mEdit_descrizione_dett;

	AlertDialog.Builder descrizione_dett = new Builder(getActivity());

	descrizione_dett.setTitle(R.string.oggetto_dett_title);

	TextView tv_descrizione_dett = (TextView) getActivity().findViewById(R.id.tv_row_descrizione_dettaglio);

	mEdit_descrizione_dett = new EditText(getActivity());
	mEdit_descrizione_dett.setText(tv_descrizione_dett.getText());

	descrizione_dett.setView(mEdit_descrizione_dett);
	descrizione_dett.setCancelable(false);
	descrizione_dett.setPositiveButton(ok_btn, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		if (!dettaglio.nuovo) {
		    dettaglio.descrizione = (mEdit_descrizione_dett.getText().toString());
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.descrizione = (mEdit_descrizione_dett.getText().toString());

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}

		updateUI();
	    }
	});

	descrizione_dett.create().show();
    }

    @Click(R.id.row_tecnici_dettaglio)
    void showDialogTecnici() {

	String[] tuttiTecnici = null;
	String[] tuttiNomiTecnici = null;

	try {
	    tuttiTecnici = getAllTecnici(getActivity().getContentResolver());

	    ManagedAsyncTask<String, Void, String[]> tuttiNomiTecniciAsyncTask = getTuttiNomiTecnici((ActionBarActivity) getActivity());

	    tuttiNomiTecnici = tuttiNomiTecniciAsyncTask.execute(tuttiTecnici).get();
	}
	catch (InterruptedException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	catch (ExecutionException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}

	final boolean[] tecniciChecked = new boolean[tuttiNomiTecnici.length];

	JSONArray tecniciOld = null;

	try {
	    tecniciOld = new JSONArray(dettaglio.tecniciintervento);

	    for (int i = 0; i < tuttiTecnici.length; i++) {

		String posTutti = tuttiTecnici[i];

		for (int j = 0; j < tecniciOld.length(); j++) {

		    String posAlcuni = tecniciOld.getString(j);

		    if (posTutti.equals(posAlcuni))
			tecniciChecked[i] = true;

		    if (j == tecniciOld.length() - 1)
			break;
		}

		if (i == tuttiTecnici.length - 1)
		    break;
	    }
	}
	catch (JSONException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}

	AlertDialog.Builder tecnici_dett = new Builder(getActivity());

	tecnici_dett.setTitle(choose_tecnici_title);
	tecnici_dett.setMultiChoiceItems(tuttiNomiTecnici, tecniciChecked, new OnMultiChoiceClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

		if (isChecked)
		    tecniciChecked[which] = isChecked;
		else
		    tecniciChecked[which] = isChecked;
	    }
	});
	try {
	    tecnici_dett.setPositiveButton(ok_btn, new DialogInterface.OnClickListener() {
		String[] tuttiTecnici = getAllTecnici(DetailInterventoFragment.this.getActivity().getContentResolver());

		@Override
		public void onClick(DialogInterface dialog, int which) {

		    dialog.dismiss();

		    JSONArray tecnici = new JSONArray();

		    for (int cont = 0; cont < tuttiTecnici.length; cont++) {

			long pos = Integer.parseInt(tuttiTecnici[cont]);

			if (tecniciChecked[cont] == true)
			    tecnici.put(pos);

			if (cont == tecniciChecked.length - 1)
			    break;
		    }

		    if (!dettaglio.nuovo) {
			dettaglio.tecniciintervento = (tecnici.toString());
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		    else {
			dettaglio.tecniciintervento = (tecnici.toString());

			if (dettaglio.modificato.length() == 0) {
			    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
			}
		    }

		    updateUI();
		}
	    });
	}
	catch (InterruptedException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	catch (ExecutionException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	tecnici_dett.setNegativeButton(cancel_btn, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();
	    }
	});

	tecnici_dett.create().show();
	;
    }

    private static String[] getAllTecnici(final ContentResolver contentResolver) throws InterruptedException, ExecutionException {

	AsyncTask<Void, Void, String[]> tuttiTecnici = new AsyncTask<Void, Void, String[]>() {

	    @Override
	    protected String[] doInBackground(Void... params) {

		ArrayList<String> arrayTecnici = new ArrayList<String>();

		QueryBuilder<Utente, Long> qb = utenteDao.queryBuilder();

		qb.selectColumns(new String[] {
		    Constants.ORMLITE_IDUTENTE
		});

		try {

		    List<Utente> listaUtenti = utenteDao.query(qb.prepare());

		    for (Utente utente : listaUtenti) {

			arrayTecnici.add("" + utente.idutente);
		    }
		}
		catch (SQLException e) {

		    e.printStackTrace();
		}

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

    private static ManagedAsyncTask<String, Void, String[]> getTuttiNomiTecnici(final ActionBarActivity activity) {

	ManagedAsyncTask<String, Void, String[]> tuttiNomiTecniciAsyncTask = new ManagedAsyncTask<String, Void, String[]>(activity) {

	    @Override
	    protected String[] doInBackground(String... params) {

		ArrayList<String> arrayNomiTecnici = new ArrayList<String>();

		for (String param : params) {

		    QueryBuilder<Utente, Long> qb = utenteDao.queryBuilder();

		    qb.selectColumns(new String[] {
			    Constants.ORMLITE_NOME, Constants.ORMLITE_COGNOME
		    });

		    try {
			qb.where().eq(Constants.ORMLITE_IDUTENTE, param);

			List<Utente> listaUtenti = utenteDao.query(qb.prepare());

			for (Utente utente : listaUtenti) {

			    arrayNomiTecnici.add(utente.nome + " " + utente.cognome);
			}
		    }
		    catch (SQLException e) {

			e.printStackTrace();
		    }
		}

		String[] tecnici = new String[arrayNomiTecnici.size()];

		return arrayNomiTecnici.toArray(tecnici);
	    }

	    @Override
	    protected void onPostExecute(String[] result) {

		super.onPostExecute(result);
	    }
	};

	return tuttiNomiTecniciAsyncTask;
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

	if (calendarDateInizioPickerDialog.isVisible()) {

	    mutableDateTimeInizio.setYear(year);
	    mutableDateTimeInizio.setMonthOfYear(monthOfYear);
	    mutableDateTimeInizio.setDayOfMonth(dayOfMonth);

	    DateTime dtFine = null;

	    DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_PATTERN);
	    fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    dtFine = fmt.parseDateTime(tvRowDateFine.getText().toString());

	    if (dtFine.getMillis() < mutableDateTimeInizio.getMillis()) {
		InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
	    }
	    else {

		tvRowDateInizio.setText(mutableDateTimeInizio.toString(Constants.DATE_PATTERN));

		DateTime dtTotOre = dtFine.minus(mutableDateTimeInizio.getMillis());

		tvRowTotOre.setText(dtTotOre.toString(Constants.TIME_PATTERN));

		if (!dettaglio.nuovo) {
		    dettaglio.inizio = (mutableDateTimeInizio.getMillis());
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.inizio = (mutableDateTimeInizio.getMillis());

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}
	    }

	    calendarDateInizioPickerDialog.dismiss();

	    updateUI();
	}

	if (calendarDateFinePickerDialog.isVisible()) {

	    mutableDateTimeFine.setYear(year);
	    mutableDateTimeFine.setMonthOfYear(monthOfYear);
	    mutableDateTimeFine.setDayOfMonth(dayOfMonth);

	    DateTime dtInizio = null;

	    DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_PATTERN);
	    fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    dtInizio = fmt.parseDateTime(tvRowDateInizio.getText().toString());

	    if (mutableDateTimeFine.getMillis() < dtInizio.getMillis()) {
		InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
	    }
	    else {

		tvRowDateFine.setText(mutableDateTimeFine.toString(Constants.DATE_PATTERN));

		mutableDateTimeFine.add(-dtInizio.getMillis());

		DateTime dt_tot_ore = mutableDateTimeFine.toDateTime();

		tvRowTotOre.setText(dt_tot_ore.toString(Constants.TIME_PATTERN));

		if (!dettaglio.nuovo) {
		    dettaglio.fine = (mutableDateTimeFine.getMillis());
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.fine = (mutableDateTimeFine.getMillis());

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}
	    }

	    calendarDateFinePickerDialog.dismiss();

	    updateUI();
	}

	return;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

	if (timeInizioPickerDialog.isVisible()) {

	    mutableDateTimeInizio.setHourOfDay(hourOfDay);
	    mutableDateTimeInizio.setMinuteOfHour(minute);

	    DateTime dtFine = null;

	    DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.TIME_PATTERN);
	    fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    dtFine = fmt.parseDateTime(tvRowTimeFine.getText().toString());

	    if (dtFine.getMillis() < mutableDateTimeInizio.getMillis()) {
		InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
	    }
	    else {

		tvRowTimeInizio.setText(mutableDateTimeInizio.toString(Constants.TIME_PATTERN));

		DateTime dtTotOre = dtFine.minus(mutableDateTimeInizio.getMillis());

		tvRowTotOre.setText(dtTotOre.toString(Constants.TIME_PATTERN));

		if (!dettaglio.nuovo) {
		    dettaglio.inizio = (mutableDateTimeInizio.getMillis());
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.inizio = (mutableDateTimeInizio.getMillis());

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}
	    }

	    calendarDateInizioPickerDialog.dismiss();

	    updateUI();
	}

	if (timeFinePickerDialog.isVisible()) {

	    mutableDateTimeFine.setHourOfDay(hourOfDay);
	    mutableDateTimeFine.setMinuteOfHour(minute);

	    DateTime dtInizio = null;

	    DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.TIME_PATTERN);
	    fmt.withZone(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    dtInizio = fmt.parseDateTime(tvRowTimeInizio.getText().toString());

	    if (mutableDateTimeFine.getMillis() < dtInizio.getMillis()) {
		InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
	    }
	    else {

		tvRowTimeFine.setText(mutableDateTimeFine.toString(Constants.TIME_PATTERN));

		mutableDateTimeFine.add(-dtInizio.getMillis());

		DateTime dt_tot_ore = mutableDateTimeFine.toDateTime();

		tvRowTotOre.setText(dt_tot_ore.toString(Constants.TIME_PATTERN));

		if (!dettaglio.nuovo) {
		    dettaglio.fine = (mutableDateTimeFine.getMillis());
		    dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		}
		else {
		    dettaglio.fine = (mutableDateTimeFine.getMillis());

		    if (dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
			dettaglio.modificato = (Constants.DETTAGLIO_MODIFICATO);
		    }
		}
	    }

	    calendarDateFinePickerDialog.dismiss();

	    updateUI();
	}

	return;
    }
}
