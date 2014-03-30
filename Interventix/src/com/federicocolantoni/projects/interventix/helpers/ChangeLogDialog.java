package com.federicocolantoni.projects.interventix.helpers;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;

public class ChangeLogDialog {

    static final private String TAG = "ChangeLogDialog";

    static final private String TITLE_CHANGELOG = "title_changelog";
    static final private String CHANGELOG_XML = "changelog";

    private Activity fActivity;

    public ChangeLogDialog(Activity context) {

	fActivity = context;
    }

    private String GetAppVersion() {

	try {
	    PackageInfo _info = fActivity.getPackageManager().getPackageInfo(fActivity.getPackageName(), 0);
	    return _info.versionName;
	}
	catch (NameNotFoundException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	    return "";
	}
    }

    private String ParseReleaseTag(XmlResourceParser aXml) throws XmlPullParserException, IOException {

	String _Result = "<h1>Release: " + aXml.getAttributeValue(null, "version") + "</h1><ul>";
	int eventType = aXml.getEventType();
	while (eventType != XmlPullParser.END_TAG || aXml.getName().equals("change")) {
	    if (eventType == XmlPullParser.START_TAG && aXml.getName().equals("change")) {
		eventType = aXml.next();
		_Result = _Result + "<li>" + aXml.getText() + "</li>";
	    }
	    eventType = aXml.next();
	}
	_Result = _Result + "</ul>";
	return _Result;
    }

    private String GetStyle() {

	return "<style type=\"text/css\">" + "h1 { margin-left: 0px; font-size: 12pt; }" + "li { margin-left: 0px; font-size: 9pt;}" + "ul { padding-left: 30px;}" + "</style>";
    }

    private String GetHTMLChangelog(int aResourceId, Resources aResource) {

	String _Result = "<html><head>" + GetStyle() + "</head><body>";
	XmlResourceParser _xml = aResource.getXml(aResourceId);
	try {
	    int eventType = _xml.getEventType();
	    while (eventType != XmlPullParser.END_DOCUMENT) {
		if (eventType == XmlPullParser.START_TAG && _xml.getName().equals("release")) {
		    _Result = _Result + ParseReleaseTag(_xml);

		}
		eventType = _xml.next();
	    }
	}
	catch (XmlPullParserException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);

	}
	finally {
	    _xml.close();
	}
	_Result = _Result + "</body></html>";
	return _Result;
    }

    public void show() {

	String _PackageName = fActivity.getPackageName();
	Resources _Resource;
	try {
	    _Resource = fActivity.getPackageManager().getResourcesForApplication(_PackageName);
	}
	catch (NameNotFoundException e) {

	    BugSenseHandler.sendException(e);
	    e.printStackTrace();
	    return;
	}

	int _resID = _Resource.getIdentifier(TITLE_CHANGELOG, "string", _PackageName);
	String _Title = _Resource.getString(_resID);
	_Title = _Title + " v" + GetAppVersion();

	_resID = _Resource.getIdentifier(CHANGELOG_XML, "xml", _PackageName);

	String _HTML = GetHTMLChangelog(_resID, _Resource);

	String _Close = _Resource.getString(R.string.changelog_close);

	if (_HTML.equals("") == true) {

	    Toast.makeText(fActivity, "Could not load change log", Toast.LENGTH_SHORT).show();
	    return;
	}

	WebView _WebView = new WebView(fActivity);
	_WebView.loadData(_HTML, "text/html", "utf-8");

	AlertDialog.Builder builder = new Builder(fActivity);

	builder.setIcon(R.drawable.ic_launcher);
	builder.setCancelable(true);

	builder.setTitle(_Title);
	builder.setView(_WebView).setPositiveButton(_Close, new Dialog.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialogInterface, int i) {

		dialogInterface.dismiss();
	    }
	});

	builder.create().show();
    }
}
