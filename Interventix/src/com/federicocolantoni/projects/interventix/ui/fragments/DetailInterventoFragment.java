package com.federicocolantoni.projects.interventix.ui.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.ManagedAsyncTask;

@SuppressLint("NewApi")
@EFragment(R.layout.detail_fragment)
public class DetailInterventoFragment extends Fragment {

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

	@ViewById(R.id.row_inizio_dettaglio)
	View rowInizio;

	@ViewById(R.id.tv_row_inizio_dettaglio)
	TextView tvRowInizio;

	@ViewById(R.id.row_fine_dettaglio)
	View rowFine;

	@ViewById(R.id.tv_row_fine_dettaglio)
	TextView tvRowFine;

	@ViewById(R.id.row_tot_ore_dettaglio)
	View rowTotOre;

	@ViewById(R.id.tv_row_tot_ore_dettaglio)
	TextView tvRowTotOre;

	@StringRes(R.string.ok_btn)
	static String ok_btn;

	@StringRes(R.string.cancel_btn)
	static String cancel_btn;

	@StringRes(R.string.tipo_dett_title)
	static String tipo_dett_title;

	@StringRes(R.string.choose_tecnici_title)
	static String choose_tecnici_title;

	private ActionBar actionbar;

	private DettaglioIntervento dettaglio;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		actionbar = ((ActionBarActivity) getActivity()).getSupportActionBar();

		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);

		setHasOptionsMenu(true);

		Bundle bundle = getArguments();

		if (bundle != null) {
			dettaglio = (DettaglioIntervento) bundle.getSerializable(Constants.DETTAGLIO_N_ESIMO);

			// if (dettaglio != null)
			// System.out.println(this.getClass().getSimpleName() + "\n" +
			// dettaglio.toString());
		}
	}

	@Override
	public void onResume() {

		super.onResume();

		updateUI();
	}

	private void updateUI() {

		// *** nuova gestione - inizio ***\\

		Bundle bundle = getArguments();

		if (dettaglio != null) {

			System.out.println(this.getClass().getSimpleName() + "\n" + dettaglio.toString());

			// da sistemare
			if (bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO) != null)

				if (!InterventoController.controller.getIntervento().isNuovo() && !dettaglio.isNuovo())
					if (!bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO).equals(Constants.NUOVO_DETTAGLIO_INTERVENTO))
						((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
								getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().getNumeroIntervento() + " - " + getString(R.string.detail)
										+ dettaglio.getIdDettaglioIntervento());
					else
						((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
								getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().getNumeroIntervento() + " - "
										+ getString(R.string.new_detail));
				else {
					if (bundle.getString(Constants.NUOVO_DETTAGLIO_INTERVENTO).equals(Constants.NUOVO_DETTAGLIO_INTERVENTO))
						((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.new_detail));
					else
						((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
								getString(R.string.new_interv) + " - " + getString(R.string.detail) + dettaglio.getIdDettaglioIntervento());
				}

			tvRowTipo.setText(dettaglio.getTipo());

			tvRowOggetto.setText(dettaglio.getOggetto());

			try {
				tvRowTecnici.setText("" + new JSONArray(dettaglio.getTecnici()).length());
			} catch (JSONException e) {

				BugSenseHandler.sendException(e);
				e.printStackTrace();
			}

			tvRowDescr.setText(dettaglio.getDescrizione());

			DateTime dtInizio = new DateTime(dettaglio.getInizio(), DateTimeZone.forID("Europe/Rome"));

			tvRowInizio.setText(dtInizio.toString("dd/MM/yyyy HH:mm", Locale.ITALY));

			DateTime dtFine = new DateTime(dettaglio.getFine(), DateTimeZone.forID("Europe/Rome"));

			tvRowFine.setText(dtFine.toString("dd/MM/yyyy HH:mm", Locale.ITALY));

			DateTime dtTotOre = new DateTime(dtFine.toDate().getTime() - dtInizio.toDate().getTime(), DateTimeZone.forID("Europe/Rome"));

			tvRowTotOre.setText(dtTotOre.toString(DateTimeFormat.forPattern("HH:mm")));
		} else {

			addNewDettaglio();
		}
		// *** nuova gestione - fine ***\\
	}

	private void addNewDettaglio() {

		getMaxIdDettaglioIntervento();
	}

	private void getMaxIdDettaglioIntervento() {

		new AsyncTask<Void, Void, Long>() {

			@Override
			protected Long doInBackground(Void... params) {

				ContentResolver cr = getActivity().getContentResolver();

				String[] projection = new String[] { DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO };

				String selection = Fields.TYPE + "=?";

				String[] selectionArgs = new String[] { DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE };

				long max = 0;

				Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);

				while (cursor.moveToNext()) {

					long temp = cursor.getLong(cursor.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO));

					if (temp > max)
						max = temp;
				}

				if (!cursor.isClosed())
					cursor.close();

				return max;
			}

			@Override
			protected void onPostExecute(Long result) {

				if (InterventoController.controller.getIntervento().isNuovo())
					((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("Nuovo intervento" + " - Nuovo dettaglio");

				dettaglio = new DettaglioIntervento();

				dettaglio.setIdDettaglioIntervento(result + 1);
				dettaglio.setNuovo(true);
				dettaglio.setModificato(Constants.DETTAGLIO_NUOVO);
				dettaglio.setIntervento(InterventoController.controller.getIntervento().getIdIntervento());

				DateTime dt_inizio = new DateTime(new Date().getTime(), DateTimeZone.forID("Europe/Rome"));

				tvRowInizio.setText(dt_inizio.toString("dd/MM/yyyy HH:mm", Locale.ITALY));

				dettaglio.setInizio(dt_inizio.getMillis());

				DateTime dt_fine = new DateTime(new Date().getTime(), DateTimeZone.forID("Europe/Rome"));

				tvRowFine.setText(dt_fine.toString("dd/MM/yyyy HH:mm", Locale.ITALY));

				dettaglio.setFine(dt_fine.getMillis());

				DateTime dt_tot_ore = new DateTime(dt_fine.toDate().getTime() - dt_inizio.toDate().getTime(), DateTimeZone.forID("Europe/Rome"));

				tvRowTotOre.setText(dt_tot_ore.toString(DateTimeFormat.forPattern("HH:mm")));
			}
		}.execute();
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

	@Click(R.id.row_inizio_dettaglio)
	void showDialogInizioDettaglio() {

		final Dialog dateTimeDialog = new Dialog(getActivity());

		final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);

		final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);

		DateTime dt = null;

		dt = DateTime.parse(tvRowInizio.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));

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

				DateTime dtInizio = new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(),
						dateTimePicker.getMinute(), DateTimeZone.forID("Europe/Rome"));

				DateTime dtFine = null;

				DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
				fmt.withZone(DateTimeZone.forID("Europe/Rome"));

				dtFine = fmt.parseDateTime(tvRowFine.getText().toString());

				if (dtFine.toDate().getTime() < dtInizio.toDate().getTime()) {
					InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
				} else {

					tvRowInizio.setText(dtInizio.toString("dd/MM/yyyy HH:mm"));

					DateTime dt_tot_ore = dtFine.minus(dtInizio.toDate().getTime());

					tvRowTotOre.setText(dt_tot_ore.toString("HH:mm"));

					if (!dettaglio.isNuovo()) {
						dettaglio.setInizio(dtInizio.toDate().getTime());
						dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
					} else {
						dettaglio.setInizio(dtInizio.toDate().getTime());

						if (dettaglio.getModificato().length() == 0) {
							dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
						}
					}
				}

				dateTimeDialog.dismiss();

				updateUI();
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

				dt = fmt.parseDateTime(tvRowInizio.getText().toString());

				dateTimePicker.setDateTime(dt);
			}
		});

		dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialog.setContentView(dateTimeDialogView);
		dateTimeDialog.setCancelable(false);
		dateTimeDialog.show();
	}

	@Click(R.id.row_fine_dettaglio)
	void showDialogFineDettaglio() {

		final Dialog dateTimeDialog = new Dialog(getActivity());

		final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);

		final DateTimePicker dateTimePicker = (DateTimePicker) dateTimeDialogView.findViewById(R.id.DateTimePicker);

		DateTime dt = null;

		dt = DateTime.parse(tvRowFine.getText().toString(), DateTimeFormat.forPattern("dd/MM/yyyy HH:mm"));

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

				DateTime dtFine = new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(),
						DateTimeZone.forID("Europe/Rome"));

				DateTime dtInizio = null;

				DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
				fmt.withZone(DateTimeZone.forID("Europe/Rome"));

				dtInizio = fmt.parseDateTime(tvRowInizio.getText().toString());

				if (dtFine.toDate().getTime() < dtInizio.toDate().getTime()) {
					InterventixToast.makeToast("Errore! Inizio intervento maggiore di fine intervento", Toast.LENGTH_LONG);
				} else {

					tvRowFine.setText(dtFine.toString("dd/MM/yyyy HH:mm"));

					DateTime dt_tot_ore = dtFine.minus(dtInizio.toDate().getTime());

					tvRowTotOre.setText(dt_tot_ore.toString("HH:mm"));

					if (!dettaglio.isNuovo()) {
						dettaglio.setFine(dtFine.toDate().getTime());
						dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
					} else {
						dettaglio.setFine(dtFine.toDate().getTime());

						if (dettaglio.getModificato().length() == 0) {
							dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
						}
					}
				}

				dateTimeDialog.dismiss();

				updateUI();
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

				dt = fmt.parseDateTime(tvRowFine.getText().toString());

				dateTimePicker.setDateTime(dt);
			}
		});

		dateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dateTimeDialog.setContentView(dateTimeDialogView);
		dateTimeDialog.setCancelable(false);
		dateTimeDialog.show();
	}

	@Click(R.id.row_tipo_dettaglio)
	void showDialogTipo() {

		AlertDialog.Builder tipo_dett = new Builder(getActivity());

		tipo_dett.setTitle(tipo_dett_title);

		final String[] tipos = getResources().getStringArray(R.array.tipo_dettaglio_intervento);

		tipo_dett.setSingleChoiceItems(tipos, -1, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (!dettaglio.isNuovo()) {
					dettaglio.setTipo(tipos[which]);
					dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
				} else {
					dettaglio.setTipo(tipos[which]);

					if (dettaglio.getModificato().length() == 0) {
						dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
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

				if (!dettaglio.isNuovo()) {
					dettaglio.setOggetto(mEdit_oggetto_dett.getText().toString());
					dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
				} else {
					dettaglio.setOggetto(mEdit_oggetto_dett.getText().toString());

					if (dettaglio.getModificato().length() == 0) {
						dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
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

				if (!dettaglio.isNuovo()) {
					dettaglio.setDescrizione(mEdit_descrizione_dett.getText().toString());
					dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
				} else {
					dettaglio.setDescrizione(mEdit_descrizione_dett.getText().toString());

					if (dettaglio.getModificato().length() == 0) {
						dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
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
		} catch (InterruptedException e) {

			BugSenseHandler.sendException(e);
			e.printStackTrace();
		} catch (ExecutionException e) {

			BugSenseHandler.sendException(e);
			e.printStackTrace();
		}

		final boolean[] tecniciChecked = new boolean[tuttiNomiTecnici.length];

		JSONArray tecniciOld = null;

		try {
			tecniciOld = new JSONArray(dettaglio.getTecnici());

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
		} catch (JSONException e) {

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

					if (!dettaglio.isNuovo()) {
						dettaglio.setTecnici(tecnici.toString());
						dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
					} else {
						dettaglio.setTecnici(tecnici.toString());

						if (dettaglio.getModificato().length() == 0) {
							dettaglio.setModificato(Constants.DETTAGLIO_MODIFICATO);
						}
					}

					updateUI();
				}
			});
		} catch (InterruptedException e) {

			BugSenseHandler.sendException(e);
			e.printStackTrace();
		} catch (ExecutionException e) {

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

				ContentResolver cr = contentResolver;

				String[] projection = new String[] { Fields._ID, UtenteDB.Fields.ID_UTENTE };

				String selection = Fields.TYPE + "=?";

				String[] selectionArgs = new String[] { UtenteDB.UTENTE_ITEM_TYPE };

				String sortOrder = UtenteDB.Fields.COGNOME + " asc";

				Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, sortOrder);

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

	private static ManagedAsyncTask<String, Void, String[]> getTuttiNomiTecnici(final ActionBarActivity activity) {

		ManagedAsyncTask<String, Void, String[]> tuttiNomiTecniciAsyncTask = new ManagedAsyncTask<String, Void, String[]>(activity) {

			@Override
			protected String[] doInBackground(String... params) {

				ContentResolver cr = activity.getContentResolver();

				ArrayList<String> arrayNomiTecnici = new ArrayList<String>();

				for (String param : params) {

					String[] projection = new String[] { Fields._ID, UtenteDB.Fields.NOME, UtenteDB.Fields.COGNOME };

					String selection = Fields.TYPE + "=? AND " + UtenteDB.Fields.ID_UTENTE + "=?";

					String[] selectionArgs = new String[] { UtenteDB.UTENTE_ITEM_TYPE, param };

					String sortOrder = UtenteDB.Fields.COGNOME + " asc";

					Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, sortOrder);

					while (cursor.moveToNext()) {

						arrayNomiTecnici
								.add(cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.NOME)) + " " + cursor.getString(cursor.getColumnIndex(UtenteDB.Fields.COGNOME)));
					}

					if (!cursor.isClosed())
						cursor.close();
				}

				System.out.println("Tutti i nomi dei tecnici: " + arrayNomiTecnici.toString());

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
}
