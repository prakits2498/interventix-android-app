package com.federicocolantoni.projects.interventix.fragments;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.R;

public class AddUserToDetailFragment extends SherlockFragment implements OnItemClickListener {
    
    private JSONObject arrayTecnici;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	super.onCreateView(inflater, container, savedInstanceState);
	
	arrayTecnici = new JSONObject();
	
	View view = inflater.inflate(R.layout.add_user_to_detail, container, false);
	
	// lista tecnici
	ListView listUsers = (ListView) view.findViewById(R.id.list_users);
	
	ListUsersAdapter userAdapter = new ListUsersAdapter(getSherlockActivity(), null, R.id.list_users, null, null);
	
	listUsers.setAdapter(userAdapter);
	listUsers.setOnItemClickListener(this);
	
	// lista amministratori
	ListView listAdmins = (ListView) view.findViewById(R.id.list_admins);
	
	ListAdminsAdapter adminAdapter = new ListAdminsAdapter(getSherlockActivity(), null, R.id.list_admins, null, null);
	
	listAdmins.setAdapter(adminAdapter);
	listAdmins.setOnItemClickListener(this);
	
	return view;
    }
    
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
	
	switch (view.getId()) {
	    case R.id.list_admins:
		
		CheckedTextView tv_users_row = (CheckedTextView) view.findViewById(R.id.users_nth);
		
		if (tv_users_row.isChecked()) {
		    
		}
		
		break;
	    
	    case R.id.list_users:
		
		CheckedTextView tv_admins_row = (CheckedTextView) view.findViewById(R.id.admins_nth);
		
		if (tv_admins_row.isChecked()) {
		    
		}
		
		break;
	    
	    default:
		break;
	}
    }
    
    private class ListUsersAdapter extends SimpleAdapter {
	
	public ListUsersAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
	    super(context, data, resource, from, to);
	}
    }
    
    private class ListAdminsAdapter extends SimpleAdapter {
	
	public ListAdminsAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
	    super(context, data, resource, from, to);
	}
    }
    
}
