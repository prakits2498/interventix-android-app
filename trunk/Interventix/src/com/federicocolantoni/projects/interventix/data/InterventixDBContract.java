package com.federicocolantoni.projects.interventix.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by federico on 19/05/13.
 */
public class InterventixDBContract {
    
    public final static String AUTHORITY = "com.federicocolantoni.projects.interventix";
    final static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
    
    private InterventixDBContract() {
    
    }
    
    public static class Data {
	
	public static final String PATH = "interventix";
	public static final String DB_TABLE = PATH;
	public final static Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
	final static String MIME_TYPE = "vnd.com.federicocolantoni." + PATH;
	public final static String COLLECTION_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "\\" + MIME_TYPE;
	public final static String SINGLE_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "\\" + MIME_TYPE;
	
	public static class Fields {
	    
	    public final static String _ID = BaseColumns._ID;
	    public final static String TYPE = "type";
	    public final static String INDEX = "indexed";
	    public final static String[] DATA = {
	    "data0", "data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9", "data10", "data11", "data12", "data13", "data14", "data15", "data16", "data17", "data18", "data19", "data20", "data21", "data22", "data23", "data24", "data25", "data26", "data27"
	    };
	    public final static String[] BLOB = {
	    "blob0", "blob1", "blob2"
	    };
	}
    }
    
    public static class UtenteDB extends Data {
	
	public static final String UTENTE_ITEM_TYPE = Data.SINGLE_ITEM_TYPE + ".utente";
	
	public static class Fields extends Data.Fields {
	    
	    public static final String NOME = DATA[0];
	    public static final String COGNOME = DATA[1];
	    public static final String USERNAME = DATA[2];
	    public static final String PASSWORD = DATA[3];
	    public static final String EMAIL = DATA[4];
	    public static final String TIPO = DATA[5];
	    public static final String CANCELLATO = DATA[6];
	    public static final String REVISIONE = DATA[7];
	    public static final String ID_UTENTE = DATA[8];
	    public static final String CESTINATO = DATA[9];
	    public static final String CONFLITTO = DATA[26];
	    public static final String MODIFICATO = DATA[27];
	}
    }
    
    public static class ClienteDB extends Data {
	
	public static final String CLIENTE_ITEM_TYPE = Data.SINGLE_ITEM_TYPE + ".cliente";
	
	public static class Fields extends Data.Fields {
	    
	    public static final String NOMINATIVO = DATA[0];
	    public static final String PARTITAIVA = DATA[1];
	    public static final String TELEFONO = DATA[2];
	    public static final String CITTA = DATA[3];
	    public static final String EMAIL = DATA[4];
	    public static final String REFERENTE = DATA[5];
	    public static final String FAX = DATA[6];
	    public static final String REVISIONE = DATA[7];
	    public static final String ID_CLIENTE = DATA[8];
	    public static final String CANCELLATO = DATA[9];
	    public static final String CODICE_FISCALE = DATA[10];
	    public static final String NOTE = DATA[11];
	    public static final String INDIRIZZO = DATA[12];
	    public static final String UFFICIO = DATA[13];
	    public static final String INTERNO = DATA[14];
	    public static final String CONFLITTO = DATA[26];
	    public static final String MODIFICATO = DATA[27];
	    
	}
    }
    
    public static class InterventoDB extends Data {
	
	public static final String INTERVENTO_ITEM_TYPE = Data.SINGLE_ITEM_TYPE + ".intervento";
	
	public static class Fields extends Data.Fields {
	    
	    public static final String TIPOLOGIA = DATA[0];
	    public static final String PRODOTTO = DATA[1];
	    public static final String MOTIVO = DATA[2];
	    public static final String NOMINATIVO = DATA[3];
	    public static final String RIFERIMENTO_FATTURA = DATA[4];
	    public static final String RIFERIMENTO_SCONTRINO = DATA[5];
	    public static final String NOTE = DATA[6];
	    public static final String FIRMA = BLOB[0];
	    public static final String SALDATO = DATA[7];
	    public static final String CANCELLATO = DATA[8];
	    public static final String DATA_ORA = DATA[9];
	    public static final String NUMERO_INTERVENTO = DATA[10];
	    public static final String CLIENTE = DATA[11];
	    public static final String TECNICO = DATA[12];
	    public static final String COSTO_MANODOPERA = DATA[13];
	    public static final String COSTO_COMPONENTI = DATA[14];
	    public static final String COSTO_ACCESSORI = DATA[15];
	    public static final String IMPORTO = DATA[16];
	    public static final String TOTALE = DATA[17];
	    public static final String IVA = DATA[18];
	    public static final String CHIUSO = DATA[19];
	    public static final String ID_INTERVENTO = DATA[20];
	    public static final String MODALITA = DATA[21];
	    public static final String CONFLITTO = DATA[26];
	    public static final String MODIFICATO = DATA[27];
	}
    }
    
    public static class DettaglioInterventoDB extends Data {
	
	public static final String DETTAGLIO_INTERVENTO_ITEM_TYPE = Data.SINGLE_ITEM_TYPE + ".dettaglio_intervento";
	
	public static class Fields extends Data.Fields {
	    
	    public static final String ID_DETTAGLIO_INTERVENTO = DATA[0];
	    public static final String TIPO = DATA[1];
	    public static final String OGGETTO = DATA[2];
	    public static final String DESCRIZIONE = DATA[3];
	    public static final String INTERVENTO = DATA[4];
	    public static final String INIZIO = DATA[5];
	    public static final String FINE = DATA[6];
	    public static final String TECNICI = DATA[7];
	    public static final String MODIFICATO = DATA[27];
	}
    }
    
    public static class RipristinoInterventoDB extends Data {
	public static final String RIPRISTINO_INTERVENTO_ITEM_TYPE = Data.SINGLE_ITEM_TYPE + ".ripristino_intervento";
	
	public static class Field extends Data.Fields {
	    
	    public static final String BACKUP_INTERVENTO = DATA[0];
	}
    }
}
