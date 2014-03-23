package com.federicocolantoni.projects.interventix.models;

import java.io.Serializable;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Tecnici")
public class Utente implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -460809443185110082L;

    @DatabaseField(canBeNull = false, useGetSet = false, id = true)
    public Long idutente;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public Long revisione;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public String nome, cognome, username, password, email, tipo;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public boolean cancellato, cestinato, conflitto;

    public Utente() {

	idutente = 0L;
	revisione = 0L;
	nome = new String();
	cognome = new String();
	username = new String();
	password = new String();
	email = new String();
	tipo = new String();
	cancellato = false;
	cestinato = false;
	conflitto = false;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

	return String.format("Utente [idutente=%s, revisione=%s, nome=%s, cognome=%s, username=%s, password=%s, email=%s, tipo=%s, cancellato=%s, cestinato=%s, conflitto=%s]", idutente, revisione,
		nome, cognome, username, password, email, tipo, cancellato, cestinato, conflitto);
    }

    @Override
    public int hashCode() {

	final int prime = 31;
	int result = 1;
	result = prime * result + (cancellato ? 1231 : 1237);
	result = prime * result + (cestinato ? 1231 : 1237);
	result = prime * result + (cognome == null ? 0 : cognome.hashCode());
	result = prime * result + (conflitto ? 1231 : 1237);
	result = prime * result + (email == null ? 0 : email.hashCode());
	result = prime * result + (idutente == null ? 0 : idutente.hashCode());
	result = prime * result + (nome == null ? 0 : nome.hashCode());
	result = prime * result + (password == null ? 0 : password.hashCode());
	result = prime * result + (revisione == null ? 0 : revisione.hashCode());
	result = prime * result + (tipo == null ? 0 : tipo.hashCode());
	result = prime * result + (username == null ? 0 : username.hashCode());
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
	if (!(obj instanceof Utente)) {
	    return false;
	}
	Utente other = (Utente) obj;
	if (cancellato != other.cancellato) {
	    return false;
	}
	if (cestinato != other.cestinato) {
	    return false;
	}
	if (cognome == null) {
	    if (other.cognome != null) {
		return false;
	    }
	}
	else if (!cognome.equals(other.cognome)) {
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
	if (idutente == null) {
	    if (other.idutente != null) {
		return false;
	    }
	}
	else if (!idutente.equals(other.idutente)) {
	    return false;
	}
	if (nome == null) {
	    if (other.nome != null) {
		return false;
	    }
	}
	else if (!nome.equals(other.nome)) {
	    return false;
	}
	if (password == null) {
	    if (other.password != null) {
		return false;
	    }
	}
	else if (!password.equals(other.password)) {
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
	if (tipo == null) {
	    if (other.tipo != null) {
		return false;
	    }
	}
	else if (!tipo.equals(other.tipo)) {
	    return false;
	}
	if (username == null) {
	    if (other.username != null) {
		return false;
	    }
	}
	else if (!username.equals(other.username)) {
	    return false;
	}
	return true;
    }

    public static ContentValues insertSQL(Utente user) {

	ContentValues values = new ContentValues();

	values.put(UtenteDB.Fields.ID_UTENTE, user.idutente);
	values.put(com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
	values.put(UtenteDB.Fields.NOME, user.nome);
	values.put(UtenteDB.Fields.COGNOME, user.cognome);
	values.put(UtenteDB.Fields.USERNAME, user.username);
	values.put(UtenteDB.Fields.CANCELLATO, user.cancellato);
	values.put(UtenteDB.Fields.REVISIONE, user.revisione);
	values.put(UtenteDB.Fields.EMAIL, user.email);
	values.put(UtenteDB.Fields.TIPO, user.tipo);
	values.put(UtenteDB.Fields.CESTINATO, user.cestinato);

	return values;
    }

    public static ContentValues updateSQL(Utente user) {

	ContentValues values = new ContentValues();

	values.put(UtenteDB.Fields.NOME, user.nome);
	values.put(UtenteDB.Fields.COGNOME, user.cognome);
	values.put(UtenteDB.Fields.USERNAME, user.username);
	values.put(UtenteDB.Fields.CANCELLATO, user.cancellato);
	values.put(UtenteDB.Fields.REVISIONE, user.revisione);
	values.put(UtenteDB.Fields.EMAIL, user.email);
	values.put(UtenteDB.Fields.TIPO, user.tipo);
	values.put(UtenteDB.Fields.CESTINATO, user.cestinato);

	return values;
    }
}
