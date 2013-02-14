
package com.federicocolantoni.projects.interventix.services;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.federicocolantoni.projects.interventix.MainActivity;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

@SuppressLint("NewApi")
public class InterventixIntentService extends IntentService {

    private static final String BASE_URL = "http://176.31.243.123:8080/interventix/connector";
    static final String GLOBAL_PREFERENCES = "Preferences";

    public InterventixIntentService() {

	super("InterventixIntentService");
	Log.d(MainActivity.DEBUG_TAG, "Service avviato");
    }

    @Override
    public void onCreate() {

	super.onCreate();
    }

    @Override
    public void onDestroy() {

	super.onDestroy();
    }

    @Override
    protected void onHandleIntent(final Intent intent) {

	if (intent != null) {
	    if (intent.getAction().equals("LOGIN")) {

		final Bundle extras = intent.getExtras();

		new Thread(new Runnable() {

		    @Override
		    public void run() {

			login(extras, intent);
		    }
		}).start();
	    }
	}
    }

    private void login(Bundle extras, Intent intent) {

	AndroidHttpClient request = new AndroidHttpClient(BASE_URL);
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", intent.getStringExtra("REQUEST_LOGIN"));

	HttpResponse response = request.post("", paramMap);

	try {
	    JSONObject resp = JsonCR2.read(response.getBodyAsString());

	    if (resp.get("response").toString().equalsIgnoreCase("success")) {

		SharedPreferences prefs = getSharedPreferences(
			GLOBAL_PREFERENCES, MODE_PRIVATE);

		final Editor editor = prefs.edit();

		int IdUser = Integer.parseInt(resp.get("iduser").toString());
		editor.putInt("ID_USER", IdUser);

		editor.putLong("REVISION", 0);

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

		int result = Activity.RESULT_OK;

		if (extras != null) {
		    Messenger messenger = (Messenger) extras.get("MESSENGER");
		    Message message = Message.obtain();
		    message.arg1 = result;
		    message.obj = MainActivity.LOGIN;
		    try {
			messenger.send(message);
		    } catch (RemoteException e) {
			Log.d(MainActivity.DEBUG_TAG, "SENDING MESSAGE ERROR!",
				e);
		    }
		}
	    } else {
		int result = Activity.RESULT_CANCELED;

		if (extras != null) {
		    Messenger messenger = (Messenger) extras.get("MESSENGER");
		    Message message = Message.obtain();
		    message.arg1 = result;
		    message.obj = MainActivity.LOGIN;
		    try {
			messenger.send(message);
		    } catch (RemoteException e) {
			Log.d(MainActivity.DEBUG_TAG, "SENDING MESSAGE ERROR!",
				e);
		    }
		}
	    }
	} catch (ParseException e) {
	    Log.d(MainActivity.DEBUG_TAG, "PARSE_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}
    }
}
