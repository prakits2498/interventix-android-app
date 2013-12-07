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

import multiface.crypto.cr2.JsonCR2;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;

public class Utils {
    
    public static JSONObject connectionForURL(String json_req, final String url_string) throws MalformedURLException, IOException, ProtocolException, ParseException, Exception, UnsupportedEncodingException {
	
	URL url = new URL(url_string + "?DATA=" + json_req);
	
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
	conn.setRequestMethod("POST");
	conn.setDoInput(true);
	conn.setReadTimeout(Constants.CONNECTION_TIMEOUT);
	
	conn.connect();
	
	if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	    
	    JSONObject json_resp = JsonCR2.read(readIt(conn.getInputStream(), conn.getContentLength()));
	    return json_resp;
	}
	else
	    return null;
    };
    
    private static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	
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
	    
	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	}
	finally {
	    if (br != null) {
		try {
		    br.close();
		}
		catch (IOException e) {
		    
		    BugSenseHandler.sendException(e);
		    e.printStackTrace();
		}
	    }
	}
	
	return sb.toString();
    }
    
    public static byte[] stringToByteArray(String convertToByteArray) {
	
	byte[] newArray = new byte[convertToByteArray.length()];
	
	for (int i = 0; i < convertToByteArray.length(); i++) {
	    
	    char character = convertToByteArray.charAt(i);
	    
	    newArray[i] = (byte) character;
	}
	
	return newArray;
    }
}
