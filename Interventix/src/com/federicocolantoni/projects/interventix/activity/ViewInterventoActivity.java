package com.federicocolantoni.projects.interventix.activity;

import java.util.concurrent.ExecutionException;

import org.androidannotations.annotations.EActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.InterventoController;
import com.federicocolantoni.projects.interventix.controller.InterventoSingleton_;
import com.federicocolantoni.projects.interventix.task.GetClienteAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetListaDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetUtenteAsyncTack;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_view_intervento)
public class ViewInterventoActivity extends ActionBarActivity {
    
    private SharedPreferences prefs;
    
    private long idIntervento;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
	
	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    @Override
    protected void onStart() {
    
	super.onStart();
	
	Bundle extras = getIntent().getExtras();
	
	prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	String nominativo = prefs.getString(Constants.USER_NOMINATIVO, "");
	
	getSupportActionBar().setTitle(nominativo);
	
	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();
	
	if (extras != null)
	    idIntervento = extras.getLong(Constants.ID_INTERVENTO);
	
	try {
	    
	    if (idIntervento > 0L) {
		
		InterventoController.controller = InterventoSingleton_.getInstance_(this);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    InterventoController.controller.setIntervento(new GetInterventoAsyncTask(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, idIntervento).get());
		    InterventoController.controller.setCliente(new GetClienteAsyncTask(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, InterventoController.controller.getIntervento().getIdCliente()).get());
		    InterventoController.controller.setTecnico(new GetUtenteAsyncTack(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, InterventoController.controller.getIntervento().getIdTecnico()).get());
		    InterventoController.controller.setListaDettagli(new GetListaDettagliInterventoAsyncTask(this).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, InterventoController.controller.getIntervento().getIdIntervento()).get().getListDetails());
		}
		else {
		    InterventoController.controller.setIntervento(new GetInterventoAsyncTask(this).execute(idIntervento).get());
		    InterventoController.controller.setCliente(new GetClienteAsyncTask(this).execute(InterventoController.controller.getIntervento().getIdCliente()).get());
		    InterventoController.controller.setTecnico(new GetUtenteAsyncTack(this).execute(InterventoController.controller.getIntervento().getIdTecnico()).get());
		    InterventoController.controller.setListaDettagli(new GetListaDettagliInterventoAsyncTask(this).execute(InterventoController.controller.getIntervento().getIdIntervento()).get().getListDetails());
		}
	    }
	    else {
		
		InterventixToast.makeToast(this, "Nuovo intervento", Toast.LENGTH_SHORT);
		
		InterventoController.controller = InterventoSingleton_.getInstance_(this);
		InterventoController.controller.getIntervento().setIdIntervento(Constants.sIdInterventoTemp);
		InterventoController.controller.getIntervento().setNumeroIntervento(Constants.sIdInterventoTemp);
	    }
	}
	catch (InterruptedException e) {
	    
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	
	final Editor edit = prefs.edit().putInt(Constants.HASH_CODE_INTERVENTO_SINGLETON, InterventoController.controller.hashCode());
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
	    edit.apply();
	else {
	    
	    new Thread(new Runnable() {
		public void run() {
		
		    edit.commit();
		}
	    }).start();
	}
	
	com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_ overView = new com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_();
	
	transaction.add(R.id.fragments_layout, overView, Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.commit();
    }
    
    @Override
    protected void onResume() {
    
	super.onResume();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    
	    final FragmentManager manager = getSupportFragmentManager();
	    
	    if (manager.getBackStackEntryCount() == 1) {
		
		int hashCodeIntervento = InterventoController.controller.hashCode();
		
		if (hashCodeIntervento == prefs.getInt(Constants.HASH_CODE_INTERVENTO_SINGLETON, 0)) {
		    
		    finish();
		    
		    InterventoController.controller = null;
		}
		else {
		    
		    new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		}
	    }
	    else {
		
		manager.popBackStackImmediate();
	    }
	    return true;
	}
	
	return super.onKeyDown(keyCode, event);
    }
    
    @Override
    public void onBackPressed() {
    
	final FragmentManager manager = getSupportFragmentManager();
	
	if (manager.getBackStackEntryCount() == 1) {
	    
	    int hashCodeIntervento = InterventoController.controller.hashCode();
	    
	    if (hashCodeIntervento == prefs.getInt(Constants.HASH_CODE_INTERVENTO_SINGLETON, 0)) {
		
		finish();
		
		InterventoController.controller = null;
	    }
	    else {
		
		new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
	    }
	}
	else {
	    manager.popBackStackImmediate();
	}
    }
    
    public static class ExitIntervento extends DialogFragment implements OnClickListener {
	
	public ExitIntervento() {
	
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	
	    AlertDialog.Builder exit_dialog = new Builder(getActivity());
	    
	    exit_dialog.setTitle(R.string.interv_mod_title);
	    exit_dialog.setMessage(R.string.interv_mod_text);
	    
	    exit_dialog.setPositiveButton(getString(R.string.yes_btn), this);
	    exit_dialog.setNegativeButton(getString(R.string.no_btn), this);
	    
	    return exit_dialog.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	
	    switch (which) {
	    
		case DialogInterface.BUTTON_POSITIVE:
		    
		    dialog.dismiss();
		    
		    saveInterventoOnDB();
		    
		    InterventixToast.makeToast(getActivity(), "Intervento modificato\n con successo!", Toast.LENGTH_SHORT);
		    
		    getActivity().finish();
		    
		    break;
		
		case DialogInterface.BUTTON_NEGATIVE:
		    
		    dialog.dismiss();
		    
		    InterventoController.controller = null;
		    
		    getActivity().finish();
		    
		    break;
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    
	super.onCreateOptionsMenu(menu);
	
	return true;
    }
    
    private static void saveInterventoOnDB() {
    
	InterventoController.saveOnDB();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		int hashCodeIntervento = InterventoController.controller.hashCode();
		
		if (hashCodeIntervento == prefs.getInt(Constants.HASH_CODE_INTERVENTO_SINGLETON, 0)) {
		    
		    finish();
		}
		else {
		    
		    new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
		}
		
		break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
    
	super.onDestroy();
    }
}
