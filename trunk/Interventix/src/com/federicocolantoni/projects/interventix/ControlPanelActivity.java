
package com.federicocolantoni.projects.interventix;

import java.util.HashMap;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.services.InterventixIntentService;

@SuppressLint("NewApi")
public class ControlPanelActivity extends Activity {

    static final String GLOBAL_PREFERENCES = "Preferences";
    public static final String GET_NOMINATIVO = "com.federico.colantoni.projects.interventix.GET_NOMINATIVO";

    private int mIdUser;
    private String mJson_req, mJson_req2;

    private TextView mLabel_nom;

    private Handler mHandler = new Handler() {

	@Override
	public void handleMessage(Message msg) {

	    String action = (String) msg.obj;

	    if (action.equals(GET_NOMINATIVO)) {
		if (msg.arg1 == RESULT_OK) {
		    Bundle bundle = msg.getData();

		    mLabel_nom.setText(bundle.getString("NOMINATIVO",
			    "Utente sconosciuto"));
		} else {
		    Toast.makeText(
			    ControlPanelActivity.this,
			    "Impossibile recuperare le informazioni sull'utente",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(R.layout.activity_control_panel);

	setNominativo();

	findViewById(R.id.btn_addInterv).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(View v) {

			Intent intent = new Intent(ControlPanelActivity.this,
				TabBarActivity.class);

			startActivity(intent);
		    }
		});

	findViewById(R.id.btn_myInterv).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(View v) {

			Intent intent = new Intent(ControlPanelActivity.this,
				MyInterventionsActivity.class);

			startActivity(intent);
		    }
		});

	findViewById(R.id.btn_exit).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		finish();
	    }
	});
    }

    private void setNominativo() {

	mLabel_nom = (TextView) findViewById(R.id.label_nominativo);

	SharedPreferences prefs = getSharedPreferences(GLOBAL_PREFERENCES,
		MODE_PRIVATE);
	mIdUser = prefs.getInt("ID_USER", Integer.valueOf(-1));

	Map parameters = new HashMap();
	parameters.put("idutente", mIdUser);

	try {
	    mJson_req = JsonCR2.createRequest("users", "get", parameters,
		    mIdUser);
	} catch (NumberFormatException e) {
	    Log.d(MainActivity.DEBUG_TAG,
		    ControlPanelActivity.class.getSimpleName()
			    + " NUMBER_FORMAT_EXCEPTION! ", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG,
		    ControlPanelActivity.class.getSimpleName()
			    + " GENERIC_EXCEPTION!", e);
	}

	Intent intent = new Intent(GET_NOMINATIVO, null, this,
		InterventixIntentService.class);

	Messenger msn = new Messenger(mHandler);

	intent.putExtra("REQUEST_GET_NOMINATIVO", mJson_req);
	intent.putExtra("MESSENGER", msn);

	startService(intent);

	/*AndroidHttpClient request = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", mJson_req);

	request.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		try {
		    JSONObject resp = JsonCR2.read(httpResponse
			    .getBodyAsString());

		    Map req = (HashMap) resp.get("request");

		    String action = req.get("action").toString();
		    String section = req.get("section").toString();

		    if (resp.get("response").toString()
			    .equalsIgnoreCase("success")) {
			if (action.equalsIgnoreCase("get")
				&& section.equalsIgnoreCase("users")) {
			    JSONArray datas = (JSONArray) resp.get("data");

			    Map data = new HashMap();

			    Iterator it = datas.iterator();

			    String nome = null, cognome = null;

			    while (it.hasNext()) {
				data = (Map) it.next();
				String id = data.get("idutente").toString();
				if (Integer.parseInt(id) == mIdUser) {
				    nome = (String) data.get("nome");
				    cognome = (String) data.get("cognome");
				    break;
				}
			    }

			    mNominativo = nome + " " + cognome;
			    mLabel_nom.setText(mNominativo);

			    SharedPreferences pref = getSharedPreferences(
				    GLOBAL_PREFERENCES,
				    ControlPanelActivity.MODE_PRIVATE);
			    final Editor editor = pref.edit();
			    editor.putString("SAVE_USER", mNominativo);

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
			}
		    }
		} catch (ParseException e) {
		    Log.d(MainActivity.DEBUG_TAG, "PARSE_EXCEPTION! ", e);
		} catch (Exception e) {
		    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
		}
	    }

	});*/

	//Map parameters2 = new HashMap();
	/*
	 * parameters2.put(Integer.valueOf(2), "revision");
	 * ERRORE. Secondo me la versione giusta e' questa..
	 */
	// Versione Corretta 
	/*parameters2.put("revision", Integer.valueOf(2));

	try {
	    json_req2 = JsonCR2.createRequest("clients", "syncro", parameters2,
		    idUser);
	} catch (NumberFormatException e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " NUMBER_FORMAT_EXCEPTION! ", e);
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " GENERIC_EXCEPTION!", e);
	}

	AndroidHttpClient request2 = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap2 = new ParameterMap();
	paramMap2.add("DATA", json_req2);

	request2.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse2) {

		try {
		    JSONObject resp = JsonCR2.read(httpResponse2
			    .getBodyAsString());

		    Map req = (Map) resp.get("request");

		    String action = req.get("action").toString();
		    String section = req.get("section").toString();

		    if (resp.get("response").toString()
			    .equalsIgnoreCase("success")) {
			if (action.equalsIgnoreCase("syncro")
				&& section.equalsIgnoreCase("clients")) {
			    Map data = (Map) resp.get("data");

			    String rev = data.get("revision").toString();

			}
		    }
		} catch (ParseException e) {
		    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
			    + " PARSE_EXCEPTION! ", e);
		} catch (Exception e) {
		    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
			    + " GENERIC_EXCEPTION!", e);
		}
	    }
	});*/
    }
}
