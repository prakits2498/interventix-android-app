package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.task.GetClientiAsyncTask;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@SuppressLint({
	"InlinedApi", "NewApi"
})
@EFragment(R.layout.clienti_fragment)
@SuppressWarnings("unchecked")
public class ClientsInterventoFragment extends Fragment implements OnItemLongClickListener, OnItemClickListener {
    
    @ViewById(R.id.list_clients)
    ListView listClienti;
    
    public static long sId_Intervento;
    
    private SharedPreferences prefs;
    
    private ListClientiAdapter mAdapter;
    
    Handler handler = new MyHandler() {
	
	public void handleMessage(Message msg) {
	    
	    switch (msg.what) {
	    
		case Constants.WHAT_MESSAGE_GET_CLIENTI:
		    
		    mAdapter = new ListClientiAdapter(getActivity(), R.layout.client_row, R.id.tv_row_nominativo, (ArrayList<Cliente>) msg.obj);
		    
		    listClienti.setAdapter(mAdapter);
		    
		    break;
	    }
	};
    };
    
    private static class MyHandler extends Handler {
	
    }
    
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	((ActionBarActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
	((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
	setHasOptionsMenu(true);
    };
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	
	super.onActivityCreated(savedInstanceState);
	
	Bundle bundle = getArguments();
	
	sId_Intervento = bundle.getLong(Constants.ID_INTERVENTO);
    }
    
    @Override
    public void onStart() {
	
	super.onStart();
	
	new GetClientiAsyncTask(getActivity(), handler).execute();
	
	listClienti.setOnItemLongClickListener(this);
	listClienti.setOnItemClickListener(this);
    }
    
    @Override
    public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
	
	return true;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	
	inflater.inflate(R.menu.menu_clients, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	
	switch (item.getItemId()) {
	    case R.id.menu_save_client:
		
		AsyncQueryHandler saveCliente = new AsyncQueryHandler(getActivity().getContentResolver()) {
		    
		    @Override
		    protected void onUpdateComplete(int token, Object cookie, int result) {
			
			InterventixToast.makeToast(getActivity(), "Cliente modificato", Toast.LENGTH_SHORT);
			
			prefs = getActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
			
			final Editor edit = prefs.edit();
			
			edit.putBoolean(Constants.INTERV_MODIFIED, true);
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			    edit.apply();
			}
			else {
			    new Thread(new Runnable() {
				
				@Override
				public void run() {
				    edit.commit();
				}
			    }).start();
			}
		    }
		};
		
		int cliente_checked = 0;
		
		SparseBooleanArray sparseArray = mAdapter.getBooleanArray();
		
		for (int i = 0; i < sparseArray.size(); i++) {
		    
		    if (sparseArray.get(i))
			cliente_checked = i;
		}
		
		Cliente clChecked = mAdapter.getItem(cliente_checked);
		
		System.out.println("Cliente selezionato: " + clChecked.getNominativo());
		
		ContentValues values = new ContentValues();
		values.put(InterventoDB.Fields.CLIENTE, clChecked.getIdCliente());
		values.put(InterventoDB.Fields.MODIFICATO, "M");
		
		String selection = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?";
		
		String[] selectionArgs = new String[] {
			InterventoDB.INTERVENTO_ITEM_TYPE, "" + sId_Intervento
		};
		
		saveCliente.startUpdate(0, null, ClienteDB.CONTENT_URI, values, selection, selectionArgs);
		
		break;
	}
	
	return getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	
	mAdapter.toggleChecked(position);
    }
}
