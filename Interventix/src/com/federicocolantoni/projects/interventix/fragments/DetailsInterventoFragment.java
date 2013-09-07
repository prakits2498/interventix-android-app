package com.federicocolantoni.projects.interventix.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.DettaglioInterventoAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;

public class DetailsInterventoFragment extends SherlockFragment
							       implements
							       LoaderCallbacks<Cursor> {
    
    private final static int MESSAGE_LOADER = 1;
    
    private long mId_intervento;
    
    private final String[] PROJECTION = new String[] {
	    DettaglioInterventoDB.Fields._ID,
	    DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO,
	    DettaglioInterventoDB.Fields.TIPO,
	    DettaglioInterventoDB.Fields.OGGETTO
    };
    
    private final String SELECTION = DettaglioInterventoDB.Fields.TYPE + " = ? AND " + DettaglioInterventoDB.Fields.INTERVENTO + " = ?";
    
    private String[] SELECTION_ARGS;
    
    private DettaglioInterventoAdapter mAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	
	BugSenseHandler.initAndStartSession(getSherlockActivity(), Constants.API_KEY);
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
	getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	final View view = inflater.inflate(R.layout.details_intervento_fragment, container, false);
	
	Bundle bundle = getArguments();
	
	mId_intervento = bundle.getLong(Constants.ID_INTERVENTO);
	
	SELECTION_ARGS = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		"" + mId_intervento
	};
	
	TextView tv_costs_intervento = (TextView) view.findViewById(R.id.tv_details_intervention);
	tv_costs_intervento.setText("Dettagli Intervento " + bundle.getLong(Constants.NUMERO_INTERVENTO));
	
	ListView detailsList = (ListView) view.findViewById(R.id.list_details_intervento);
	
	mAdapter = new DettaglioInterventoAdapter(getSherlockActivity(), null);
	
	detailsList.setAdapter(mAdapter);
	
	detailsList.setOnItemClickListener(new OnItemClickListener() {
	    
	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view,
				    int position, long id) {
		
		Bundle bundle = new Bundle();
		
		Cursor cur = (Cursor) adapter.getItemAtPosition(position);
		
		bundle.putLong(Constants.ID_DETTAGLIO_INTERVENTO, cur.getInt(cur.getColumnIndex(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO)));
		
		FragmentManager manager = getSherlockActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		DetailInterventoFragment dettInterv = new DetailInterventoFragment();
		dettInterv.setArguments(bundle);
		
		transaction.replace(R.id.fragments_layout, dettInterv, Constants.DETAILS_DETTAGLIO_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.INFORMATIONS_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		transaction.commit();
	    }
	});
	
	getSherlockActivity().getSupportLoaderManager().initLoader(MESSAGE_LOADER, null, this);
	
	return view;
    }
    
    // @Override
    // public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    //
    // getSherlockActivity().getSupportMenuInflater().inflate(
    // R.menu.details_interve_menu, menu);
    // }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
	
	Loader<Cursor> loader = new CursorLoader(getSherlockActivity(), DettaglioInterventoDB.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, null);
	
	return loader;
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	
	mAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
	
	mAdapter.swapCursor(null);
    }
}
