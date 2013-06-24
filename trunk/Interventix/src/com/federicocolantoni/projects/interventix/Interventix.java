
package com.federicocolantoni.projects.interventix;

// import static org.acra.ReportField.ANDROID_VERSION;
// import static org.acra.ReportField.APP_VERSION_CODE;
// import static org.acra.ReportField.APP_VERSION_NAME;
// import static org.acra.ReportField.AVAILABLE_MEM_SIZE;
// import static org.acra.ReportField.BRAND;
// import static org.acra.ReportField.BUILD;
// import static org.acra.ReportField.CRASH_CONFIGURATION;
// import static org.acra.ReportField.CUSTOM_DATA;
// import static org.acra.ReportField.DEVICE_FEATURES;
// import static org.acra.ReportField.DEVICE_ID;
// import static org.acra.ReportField.DISPLAY;
// import static org.acra.ReportField.DROPBOX;
// import static org.acra.ReportField.DUMPSYS_MEMINFO;
// import static org.acra.ReportField.ENVIRONMENT;
// import static org.acra.ReportField.EVENTSLOG;
// import static org.acra.ReportField.FILE_PATH;
// import static org.acra.ReportField.INITIAL_CONFIGURATION;
// import static org.acra.ReportField.INSTALLATION_ID;
// import static org.acra.ReportField.LOGCAT;
// import static org.acra.ReportField.PACKAGE_NAME;
// import static org.acra.ReportField.PHONE_MODEL;
// import static org.acra.ReportField.PRODUCT;
// import static org.acra.ReportField.REPORT_ID;
// import static org.acra.ReportField.SETTINGS_SECURE;
// import static org.acra.ReportField.SETTINGS_SYSTEM;
// import static org.acra.ReportField.SHARED_PREFERENCES;
// import static org.acra.ReportField.STACK_TRACE;
// import static org.acra.ReportField.THREAD_DETAILS;
// import static org.acra.ReportField.TOTAL_MEM_SIZE;
// import static org.acra.ReportField.USER_CRASH_DATE;
//
// import org.acra.ACRA;
// import org.acra.ReportingInteractionMode;
// import org.acra.annotation.ReportsCrashes;

import android.annotation.SuppressLint;
import android.app.Application;

// @ReportsCrashes(formKey = "", formUri =
// "http://www.bugsense.com/api/acra?api_key=0ec355e8", /*reportType =
// org.acra.sender.HttpSender.Type.JSON,*/httpMethod =
// org.acra.sender.HttpSender.Method.POST, /*formUriBasicAuthLogin = "devilman",
// formUriBasicAuthPassword = "pippo100", mailTo =
// "chicco.colantoni@gmail.com",*/customReportContent = {
// REPORT_ID, USER_CRASH_DATE, ANDROID_VERSION, APP_VERSION_NAME,
// APP_VERSION_CODE, PHONE_MODEL, CRASH_CONFIGURATION, STACK_TRACE,
// AVAILABLE_MEM_SIZE, BRAND, BUILD, CUSTOM_DATA, DEVICE_FEATURES,
// DEVICE_ID, DISPLAY, DROPBOX, DUMPSYS_MEMINFO, ENVIRONMENT, EVENTSLOG,
// FILE_PATH, INITIAL_CONFIGURATION, INSTALLATION_ID, PACKAGE_NAME,
// PRODUCT, SETTINGS_SECURE, SETTINGS_SYSTEM, SHARED_PREFERENCES,
// THREAD_DETAILS, TOTAL_MEM_SIZE, LOGCAT }, mode =
// ReportingInteractionMode.TOAST, forceCloseDialogAfterToast = false,
// resToastText = R.string.crash_toast_report)
public class Interventix extends Application {

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {

	super.onCreate();
	//	ACRA.init(this);
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
