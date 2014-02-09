package com.federicocolantoni.projects.interventix.service;

import android.app.IntentService;
import android.content.Intent;

import com.federicocolantoni.projects.interventix.Constants;

public class InterventixService extends IntentService {
    
    public InterventixService() {
    
	super("InterventixService");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
    
	if (intent.getAction().equals(Constants.ACTION_GET_INTERVENTI)) {
	    
	    System.out.println("Action " + Constants.ACTION_GET_INTERVENTI);
	    
	    inviaInterventi();
	}
	else
	    if (intent.getAction().equals(Constants.ACTION_GET_CLIENTI)) {
		
		System.out.println("Action " + Constants.ACTION_GET_CLIENTI);
		
		inviaClienti();
	    }
    }
    
    private void inviaClienti() {
    
    }
    
    private void inviaInterventi() {
    
    }
}
