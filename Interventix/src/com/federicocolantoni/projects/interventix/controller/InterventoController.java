package com.federicocolantoni.projects.interventix.controller;

import java.util.ArrayList;

import android.widget.Toast;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.R;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.federicocolantoni.projects.interventix.utils.InterventixToast;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class InterventoController {

    public static InterventoSingleton controller;
    public static long revisioneInterventi = 0L;
    public static ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();

    public static void insertOnDB() {

	RuntimeExceptionDao<Intervento, Long> interventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeInterventoDao();

	InterventoController.controller.getIntervento().modificato = Constants.INTERVENTO_NUOVO;

	interventoDao.createIfNotExists(InterventoController.controller.getIntervento());

	RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

	utenteDao.createIfNotExists(InterventoController.controller.getTecnico());

	RuntimeExceptionDao<Cliente, Long> clienteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeClienteDao();

	clienteDao.createIfNotExists(InterventoController.controller.getCliente());

	RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeDettaglioInterventoDao();

	for (DettaglioIntervento dettaglio : InterventoController.controller.getListaDettagli()) {

	    if (dettaglioDao.idExists(dettaglio.iddettagliointervento)) {

		dettaglio.modificato = Constants.DETTAGLIO_MODIFICATO;

		dettaglioDao.update(dettaglio);
	    }
	    else {

		dettaglioDao.create(dettaglio);
	    }
	}

	com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

	InterventixToast.makeToast(com.federicocolantoni.projects.interventix.Interventix_.getContext().getString(R.string.new_intervention_added), Toast.LENGTH_SHORT);
    }

    public static void updateOnDB() {

	RuntimeExceptionDao<Intervento, Long> interventoDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeInterventoDao();

	InterventoController.controller.getIntervento().modificato = Constants.INTERVENTO_MODIFICATO;

	interventoDao.update(InterventoController.controller.getIntervento());

	RuntimeExceptionDao<Utente, Long> utenteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeUtenteDao();

	utenteDao.update(InterventoController.controller.getTecnico());

	RuntimeExceptionDao<Cliente, Long> clienteDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeClienteDao();

	clienteDao.update(InterventoController.controller.getCliente());

	RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao = com.federicocolantoni.projects.interventix.Interventix_.getDbHelper().getRuntimeDettaglioInterventoDao();

	for (DettaglioIntervento dettaglio : InterventoController.controller.getListaDettagli()) {

	    if (dettaglioDao.idExists(dettaglio.iddettagliointervento)) {

		dettaglio.modificato = Constants.DETTAGLIO_MODIFICATO;

		int updated = dettaglioDao.update(dettaglio);

		if (updated == 1) {

		}
		else {

		}
	    }
	    else {

		dettaglioDao.create(dettaglio);
	    }
	}

	com.federicocolantoni.projects.interventix.Interventix_.releaseDbHelper();

	InterventixToast.makeToast(
		String.format(com.federicocolantoni.projects.interventix.Interventix_.getContext().getString(R.string.intervention_updated), InterventoController.controller.getIntervento().numero),
		Toast.LENGTH_SHORT);
    }
}
