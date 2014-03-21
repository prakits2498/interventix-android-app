package com.federicocolantoni.projects.interventix.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.federicocolantoni.projects.interventix.application.Interventix_;

public class CheckConnection {

    static ConnectivityManager connMgr = (ConnectivityManager) Interventix_.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

    public static boolean connectionIsAlive() {

	NetworkInfo info = connMgr.getActiveNetworkInfo();

	if (info != null)
	    switch (info.getType()) {

		case ConnectivityManager.TYPE_WIFI:
		case ConnectivityManager.TYPE_MOBILE:
		    if (info.isAvailable() && (info.isConnectedOrConnecting() || info.isConnected()))
			return true;
	    }

	return false;
    }
}
