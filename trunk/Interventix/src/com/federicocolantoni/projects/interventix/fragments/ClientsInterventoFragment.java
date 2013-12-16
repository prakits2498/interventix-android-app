package com.federicocolantoni.projects.interventix.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.ListClientiAdapter;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.task.GetClientiAsyncTask;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.clienti_fragment)
@SuppressWarnings("unchecked")
public class ClientsInterventoFragment extends Fragment implements OnItemClickListener {
    
    @ViewById(R.id.list_clients)
    ListView listClienti;
    
    private ListClientiAdapter mAdapter;
    
    Handler handler = new MyHandler() {
	
	public void handleMessage(Message msg) {
	    
	    switch (msg.what) {
	    
		case Constants.WHAT_MESSAGE_GET_CLIENTI:
		    
		    mAdapter = new ListClientiAdapter(getActivity(), R.layout.client_row, R.id.tv_row_nominativo, (ArrayList<Cliente>) msg.obj);
		    
		    listClienti.setAdapter(mAdapter);
		    listClienti.setOnItemClickListener(ClientsInterventoFragment.this);
		    
		    break;
	    }
	};
    };
    
    private static class MyHandler extends Handler {
	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
	
	super.onStart();
	
	new GetClientiAsyncTask(getActivity(), handler).execute();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	
    }
}
