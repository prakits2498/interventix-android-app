package com.federicocolantoni.projects.interventix.activities;

import java.sql.SQLException;
import java.util.ArrayList;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.InterventixToast;
import com.federicocolantoni.projects.interventix.models.Cliente;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.Intervento;
import com.federicocolantoni.projects.interventix.models.InterventoController;
import com.federicocolantoni.projects.interventix.models.InterventoSingleton;
import com.federicocolantoni.projects.interventix.models.Utente;
import com.federicocolantoni.projects.interventix.models.UtenteController;
import com.j256.ormlite.dao.RuntimeExceptionDao;

@SuppressLint("NewApi")
@EActivity(R.layout.activity_view_intervento)
public class ViewInterventoActivity extends ActionBarActivity {

    private SharedPreferences prefs;

    private Intervento intervento;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Cliente.class)
    RuntimeExceptionDao<Cliente, Long> clienteDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Intervento.class)
    RuntimeExceptionDao<Intervento, Long> interventoDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = DettaglioIntervento.class)
    RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    RuntimeExceptionDao<Utente, Long> utenteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	BugSenseHandler.initAndStartSession(this, Constants.API_KEY);

	getSupportActionBar().setHomeButtonEnabled(true);
	getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	InterventoController.listaClienti = (ArrayList<Cliente>) clienteDao.queryForAll();
    }

    @Override
    protected void onStart() {

	super.onStart();

	Bundle extras = getIntent().getExtras();

	prefs = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);

	SpannableStringBuilder spanStringBuilder = new SpannableStringBuilder(UtenteController.tecnicoLoggato.nome + " " + UtenteController.tecnicoLoggato.cognome);
	spanStringBuilder.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	spanStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), 0, spanStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	getSupportActionBar().setTitle(spanStringBuilder);

	FragmentManager manager = getSupportFragmentManager();
	FragmentTransaction transaction = manager.beginTransaction();

	if (extras != null)
	    intervento = (Intervento) extras.getSerializable(Constants.INTERVENTO);

	if (intervento != null) {

	    InterventoController.controller = InterventoSingleton.getInstance();

	    InterventoController.controller.setIntervento(interventoDao.queryForId(intervento.idintervento));

	    InterventoController.controller.setCliente(clienteDao.queryForId(intervento.cliente));

	    InterventoController.controller.setListaDettagli(dettaglioDao.queryForEq(Constants.ORMLITE_IDINTERVENTO, intervento.idintervento));

	    System.out.println("Lista dettagli caricata dal DB\n" + InterventoController.controller.getListaDettagli().toString());

	    InterventoController.controller.setTecnico(utenteDao.queryForId(InterventoController.controller.getIntervento().tecnico));

	    if (manager.getBackStackEntryCount() == 0) {
		com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_ overView = new com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_();

		transaction.add(R.id.fragments_layout, overView, Constants.OVERVIEW_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
	    }
	}
	else {

	    InterventixToast.makeToast("Nuovo intervento", Toast.LENGTH_SHORT);

	    InterventoController.controller = InterventoSingleton.getInstance();

	    long maxNumero = interventoDao.queryRawValue("select max(" + Constants.ORMLITE_NUMERO + ") from Interventi");

	    try {
		Intervento foo = interventoDao.queryBuilder().where().eq(Constants.ORMLITE_NUMERO, maxNumero).queryForFirst();

		InterventoController.controller.getIntervento().numero = foo.numero + 1;
	    }
	    catch (SQLException e) {

		e.printStackTrace();
	    }

	    long maxId = interventoDao.queryRawValue("select max(" + Constants.ORMLITE_IDINTERVENTO + ") from Interventi");

	    try {
		Intervento foo = interventoDao.queryBuilder().where().eq(Constants.ORMLITE_IDINTERVENTO, maxId).queryForFirst();

		InterventoController.controller.getIntervento().idintervento = foo.idintervento + 1;
	    }
	    catch (SQLException e) {

		e.printStackTrace();
	    }

	    InterventoController.controller.getIntervento().dataora = new DateTime().getMillis();
	    InterventoController.controller.getIntervento().nuovo = true;
	    InterventoController.controller.getIntervento().chiuso = false;
	    InterventoController.controller.getIntervento().tecnico = UtenteController.tecnicoLoggato.idutente;

	    manager = getSupportFragmentManager();
	    transaction = manager.beginTransaction();

	    if (manager.getBackStackEntryCount() == 0) {
		com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_ overView = new com.federicocolantoni.projects.interventix.fragments.OverViewInterventoFragment_();

		transaction.add(R.id.fragments_layout, overView, Constants.OVERVIEW_INTERVENTO_FRAGMENT);
		transaction.addToBackStack(Constants.OVERVIEW_INTERVENTO_FRAGMENT);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		transaction.commit();
	    }
	}

	final Editor edit = prefs.edit().putInt(Constants.HASH_CODE_INTERVENTO_SINGLETON, InterventoController.controller.hashCode());

	edit.apply();
    }

    @Override
    protected void onPause() {

	super.onPause();

	InterventoSingleton.reset();
    }

    @Override
    protected void onDestroy() {

	super.onDestroy();

	com.federicocolantoni.projects.interventix.application.Interventix_.releaseDbHelper();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

	if (keyCode == KeyEvent.KEYCODE_BACK) {

	    final FragmentManager manager = getSupportFragmentManager();

	    if (manager.getBackStackEntryCount() == 1) {

		checkInterventionsHashcode();
	    }
	    else {

		manager.popBackStackImmediate();
	    }
	    return true;
	}

	return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

	final FragmentManager manager = getSupportFragmentManager();

	if (manager.getBackStackEntryCount() == 1) {

	    checkInterventionsHashcode();
	}
	else {
	    manager.popBackStackImmediate();
	}
    }

    public static class ExitIntervento extends DialogFragment implements OnClickListener {

	public ExitIntervento() {

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    AlertDialog.Builder exitDialog = new Builder(getActivity());

	    exitDialog.setTitle(R.string.interv_mod_title);
	    exitDialog.setMessage(R.string.interv_mod_text);

	    exitDialog.setPositiveButton(getString(R.string.yes_btn), this);
	    exitDialog.setNegativeButton(getString(R.string.no_btn), this);

	    return exitDialog.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	    switch (which) {

		case DialogInterface.BUTTON_POSITIVE:

		    dialog.dismiss();

		    saveInterventoOnDB();

		    getActivity().finish();

		    break;

		case DialogInterface.BUTTON_NEGATIVE:

		    dialog.dismiss();

		    InterventoSingleton.reset();

		    getActivity().finish();

		    break;
	    }
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	super.onCreateOptionsMenu(menu);

	return true;
    }

    private static void saveInterventoOnDB() {

	if (!InterventoController.controller.getIntervento().nuovo) {

	    InterventoController.updateOnDB();
	}
	else {

	    InterventoController.insertOnDB();
	}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

	switch (item.getItemId()) {

	    case android.R.id.home:

		checkInterventionsHashcode();

		break;
	}

	return super.onOptionsItemSelected(item);
    }

    private void checkInterventionsHashcode() {

	int hashCodeIntervento = InterventoController.controller.hashCode();

	if (hashCodeIntervento == prefs.getInt(Constants.HASH_CODE_INTERVENTO_SINGLETON, 0)) {

	    InterventoSingleton.reset();

	    finish();
	}
	else {

	    new ExitIntervento().show(getSupportFragmentManager(), Constants.EXIT_INTERVENTO_DIALOG_FRAGMENT);
	}
    }
}
