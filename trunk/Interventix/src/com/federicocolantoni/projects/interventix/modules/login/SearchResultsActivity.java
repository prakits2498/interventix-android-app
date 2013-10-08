package com.federicocolantoni.projects.interventix.modules.login;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListActivity;

public class SearchResultsActivity extends SherlockListActivity {
    
    protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	handleIntent(getIntent());
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
	
	super.onNewIntent(intent);
	handleIntent(intent);
    }
    
    private void handleIntent(Intent intent) {
	
	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    String query = intent.getStringExtra(SearchManager.QUERY);
	    search(query);
	}
    }
    
    private void search(String query) {
	
    }
}
