package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Clienti")
public class Cliente implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6163117197009443000L;

    @DatabaseField(canBeNull = true, useGetSet = false, id = true)
    public Long idcliente;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public Long revisione;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public String nominativo, codicefiscale, partitaiva, telefono, fax, email, referente, citta, indirizzo, interno, ufficio, note;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public boolean cancellato, conflitto;

    public Cliente() {

	idcliente = 0L;
	revisione = 0L;
	nominativo = new String();
	codicefiscale = new String();
	partitaiva = new String();
	telefono = new String();
	fax = new String();
	email = new String();
	referente = new String();
	citta = new String();
	indirizzo = new String();
	interno = new String();
	ufficio = new String();
	note = new String();
	cancellato = false;
	conflitto = false;
    }

    @Override
    public String toString() {

	return String
		.format("Cliente [idcliente=%s, nominativo=%s, codicefiscale=%s, partitaiva=%s, telefono=%s, fax=%s, email=%s, referente=%s, citta=%s, indirizzo=%s, interno=%s, ufficio=%s, note=%s, cancellato=%s, conflitto=%s, revisione=%s]",
			idcliente, nominativo, codicefiscale, partitaiva, telefono, fax, email, referente, citta, indirizzo, interno, ufficio, note, cancellato, conflitto, revisione);
    }

    @Override
    public int hashCode() {

	final int prime = 31;
	int result = 1;
	result = prime * result + (cancellato ? 1231 : 1237);
	result = prime * result + (citta == null ? 0 : citta.hashCode());
	result = prime * result + (codicefiscale == null ? 0 : codicefiscale.hashCode());
	result = prime * result + (conflitto ? 1231 : 1237);
	result = prime * result + (email == null ? 0 : email.hashCode());
	result = prime * result + (fax == null ? 0 : fax.hashCode());
	result = prime * result + (idcliente == null ? 0 : idcliente.hashCode());
	result = prime * result + (indirizzo == null ? 0 : indirizzo.hashCode());
	result = prime * result + (interno == null ? 0 : interno.hashCode());
	result = prime * result + (nominativo == null ? 0 : nominativo.hashCode());
	result = prime * result + (note == null ? 0 : note.hashCode());
	result = prime * result + (partitaiva == null ? 0 : partitaiva.hashCode());
	result = prime * result + (referente == null ? 0 : referente.hashCode());
	result = prime * result + (revisione == null ? 0 : revisione.hashCode());
	result = prime * result + (telefono == null ? 0 : telefono.hashCode());
	result = prime * result + (ufficio == null ? 0 : ufficio.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {

	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Cliente)) {
	    return false;
	}
	Cliente other = (Cliente) obj;
	if (cancellato != other.cancellato) {
	    return false;
	}
	if (citta == null) {
	    if (other.citta != null) {
		return false;
	    }
	}
	else if (!citta.equals(other.citta)) {
	    return false;
	}
	if (codicefiscale == null) {
	    if (other.codicefiscale != null) {
		return false;
	    }
	}
	else if (!codicefiscale.equals(other.codicefiscale)) {
	    return false;
	}
	if (conflitto != other.conflitto) {
	    return false;
	}
	if (email == null) {
	    if (other.email != null) {
		return false;
	    }
	}
	else if (!email.equals(other.email)) {
	    return false;
	}
	if (fax == null) {
	    if (other.fax != null) {
		return false;
	    }
	}
	else if (!fax.equals(other.fax)) {
	    return false;
	}
	if (idcliente == null) {
	    if (other.idcliente != null) {
		return false;
	    }
	}
	else if (!idcliente.equals(other.idcliente)) {
	    return false;
	}
	if (indirizzo == null) {
	    if (other.indirizzo != null) {
		return false;
	    }
	}
	else if (!indirizzo.equals(other.indirizzo)) {
	    return false;
	}
	if (interno == null) {
	    if (other.interno != null) {
		return false;
	    }
	}
	else if (!interno.equals(other.interno)) {
	    return false;
	}
	if (nominativo == null) {
	    if (other.nominativo != null) {
		return false;
	    }
	}
	else if (!nominativo.equals(other.nominativo)) {
	    return false;
	}
	if (note == null) {
	    if (other.note != null) {
		return false;
	    }
	}
	else if (!note.equals(other.note)) {
	    return false;
	}
	if (partitaiva == null) {
	    if (other.partitaiva != null) {
		return false;
	    }
	}
	else if (!partitaiva.equals(other.partitaiva)) {
	    return false;
	}
	if (referente == null) {
	    if (other.referente != null) {
		return false;
	    }
	}
	else if (!referente.equals(other.referente)) {
	    return false;
	}
	if (revisione == null) {
	    if (other.revisione != null) {
		return false;
	    }
	}
	else if (!revisione.equals(other.revisione)) {
	    return false;
	}
	if (telefono == null) {
	    if (other.telefono != null) {
		return false;
	    }
	}
	else if (!telefono.equals(other.telefono)) {
	    return false;
	}
	if (ufficio == null) {
	    if (other.ufficio != null) {
		return false;
	    }
	}
	else if (!ufficio.equals(other.ufficio)) {
	    return false;
	}
	return true;
    }

    public static ContentValues insertSQL(Cliente cliente) {

	ContentValues values = new ContentValues();

	values.put(ClienteDB.Fields.ID_CLIENTE, cliente.idcliente);
	values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
	values.put(ClienteDB.Fields.CITTA, cliente.citta);
	values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.codicefiscale);
	values.put(ClienteDB.Fields.CONFLITTO, cliente.conflitto);
	values.put(ClienteDB.Fields.EMAIL, cliente.email);
	values.put(ClienteDB.Fields.FAX, cliente.fax);
	values.put(ClienteDB.Fields.INDIRIZZO, cliente.indirizzo);
	values.put(ClienteDB.Fields.INTERNO, cliente.interno);
	values.put(ClienteDB.Fields.NOMINATIVO, cliente.nominativo);
	values.put(ClienteDB.Fields.NOTE, cliente.note);
	values.put(ClienteDB.Fields.PARTITAIVA, cliente.partitaiva);
	values.put(ClienteDB.Fields.REFERENTE, cliente.referente);
	values.put(ClienteDB.Fields.REVISIONE, cliente.revisione);
	values.put(ClienteDB.Fields.TELEFONO, cliente.telefono);
	values.put(ClienteDB.Fields.UFFICIO, cliente.ufficio);

	return values;
    }

    public static ContentValues updateSQL(Cliente cliente) {

	ContentValues values = new ContentValues();

	values.put(ClienteDB.Fields.CITTA, cliente.citta);
	values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.codicefiscale);
	values.put(ClienteDB.Fields.CONFLITTO, cliente.conflitto);
	values.put(ClienteDB.Fields.EMAIL, cliente.email);
	values.put(ClienteDB.Fields.FAX, cliente.fax);
	values.put(ClienteDB.Fields.INDIRIZZO, cliente.indirizzo);
	values.put(ClienteDB.Fields.INTERNO, cliente.interno);
	values.put(ClienteDB.Fields.NOMINATIVO, cliente.nominativo);
	values.put(ClienteDB.Fields.NOTE, cliente.note);
	values.put(ClienteDB.Fields.PARTITAIVA, cliente.partitaiva);
	values.put(ClienteDB.Fields.REFERENTE, cliente.referente);
	values.put(ClienteDB.Fields.REVISIONE, cliente.revisione);
	values.put(ClienteDB.Fields.TELEFONO, cliente.telefono);
	values.put(ClienteDB.Fields.UFFICIO, cliente.ufficio);

	return values;
    }

    public static Cliente getFromCursor(Cursor cursor) {

	Cliente cliente = new Cliente();

	cliente.cancellato = (cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CANCELLATO)) == 1 ? true : false);
	cliente.citta = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CITTA)));
	cliente.codicefiscale = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
	cliente.conflitto = (cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CONFLITTO)) == 1 ? true : false);
	cliente.email = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.EMAIL)));
	cliente.fax = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.FAX)));
	cliente.idcliente = (cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE)));
	cliente.indirizzo = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INDIRIZZO)));
	cliente.interno = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INTERNO)));
	cliente.nominativo = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	cliente.note = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOTE)));
	cliente.partitaiva = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));
	cliente.referente = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.REFERENTE)));
	cliente.revisione = (cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.REVISIONE)));
	cliente.telefono = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.TELEFONO)));
	cliente.ufficio = (cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.UFFICIO)));

	return cliente;
    }
}
