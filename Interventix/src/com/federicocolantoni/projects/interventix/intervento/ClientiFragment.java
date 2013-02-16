
package com.federicocolantoni.projects.interventix.intervento;

import java.util.HashMap;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.MainActivity;
import com.federicocolantoni.projects.interventix.R;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

@SuppressLint("NewApi")
public class ClientiFragment extends Fragment {

    static final String GLOBAL_PREFERENCES = "Preferences";

    private SharedPreferences mPrefs;
    private ListView mListClients;

    private String mJson_req;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.clienti_fragment, container,
		false);

	mPrefs = getActivity().getSharedPreferences(GLOBAL_PREFERENCES,
		Context.MODE_PRIVATE);

	mListClients = (ListView) v.findViewById(R.id.list_clients);

	v.findViewById(R.id.btn_reload_clients).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(View v) {

		    }
		});

	try {

	    Map<String, Long> param = new HashMap<String, Long>();
	    param.put("revision", mPrefs.getLong("REVISION", 0));

	    mJson_req = JsonCR2.createRequest("clients", "syncro", param,
		    mPrefs.getInt("ID_USER", Integer.valueOf(-1)));
	} catch (NumberFormatException e) {
	    Log.d(MainActivity.DEBUG_TAG, "NUMBER_FORMAT_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}

	AndroidHttpClient request = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", mJson_req);

	request.post("", paramMap, new AsyncCallback() {

	    @Override
	    public void onComplete(HttpResponse httpResponse) {

		requestComplete(httpResponse);
	    }

	});

	return v;
    }

    private void requestComplete(HttpResponse httpResponse) {

	try {
	    JSONObject resp = JsonCR2.read(httpResponse.getBodyAsString());

	    Map req = (Map) resp.get("request");

	    String action = req.get("action").toString();
	    String section = req.get("section").toString();

	    if (resp.get("response").toString().equalsIgnoreCase("success")) {
		if (action.equalsIgnoreCase("syncro")
			&& section.equalsIgnoreCase("clients")) {

		    Map data = (Map) resp.get("data");

		    long remoteRev = (Long) data.get("revision");

		    if (remoteRev > mPrefs.getLong("REVISION", 0)) {
			final Editor editor = mPrefs.edit();

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

			//JSONArray clients = (JSONArray) data.get("del");
		    }

		    Toast.makeText(getActivity(),
			    "Request Syncro OK\nRevisione: " + remoteRev,
			    Toast.LENGTH_SHORT).show();
		}
	    }
	} catch (ParseException e) {
	    Log.d(MainActivity.DEBUG_TAG, "PARSE_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(MainActivity.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
	}
    }
}
