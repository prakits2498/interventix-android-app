package com.federicocolantoni.projects.interventix.controller;

import java.util.ArrayList;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.Interventix_;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class InterventoController {
    
    public static InterventoSingleton controller;
    public static long revisioneInterventi = 0L;
    public static ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();
    
    public static void insertOnDB() {
    
	RuntimeExceptionDao<Intervento, Long> interventoDao = Interventix_.getDbHelper().getRuntimeInterventoDao();
	
	InterventoController.controller.getIntervento().modificato = Constants.INTERVENTO_MODIFICATO;
	
	interventoDao.createIfNotExists(InterventoController.controller.getIntervento());
	
	RuntimeExceptionDao<Utente, Long> utenteDao = Interventix_.getDbHelper().getRuntimeUtenteDao();
	
	utenteDao.createIfNotExists(InterventoController.controller.getTecnico());
	
	RuntimeExceptionDao<Cliente, Long> clienteDao = Interventix_.getDbHelper().getRuntimeClienteDao();
	
	clienteDao.createIfNotExists(InterventoController.controller.getCliente());
	
	RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao = Interventix_.getDbHelper().getRuntimeDettaglioInterventoDao();
	
	for (DettaglioIntervento dettaglio : InterventoController.controller.getListaDettagli())
	    dettaglioDao.createIfNotExists(dettaglio);
	
	Interventix_.releaseDbHelper();
    }
    
    public static void updateOnDB() {
    
	RuntimeExceptionDao<Intervento, Long> interventoDao = Interventix_.getDbHelper().getRuntimeInterventoDao();
	
	interventoDao.update(InterventoController.controller.getIntervento());
	
	RuntimeExceptionDao<Utente, Long> utenteDao = Interventix_.getDbHelper().getRuntimeUtenteDao();
	
	utenteDao.update(InterventoController.controller.getTecnico());
	
	RuntimeExceptionDao<Cliente, Long> clienteDao = Interventix_.getDbHelper().getRuntimeClienteDao();
	
	clienteDao.update(InterventoController.controller.getCliente());
	
	RuntimeExceptionDao<DettaglioIntervento, Long> dettaglioDao = Interventix_.getDbHelper().getRuntimeDettaglioInterventoDao();
	
	for (DettaglioIntervento dettaglio : InterventoController.controller.getListaDettagli())
	    dettaglioDao.update(dettaglio);
	
	Interventix_.releaseDbHelper();
    }
    
    // public static void putDettaglioOnDB(AsyncQueryHandler queryHandler) {
    //
    // // *** salvataggio dati dettagli - inizio ***\\
    //
    // for (DettaglioIntervento dettaglio : controller.getListaDettagli()) {
    //
    // if (dettaglio.nuovo &&
    // dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
    //
    // ContentValues valuesDettaglio = DettaglioIntervento.insertSQL(dettaglio,
    // false, dettaglio.nuovo);
    //
    // queryHandler.startInsert(Constants.TOKEN_SAVE_DETTAGLIO, null,
    // Data.CONTENT_URI, valuesDettaglio);
    // }
    // else {
    //
    // ContentValues valuesDettaglio = DettaglioIntervento.updateSQL(dettaglio,
    // false, false);
    //
    // String selectionDettaglio = Fields.TYPE + "=? AND " +
    // DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "=?";
    //
    // String[] selectionArgsDettaglio = new String[] {
    // DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" +
    // dettaglio.iddettagliointervento
    // };
    //
    // queryHandler.startUpdate(Constants.TOKEN_SAVE_DETTAGLIO, null,
    // Data.CONTENT_URI, valuesDettaglio, selectionDettaglio,
    // selectionArgsDettaglio);
    // }
    // }
    //
    // // *** salvataggio dati dettagli - fine ***\\
    // }
}
