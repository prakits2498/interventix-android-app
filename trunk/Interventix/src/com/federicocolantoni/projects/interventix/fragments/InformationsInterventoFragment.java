package com.federicocolantoni.projects.interventix.fragments;

import java.util.Calendar;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker;
import com.federicocolantoni.projects.interventix.utils.DateTimePicker.DateWatcher;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
@EFragment(R.layout.information_intervento_fragment)
public class InformationsInterventoFragment extends Fragment {
    
    @ViewById(R.id.tv_info_intervention)
    TextView info_interv;
    
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
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
    }
    
    @Override
    public void onStart() {
    
	super.onStart();
	
	info_interv.setText(R.string.row_informations);
	
	date_interv.setOnClickListener(new View.OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
	    
		final Dialog dateTimeDialog = new Dialog(getActivity());
		
		final RelativeLayout dateTimeDialogView = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.date_time_dialog, null);
		
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
			
			InterventoController.controller.getIntervento().setDataOra(dt.toDate().getTime());
			
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
    
	tv_tipology.setText(InterventoController.controller.getIntervento().getTipologia());
	
	tv_product.setText(InterventoController.controller.getIntervento().getProdotto());
	
	tv_mode.setText(InterventoController.controller.getIntervento().getModalita());
	
	tv_motivation.setText(InterventoController.controller.getIntervento().getMotivo());
	
	tv_nominativo.setText(InterventoController.controller.getIntervento().getNominativo());
	
	DateTime dt = new DateTime(InterventoController.controller.getIntervento().getDataOra(), DateTimeZone.forID("Europe/Rome"));
	
	tv_date_interv.setText(dt.toString("dd/MM/yyyy HH:mm"));
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
    
    @Click(R.id.row_tipology)
    void showDialogTipologia() {
    
	AlertDialog.Builder tipologia = new Builder(getActivity());
	
	tipologia.setTitle(getResources().getString(R.string.tipologia_title));
	final String[] choices = getResources().getStringArray(R.array.tipologia_choose);
	tipologia.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		InterventoController.controller.getIntervento().setTipologia(choices[which]);
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
    
	AlertDialog.Builder modalita = new Builder(getActivity());
	
	modalita.setTitle(getResources().getString(R.string.modalita_title));
	final String[] choices = getResources().getStringArray(R.array.modalita_choose);
	modalita.setSingleChoiceItems(choices, -1, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		InterventoController.controller.getIntervento().setModalita(choices[which]);
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
	
	AlertDialog.Builder prodotto = new Builder(getActivity());
	
	prodotto.setTitle(R.string.prodotto_title);
	
	mEdit_prodotto = new EditText(getActivity());
	mEdit_prodotto.setText(tv_product.getText());
	
	prodotto.setView(mEdit_prodotto);
	prodotto.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		dialog.dismiss();
		
		InterventoController.controller.getIntervento().setProdotto(mEdit_prodotto.getText().toString());
		
		updateUI();
	    }
	});
	
	prodotto.create().show();
    }
    
    @Click(R.id.row_name)
    void showDialogNominativo() {
    
	final EditText mEdit_nominativo;
	
	AlertDialog.Builder nominativo = new Builder(getActivity());
	
	nominativo.setTitle(R.string.nominativo_title);
	
	mEdit_nominativo = new EditText(getActivity());
	mEdit_nominativo.setText(tv_nominativo.getText());
	
	nominativo.setView(mEdit_nominativo);
	
	nominativo.setPositiveButton(getResources().getString(R.string.ok_btn), new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		dialog.dismiss();
		
		InterventoController.controller.getIntervento().setNominativo(mEdit_nominativo.getText().toString());
		
		updateUI();
	    }
	});
	
	nominativo.create().show();
    }
    
    @Click(R.id.row_motivation)
    void showDialogMotivazione() {
    
	final EditText mEdit_motivo;
	
	AlertDialog.Builder motivo = new Builder(getActivity());
	
	motivo.setTitle(R.string.motivation_title);
	
	TextView tv_motivo = (TextView) getActivity().findViewById(R.id.tv_row_motivation);
	
	mEdit_motivo = new EditText(getActivity());
	mEdit_motivo.setText(tv_motivo.getText());
	
	motivo.setView(mEdit_motivo);
	
	motivo.setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener() {
	    
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    
		dialog.dismiss();
		
		InterventoController.controller.getIntervento().setMotivo(mEdit_motivo.getText().toString());
		
		updateUI();
	    }
	});
	
	motivo.create().show();
    }
}
