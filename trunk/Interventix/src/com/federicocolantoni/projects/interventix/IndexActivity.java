
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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class IndexActivity extends Activity {

    private String idUser, nominativo, json_req;
    private final String debugTag = "xxx";
    private TextView label_nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_index);

	Bundle extra = getIntent().getExtras();
	setIdUser(extra.getString("iduser"));

	Log.d(debugTag, "ID user: " + getIdUser());

	label_nom = (TextView) findViewById(R.id.label_nominativo);

	Map parameters = new HashMap();
	parameters.put(idUser, "idutente");

	try {
	    json_req = JsonCR2.createRequest("users", "get", parameters,
		    Integer.parseInt(idUser));
	} catch (NumberFormatException e) {
	    Log.d(debugTag, e.toString());
	} catch (Exception e) {
	    Log.d(debugTag, e.toString());
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
		    JSONObject resp = JsonCR2.readRequest(httpResponse
			    .getBodyAsString());

		    if (resp.get("response").toString()
			    .equalsIgnoreCase("success")) {
			JSONArray datas = (JSONArray) resp.get("data");

			Map data = new HashMap();

			Iterator it = datas.iterator();

			String nome = null, cognome = null;

			while (it.hasNext()) {
			    data = (Map) it.next();
			    String id = data.get("idutente").toString();
			    if (id.equalsIgnoreCase(idUser)) {
				nome = (String) data.get("nome");
				cognome = (String) data.get("cognome");
				break;
			    }
			}

			nominativo = nome + " " + cognome;
			label_nom.setText(nominativo);
		    }
		} catch (ParseException e) {
		    Log.d(debugTag, e.toString());
		} catch (Exception e) {
		    Log.d(debugTag, e.toString());
		}
	    }

	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_index, menu);
	return true;
    }

    public void addInterv(View v) {

	Intent intent = new Intent(IndexActivity.this, TabBarActivity.class);

	startActivity(intent);
    }

    public void myInterv(View v) {

	Intent intent = new Intent(IndexActivity.this,
		MyInterventionsActivity.class);
	Bundle extra = new Bundle();
	extra.putString("iduser", idUser);

	intent.putExtras(extra);

	startActivity(intent);
    }

    public void back(View v) {

	finish();
    }

    /**
     * @return the idUser
     */
    public String getIdUser() {

	return idUser;
    }

    /**
     * @param idUser
     *            the idUser to set
     */
    public void setIdUser(String idUser) {

	this.idUser = idUser;
    }
}
