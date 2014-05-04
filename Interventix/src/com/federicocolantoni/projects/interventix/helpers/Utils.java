package com.federicocolantoni.projects.interventix.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import multiface.crypto.cr2.JsonCR2;

import org.apache.http.client.methods.HttpPost;
import org.json.simple.JSONObject;

import android.annotation.SuppressLint;

import com.bugsense.trace.BugSenseHandler;

@SuppressLint("DefaultLocale")
public class Utils {

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static JSONObject connectionForURL(String json_req, final String url_string) throws Exception {

	String formattedURL = url_string + "?DATA=" + json_req;

	URL url = new URL(formattedURL);

	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
	conn.setRequestMethod(HttpPost.METHOD_NAME);

	// set user-agent
	// conn.setRequestProperty("User-Agent", "...");

	conn.setRequestProperty("Accept-Charset", "UTF-8");
	conn.setRequestProperty("Content-Type", "text/plain");
	conn.setRequestProperty("Content-Length", Integer.toString(formattedURL.length()));
	conn.setDefaultUseCaches(true);
	conn.setDoOutput(true);
	conn.setReadTimeout(Constants.CONNECTION_TIMEOUT);

	conn.connect();

	if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

	    JSONObject json_resp = JsonCR2.read(readIt(conn.getInputStream()));

	    conn.disconnect();

	    return json_resp;
	}
	else {

	    System.out.println(conn.getHeaderFields());
	    System.out.println("Error: " + conn.getResponseCode() + " - " + conn.getResponseMessage() + "\n" + readIt(conn.getErrorStream()));

	    conn.disconnect();

	    return null;
	}
    }

    private static String readIt(InputStream stream) throws IOException {

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

    public static byte[] hexToBytes(char[] hex) {

	int length = hex.length / 2;

	byte[] raw = new byte[length];

	for (int i = 0; i < length; i++) {
	    int high = Character.digit(hex[i * 2], 16);
	    int low = Character.digit(hex[i * 2 + 1], 16);
	    int value = high << 4 | low;
	    if (value > 127)
		value -= 256;

	    raw[i] = (byte) value;
	}

	return raw;
    }

    public static String bytesToHex(byte[] bytes) {

	char[] hexChars = new char[bytes.length * 2];

	int v;

	for (int j = 0; j < bytes.length; j++) {
	    v = bytes[j] & 0xFF;
	    hexChars[j * 2] = HEX_ARRAY[v >> 4];
	    hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	}

	return new String(hexChars).toLowerCase();
    }
}
