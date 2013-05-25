
package com.federicocolantoni.projects.interventix.core;

import android.os.Bundle;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.federicocolantoni.projects.interventix.BaseActivity;
import com.federicocolantoni.projects.interventix.R;

public class ControlPanelActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	setContentView(R.layout.activity_home);

	setNominativo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	super.onCreateOptionsMenu(menu);

	final MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.activity_index, menu);

	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	if (item.getItemId() == android.R.id.home) {
	    this.finish();
	    return true;
	}

	return super.onOptionsItemSelected(item);
    }

    private void setNominativo() {

    }
}
