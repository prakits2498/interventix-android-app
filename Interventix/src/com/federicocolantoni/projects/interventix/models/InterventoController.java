package com.federicocolantoni.projects.interventix.models;

import java.util.ArrayList;

import android.widget.Toast;

import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.application.Interventix;
import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.federicocolantoni.projects.interventix.helpers.InterventixToast;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class InterventoController {

    public static InterventoSingleton controller;
    public static long revisioneInterventi = 0L;
    public static ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();

    public static void insertOnDB() {

	RuntimeExceptionDao<Intervento, Long> interventoDao = Interventix.getDbHelper().getRuntimeInterventoDao();

	if (interventoDao.idExists(InterventoController.controller.getIntervento().idintervento)) {

	    InterventoController.controller.getIntervento().modificato = Constants.INTERVENTO_MODIFICATO;
	}
	else {

	    InterventoController.controller.getIntervento().modificato = Constants.INTERVENTO_NUOVO;
	}

	interventoDao.createOrUpdate(InterventoController.controller.getIntervento());

	RuntimeExceptionDao<Utente, Long> utenteDao = Interventix.getDbHelper().getRuntimeUtenteDao();

	utenteDao.createOrUpdate(InterventoController.controller.getTecnico());

	RuntimeExceptionDao<Cliente, Long> clienteDao = Interventix.getDbHelper().getRuntimeClienteDao();

	clienteDao.createOrUpdate(InterventoController.controller.getCliente());

	RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao = Interventix.getDbHelper().getRuntimeDettaglioInterventoDao();

	for (DettaglioIntervento dettaglio : InterventoController.controller.getListaDettagli()) {

	    if (dettaglioDao.idExists(dettaglio.iddettagliointervento)) {

		dettaglio.modificato = Constants.DETTAGLIO_MODIFICATO;
	    }
	    else {

		dettaglio.modificato = Constants.DETTAGLIO_NUOVO;
	    }

	    dettaglioDao.createOrUpdate(dettaglio);
	}

	InterventixToast.makeToast(Interventix.getContext().getString(R.string.new_intervention_added), Toast.LENGTH_SHORT);
    }

    public static void updateOnDB() {

	RuntimeExceptionDao<Intervento, Long> interventoDao = Interventix.getDbHelper().getRuntimeInterventoDao();

	InterventoController.controller.getIntervento().modificato = Constants.INTERVENTO_MODIFICATO;

	interventoDao.update(InterventoController.controller.getIntervento());

	RuntimeExceptionDao<Utente, Long> utenteDao = Interventix.getDbHelper().getRuntimeUtenteDao();

	utenteDao.update(InterventoController.controller.getTecnico());

	RuntimeExceptionDao<Cliente, Long> clienteDao = Interventix.getDbHelper().getRuntimeClienteDao();

	clienteDao.update(InterventoController.controller.getCliente());

	RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao = Interventix.getDbHelper().getRuntimeDettaglioInterventoDao();

	for (DettaglioIntervento dettaglio : InterventoController.controller.getListaDettagli()) {

	    if (dettaglioDao.idExists(dettaglio.iddettagliointervento)) {

		dettaglio.modificato = Constants.DETTAGLIO_MODIFICATO;

		dettaglioDao.update(dettaglio);
	    }
	    else {

		dettaglioDao.create(dettaglio);
	    }
	}

	InterventixToast.makeToast(String.format(Interventix.getContext().getString(R.string.intervention_updated), InterventoController.controller.getIntervento().numero), Toast.LENGTH_SHORT);
    }
}
