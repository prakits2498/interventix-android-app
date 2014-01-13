package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;

@EFragment(R.layout.client_detail_fragment)
public class ClientInterventoFragment extends Fragment {
    
    private static long sId_Cliente_Intervento, sId_Intervento;
    
    private Cliente cliente;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
	super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
    
	super.onStart();
    }
}
