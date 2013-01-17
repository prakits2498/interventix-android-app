
package com.federicocolantoni.projects.interventix;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

public class IndexActivity extends Activity {

    private String idUser;
    private final String debugTag = "xxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_index);

	Bundle extra = getIntent().getExtras();
	setIdUser(extra.getString("iduser"));

	Log.d(debugTag, "ID user: " + getIdUser());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_index, menu);
	return true;
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
