package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListUsersAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;

@SuppressLint("NewApi")
public class AddUserToDetailFragment extends Fragment implements LoaderCallbacks<Cursor>, OnItemClickListener {
    
    private final static int MESSAGE_LOADER = 1;
    
    private final String[] PROJECTION = new String[] {
	    UtenteDB.Fields._ID, UtenteDB.Fields.TIPO, UtenteDB.Fields.ID_UTENTE, UtenteDB.Fields.NOME, UtenteDB.Fields.COGNOME
    };
    private final String SELECTION = UtenteDB.Fields.TYPE + "=?";
    private final String[] SELECTION_ARGS = new String[] {
	    UtenteDB.UTENTE_ITEM_TYPE
    };
    
    private final String ORDER_BY = UtenteDB.Fields.TIPO;
    
    private JSONArray mArrayTecnici;
    private List<Integer> mListTecnici;
    
    private ListUsersAdapter mUserAdapter;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
	
	mListTecnici = new ArrayList<Integer>();
	
	View view = inflater.inflate(R.layout.add_user_to_detail, container, false);
	
	// lista tecnici
	ListView listUsers = (ListView) view.findViewById(R.id.list_users);
	
	mUserAdapter = new ListUsersAdapter(getActivity(), null);
	
	listUsers.setAdapter(mUserAdapter);
	listUsers.setOnItemClickListener(this);
	
	getActivity().getSupportLoaderManager().initLoader(MESSAGE_LOADER, null, this);
	
	return view;
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	
	Cursor cur = (Cursor) adapter.getItemAtPosition(position);
	
	switch (view.getId()) {
	
	    case R.id.list_users:
		
		CheckedTextView tv_admins_row = (CheckedTextView) view.findViewById(R.id.users_nth);
		
		int user = Integer.valueOf((int) cur.getLong(cur.getColumnIndex(UtenteDB.Fields.ID_UTENTE)));
		
		if (tv_admins_row.isChecked() && !mListTecnici.contains(user)) {
		    mListTecnici.add(user);
		}
		else {
		    mListTecnici.remove(user);
		}
		
		break;
	    
	    default:
		break;
	}
    }
    
    @Override
    public void onDestroy() {
	
	super.onDestroy();
    }
    
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
	
	Loader<Cursor> loader = new CursorLoader(getActivity(), UtenteDB.CONTENT_URI, PROJECTION, SELECTION, SELECTION_ARGS, ORDER_BY);
	
	return loader;
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
	
	mUserAdapter.swapCursor(cursor);
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
	
	mUserAdapter.swapCursor(null);
    }
}
