package com.federicocolantoni.projects.interventix.fragments;

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

import com.bugsense.trace.BugSenseHandler;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog.OnDateSetListener;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog.OnTimeSetListener;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.ManagedAsyncTask;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.federicocolantoni.projects.interventix.models.Utente;
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
    }

    @Override
    public void onStart() {

	super.onStart();

	if (dettaglio != null) {

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

	    if (mutableDateTimeInizio == null) {
		mutableDateTimeInizio = new MutableDateTime(dettaglio.inizio, DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	    }

	    if (mutableDateTimeFine == null) {
		mutableDateTimeFine = new MutableDateTime(dettaglio.fine, DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	    }

	    tvRowDateInizio.setText(mutableDateTimeInizio.toString(Constants.DATE_PATTERN, Locale.ITALY));
	    tvRowTimeInizio.setText(mutableDateTimeInizio.toString(Constants.TIME_PATTERN, Locale.ITALY));

	    tvRowDateFine.setText(mutableDateTimeFine.toString(Constants.DATE_PATTERN, Locale.ITALY));
	    tvRowTimeFine.setText(mutableDateTimeFine.toString(Constants.TIME_PATTERN, Locale.ITALY));

	    DateTime dtTotOre = new DateTime(mutableDateTimeFine.getMillis() - mutableDateTimeFine.getMillis(), DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	    tvRowTotOre.setText(dtTotOre.toString(DateTimeFormat.forPattern(Constants.TIME_PATTERN)));
	}
	else {

	    addNewDettaglio();
	}
    }

    @Override
    public void onStop() {

	super.onStop();

	// Interventix_.releaseDbHelper();
    }

    private void updateUI() {

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

	tvRowDateInizio.setText(mutableDateTimeInizio.toString(Constants.DATE_PATTERN, Locale.ITALY));
	tvRowTimeInizio.setText(mutableDateTimeInizio.toString(Constants.TIME_PATTERN, Locale.ITALY));

	tvRowDateFine.setText(mutableDateTimeFine.toString(Constants.DATE_PATTERN, Locale.ITALY));
	tvRowTimeFine.setText(mutableDateTimeFine.toString(Constants.TIME_PATTERN, Locale.ITALY));

	DateTime dtTotOre = new DateTime(mutableDateTimeFine.getMillis() - mutableDateTimeInizio.getMillis(), DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	tvRowTotOre.setText(dtTotOre.toString(DateTimeFormat.forPattern(Constants.TIME_PATTERN)));
    }

    private void addNewDettaglio() {

	long maxId = dettaglioDao.queryRawValue(String.format("select max(%s) from DettagliIntervento", Constants.ORMLITE_IDDETTAGLIOINTERVENTO));

	dettaglio = new DettaglioIntervento();

	dettaglio.iddettagliointervento = (maxId + Constants.contatoreNuovoId);

	dettaglio.nuovo = (true);
	dettaglio.modificato = (Constants.DETTAGLIO_NUOVO);
	dettaglio.idintervento = (InterventoController.controller.getIntervento().idintervento);

	try {
	    tvRowTecnici.setText("" + new JSONArray(dettaglio.tecniciintervento).length());
	}
	catch (JSONException e) {

	    e.printStackTrace();
	}

	mutableDateTimeInizio = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	dettaglio.inizio = mutableDateTimeInizio.getMillis();

	tvRowDateInizio.setText(mutableDateTimeInizio.toString(Constants.DATE_PATTERN, Locale.ITALY));
	tvRowTimeInizio.setText(mutableDateTimeInizio.toString(Constants.TIME_PATTERN, Locale.ITALY));

	mutableDateTimeFine = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
	mutableDateTimeFine.addHours(1);

	dettaglio.fine = mutableDateTimeFine.getMillis();

	tvRowDateFine.setText(mutableDateTimeFine.toString(Constants.DATE_PATTERN, Locale.ITALY));
	tvRowTimeFine.setText(mutableDateTimeFine.toString(Constants.TIME_PATTERN, Locale.ITALY));

	DateTime dtTotOre = new DateTime(mutableDateTimeFine.getMillis() - mutableDateTimeInizio.getMillis(), DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	tvRowTotOre.setText(dtTotOre.toString(DateTimeFormat.forPattern(Constants.TIME_PATTERN)));
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
    }

    @Click(R.id.tv_row_date_fine_dettaglio)
    void showDialogDateFineDettaglio() {

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

		updateUI();

		dialog.dismiss();
	    }
	});
	tipo_dett.setCancelable(false);

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

	mEdit_descrizione_dett = new EditText(getActivity());
	mEdit_descrizione_dett.setText(tvRowDescr.getText());

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

	if (calendarDateInizioPickerDialog != null && calendarDateInizioPickerDialog.isVisible()) {

	    mutableDateTimeInizio.setDate(year, monthOfYear + 1, dayOfMonth);

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

	    calendarDateInizioPickerDialog.dismiss();
	}

	if (calendarDateFinePickerDialog != null && calendarDateFinePickerDialog.isVisible()) {

	    mutableDateTimeFine.setDate(year, monthOfYear + 1, dayOfMonth);

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

	    calendarDateFinePickerDialog.dismiss();
	}

	updateUI();

	return;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog dialog, int hourOfDay, int minute) {

	if (timeInizioPickerDialog != null && timeInizioPickerDialog.isVisible()) {

	    mutableDateTimeInizio.setTime(hourOfDay, minute, 0, 0);

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

	    timeInizioPickerDialog.dismiss();
	}

	if (timeFinePickerDialog != null && timeFinePickerDialog.isVisible()) {

	    mutableDateTimeFine.setTime(hourOfDay, minute, 0, 0);

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

	    timeFinePickerDialog.dismiss();
	}

	updateUI();

	return;
    }
}
