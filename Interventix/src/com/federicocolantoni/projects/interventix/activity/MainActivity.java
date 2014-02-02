package com.federicocolantoni.projects.interventix.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
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
import com.federicocolantoni.projects.interventix.settings.SettingActivity;
import com.federicocolantoni.projects.interventix.settings.SettingSupportActivity;
import com.federicocolantoni.projects.interventix.utils.ChangeLogDialog;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

	@ViewById(R.id.tv_changelog)
	TextView tvChangelog;

	@StringRes(R.string.welcome_title)
	static String welcomeTitle;

	@StringRes(R.string.welcome_message)
	static String welcomeMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

		FragmentManager manager = getSupportFragmentManager();

		com.federicocolantoni.projects.interventix.modules.login.Login_ fragLogin = new com.federicocolantoni.projects.interventix.modules.login.Login_();

		FragmentTransaction transaction = manager.beginTransaction();

		transaction.replace(R.id.frag_login, fragLogin);
		transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
		transaction.commit();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.activity_preferences_options, true);
		else
			PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.activity_support_preferences_options, true);
	}

	@Override
	protected void onStart() {

		super.onStart();

		tvChangelog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Launch change log dialog
				ChangeLogDialog changelogDialog = new ChangeLogDialog(MainActivity.this);
				changelogDialog.show();
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
			case R.id.menu_options:

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

		private Button btn_ok;

		public FirstRunDialog() {

		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder firstRunDialog = new Builder(getActivity());

			firstRunDialog.setTitle(welcomeTitle);
			firstRunDialog.setMessage(welcomeMessage);
			firstRunDialog.setIcon(R.drawable.ic_launcher);

			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.first_run, null);

			firstRunDialog.setView(view);

			btn_ok = (Button) view.findViewById(R.id.save_prefs_url);
			btn_ok.setOnClickListener(this);

			return firstRunDialog.create();
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
