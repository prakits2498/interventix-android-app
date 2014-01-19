package com.federicocolantoni.projects.interventix.modules.login;

import java.util.HashMap;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.activity.HomeActivity;
import com.federicocolantoni.projects.interventix.task.GetLogin;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_login)
public class Login extends Fragment {

	@ViewById(R.id.field_password)
	EditText password;

	@ViewById(R.id.field_username)
	EditText username;

	@SystemService
	ConnectivityManager connMgr;

	private SharedPreferences prefs;

	@Override
	public void onStart() {

		super.onStart();

		prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	}

	@Click(R.id.btn_login)
	void startLogin() {

		NetworkInfo info = connMgr.getActiveNetworkInfo();

		if (username.getText().toString().length() == 0 || password.getText().toString().length() == 0)
			InterventixToast.makeToast(getActivity(), getString(R.string.toast_username_and_password_required), Toast.LENGTH_SHORT);
		else {

			String json_req = new String();

			if (info != null)
				switch (info.getType()) {

					case ConnectivityManager.TYPE_WIFI:

						if (info.isAvailable() && info.isConnected())
							try {
								HashMap<String, String> parameters = new HashMap<String, String>();

								parameters.put("username", username.getText().toString());
								parameters.put("password", password.getText().toString());
								parameters.put("type", "TECNICO");

								json_req = JsonCR2.createRequest("users", "login", parameters, -1);

								new GetLogin(getActivity(), username.getText().toString(), password.getText().toString()).execute(json_req);

								password.setText("");
							} catch (Exception e) {

								e.printStackTrace();
								BugSenseHandler.sendException(e);
							}
						else {

							String usrnm = prefs.getString(Constants.USERNAME, null);
							String psswrd = prefs.getString(Constants.PASSWORD, null);

							if (usrnm != null && psswrd != null)
								if (usrnm.equals(username.getText().toString()) && psswrd.equals(password.getText().toString())) {

									password.setText("");

									InterventixToast.makeToast(getActivity(), getString(R.string.toast_offline_access), Toast.LENGTH_LONG);

									startActivity(new Intent(getActivity(), HomeActivity.class));
								}
						}

						break;

					case ConnectivityManager.TYPE_MOBILE:

						if (info.isAvailable() && info.isConnected())
							try {
								HashMap<String, String> parameters = new HashMap<String, String>();

								parameters.put("username", username.getText().toString());
								parameters.put("password", password.getText().toString());
								parameters.put("type", "TECNICO");

								json_req = JsonCR2.createRequest("users", "login", parameters, -1);

								new GetLogin(getActivity(), username.getText().toString(), password.getText().toString()).execute(json_req, username.getText().toString(), password
										.getText().toString());

								password.setText("");
							} catch (Exception e) {

								e.printStackTrace();
								BugSenseHandler.sendException(e);
							}
						else {

							String usrnm = prefs.getString(Constants.USERNAME, null);
							String psswrd = prefs.getString(Constants.PASSWORD, null);

							if (usrnm != null && psswrd != null)
								if (usrnm.equals(username.getText().toString()) && psswrd.equals(password.getText().toString())) {

									password.setText("");

									InterventixToast.makeToast(getActivity(), getString(R.string.toast_offline_access), Toast.LENGTH_LONG);

									startActivity(new Intent(getActivity(), com.federicocolantoni.projects.interventix.activity.HomeActivity_.class));
								}
						}

						break;
				}
			else {

				AlertDialog.Builder connUnavailable = new Builder(getActivity());

				connUnavailable.setTitle(getString(R.string.no_active_connection));
				connUnavailable.setMessage(R.string.conn_unavailable_text);
				connUnavailable.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
				connUnavailable.setNeutralButton(R.string.enable_wifi, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						dialog.dismiss();
					}
				});
				connUnavailable.setPositiveButton(R.string.enable_mobile, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(intent);
						dialog.dismiss();
					}
				});
				connUnavailable.setIcon(R.drawable.ic_launcher);

				connUnavailable.create().show();
			}
		}
	}
}
