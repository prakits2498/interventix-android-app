
package com.federicocolantoni.projects.interventix.intervento;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.federicocolantoni.projects.interventix.R;

public class InterventoFragment extends Fragment implements OnClickListener {

    private OnSaveInterventoListener mListener;

    public static interface OnSaveInterventoListener {

	public void onSaveListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.intervento_fragment,
		container, false);

	v.findViewById(R.id.btn_save_interv).setOnClickListener(this);

	v.findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		getActivity().finish();
	    }
	});

	return v;
    }

    @Override
    public void onAttach(Activity a) {

	super.onAttach(a);
	if (a instanceof OnSaveInterventoListener) {
	    mListener = (OnSaveInterventoListener) a;
	}
    }

    public void OnSaveInterventListener(OnSaveInterventoListener listener) {

	mListener = listener;
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	    case R.id.btn_save_interv:
		mListener.onSaveListener();
		break;
	}
    }
}
