package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;

public class DettaglioIntervento implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3301272979527832708L;
    
    private Long iddettagliointervento, idintervento, inizio, fine;
    private String tipo, oggetto, descrizione, tecniciintervento;
    
    public DettaglioIntervento() {
    
	iddettagliointervento = 0L;
	idintervento = 0L;
	inizio = 0L;
	fine = 0L;
	tipo = new String();
	oggetto = new String();
	descrizione = new String();
	tecniciintervento = new String();
    }
    
    public DettaglioIntervento(Long idDettaglioIntervento) {
    
	this.iddettagliointervento = idDettaglioIntervento;
    }
    
    /**
     * @return the mIdDettaglioIntervento
     */
    public Long getIdDettaglioIntervento() {
    
	return iddettagliointervento;
    }
    
    /**
     * @param mIdDettaglioIntervento
     *            the mIdDettaglioIntervento to set
     */
    public void setIdDettaglioIntervento(Long mIdDettaglioIntervento) {
    
	this.iddettagliointervento = mIdDettaglioIntervento;
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
     * @return the mOggetto
     */
    public String getOggetto() {
    
	return oggetto;
    }
    
    /**
     * @param mOggetto
     *            the mOggetto to set
     */
    public void setOggetto(String mOggetto) {
    
	this.oggetto = mOggetto;
    }
    
    /**
     * @return the mDescrizione
     */
    public String getDescrizione() {
    
	return descrizione;
    }
    
    /**
     * @param mDescrizione
     *            the mDescrizione to set
     */
    public void setDescrizione(String mDescrizione) {
    
	this.descrizione = mDescrizione;
    }
    
    /**
     * @return the mIntervento
     */
    public Long getIntervento() {
    
	return idintervento;
    }
    
    /**
     * @param mIntervento
     *            the mIntervento to set
     */
    public void setIntervento(Long mIntervento) {
    
	this.idintervento = mIntervento;
    }
    
    public Long getInizio() {
    
	return inizio;
    }
    
    public void setInizio(Long mInizio) {
    
	this.inizio = mInizio;
    }
    
    public Long getFine() {
    
	return fine;
    }
    
    public void setFine(Long mFine) {
    
	this.fine = mFine;
    }
    
    /**
     * @return the mTecnici
     */
    public String getTecnici() {
    
	return tecniciintervento;
    }
    
    /**
     * @param mTecnici
     *            the mTecnici to set
     */
    public void setTecnici(String mTecnici) {
    
	this.tecniciintervento = mTecnici;
    }
    
    @Override
    public String toString() {
    
	return String.format("DettaglioIntervento [idDettaglioIntervento=%s, tipo=%s, oggetto=%s, descrizione=%s, intervento=%s, inizio=%s, fine=%s, tecnici=%s]", iddettagliointervento, tipo, oggetto, descrizione, idintervento, inizio, fine, tecniciintervento);
    }
    
    @Override
    public int hashCode() {
    
	final int prime = 31;
	int result = 1;
	result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
	result = prime * result + ((fine == null) ? 0 : fine.hashCode());
	result = prime * result + ((iddettagliointervento == null) ? 0 : iddettagliointervento.hashCode());
	result = prime * result + ((idintervento == null) ? 0 : idintervento.hashCode());
	result = prime * result + ((inizio == null) ? 0 : inizio.hashCode());
	result = prime * result + ((oggetto == null) ? 0 : oggetto.hashCode());
	result = prime * result + ((tecniciintervento == null) ? 0 : tecniciintervento.hashCode());
	result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
	return result;
    }
    
    @Override
    public boolean equals(Object obj) {
    
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof DettaglioIntervento)) {
	    return false;
	}
	DettaglioIntervento other = (DettaglioIntervento) obj;
	if (descrizione == null) {
	    if (other.descrizione != null) {
		return false;
	    }
	}
	else
	    if (!descrizione.equals(other.descrizione)) {
		return false;
	    }
	if (fine == null) {
	    if (other.fine != null) {
		return false;
	    }
	}
	else
	    if (!fine.equals(other.fine)) {
		return false;
	    }
	if (iddettagliointervento == null) {
	    if (other.iddettagliointervento != null) {
		return false;
	    }
	}
	else
	    if (!iddettagliointervento.equals(other.iddettagliointervento)) {
		return false;
	    }
	if (idintervento == null) {
	    if (other.idintervento != null) {
		return false;
	    }
	}
	else
	    if (!idintervento.equals(other.idintervento)) {
		return false;
	    }
	if (inizio == null) {
	    if (other.inizio != null) {
		return false;
	    }
	}
	else
	    if (!inizio.equals(other.inizio)) {
		return false;
	    }
	if (oggetto == null) {
	    if (other.oggetto != null) {
		return false;
	    }
	}
	else
	    if (!oggetto.equals(other.oggetto)) {
		return false;
	    }
	if (tecniciintervento == null) {
	    if (other.tecniciintervento != null) {
		return false;
	    }
	}
	else
	    if (!tecniciintervento.equals(other.tecniciintervento)) {
		return false;
	    }
	if (tipo == null) {
	    if (other.tipo != null) {
		return false;
	    }
	}
	else
	    if (!tipo.equals(other.tipo)) {
		return false;
	    }
	return true;
    }
    
    public static ContentValues insertSQL(DettaglioIntervento dettIntervento) {
    
	ContentValues values = new ContentValues();
	
	values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, dettIntervento.getIdDettaglioIntervento());
	values.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
	values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getDescrizione());
	values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getIntervento());
	values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getOggetto());
	values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getTipo());
	values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getInizio());
	values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getFine());
	values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getTecnici().toString());
	
	return values;
    }
    
    public static ContentValues updateSQL(DettaglioIntervento dettIntervento) {
    
	ContentValues values = new ContentValues();
	
	values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getDescrizione());
	values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getIntervento());
	values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getOggetto());
	values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getTipo());
	values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getInizio());
	values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getFine());
	values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getTecnici().toString());
	
	return values;
    }
}
