
package com.federicocolantoni.projects.interventix;

import java.io.IOException;

import multiface.crypto.cr2.JsonCR2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.services.InterventixIntentService;

@SuppressLint({ "HandlerLeak", "NewApi" })
public class MainActivity extends Activity {

    public static final String DEBUG_TAG = "INTERVENTIX";
    static final String GLOBAL_PREFERENCES = "Preferences";

    public static final String LOGIN = "com.federico.colantoni.projects.interventix.LOGIN_SUCCESSFULL";

    private EditText username, password;
    private String json_req;

    private ProgressDialog dialog;

    private Handler handler = new Handler() {

	@Override
	public void handleMessage(Message msg) {

	    String action = (String) msg.obj;

	    if (action.equals(LOGIN)) {
		if (msg.arg1 == RESULT_OK) {

		    dialog.dismiss();

		    Toast.makeText(MainActivity.this, "ACCESSO CONSENTITO",
			    Toast.LENGTH_SHORT).show();

		    username.setText(new String());
		    password.setText(new String());

		    Intent intent = new Intent(MainActivity.this,
			    ControlPanelActivity.class);

		    startActivity(intent);
		} else {

		    dialog.dismiss();

		    Toast.makeText(MainActivity.this, "ACCESSO NEGATO!",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	}
    };

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
		    Log.d(DEBUG_TAG, "INTERRUPTED_EXCEPTION!", e);
		} catch (IOException e) {
		    Log.d(DEBUG_TAG, "IO_EXCEPTION!", e);
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
	    Log.d(DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}

	Intent intent = new Intent("LOGIN", null, this,
		InterventixIntentService.class);

	Messenger msn = new Messenger(handler);

	intent.putExtra("REQUEST_LOGIN", json_req);
	intent.putExtra("MESSENGER", msn);

	startService(intent);

	dialog = ProgressDialog.show(MainActivity.this, "Connessione",
		"Attendere prego...", true);
	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

}
