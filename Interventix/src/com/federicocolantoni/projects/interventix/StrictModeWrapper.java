package com.federicocolantoni.projects.interventix;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

@SuppressLint("NewApi")
public class StrictModeWrapper {

    public static void init(Context context) {

	if (BuildConfig.DEBUG) {
	    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {

		// *** Strict Mode - Start ***///

		StrictMode
			.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites()
				.detectNetwork().penaltyLog().penaltyDeath()
				.build());

		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects().detectActivityLeaks()
			.detectLeakedClosableObjects().penaltyLog()
			.penaltyDeath().build());

		// *** Strict Mode - End ***///
	    }
	}
    }
}
