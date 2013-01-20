
package com.federicocolantoni.projects.interventix;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class MyInterventionsActivity extends Activity {

    private String idUser;
    private final String debugTag = "xxx";
    private int first, max;
    private JSONArray datas;

    private ListView table;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_my_interventions);

	Bundle extra = getIntent().getExtras();
	setIdUser(extra.getString("iduser"));

	Log.d(debugTag, "ID user: " + getIdUser());

	first = 0;
	max = 10;

	table = (ListView) findViewById(R.id.list_myInterv);
	this.request();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_my_interventions, menu);
	return true;
    }

    private void request() {

	Map parameters = new HashMap();
	parameters.put(first, "first");
	parameters.put(max, "max");

	JSONArray exclude = new JSONArray();
	exclude.add("firma");

	parameters.put(exclude, "exclude");

	String json_req = null;

	try {
	    json_req = JsonCR2.createRequest("interventions", "my", parameters,
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

	dialog = ProgressDialog.show(this, "Connessione", "Attendere prego...",
		true);
	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	request.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		try {
		    JSONObject resp = JsonCR2.readRequest(httpResponse
			    .getBodyAsString());

		    if (resp.get("response").toString()
			    .equalsIgnoreCase("success")) {
			JSONArray datas = (JSONArray) resp.get("data");

			//Log.d("xxx", datas.toString());

			String[] intervs = new String[datas.size()];
			int cont = 0;

			Iterator it = datas.iterator();
			while (it.hasNext()) {

			    Map data = (HashMap) it.next();

			    Map cliente = (HashMap) data.get("cliente");

			    String intervento = data.get("idintervento")
				    .toString();

			    long dataora = Long.parseLong(data.get("dataora")
				    .toString());
			    Timestamp date = new Timestamp(dataora);

			    String format = "dd-MM-yyyy HH:mm:ss";
			    DateFormat df = new SimpleDateFormat(format);

			    String dataoraStringa = df.format(date);

			    //Log.d("xxx", "Intervento: " + intervento);

			    intervs[cont] = "Intervento n. " + intervento
				    + "\n"
				    + cliente.get("nominativo").toString()
				    + "\ndel " + dataoraStringa;
			    cont++;
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MyInterventionsActivity.this,
				R.layout.table_cell, R.id.lbl_tableCell,
				intervs);

			table.setAdapter(adapter);

			dialog.dismiss();
		    }
		} catch (ParseException e) {
		    Log.d(debugTag, e.toString());
		} catch (Exception e) {
		    Log.d(debugTag, e.toString());
		}
	    }

	});
    }

    public void back(View v) {

	finish();
    }

    public void others(View v) {

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
