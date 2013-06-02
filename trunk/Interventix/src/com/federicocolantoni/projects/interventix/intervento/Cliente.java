
package com.federicocolantoni.projects.interventix.intervento;


public class Cliente {

    private Integer mIdCliente;
    private Intervento mNominativo, mCodiceFiscale, mPartitaIVA, mTelefono, mFax,
	    mEmail, mReferente, mCitta, mIndirizzo, mInterno, mUfficio, mNote;
    private boolean mCancellato;
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
    public Intervento getmNominativo() {

	return mNominativo;
    }

    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setmNominativo(Intervento mNominativo) {

	this.mNominativo = mNominativo;
    }

    /**
     * @return the mCodiceFiscale
     */
    public Intervento getmCodiceFiscale() {

	return mCodiceFiscale;
    }

    /**
     * @param mCodiceFiscale
     *            the mCodiceFiscale to set
     */
    public void setmCodiceFiscale(Intervento mCodiceFiscale) {

	this.mCodiceFiscale = mCodiceFiscale;
    }

    /**
     * @return the mPartitaIVA
     */
    public Intervento getmPartitaIVA() {

	return mPartitaIVA;
    }

    /**
     * @param mPartitaIVA
     *            the mPartitaIVA to set
     */
    public void setmPartitaIVA(Intervento mPartitaIVA) {

	this.mPartitaIVA = mPartitaIVA;
    }

    /**
     * @return the mTelefono
     */
    public Intervento getmTelefono() {

	return mTelefono;
    }

    /**
     * @param mTelefono
     *            the mTelefono to set
     */
    public void setmTelefono(Intervento mTelefono) {

	this.mTelefono = mTelefono;
    }

    /**
     * @return the mFax
     */
    public Intervento getmFax() {

	return mFax;
    }

    /**
     * @param mFax
     *            the mFax to set
     */
    public void setmFax(Intervento mFax) {

	this.mFax = mFax;
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
     * @return the mReferente
     */
    public Intervento getmReferente() {

	return mReferente;
    }

    /**
     * @param mReferente
     *            the mReferente to set
     */
    public void setmReferente(Intervento mReferente) {

	this.mReferente = mReferente;
    }

    /**
     * @return the mCitta
     */
    public Intervento getmCitta() {

	return mCitta;
    }

    /**
     * @param mCitta
     *            the mCitta to set
     */
    public void setmCitta(Intervento mCitta) {

	this.mCitta = mCitta;
    }

    /**
     * @return the mIndirizzo
     */
    public Intervento getmIndirizzo() {

	return mIndirizzo;
    }

    /**
     * @param mIndirizzo
     *            the mIndirizzo to set
     */
    public void setmIndirizzo(Intervento mIndirizzo) {

	this.mIndirizzo = mIndirizzo;
    }

    /**
     * @return the mInterno
     */
    public Intervento getmInterno() {

	return mInterno;
    }

    /**
     * @param mInterno
     *            the mInterno to set
     */
    public void setmInterno(Intervento mInterno) {

	this.mInterno = mInterno;
    }

    /**
     * @return the mUfficio
     */
    public Intervento getmUfficio() {

	return mUfficio;
    }

    /**
     * @param mUfficio
     *            the mUfficio to set
     */
    public void setmUfficio(Intervento mUfficio) {

	this.mUfficio = mUfficio;
    }

    /**
     * @return the mNote
     */
    public Intervento getmNote() {

	return mNote;
    }

    /**
     * @param mNote
     *            the mNote to set
     */
    public void setmNote(Intervento mNote) {

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
}
