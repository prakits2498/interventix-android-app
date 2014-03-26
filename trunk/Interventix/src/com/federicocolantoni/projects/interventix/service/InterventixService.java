package com.federicocolantoni.projects.interventix.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.res.StringRes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.Intervento;
import com.federicocolantoni.projects.interventix.models.UtenteController;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

@EService
public class InterventixService extends IntentService {

    @StringRes(R.string.prefs_key_url)
    String prefsUrl;

    @StringRes(R.string.formatted_url_string)
    String formattedURL;

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

	    inviaInterventi();
	}
	else if (intent.getAction().equals(Constants.ACTION_GET_CLIENTI)) {

	    inviaClienti();
	}
    }

    private void inviaClienti() {

    }

    private void inviaInterventi() {

	QueryBuilder<Intervento, Long> qb = interventoDao.queryBuilder();

	qb.selectColumns();

	try {
	    qb.where().in(Constants.ORMLITE_MODIFICATO, new Object[] {
		    Constants.INTERVENTO_MODIFICATO, Constants.INTERVENTO_NUOVO
	    });
	    List<Intervento> tuttiInterventi = interventoDao.query(qb.prepare());

	    SharedPreferences prefsDefault = PreferenceManager.getDefaultSharedPreferences(Interventix.getContext());

	    // String prefsUrl = getResources().getString(R.string.prefs_key_url);

	    String url = prefsDefault.getString(prefsUrl, null);

	    for (final Intervento intervento : tuttiInterventi) {

		if (intervento.nuovo) {

		    // invio di un nuovo intervento

		    Map<String, String> parameters = new HashMap<String, String>();

		    parameters.put(Constants.JSON_CLIENTE, Long.toString(intervento.cliente));
		    parameters.put(Constants.JSON_TECNICO, Long.toString(intervento.tecnico));
		    parameters.put(Constants.JSON_TIPOLOGIA, intervento.tipologia);
		    parameters.put(Constants.JSON_MODALITA, intervento.modalita);
		    parameters.put(Constants.JSON_PRODOTTO, intervento.prodotto);
		    parameters.put(Constants.JSON_MOTIVO, intervento.motivo);
		    parameters.put(Constants.JSON_NOMINATIVO, intervento.nominativo);
		    parameters.put(Constants.JSON_DATAORA, "" + intervento.dataora);
		    parameters.put(Constants.JSON_RIF_FATTURA, intervento.riffattura);
		    parameters.put(Constants.JSON_RIF_SCONTRINO, intervento.rifscontrino);
		    parameters.put(Constants.JSON_COSTOMANODOPERA, intervento.costomanodopera.toPlainString());
		    parameters.put(Constants.JSON_COSTOCOMPONENTI, intervento.costocomponenti.toPlainString());
		    parameters.put(Constants.JSON_COSTOACCESSORI, intervento.costoaccessori.toPlainString());
		    parameters.put(Constants.JSON_IMPORTO, intervento.importo.toPlainString());
		    parameters.put(Constants.JSON_IVA, intervento.iva.toPlainString());
		    parameters.put(Constants.JSON_TOTALE, intervento.totale.toPlainString());
		    parameters.put(Constants.JSON_NOTE, intervento.note);
		    parameters.put(Constants.JSON_CHIUSO, Boolean.toString(intervento.chiuso));
		    parameters.put(Constants.JSON_FIRMA, intervento.firma);

		    List<DettaglioIntervento> listaDettagli = dettaglioDao.queryForEq(Constants.ORMLITE_IDINTERVENTO, intervento.idintervento);

		    JSONArray arrayDettagli = new JSONArray();

		    for (DettaglioIntervento dettaglio : listaDettagli) {

			JSONObject object = new JSONObject();

			object.put(Constants.JSON_TIPO, dettaglio.tipo);
			object.put(Constants.JSON_OGGETTO, dettaglio.oggetto);
			object.put(Constants.JSON_INIZIO, dettaglio.inizio);
			object.put(Constants.JSON_FINE, dettaglio.fine);
			object.put(Constants.JSON_DESCRIZIONE, dettaglio.descrizione);
			object.put(Constants.JSON_TECNICI, dettaglio.tecniciintervento);

			arrayDettagli.put(object);
		    }

		    parameters.put(Constants.JSON_DETTAGLIINTERVENTO, arrayDettagli.toString());

		    String jsonReq =
			    JsonCR2.createRequest(Constants.JSON_INTERVENTIONS_SECTION, Constants.JSON_ADD_INTERVENTIONS_ACTION, parameters, UtenteController.tecnicoLoggato.idutente.intValue());

		    RequestQueue requestQueue = Volley.newRequestQueue(this);

		    StringRequest jsonRequest = new StringRequest(Method.POST, String.format(formattedURL, url, jsonReq), new Listener<String>() {

			@Override
			public void onResponse(String response) {

			    try {

				final JSONObject jsonResp = new JSONObject(JsonCR2.read(response.trim()).toJSONString());

				System.out.println(jsonResp.toString(2));

				if (jsonResp != null && jsonResp.getString(Constants.JSON_RESPONSE).equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {

				    System.out.println("Intervento " + intervento.numero + " inviato con successo");

				    interventoDao.delete(intervento);
				}
				else {

				    System.out.println("Errore durante l'invio dell'intervento " + intervento.numero);
				}
			    }
			    catch (ParseException e) {

				BugSenseHandler.sendException(e);
				e.printStackTrace();
			    }
			    catch (Exception e) {

				BugSenseHandler.sendException(e);
				e.printStackTrace();
			    }
			    finally {

			    }
			}
		    }, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

			    // InterventixToast.makeToast(getString(R.string.service_not_available), Toast.LENGTH_LONG);
			}
		    });

		    requestQueue.add(jsonRequest);

		    // JSONObject response = new org.json.JSONObject(Utils.connectionForURL(jsonRequest, url_string).toJSONString());
		    //
		    // if (response != null && response.getString(Constants.JSON_RESPONSE).equalsIgnoreCase(Constants.JSON_RESPONSE_SUCCESS)) {
		    //
		    // System.out.println("Intervento " + intervento.numero + " inviato con successo");
		    //
		    // interventoDao.delete(intervento);
		    // }
		    // else {
		    //
		    // System.out.println("Errore durante l'invio dell'intervento " + intervento.numero);
		    // }
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

	Intent finishBuffer = new Intent(Constants.ACTION_FINISH_BUFFER);

	sendBroadcast(finishBuffer);
    }
}
