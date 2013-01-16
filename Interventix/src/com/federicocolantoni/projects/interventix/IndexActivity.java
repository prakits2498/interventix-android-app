
package com.federicocolantoni.projects.interventix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.federicocolanoni.projects.interventix.R;

public class IndexActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_index, menu);
	return true;
    }
}
