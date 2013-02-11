
package com.federicocolantoni.projects.interventix;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class MenuBarFragment extends Fragment implements OnClickListener {

    private OnMenuBarListener mListener;

    public static interface OnMenuBarListener {

	public void onTabInterventoClick();

	public void onTabClientiClick();

	public void onTabCostiClick();

	public void onTabDettagliClick();

	public void onTabFirmaClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);
	final View v = inflater.inflate(R.layout.menu_bar_fragment, container);

	v.findViewById(R.id.btn_interv).setOnClickListener(this);
	v.findViewById(R.id.btn_clients).setOnClickListener(this);
	v.findViewById(R.id.btn_costs).setOnClickListener(this);
	v.findViewById(R.id.btn_details).setOnClickListener(this);
	v.findViewById(R.id.btn_signature).setOnClickListener(this);

	return v;

    }

    @Override
    public void onAttach(Activity a) {

	super.onAttach(a);
	if (a instanceof OnMenuBarListener) {
	    mListener = (OnMenuBarListener) a;
	}
    }

    public void OnMenuBarListener(OnMenuBarListener listener) {

	mListener = listener;
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	    case R.id.btn_interv:
		mListener.onTabInterventoClick();
		break;

	    case R.id.btn_clients:
		mListener.onTabClientiClick();
		break;

	    case R.id.btn_costs:
		mListener.onTabCostiClick();
		break;

	    case R.id.btn_details:
		mListener.onTabDettagliClick();
		break;

	    case R.id.btn_signature:
		mListener.onTabFirmaClick();
		break;
	}
    }
}
