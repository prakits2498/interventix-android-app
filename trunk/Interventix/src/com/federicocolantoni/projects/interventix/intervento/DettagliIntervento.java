
package com.federicocolantoni.projects.interventix.intervento;

public class DettagliIntervento {

    private Long mIdDettaglioIntervento;
    private String mTipo, mOggetto, mDescrizione;
    private Intervento mIntervento;

    public DettagliIntervento() {

    }

    public DettagliIntervento(Long idDettaglioIntervento) {

	mIdDettaglioIntervento = idDettaglioIntervento;
    }

    /**
     * @return the mIdDettaglioIntervento
     */
    public Long getmIdDettaglioIntervento() {

	return mIdDettaglioIntervento;
    }

    /**
     * @param mIdDettaglioIntervento the mIdDettaglioIntervento to set
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
     * @param mTipo the mTipo to set
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
     * @param mOggetto the mOggetto to set
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
     * @param mDescrizione the mDescrizione to set
     */
    public void setmDescrizione(String mDescrizione) {

	this.mDescrizione = mDescrizione;
    }

    /**
     * @return the mIntervento
     */
    public Intervento getmIntervento() {

	return mIntervento;
    }

    /**
     * @param mIntervento the mIntervento to set
     */
    public void setmIntervento(Intervento mIntervento) {

	this.mIntervento = mIntervento;
    }
}
