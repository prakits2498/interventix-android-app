package com.federicocolantoni.projects.interventix.service;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.Intervento;

public class InterventixService extends IntentService {
    
    public InterventixService() {
    
	super("InterventixService");
    }
    
    @Override
    public void onCreate() {
    
	super.onCreate();
	
	System.out.println(this.getClass().getSimpleName() + " started");
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
    
	if (intent.getAction().equals(Constants.ACTION_GET_INTERVENTI)) {
	    
	    System.out.println("Action " + Constants.ACTION_GET_INTERVENTI);
	    
	    inviaInterventi();
	}
	else
	    if (intent.getAction().equals(Constants.ACTION_GET_CLIENTI)) {
		
		System.out.println("Action " + Constants.ACTION_GET_CLIENTI);
		
		inviaClienti();
	    }
    }
    
    private void inviaClienti() {
    
	ContentResolver cr = getContentResolver();
	
	System.out.println(this.getClass().getSimpleName() + " - inviaClienti");
	
	String[] projection = new String[] {
	Fields._ID, ClienteDB.Fields.ID_CLIENTE, ClienteDB.Fields.CANCELLATO, ClienteDB.Fields.CITTA, ClienteDB.Fields.CODICE_FISCALE, ClienteDB.Fields.CONFLITTO, ClienteDB.Fields.EMAIL, ClienteDB.Fields.FAX, ClienteDB.Fields.INDIRIZZO, ClienteDB.Fields.INTERNO, ClienteDB.Fields.NOMINATIVO, ClienteDB.Fields.NOTE, ClienteDB.Fields.PARTITAIVA, ClienteDB.Fields.REFERENTE, ClienteDB.Fields.REVISIONE, ClienteDB.Fields.TELEFONO, ClienteDB.Fields.UFFICIO
	};
	
	// String selection = ClienteDB.Fields.TYPE + "=? AND (" +
	// ClienteDB.Fields.MODIFICATO + "=? OR " + ClienteDB.Fields.ID_CLIENTE
	// + "<?)";
	//
	// String[] selectionArgs = new String[] {
	// ClienteDB.CLIENTE_ITEM_TYPE,
	// "M",
	// "0"
	// };
	
	String selection = Fields.TYPE + "=?";
	
	String[] selectionArgs = new String[] {
	    ClienteDB.CLIENTE_ITEM_TYPE
	};
	
	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
	
	if (cursor != null) {
	    
	    System.out.println("Size result set: " + cursor.getCount());
	    
	    while (cursor.moveToNext()) {
		
		Cliente newCliente = Cliente.getFromCursor(cursor);
		
		System.out.println(Cliente.class.getSimpleName() + ": " + newCliente.getNominativo());
	    }
	    
	    if (!cursor.isClosed()) {
		cursor.close();
	    }
	    else {
		System.out.println("Cursor for inviaClienti is closed");
	    }
	}
	else
	    System.out.println("Errore nel recupero dei dati dal DB");
    }
    
    private void inviaInterventi() {
    
	ContentResolver cr = getContentResolver();
	
	System.out.println(this.getClass().getSimpleName() + " - inviaInterventi");
	
	String[] projection = new String[] {
	Fields._ID, InterventoDB.Fields.CANCELLATO, InterventoDB.Fields.CHIUSO, InterventoDB.Fields.CLIENTE, InterventoDB.Fields.CONFLITTO, InterventoDB.Fields.COSTO_ACCESSORI, InterventoDB.Fields.COSTO_COMPONENTI, InterventoDB.Fields.COSTO_MANODOPERA, InterventoDB.Fields.DATA_ORA, InterventoDB.Fields.FIRMA, InterventoDB.Fields.ID_INTERVENTO, InterventoDB.Fields.IMPORTO, InterventoDB.Fields.IVA, InterventoDB.Fields.MODALITA, InterventoDB.Fields.MODIFICATO, InterventoDB.Fields.MOTIVO, InterventoDB.Fields.NOMINATIVO, InterventoDB.Fields.NOTE, InterventoDB.Fields.NUMERO_INTERVENTO, InterventoDB.Fields.PRODOTTO, InterventoDB.Fields.RIFERIMENTO_FATTURA, InterventoDB.Fields.RIFERIMENTO_SCONTRINO, InterventoDB.Fields.SALDATO, InterventoDB.Fields.TECNICO, InterventoDB.Fields.TIPOLOGIA, InterventoDB.Fields.TOTALE
	};
	
	String selection = Fields.TYPE + "=? AND " + InterventoDB.Fields.MODIFICATO + " IN(?,?)";
	
	String[] selectionArgs = new String[] {
	InterventoDB.INTERVENTO_ITEM_TYPE, Constants.INTERVENTO_MODIFICATO, Constants.INTERVENTO_NUOVO
	};
	
	Cursor cursor = cr.query(Data.CONTENT_URI, projection, selection, selectionArgs, null);
	
	if (cursor != null) {
	    
	    System.out.println("Size result set: " + cursor.getCount());
	    
	    while (cursor.moveToNext()) {
		
		Intervento newIntervento = Intervento.getFromCursor(cursor);
		
		System.out.println(Intervento.class.getSimpleName() + " " + newIntervento.getNumeroIntervento());
	    }
	    
	    if (!cursor.isClosed()) {
		cursor.close();
	    }
	    else {
		System.out.println("Cursor for inviaInterventi is closed");
	    }
	}
	else
	    System.out.println("Errore nel recupero dei dati dal DB");
    }
}
