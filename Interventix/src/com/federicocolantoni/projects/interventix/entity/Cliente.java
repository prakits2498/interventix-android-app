package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;

public class Cliente implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6163117197009443000L;
    
    private Integer mIdCliente;
    private String mNominativo, mCodiceFiscale, mPartitaIVA, mTelefono, mFax,
	    mEmail, mReferente, mCitta, mIndirizzo, mInterno, mUfficio, mNote;
    private boolean mCancellato, mConflitto;
    private int mRevisione;
    
    public Cliente() {
	
    }
    
    public Cliente(Integer idCliente) {
	
	mIdCliente = idCliente;
    }
    
    /**
     * @return the mIdCliente
     */
    public Integer getmIdCliente() {
	
	return mIdCliente;
    }
    
    /**
     * @param mIdCliente
     *            the mIdCliente to set
     */
    public void setmIdCliente(Integer mIdCliente) {
	
	this.mIdCliente = mIdCliente;
    }
    
    /**
     * @return the mNominativo
     */
    public String getmNominativo() {
	
	return mNominativo;
    }
    
    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setmNominativo(String mNominativo) {
	
	this.mNominativo = mNominativo;
    }
    
    /**
     * @return the mCodiceFiscale
     */
    public String getmCodiceFiscale() {
	
	return mCodiceFiscale;
    }
    
    /**
     * @param mCodiceFiscale
     *            the mCodiceFiscale to set
     */
    public void setmCodiceFiscale(String mCodiceFiscale) {
	
	this.mCodiceFiscale = mCodiceFiscale;
    }
    
    /**
     * @return the mPartitaIVA
     */
    public String getmPartitaIVA() {
	
	return mPartitaIVA;
    }
    
    /**
     * @param mPartitaIVA
     *            the mPartitaIVA to set
     */
    public void setmPartitaIVA(String mPartitaIVA) {
	
	this.mPartitaIVA = mPartitaIVA;
    }
    
    /**
     * @return the mTelefono
     */
    public String getmTelefono() {
	
	return mTelefono;
    }
    
    /**
     * @param mTelefono
     *            the mTelefono to set
     */
    public void setmTelefono(String mTelefono) {
	
	this.mTelefono = mTelefono;
    }
    
    /**
     * @return the mFax
     */
    public String getmFax() {
	
	return mFax;
    }
    
    /**
     * @param mFax
     *            the mFax to set
     */
    public void setmFax(String mFax) {
	
	this.mFax = mFax;
    }
    
    /**
     * @return the mEmail
     */
    public String getmEmail() {
	
	return mEmail;
    }
    
    /**
     * @param mEmail
     *            the mEmail to set
     */
    public void setmEmail(String mEmail) {
	
	this.mEmail = mEmail;
    }
    
    /**
     * @return the mReferente
     */
    public String getmReferente() {
	
	return mReferente;
    }
    
    /**
     * @param mReferente
     *            the mReferente to set
     */
    public void setmReferente(String mReferente) {
	
	this.mReferente = mReferente;
    }
    
    /**
     * @return the mCitta
     */
    public String getmCitta() {
	
	return mCitta;
    }
    
    /**
     * @param mCitta
     *            the mCitta to set
     */
    public void setmCitta(String mCitta) {
	
	this.mCitta = mCitta;
    }
    
    /**
     * @return the mIndirizzo
     */
    public String getmIndirizzo() {
	
	return mIndirizzo;
    }
    
    /**
     * @param mIndirizzo
     *            the mIndirizzo to set
     */
    public void setmIndirizzo(String mIndirizzo) {
	
	this.mIndirizzo = mIndirizzo;
    }
    
    /**
     * @return the mInterno
     */
    public String getmInterno() {
	
	return mInterno;
    }
    
    /**
     * @param mInterno
     *            the mInterno to set
     */
    public void setmInterno(String mInterno) {
	
	this.mInterno = mInterno;
    }
    
    /**
     * @return the mUfficio
     */
    public String getmUfficio() {
	
	return mUfficio;
    }
    
    /**
     * @param mUfficio
     *            the mUfficio to set
     */
    public void setmUfficio(String mUfficio) {
	
	this.mUfficio = mUfficio;
    }
    
    /**
     * @return the mNote
     */
    public String getmNote() {
	
	return mNote;
    }
    
    /**
     * @param mNote
     *            the mNote to set
     */
    public void setmNote(String mNote) {
	
	this.mNote = mNote;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean ismCancellato() {
	
	return mCancellato;
    }
    
    /**
     * @param mCancellato
     *            the mCancellato to set
     */
    public void setmCancellato(boolean mCancellato) {
	
	this.mCancellato = mCancellato;
    }
    
    /**
     * @return the revisione
     */
    public int getmRevisione() {
	
	return mRevisione;
    }
    
    /**
     * @param revisione
     *            the revisione to set
     */
    public void setmRevisione(int revisione) {
	
	mRevisione = revisione;
    }
    
    /**
     * @return the mConflitto
     */
    public boolean ismConflitto() {
	return mConflitto;
    }
    
    /**
     * @param mConflitto
     *            the mConflitto to set
     */
    public void setmConflitto(boolean mConflitto) {
	this.mConflitto = mConflitto;
    }
    
    @Override
    public String toString() {
	
	String result = "";
	
	result += "Cliente " + mIdCliente + "\nNominativo: " + mNominativo + "\nCodice fiscale: " + mCodiceFiscale + "\nCittà: " + mCitta +
		"\nEmail: " + mEmail + "\nFax: " + mFax + "\nIndirizzo: " + mIndirizzo + "\nInterno: " + mInterno + "\nPartita IVA: " + mPartitaIVA +
		"\nReferente: " + mReferente + "\nNote: " + mNote + "\nTelefono: " + mTelefono + "\nUfficio: " + mUfficio;
	
	return result;
    }
    
    public static ContentValues insertSQL(Long id, String citta, String codiceFiscale, String email, String fax, String indirizzo, String interno,
	    String nominativo, String note, String partitaIva, String referente, Long revisione, String telefono, String ufficio) {
	
	ContentValues values = new ContentValues();
	
	values.put(ClienteDB.Fields.ID_CLIENTE, id);
	values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
	values.put(ClienteDB.Fields.CITTA, citta);
	values.put(ClienteDB.Fields.CODICE_FISCALE, codiceFiscale);
	values.put(ClienteDB.Fields.EMAIL, email);
	values.put(ClienteDB.Fields.FAX, fax);
	values.put(ClienteDB.Fields.INDIRIZZO, indirizzo);
	values.put(ClienteDB.Fields.INTERNO, interno);
	values.put(ClienteDB.Fields.NOMINATIVO, nominativo);
	values.put(ClienteDB.Fields.NOTE, note);
	values.put(ClienteDB.Fields.PARTITAIVA, partitaIva);
	values.put(ClienteDB.Fields.REFERENTE, referente);
	values.put(ClienteDB.Fields.REVISIONE, revisione);
	values.put(ClienteDB.Fields.TELEFONO, telefono);
	values.put(ClienteDB.Fields.UFFICIO, ufficio);
	
	return values;
    }
    
    public static ContentValues updateSQL(String citta, String codiceFiscale, String email, String fax, String indirizzo, String interno,
	    String nominativo, String note, String partitaIva, String referente, Long revisione, String telefono, String ufficio) {
	
	ContentValues values = new ContentValues();
	
	values.put(ClienteDB.Fields.CITTA, citta);
	values.put(ClienteDB.Fields.CODICE_FISCALE, codiceFiscale);
	values.put(ClienteDB.Fields.EMAIL, email);
	values.put(ClienteDB.Fields.FAX, fax);
	values.put(ClienteDB.Fields.INDIRIZZO, indirizzo);
	values.put(ClienteDB.Fields.INTERNO, interno);
	values.put(ClienteDB.Fields.NOMINATIVO, nominativo);
	values.put(ClienteDB.Fields.NOTE, note);
	values.put(ClienteDB.Fields.PARTITAIVA, partitaIva);
	values.put(ClienteDB.Fields.REFERENTE, referente);
	values.put(ClienteDB.Fields.REVISIONE, revisione);
	values.put(ClienteDB.Fields.TELEFONO, telefono);
	values.put(ClienteDB.Fields.UFFICIO, ufficio);
	
	return values;
    }
}