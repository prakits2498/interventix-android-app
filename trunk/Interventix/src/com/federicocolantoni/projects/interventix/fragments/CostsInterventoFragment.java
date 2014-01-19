package com.federicocolantoni.projects.interventix.fragments;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
@EFragment(R.layout.costs_intervento_fragment)
public class CostsInterventoFragment extends Fragment {

	// retrieve views using annotations
	@ViewById(R.id.tv_costs_intervention)
	TextView tv_costs_intervento;

	@ViewById(R.id.tv_row_manodopera)
	TextView tv_row_manodopera;

	@ViewById(R.id.tv_row_componenti)
	TextView tv_row_componenti;

	@ViewById(R.id.tv_row_accessori)
	TextView tv_row_accessori;

	@ViewById(R.id.tv_row_importo)
	TextView tv_row_importo;

	@ViewById(R.id.tv_row_iva)
	TextView tv_row_iva;

	@ViewById(R.id.tv_row_totale)
	TextView tv_row_totale;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(getActivity(), Constants.API_KEY);

		((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
		((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setHasOptionsMenu(true);
	}

	@Override
	public void onStart() {

		super.onStart();

		tv_costs_intervento.setText("Costi");
	}

	@Override
	public void onResume() {

		super.onResume();

		updateUI();
	}

	private void updateUI() {

		DecimalFormat formatter = new DecimalFormat("###,###,###.##");

		tv_row_manodopera.setText(formatter.format(InterventoController.controller.getIntervento().getCostoManodopera().doubleValue()) + " €");

		tv_row_componenti.setText(formatter.format(InterventoController.controller.getIntervento().getCostoComponenti().doubleValue()) + " €");

		tv_row_accessori.setText(formatter.format(InterventoController.controller.getIntervento().getCostoAccessori().doubleValue()) + " €");

		tv_row_importo.setText(formatter.format(InterventoController.controller.getIntervento().getImporto().doubleValue()) + " €");

		tv_row_iva.setText(formatter.format(InterventoController.controller.getIntervento().getIva().doubleValue()) + " €");

		tv_row_totale.setText(formatter.format(InterventoController.controller.getIntervento().getTotale().doubleValue()) + " €");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.menu_view_intervento, menu);

		MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
		itemAddDetail.setVisible(true);
		itemAddDetail.setEnabled(false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.pay:

				InterventixToast.makeToast(getActivity(), "Saldare l'intervento?", Toast.LENGTH_SHORT);

				break;

			case R.id.send_mail:

				InterventixToast.makeToast(getActivity(), "Inviare email?", Toast.LENGTH_SHORT);

				break;

			case R.id.close:

				InterventixToast.makeToast(getActivity(), "Chiudere l'intervento?", Toast.LENGTH_SHORT);

				break;
		}

		return true;
	}

	@Click(R.id.row_manodopera)
	void showDialogManodopera() {

		final EditText mEditManodopera;

		AlertDialog.Builder costoAccessoriDialog = new Builder(getActivity());

		costoAccessoriDialog.setTitle(R.string.componenti_title);

		mEditManodopera = new EditText(getActivity());
		mEditManodopera.setText("" + InterventoController.controller.getIntervento().getCostoManodopera());
		mEditManodopera.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		costoAccessoriDialog.setView(mEditManodopera);
		costoAccessoriDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				InterventoController.controller.getIntervento().setCostoManodopera(new BigDecimal(mEditManodopera.getText().toString()));

				InterventoController.controller.getIntervento().setImporto(
						new BigDecimal(InterventoController.controller.getIntervento().getCostoManodopera().doubleValue()
								+ InterventoController.controller.getIntervento().getCostoAccessori().doubleValue()
								+ InterventoController.controller.getIntervento().getCostoComponenti().doubleValue()));

				InterventoController.controller.getIntervento().setIva(new BigDecimal(InterventoController.controller.getIntervento().getImporto().doubleValue() * Constants.IVA));

				InterventoController.controller.getIntervento().setTotale(
						new BigDecimal(InterventoController.controller.getIntervento().getImporto().doubleValue()
								+ InterventoController.controller.getIntervento().getIva().doubleValue()));

				dialog.dismiss();

				updateUI();
			}
		});
		costoAccessoriDialog.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_launcher));

		costoAccessoriDialog.create().show();
	}

	@Click(R.id.row_componenti)
	void showDialogComponenti() {

		final EditText mEditComponenti;

		AlertDialog.Builder costoAccessoriDialog = new Builder(getActivity());

		costoAccessoriDialog.setTitle(R.string.componenti_title);

		mEditComponenti = new EditText(getActivity());
		mEditComponenti.setText("" + InterventoController.controller.getIntervento().getCostoComponenti());
		mEditComponenti.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		costoAccessoriDialog.setView(mEditComponenti);
		costoAccessoriDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				InterventoController.controller.getIntervento().setCostoComponenti(new BigDecimal(mEditComponenti.getText().toString()));

				InterventoController.controller.getIntervento().setImporto(
						new BigDecimal(InterventoController.controller.getIntervento().getCostoManodopera().doubleValue()
								+ InterventoController.controller.getIntervento().getCostoAccessori().doubleValue()
								+ InterventoController.controller.getIntervento().getCostoComponenti().doubleValue()));

				InterventoController.controller.getIntervento().setIva(new BigDecimal(InterventoController.controller.getIntervento().getImporto().doubleValue() * Constants.IVA));

				InterventoController.controller.getIntervento().setTotale(
						new BigDecimal(InterventoController.controller.getIntervento().getImporto().doubleValue()
								+ InterventoController.controller.getIntervento().getIva().doubleValue()));

				dialog.dismiss();

				updateUI();
			}
		});
		costoAccessoriDialog.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_launcher));

		costoAccessoriDialog.create().show();
	}

	@Click(R.id.row_accessori)
	void showDialogAccessori() {

		final EditText mEditAccessori;

		AlertDialog.Builder costoAccessoriDialog = new Builder(getActivity());

		costoAccessoriDialog.setTitle(R.string.accessori_title);

		mEditAccessori = new EditText(getActivity());
		mEditAccessori.setText("" + InterventoController.controller.getIntervento().getCostoAccessori());
		mEditAccessori.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		costoAccessoriDialog.setView(mEditAccessori);
		costoAccessoriDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				InterventoController.controller.getIntervento().setCostoAccessori(new BigDecimal(mEditAccessori.getText().toString()));

				InterventoController.controller.getIntervento().setImporto(
						new BigDecimal(InterventoController.controller.getIntervento().getCostoManodopera().doubleValue()
								+ InterventoController.controller.getIntervento().getCostoAccessori().doubleValue()
								+ InterventoController.controller.getIntervento().getCostoComponenti().doubleValue()));

				InterventoController.controller.getIntervento().setIva(new BigDecimal(InterventoController.controller.getIntervento().getImporto().doubleValue() * Constants.IVA));

				InterventoController.controller.getIntervento().setTotale(
						new BigDecimal(InterventoController.controller.getIntervento().getImporto().doubleValue()
								+ InterventoController.controller.getIntervento().getIva().doubleValue()));

				dialog.dismiss();

				updateUI();
			}
		});
		costoAccessoriDialog.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_launcher));

		costoAccessoriDialog.create().show();
	}
}
