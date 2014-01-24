package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;

public class Cliente implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6163117197009443000L;
    
    private Long idcliente, revisione;
    private String nominativo, codicefiscale, partitaiva, telefono, fax, email, referente, citta, indirizzo, interno, ufficio, note;
    private boolean cancellato, conflitto;
    
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
    
    public Cliente(Long idCliente) {
    
	this.idcliente = idCliente;
    }
    
    /**
     * @return the mIdCliente
     */
    public Long getIdCliente() {
    
	return idcliente;
    }
    
    /**
     * @param mIdCliente
     *            the mIdCliente to set
     */
    public void setIdCliente(Long mIdCliente) {
    
	this.idcliente = mIdCliente;
    }
    
    /**
     * @return the mNominativo
     */
    public String getNominativo() {
    
	return nominativo;
    }
    
    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setNominativo(String mNominativo) {
    
	this.nominativo = mNominativo;
    }
    
    /**
     * @return the mCodiceFiscale
     */
    public String getCodiceFiscale() {
    
	return codicefiscale;
    }
    
    /**
     * @param mCodiceFiscale
     *            the mCodiceFiscale to set
     */
    public void setCodiceFiscale(String mCodiceFiscale) {
    
	this.codicefiscale = mCodiceFiscale;
    }
    
    /**
     * @return the mPartitaIVA
     */
    public String getPartitaIVA() {
    
	return partitaiva;
    }
    
    /**
     * @param mPartitaIVA
     *            the mPartitaIVA to set
     */
    public void setPartitaIVA(String mPartitaIVA) {
    
	this.partitaiva = mPartitaIVA;
    }
    
    /**
     * @return the mTelefono
     */
    public String getTelefono() {
    
	return telefono;
    }
    
    /**
     * @param mTelefono
     *            the mTelefono to set
     */
    public void setTelefono(String mTelefono) {
    
	this.telefono = mTelefono;
    }
    
    /**
     * @return the mFax
     */
    public String getFax() {
    
	return fax;
    }
    
    /**
     * @param mFax
     *            the mFax to set
     */
    public void setFax(String mFax) {
    
	this.fax = mFax;
    }
    
    /**
     * @return the mEmail
     */
    public String getEmail() {
    
	return email;
    }
    
    /**
     * @param mEmail
     *            the mEmail to set
     */
    public void setEmail(String mEmail) {
    
	this.email = mEmail;
    }
    
    /**
     * @return the mReferente
     */
    public String getReferente() {
    
	return referente;
    }
    
    /**
     * @param mReferente
     *            the mReferente to set
     */
    public void setReferente(String mReferente) {
    
	this.referente = mReferente;
    }
    
    /**
     * @return the mCitta
     */
    public String getCitta() {
    
	return citta;
    }
    
    /**
     * @param mCitta
     *            the mCitta to set
     */
    public void setCitta(String mCitta) {
    
	this.citta = mCitta;
    }
    
    /**
     * @return the mIndirizzo
     */
    public String getIndirizzo() {
    
	return indirizzo;
    }
    
    /**
     * @param mIndirizzo
     *            the mIndirizzo to set
     */
    public void setIndirizzo(String mIndirizzo) {
    
	this.indirizzo = mIndirizzo;
    }
    
    /**
     * @return the mInterno
     */
    public String getInterno() {
    
	return interno;
    }
    
    /**
     * @param mInterno
     *            the mInterno to set
     */
    public void setInterno(String mInterno) {
    
	this.interno = mInterno;
    }
    
    /**
     * @return the mUfficio
     */
    public String getUfficio() {
    
	return ufficio;
    }
    
    /**
     * @param mUfficio
     *            the mUfficio to set
     */
    public void setUfficio(String mUfficio) {
    
	this.ufficio = mUfficio;
    }
    
    /**
     * @return the mNote
     */
    public String getNote() {
    
	return note;
    }
    
    /**
     * @param mNote
     *            the mNote to set
     */
    public void setNote(String mNote) {
    
	this.note = mNote;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean ismCancellato() {
    
	return cancellato;
    }
    
    /**
     * @param mCancellato
     *            the mCancellato to set
     */
    public void setCancellato(boolean mCancellato) {
    
	this.cancellato = mCancellato;
    }
    
    /**
     * @return the revisione
     */
    public Long getRevisione() {
    
	return revisione;
    }
    
    /**
     * @param revisione
     *            the revisione to set
     */
    public void setRevisione(Long revisione) {
    
	this.revisione = revisione;
    }
    
    /**
     * @return the mConflitto
     */
    public boolean isConflitto() {
    
	return conflitto;
    }
    
    /**
     * @param mConflitto
     *            the mConflitto to set
     */
    public void setConflitto(boolean mConflitto) {
    
	this.conflitto = mConflitto;
    }
    
    @Override
    public String toString() {
    
	return String.format("Cliente [idcliente=%s, nominativo=%s, codicefiscale=%s, partitaiva=%s, telefono=%s, fax=%s, email=%s, referente=%s, citta=%s, indirizzo=%s, interno=%s, ufficio=%s, note=%s, cancellato=%s, conflitto=%s, revisione=%s]", idcliente, nominativo, codicefiscale, partitaiva, telefono, fax, email, referente, citta, indirizzo, interno, ufficio, note, cancellato, conflitto, revisione);
    }
    
    @Override
    public int hashCode() {
    
	final int prime = 31;
	int result = 1;
	result = prime * result + (cancellato ? 1231 : 1237);
	result = prime * result + ((citta == null) ? 0 : citta.hashCode());
	result = prime * result + ((codicefiscale == null) ? 0 : codicefiscale.hashCode());
	result = prime * result + (conflitto ? 1231 : 1237);
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + ((fax == null) ? 0 : fax.hashCode());
	result = prime * result + ((idcliente == null) ? 0 : idcliente.hashCode());
	result = prime * result + ((indirizzo == null) ? 0 : indirizzo.hashCode());
	result = prime * result + ((interno == null) ? 0 : interno.hashCode());
	result = prime * result + ((nominativo == null) ? 0 : nominativo.hashCode());
	result = prime * result + ((note == null) ? 0 : note.hashCode());
	result = prime * result + ((partitaiva == null) ? 0 : partitaiva.hashCode());
	result = prime * result + ((referente == null) ? 0 : referente.hashCode());
	result = prime * result + ((revisione == null) ? 0 : revisione.hashCode());
	result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
	result = prime * result + ((ufficio == null) ? 0 : ufficio.hashCode());
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
	else
	    if (!citta.equals(other.citta)) {
		return false;
	    }
	if (codicefiscale == null) {
	    if (other.codicefiscale != null) {
		return false;
	    }
	}
	else
	    if (!codicefiscale.equals(other.codicefiscale)) {
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
	else
	    if (!email.equals(other.email)) {
		return false;
	    }
	if (fax == null) {
	    if (other.fax != null) {
		return false;
	    }
	}
	else
	    if (!fax.equals(other.fax)) {
		return false;
	    }
	if (idcliente == null) {
	    if (other.idcliente != null) {
		return false;
	    }
	}
	else
	    if (!idcliente.equals(other.idcliente)) {
		return false;
	    }
	if (indirizzo == null) {
	    if (other.indirizzo != null) {
		return false;
	    }
	}
	else
	    if (!indirizzo.equals(other.indirizzo)) {
		return false;
	    }
	if (interno == null) {
	    if (other.interno != null) {
		return false;
	    }
	}
	else
	    if (!interno.equals(other.interno)) {
		return false;
	    }
	if (nominativo == null) {
	    if (other.nominativo != null) {
		return false;
	    }
	}
	else
	    if (!nominativo.equals(other.nominativo)) {
		return false;
	    }
	if (note == null) {
	    if (other.note != null) {
		return false;
	    }
	}
	else
	    if (!note.equals(other.note)) {
		return false;
	    }
	if (partitaiva == null) {
	    if (other.partitaiva != null) {
		return false;
	    }
	}
	else
	    if (!partitaiva.equals(other.partitaiva)) {
		return false;
	    }
	if (referente == null) {
	    if (other.referente != null) {
		return false;
	    }
	}
	else
	    if (!referente.equals(other.referente)) {
		return false;
	    }
	if (revisione == null) {
	    if (other.revisione != null) {
		return false;
	    }
	}
	else
	    if (!revisione.equals(other.revisione)) {
		return false;
	    }
	if (telefono == null) {
	    if (other.telefono != null) {
		return false;
	    }
	}
	else
	    if (!telefono.equals(other.telefono)) {
		return false;
	    }
	if (ufficio == null) {
	    if (other.ufficio != null) {
		return false;
	    }
	}
	else
	    if (!ufficio.equals(other.ufficio)) {
		return false;
	    }
	return true;
    }
    
    public static ContentValues insertSQL(Cliente cliente) {
    
	ContentValues values = new ContentValues();
	
	values.put(ClienteDB.Fields.ID_CLIENTE, cliente.getIdCliente());
	values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
	values.put(ClienteDB.Fields.CITTA, cliente.getCitta());
	values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getCodiceFiscale());
	values.put(ClienteDB.Fields.CONFLITTO, cliente.isConflitto());
	values.put(ClienteDB.Fields.EMAIL, cliente.getEmail());
	values.put(ClienteDB.Fields.FAX, cliente.getFax());
	values.put(ClienteDB.Fields.INDIRIZZO, cliente.getIndirizzo());
	values.put(ClienteDB.Fields.INTERNO, cliente.getInterno());
	values.put(ClienteDB.Fields.NOMINATIVO, cliente.getNominativo());
	values.put(ClienteDB.Fields.NOTE, cliente.getNote());
	values.put(ClienteDB.Fields.PARTITAIVA, cliente.getPartitaIVA());
	values.put(ClienteDB.Fields.REFERENTE, cliente.getReferente());
	values.put(ClienteDB.Fields.REVISIONE, cliente.getRevisione());
	values.put(ClienteDB.Fields.TELEFONO, cliente.getTelefono());
	values.put(ClienteDB.Fields.UFFICIO, cliente.getUfficio());
	
	return values;
    }
    
    public static ContentValues updateSQL(Cliente cliente) {
    
	ContentValues values = new ContentValues();
	
	values.put(ClienteDB.Fields.CITTA, cliente.getCitta());
	values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getCodiceFiscale());
	values.put(ClienteDB.Fields.CONFLITTO, cliente.isConflitto());
	values.put(ClienteDB.Fields.EMAIL, cliente.getEmail());
	values.put(ClienteDB.Fields.FAX, cliente.getFax());
	values.put(ClienteDB.Fields.INDIRIZZO, cliente.getIndirizzo());
	values.put(ClienteDB.Fields.INTERNO, cliente.getInterno());
	values.put(ClienteDB.Fields.NOMINATIVO, cliente.getNominativo());
	values.put(ClienteDB.Fields.NOTE, cliente.getNote());
	values.put(ClienteDB.Fields.PARTITAIVA, cliente.getPartitaIVA());
	values.put(ClienteDB.Fields.REFERENTE, cliente.getReferente());
	values.put(ClienteDB.Fields.REVISIONE, cliente.getRevisione());
	values.put(ClienteDB.Fields.TELEFONO, cliente.getTelefono());
	values.put(ClienteDB.Fields.UFFICIO, cliente.getUfficio());
	
	return values;
    }
    
    public static Cliente getFromCursor(Cursor cursor) {
    
	Cliente cliente = new Cliente();
	
	cliente.setCancellato(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CANCELLATO)) == 1 ? true : false);
	cliente.setCitta(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CITTA)));
	cliente.setCodiceFiscale(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
	cliente.setConflitto(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CONFLITTO)) == 1 ? true : false);
	cliente.setEmail(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.EMAIL)));
	cliente.setFax(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.FAX)));
	cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE)));
	cliente.setIndirizzo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INDIRIZZO)));
	cliente.setInterno(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INTERNO)));
	cliente.setNominativo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
	cliente.setNote(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOTE)));
	cliente.setPartitaIVA(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));
	cliente.setReferente(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.REFERENTE)));
	cliente.setRevisione(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.REVISIONE)));
	cliente.setTelefono(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.TELEFONO)));
	cliente.setUfficio(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.UFFICIO)));
	
	return cliente;
    }
}
