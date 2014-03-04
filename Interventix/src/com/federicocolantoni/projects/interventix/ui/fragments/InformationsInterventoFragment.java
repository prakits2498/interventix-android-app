package com.federicocolantoni.projects.interventix.ui.fragments;

import java.util.Calendar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;
import com.qustom.dialog.QustomDialogBuilder;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_informations)
public class InformationsInterventoFragment extends Fragment {

    @ViewById(R.id.tv_row_tipology)
    TextView tv_tipology;

    @ViewById(R.id.tv_row_mode)
    TextView tv_mode;

    @ViewById(R.id.tv_row_product)
    TextView tv_product;

    @ViewById(R.id.tv_row_motivation)
    TextView tv_motivation;

    @ViewById(R.id.tv_row_name)
    TextView tv_nominativo;

    @ViewById(R.id.row_date)
    View date_interv;

    @ViewById(R.id.tv_row_date)
    TextView tv_date_interv;

    @ViewById(R.id.row_references)
    LinearLayout row_references;

    @ViewById(R.id.row_notes)
    LinearLayout row_notes;

    private FragmentManager manager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {

	super.onStart();

	manager = getActivity().getSupportFragmentManager();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
		    getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_informations));
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_informations));

	date_interv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {

		final Dialog dateTimeDialog = new Dialog(getActivity());

		final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_date_time, null);

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

			DateTime dt =
				new DateTime(dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay(), dateTimePicker.getHour(), dateTimePicker.getMinute(), DateTimeZone
					.forID("Europe/Rome"));

			tv_date_interv.setText(dt.toString("dd/MM/yyyy HH:mm"));

			InterventoController.controller.getIntervento().dataora = (dt.toDate().getTime());

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
    }

    @Override
    public void onResume() {

	super.onResume();

	updateUI();
    }

    private void updateUI() {

	tv_tipology.setText(InterventoController.controller.getIntervento().tipologia);

	tv_product.setText(InterventoController.controller.getIntervento().prodotto);

	tv_mode.setText(InterventoController.controller.getIntervento().modalita);

	tv_motivation.setText(InterventoController.controller.getIntervento().motivo);

	tv_nominativo.setText(InterventoController.controller.getIntervento().nominativo);

	DateTime dt = new DateTime(InterventoController.controller.getIntervento().dataora, DateTimeZone.forID("Europe/Rome"));

	tv_date_interv.setText(dt.toString("dd/MM/yyyy HH:mm"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	super.onCreateOptionsMenu(menu, inflater);

	inflater.inflate(R.menu.menu_view_intervento, menu);

	MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
	itemAddDetail.setVisible(true);
	itemAddDetail.setEnabled(false);
	itemAddDetail.setIcon(R.drawable.ic_action_add_disabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {
	    case R.id.pay:

		// InterventixToast.makeToast("Saldare l'intervento?",
		// Toast.LENGTH_SHORT);

		break;

	    case R.id.send_mail:

		// InterventixToast.makeToast("Inviare email?",
		// Toast.LENGTH_SHORT);

		break;

	    case R.id.close:

		// InterventixToast.makeToast("Chiudere l'intervento?",
		// Toast.LENGTH_SHORT);

		break;
	}

	return true;
    }

    @Click(R.id.row_tipology)
    void showDialogTipologia() {

	QustomDialogBuilder tipologia = new QustomDialogBuilder(getActivity());

	tipologia.setIcon(R.drawable.ic_launcher);
	tipologia.setTitleColor(getActivity().getResources().getColor(R.color.interventix_color));
	tipologia.setDividerColor(getActivity().getResources().getColor(R.color.interventix_color));

	tipologia.setTitle(getString(R.string.tipologia_title));
	final String[] choices = getResources().getStringArray(R.array.tipologia_choose);
	tipologia.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		InterventoController.controller.getIntervento().tipologia = (choices[which]);
	    }
	});

	tipologia.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		updateUI();
	    }
	});

	tipologia.create().show();
    }

    @Click(R.id.row_mode)
    void showDialogModalita() {

	QustomDialogBuilder modalita = new QustomDialogBuilder(getActivity());

	modalita.setIcon(R.drawable.ic_launcher);
	modalita.setTitleColor(getActivity().getResources().getColor(R.color.interventix_color));
	modalita.setDividerColor(getActivity().getResources().getColor(R.color.interventix_color));
	modalita.setTitle(getResources().getString(R.string.modalita_title));
	final String[] choices = getResources().getStringArray(R.array.modalita_choose);
	modalita.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		InterventoController.controller.getIntervento().modalita = (choices[which]);
	    }
	});

	modalita.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		updateUI();
	    }
	});

	modalita.create().show();
    }

    @Click(R.id.row_product)
    void showDialogProdotto() {

	final EditText mEdit_prodotto;

	QustomDialogBuilder prodotto = new QustomDialogBuilder(getActivity());

	prodotto.setIcon(R.drawable.ic_launcher);
	prodotto.setTitleColor(getActivity().getResources().getColor(R.color.interventix_color));
	prodotto.setDividerColor(getActivity().getResources().getColor(R.color.interventix_color));

	prodotto.setTitle(getString(R.string.prodotto_title));

	mEdit_prodotto = new EditText(getActivity());
	mEdit_prodotto.setText(tv_product.getText());

	prodotto.setCustomView(mEdit_prodotto, getActivity());
	prodotto.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		InterventoController.controller.getIntervento().prodotto = (mEdit_prodotto.getText().toString());

		updateUI();
	    }
	});

	prodotto.create().show();
    }

    @Click(R.id.row_name)
    void showDialogNominativo() {

	final EditText mEdit_nominativo;

	QustomDialogBuilder nominativo = new QustomDialogBuilder(getActivity());

	nominativo.setIcon(R.drawable.ic_launcher);
	nominativo.setTitleColor(getActivity().getResources().getColor(R.color.interventix_color));
	nominativo.setDividerColor(getActivity().getResources().getColor(R.color.interventix_color));

	nominativo.setTitle(R.string.nominativo_title);

	mEdit_nominativo = new EditText(getActivity());
	mEdit_nominativo.setText(tv_nominativo.getText());

	nominativo.setCustomView(mEdit_nominativo, getActivity());

	nominativo.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		InterventoController.controller.getIntervento().nominativo = (mEdit_nominativo.getText().toString());

		updateUI();
	    }
	});

	nominativo.create().show();
    }

    @Click(R.id.row_motivation)
    void showDialogMotivazione() {

	final EditText mEdit_motivo;

	QustomDialogBuilder motivo = new QustomDialogBuilder(getActivity());

	motivo.setIcon(R.drawable.ic_launcher);
	motivo.setTitleColor(getActivity().getResources().getColor(R.color.interventix_color));
	motivo.setDividerColor(getActivity().getResources().getColor(R.color.interventix_color));

	motivo.setTitle(R.string.motivation_title);

	TextView tv_motivo = (TextView) getActivity().findViewById(R.id.tv_row_motivation);

	mEdit_motivo = new EditText(getActivity());
	mEdit_motivo.setText(tv_motivo.getText());

	motivo.setCustomView(mEdit_motivo, getActivity());

	motivo.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();

		InterventoController.controller.getIntervento().motivo = (mEdit_motivo.getText().toString());

		updateUI();
	    }
	});

	motivo.create().show();
    }

    @Click(R.id.row_references)
    void showRiferimenti() {

	FragmentTransaction transaction = manager.beginTransaction();

	ReferencesInterventoFragment_ referencesInterv = new ReferencesInterventoFragment_();

	transaction.replace(R.id.fragments_layout, referencesInterv, Constants.REFERENCES_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.REFERENCES_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }

    @Click(R.id.row_notes)
    void showAnnotazioni() {

	FragmentTransaction transaction = manager.beginTransaction();

	AnnotationsInterventoFragment_ annotationsInterv = new AnnotationsInterventoFragment_();

	transaction.replace(R.id.fragments_layout, annotationsInterv, Constants.ANNOTATIONS_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.ANNOTATIONS_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

	transaction.commit();
    }
}