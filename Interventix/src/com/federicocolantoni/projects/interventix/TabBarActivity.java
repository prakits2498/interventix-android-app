
package com.federicocolantoni.projects.interventix;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.federicocolantoni.projects.interventix.MenuBarFragment.OnMenuBarListener;
import com.federicocolantoni.projects.interventix.intervento.ClientiFragment;
import com.federicocolantoni.projects.interventix.intervento.CostiFragment;
import com.federicocolantoni.projects.interventix.intervento.DettagliFragment;
import com.federicocolantoni.projects.interventix.intervento.FirmaFragment;
import com.federicocolantoni.projects.interventix.intervento.InterventoFragment;

public class TabBarActivity extends FragmentActivity implements
	OnMenuBarListener {

    private static final String DEBUG_TAG = "INTERVENTIX";
    private static final String TAG_INTERVENTO = "INTERVENT";
    private static final String TAG_CLIENTI = "CLIENTS";
    private static final String TAG_COSTI = "COSTS";
    private static final String TAG_DETTAGLI = "DETAILS";
    private static final String TAG_FIRMA = "SIGNATURE";

    private MenuBarFragment mMenuFrag;
    private FragmentManager fragMng;
    private FragmentTransaction xact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_tab_bar);

	fragMng = getSupportFragmentManager();
	mMenuFrag = (MenuBarFragment) fragMng
		.findFragmentById(R.id.menuBar_fragment);
    }

    @Override
    public void onTabInterventoClick() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new InterventoFragment(),
		TAG_INTERVENTO);
	xact.commit();
    }

    @Override
    public void onTabClientiClick() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new ClientiFragment(),
		TAG_CLIENTI);
	xact.commit();
    }

    @Override
    public void onTabCostiClick() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new CostiFragment(), TAG_COSTI);
	xact.commit();
    }

    @Override
    public void onTabDettagliClick() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new DettagliFragment(),
		TAG_DETTAGLI);
	xact.commit();
    }

    @Override
    public void onTabFirmaClick() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new FirmaFragment(), TAG_FIRMA);
	xact.commit();
    }
}
