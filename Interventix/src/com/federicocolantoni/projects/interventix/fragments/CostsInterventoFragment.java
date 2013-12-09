package com.federicocolantoni.projects.interventix.fragments;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetCostsInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.SaveChangesInterventoAsyncQueryHandler;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
public class CostsInterventoFragment extends Fragment {
    
    public static long sId_intervento;
    
    private static Double sCosto_componenti;
    private static Double sCosto_accessori;
    private static Double sCosto_manodopera;
    private static Double sImporto;
    private static Double sIva;
    private static Double sTotale;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	BugSenseHandler.initAndStartSession(getActivity(), Constants.API_KEY);
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
	
	final View view = inflater.inflate(R.layout.costs_intervento_fragment, container, false);
	
	Bundle bundle = getArguments();
	
	sId_intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
	TextView tv_costs_intervento = (TextView) view.findViewById(R.id.tv_costs_intervention);
	tv_costs_intervento.setText("Costi");
	
	Intervento interv = null;
	
	try {
	    interv = new GetCostsInterventoAsyncTask(getActivity()).execute(sId_intervento).get();
	}
	catch (InterruptedException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	
	View rowManodopera = view.findViewById(R.id.row_manodopera);
	rowManodopera.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		new SetCostoManodopera().show(getFragmentManager(), Constants.MANODOPERA_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_manodopera = (TextView) rowManodopera.findViewById(R.id.tv_row_manodopera);
	sCosto_manodopera = interv.getmCostoManodopera().doubleValue();
	tv_row_manodopera.setText(formatter.format(sCosto_manodopera) + " €");
	
	View rowComponenti = view.findViewById(R.id.row_componenti);
	rowComponenti.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		new SetCostoComponenti().show(getFragmentManager(), Constants.COMPONENTI_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_componenti = (TextView) rowComponenti.findViewById(R.id.tv_row_componenti);
	sCosto_componenti = interv.getmCostoComponenti().doubleValue();
	tv_row_componenti.setText(formatter.format(sCosto_componenti) + " €");
	
	View rowAccessori = view.findViewById(R.id.row_accessori);
	rowAccessori.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		new SetCostoAccessori().show(getFragmentManager(), Constants.ACCESSORI_DIALOG_FRAGMENT);
	    }
	});
	
	TextView tv_row_accessori = (TextView) rowAccessori.findViewById(R.id.tv_row_accessori);
	sCosto_accessori = interv.getmCostoAccessori().doubleValue();
	tv_row_accessori.setText(formatter.format(sCosto_accessori) + " €");
	
	View rowImporto = view.findViewById(R.id.row_importo);
	
	TextView tv_row_importo = (TextView) rowImporto.findViewById(R.id.tv_row_importo);
	sImporto = interv.getmImporto().doubleValue();
	tv_row_importo.setText(formatter.format(sImporto) + " €");
	
	View rowIVA = view.findViewById(R.id.row_iva);
	
	TextView tv_row_iva = (TextView) rowIVA.findViewById(R.id.tv_row_iva);
	sIva = interv.getmIva().doubleValue();
	tv_row_iva.setText(formatter.format(sIva) + " €");
	
	View rowTotale = view.findViewById(R.id.row_totale);
	
	TextView tv_row_totale = (TextView) rowTotale.findViewById(R.id.tv_row_totale);
	sTotale = interv.getmTotale().doubleValue();
	tv_row_totale.setText(formatter.format(sTotale) + " €");
	
	return view;
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
    
    public static class SetCostoManodopera extends DialogFragment implements android.content.DialogInterface.OnClickListener {
	
	private EditText mEdit_manodopera;
	
	public SetCostoManodopera() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder manodopera = new AlertDialog.Builder(getActivity());
	    
	    manodopera.setTitle(R.string.manodopera_title);
	    
	    mEdit_manodopera = new EditText(getActivity());
	    mEdit_manodopera.setText("" + sCosto_manodopera);
	    mEdit_manodopera.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	    
	    manodopera.setView(mEdit_manodopera);
	    
	    manodopera.setPositiveButton(R.string.ok_btn, this);
	    
	    return manodopera.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	    
	    TextView tv_manodopera = (TextView) getActivity().findViewById(R.id.tv_row_manodopera);
	    
	    TextView tv_row_importo = (TextView) getActivity().findViewById(R.id.tv_row_importo);
	    TextView tv_row_iva = (TextView) getActivity().findViewById(R.id.tv_row_iva);
	    TextView tv_row_totale = (TextView) getActivity().findViewById(R.id.tv_row_totale);
	    
	    sCosto_manodopera = new BigDecimal(mEdit_manodopera.getText().toString()).doubleValue();
	    
	    tv_manodopera.setText(formatter.format(sCosto_manodopera) + " €");
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getActivity());
	    
	    sImporto = sCosto_accessori + sCosto_componenti + sCosto_manodopera;
	    
	    tv_row_importo.setText(formatter.format(sImporto) + " €");
	    
	    sIva = sImporto * Constants.IVA;
	    
	    tv_row_iva.setText(formatter.format(sIva) + " €");
	    
	    sTotale = sImporto + sIva;
	    
	    tv_row_totale.setText(formatter.format(sTotale) + " €");
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.COSTO_MANODOPERA, sCosto_manodopera);
	    values.put(InterventoDB.Fields.IMPORTO, sImporto);
	    values.put(InterventoDB.Fields.IVA, sIva);
	    values.put(InterventoDB.Fields.TOTALE, sTotale);
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		    InterventoDB.INTERVENTO_ITEM_TYPE, "" + sId_intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_COSTO_MANODOPERA, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
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
    
    public static class SetCostoComponenti extends DialogFragment implements android.content.DialogInterface.OnClickListener {
	
	private EditText mEdit_Componenti;
	
	public SetCostoComponenti() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder componenti = new Builder(getActivity());
	    
	    componenti.setTitle(R.string.componenti_title);
	    
	    mEdit_Componenti = new EditText(getActivity());
	    mEdit_Componenti.setText("" + sCosto_componenti);
	    mEdit_Componenti.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	    
	    componenti.setView(mEdit_Componenti);
	    
	    componenti.setPositiveButton(R.string.ok_btn, this);
	    
	    return componenti.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	    
	    TextView tv_row_componenti = (TextView) getActivity().findViewById(R.id.tv_row_componenti);
	    
	    TextView tv_row_importo = (TextView) getActivity().findViewById(R.id.tv_row_importo);
	    TextView tv_row_iva = (TextView) getActivity().findViewById(R.id.tv_row_iva);
	    TextView tv_row_totale = (TextView) getActivity().findViewById(R.id.tv_row_totale);
	    
	    sCosto_componenti = new BigDecimal(mEdit_Componenti.getText().toString()).doubleValue();
	    
	    tv_row_componenti.setText(formatter.format(sCosto_componenti) + " €");
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getActivity());
	    
	    sImporto = sCosto_accessori + sCosto_componenti + sCosto_manodopera;
	    
	    tv_row_importo.setText(formatter.format(sImporto) + " €");
	    
	    sIva = sImporto * Constants.IVA;
	    
	    tv_row_iva.setText(formatter.format(sIva) + " €");
	    
	    sTotale = sImporto + sIva;
	    
	    tv_row_totale.setText(formatter.format(sTotale) + " €");
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.COSTO_COMPONENTI, sCosto_componenti);
	    values.put(InterventoDB.Fields.IMPORTO, sImporto);
	    values.put(InterventoDB.Fields.IVA, sIva);
	    values.put(InterventoDB.Fields.TOTALE, sTotale);
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		    InterventoDB.INTERVENTO_ITEM_TYPE, "" + sId_intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_COSTO_COMPONENTI, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
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
    
    public static class SetCostoAccessori extends DialogFragment implements android.content.DialogInterface.OnClickListener {
	
	private EditText mEdit_Accessori;
	
	public SetCostoAccessori() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder accessori = new Builder(getActivity());
	    
	    accessori.setTitle(R.string.accessori_title);
	    
	    mEdit_Accessori = new EditText(getActivity());
	    mEdit_Accessori.setText(sCosto_accessori + "");
	    mEdit_Accessori.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
	    
	    accessori.setView(mEdit_Accessori);
	    
	    accessori.setPositiveButton(R.string.ok_btn, this);
	    
	    return accessori.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	    
	    TextView tv_row_accessori = (TextView) getActivity().findViewById(R.id.tv_row_accessori);
	    
	    TextView tv_row_importo = (TextView) getActivity().findViewById(R.id.tv_row_importo);
	    TextView tv_row_iva = (TextView) getActivity().findViewById(R.id.tv_row_iva);
	    TextView tv_row_totale = (TextView) getActivity().findViewById(R.id.tv_row_totale);
	    
	    sCosto_accessori = new BigDecimal(mEdit_Accessori.getText().toString()).doubleValue();
	    
	    tv_row_accessori.setText(formatter.format(sCosto_accessori) + " €");
	    
	    SaveChangesInterventoAsyncQueryHandler saveChange = new SaveChangesInterventoAsyncQueryHandler(getActivity());
	    
	    sImporto = sCosto_accessori + sCosto_componenti + sCosto_manodopera;
	    
	    tv_row_importo.setText(formatter.format(sImporto) + " €");
	    
	    sIva = sImporto * Constants.IVA;
	    
	    tv_row_iva.setText(formatter.format(sIva) + " €");
	    
	    sTotale = sImporto + sIva;
	    
	    tv_row_totale.setText(formatter.format(sTotale) + " €");
	    
	    ContentValues values = new ContentValues();
	    values.put(InterventoDB.Fields.COSTO_ACCESSORI, sCosto_accessori);
	    values.put(InterventoDB.Fields.IMPORTO, sImporto);
	    values.put(InterventoDB.Fields.IVA, sIva);
	    values.put(InterventoDB.Fields.TOTALE, sTotale);
	    values.put(InterventoDB.Fields.MODIFICATO, "M");
	    
	    String selection = InterventoDB.Fields.TYPE + " = ? AND " + InterventoDB.Fields.ID_INTERVENTO + " = ?";
	    
	    String[] selectionArgs = new String[] {
		    InterventoDB.INTERVENTO_ITEM_TYPE, "" + sId_intervento
	    };
	    
	    saveChange.startUpdate(Constants.TOKEN_COSTO_ACCESSORI, null, InterventoDB.CONTENT_URI, values, selection, selectionArgs);
	    
	    SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
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
