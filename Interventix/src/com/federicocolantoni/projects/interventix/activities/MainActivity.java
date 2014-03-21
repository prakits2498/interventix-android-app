package com.federicocolantoni.projects.interventix.activities;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.joda.time.DateTime;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog.OnDateSetListener;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.ChangeLogDialog;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.InterventixToast;
import com.federicocolantoni.projects.interventix.models.Utente;
import com.federicocolantoni.projects.interventix.models.UtenteController;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements OnDateSetListener {

    @ViewById(R.id.tv_changelog)
    TextView tvChangelog;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    RuntimeExceptionDao<Utente, Long> utenteDao;

    @StringRes(R.string.welcome_title)
    static String welcomeTitle;

    @StringRes(R.string.welcome_message)
    static String welcomeMessage;

    private AccountManager accountManager;

    SharedPreferences globalPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	accountManager = AccountManager.get(this);

	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

	globalPrefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

	String username = globalPrefs.getString(Constants.USERNAME, "");

	Account[] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE_INTERVENTIX);

	boolean utenteTrovato = false;

	for (Account account : accounts) {

	    // passo 3)
	    if (account.name.equals(username)) {

		utenteTrovato = true;

		String pwd = accountManager.getPassword(account);

		// passo 5)
		if (pwd == null) {

		    System.out.println("Utente " + account.name + " trovato - password nulla");

		    FragmentManager manager = getSupportFragmentManager();

		    com.federicocolantoni.projects.interventix.fragments.LoginFragment_ fragLogin = new com.federicocolantoni.projects.interventix.fragments.LoginFragment_();

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

		    UtenteController.tecnicoLoggato = utenteDao.queryForEq(Constants.ORMLITE_USERNAME, account.name).get(0);

		    startActivity(new Intent(this, com.federicocolantoni.projects.interventix.activities.HomeActivity_.class));
		}

		break;
	    }
	}

	// passo 2)
	if (!utenteTrovato) {

	    System.out.println("Utente " + username + " non trovato");

	    FragmentManager manager = getSupportFragmentManager();

	    com.federicocolantoni.projects.interventix.fragments.LoginFragment_ fragLogin = new com.federicocolantoni.projects.interventix.fragments.LoginFragment_();

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

		ChangeLogDialog changelogDialog = new ChangeLogDialog(MainActivity.this);
		changelogDialog.show();
	    }
	});
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();

	Interventix.releaseDbHelper();
    }

    public void showPicker(View view) {

	FragmentManager fm = getSupportFragmentManager();
	DateTime now = DateTime.now();
	CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(this, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
	calendarDatePickerDialog.setYearRange(1970, 3000);
	calendarDatePickerDialog.show(fm, "fragment_date_picker_name");
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

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

	InterventixToast.makeToast("Year: " + year + "\nMonth: " + monthOfYear + "\nDay: " + dayOfMonth, Toast.LENGTH_LONG);
    }
}
