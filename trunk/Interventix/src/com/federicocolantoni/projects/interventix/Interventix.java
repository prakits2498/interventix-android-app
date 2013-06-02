
package com.federicocolantoni.projects.interventix;

import static org.acra.ReportField.ANDROID_VERSION;
import static org.acra.ReportField.APP_VERSION_CODE;
import static org.acra.ReportField.APP_VERSION_NAME;
import static org.acra.ReportField.PHONE_MODEL;
import static org.acra.ReportField.STACK_TRACE;
import static org.acra.ReportField.THREAD_DETAILS;
import static org.acra.ReportField.USER_CRASH_DATE;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.annotation.SuppressLint;
import android.app.Application;

@ReportsCrashes(formKey = "", /*formUri = "https://192.168.1.197:5984/acra-interventix/_design/acra-storage/_update/report", reportType = org.acra.sender.HttpSender.Type.JSON, httpMethod = org.acra.sender.HttpSender.Method.PUT, formUriBasicAuthLogin = "federico", formUriBasicAuthPassword = "pippo100",*/customReportContent = {
	USER_CRASH_DATE, APP_VERSION_CODE, APP_VERSION_NAME, ANDROID_VERSION,
	PHONE_MODEL, THREAD_DETAILS, STACK_TRACE }, /*mailTo = "chicco.colantoni@gmail.com",*/mode = ReportingInteractionMode.TOAST, forceCloseDialogAfterToast = false, resToastText = R.string.crash_toast_report)
public class Interventix extends Application {

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {

	super.onCreate();
	ACRA.init(this);
	loadAsyncTask();
    }

    private void loadAsyncTask() {

	try {
	    Class.forName("android.os.AsyncTask");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
    }
}
