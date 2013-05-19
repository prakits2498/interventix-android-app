
package com.federicocolantoni.projects.interventix.core;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.modules.login.Login.OnLoginListener;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.android.AndroidHttpClient;
import multiface.crypto.cr2.JsonCR2;

import java.io.File;
import java.io.IOException;

public class MainActivity
        extends FragmentActivity
        implements OnLoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    onLogin();
                }
                catch (InterruptedException e) {
                    Log.d(Constants.DEBUG_TAG, "INTERRUPTED_EXCEPTION!", e);
                }
                catch (IOException e) {
                    Log.d(Constants.DEBUG_TAG, "IO_EXCEPTION!", e);
                }
            }
        });
    }

    @Override
    public void onLogin() throws
                          InterruptedException,
                          IOException {

        EditText mUsername = (EditText) findViewById(R.id.field_username);
        EditText mPassword = (EditText) findViewById(R.id.field_password);

        String json_req = new String();

        try {
            json_req = JsonCR2.createRequestLogin(mUsername.getText()
                                                          .toString(), mPassword.getText().toString());

            new com.federicocolantoni.projects.interventix.core.MainActivity.GetLogin(this.getApplicationContext())
                    .execute(json_req);
        }
        catch (Exception e) {
            Log.d(Constants.DEBUG_TAG, "GENERIC_EXCEPTION!", e);
        }
    }

    private class GetLogin
            extends android.os.AsyncTask<String, Void, Void> {

        private android.app.ProgressDialog progressBar;
        private android.content.Context context;
        private File store_json;

        public GetLogin(android.content.Context context) {
            this.context = context;
            progressBar = android.app.ProgressDialog
                    .show(this.context, "Connessione in corso", "Attendere prego...", true, false);
        }

        @Override
        protected void onPreExecute() {
            progressBar.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            //TODO inserire il codice per la connessione a internet

            final AndroidHttpClient request = new AndroidHttpClient(
                    Constants.BASE_URL_LOCAL);
            request.setMaxRetries(5);
            com.turbomanage.httpclient.ParameterMap paramMap = new com.turbomanage.httpclient.ParameterMap();
            paramMap.add("DATA", strings[0]);

            HttpResponse response;
            response = request.post("", paramMap);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
