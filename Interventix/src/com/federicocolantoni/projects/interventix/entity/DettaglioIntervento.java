package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import org.json.JSONArray;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.DettaglioInterventoDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.roscopeco.ormdroid.Entity;
import com.roscopeco.ormdroid.Table;

@Table(name = "DettaglioIntervento")
public class DettaglioIntervento extends Entity implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 3301272979527832708L;

	private Long iddettagliointervento, idintervento, inizio, fine;
	private String tipo, oggetto, descrizione, tecniciintervento, modificato;
	private boolean nuovo;

	public DettaglioIntervento() {

		iddettagliointervento = 0L;
		idintervento = 0L;
		inizio = 0L;
		fine = 0L;
		tipo = new String();
		oggetto = new String();
		descrizione = new String();
		tecniciintervento = new JSONArray().toString();
		nuovo = false;
	}

	public DettaglioIntervento(Long idDettaglioIntervento) {

		iddettagliointervento = idDettaglioIntervento;
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

		iddettagliointervento = mIdDettaglioIntervento;
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

		tipo = mTipo;
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

		oggetto = mOggetto;
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

		descrizione = mDescrizione;
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

		idintervento = mIntervento;
	}

	public Long getInizio() {

		return inizio;
	}

	public void setInizio(Long mInizio) {

		inizio = mInizio;
	}

	public Long getFine() {

		return fine;
	}

	public void setFine(Long mFine) {

		fine = mFine;
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

		tecniciintervento = mTecnici;
	}

	/**
	 * @return the nuovo
	 */
	public boolean isNuovo() {

		return nuovo;
	}

	/**
	 * @param nuovo
	 *            the nuovo to set
	 */
	public void setNuovo(boolean nuovo) {

		this.nuovo = nuovo;
	}

	/**
	 * @return the modificato
	 */
	public String getModificato() {

		return modificato;
	}

	/**
	 * @param modificato
	 *            the modificato to set
	 */
	public void setModificato(String modificato) {

		this.modificato = modificato;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return String
				.format("DettaglioIntervento [iddettagliointervento=%s, idintervento=%s, inizio=%s, fine=%s, tipo=%s, oggetto=%s, descrizione=%s, tecniciintervento=%s, modificato=%s, nuovo=%s]",
						iddettagliointervento, idintervento, inizio, fine, tipo, oggetto, descrizione, tecniciintervento, modificato, nuovo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((descrizione == null) ? 0 : descrizione.hashCode());
		result = prime * result + ((fine == null) ? 0 : fine.hashCode());
		result = prime * result + ((iddettagliointervento == null) ? 0 : iddettagliointervento.hashCode());
		result = prime * result + ((idintervento == null) ? 0 : idintervento.hashCode());
		result = prime * result + ((inizio == null) ? 0 : inizio.hashCode());
		result = prime * result + ((modificato == null) ? 0 : modificato.hashCode());
		result = prime * result + (nuovo ? 1231 : 1237);
		result = prime * result + ((oggetto == null) ? 0 : oggetto.hashCode());
		result = prime * result + ((tecniciintervento == null) ? 0 : tecniciintervento.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		} else if (!descrizione.equals(other.descrizione)) {
			return false;
		}
		if (fine == null) {
			if (other.fine != null) {
				return false;
			}
		} else if (!fine.equals(other.fine)) {
			return false;
		}
		if (iddettagliointervento == null) {
			if (other.iddettagliointervento != null) {
				return false;
			}
		} else if (!iddettagliointervento.equals(other.iddettagliointervento)) {
			return false;
		}
		if (idintervento == null) {
			if (other.idintervento != null) {
				return false;
			}
		} else if (!idintervento.equals(other.idintervento)) {
			return false;
		}
		if (inizio == null) {
			if (other.inizio != null) {
				return false;
			}
		} else if (!inizio.equals(other.inizio)) {
			return false;
		}
		if (modificato == null) {
			if (other.modificato != null) {
				return false;
			}
		} else if (!modificato.equals(other.modificato)) {
			return false;
		}
		if (nuovo != other.nuovo) {
			return false;
		}
		if (oggetto == null) {
			if (other.oggetto != null) {
				return false;
			}
		} else if (!oggetto.equals(other.oggetto)) {
			return false;
		}
		if (tecniciintervento == null) {
			if (other.tecniciintervento != null) {
				return false;
			}
		} else if (!tecniciintervento.equals(other.tecniciintervento)) {
			return false;
		}
		if (tipo == null) {
			if (other.tipo != null) {
				return false;
			}
		} else if (!tipo.equals(other.tipo)) {
			return false;
		}
		return true;
	}

	public static ContentValues insertSQL(DettaglioIntervento dettIntervento, boolean sincronizzato, boolean nuovo) {

		ContentValues values = new ContentValues();

		values.put(DettaglioInterventoDB.Fields.ID_DETTAGLIO_INTERVENTO, dettIntervento.getIdDettaglioIntervento());
		values.put(Fields.TYPE, DettaglioInterventoDB.DETTAGLIO_INTERVENTO_ITEM_TYPE);
		values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getDescrizione());
		values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getIntervento());

		if (sincronizzato)
			values.put(DettaglioInterventoDB.Fields.MODIFICATO, Constants.DETTAGLIO_SINCRONIZZATO);
		else
			values.put(DettaglioInterventoDB.Fields.MODIFICATO, Constants.DETTAGLIO_MODIFICATO);

		if (nuovo) {
			values.put(DettaglioInterventoDB.Fields.NUOVO, Constants.DETTAGLIO_NUOVO);
			values.put(DettaglioInterventoDB.Fields.MODIFICATO, "");
		}

		values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getOggetto());
		values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getTipo());
		values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getInizio());
		values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getFine());
		values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getTecnici().toString());

		return values;
	}

	public static ContentValues updateSQL(DettaglioIntervento dettIntervento, boolean sincronizzato, boolean nuovo) {

		ContentValues values = new ContentValues();

		values.put(DettaglioInterventoDB.Fields.DESCRIZIONE, dettIntervento.getDescrizione());
		values.put(DettaglioInterventoDB.Fields.INTERVENTO, dettIntervento.getIntervento());

		if (sincronizzato)
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_SINCRONIZZATO);
		else
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);

		if (nuovo) {
			values.put(DettaglioInterventoDB.Fields.NUOVO, Constants.DETTAGLIO_NUOVO);
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);
		}

		values.put(DettaglioInterventoDB.Fields.OGGETTO, dettIntervento.getOggetto());
		values.put(DettaglioInterventoDB.Fields.TIPO, dettIntervento.getTipo());
		values.put(DettaglioInterventoDB.Fields.INIZIO, dettIntervento.getInizio());
		values.put(DettaglioInterventoDB.Fields.FINE, dettIntervento.getFine());
		values.put(DettaglioInterventoDB.Fields.TECNICI, dettIntervento.getTecnici().toString());

		return values;
	}
}
