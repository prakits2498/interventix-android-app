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
    private boolean mCancellato, mCestinato;
    private List<Intervento> mInterventi;
    
    public Utente() {
	
    }
    
    public Utente(Long idUtente) {
	
	mIdUtente = idUtente;
    }
    
    /**
     * @return the mIdUtente
     */
    public Long getmIdUtente() {
	
	return mIdUtente;
    }
    
    /**
     * @param mIdUtente
     *            the mIdUtente to set
     */
    public void setmIdUtente(Long mIdUtente) {
	
	this.mIdUtente = mIdUtente;
    }
    
    /**
     * @return the mNome
     */
    public String getmNome() {
	
	return mNome;
    }
    
    /**
     * @param mNome
     *            the mNome to set
     */
    public void setmNome(String mNome) {
	
	this.mNome = mNome;
    }
    
    /**
     * @return the mCognome
     */
    public String getmCognome() {
	
	return mCognome;
    }
    
    /**
     * @param mCognome
     *            the mCognome to set
     */
    public void setmCognome(String mCognome) {
	
	this.mCognome = mCognome;
    }
    
    /**
     * @return the mUserName
     */
    public String getmUserName() {
	
	return mUserName;
    }
    
    /**
     * @param mUserName
     *            the mUserName to set
     */
    public void setmUserName(String mUserName) {
	
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
     * @return the mTipo
     */
    public String getmTipo() {
	
	return mTipo;
    }
    
    /**
     * @param mTipo
     *            the mTipo to set
     */
    public void setmTipo(String mTipo) {
	
	this.mTipo = mTipo;
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
     * @return the mCestinato
     */
    public boolean ismCestinato() {
	return mCestinato;
    }
    
    /**
     * @param mCestinato
     *            the mCestinato to set
     */
    public void setmCestinato(boolean mCestinato) {
	this.mCestinato = mCestinato;
    }
    
    /**
     * @return the mInterventi
     */
    public List<Intervento> getmInterventi() {
	
	return mInterventi;
    }
    
    /**
     * @param mInterventi
     *            the mInterventi to set
     */
    public void setmInterventi(List<Intervento> mInterventi) {
	
	this.mInterventi = mInterventi;
    }
    
    /**
     * @return the mRevisione
     */
    public Long getmRevisione() {
	
	return mRevisione;
    }
    
    /**
     * @param mRevisione
     *            the mRevisione to set
     */
    public void setmRevisione(Long mRevisione) {
	
	this.mRevisione = mRevisione;
    }
    
    @Override
    public String toString() {
	
	String result = "";
	
	result += "Utente " + mIdUtente + "\nNome: " + mNome + "\nCognome" + mCognome + "\nUsername: " + mUserName +
		"\nEmail: " + mEmail + "\nTipo: " + mTipo + "\nRevisione: " + mRevisione + "\nCancellato: " + mCancellato + "\nCestinato: " + mCestinato;
	
	return result;
    }
    
    public static ContentValues insertSQL(Long id, String nome, String cognome, String username, String email, String tipo, Long revisione, Boolean cancellato, Boolean cestinato) {
	
	ContentValues values = new ContentValues();
	
	values.put(UtenteDB.Fields.ID_UTENTE, id);
	values.put(UtenteDB.Fields.TYPE, UtenteDB.UTENTE_ITEM_TYPE);
	values.put(UtenteDB.Fields.NOME, nome);
	values.put(UtenteDB.Fields.COGNOME, cognome);
	values.put(UtenteDB.Fields.USERNAME, username);
	values.put(UtenteDB.Fields.CANCELLATO, cancellato);
	values.put(UtenteDB.Fields.REVISIONE, revisione);
	values.put(UtenteDB.Fields.EMAIL, email);
	values.put(UtenteDB.Fields.TIPO, tipo);
	values.put(UtenteDB.Fields.CESTINATO, cestinato);
	
	return values;
    }
    
    public static ContentValues updateSQL(String nome, String cognome, String username, String email, String tipo, Long revisione, Boolean cancellato, Boolean cestinato) {
	
	ContentValues values = new ContentValues();
	
	values.put(UtenteDB.Fields.NOME, nome);
	values.put(UtenteDB.Fields.COGNOME, cognome);
	values.put(UtenteDB.Fields.USERNAME, username);
	values.put(UtenteDB.Fields.CANCELLATO, cancellato);
	values.put(UtenteDB.Fields.REVISIONE, revisione);
	values.put(UtenteDB.Fields.EMAIL, email);
	values.put(UtenteDB.Fields.TIPO, tipo);
	values.put(UtenteDB.Fields.CESTINATO, cestinato);
	
	return values;
    }
}
