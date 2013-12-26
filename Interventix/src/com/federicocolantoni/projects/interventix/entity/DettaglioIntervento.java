package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import org.json.JSONArray;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;

public class DettaglioIntervento implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3301272979527832708L;
    
    private Long mIdDettaglioIntervento;
    private String mTipo, mOggetto, mDescrizione, mModificato;
    private Long mIntervento;
    private Long mInizio, mFine;
    private JSONArray mTecnici;
    
    public DettaglioIntervento() {
	
    }
    
    @Override
    public String toString() {
	
	String result = "";
	
	result += "Dettaglio " + mIdDettaglioIntervento + "\nOggetto: " + mOggetto + "\nDescrizione: " + mDescrizione + "\nTipo: " + mTipo + "\nIntervento: " + mIntervento;
	
	return result;
	
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
    public void setIdDettaglioIntervento(Long mIdDettaglioIntervento) {
	
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
    public void setTipo(String mTipo) {
	
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
    public void setOggetto(String mOggetto) {
	
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
    public void setDescrizione(String mDescrizione) {
	
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
    public void setIntervento(Long mIntervento) {
	
	this.mIntervento = mIntervento;
    }
    
    public Long getmInizio() {
	return mInizio;
    }
    
    public void setInizio(Long mInizio) {
	this.mInizio = mInizio;
    }
    
    public Long getmFine() {
	return mFine;
    }
    
    public void setFine(Long mFine) {
	this.mFine = mFine;
    }
    
    /**
     * @return the mTecnici
     */
    public JSONArray getmTecnici() {
	return mTecnici;
    }
    
    /**
     * @param mTecnici
     *            the mTecnici to set
     */
    public void setTecnici(JSONArray mTecnici) {
	this.mTecnici = mTecnici;
    }
    
    /**
     * @return the mModificato
     */
    public String getmModificato() {
	return mModificato;
    }
    
    /**
     * @param mModificato
     *            the mModificato to set
     */
    public void setModificato(String mModificato) {
	this.mModificato = mModificato;
    }
    
    public static ContentValues insertSQL(DettaglioIntervento dettIntervento) {
	
	ContentValues values = new ContentValues();
	
	values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, dettIntervento.getmIdDettaglioIntervento());
	values.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
	values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getmDescrizione());
	values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getmIntervento());
	values.put(DettaglioInterventoDB.Fields.MODIFICATO, dettIntervento.getmModificato());
	values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getmOggetto());
	values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getmTipo());
	values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getmInizio());
	values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getmFine());
	values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getmTecnici().toString());
	
	return values;
    }
    
    public static ContentValues updateSQL(DettaglioIntervento dettIntervento) {
	
	ContentValues values = new ContentValues();
	
	values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getmDescrizione());
	values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getmIntervento());
	values.put(DettaglioInterventoDB.Fields.MODIFICATO, dettIntervento.getmModificato());
	values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getmOggetto());
	values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getmTipo());
	values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getmInizio());
	values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getmFine());
	values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getmTecnici().toString());
	
	return values;
    }
}
