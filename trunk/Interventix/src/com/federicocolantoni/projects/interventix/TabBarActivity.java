
package com.federicocolantoni.projects.interventix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class TabBarActivity extends Activity {

    private static final String DEBUG_TAG = "INTERVENTIX";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_tab_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_tab_bar, menu);
	return true;
    }

}
