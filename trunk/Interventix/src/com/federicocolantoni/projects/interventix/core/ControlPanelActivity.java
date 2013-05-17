
package com.federicocolantoni.projects.interventix.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.services.InterventixIntentService;
import multiface.crypto.cr2.JsonCR2;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("NewApi")
public class ControlPanelActivity extends Activity {

    private int mIdUser;
    private String mJson_req;

    private TextView mLabel_nom;

    private MyHandler mHandler = new MyHandler() {

        @Override
        public void handleMessage(Message msg) {

            String action = (String) msg.obj;

            if (action.equals(Constants.GET_NOMINATIVO)) {
                if (msg.arg1 == RESULT_OK) {
                    Bundle bundle = msg.getData();

                    mLabel_nom.setText(bundle.getString(Constants.NOMINATIVO));
                } else {
                    Toast.makeText(
                            ControlPanelActivity.this,
                            "Impossibile recuperare le informazioni sull'utente",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private static class MyHandler extends Handler {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control_panel);

        setNominativo();

        findViewById(R.id.btn_addInterv).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ControlPanelActivity.this,
                                TabBarActivity.class);

                        startActivity(intent);
                    }
                });

        findViewById(R.id.btn_exit).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void setNominativo() {

        mLabel_nom = (TextView) findViewById(R.id.label_nominativo);

        SharedPreferences prefs = getSharedPreferences(
                Constants.GLOBAL_PREFERENCES, MODE_PRIVATE);
        mIdUser = prefs.getInt("ID_USER", Integer.valueOf(-1));

        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("idutente", mIdUser);

        try {
            mJson_req = JsonCR2.createRequest("users", "get", parameters,
                    mIdUser);
        } catch (NumberFormatException e) {
            Log.d(Constants.DEBUG_TAG,
                    ControlPanelActivity.class.getSimpleName()
                            + " NUMBER_FORMAT_EXCEPTION! ", e);
        } catch (Exception e) {
            Log.d(Constants.DEBUG_TAG,
                    ControlPanelActivity.class.getSimpleName()
                            + " GENERIC_EXCEPTION!", e);
        }

        Intent intent = new Intent(Constants.GET_NOMINATIVO, null, this,
                InterventixIntentService.class);

        Messenger msn = new Messenger(mHandler);

        intent.putExtra(Constants.REQUEST_GET_NOMINATIVO, mJson_req);
        intent.putExtra(Constants.MESSENGER, msn);

        startService(intent);
    }
}
