package com.federicocolantoni.projects.interventix.ui.fragments;

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
@EFragment(R.layout.fragment_costs)
public class CostsInterventoFragment extends Fragment {
    
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
	
	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_costs));
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_costs));
    }
    
    @Override
    public void onResume() {
    
	super.onResume();
	
	updateUI();
    }
    
    private void updateUI() {
    
	DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	
	if (InterventoController.controller.getIntervento().costomanodopera != null)
	    tv_row_manodopera.setText(formatter.format(InterventoController.controller.getIntervento().costomanodopera.doubleValue()) + " €");
	else
	    tv_row_manodopera.setText(formatter.format(0.0) + " €");
	
	if (InterventoController.controller.getIntervento().costocomponenti != null)
	    tv_row_componenti.setText(formatter.format(InterventoController.controller.getIntervento().costocomponenti.doubleValue()) + " €");
	else
	    tv_row_componenti.setText(formatter.format(0.0) + " €");
	
	if (InterventoController.controller.getIntervento().costoaccessori != null)
	    tv_row_accessori.setText(formatter.format(InterventoController.controller.getIntervento().costoaccessori.doubleValue()) + " €");
	else
	    tv_row_accessori.setText(formatter.format(0.0) + " €");
	
	if (InterventoController.controller.getIntervento().importo != null)
	    tv_row_importo.setText(formatter.format(InterventoController.controller.getIntervento().importo.doubleValue()) + " €");
	else
	    tv_row_importo.setText(formatter.format(0.0) + " €");
	
	if (InterventoController.controller.getIntervento().iva != null)
	    tv_row_iva.setText(formatter.format(InterventoController.controller.getIntervento().iva.doubleValue()) + " €");
	else
	    tv_row_iva.setText(formatter.format(0.0) + " €");
	
	if (InterventoController.controller.getIntervento().totale != null)
	    tv_row_totale.setText(formatter.format(InterventoController.controller.getIntervento().totale.doubleValue()) + " €");
	else
	    tv_row_totale.setText(formatter.format(0.0) + " €");
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    
	super.onCreateOptionsMenu(menu, inflater);
	
	inflater.inflate(R.menu.menu_view_intervento, menu);
	
	MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
	itemAddDetail.setVisible(true);
	itemAddDetail.setIcon(R.drawable.ic_action_add_disabled);
	itemAddDetail.setEnabled(false);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
	switch (item.getItemId()) {
	    case R.id.pay:
		
		InterventixToast.makeToast("Saldare l'intervento?", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.send_mail:
		
		InterventixToast.makeToast("Inviare email?", Toast.LENGTH_SHORT);
		
		break;
	    
	    case R.id.close:
		
		InterventixToast.makeToast("Chiudere l'intervento?", Toast.LENGTH_SHORT);
		
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
	mEditManodopera.setText("" + (InterventoController.controller.getIntervento().costomanodopera != null ? InterventoController.controller.getIntervento().costomanodopera : 0.0));
	mEditManodopera.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	
	costoAccessoriDialog.setView(mEditManodopera);
	costoAccessoriDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		InterventoController.controller.getIntervento().costomanodopera = (new BigDecimal(mEditManodopera.getText().toString()));
		InterventoController.controller.getIntervento().importo = (new BigDecimal(InterventoController.controller.getIntervento().costomanodopera.doubleValue() + (InterventoController.controller.getIntervento().costoaccessori != null ? InterventoController.controller.getIntervento().costoaccessori.doubleValue() : 0.0) + (InterventoController.controller.getIntervento().costocomponenti != null ? InterventoController.controller.getIntervento().costocomponenti.doubleValue() : 0.0)));
		InterventoController.controller.getIntervento().iva = (new BigDecimal(InterventoController.controller.getIntervento().importo.doubleValue() * Constants.IVA));
		InterventoController.controller.getIntervento().totale = (new BigDecimal(InterventoController.controller.getIntervento().importo.doubleValue() + InterventoController.controller.getIntervento().iva.doubleValue()));
		
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
	mEditComponenti.setText("" + (InterventoController.controller.getIntervento().costocomponenti != null ? InterventoController.controller.getIntervento().costocomponenti : 0.0));
	mEditComponenti.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	
	costoAccessoriDialog.setView(mEditComponenti);
	costoAccessoriDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		InterventoController.controller.getIntervento().costocomponenti = (new BigDecimal(mEditComponenti.getText().toString()));
		InterventoController.controller.getIntervento().importo = (new BigDecimal(InterventoController.controller.getIntervento().costocomponenti.doubleValue() + (InterventoController.controller.getIntervento().costoaccessori != null ? InterventoController.controller.getIntervento().costoaccessori.doubleValue() : 0.0) + (InterventoController.controller.getIntervento().costomanodopera != null ? InterventoController.controller.getIntervento().costomanodopera.doubleValue() : 0.0)));
		InterventoController.controller.getIntervento().iva = (new BigDecimal(InterventoController.controller.getIntervento().importo.doubleValue() * Constants.IVA));
		InterventoController.controller.getIntervento().totale = (new BigDecimal(InterventoController.controller.getIntervento().importo.doubleValue() + InterventoController.controller.getIntervento().iva.doubleValue()));
		
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
	mEditAccessori.setText("" + (InterventoController.controller.getIntervento().costoaccessori != null ? InterventoController.controller.getIntervento().costoaccessori : 0.0));
	mEditAccessori.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	
	costoAccessoriDialog.setView(mEditAccessori);
	costoAccessoriDialog.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		InterventoController.controller.getIntervento().costoaccessori = (new BigDecimal(mEditAccessori.getText().toString()));
		InterventoController.controller.getIntervento().importo = (new BigDecimal(InterventoController.controller.getIntervento().costoaccessori.doubleValue() + (InterventoController.controller.getIntervento().costomanodopera != null ? InterventoController.controller.getIntervento().costomanodopera.doubleValue() : 0.0) + (InterventoController.controller.getIntervento().costocomponenti != null ? InterventoController.controller.getIntervento().costocomponenti.doubleValue() : 0.0)));
		InterventoController.controller.getIntervento().iva = (new BigDecimal(InterventoController.controller.getIntervento().importo.doubleValue() * Constants.IVA));
		InterventoController.controller.getIntervento().totale = (new BigDecimal(InterventoController.controller.getIntervento().importo.doubleValue() + InterventoController.controller.getIntervento().iva.doubleValue()));
		
		dialog.dismiss();
		
		updateUI();
	    }
	});
	costoAccessoriDialog.setIcon(getActivity().getResources().getDrawable(R.drawable.ic_launcher));
	
	costoAccessoriDialog.create().show();
    }
}
