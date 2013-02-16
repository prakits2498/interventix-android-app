
package com.federicocolantoni.projects.interventix;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
    private FragmentManager mFragMng;
    private FragmentTransaction mXact;

    private static Intervento sInterv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	requestWindowFeature(Window.FEATURE_NO_TITLE);

	setContentView(R.layout.activity_tab_bar);

	mFragMng = getSupportFragmentManager();
	mMenuFrag = (MenuBarFragment) mFragMng
		.findFragmentById(R.id.menuBar_fragment);

	onTabInterventoListener();
    }

    @Override
    public void onTabInterventoListener() {

	mXact = mFragMng.beginTransaction();
	mXact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	mXact.replace(R.id.frame_for_fragments, new InterventoFragment(),
		TAG_INTERVENTO);
	mXact.commit();
    }

    @Override
    public void onTabClientiListener() {

	try {
	    mXact = mFragMng.beginTransaction();
	    mXact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    mXact.replace(R.id.frame_for_fragments, new ClientiFragment(),
		    TAG_CLIENTI);
	    mXact.commit();
	} catch (NoClassDefFoundError e) {
	    Log.d(MainActivity.DEBUG_TAG, "NO_CLASS_DEF_FOUND_ERROR!", e);
	}
    }

    @Override
    public void onTabCostiListener() {

	mXact = mFragMng.beginTransaction();
	mXact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	mXact.replace(R.id.frame_for_fragments, new CostiFragment(), TAG_COSTI);
	mXact.commit();
    }

    @Override
    public void onTabDettagliListener() {

	mXact = mFragMng.beginTransaction();
	mXact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	mXact.replace(R.id.frame_for_fragments, new DettagliFragment(),
		TAG_DETTAGLI);
	mXact.commit();
    }

    @Override
    public void onTabFirmaListener() {

	mXact = mFragMng.beginTransaction();
	mXact.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	mXact.replace(R.id.frame_for_fragments, new FirmaFragment(), TAG_FIRMA);
	mXact.commit();
    }

    @Override
    public void onSaveListener() {

	sInterv = new Intervento();

	EditText tipologia = (EditText) findViewById(R.id.edit_type_interv);
	sInterv.setmTipologia(tipologia.getText().toString());

	EditText prodotto = (EditText) findViewById(R.id.edit_prod);
	sInterv.setmProdotto(prodotto.getText().toString());

	EditText motivo = (EditText) findViewById(R.id.edit_what);
	sInterv.setmMotivo(motivo.getText().toString());

	EditText nominativo = (EditText) findViewById(R.id.edit_nomin);
	sInterv.setmTipologia(nominativo.getText().toString());

	Toast.makeText(
		this,
		sInterv.getmTipologia() + "\n" + sInterv.getmProdotto() + "\n"
			+ sInterv.getmMotivo() + "\n"
			+ sInterv.getmNominativo(), Toast.LENGTH_LONG).show();

	Toast.makeText(this, "Intervento salvato", Toast.LENGTH_SHORT).show();
    }
}
