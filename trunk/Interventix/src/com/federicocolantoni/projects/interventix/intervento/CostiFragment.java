
package com.federicocolantoni.projects.interventix.intervento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.R;

public class CostiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.costi_fragment, container,
		false);

	Toast.makeText(getActivity(), "Fragment Costi intervento",
		Toast.LENGTH_SHORT).show();

	return v;
    }
}
