
package com.federicocolantoni.projects.interventix.core;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.federicocolantoni.projects.interventix.R;

public class OverViewIntervento extends SherlockFragment implements
	OnClickListener {

    private OverViewIntervento.OnOverViewListener mListener;

    @Override
    public void onAttach(Activity activity) {

	super.onAttach(activity);
	if (activity instanceof OverViewIntervento.OnOverViewListener) {
	    mListener = (OverViewIntervento.OnOverViewListener) activity;
	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	super.onCreateView(inflater, container, savedInstanceState);

	Bundle bundle = getArguments();

	final View view = inflater.inflate(
		R.layout.overview_intervento_fragment, container, false);

	View rowClient = view.findViewById(R.id.row_client);
	rowClient.setOnClickListener(this);

	View rowUser = view.findViewById(R.id.row_user);
	rowUser.setOnClickListener(this);

	View rowDetails = view.findViewById(R.id.row_details);
	rowDetails.setOnClickListener(this);

	View rowSignature = view.findViewById(R.id.row_signature);
	rowSignature.setOnClickListener(this);

	View rowInformations = view.findViewById(R.id.row_informations);
	rowInformations.setOnClickListener(this);

	View rowCosts = view.findViewById(R.id.row_costs);
	rowCosts.setOnClickListener(this);

	return view;
    }

    @Override
    public void onClick(View v) {

	switch (v.getId()) {
	    case R.id.row_client:
		mListener.onClickClient();
		break;

	    case R.id.row_user:
		mListener.onClickUser();
		break;

	    case R.id.row_informations:
		mListener.onClickInformations();
		break;

	    case R.id.row_costs:
		mListener.onClickCosts();
		break;

	    case R.id.row_details:
		mListener.onClickDetails();
		break;

	    case R.id.row_signature:
		mListener.onClickSignature();
		break;
	}
    }

    public void onOverViewListener(OnOverViewListener listener) {

	mListener = listener;
    }

    public static interface OnOverViewListener {

	public void onClickClient();

	public void onClickUser();

	public void onClickInformations();

	public void onClickDetails();

	public void onClickCosts();

	public void onClickSignature();
    }
}
