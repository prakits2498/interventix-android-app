package com.federicocolantoni.projects.interventix.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class InterventixAuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {

	InterventixAuthenticator authenticator = new InterventixAuthenticator(this);
	return authenticator.getIBinder();
    }
}
