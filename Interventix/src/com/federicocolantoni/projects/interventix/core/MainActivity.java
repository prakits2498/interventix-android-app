package com.federicocolantoni.projects.interventix.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.id;
import com.federicocolantoni.projects.interventix.modules.login.Login;
import com.federicocolantoni.projects.interventix.settings.SettingActivity;
import com.federicocolantoni.projects.interventix.settings.SettingSupportActivity;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	BugSenseHandler.initAndStartSession(MainActivity.this, Constants.API_KEY);
	
	setContentView(R.layout.activity_main);
	
	FragmentManager manager = getSupportFragmentManager();
	
	final Login fragLogin = new Login();
	
	final FragmentTransaction transaction = manager.beginTransaction();
	
	Timer timer = new Timer();
	
	timer.schedule(new TimerTask() {
	    
	    @Override
	    public void run() {
		transaction.replace(R.id.frag_login, fragLogin);
		transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
		transaction.commit();
	    }
	}, 2500);
	
	SharedPreferences prefs = null;
	
	ReadDefaultPreferences readPref = new ReadDefaultPreferences(MainActivity.this);
	readPref.execute();
	
	try {
	    
	    prefs = readPref.get();
	}
	catch (InterruptedException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
	    if (prefs.getString(getResources().getString(R.string.prefs_key_url), "").isEmpty()) {
		
		FirstRunDialog dialog = new FirstRunDialog();
		dialog.show(getSupportFragmentManager(), getString(R.string.first_run));
	    }
	    else
	    if (prefs.getString(getResources().getString(R.string.prefs_key_url), "").length() == 0) {
		
		FirstRunDialog dialog = new FirstRunDialog();
		dialog.show(getSupportFragmentManager(), Constants.FIRST_RUN_DIALOG_FRAGMENT);
	    }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
	super.onCreateOptionsMenu(menu);
	
	final MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.menu_main, menu);
	
	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	super.onOptionsItemSelected(item);
	
	switch (item.getItemId()) {
	    case id.menu_options:
		
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1)
		    startActivity(new Intent(this, SettingActivity.class));
		else
		    startActivity(new Intent(this, SettingSupportActivity.class));
		return true;
	    default:
		if (super.onOptionsItemSelected(item))
		    return true;
		else
		    return false;
	}
    }
    
    public static class FirstRunDialog extends DialogFragment implements OnClickListener {
	
	private EditText input_url;
	private Button save_url;
	
	public FirstRunDialog() {
	    
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder first_run_dialog = new Builder(getActivity());
	    
	    first_run_dialog.setTitle(R.string.welcome_title);
	    first_run_dialog.setMessage(R.string.welcome_message);
	    first_run_dialog.setIcon(R.drawable.ic_launcher);
	    
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View view = inflater.inflate(R.layout.first_run, null);
	    
	    first_run_dialog.setView(view);
	    
	    input_url = (EditText) view.findViewById(R.id.input_first_run);
	    save_url = (Button) view.findViewById(R.id.save_prefs_url);
	    save_url.setOnClickListener(this);
	    
	    return first_run_dialog.create();
	}
	
	@Override
	public void onClick(View v) {
	    
	    switch (v.getId()) {
		case R.id.save_prefs_url:
		    
		    if (input_url.getText().toString().length() != 0) {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			final Editor editor = prefs.edit();
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			    editor.putString(getResources().getString(R.string.prefs_key_url), "http://" + input_url.getText().toString() + "/interventix/connector").apply();
			else
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    
				    editor.putString(getResources().getString(R.string.prefs_key_url), input_url.getText().toString()).commit();
				}
			    }).start();
			
			dismiss();
		    }
		    break;
	    }
	}
    }
}
