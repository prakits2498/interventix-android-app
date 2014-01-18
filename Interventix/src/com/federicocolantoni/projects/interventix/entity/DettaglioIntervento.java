package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;

public class DettaglioIntervento implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 3301272979527832708L;

	private Long iddettagliointervento;
	private String tipo, oggetto, descrizione, modificato;
	private Long idintervento;
	private Long inizio, fine;
	private String tecniciintervento;

	public DettaglioIntervento() {

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

	/**
	 * @return the mModificato
	 */
	public String getModificato() {

		return modificato;
	}

	/**
	 * @param mModificato
	 *            the mModificato to set
	 */
	public void setModificato(String mModificato) {

		this.modificato = mModificato;
	}

	@Override
	public String toString() {

		return String.format("DettaglioIntervento [idDettaglioIntervento=%s, tipo=%s, oggetto=%s, descrizione=%s, modificato=%s, intervento=%s, inizio=%s, fine=%s, tecnici=%s]",
				iddettagliointervento, tipo, oggetto, descrizione, modificato, idintervento, inizio, fine, tecniciintervento);
	}

	public static ContentValues insertSQL(DettaglioIntervento dettIntervento) {

		ContentValues values = new ContentValues();

		values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, dettIntervento.getIdDettaglioIntervento());
		values.put(DettaglioInterventoDB.Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
		values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getDescrizione());
		values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getIntervento());
		values.put(DettaglioInterventoDB.Fields.MODIFICATO, dettIntervento.getModificato());
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
		values.put(DettaglioInterventoDB.Fields.MODIFICATO, dettIntervento.getModificato());
		values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getOggetto());
		values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getTipo());
		values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getInizio());
		values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getFine());
		values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getTecnici().toString());

		return values;
	}
}
