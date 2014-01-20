package com.federicocolantoni.projects.interventix.controller;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;

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
    
    public static InterventoSingleton_ controller;
    
    public static void saveOnDB() {
    
	AsyncQueryHandler saveOnDB = new AsyncQueryHandler(Interventix_.getContext().getContentResolver()) {
	    
	    @Override
	    protected void onUpdateComplete(int token, Object cookie, int result) {
	    
		switch (token) {
		    case Constants.TOKEN_SAVE_TECNICO:
			
			System.out.println("Tecnico salvato correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_CLIENTE:
			
			System.out.println("Cliente salvato correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_DETTAGLIO:
			
			System.out.println("Dettaglio salvato correttamente sul DB");
			
			break;
		    
		    case Constants.TOKEN_SAVE_INTERVENTO:
			
			System.out.println("Intervento salvato correttamente sul DB");
			
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
	
	saveOnDB.startUpdate(Constants.TOKEN_SAVE_TECNICO, null, UtenteDB.CONTENT_URI, valuesUtente, selectionTecnico, selectionArgsTecnico);
	
	// *** salvataggio dati tecnico - fine ***\\
	
	// *** salvataggio dati cliente - inizio ***\\
	
	ContentValues valuesCliente = Cliente.updateSQL(controller.getCliente());
	
	String selectionCliente = ClienteDB.Fields.TYPE + "=? AND " + ClienteDB.Fields.ID_CLIENTE + "=?";
	
	String[] selectionArgsCliente = new String[] {
	ClienteDB.CLIENTE_ITEM_TYPE, "" + controller.getCliente().getIdCliente()
	};
	
	saveOnDB.startUpdate(Constants.TOKEN_SAVE_CLIENTE, null, ClienteDB.CONTENT_URI, valuesCliente, selectionCliente, selectionArgsCliente);
	
	// *** salvataggio dati cliente - fine ***\\
	
	// *** salvataggio dati dettagli - inizio ***\\
	
	for (DettaglioIntervento dettaglio : controller.getListaDettagli()) {
	    
	    ContentValues valuesDettaglio = DettaglioIntervento.updateSQL(dettaglio);
	    
	    String selectionDettaglio = DettaglioInterventoDB.Fields.TYPE + "=? AND " + DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO + "=?";
	    
	    String[] selectionArgsDettaglio = new String[] {
	    DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE, "" + dettaglio.getIdDettaglioIntervento()
	    };
	    
	    saveOnDB.startUpdate(Constants.TOKEN_SAVE_DETTAGLIO, null, DettaglioInterventoDB.CONTENT_URI, valuesDettaglio, selectionDettaglio, selectionArgsDettaglio);
	}
	
	// *** salvataggio dati dettagli - fine ***\\
	
	// *** salvataggio dati intervento - inizio ***\\
	
	ContentValues valuesIntervento = Intervento.updateSQL(controller.getIntervento());
	valuesIntervento.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);
	valuesIntervento.put(InterventoDB.Fields.CHIUSO, false);
	
	String selectionIntervento = InterventoDB.Fields.TYPE + "=? AND " + InterventoDB.Fields.ID_INTERVENTO + "=?";
	
	String[] selectionArgsIntervento = new String[] {
	InterventoDB.INTERVENTO_ITEM_TYPE, "" + controller.getIntervento().getIdIntervento()
	};
	
	saveOnDB.startUpdate(Constants.TOKEN_SAVE_INTERVENTO, null, InterventoDB.CONTENT_URI, valuesIntervento, selectionIntervento, selectionArgsIntervento);
	
	// *** salvataggio dati intervento - fine ***\\
    }
}
