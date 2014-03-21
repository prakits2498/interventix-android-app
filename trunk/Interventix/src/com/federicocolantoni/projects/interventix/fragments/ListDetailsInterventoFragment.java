package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.adapters.ListDettagliInterventiAdapter;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@SuppressLint("NewApi")
@EFragment(R.layout.fragment_list_details)
public class ListDetailsInterventoFragment extends Fragment implements OnDismissCallback {

    private ListDettagliInterventiAdapter mAdapter;

    @ViewById(R.id.layout_list_details_intervento)
    LinearLayout layoutListDetails;

    @ViewById(R.id.list_details_intervento)
    ListView detailsList;

    @ViewById(R.id.tv_no_details)
    TextView noDetails;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = DettaglioIntervento.class)
    RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {

	super.onStart();

	if (!InterventoController.controller.getIntervento().nuovo)
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(
		    getString(R.string.numero_intervento) + InterventoController.controller.getIntervento().numero + " - " + getString(R.string.row_list_details));
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(getString(R.string.new_interv) + " - " + getString(R.string.row_list_details));

	if (InterventoController.controller.getListaDettagli().size() == 0) {
	    layoutListDetails.setVisibility(View.INVISIBLE);
	    noDetails.setText("Nessun dettaglio presente");
	    noDetails.setVisibility(View.VISIBLE);
	}
	else {
	    layoutListDetails.setVisibility(View.VISIBLE);
	    noDetails.setVisibility(View.INVISIBLE);
	}

	mAdapter = new ListDettagliInterventiAdapter(getActivity());

	SwipeDismissAdapter swipeDismissAdapter = new SwipeDismissAdapter(mAdapter, this);
	swipeDismissAdapter.setAbsListView(detailsList);

	detailsList.setAdapter(swipeDismissAdapter);

	detailsList.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

		Bundle bundle = new Bundle();

		bundle.putSerializable(Constants.DETTAGLIO_N_ESIMO, InterventoController.controller.getListaDettagli().get(position));

		FragmentManager manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		DetailInterventoFragment_ dettInterv = new DetailInterventoFragment_();

		dettInterv.setArguments(bundle);

		transaction.replace(R.id.fragments_layout, dettInterv, Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		transaction.commit();
	    }
	});
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

	super.onCreateOptionsMenu(menu, inflater);

	inflater.inflate(R.menu.menu_view_intervento, menu);

	MenuItem itemAddDetail = menu.findItem(R.id.add_detail_interv);
	itemAddDetail.setVisible(true);
	itemAddDetail.setIcon(R.drawable.ic_action_add);
	itemAddDetail.setEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {

	    case R.id.add_detail_interv:

		FragmentManager manager = ((ActionBarActivity) getActivity()).getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();

		Bundle bundle = new Bundle();
		bundle.putString(Constants.NUOVO_DETTAGLIO_INTERVENTO, Constants.NUOVO_DETTAGLIO_INTERVENTO);

		DetailInterventoFragment_ dettInterv = new DetailInterventoFragment_();
		dettInterv.setArguments(bundle);

		transaction.replace(R.id.fragments_layout, dettInterv, Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.INFO_DETAIL_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		transaction.commit();

		break;

	    case R.id.pay:

		break;

	    case R.id.send_mail:

		break;

	    case R.id.close:

		break;
	}

	return true;
    }

    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {

	for (int position : reverseSortedPositions) {

	    DettaglioIntervento dettaglio = InterventoController.controller.getListaDettagli().get(position);

	    InterventoController.controller.getListaDettagli().remove(position);

	    dettaglioDao.delete(dettaglio);
	}

	if (InterventoController.controller.getListaDettagli().size() == 0) {
	    layoutListDetails.setVisibility(View.INVISIBLE);
	    noDetails.setText(R.string.no_details);
	    noDetails.setVisibility(View.VISIBLE);
	}
    }

    @Override
    public void onStop() {

	super.onStop();

	com.federicocolantoni.projects.interventix.application.Interventix_.releaseDbHelper();
    }
}