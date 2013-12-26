package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;
import java.util.List;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.UtenteDB;

public class Utente implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -460809443185110082L;
    
    private Long mIdUtente, mRevisione;
    private String mNome, mCognome, mUserName, mPassword, mEmail, mTipo;
    private boolean mCancellato, mCestinato, mConflitto;
    private List<Intervento> mInterventi;
    
    public Utente() {
	
    }
    
    public Utente(Long idUtente) {
	
	mIdUtente = idUtente;
    }
    
    /**
     * @return the mIdUtente
     */
    public Long getIdUtente() {
	
	return mIdUtente;
    }
    
    /**
     * @param mIdUtente
     *            the mIdUtente to set
     */
    public void setIdUtente(Long mIdUtente) {
	
	this.mIdUtente = mIdUtente;
    }
    
    /**
     * @return the mNome
     */
    public String getNome() {
	
	return mNome;
    }
    
    /**
     * @param mNome
     *            the mNome to set
     */
    public void setNome(String mNome) {
	
	this.mNome = mNome;
    }
    
    /**
     * @return the mCognome
     */
    public String getCognome() {
	
	return mCognome;
    }
    
    /**
     * @param mCognome
     *            the mCognome to set
     */
    public void setCognome(String mCognome) {
	
	this.mCognome = mCognome;
    }
    
    /**
     * @return the mUserName
     */
    public String getUsername() {
	
	return mUserName;
    }
    
    /**
     * @param mUserName
     *            the mUserName to set
     */
    public void setUserName(String mUserName) {
	
	this.mUserName = mUserName;
    }
    
    /**
     * @return the mPassword
     */
    public String getmPassword() {
	
	return mPassword;
    }
    
    /**
     * @param mPassword
     *            the mPassword to set
     */
    public void setmPassword(String mPassword) {
	
	this.mPassword = mPassword;
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
     * @return the mTipo
     */
    public String getTipo() {
	
	return mTipo;
    }
    
    /**
     * @param mTipo
     *            the mTipo to set
     */
    public void setTipo(String mTipo) {
	
	this.mTipo = mTipo;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean isCancellato() {
	
	return mCancellato;
    }
    
    /**
     * @param mCancellato
     *            the mCancellato to set
     */
    public void setCancellato(boolean mCancellato) {
	
	this.mCancellato = mCancellato;
    }
    
    /**
     * @return the mCestinato
     */
    public boolean isCestinato() {
	return mCestinato;
    }
    
    /**
     * @param mCestinato
     *            the mCestinato to set
     */
    public void setCestinato(boolean mCestinato) {
	this.mCestinato = mCestinato;
    }
    
    /**
     * @return the mInterventi
     */
    public List<Intervento> getInterventi() {
	
	return mInterventi;
    }
    
    /**
     * @param mInterventi
     *            the mInterventi to set
     */
    public void setInterventi(List<Intervento> mInterventi) {
	
	this.mInterventi = mInterventi;
    }
    
    /**
     * @return the mRevisione
     */
    public Long getRevisione() {
	
	return mRevisione;
    }
    
    /**
     * @param mRevisione
     *            the mRevisione to set
     */
    public void setRevisione(Long mRevisione) {
	
	this.mRevisione = mRevisione;
    }
    
    /**
     * @return the mConflitto
     */
    public boolean isConflitto() {
	return mConflitto;
    }
    
    /**
     * @param mConflitto
     *            the mConflitto to set
     */
    public void setConflitto(boolean mConflitto) {
	this.mConflitto = mConflitto;
    }
    
    @Override
    public String toString() {
	
	String result = "";
	
	result += "Utente " + mIdUtente + "\nNome: " + mNome + "\nCognome" + mCognome + "\nUsername: " + mUserName +
		"\nEmail: " + mEmail + "\nTipo: " + mTipo + "\nRevisione: " + mRevisione + "\nCancellato: " + mCancellato + "\nCestinato: " + mCestinato;
	
	return result;
    }
    
    public static ContentValues insertSQL(Utente user) {
	
	ContentValues values = new ContentValues();
	
	values.put(UtenteDB.Fields.ID_UTENTE, user.getIdUtente());
	values.put(UtenteDB.Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
	values.put(UtenteDB.Fields.NOME, user.getNome());
	values.put(UtenteDB.Fields.COGNOME, user.getCognome());
	values.put(UtenteDB.Fields.USERNAME, user.getUsername());
	values.put(UtenteDB.Fields.CANCELLATO, user.isCancellato());
	values.put(UtenteDB.Fields.REVISIONE, user.getRevisione());
	values.put(UtenteDB.Fields.EMAIL, user.getEmail());
	values.put(UtenteDB.Fields.TIPO, user.getTipo());
	values.put(UtenteDB.Fields.CESTINATO, user.isCestinato());
	
	return values;
    }
    
    public static ContentValues updateSQL(Utente user) {
	
	ContentValues values = new ContentValues();
	
	values.put(UtenteDB.Fields.NOME, user.getNome());
	values.put(UtenteDB.Fields.COGNOME, user.getCognome());
	values.put(UtenteDB.Fields.USERNAME, user.getUsername());
	values.put(UtenteDB.Fields.CANCELLATO, user.isCancellato());
	values.put(UtenteDB.Fields.REVISIONE, user.getRevisione());
	values.put(UtenteDB.Fields.EMAIL, user.getEmail());
	values.put(UtenteDB.Fields.TIPO, user.getTipo());
	values.put(UtenteDB.Fields.CESTINATO, user.isCestinato());
	
	return values;
    }
}
