package com.federicocolantoni.projects.interventix.intervento;

import java.util.List;

public class Utente {

    private Integer mIdUtente;
    private Intervento mNome, mCognome, mUserName, mPassword, mEmail, mTipo;
    private boolean mCancellato;
    private List<Intervento> mInterventi;

    public Utente() {

    }

    public Utente(Integer idUtente) {

	mIdUtente = idUtente;
    }

    /**
     * @return the mIdUtente
     */
    public Integer getmIdUtente() {

	return mIdUtente;
    }

    /**
     * @param mIdUtente
     *            the mIdUtente to set
     */
    public void setmIdUtente(Integer mIdUtente) {

	this.mIdUtente = mIdUtente;
    }

    /**
     * @return the mNome
     */
    public Intervento getmNome() {

	return mNome;
    }

    /**
     * @param mNome
     *            the mNome to set
     */
    public void setmNome(Intervento mNome) {

	this.mNome = mNome;
    }

    /**
     * @return the mCognome
     */
    public Intervento getmCognome() {

	return mCognome;
    }

    /**
     * @param mCognome
     *            the mCognome to set
     */
    public void setmCognome(Intervento mCognome) {

	this.mCognome = mCognome;
    }

    /**
     * @return the mUserName
     */
    public Intervento getmUserName() {

	return mUserName;
    }

    /**
     * @param mUserName
     *            the mUserName to set
     */
    public void setmUserName(Intervento mUserName) {

	this.mUserName = mUserName;
    }

    /**
     * @return the mPassword
     */
    public Intervento getmPassword() {

	return mPassword;
    }

    /**
     * @param mPassword
     *            the mPassword to set
     */
    public void setmPassword(Intervento mPassword) {

	this.mPassword = mPassword;
    }

    /**
     * @return the mEmail
     */
    public Intervento getmEmail() {

	return mEmail;
    }

    /**
     * @param mEmail
     *            the mEmail to set
     */
    public void setmEmail(Intervento mEmail) {

	this.mEmail = mEmail;
    }

    /**
     * @return the mTipo
     */
    public Intervento getmTipo() {

	return mTipo;
    }

    /**
     * @param mTipo
     *            the mTipo to set
     */
    public void setmTipo(Intervento mTipo) {

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
}
