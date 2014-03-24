package com.federicocolantoni.projects.interventix.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.OrmLiteDao;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.Utils;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.Intervento;
import com.federicocolantoni.projects.interventix.models.UtenteController;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

@EService
public class InterventixService extends IntentService {

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Intervento.class)
    RuntimeExceptionDao<Intervento, Long> interventoDao;

    @OrmLiteDao(helper = InterventixDBHelper.class, model = DettaglioIntervento.class)
    RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao;

    public InterventixService() {

	super("InterventixService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

	if (intent.getAction().equals(Constants.ACTION_GET_INTERVENTI)) {

	    // System.out.println("Action " + Constants.ACTION_GET_INTERVENTI);

	    inviaInterventi();
	}
	else if (intent.getAction().equals(Constants.ACTION_GET_CLIENTI)) {

	    // System.out.println("Action " + Constants.ACTION_GET_CLIENTI);

	    inviaClienti();
	}
    }

    private void inviaClienti() {

    }

    private void inviaInterventi() {

	// RuntimeExceptionDao<Intervento, Long> interventoDao = com.federicocolantoni.projects.interventix.application.Interventix_.getDbHelper().getRuntimeInterventoDao();

	QueryBuilder<Intervento, Long> qb = interventoDao.queryBuilder();

	qb.selectColumns();

	try {
	    qb.where().in(Constants.ORMLITE_MODIFICATO, new Object[] {
		    Constants.INTERVENTO_MODIFICATO, Constants.INTERVENTO_NUOVO
	    });
	    List<Intervento> tuttiInterventi = interventoDao.query(qb.prepare());

	    SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(Interventix.getContext());

	    String prefs_url = getResources().getString(R.string.prefs_key_url);

	    String url_string = prefsDefault.getString(prefs_url, null);

	    for (Intervento intervento : tuttiInterventi) {

		if (intervento.nuovo) {

		    // invio di un nuovo intervento

		    Map<String, String> parameters = new HashMap<String, String>();

		    parameters.put("cliente", Long.toString(intervento.cliente));
		    parameters.put("tecnico", Long.toString(intervento.tecnico));
		    parameters.put("tipologia", intervento.tipologia);
		    parameters.put("modalita", intervento.modalita);
		    parameters.put("prodotto", intervento.prodotto);
		    parameters.put("motivo", intervento.motivo);
		    parameters.put("nominativo", intervento.nominativo);
		    parameters.put("dataora", "" + intervento.dataora);
		    parameters.put("riffattura", intervento.riffattura);
		    parameters.put("rifscontrino", intervento.rifscontrino);
		    parameters.put("costomanodopera", intervento.costomanodopera.toPlainString());
		    parameters.put("costocomponenti", intervento.costocomponenti.toPlainString());
		    parameters.put("costoaccessori", intervento.costoaccessori.toPlainString());
		    parameters.put("importo", intervento.importo.toPlainString());
		    parameters.put("iva", intervento.iva.toPlainString());
		    parameters.put("totale", intervento.totale.toPlainString());
		    parameters.put("note", intervento.note);
		    parameters.put("chiuso", Boolean.toString(intervento.chiuso));
		    parameters.put("firma", intervento.firma);

		    List<DettaglioIntervento> listaDettagli = dettaglioDao.queryForEq("idintervento", intervento.idintervento);

		    JSONArray arrayDettagli = new JSONArray();

		    for (DettaglioIntervento dettaglio : listaDettagli) {

			JSONObject object = new JSONObject();

			object.put("tipo", dettaglio.tipo);
			object.put("oggetto", dettaglio.oggetto);
			object.put("inizio", dettaglio.inizio);
			object.put("fine", dettaglio.fine);
			object.put("descrizione", dettaglio.descrizione);
			object.put("tecnici", dettaglio.tecniciintervento);

			arrayDettagli.put(object);
		    }

		    parameters.put("dettagliintervento", arrayDettagli.toString());

		    String jsonRequest = JsonCR2.createRequest("interventions", "add", parameters, UtenteController.tecnicoLoggato.idutente.intValue());

		    JSONObject response = new org.json.JSONObject(Utils.connectionForURL(jsonRequest, url_string).toJSONString());

		    if (response != null && response.getString("response").equalsIgnoreCase("success")) {

			System.out.println("Intervento " + intervento.numero + " inviato con successo");

			interventoDao.delete(intervento);
		    }
		    else {

			System.out.println("Errore durante l'invio dell'intervento " + intervento.numero);
		    }
		}
		else {

		    // invio di un intervento modificato
		}
	    }
	}
	catch (SQLException e) {

	    e.printStackTrace();
	}
	catch (Exception e) {

	    e.printStackTrace();
	}

	// Interventix_.releaseDbHelper();

	Intent finishBuffer = new Intent(Constants.ACTION_FINISH_BUFFER);

	sendBroadcast(finishBuffer);
    }
}
