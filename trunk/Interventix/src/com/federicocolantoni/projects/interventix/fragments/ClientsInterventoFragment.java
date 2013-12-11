package com.federicocolantoni.projects.interventix.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.clienti_fragment)
public class ClientsInterventoFragment extends Fragment implements LoaderCallbacks<Cursor>, OnItemClickListener {
    
    private final static int MESSAGE_LOADER = 1;
    
    static final String[] PROJECTION = new String[] {
	    ClienteDB.Fields._ID, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.PARTITAIVA
    };
    
    static final String SELECTION = ClienteDB.Fields.TYPE + " =?";
    
    static final String[] SELECTION_ARGS = new String[] {
	    ClienteDB.CLIENTE_ITEM_TYPE
    };
    
    @ViewById(R.id.list_clients)
    ListView listClienti;
    
    @ViewById(R.id.inputSearch)
    EditText search_clients;
    
    private ListClientiAdapter mAdapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
	
	super.onStart();
	
	mAdapter = new ListClientiAdapter(getActivity(), null);
	
	listClienti.setAdapter(mAdapter);
	listClienti.setOnItemClickListener(this);
	
	getActivity().getSupportLoaderManager().initLoader(MESSAGE_LOADER, null, this);
	
	search_clients.addTextChangedListener(new TextWatcher() {
	    
	    @Override
	    public void onTextChanged(CharSequence sequence, int start, int before, int count) {
		
		mAdapter.getFilter().filter(sequence);
	    }
	    
	    @Override
	    public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
		
	    }
	    
	    @Override
	    public void afterTextChanged(Editable s) {
		
	    }
	});
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
	
	String sortOrder = ClienteDB.Fields.NOMINATIVO + " asc";
	
	Loader<Cursor> loader = new CursorLoader(getActivity(), ClienteDB.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, sortOrder);
	
	return loader;
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	
	mAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
	
	mAdapter.swapCursor(null);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
    }
}
