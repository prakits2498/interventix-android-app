package com.federicocolantoni.projects.interventix.service;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.res.StringRes;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.bugsense.trace.BugSenseHandler;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.Utils;
import com.federicocolantoni.projects.interventix.models.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.models.Intervento;
import com.federicocolantoni.projects.interventix.models.Utente;
import com.j256.ormlite.dao.CloseableIterator;
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

    @OrmLiteDao(helper = InterventixDBHelper.class, model = Utente.class)
    RuntimeExceptionDao<Utente, Long> utenteDao;

    Utente tecnico;

    private long counter = 0l;

    SharedPreferences prefsDefault, globalPrefs;

    Map<String, Object> parameters;

    public InterventixService() {

	super("InterventixService");
    }

    @Override
    public void onStart(Intent intent, int startId) {

	super.onStart(intent, startId);

	Constants.sNumberOfNotificationEvents = 0;

	SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

	tecnico = utenteDao.queryForEq(Constants.ORMLITE_USERNAME, prefs.getString(Constants.USERNAME, "")).get(0);

	prefsDefault = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

	if (intent.getAction().equals(Constants.ACTION_GET_INTERVENTI)) {

	    try {

		inviaInterventi();
	    }
	    catch (SQLException e) {

		e.printStackTrace();
	    }
	}
	else if (intent.getAction().equals(Constants.ACTION_GET_CLIENTI)) {

	    inviaClienti();
	}
    }

    private void inviaClienti() {

    }

    private void inviaInterventi() throws SQLException {

	System.out.println("Invia interventi modificati e nuovi");

	parameters = new HashMap<String, Object>();

	QueryBuilder<Intervento, Long> qb = interventoDao.queryBuilder();

	qb.where().in(Constants.ORMLITE_MODIFICATO, Constants.INTERVENTO_MODIFICATO, Constants.INTERVENTO_NUOVO);

	CloseableIterator<Intervento> listaInterventi = interventoDao.iterator(qb.prepare());

	while (listaInterventi.hasNext()) {

	    Intervento intervento = listaInterventi.next();

	    QueryBuilder<DettaglioIntervento, Long> qbDettagli = dettaglioDao.queryBuilder();

	    qbDettagli.where().eq(Constants.ORMLITE_IDINTERVENTO, intervento.idintervento);

	    CloseableIterator<DettaglioIntervento> listaDettagli = dettaglioDao.iterator(qbDettagli.prepare());

	    parameters.put(Constants.JSON_CLIENTE, Long.toString(intervento.cliente));
	    parameters.put(Constants.JSON_TECNICO, Long.toString(intervento.tecnico));
	    parameters.put(Constants.JSON_TIPOLOGIA, intervento.tipologia);
	    parameters.put(Constants.JSON_MODALITA, intervento.modalita);
	    parameters.put(Constants.JSON_PRODOTTO, intervento.prodotto);
	    parameters.put(Constants.JSON_MOTIVO, intervento.motivo);
	    parameters.put(Constants.JSON_NOMINATIVO, intervento.nominativo);
	    parameters.put(Constants.JSON_DATAORA, Long.toString(intervento.dataora));
	    parameters.put(Constants.JSON_RIF_FATTURA, intervento.riffattura);
	    parameters.put(Constants.JSON_RIF_SCONTRINO, intervento.rifscontrino);
	    parameters.put(Constants.JSON_COSTOMANODOPERA, intervento.costomanodopera.toString());
	    parameters.put(Constants.JSON_COSTOCOMPONENTI, intervento.costocomponenti.toString());
	    parameters.put(Constants.JSON_COSTOACCESSORI, intervento.costoaccessori.toString());
	    parameters.put(Constants.JSON_IMPORTO, intervento.importo.toString());
	    parameters.put(Constants.JSON_IVA, intervento.iva.toString());
	    parameters.put(Constants.JSON_TOTALE, intervento.totale.toString());
	    parameters.put(Constants.JSON_NOTE, intervento.note);
	    parameters.put(Constants.JSON_CHIUSO, Boolean.toString(intervento.chiuso));
	    parameters.put(Constants.JSON_FIRMA, intervento.firma);

	    if (listaDettagli.hasNext()) {

		JSONArray arrayDettagli = new JSONArray();

		while (listaDettagli.hasNext()) {

		    DettaglioIntervento dettInter = listaDettagli.next();

		    JSONObject dettaglio = new JSONObject();

		    try {

			dettaglio.put(Constants.JSON_TIPO, dettInter.tipo);
			dettaglio.put(Constants.JSON_OGGETTO, dettInter.oggetto);
			dettaglio.put(Constants.JSON_INIZIO, dettInter.inizio);
			dettaglio.put(Constants.JSON_FINE, dettInter.fine);
			dettaglio.put(Constants.JSON_DESCRIZIONE, dettInter.descrizione);
			dettaglio.put(Constants.JSON_TECNICIINTERVENTO, new JSONArray(dettInter.tecniciintervento));
		    }
		    catch (JSONException e) {

			BugSenseHandler.sendException(e);
			e.printStackTrace();
		    }

		    arrayDettagli.put(dettaglio);
		}

		listaDettagli.closeQuietly();

		parameters.put(Constants.JSON_DETTAGLIINTERVENTO, arrayDettagli);
	    }

	    try {

		String urlString = prefsDefault.getString(prefsUrl, null);

		String jsonReq = null;

		if (intervento.nuovo)
		    jsonReq = JsonCR2.createRequest(Constants.JSON_INTERVENTIONS_SECTION, Constants.JSON_ADD_INTERVENTIONS_ACTION, parameters, tecnico.idutente.intValue());
		else {

		    parameters.put(Constants.JSON_IDINTERVENTO, Long.toString(intervento.idintervento));

		    SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE);

		    parameters.put(Constants.JSON_REVISIONE, Long.toString(prefs.getLong(Constants.REVISION_INTERVENTI, 0l)));

		    jsonReq = JsonCR2.createRequest(Constants.JSON_INTERVENTIONS_SECTION, Constants.JSON_MOD_INTERVENTIONS_ACTION, parameters, tecnico.idutente.intValue());
		}

		File reqJSON = new File(getFilesDir(), "request_json.txt");

		FileUtils.writeStringToFile(reqJSON, jsonReq);

		JSONObject response = new JSONObject(Utils.connectionForURL(jsonReq, urlString).toJSONString());

		if (response.getString(Constants.JSON_RESPONSE).equals(Constants.JSON_RESPONSE_SUCCESS)) {

		    JSONObject request = response.getJSONObject("request");

		    if (request.getString(Constants.JSON_ACTION).equals(Constants.JSON_MOD_INTERVENTIONS_ACTION)
			    && request.getString(Constants.JSON_SECTION).equals(Constants.JSON_INTERVENTIONS_SECTION)) {

			counter++;

			intervento.modificato = Constants.INTERVENTO_SINCRONIZZATO;
			intervento.nuovo = false;

			interventoDao.update(intervento);

			List<DettaglioIntervento> listDett = dettaglioDao.queryForEq(Constants.ORMLITE_IDINTERVENTO, intervento.idintervento);

			for (DettaglioIntervento dett : listDett) {

			    dett.modificato = Constants.DETTAGLIO_SINCRONIZZATO;
			    dett.nuovo = false;

			    dettaglioDao.update(dett);
			}
		    }
		    else if (request.getString(Constants.JSON_ACTION).equals(Constants.JSON_ADD_INTERVENTIONS_ACTION)
			    && request.getString(Constants.JSON_SECTION).equals(Constants.JSON_INTERVENTIONS_SECTION)) {

			interventoDao.delete(intervento);

			counter++;

			System.out.println(response.getJSONObject(Constants.JSON_DATA).toString());
		    }
		}
		else {

		    System.out.println(response.toString());
		    break;
		}
	    }
	    catch (Exception e) {

		e.printStackTrace();
	    }
	}

	if (counter > 0)
	    sendNotification();
    }

    private void sendNotification() {

	NotificationCompat.Builder notification = new Builder(this);

	notification.setSmallIcon(R.drawable.ic_stat_interventix_icon);
	notification.setContentTitle(getString(R.string.app_name));
	notification.setContentText(getString(R.string.notification_content_text));

	Intent updateInterventions = new Intent(this, com.federicocolantoni.projects.interventix.activities.HomeActivity_.class);

	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, updateInterventions, PendingIntent.FLAG_ONE_SHOT);

	notification.setContentIntent(pendingIntent);
	notification.setNumber(++Constants.sNumberOfNotificationEvents);
	notification.setAutoCancel(true);
	notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
	notification.setVibrate(new long[] {
		0, 300, 100, 300, 100, 500
	});
	notification.setLights(getResources().getColor(R.color.interventix_color), 300, 1000);

	NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	notifManager.notify(Constants.NOTIFICATIOND_ID_INTERVENTIX, notification.build());
    }
}
