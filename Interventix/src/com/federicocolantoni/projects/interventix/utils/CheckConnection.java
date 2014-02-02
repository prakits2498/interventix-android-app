package com.federicocolantoni.projects.interventix.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.federicocolantoni.projects.interventix.Interventix_;

public class CheckConnection {

	static ConnectivityManager connMgr = (ConnectivityManager) Interventix_.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

	static NetworkInfo info = connMgr.getActiveNetworkInfo();

	public static boolean connectionIsAlive() {

		if (info != null)
			switch (info.getType()) {

				case ConnectivityManager.TYPE_WIFI:
				case ConnectivityManager.TYPE_MOBILE:
					if (info.isAvailable() && info.isConnected())
						return true;
			}

		return false;
	}
}