
package com.federicocolantoni.projects.interventix;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class MyInterventionsActivity extends Activity {

    private static final String DEBUG_TAG = "INTERVENTIX";
    static final String GLOBAL_PREFERENCES = "Preferences";

    private int idUser;

    private int first, max;
    private JSONArray datas;

    private ListView table;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_my_interventions);

	SharedPreferences prefs = getSharedPreferences(GLOBAL_PREFERENCES,
		MODE_PRIVATE);
	idUser = prefs.getInt("iduser", Integer.valueOf(-1));

	first = 0;
	max = 10;

	table = (ListView) findViewById(R.id.list_myInterv);

	findViewById(R.id.btn_others).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		int f = first + 10;

		first = f;

		MyInterventionsActivity.this.request();
	    }
	});

	findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		finish();
	    }
	});

	request();
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
		    idUser);
	} catch (NumberFormatException e) {
	    Log.d(DEBUG_TAG, e.toString());
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, e.toString());
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
		    JSONObject resp = JsonCR2.read(httpResponse
			    .getBodyAsString());

		    if (resp.get("response").toString()
			    .equalsIgnoreCase("success")) {
			JSONArray datas = (JSONArray) resp.get("data");

			String[] intervs = new String[datas.size()];
			int cont = 0;

			Iterator it = datas.iterator();
			while (it.hasNext()) {

			    Map data = (Map) it.next();

			    Map cliente = (HashMap) data.get("cliente");

			    String intervento = data.get("idintervento")
				    .toString();

			    long dataora = Long.parseLong(data.get("dataora")
				    .toString());
			    Timestamp date = new Timestamp(dataora);

			    String format = "dd-MM-yyyy HH:mm:ss";
			    DateFormat df = new SimpleDateFormat(format,
				    Locale.ITALY);

			    String dataOraStringa = df.format(date);

			    intervs[cont] = "Intervento n. " + intervento
				    + "\n"
				    + cliente.get("nominativo").toString()
				    + "\ndel " + dataOraStringa;
			    cont++;
			}

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				MyInterventionsActivity.this,
				R.layout.interv_row, R.id.lbl_tableCell,
				intervs);

			table.setAdapter(adapter);

			dialog.dismiss();
		    }
		} catch (ParseException e) {
		    Log.d(DEBUG_TAG,
			    MyInterventionsActivity.class.getSimpleName()
				    + " PARSE_EXCEPTION! " + e.toString());
		} catch (Exception e) {
		    Log.d(DEBUG_TAG,
			    MyInterventionsActivity.class.getSimpleName()
				    + " GENERIC_EXCEPTION! " + e.toString());
		}
	    }

	});
    }
}
