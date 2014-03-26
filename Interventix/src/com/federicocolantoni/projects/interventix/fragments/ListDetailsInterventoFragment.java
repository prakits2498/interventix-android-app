package com.federicocolantoni.projects.interventix.fragments;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

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
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

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

    @StringRes(R.string.numero_intervento)
    String numeroIntervento;

    @StringRes(R.string.row_list_details)
    String rowListDetails;

    @StringRes(R.string.new_interv)
    String nuovoIntervento;

    @StringRes(R.string.no_details)
    String stringNoDetails;

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
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(numeroIntervento + InterventoController.controller.getIntervento().numero + " - " + rowListDetails);
	else
	    ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(nuovoIntervento + " - " + rowListDetails);

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

		bundle.putSerializable(Constants.DETTAGLIO_N_ESIMO, mAdapter.getItem(position));

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
    public void onResume() {

	super.onResume();

	System.out.println("Lista dettagli caricata dal DB\n" + InterventoController.controller.getListaDettagli().toString());
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
	    noDetails.setText(stringNoDetails);
	    noDetails.setVisibility(View.VISIBLE);
	}
    }
}
