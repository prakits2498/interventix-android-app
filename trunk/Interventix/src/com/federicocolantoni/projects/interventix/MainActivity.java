
package com.federicocolantoni.projects.interventix;

import java.io.IOException;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class MainActivity extends Activity {

    private EditText username, password;
    private String json_req;

    private static final String DEBUG_TAG = "INTERVENTIX";
    static final String GLOBAL_PREFERENCES = "Preferences";

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_main);

	findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		try {
		    loginService(v);
		} catch (InterruptedException e) {
		    Log.d(DEBUG_TAG, MainActivity.class.getSimpleName()
			    + " INTERRUPTED_EXCEPTION!", e);
		} catch (IOException e) {
		    Log.d(DEBUG_TAG, MainActivity.class.getSimpleName()
			    + " IO_EXCEPTION!", e);
		}
	    }
	});

    }

    private void loginService(View v) throws InterruptedException, IOException {

	username = (EditText) findViewById(R.id.field_username);
	password = (EditText) findViewById(R.id.field_password);

	try {
	    json_req = JsonCR2.createRequestLogin(
		    username.getText().toString(), password.getText()
			    .toString());
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " GENERIC_EXCEPTION!", e);
	}

	dialog = ProgressDialog.show(this, "Connessione", "Attendere prego...",
		true);
	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	AndroidHttpClient request = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", json_req);

	request.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		requestComplete(httpResponse);
	    }
	});
    }

    private void requestComplete(HttpResponse response) {

	try {
	    JSONObject resp = JsonCR2.read(response.getBodyAsString());
	    if (resp.get("response").toString().equalsIgnoreCase("success")) {
		Toast.makeText(this.getBaseContext(), "ACCESSO CONSENTITO",
			Toast.LENGTH_SHORT).show();

		dialog.dismiss();

		Intent intent = new Intent(MainActivity.this,
			ControlPanelActivity.class);

		SharedPreferences prefs = getSharedPreferences(
			GLOBAL_PREFERENCES, MODE_PRIVATE);

		final Editor editor = prefs.edit();

		int IdUser = Integer.parseInt(resp.get("iduser").toString());
		editor.putInt("ID_USER", IdUser);

		editor.putString("REVISION", "0");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		    editor.apply();
		} else {
		    new Thread() {

			@Override
			public void run() {

			    editor.commit();
			}
		    }.start();
		}

		username.setText(new String());
		password.setText(new String());

		startActivity(intent);

	    } else {
		Toast.makeText(this.getBaseContext(), "ACCESSO NEGATO!",
			Toast.LENGTH_SHORT).show();
		dialog.dismiss();
	    }
	} catch (ParseException e) {
	    Log.d(DEBUG_TAG, MainActivity.class.getSimpleName()
		    + " PARSE_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, MainActivity.class.getSimpleName()
		    + " GENERIC_EXCEPTION!", e);
	}
    }
}
