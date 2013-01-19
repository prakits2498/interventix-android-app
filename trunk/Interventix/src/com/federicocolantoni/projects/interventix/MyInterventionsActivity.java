
package com.federicocolantoni.projects.interventix;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MyInterventionsActivity extends Activity {

    private String idUser;
    private final String debugTag = "xxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_my_interventions);

	Bundle extra = getIntent().getExtras();
	setIdUser(extra.getString("iduser"));

	Log.d(debugTag, "ID user: " + getIdUser());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_my_interventions, menu);
	return true;
    }

    public void back(View v) {

	finish();
    }

    public void others(View v) {

    }

    /**
     * @return the idUser
     */
    public String getIdUser() {

	return idUser;
    }

    /**
     * @param idUser
     *            the idUser to set
     */
    public void setIdUser(String idUser) {

	this.idUser = idUser;
    }

}
