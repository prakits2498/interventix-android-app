
package com.federicocolantoni.projects.interventix;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.federicocolantoni.projects.interventix.MenuBarFragment.OnMenuBarListener;
import com.federicocolantoni.projects.interventix.intervento.ClientiFragment;
import com.federicocolantoni.projects.interventix.intervento.CostiFragment;
import com.federicocolantoni.projects.interventix.intervento.DettagliFragment;
import com.federicocolantoni.projects.interventix.intervento.FirmaFragment;
import com.federicocolantoni.projects.interventix.intervento.Intervento;
import com.federicocolantoni.projects.interventix.intervento.InterventoFragment;
import com.federicocolantoni.projects.interventix.intervento.InterventoFragment.OnSaveInterventoListener;

public class TabBarActivity extends FragmentActivity implements
	OnMenuBarListener, OnSaveInterventoListener {

    static final String GLOBAL_PREFERENCES = "Preferences";

    private static final String TAG_INTERVENTO = "INTERVENT";
    private static final String TAG_CLIENTI = "CLIENTS";
    private static final String TAG_COSTI = "COSTS";
    private static final String TAG_DETTAGLI = "DETAILS";
    private static final String TAG_FIRMA = "SIGNATURE";

    private MenuBarFragment mMenuFrag;
    private FragmentManager fragMng;
    private FragmentTransaction xact;

    private static Intervento interv;

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
    public void onTabInterventoListener() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new InterventoFragment(),
		TAG_INTERVENTO);
	xact.commit();
    }

    @Override
    public void onTabClientiListener() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new ClientiFragment(),
		TAG_CLIENTI);
	xact.commit();
    }

    @Override
    public void onTabCostiListener() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new CostiFragment(), TAG_COSTI);
	xact.commit();
    }

    @Override
    public void onTabDettagliListener() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new DettagliFragment(),
		TAG_DETTAGLI);
	xact.commit();
    }

    @Override
    public void onTabFirmaListener() {

	xact = fragMng.beginTransaction();
	xact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	xact.replace(R.id.frame_for_fragments, new FirmaFragment(), TAG_FIRMA);
	xact.commit();
    }

    @Override
    public void onSaveListener() {

	interv = new Intervento();

	EditText tipologia = (EditText) findViewById(R.id.edit_type_interv);
	interv.setmTipologia(tipologia.getText().toString());

	EditText prodotto = (EditText) findViewById(R.id.edit_prod);
	interv.setmProdotto(prodotto.getText().toString());

	EditText motivo = (EditText) findViewById(R.id.edit_what);
	interv.setmMotivo(motivo.getText().toString());

	EditText nominativo = (EditText) findViewById(R.id.edit_nomin);
	interv.setmTipologia(nominativo.getText().toString());

	Toast.makeText(
		this,
		interv.getmTipologia() + "\n" + interv.getmProdotto() + "\n"
			+ interv.getmMotivo() + "\n" + interv.getmNominativo(),
		Toast.LENGTH_LONG).show();

	Toast.makeText(this, "Intervento salvato", Toast.LENGTH_SHORT).show();
    }
}
