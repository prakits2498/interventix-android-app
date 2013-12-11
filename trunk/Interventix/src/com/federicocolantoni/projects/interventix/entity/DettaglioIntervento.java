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
    public void setmTecnici(JSONArray mTecnici) {
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
    public void setmModificato(String mModificato) {
	this.mModificato = mModificato;
    }
    
    public static ContentValues insertSQL(Long id, String descrizione, Long intervento, String modificato, String oggetto, String tipo,
	    Long inizio, Long fine, String tecnici) {
	
	ContentValues values = new ContentValues();
	
	values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, id);
	values.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
	values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, descrizione);
	values.put(DettaglioInterventoDB.Fields.INTERVENTO, intervento);
	values.put(DettaglioInterventoDB.Fields.MODIFICATO, modificato);
	values.put(DettaglioInterventoDB.Fields.OGGETTO, oggetto);
	values.put(DettaglioInterventoDB.Fields.TIPO, tipo);
	values.put(DettaglioInterventoDB.Fields.INIZIO, inizio);
	values.put(DettaglioInterventoDB.Fields.FINE, fine);
	values.put(DettaglioInterventoDB.Fields.TECNICI, tecnici);
	
	return values;
    }
    
    public static ContentValues updateSQL(String descrizione, Long intervento, String modificato, String oggetto, String tipo,
	    Long inizio, Long fine, String tecnici) {
	
	ContentValues values = new ContentValues();
	
	values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, descrizione);
	values.put(DettaglioInterventoDB.Fields.INTERVENTO, intervento);
	values.put(DettaglioInterventoDB.Fields.MODIFICATO, modificato);
	values.put(DettaglioInterventoDB.Fields.OGGETTO, oggetto);
	values.put(DettaglioInterventoDB.Fields.TIPO, tipo);
	values.put(DettaglioInterventoDB.Fields.INIZIO, inizio);
	values.put(DettaglioInterventoDB.Fields.FINE, fine);
	values.put(DettaglioInterventoDB.Fields.TECNICI, tecnici);
	
	return values;
    }
}
