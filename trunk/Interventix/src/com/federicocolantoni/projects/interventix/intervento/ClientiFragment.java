
package com.federicocolantoni.projects.interventix.intervento;

import java.util.HashMap;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.ControlPanelActivity;
import com.federicocolantoni.projects.interventix.R;
import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class ClientiFragment extends Fragment {

    private static final String DEBUG_TAG = "INTERVENTIX";
    static final String GLOBAL_PREFERENCES = "Preferences";

    private ListView mListClients;
    private ClientAdapter adapter;
    private String json_req;

    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.clienti_fragment, container,
		false);

	prefs = getActivity().getSharedPreferences(GLOBAL_PREFERENCES,
		Context.MODE_PRIVATE);

	try {

	    Map<String, String> param = new HashMap<String, String>();
	    param.put("revision", prefs.getString("REVISION", "-1"));

	    json_req = JsonCR2.createRequest("clients", "syncro", param,
		    prefs.getInt("ID_USER", Integer.valueOf(-1)));
	} catch (NumberFormatException e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " NUMBER_FORMAT_EXCEPTION! ", e);
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, ControlPanelActivity.class.getSimpleName()
		    + " GENERIC_EXCEPTION!", e);
	}

	AndroidHttpClient request = new AndroidHttpClient(
		"http://176.31.243.123:8080/interventix/connector");
	request.setMaxRetries(5);
	ParameterMap paramMap = new ParameterMap();
	paramMap.add("DATA", json_req);

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

	    if (resp.get("response").toString().equalsIgnoreCase("success")) {
		Toast.makeText(getActivity(), "Request Syncro OK",
			Toast.LENGTH_SHORT).show();
	    }
	} catch (ParseException e) {
	    Log.d(DEBUG_TAG, ClientiFragment.class.getSimpleName()
		    + " PARSE_EXCEPTION!", e);
	} catch (Exception e) {
	    Log.d(DEBUG_TAG, ClientiFragment.class.getSimpleName()
		    + " GENERIC_EXCEPTION!", e);
	}
    }
}
