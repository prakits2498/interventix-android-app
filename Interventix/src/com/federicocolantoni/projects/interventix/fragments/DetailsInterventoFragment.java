package com.federicocolantoni.projects.interventix.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapter.DettaglioInterventoAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;

public class DetailsInterventoFragment extends SherlockFragment implements
	LoaderCallbacks<Cursor> {

    private final static int MESSAGE_LOADER = 1;

    private long mId_intervento;

    private final String[] PROJECTION = new String[] {
	    DettaglioInterventoDB.Fields._ID,
	    DettaglioInterventoDB.Fields.TIPO,
	    DettaglioInterventoDB.Fields.OGGETTO };

    private final String SELECTION = DettaglioInterventoDB.Fields.TYPE
	    + " = ? AND " + DettaglioInterventoDB.Fields.INTERVENTO + " = ?";

    private String[] SELECTION_ARGS;

    private DettaglioInterventoAdapter mAdapter;

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

	mId_intervento = bundle.getLong(Constants.ID_INTERVENTO);

	SELECTION_ARGS = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE,
		"" + mId_intervento };

	TextView tv_costs_intervento = (TextView) view
		.findViewById(R.id.tv_details_intervention);
	tv_costs_intervento.setText("Dettagli Intervento "
		+ bundle.getLong(Constants.NUMERO_INTERVENTO));

	// ListDetailsIntervento details = (ListDetailsIntervento) bundle
	// .getSerializable(Constants.LIST_DETAILS_INTERVENTO);

	// List<DettaglioIntervento> listDetails = details.getListDetails();

	ListView detailsList = (ListView) view
		.findViewById(R.id.list_details_intervento);

	mAdapter = new DettaglioInterventoAdapter(getSherlockActivity(), null);

	detailsList.setAdapter(mAdapter);

	getSherlockActivity().getSupportLoaderManager().initLoader(
		MESSAGE_LOADER, null, this);

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

	Loader<Cursor> loader = new CursorLoader(getSherlockActivity(),
		DettaglioInterventoDB.CONTENT_URI, PROJECTION, SELECTION,
		SELECTION_ARGS, null);

	return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

	mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

	mAdapter.swapCursor(null);
    }
}
