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
    
    private Long idutente, revisione;
    private String nome, cognome, username, password, email, tipo;
    private boolean cancellato, cestinato, conflitto;
    private List<Intervento> interventi;
    
    public Utente() {
    
    }
    
    public Utente(Long idUtente) {
    
	this.idutente = idUtente;
    }
    
    /**
     * @return the mIdUtente
     */
    public Long getIdUtente() {
    
	return idutente;
    }
    
    /**
     * @param mIdUtente
     *            the mIdUtente to set
     */
    public void setIdUtente(Long mIdUtente) {
    
	this.idutente = mIdUtente;
    }
    
    /**
     * @return the mNome
     */
    public String getNome() {
    
	return nome;
    }
    
    /**
     * @param mNome
     *            the mNome to set
     */
    public void setNome(String mNome) {
    
	this.nome = mNome;
    }
    
    /**
     * @return the mCognome
     */
    public String getCognome() {
    
	return cognome;
    }
    
    /**
     * @param mCognome
     *            the mCognome to set
     */
    public void setCognome(String mCognome) {
    
	this.cognome = mCognome;
    }
    
    /**
     * @return the mUserName
     */
    public String getUsername() {
    
	return username;
    }
    
    /**
     * @param mUserName
     *            the mUserName to set
     */
    public void setUserName(String mUserName) {
    
	this.username = mUserName;
    }
    
    /**
     * @return the mPassword
     */
    public String getmPassword() {
    
	return password;
    }
    
    /**
     * @param mPassword
     *            the mPassword to set
     */
    public void setmPassword(String mPassword) {
    
	this.password = mPassword;
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
     * @return the mTipo
     */
    public String getTipo() {
    
	return tipo;
    }
    
    /**
     * @param mTipo
     *            the mTipo to set
     */
    public void setTipo(String mTipo) {
    
	this.tipo = mTipo;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean isCancellato() {
    
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
     * @return the mCestinato
     */
    public boolean isCestinato() {
    
	return cestinato;
    }
    
    /**
     * @param mCestinato
     *            the mCestinato to set
     */
    public void setCestinato(boolean mCestinato) {
    
	this.cestinato = mCestinato;
    }
    
    /**
     * @return the mInterventi
     */
    public List<Intervento> getInterventi() {
    
	return interventi;
    }
    
    /**
     * @param mInterventi
     *            the mInterventi to set
     */
    public void setInterventi(List<Intervento> mInterventi) {
    
	this.interventi = mInterventi;
    }
    
    /**
     * @return the mRevisione
     */
    public Long getRevisione() {
    
	return revisione;
    }
    
    /**
     * @param revisione
     *            the mRevisione to set
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
    
	return String.format("Utente [idutente=%s, revisione=%s, nome=%s, cognome=%s, username=%s, password=%s, email=%s, tipo=%s, cancellato=%s, cestinato=%s, conflitto=%s, interventi=%s]", idutente, revisione, nome, cognome, username, password, email, tipo, cancellato, cestinato, conflitto, interventi);
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
