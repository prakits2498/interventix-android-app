
package com.federicocolantoni.projects.interventix.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.federicocolantoni.projects.interventix.R;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("NewApi")
public class ControlPanelActivity
        extends Activity {

    private int mIdUser;
    private String mJson_req;
    private TextView mLabel_nom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setNominativo();

        findViewById(R.id.btn_addInterv).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

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

        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("idutente", mIdUser);

//        try {
//            mJson_req = JsonCR2.createRequest("users", "get", parameters,
//                                              mIdUser);
//        }
//        catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
