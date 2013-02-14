
package com.federicocolantoni.projects.interventix.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InterventixAPI {

    public final static String AUTHORITY = "com.federico.colantoni.projects.interventix.cliente";

    final static Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Cliente {

	final static String PATH = "cliente";
	public final static String MIME_TYPE = "vnd.com.federico.colantoni.projects.interventix"
		+ PATH;
	public final static Uri URI = BASE_URI.buildUpon().appendPath(PATH)
		.build();

	public final static String TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
		+ '/' + MIME_TYPE;
	public final static String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
		+ '/' + MIME_TYPE;

	public static final class Fields {

	    public static final String KEY_ROW_ID = BaseColumns._ID;
	    public static final String KEY_ID_CLIENTE = "idcliente";
	    public static final String KEY_NOMINATIVO = "nominativo";
	    public static final String KEY_CODICE_FISCALE = "cod_fis";
	    public static final String KEY_PARTITA_IVA = "partita_iva";
	    public static final String KEY_TELEFONO = "telefono";
	    public static final String KEY_FAX = "fax";
	    public static final String KEY_EMAIL = "email";
	    public static final String KEY_REFERENTE = "referente";
	    public static final String KEY_CITTA = "citta";
	    public static final String KEY_INDIRIZZO = "indirizzo";
	    public static final String KEY_INTERNO = "interno";
	    public static final String KEY_UFFICIO = "ufficio";
	    public static final String KEY_NOTE = "note";
	    public static final String KEY_CANCELLATO = "cancellato";
	    public static final String KEY_REVISIONE = "revisione";
	}
    }
}
