
package com.federicocolantoni.projects.interventix.core;

import java.io.IOException;

import multiface.crypto.cr2.JsonCR2;
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

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.services.InterventixIntentService;

public class MainActivity extends Activity {

    private EditText mUsername, mPassword;
    private String mJson_req;

    private ProgressDialog mDialog;

    private Handler mHandler = new MyHandler() {

	@Override
	public void handleMessage(Message msg) {

	    String action = (String) msg.obj;

	    if (action.equals(Constants.LOGIN)) {
		if (msg.arg1 == RESULT_OK) {

		    mDialog.dismiss();

		    Toast.makeText(MainActivity.this, "ACCESSO CONSENTITO",
			    Toast.LENGTH_SHORT).show();

		    mUsername.setText(new String());
		    mPassword.setText(new String());

		    Intent intent = new Intent(MainActivity.this,
			    ControlPanelActivity.class);

		    startActivity(intent);
		} else {

		    mDialog.dismiss();

		    Toast.makeText(MainActivity.this, "ACCESSO NEGATO!",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	}
    };

    private static class MyHandler extends Handler {

    }

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
		    Log.d(Constants.DEBUG_TAG, "INTERRUPTED_EXCEPTION!", e);
		} catch (IOException e) {
		    Log.d(Constants.DEBUG_TAG, "IO_EXCEPTION!", e);
		}
	    }
	});

    }

    private void loginService(View v) throws InterruptedException, IOException {

	mUsername = (EditText) findViewById(R.id.field_username);
	mPassword = (EditText) findViewById(R.id.field_password);

	try {
	    mJson_req = JsonCR2.createRequestLogin(mUsername.getText()
		    .toString(), mPassword.getText().toString());
	} catch (Exception e) {
	    Log.d(Constants.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}

	Intent intent = new Intent(Constants.LOGIN, null, this,
		InterventixIntentService.class);

	Messenger msn = new Messenger(mHandler);

	intent.putExtra(Constants.REQUEST_LOGIN, mJson_req);
	intent.putExtra(Constants.MESSENGER, msn);

	startService(intent);

	mDialog = ProgressDialog.show(MainActivity.this, "Connessione",
		"Attendere prego...", true);
	mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }
}
