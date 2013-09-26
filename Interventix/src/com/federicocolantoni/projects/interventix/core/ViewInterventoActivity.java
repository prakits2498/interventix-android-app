package com.federicocolantoni.projects.interventix.core;

import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.task.GetDettagliInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.task.GetInterventoAsyncTask;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.federicocolantoni.projects.interventix.utils.ListDetailsIntervento;

@SuppressLint("NewApi")
public class ViewInterventoActivity extends BaseActivity {
    
    private static long id_intervento;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setContentView(R.layout.view_intervento);
	
	Bundle extras = getIntent().getExtras();
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	id_intervento = extras.getLong(Constants.ID_INTERVENTO);
	
	Intervento interv_old = null;
	ListDetailsIntervento listaDettagliIntervento_old = null;
	
	try {
	    interv_old = new GetInterventoAsyncTask(this).execute(id_intervento).get();
	    
	    listaDettagliIntervento_old = new GetDettagliInterventoAsyncTask(this).execute(id_intervento).get();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
	catch (ExecutionException e) {
	    e.printStackTrace();
	}
	
	String nominativo = prefs.getString(Constants.USER_NOMINATIVO, null);
	
	getSupportActionBar().setTitle(nominativo);
	
	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();
	
	OverViewInterventoFragment overView = new OverViewInterventoFragment();
	
	Bundle bundle = new Bundle();
	bundle.putString(Constants.USER_NOMINATIVO, nominativo);
	bundle.putAll(extras);
	
	overView.setArguments(bundle);
	
	transaction.add(R.id.fragments_layout, overView, Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
	transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	transaction.commit();
    }
    
    @Override
    protected void onResume() {
	
	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	final Editor edit = prefs.edit();
	
	edit.putBoolean(Constants.INTERV_MODIFIED, false);
	edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
	
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
	
	super.onResume();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	
	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    
	    FragmentManager manager = getSupportFragmentManager();
	    
	    if (manager.getBackStackEntryCount() == 1) {
		
		SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
		
		boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
		boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
		
		if (!interv_modified && !dett_interv_modified) {
		    
		    finish();
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
	
	FragmentManager manager = getSupportFragmentManager();
	
	if (manager.getBackStackEntryCount() == 1) {
	    
	    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    boolean interv_modified = prefs.getBoolean(Constants.INTERV_MODIFIED, false);
	    boolean dett_interv_modified = prefs.getBoolean(Constants.DETT_INTERV_MODIFIED, false);
	    
	    if (!interv_modified && !dett_interv_modified) {
		
		finish();
	    }
	    else {
		
		new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
	    }
	}
	else {
	    manager.popBackStackImmediate();
	}
    }
    
    public static class ExitIntervento extends SherlockDialogFragment implements
								     OnClickListener {
	
	public ExitIntervento() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder exit_dialog = new Builder(getSherlockActivity());
	    
	    exit_dialog.setTitle(R.string.interv_mod_title);
	    exit_dialog.setMessage(R.string.interv_mod_text);
	    
	    exit_dialog.setPositiveButton("SI", this);
	    exit_dialog.setNegativeButton("NO", this);
	    
	    return exit_dialog.create();
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
	    
	    SharedPreferences prefs = getSherlockActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	    
	    final Editor edit = prefs.edit();
	    
	    switch (which) {
	    
		case DialogInterface.BUTTON_POSITIVE:
		    
		    dialog.dismiss();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
		    edit.putBoolean(Constants.INTERV_MODIFIED, false);
		    
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
		    getSherlockActivity().finish();
		    
		    break;
		
		case DialogInterface.BUTTON_NEGATIVE:
		    
		    dialog.dismiss();
		    
		    edit.putBoolean(Constants.DETT_INTERV_MODIFIED, false);
		    edit.putBoolean(Constants.INTERV_MODIFIED, false);
		    
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
		    
		    getSherlockActivity().finish();
		    
		    break;
	    }
	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	final MenuInflater inflater = getSupportMenuInflater();
	
	inflater.inflate(R.menu.view_intervento, menu);
	
	FragmentManager manager = getSupportFragmentManager();
	
	int cont = manager.getBackStackEntryCount();
	
	for (int i = 0; i < cont; i++) {
	    BackStackEntry entry = manager.getBackStackEntryAt(i);
	    
	    if (entry.getName().equals(Constants.DETAILS_INTERVENTO_FRAGMENT)) {
		
		MenuItem item = menu.findItem(R.id.add_detail_interv);
		
		item.setVisible(true);
	    }
	}
	
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	
	    case android.R.id.home:
		
		finish();
		
		break;
	    
	    case R.id.add_detail_interv:
		
		InterventixToast.makeToast(this, "Aggiungi dettaglio intervento", Toast.LENGTH_SHORT);
		break;
	}
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
	
	super.onDestroy();
    }
}
