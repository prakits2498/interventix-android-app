
package com.federicocolantoni.projects.interventix;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class ControlPanelActivity extends Activity {

    private static final String DEBUG_TAG = "INTERVENTIX";
    static final String GLOBAL_PREFERENCES = "Preferences";

    private int idUser;
    private String nominativo, json_req, json_req2;

    private TextView label_nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_index);

	label_nom = (TextView) findViewById(R.id.label_nominativo);

	SharedPreferences prefs = getSharedPreferences(GLOBAL_PREFERENCES,
		MODE_PRIVATE);
	idUser = prefs.getInt("iduser", Integer.valueOf(-1));

	Map parameters = new HashMap();
	parameters.put(idUser, "idutente");

	try {
	    json_req = JsonCR2
		    .createRequest("users", "get", parameters, idUser);
	} catch (NumberFormatException e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " NUMBER_FORMAT_EXCEPTION! " + e.toString());
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " GENERIC_EXCEPTION! " + e.toString());
	}

	AndroidHttpClient request = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", json_req);

	request.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		try {
		    JSONObject resp = JsonCR2.read(httpResponse
			    .getBodyAsString());

		    Map req = (HashMap) resp.get("action");

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
				if (Integer.parseInt(id) == idUser) {
				    nome = (String) data.get("nome");
				    cognome = (String) data.get("cognome");
				    break;
				}
			    }

			    nominativo = nome + " " + cognome;
			    label_nom.setText(nominativo);

			    SharedPreferences pref = getSharedPreferences(
				    GLOBAL_PREFERENCES,
				    ControlPanelActivity.MODE_PRIVATE);
			    final Editor editor = pref.edit();
			    editor.putString("SAVE_USER", nominativo);

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
		    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
			    + " PARSE_EXCEPTION! " + e.toString());
		} catch (Exception e) {
		    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
			    + " GENERIC_EXCEPTION! " + e.toString());
		}
	    }

	});

	Map parameters2 = new HashMap();
	parameters2.put(Integer.valueOf(2), "revision");

	try {
	    json_req2 = JsonCR2.createRequest("clients", "syncro", parameters2,
		    idUser);
	} catch (NumberFormatException e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " NUMBER_FORMAT_EXCEPTION! " + e.toString());
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " GENERIC_EXCEPTION! " + e.toString());
	}

	AndroidHttpClient request2 = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap2 = new ParameterMap();
	paramMap2.add("DATA", json_req2);

	request2.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		try {
		    JSONObject resp = JsonCR2.read(httpResponse
			    .getBodyAsString());

		    Map req = (Map) resp.get("action");

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
			    + " PARSE_EXCEPTION! " + e.toString());
		} catch (Exception e) {
		    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
			    + " GENERIC_EXCEPTION! " + e.toString());
		}
	    }
	});
    }

    public void addInterv(View v) {

	Intent intent = new Intent(ControlPanelActivity.this, TabBarActivity.class);

	startActivity(intent);
    }

    public void myInterv(View v) {

	Intent intent = new Intent(ControlPanelActivity.this,
		MyInterventionsActivity.class);

	startActivity(intent);
    }

    public void back(View v) {

	finish();
    }
}
