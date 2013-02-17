
package com.federicocolantoni.projects.interventix.services;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.federicocolantoni.projects.interventix.ControlPanelActivity;
import com.federicocolantoni.projects.interventix.MainActivity;
import com.federicocolantoni.projects.interventix.data.InterventixAPI;
import com.federicocolantoni.projects.interventix.intervento.ClientiFragment;
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
	    if (intent.getAction().equals(MainActivity.LOGIN)) {

		final Bundle extras = intent.getExtras();

		new Thread(new Runnable() {

		    @Override
		    public void run() {

			login(extras, intent);
		    }
		}).start();
	    } else if (intent.getAction().equals(
		    ControlPanelActivity.GET_NOMINATIVO)) {

		final Bundle extras = intent.getExtras();

		new Thread(new Runnable() {

		    @Override
		    public void run() {

			getNominativo(extras, intent);
		    }

		}).start();
	    } else if (intent.getAction().equals(
		    ClientiFragment.GET_LISTA_CLIENTI)) {

		final Bundle extras = intent.getExtras();

		if (intent.getBooleanExtra("DOWNLOADED", false) == false) {

		    new Thread(new Runnable() {

			@Override
			public void run() {

			    getListaClienti(extras, intent);
			}

		    }).start();
		} else {
		    int result = Activity.RESULT_OK;

		    if (extras != null) {
			Messenger messenger = (Messenger) extras
				.get("MESSENGER");
			Message message = Message.obtain();
			message.arg1 = result;
			message.obj = ClientiFragment.GET_LISTA_CLIENTI;

			Bundle bundle = new Bundle();
			bundle.putBoolean("DOWNLOADED", false);

			message.setData(bundle);

			try {
			    messenger.send(message);
			} catch (RemoteException e) {
			    Log.d(MainActivity.DEBUG_TAG,
				    "SENDING MESSAGE ERROR!", e);
			}
		    }
		}
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

    private void getNominativo(Bundle extras, Intent intent) {

	AndroidHttpClient request = new AndroidHttpClient(BASE_URL);
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", intent.getStringExtra("REQUEST_GET_NOMINATIVO"));

	HttpResponse response = request.post("", paramMap);

	try {
	    JSONObject resp = JsonCR2.read(response.getBodyAsString());

	    Map req = (HashMap) resp.get("request");

	    String action = req.get("action").toString();
	    String section = req.get("section").toString();

	    if (resp.get("response").toString().equalsIgnoreCase("success")) {
		if (action.equalsIgnoreCase("get")
			&& section.equalsIgnoreCase("users")) {
		    JSONArray datas = (JSONArray) resp.get("data");

		    Map data = new HashMap();

		    Iterator it = datas.iterator();

		    String nome = null, cognome = null;

		    SharedPreferences prefs = getSharedPreferences(
			    GLOBAL_PREFERENCES, MODE_PRIVATE);

		    while (it.hasNext()) {
			data = (Map) it.next();
			String id = data.get("idutente").toString();
			if (Integer.parseInt(id) == prefs.getInt("ID_USER",
				Integer.valueOf(-1))) {
			    nome = (String) data.get("nome");
			    cognome = (String) data.get("cognome");
			    break;
			}
		    }

		    SharedPreferences pref = getSharedPreferences(
			    GLOBAL_PREFERENCES,
			    ControlPanelActivity.MODE_PRIVATE);
		    final Editor editor = pref.edit();
		    editor.putString("SAVE_USER", nome + " " + cognome);

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
			Messenger messenger = (Messenger) extras
				.get("MESSENGER");
			Message message = Message.obtain();
			message.arg1 = result;
			message.obj = ControlPanelActivity.GET_NOMINATIVO;

			Bundle bundle = new Bundle();
			bundle.putString("NOMINATIVO", nome + " " + cognome);
			message.setData(bundle);

			try {
			    messenger.send(message);
			} catch (RemoteException e) {
			    Log.d(MainActivity.DEBUG_TAG,
				    "SENDING MESSAGE ERROR!", e);
			}
		    }
		} else {
		    int result = Activity.RESULT_CANCELED;

		    if (extras != null) {
			Messenger messenger = (Messenger) extras
				.get("MESSENGER");
			Message message = Message.obtain();
			message.arg1 = result;
			message.obj = ControlPanelActivity.GET_NOMINATIVO;

			try {
			    messenger.send(message);
			} catch (RemoteException e) {
			    Log.d(MainActivity.DEBUG_TAG,
				    "SENDING MESSAGE ERROR!", e);
			}
		    }
		}
	    }
	} catch (ParseException e) {
	    Log.d(MainActivity.DEBUG_TAG, "PARSE_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}
    }

    private void getListaClienti(Bundle extras, Intent intent) {

	AndroidHttpClient request = new AndroidHttpClient(BASE_URL);
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", intent.getStringExtra("REQUEST_GET_LISTA_CLIENTI"));

	request.post("", paramMap);

	HttpResponse response = request.post("", paramMap);

	try {
	    JSONObject resp = JsonCR2.read(response.getBodyAsString());

	    Map req = (Map) resp.get("request");

	    String action = req.get("action").toString();
	    String section = req.get("section").toString();

	    if (resp.get("response").toString().equalsIgnoreCase("success")) {
		if (action.equalsIgnoreCase("syncro")
			&& section.equalsIgnoreCase("clients")) {

		    Map data = (Map) resp.get("data");

		    long remoteRev = (Long) data.get("revision");

		    SharedPreferences prefs = getSharedPreferences(
			    GLOBAL_PREFERENCES, MODE_PRIVATE);

		    if (remoteRev > prefs.getLong("REVISION", 0)) {
			final Editor editor = prefs.edit();

			editor.putLong("REVISION", remoteRev);

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

			//************* UPDATING DATABASE - START **************\\

			JSONArray clienti_del = (JSONArray) data.get("del");

			for (int i = 0; i < clienti_del.size(); i++) {
			    JSONObject obj = (JSONObject) clienti_del.get(i);

			    insertClient(obj);
			}

			JSONArray clienti_mod = (JSONArray) data.get("mod");

			for (int i = 0; i < clienti_mod.size(); i++) {
			    JSONObject obj = (JSONObject) clienti_mod.get(i);

			    insertClient(obj);
			}

			//************* UPDATING DATABASE - FINISH **************\\

			int result = Activity.RESULT_OK;

			if (extras != null) {
			    Messenger messenger = (Messenger) extras
				    .get("MESSENGER");
			    Message message = Message.obtain();
			    message.arg1 = result;
			    message.obj = ClientiFragment.GET_LISTA_CLIENTI;

			    Bundle bundle = new Bundle();
			    bundle.putBoolean("DOWNLOADED",
				    ClientiFragment.firstTimeDownloaded = true);

			    message.setData(bundle);

			    try {
				messenger.send(message);
			    } catch (RemoteException e) {
				Log.d(MainActivity.DEBUG_TAG,
					"SENDING MESSAGE ERROR!", e);
			    }
			}
		    } else {
			int result = Activity.RESULT_OK;

			if (extras != null) {
			    Messenger messenger = (Messenger) extras
				    .get("MESSENGER");
			    Message message = Message.obtain();
			    message.arg1 = result;
			    message.obj = ClientiFragment.GET_LISTA_CLIENTI;

			    Bundle bundle = new Bundle();
			    bundle.putBoolean("DOWNLOADED", false);
			    bundle.putBoolean("REV_UPDATED", true);

			    message.setData(bundle);

			    try {
				messenger.send(message);
			    } catch (RemoteException e) {
				Log.d(MainActivity.DEBUG_TAG,
					"SENDING MESSAGE ERROR!", e);
			    }
			}
		    }
		} else {
		    int result = Activity.RESULT_CANCELED;

		    if (extras != null) {
			Messenger messenger = (Messenger) extras
				.get("MESSENGER");
			Message message = Message.obtain();
			message.arg1 = result;
			message.obj = ClientiFragment.GET_LISTA_CLIENTI;

			try {
			    messenger.send(message);
			} catch (RemoteException e) {
			    Log.d(MainActivity.DEBUG_TAG,
				    "SENDING MESSAGE ERROR!", e);
			}
		    }
		}
	    }
	} catch (ParseException e) {
	    Log.d(MainActivity.DEBUG_TAG, "PARSE_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}
    }

    private void insertClient(JSONObject obj) {

	ContentResolver cr = getContentResolver();
	Uri target = InterventixAPI.Cliente.URI;

	ContentValues cv = new ContentValues();

	cv.put(InterventixAPI.Cliente.Fields.KEY_CANCELLATO,
		Boolean.parseBoolean(obj.get("cancellato").toString()));
	cv.put(InterventixAPI.Cliente.Fields.KEY_CITTA, obj.get("citta")
		.toString());
	cv.put(InterventixAPI.Cliente.Fields.KEY_CODICE_FISCALE,
		obj.get("codicefiscale").toString());

	if (obj.get("email") != null) {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_EMAIL, obj.get("email")
		    .toString());
	} else {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_EMAIL, "");
	}

	if (obj.get("fax") != null) {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_FAX, obj.get("fax")
		    .toString());
	} else {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_FAX, "");
	}

	cv.put(InterventixAPI.Cliente.Fields.KEY_ID_CLIENTE,
		Integer.parseInt(obj.get("idcliente").toString()));
	cv.put(InterventixAPI.Cliente.Fields.KEY_INDIRIZZO, obj
		.get("indirizzo").toString());

	if (obj.get("interno") != null) {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_INTERNO, obj
		    .get("interno").toString());
	} else {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_INTERNO, "");
	}

	cv.put(InterventixAPI.Cliente.Fields.KEY_NOMINATIVO,
		obj.get("nominativo").toString());

	if (obj.get("note") != null) {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_NOTE, obj.get("note")
		    .toString());
	} else {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_NOTE, "");
	}

	cv.put(InterventixAPI.Cliente.Fields.KEY_PARTITA_IVA,
		obj.get("partitaiva").toString());

	if (obj.get("referente") != null) {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_REFERENTE,
		    obj.get("referente").toString());
	} else {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_REFERENTE, "");
	}

	cv.put(InterventixAPI.Cliente.Fields.KEY_REVISIONE,
		Integer.parseInt(obj.get("revisione").toString()));
	cv.put(InterventixAPI.Cliente.Fields.KEY_TELEFONO, obj.get("telefono")
		.toString());

	if (obj.get("ufficio") != null) {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_UFFICIO, obj
		    .get("ufficio").toString());
	} else {
	    cv.put(InterventixAPI.Cliente.Fields.KEY_UFFICIO, "");
	}

	cr.insert(target, cv);
    }
}
