
package com.federicocolantoni.projects.interventix.intervento;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClienteDBAdapter {

    private static final String DEBUG_TAG = "INTERVENTIX";

    static final String DATABASE_NAME = "interventix";
    static final String DATABASE_TABLE = "cliente";
    static final int DATABASE_VERSION = 1;

    static final String KEY_ROW_ID = "_id";
    static final String KEY_ID_CLIENTE = "idcliente";
    static final String KEY_NOMINATIVO = "nominativo";
    static final String KEY_CODICE_FISCALE = "cod_fis";
    static final String KEY_PARTITA_IVA = "partita_iva";
    static final String KEY_TELEFONO = "telefono";
    static final String KEY_FAX = "fax";
    static final String KEY_EMAIL = "email";
    static final String KEY_REFERENTE = "referente";
    static final String KEY_CITTA = "citta";
    static final String KEY_INDIRIZZO = "indirizzo";
    static final String KEY_INTERNO = "interno";
    static final String KEY_UFFICIO = "ufficio";
    static final String KEY_NOTE = "note";
    static final String KEY_CANCELLATO = "cancellato";
    static final String KEY_REVISIONE = "revisione";

    static final String TABLE_CREATE = "DROP TABLE IF EXISTS cliente;\n"
	    + "CREATE TABLE "
	    + DATABASE_TABLE
	    + " (\n"
	    + KEY_ROW_ID
	    + " int(11) NOT NULL AUTO_INCREMENT,\n"
	    + KEY_ID_CLIENTE
	    + " int(11) AUTO_INCREMENT=14 UNIQUE,\n"
	    + KEY_NOMINATIVO
	    + " varchar(255) NOT NULL,\n"
	    + KEY_CODICE_FISCALE
	    + " varchar(255) NOT NULL UNIQUE,\n"
	    + KEY_PARTITA_IVA
	    + " varchar(255) NOT NULL UNIQUE,\n"
	    + KEY_TELEFONO
	    + " varchar(255) NOT NULL,\n"
	    + KEY_FAX
	    + " varchar(255) DEFAULT NULL,\n"
	    + KEY_EMAIL
	    + " varchar(255) DEFAULT NULL,\n"
	    + KEY_REFERENTE
	    + " varchar(255) DEFAULT NULL,\n"
	    + KEY_CITTA
	    + " varchar(255) NOT NULL,\n"
	    + KEY_INDIRIZZO
	    + " varchar(255) NOT NULL,\n"
	    + KEY_INTERNO
	    + " varchar(255) DEFAULT NULL,\n"
	    + KEY_UFFICIO
	    + " varchar(255) DEFAULT NULL,\n"
	    + KEY_NOTE
	    + " text,\n"
	    + KEY_CANCELLATO
	    + " tinyint(1) NOT NULL DEFAULT '0',\n"
	    + KEY_REVISIONE
	    + " int(11) DEFAULT '1',\n"
	    + "PRIMARY KEY (_id)\n"
	    + ");";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase mDB;

    public ClienteDBAdapter(Context ctx) {

	context = ctx;
	DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {

	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	    try {
		db.execSQL(TABLE_CREATE);
	    } catch (SQLException e) {
		Log.d(DEBUG_TAG, "ERROR TABLE " + DATABASE_TABLE + " CREATION",
			e);
	    }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	    Log.d(DEBUG_TAG, "Upgrading database from version " + oldVersion
		    + " to " + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS cliente");
	    onCreate(db);
	}
    }

    //---opens the database---
    public ClienteDBAdapter open() throws SQLException {

	mDB = DBHelper.getWritableDatabase();
	return this;
    }

    //---closes the database---
    public void close() {

	DBHelper.close();
    }

    //---insert a client into the database---
    public long insertCliente(Integer idCliente, String nominativo,
	    String codFis, String partitaIva, String telefono, String fax,
	    String email, String referente, String citta, String indirizzo,
	    String interno, String ufficio, String note, boolean cancellato,
	    int revisione) {

	ContentValues initialValues = new ContentValues();
	initialValues.put(KEY_ID_CLIENTE, idCliente);
	initialValues.put(KEY_NOMINATIVO, nominativo);
	initialValues.put(KEY_CODICE_FISCALE, codFis);
	initialValues.put(KEY_PARTITA_IVA, partitaIva);
	initialValues.put(KEY_TELEFONO, telefono);
	initialValues.put(KEY_FAX, fax);
	initialValues.put(KEY_EMAIL, email);
	initialValues.put(KEY_REFERENTE, referente);
	initialValues.put(KEY_CITTA, citta);
	initialValues.put(KEY_INDIRIZZO, indirizzo);
	initialValues.put(KEY_INTERNO, interno);
	initialValues.put(KEY_UFFICIO, ufficio);
	initialValues.put(KEY_NOTE, note);
	initialValues.put(KEY_CANCELLATO, cancellato);
	initialValues.put(KEY_REVISIONE, revisione);

	return mDB.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular client---
    public boolean deleteContact(Integer idCliente) {

	return mDB.delete(DATABASE_TABLE, KEY_ID_CLIENTE + "=" + idCliente,
		null) > 0;
    }

    //---retrieves all the clients---
    public Cursor getAllContacts() {

	return mDB.query(DATABASE_TABLE, new String[] { KEY_ID_CLIENTE,
		KEY_NOMINATIVO, KEY_CODICE_FISCALE, KEY_PARTITA_IVA,
		KEY_TELEFONO, KEY_FAX, KEY_EMAIL, KEY_REFERENTE, KEY_CITTA,
		KEY_INDIRIZZO, KEY_INTERNO, KEY_UFFICIO, KEY_NOTE,
		KEY_CANCELLATO, KEY_REVISIONE }, null, null, null, null,
		KEY_NOMINATIVO);
    }

    //---retrieves a particular client---
    public Cursor getClient(Integer idCliente) throws SQLException {

	Cursor mCursor = mDB.query(true, DATABASE_TABLE, null, KEY_ID_CLIENTE
		+ "=" + idCliente, null, null, null, null, null);
	if (mCursor != null) {
	    mCursor.moveToFirst();
	}
	return mCursor;
    }

    //---updates a client---
    public boolean updateContact(Integer idCliente, String nominativo,
	    String codFis, String partitaIva, String telefono, String fax,
	    String email, String referente, String citta, String indirizzo,
	    String interno, String ufficio, String note, boolean cancellato,
	    int revisione) {

	ContentValues args = new ContentValues();
	args.put(KEY_NOMINATIVO, nominativo);
	args.put(KEY_CODICE_FISCALE, codFis);
	args.put(KEY_PARTITA_IVA, partitaIva);
	args.put(KEY_TELEFONO, telefono);
	args.put(KEY_FAX, fax);
	args.put(KEY_EMAIL, email);
	args.put(KEY_REFERENTE, referente);
	args.put(KEY_CITTA, citta);
	args.put(KEY_INDIRIZZO, indirizzo);
	args.put(KEY_INTERNO, interno);
	args.put(KEY_UFFICIO, ufficio);
	args.put(KEY_NOTE, note);
	args.put(KEY_CANCELLATO, cancellato);
	args.put(KEY_REVISIONE, revisione);
	return mDB.update(DATABASE_TABLE, args, KEY_ROW_ID + "=" + idCliente,
		null) > 0;
    }
}
