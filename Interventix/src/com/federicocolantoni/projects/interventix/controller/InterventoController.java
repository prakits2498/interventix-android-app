package com.federicocolantoni.projects.interventix.controller;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.net.Uri;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.Interventix_;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;

public class InterventoController {
    
    public static InterventoSingleton controller;
    
    public static void insertOnDB() {
    
	AsyncQueryHandler saveOnDB = new AsyncQueryHandler(Interventix_.getContext().getContentResolver()) {
	    
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
			
			System.out.println("Dettaglio aggiunto correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_INTERVENTO:
			
			System.out.println("Intervento aggiunto correttamente sul DB\n" + uri.toString());
			
			break;
		}
	    }
	};
	
	// *** salvataggio dati tecnico - inizio ***\\
	
	ContentValues valuesUtente = Utente.insertSQL(controller.getTecnico());
	
	saveOnDB.startInsert(Constants.TOKEN_SAVE_TECNICO, null, UtenteDB.CONTENT_URI, valuesUtente);
	
	// *** salvataggio dati tecnico - fine ***\\
	
	// *** salvataggio dati cliente - inizio ***\\
	
	ContentValues valuesCliente = Cliente.insertSQL(controller.getCliente());
	
	saveOnDB.startInsert(Constants.TOKEN_SAVE_CLIENTE, null, ClienteDB.CONTENT_URI, valuesCliente);
	
	// *** salvataggio dati cliente - fine ***\\
	
	// *** salvataggio dati dettagli - inizio ***\\
	
	for (DettaglioIntervento dettaglio : controller.getListaDettagli()) {
	    
	    ContentValues valuesDettaglio = DettaglioIntervento.insertSQL(dettaglio);
	    
	    saveOnDB.startInsert(Constants.TOKEN_SAVE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, valuesDettaglio);
	}
	
	// *** salvataggio dati dettagli - fine ***\\
	
	// *** salvataggio dati intervento - inizio ***\\
	
	ContentValues valuesIntervento = Intervento.insertSQL(controller.getIntervento(), false);
	valuesIntervento.put(InterventoDB.Fields.CHIUSO, false);
	
	saveOnDB.startInsert(Constants.TOKEN_SAVE_INTERVENTO, null, InterventoDB.CONTENT_URI, valuesIntervento);
	
	// *** salvataggio dati intervento - fine ***\\
    }
    
    public static void updateOnDB() {
    
	AsyncQueryHandler updateOnDB = new AsyncQueryHandler(Interventix_.getContext().getContentResolver()) {
	    
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
	
	String selectionTecnico = UtenteDB.Fields.TYPE + "=? AND " + UtenteDB.Fields.ID_UTENTE + "=?";
	
	String[] selectionArgsTecnico = new String[] {
	UtenteDB.UTENTE_ITEM_TYPE, "" + controller.getTecnico().getIdUtente()
	};
	
	updateOnDB.startUpdate(Constants.TOKEN_SAVE_TECNICO, null, UtenteDB.CONTENT_URI, valuesUtente, selectionTecnico, selectionArgsTecnico);
	
	// *** salvataggio dati tecnico - fine ***\\
	
	// *** salvataggio dati cliente - inizio ***\\
	
	ContentValues valuesCliente = Cliente.updateSQL(controller.getCliente());
	
	String selectionCliente = ClienteDB.Fields.TYPE + "=? AND " + ClienteDB.Fields.ID_CLIENTE + "=?";
	
	String[] selectionArgsCliente = new String[] {
	ClienteDB.CLIENTE_ITEM_TYPE, "" + controller.getCliente().getIdCliente()
	};
	
	updateOnDB.startUpdate(Constants.TOKEN_SAVE_CLIENTE, null, ClienteDB.CONTENT_URI, valuesCliente, selectionCliente, selectionArgsCliente);
	
	// *** salvataggio dati cliente - fine ***\\
	
	// *** salvataggio dati dettagli - inizio ***\\
	
	for (DettaglioIntervento dettaglio : controller.getListaDettagli()) {
	    
	    ContentValues valuesDettaglio = DettaglioIntervento.updateSQL(dettaglio);
	    
	    String selectionDettaglio = DettaglioInterventoDB.Fields.TYPE + "=? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "=?";
	    
	    String[] selectionArgsDettaglio = new String[] {
	    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + dettaglio.getIdDettaglioIntervento()
	    };
	    
	    updateOnDB.startUpdate(Constants.TOKEN_SAVE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, valuesDettaglio, selectionDettaglio, selectionArgsDettaglio);
	}
	
	// *** salvataggio dati dettagli - fine ***\\
	
	// *** salvataggio dati intervento - inizio ***\\
	
	ContentValues valuesIntervento = Intervento.updateSQL(controller.getIntervento(), false);
	valuesIntervento.put(InterventoDB.Fields.CHIUSO, false);
	
	String selectionIntervento = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?";
	
	String[] selectionArgsIntervento = new String[] {
	InterventoDB.INTERVENTO_ITEM_TYPE, "" + controller.getIntervento().getIdIntervento()
	};
	
	updateOnDB.startUpdate(Constants.TOKEN_SAVE_INTERVENTO, null, InterventoDB.CONTENT_URI, valuesIntervento, selectionIntervento, selectionArgsIntervento);
	
	// *** salvataggio dati intervento - fine ***\\
    }
}
