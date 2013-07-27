package com.federicocolantoni.projects.interventix.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;

public class DetailsInterventoFragment extends SherlockFragment
		implements
			LoaderCallbacks<Cursor> {

	public static long sId_intervento;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		BugSenseHandler.initAndStartSession(getSherlockActivity(),
				Constants.API_KEY);

		super.onCreateView(inflater, container, savedInstanceState);

		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(true);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(
				true);

		final View view = inflater.inflate(
				R.layout.details_intervento_fragment, container, false);

		Bundle bundle = getArguments();

		sId_intervento = bundle.getLong(Constants.ID_INTERVENTO);

		TextView tv_costs_intervento = (TextView) view
				.findViewById(R.id.tv_details_intervention);
		tv_costs_intervento.setText("Dettagli Intervento "
				+ bundle.getLong(Constants.NUMERO_INTERVENTO));

		// ListDetailsIntervento details = (ListDetailsIntervento) bundle
		// .getSerializable(Constants.LIST_DETAILS_INTERVENTO);
		//
		// List<DettaglioIntervento> listDetails = details.getListDetails();

		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		getSherlockActivity().getSupportMenuInflater().inflate(
				R.menu.details_intervento, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}
