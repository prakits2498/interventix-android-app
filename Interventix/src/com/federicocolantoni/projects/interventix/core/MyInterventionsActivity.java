
package com.federicocolantoni.projects.interventix.core;

import java.io.Serializable;
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

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class MyInterventionsActivity extends Activity {

    private int mIdUser;

    private int mFirst, mMax;
    private JSONArray mDatas;

    private ListView mTable;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_my_interventions);

	SharedPreferences prefs = getSharedPreferences(
		Constants.GLOBAL_PREFERENCES, MODE_PRIVATE);
	mIdUser = prefs.getInt("iduser", Integer.valueOf(-1));

	mFirst = 0;
	mMax = 10;

	mTable = (ListView) findViewById(R.id.list_myInterv);

	findViewById(R.id.btn_others).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		int f = mFirst + 10;

		mFirst = f;

		//MyInterventionsActivity.this.request();
	    }
	});

	findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		finish();
	    }
	});

	//request();
    }

    private void request() {

	Map<Serializable, Comparable<?>> parameters = new HashMap<Serializable, Comparable<?>>();

	parameters.put("first", mFirst);
	parameters.put("max", mMax);

	JSONArray exclude = new JSONArray();
	exclude.add("firma");

	parameters.put(exclude, "exclude");

	String json_req = null;

	try {
	    json_req = JsonCR2.createRequest("interventions", "my", parameters,
		    mIdUser);
	} catch (NumberFormatException e) {
	    Log.d(Constants.DEBUG_TAG, "NUMBER_FORMAT_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(Constants.DEBUG_TAG, "GENERIC EXCEPTION!", e);
	}

	AndroidHttpClient request = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", json_req);

	mDialog = ProgressDialog.show(this, "Connessione",
		"Attendere prego...", true);
	mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

	request.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		try {
		    JSONObject resp = JsonCR2.read(httpResponse
			    .getBodyAsString());

		    if (resp.get("response").toString()
			    .equalsIgnoreCase("success")) {
			mDatas = (JSONArray) resp.get("data");

			String[] intervs = new String[mDatas.size()];
			int cont = 0;

			Iterator<?> it = mDatas.iterator();
			while (it.hasNext()) {

			    Map<?, ?> data = (Map<?, ?>) it.next();

			    Map<?, ?> cliente = (HashMap<?, ?>) data
				    .get("cliente");

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

			mTable.setAdapter(adapter);

			mDialog.dismiss();
		    }
		} catch (ParseException e) {
		    Log.d(Constants.DEBUG_TAG, "PARSE_EXCEPTION!", e);
		} catch (Exception e) {
		    Log.d(Constants.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
		}
	    }

	});
    }
}