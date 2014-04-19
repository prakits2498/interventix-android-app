package com.federicocolantoni.projects.interventix.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import multiface.crypto.cr2.JsonCR2;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.res.StringRes;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.activities.HomeActivity_;
import com.federicocolantoni.projects.interventix.data.InterventixDBHelper;
import com.federicocolantoni.projects.interventix.helpers.Constants;
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

    Utente tecnico;

    Map<String, Object> parameters;

    public InterventixService() {

	super("InterventixService");
    }

    @Override
    public void onStart(Intent intent, int startId) {

	super.onStart(intent, startId);

	tecnico = (Utente) intent.getSerializableExtra(Constants.TECNICO);
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

	    System.out.println(intervento);
	}

	try {
	    JsonCR2.createRequest(Constants.JSON_INTERVENTIONS_SECTION, Constants.JSON_ADD_INTERVENTIONS_ACTION, parameters, tecnico.idutente.intValue());
	}
	catch (Exception e) {

	    e.printStackTrace();
	}

	// TODO send notification after all interventions are sent to the server
	sendNotification();
    }

    private void sendNotification() {

	NotificationCompat.Builder notification = new Builder(this);

	notification.setSmallIcon(R.drawable.ic_stat_interventix_icon);
	notification.setContentTitle(getString(R.string.app_name));
	// notification.setContentText(getString(R.string.notification_content_text));

	Intent updateInterventions = new Intent(this, HomeActivity_.class);

	PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, updateInterventions, PendingIntent.FLAG_UPDATE_CURRENT);

	notification.setContentIntent(pendingIntent);
	notification.setNumber(++Constants.sNumberOfNotificationEvents);
	notification.setAutoCancel(true);
	notification.setDefaults(Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_SHOW_LIGHTS);
	notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
	notification.setVibrate(new long[] {
		0, 300, 100, 300, 100, 500
	});

	int red = Color.red(R.color.interventix_color);
	int green = Color.green(R.color.interventix_color);
	int blue = Color.blue(R.color.interventix_color);

	int notifLightColor = Color.argb(255, red, green, blue);

	notification.setLights(notifLightColor, 300, 1000);
	notification.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.notification_content_text)));

	NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	notifManager.notify(Constants.NOTIFICATIOND_ID_INTERVENTIX, notification.build());
    }
}
