
package com.federicocolantoni.projects.interventix;

import java.io.IOException;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.federicocolanoni.projects.interventix.R;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class MainActivity extends Activity {

    private EditText username, password;
    private String json_req;

    private ProgressDialog dialog;

    // private HttpResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_main, menu);
	return true;
    }

    public void loginService(View v) throws InterruptedException, IOException {

	username = (EditText) findViewById(R.id.field_username);
	password = (EditText) findViewById(R.id.field_password);

	try {
	    json_req = JsonCR2.createRequestLogin(
		    username.getText().toString(), password.getText()
			    .toString());
	} catch (Exception e) {
	    e.printStackTrace();
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
	    JSONObject resp = JsonCR2.readRequest(response.getBodyAsString());
	    if (resp.get("response").toString().equalsIgnoreCase("success")) {
		Toast.makeText(this.getBaseContext(), "ACCESSO CONSENTITO",
			Toast.LENGTH_SHORT).show();
		Log.d("xxx", "ACCESSO CONSENTITO");
		dialog.dismiss();
		startActivity(new Intent(this, IndexActivity.class));

	    } else {
		Toast.makeText(this.getBaseContext(), "ACCESSO NEGATO!",
			Toast.LENGTH_SHORT).show();
		Log.d("xxx", "ACCESSO NEGATO");
	    }
	} catch (ParseException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
