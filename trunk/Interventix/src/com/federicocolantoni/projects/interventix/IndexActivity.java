
package com.federicocolantoni.projects.interventix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class IndexActivity extends Activity {

    private String idUser, nominativo;
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

    public void addInterv(View v) {

	Intent intent = new Intent(IndexActivity.this, TabBarActivity.class);

	startActivity(intent);
    }

    public void myInterv(View v) {

	Intent intent = new Intent(IndexActivity.this,
		MyInterventionsActivity.class);
	Bundle extra = new Bundle();
	extra.putString("iduser", idUser);

	intent.putExtras(extra);

	startActivity(intent);
    }

    public void back(View v) {

	finish();
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
