package com.federicocolantoni.projects.interventix.ui.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.controller.UtenteController;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.settings.SettingActivity;
import com.federicocolantoni.projects.interventix.utils.ChangeLogDialog;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

	@ViewById(R.id.tv_changelog)
	TextView tvChangelog;

	@StringRes(R.string.welcome_title)
	static String welcomeTitle;

	@StringRes(R.string.welcome_message)
	static String welcomeMessage;

	private AccountManager accountManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		accountManager = AccountManager.get(this);

		BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

		SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

		String username = prefs.getString(Constants.USERNAME, "");

		Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

		boolean utenteTrovato = false;

		for (Account account : accounts) {

			// passo 3)
			if (account.name.equals(username)) {

				utenteTrovato = true;

				String pwd = accountManager.getPassword(account);

				// passo 5)
				if (pwd == null) {

					System.out.println("Utente " + username + " trovato - password nulla");

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

				// passo 4)
				else {

					RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

					UtenteController.tecnicoLoggato = utenteDao.queryForEq("username", account.name).get(0);

					com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

					startActivity(new Intent(this, com.federicocolantoni.projects.interventix.ui.activity.HomeActivity_.class));
				}

				break;
			}
		}

		// passo 2)
		if (!utenteTrovato) {

			System.out.println("Utente " + username + " non trovato");

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

		getMenuInflater().inflate(R.menu.menu_main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case R.id.menu_options:

				Intent intent = new Intent(this, SettingActivity.class);
				intent.setAction("com.federicocolantoni.projects.interventix.OPEN_SETTINGS");

				startActivity(intent);

				break;
		}

		return super.onOptionsItemSelected(item);
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
			View view = inflater.inflate(R.layout.dialog_first_run, null);

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
}
