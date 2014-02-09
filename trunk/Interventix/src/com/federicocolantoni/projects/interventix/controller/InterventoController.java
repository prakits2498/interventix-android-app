package com.federicocolantoni.projects.interventix.controller;

import java.util.ArrayList;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.net.Uri;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.Interventix;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;

public class InterventoController {
    
    public static InterventoSingleton controller;
    public static long revisioneInterventi = 0L;
    public static ArrayList<Cliente> listaClienti = new ArrayList<Cliente>();
    
    public static void insertOnDB() {
    
	AsyncQueryHandler saveOnDB = new AsyncQueryHandler(Interventix.getContext().getContentResolver()) {
	    
	    @Override
	    protected void onInsertComplete(int token, Object cookie, Uri uri) {
	    
		switch (token) {
		    case Constants.TOKEN_SAVE_TECNICO:
			
			System.out.println("Tecnico aggiunto correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_CLIENTE:
			
			System.out.println("Cliente aggiunto correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_DETTAGLIO:
			
			System.out.println("Dettaglio aggiunto correttamente sul DB\n" + uri.toString());
			
			break;
		    
		    case Constants.TOKEN_SAVE_INTERVENTO:
			
			System.out.println("Intervento aggiunto correttamente sul DB\n" + uri.toString());
			
			break;
		}
	    }
	};
	
	// *** salvataggio dati tecnico - inizio ***\\
	
	ContentValues valuesUtente = Utente.insertSQL(controller.getTecnico());
	
	saveOnDB.startInsert(Constants.TOKEN_SAVE_TECNICO, null, Data.CONTENT_URI, valuesUtente);
	
	// *** salvataggio dati tecnico - fine ***\\
	
	// *** salvataggio dati cliente - inizio ***\\
	
	ContentValues valuesCliente = Cliente.insertSQL(controller.getCliente());
	
	saveOnDB.startInsert(Constants.TOKEN_SAVE_CLIENTE, null, Data.CONTENT_URI, valuesCliente);
	
	// *** salvataggio dati cliente - fine ***\\
	
	// *** salvataggio dati intervento - inizio ***\\
	
	ContentValues valuesIntervento = Intervento.insertSQL(controller.getIntervento(), false);
	valuesIntervento.put(InterventoDB.Fields.CHIUSO, false);
	
	saveOnDB.startInsert(Constants.TOKEN_SAVE_INTERVENTO, null, Data.CONTENT_URI, valuesIntervento);
	
	// *** salvataggio dati intervento - fine ***\\
	
	putDettaglioOnDB(saveOnDB);
    }
    
    public static void updateOnDB() {
    
	AsyncQueryHandler updateOnDB = new AsyncQueryHandler(Interventix.getContext().getContentResolver()) {
	    
	    @Override
	    protected void onInsertComplete(int token, Object cookie, Uri uri) {
	    
		switch (token) {
		
		    case Constants.TOKEN_SAVE_DETTAGLIO:
			
			System.out.println("Dettaglio aggiunto correttamente sul DB\n" + uri.toString());
			
			break;
		}
	    }
	    
	    @Override
	    protected void onUpdateComplete(int token, Object cookie, int result) {
	    
		switch (token) {
		    case Constants.TOKEN_SAVE_TECNICO:
			
			System.out.println("Tecnico aggiornato correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_CLIENTE:
			
			System.out.println("Cliente aggiornato correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_DETTAGLIO:
			
			System.out.println("Dettaglio aggiornato correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_INTERVENTO:
			
			System.out.println("Intervento aggiornato correttamente sul DB");
			
			break;
		}
	    }
	};
	
	// *** salvataggio dati tecnico - inizio ***\\
	
	ContentValues valuesUtente = Utente.updateSQL(controller.getTecnico());
	
	String selectionTecnico = Fields.TYPE + "=? AND " + UtenteDB.Fields.ID_UTENTE + "=?";
	
	String[] selectionArgsTecnico = new String[] {
	UtenteDB.UTENTE_ITEM_TYPE, "" + controller.getTecnico().idutente
	};
	
	updateOnDB.startUpdate(Constants.TOKEN_SAVE_TECNICO, null, Data.CONTENT_URI, valuesUtente, selectionTecnico, selectionArgsTecnico);
	
	// *** salvataggio dati tecnico - fine ***\\
	
	// *** salvataggio dati cliente - inizio ***\\
	
	ContentValues valuesCliente = Cliente.updateSQL(controller.getCliente());
	
	String selectionCliente = Fields.TYPE + "=? AND " + ClienteDB.Fields.ID_CLIENTE + "=?";
	
	String[] selectionArgsCliente = new String[] {
	ClienteDB.CLIENTE_ITEM_TYPE, "" + controller.getCliente().idcliente
	};
	
	updateOnDB.startUpdate(Constants.TOKEN_SAVE_CLIENTE, null, Data.CONTENT_URI, valuesCliente, selectionCliente, selectionArgsCliente);
	
	// *** salvataggio dati cliente - fine ***\\
	
	// *** salvataggio dati intervento - inizio ***\\
	
	ContentValues valuesIntervento = Intervento.updateSQL(controller.getIntervento(), false);
	valuesIntervento.put(InterventoDB.Fields.CHIUSO, false);
	
	String selectionIntervento = Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?";
	
	String[] selectionArgsIntervento = new String[] {
	InterventoDB.INTERVENTO_ITEM_TYPE, "" + controller.getIntervento().idintervento
	};
	
	updateOnDB.startUpdate(Constants.TOKEN_SAVE_INTERVENTO, null, Data.CONTENT_URI, valuesIntervento, selectionIntervento, selectionArgsIntervento);
	
	// *** salvataggio dati intervento - fine ***\\
	
	putDettaglioOnDB(updateOnDB);
    }
    
    public static void putDettaglioOnDB(AsyncQueryHandler queryHandler) {
    
	// *** salvataggio dati dettagli - inizio ***\\
	
	for (DettaglioIntervento dettaglio : controller.getListaDettagli()) {
	    
	    if (dettaglio.nuovo && dettaglio.modificato.equals(Constants.DETTAGLIO_NUOVO)) {
		
		ContentValues valuesDettaglio = DettaglioIntervento.insertSQL(dettaglio, false, dettaglio.nuovo);
		
		queryHandler.startInsert(Constants.TOKEN_SAVE_DETTAGLIO, null, Data.CONTENT_URI, valuesDettaglio);
	    }
	    else {
		
		ContentValues valuesDettaglio = DettaglioIntervento.updateSQL(dettaglio, false, false);
		
		String selectionDettaglio = Fields.TYPE + "=? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "=?";
		
		String[] selectionArgsDettaglio = new String[] {
		DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + dettaglio.iddettagliointervento
		};
		
		queryHandler.startUpdate(Constants.TOKEN_SAVE_DETTAGLIO, null, Data.CONTENT_URI, valuesDettaglio, selectionDettaglio, selectionArgsDettaglio);
	    }
	}
	
	// *** salvataggio dati dettagli - fine ***\\
    }
}
