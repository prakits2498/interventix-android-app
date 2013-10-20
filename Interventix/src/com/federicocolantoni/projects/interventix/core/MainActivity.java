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
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BuildConfig;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.R.id;
import com.federicocolantoni.projects.interventix.modules.login.Login;
import com.federicocolantoni.projects.interventix.settings.SettingActivity;
import com.federicocolantoni.projects.interventix.settings.SettingSupportActivity;
import com.federicocolantoni.projects.interventix.task.ReadDefaultPreferences;
import com.federicocolantoni.projects.interventix.utils.ChangeLogDialog;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.activity_main);
	
	BugSenseHandler.initAndStartSession(MainActivity.this, Constants.API_KEY);
	
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
	    
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.activity_preferences_options, true);
	    else
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.activity_support_preferences_options, true);
	    
	    // final Editor edit = prefs.edit();
	    // edit.putString(getResources().getString(R.string.prefs_key_url),
	    // prefs.getString(getResources().getString(R.string.prefs_key_url),
	    // ""));
	    //
	    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
	    // edit.apply();
	    // }
	    // else {
	    // new Thread(new Runnable() {
	    //
	    // @Override
	    // public void run() {
	    // edit.commit();
	    // }
	    // }).start();
	    // }
	}
	catch (InterruptedException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	catch (ExecutionException e) {
	    
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	System.out.println("DEFAULT URL: " + prefs.getString(getResources().getString(R.string.prefs_key_url), ""));
	
	TextView tv_changelog = (TextView) findViewById(R.id.tv_changelog);
	tv_changelog.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View v) {
		
		// Launch change log dialog
		ChangeLogDialog _ChangelogDialog = new ChangeLogDialog(MainActivity.this);
		_ChangelogDialog.show();
	    }
	});
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
	    
	    save_url = (Button) view.findViewById(R.id.save_prefs_url);
	    save_url.setOnClickListener(this);
	    
	    return first_run_dialog.create();
	}
	
	@Override
	public void onClick(View v) {
	    
	    switch (v.getId()) {
		case R.id.save_prefs_url:
		    
		    dismiss();
		    break;
	    }
	}
    }
    
    @Override
    protected void onDestroy() {
	super.onDestroy();
	if (BuildConfig.DEBUG) {
	    System.gc();
	}
    }
}
