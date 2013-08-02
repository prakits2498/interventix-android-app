package com.federicocolantoni.projects.interventix.intervento;

public class DettaglioIntervento {

    private Long mIdDettaglioIntervento;
    private String mTipo, mOggetto, mDescrizione;
    private Long mIntervento;
    private Long mInizio, mFine;

    public DettaglioIntervento() {

    }

    public DettaglioIntervento(Long idDettaglioIntervento) {

	mIdDettaglioIntervento = idDettaglioIntervento;
    }

    /**
     * @return the mIdDettaglioIntervento
     */
    public Long getmIdDettaglioIntervento() {

	return mIdDettaglioIntervento;
    }

    /**
     * @param mIdDettaglioIntervento
     *            the mIdDettaglioIntervento to set
     */
    public void setmIdDettaglioIntervento(Long mIdDettaglioIntervento) {

	this.mIdDettaglioIntervento = mIdDettaglioIntervento;
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
     * @return the mOggetto
     */
    public String getmOggetto() {

	return mOggetto;
    }

    /**
     * @param mOggetto
     *            the mOggetto to set
     */
    public void setmOggetto(String mOggetto) {

	this.mOggetto = mOggetto;
    }

    /**
     * @return the mDescrizione
     */
    public String getmDescrizione() {

	return mDescrizione;
    }

    /**
     * @param mDescrizione
     *            the mDescrizione to set
     */
    public void setmDescrizione(String mDescrizione) {

	this.mDescrizione = mDescrizione;
    }

    /**
     * @return the mIntervento
     */
    public Long getmIntervento() {

	return mIntervento;
    }

    /**
     * @param mIntervento
     *            the mIntervento to set
     */
    public void setmIntervento(Long mIntervento) {

	this.mIntervento = mIntervento;
    }

    public Long getmInizio() {
	return mInizio;
    }

    public void setmInizio(Long mInizio) {
	this.mInizio = mInizio;
    }

    public Long getmFine() {
	return mFine;
    }

    public void setmFine(Long mFine) {
	this.mFine = mFine;
    }
}
