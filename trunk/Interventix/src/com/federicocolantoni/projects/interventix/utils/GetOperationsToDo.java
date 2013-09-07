package com.federicocolantoni.projects.interventix.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R.string;
import com.slezica.tools.async.ManagedAsyncTask;

@SuppressLint("NewApi")
public class GetOperationsToDo extends ManagedAsyncTask<Long, Void, Integer> {
    
    private final Context context;
    
    public GetOperationsToDo(FragmentActivity activity) {
	
	super(activity);
	context = activity.getApplicationContext();
    }
    
    @Override
    protected Integer doInBackground(Long... params) {
	
	final SharedPreferences prefsLocal = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
	
	int maxProgressRange = 0;
	
	Map<String, Object> parametersUsers = new HashMap<String, Object>();
	parametersUsers.put("revision", prefsLocal.getLong(Constants.REVISION_USERS, 0));
	
	maxProgressRange += usersSyncro(prefsLocal, maxProgressRange, parametersUsers, params);
	
	Map<String, Object> parametersClients = new HashMap<String, Object>();
	parametersClients.put("revision", prefsLocal.getLong(Constants.REVISION_CLIENTS, 0));
	
	maxProgressRange += clientsSyncro(prefsLocal, maxProgressRange, parametersClients, params);
	
	Map<String, Object> parametersInterv = new HashMap<String, Object>();
	parametersInterv.put("revision", prefsLocal.getLong(Constants.REVISION_INTERVENTIONS, 0));
	
	maxProgressRange += interventionsSyncro(prefsLocal, maxProgressRange, parametersInterv, params);
	
	return maxProgressRange;
    }
    
    @Override
    protected void onPostExecute(Integer result) {
	
    }
    
    private
	    JSONObject
	    connectionForURL(String json_req, final String url_string)
								      throws MalformedURLException,
								      IOException,
								      ProtocolException,
								      ParseException,
								      Exception,
								      UnsupportedEncodingException {
	
	URL url = new URL(url_string + "?DATA=" + json_req);
	
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
	conn.setRequestMethod("POST");
	conn.setDoInput(true);
	conn.setReadTimeout(Constants.CONNECTION_TIMEOUT);
	
	// System.out.println("URL REQUEST HttpURLConnection - "
	// + conn.getURL());
	
	conn.connect();
	
	if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	    
	    JSONObject json_resp = JsonCR2.read(readIt(conn.getInputStream(), conn.getContentLength()));
	    return json_resp;
	}
	else
	    return null;
    };
    
    private String readIt(InputStream stream, int len) throws IOException,
						      UnsupportedEncodingException {
	
	BufferedReader br = null;
	StringBuilder sb = new StringBuilder();
	
	String line;
	try {
	    
	    br = new BufferedReader(new InputStreamReader(stream));
	    while ((line = br.readLine()) != null) {
		sb.append(line);
	    }
	    
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
	finally {
	    if (br != null) {
		try {
		    br.close();
		}
		catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}
	
	return sb.toString();
    }
    
    private int
	    usersSyncro(final SharedPreferences prefsLocal,
			int maxProgressRange,
			Map<String, Object> parametersUsers, Long... params) {
	String json_req;
	try {
	    
	    json_req = JsonCR2.createRequest("users", "syncro", parametersUsers, params[0].intValue());
	    
	    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
	    
	    final String prefs_url = context.getResources().getString(string.prefs_key_url);
	    
	    final String url_string = prefsDefault.getString(prefs_url, null);
	    
	    JSONObject json_resp = connectionForURL(json_req, url_string);
	    
	    if (json_resp != null && json_resp.get("response").toString().equalsIgnoreCase("success")) {
		JSONObject data = (JSONObject) json_resp.get("data");
		
		// final Editor editor = prefsLocal.edit();
		//
		// editor.putLong(Constants.REVISION_USERS,
		// (Long) data.get("revision"));
		//
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		// {
		// editor.apply();
		// } else {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// editor.commit();
		// }
		// });
		// }
		
		JSONArray usersMOD = (JSONArray) data.get("mod");
		JSONArray usersDEL = (JSONArray) data.get("del");
		
		maxProgressRange += usersMOD.size();
		maxProgressRange += usersDEL.size();
	    }
	    
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	
	System.out.println("Users syncronized: " + maxProgressRange);
	
	return maxProgressRange;
    }
    
    private int clientsSyncro(SharedPreferences prefsLocal,
			      int maxProgressRange,
			      Map<String, Object> parametersClients,
			      Long[] params) {
	
	String json_req;
	try {
	    
	    json_req = JsonCR2.createRequest("clients", "syncro", parametersClients, params[0].intValue());
	    
	    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
	    
	    final String prefs_url = context.getResources().getString(string.prefs_key_url);
	    
	    final String url_string = prefsDefault.getString(prefs_url, null);
	    
	    JSONObject json_resp = connectionForURL(json_req, url_string);
	    
	    if (json_resp != null && json_resp.get("response").toString().equalsIgnoreCase("success")) {
		JSONObject data = (JSONObject) json_resp.get("data");
		
		// final Editor editor = prefsLocal.edit();
		//
		// editor.putLong(Constants.REVISION_CLIENTS,
		// (Long) data.get("revision"));
		//
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		// {
		// editor.apply();
		// } else {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// editor.commit();
		// }
		// });
		// }
		
		JSONArray clientsMOD = (JSONArray) data.get("mod");
		JSONArray clientsDEL = (JSONArray) data.get("del");
		
		maxProgressRange += clientsMOD.size();
		maxProgressRange += clientsDEL.size();
	    }
	    
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	
	System.out.println("Clients syncronized: " + maxProgressRange);
	
	return maxProgressRange;
    }
    
    private int interventionsSyncro(final SharedPreferences prefsLocal,
				    int maxProgressRange,
				    Map<String, Object> parametersInterv,
				    Long... params) {
	String json_req;
	long iduser = params[0];
	
	try {
	    json_req = JsonCR2.createRequest("interventions", "mysyncro", parametersInterv, (int) iduser);
	    
	    final SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(context);
	    
	    final String prefs_url = context.getResources().getString(string.prefs_key_url);
	    
	    final String url_string = prefsDefault.getString(prefs_url, null);
	    
	    JSONObject json_resp = connectionForURL(json_req, url_string);
	    
	    if (json_resp != null && json_resp.get("response").toString().equalsIgnoreCase("success")) {
		
		JSONObject data = (JSONObject) json_resp.get("data");
		
		// final Editor editor = prefsLocal.edit();
		//
		// editor.putLong(Constants.REVISION_INTERVENTIONS,
		// (Long) data.get("revision"));
		//
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		// {
		// editor.apply();
		// } else {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		//
		// editor.commit();
		// }
		// });
		// }
		
		JSONArray intervMOD = (JSONArray) data.get("mod");
		JSONArray intervDEL = (JSONArray) data.get("del");
		JSONArray interventions = (JSONArray) data.get("intervents");
		
		maxProgressRange += intervMOD.size();
		maxProgressRange += intervDEL.size();
		maxProgressRange += interventions.size();
		
	    }
	    
	}
	catch (Exception e) {
	    e.printStackTrace();
	    BugSenseHandler.sendException(e);
	}
	
	System.out.println("Interventions syncronized: " + maxProgressRange);
	
	return maxProgressRange;
    }
}
