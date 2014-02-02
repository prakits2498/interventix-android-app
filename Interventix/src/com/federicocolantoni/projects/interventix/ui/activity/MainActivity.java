package com.federicocolantoni.projects.interventix.ui.activity;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.BuildConfig;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
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

	boolean authenticated = false;

	@SystemService
	AccountManager accountManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

		Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

		for (Account account : accounts) {

			if (account.type.equals(Constants.ACCOUNT_TYPE_INTERVENTIX)) {
				authenticated = true;
				break;
			}
		}

		if (!authenticated) {
			FragmentManager manager = getSupportFragmentManager();

			com.federicocolantoni.projects.interventix.modules.login.Login_ fragLogin = new com.federicocolantoni.projects.interventix.modules.login.Login_();

			Bundle bundle = new Bundle();
			bundle.putString(AccountManager.KEY_AUTHTOKEN, Constants.ACCOUNT_AUTH_TOKEN);

			fragLogin.setArguments(bundle);

			FragmentTransaction transaction = manager.beginTransaction();

			transaction.replace(R.id.frag_login, fragLogin);
			transaction.setCustomAnimations(R.anim.fade_out, R.anim.fade_in);
			transaction.commit();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.activity_preferences_options, true);
			else
				PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.activity_support_preferences_options, true);
		} else {
			startActivity(new Intent(this, com.federicocolantoni.projects.interventix.ui.activity.HomeActivity_.class));
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
	protected void onResume() {

		super.onResume();

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

			for (Account account : accounts) {

				if (account.type.equals(Constants.ACCOUNT_TYPE_INTERVENTIX)) {
					authenticated = true;
					break;
				}
			}

			if (authenticated)
				startActivity(new Intent(this, com.federicocolantoni.projects.interventix.ui.activity.HomeActivity_.class));
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