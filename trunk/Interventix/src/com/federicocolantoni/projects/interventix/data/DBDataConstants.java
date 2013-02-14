
package com.federicocolantoni.projects.interventix.data;

import android.content.UriMatcher;

final class DBDataConstants {

    private DBDataConstants() {

    }

    static final String DATABASE_NAME = "interventix";
    static final int DATABASE_VERSION = 1;

    static final String TABLE_CREATE = "CREATE TABLE IF EXISTS "
	    + InterventixAPI.Cliente.PATH + " ("
	    + InterventixAPI.Cliente.Fields.KEY_ROW_ID
	    + " INTEGER PRIMARY KEY,"
	    + InterventixAPI.Cliente.Fields.KEY_ID_CLIENTE + " INTEGER UNIQUE,"
	    + InterventixAPI.Cliente.Fields.KEY_NOMINATIVO
	    + " VARCHAR(255) NOT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_CODICE_FISCALE
	    + " VARCHAR(255) NOT NULL UNIQUE,"
	    + InterventixAPI.Cliente.Fields.KEY_PARTITA_IVA
	    + " VARCHAR(255) NOT NULL UNIQUE,"
	    + InterventixAPI.Cliente.Fields.KEY_TELEFONO
	    + " VARCHAR(255) NOT NULL," + InterventixAPI.Cliente.Fields.KEY_FAX
	    + " VARCHAR(255) DEFAULT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_EMAIL
	    + " VARCHAR(255) DEFAULT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_REFERENTE
	    + " VARCHAR(255) DEFAULT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_CITTA
	    + " VARCHAR(255) NOT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_INDIRIZZO
	    + " VARCHAR(255) NOT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_INTERNO
	    + " VARCHAR(255) DEFAULT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_UFFICIO
	    + " VARCHAR(255) DEFAULT NULL,"
	    + InterventixAPI.Cliente.Fields.KEY_NOTE + " TEXT,"
	    + InterventixAPI.Cliente.Fields.KEY_CANCELLATO
	    + " INTEGER NOT NULL DEFAULT '0',"
	    + InterventixAPI.Cliente.Fields.KEY_REVISIONE
	    + " INTEGER DEFAULT '1'" + ");";

    static final String TABLE_DROP = "DROP TABLE IF EXISTS "
	    + InterventixAPI.Cliente.PATH;

    final static int CLIENT_COLLECTION = 1;
    final static int CLIENT_ITEM = 2;
    final static UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
	MATCHER.addURI(InterventixAPI.AUTHORITY, InterventixAPI.Cliente.PATH,
		CLIENT_COLLECTION);
	MATCHER.addURI(InterventixAPI.AUTHORITY, InterventixAPI.Cliente.PATH
		+ "/#", CLIENT_ITEM);
    }
}
