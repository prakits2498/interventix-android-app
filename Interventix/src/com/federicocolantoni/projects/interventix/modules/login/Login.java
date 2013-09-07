package com.federicocolantoni.projects.interventix.modules.login;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.R;

public class Login extends SherlockFragment implements OnClickListener {
    
    private Login.OnLoginListener mListener;
    
    @Override
    public void onAttach(Activity a) {
	
	super.onAttach(a);
	if (a instanceof Login.OnLoginListener) {
	    mListener = (Login.OnLoginListener) a;
	}
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			     Bundle savedInstanceState) {
	
	super.onCreateView(inflater, container, savedInstanceState);
	final View view = inflater.inflate(R.layout.login, container, false);
	
	view.findViewById(R.id.btn_login).setOnClickListener(this);
	
	return view;
    }
    
    public void OnLoginListener(Login.OnLoginListener listener) {
	
	mListener = listener;
    }
    
    @Override
    public void onClick(View v) {
	
	try {
	    mListener.onLogin();
	}
	catch (InterruptedException e) {
	    e.printStackTrace();
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }
    
    public static interface OnLoginListener {
	
	public void onLogin() throws InterruptedException, IOException;
    }
}
