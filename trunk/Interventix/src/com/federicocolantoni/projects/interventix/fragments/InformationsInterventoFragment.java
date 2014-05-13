package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringArrayRes;
import org.androidannotations.annotations.res.StringRes;
import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.models.InterventoController;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_informations)
public class InformationsInterventoFragment extends Fragment /* implements OnDateSetListener, OnTimeSetListener */{

    @ViewById(R.id.tv_row_tipology)
    TextView tvTipology;

    @ViewById(R.id.tv_row_mode)
    TextView tvMode;

    @ViewById(R.id.tv_row_product)
    TextView tvProduct;

    @ViewById(R.id.tv_row_motivation)
    TextView tvMotivation;

    @ViewById(R.id.tv_row_name)
    TextView tvNominativo;

    @ViewById(R.id.tv_row_date)
    TextView tvDateInterv;

    @ViewById(R.id.tv_row_time)
    TextView tvTimeInterv;

    @ViewById(R.id.row_references)
    LinearLayout rowReferences;

    @ViewById(R.id.row_notes)
    LinearLayout rowNotes;

    @StringRes(R.string.numero_intervento)
    String numeroIntervento;

    @StringRes(R.string.row_informations)
    String rowInformations;

    @StringRes(R.string.new_interv)
    String nuovoIntervento;

    @DrawableRes(R.drawable.ic_launcher)
    Drawable icLaucher;

    @StringRes(R.string.tipologia_title)
    String tipologiaTitle;

    @StringArrayRes(R.array.tipologia_choose)
    String[] tipologiaChoose;

    @StringRes(R.string.ok_btn)
    String btnOk;

    @StringRes(R.string.modalita_title)
    String modalitaTitle;

    @StringArrayRes(R.array.modalita_choose)
    String[] modalitaChoose;

    @StringRes(R.string.prodotto_title)
    String prodottoTitle;

    @StringRes(R.string.nominativo_title)
    String nominativoTitle;

    @StringRes(R.string.motivation_title)
    String motivoTitle;

    private FragmentManager manager;

    private MutableDateTime mutableDateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);

	mutableDateTime = new MutableDateTime(DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));
    }

    @Override
    public void onStart() {

	super.onStart();

	manager = getActivity().getSupportFragmentManager();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(numeroIntervento + InterventoController.controller.getIntervento().numero + " - " + rowInformations);
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(nuovoIntervento + " - " + rowInformations);
    }

    @Override
    public void onResume() {

	super.onResume();

	updateUI();
    }

    private void updateUI() {

	if (InterventoController.controller.getIntervento().tipologia.equals(Constants.TYPE_INTERVENTO.REQUEST.getName()))
	    tvTipology.setText(Constants.RICHIESTA);
	else
	    tvTipology.setText(Constants.PROGRAMMATA);

	tvProduct.setText(InterventoController.controller.getIntervento().prodotto);

	if (InterventoController.controller.getIntervento().modalita.equals(Constants.MODE_INTERVENTO.CONTRACT.getName()))
	    tvMode.setText(Constants.CONTRATTUALIZZATO);
	else if (InterventoController.controller.getIntervento().modalita.equals(Constants.MODE_INTERVENTO.PAYMENT.getName()))
	    tvMode.setText(Constants.PAGAMENTO_DIRETTO);
	else if (InterventoController.controller.getIntervento().modalita.equals(Constants.MODE_INTERVENTO.POSTSALE.getName()))
	    tvMode.setText(Constants.POST_VENDITA);
	else
	    tvMode.setText(Constants.COMODATO_USO);

	tvMotivation.setText(InterventoController.controller.getIntervento().motivo);

	tvNominativo.setText(InterventoController.controller.getIntervento().nominativo);

	mutableDateTime =
		new MutableDateTime(InterventoController.controller.getIntervento().dataora != 0 ? InterventoController.controller.getIntervento().dataora : new MutableDateTime(),
			DateTimeZone.forID(Constants.TIMEZONE_EUROPE_ROME));

	tvDateInterv.setText(mutableDateTime.toString(Constants.DATE_PATTERN));

	tvTimeInterv.setText(mutableDateTime.toString(Constants.TIME_PATTERN));
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

		break;

	    case R.id.send_mail:

		break;

	    case R.id.close:

		break;
	}

	return true;
    }

    @Click(R.id.row_tipology)
    void showDialogTipologia() {

	AlertDialog.Builder tipologia = new Builder(getActivity());

	tipologia.setIcon(icLaucher);
	tipologia.setTitle(tipologiaTitle);
	tipologia.setCancelable(true);
	tipologia.setSingleChoiceItems(tipologiaChoose, -1, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		if (tipologiaChoose[which].equals(Constants.RICHIESTA))
		    InterventoController.controller.getIntervento().tipologia = Constants.TYPE_INTERVENTO.REQUEST.getName();
		else
		    InterventoController.controller.getIntervento().tipologia = Constants.TYPE_INTERVENTO.PROGRAMMED.getName();

		dialog.dismiss();

		updateUI();
	    }
	});

	tipologia.create().show();
    }

    @Click(R.id.row_mode)
    void showDialogModalita() {

	AlertDialog.Builder modalita = new Builder(getActivity());

	modalita.setIcon(icLaucher);
	modalita.setTitle(modalitaTitle);
	modalita.setCancelable(true);
	modalita.setSingleChoiceItems(modalitaChoose, -1, new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface dialog, int which) {

		if (modalitaChoose[which].equals(Constants.CONTRATTUALIZZATO))
		    InterventoController.controller.getIntervento().modalita = Constants.MODE_INTERVENTO.CONTRACT.getName();
		else if (modalitaChoose[which].equals(Constants.PAGAMENTO_DIRETTO))
		    InterventoController.controller.getIntervento().modalita = Constants.MODE_INTERVENTO.PAYMENT.getName();
		else if (modalitaChoose[which].equals(Constants.POST_VENDITA))
		    InterventoController.controller.getIntervento().modalita = Constants.MODE_INTERVENTO.POSTSALE.getName();
		else
		    InterventoController.controller.getIntervento().modalita = Constants.MODE_INTERVENTO.RENTUSE.getName();

		dialog.dismiss();

		updateUI();
	    }
	});

	modalita.create().show();
    }

    @Click(R.id.row_product)
    void showDialogProdotto() {

	final EditText mEdit_prodotto;

	AlertDialog.Builder prodotto = new Builder(getActivity());

	prodotto.setIcon(icLaucher);
	prodotto.setTitle(prodottoTitle);
	prodotto.setCancelable(true);

	mEdit_prodotto = new EditText(getActivity());
	mEdit_prodotto.setText(tvProduct.getText());

	prodotto.setView(mEdit_prodotto);
	prodotto.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {

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

	AlertDialog.Builder nominativo = new Builder(getActivity());

	nominativo.setIcon(icLaucher);
	nominativo.setTitle(nominativoTitle);
	nominativo.setCancelable(true);

	mEdit_nominativo = new EditText(getActivity());
	mEdit_nominativo.setText(tvNominativo.getText());

	nominativo.setView(mEdit_nominativo);

	nominativo.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {

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

	AlertDialog.Builder motivo = new Builder(getActivity());

	motivo.setIcon(icLaucher);
	motivo.setTitle(motivoTitle);
	motivo.setCancelable(true);

	TextView tv_motivo = (TextView) getActivity().findViewById(R.id.tv_row_motivation);

	mEdit_motivo = new EditText(getActivity());
	mEdit_motivo.setText(tv_motivo.getText());

	motivo.setView(mEdit_motivo);

	motivo.setPositiveButton(btnOk, new DialogInterface.OnClickListener() {

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
