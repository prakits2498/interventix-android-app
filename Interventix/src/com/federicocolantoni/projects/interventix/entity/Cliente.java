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
    
    private Long mIdCliente;
    private String mNominativo, mCodiceFiscale, mPartitaIVA, mTelefono, mFax,
	    mEmail, mReferente, mCitta, mIndirizzo, mInterno, mUfficio, mNote;
    private boolean mCancellato, mConflitto;
    private Long mRevisione;
    
    public Cliente() {
	
    }
    
    public Cliente(Long idCliente) {
	
	mIdCliente = idCliente;
    }
    
    /**
     * @return the mIdCliente
     */
    public Long getIdCliente() {
	
	return mIdCliente;
    }
    
    /**
     * @param mIdCliente
     *            the mIdCliente to set
     */
    public void setIdCliente(Long mIdCliente) {
	
	this.mIdCliente = mIdCliente;
    }
    
    /**
     * @return the mNominativo
     */
    public String getNominativo() {
	
	return mNominativo;
    }
    
    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setNominativo(String mNominativo) {
	
	this.mNominativo = mNominativo;
    }
    
    /**
     * @return the mCodiceFiscale
     */
    public String getCodiceFiscale() {
	
	return mCodiceFiscale;
    }
    
    /**
     * @param mCodiceFiscale
     *            the mCodiceFiscale to set
     */
    public void setCodiceFiscale(String mCodiceFiscale) {
	
	this.mCodiceFiscale = mCodiceFiscale;
    }
    
    /**
     * @return the mPartitaIVA
     */
    public String getPartitaIVA() {
	
	return mPartitaIVA;
    }
    
    /**
     * @param mPartitaIVA
     *            the mPartitaIVA to set
     */
    public void setPartitaIVA(String mPartitaIVA) {
	
	this.mPartitaIVA = mPartitaIVA;
    }
    
    /**
     * @return the mTelefono
     */
    public String getTelefono() {
	
	return mTelefono;
    }
    
    /**
     * @param mTelefono
     *            the mTelefono to set
     */
    public void setTelefono(String mTelefono) {
	
	this.mTelefono = mTelefono;
    }
    
    /**
     * @return the mFax
     */
    public String getFax() {
	
	return mFax;
    }
    
    /**
     * @param mFax
     *            the mFax to set
     */
    public void setFax(String mFax) {
	
	this.mFax = mFax;
    }
    
    /**
     * @return the mEmail
     */
    public String getEmail() {
	
	return mEmail;
    }
    
    /**
     * @param mEmail
     *            the mEmail to set
     */
    public void setEmail(String mEmail) {
	
	this.mEmail = mEmail;
    }
    
    /**
     * @return the mReferente
     */
    public String getReferente() {
	
	return mReferente;
    }
    
    /**
     * @param mReferente
     *            the mReferente to set
     */
    public void setReferente(String mReferente) {
	
	this.mReferente = mReferente;
    }
    
    /**
     * @return the mCitta
     */
    public String getCitta() {
	
	return mCitta;
    }
    
    /**
     * @param mCitta
     *            the mCitta to set
     */
    public void setCitta(String mCitta) {
	
	this.mCitta = mCitta;
    }
    
    /**
     * @return the mIndirizzo
     */
    public String getIndirizzo() {
	
	return mIndirizzo;
    }
    
    /**
     * @param mIndirizzo
     *            the mIndirizzo to set
     */
    public void setIndirizzo(String mIndirizzo) {
	
	this.mIndirizzo = mIndirizzo;
    }
    
    /**
     * @return the mInterno
     */
    public String getInterno() {
	
	return mInterno;
    }
    
    /**
     * @param mInterno
     *            the mInterno to set
     */
    public void setInterno(String mInterno) {
	
	this.mInterno = mInterno;
    }
    
    /**
     * @return the mUfficio
     */
    public String getUfficio() {
	
	return mUfficio;
    }
    
    /**
     * @param mUfficio
     *            the mUfficio to set
     */
    public void setUfficio(String mUfficio) {
	
	this.mUfficio = mUfficio;
    }
    
    /**
     * @return the mNote
     */
    public String getNote() {
	
	return mNote;
    }
    
    /**
     * @param mNote
     *            the mNote to set
     */
    public void setNote(String mNote) {
	
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
    public Long getRevisione() {
	
	return mRevisione;
    }
    
    /**
     * @param revisione
     *            the revisione to set
     */
    public void setRevisione(Long revisione) {
	
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
    
    public static ContentValues insertSQL(Cliente cliente) {
	
	ContentValues values = new ContentValues();
	
	values.put(ClienteDB.Fields.ID_CLIENTE, cliente.getIdCliente());
	values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
	values.put(ClienteDB.Fields.CITTA, cliente.getCitta());
	values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getCodiceFiscale());
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
}
