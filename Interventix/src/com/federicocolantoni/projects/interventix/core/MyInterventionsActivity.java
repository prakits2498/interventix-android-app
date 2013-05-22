
package com.federicocolantoni.projects.interventix.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import com.federicocolantoni.projects.interventix.R;
import multiface.crypto.cr2.JsonCR2;
import org.json.simple.JSONArray;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MyInterventionsActivity
        extends Activity {

    private int mIdUser;
    private int mFirst, mMax;
    private JSONArray mDatas;
    private ListView mTable;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interventions);

        mFirst = 0;
        mMax = 10;

        mTable = (ListView) findViewById(R.id.list_myInterv);

        findViewById(R.id.btn_others).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int f = mFirst + 10;

                mFirst = f;

                //MyInterventionsActivity.this.request();
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //request();
    }

    private void request() {

        Map<Serializable, Comparable<?>> parameters = new HashMap<Serializable, Comparable<?>>();

        parameters.put("first", mFirst);
        parameters.put("max", mMax);

        JSONArray exclude = new JSONArray();
        exclude.add("firma");

        parameters.put(exclude, "exclude");

        String json_req = null;

        try {
            json_req = JsonCR2.createRequest("interventions", "my", parameters,
                                             mIdUser);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
