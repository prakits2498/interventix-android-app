package com.federicocolantoni.projects.interventix.models;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import com.federicocolantoni.projects.interventix.helpers.Constants;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DettagliIntervento")
public class DettaglioIntervento implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3301272979527832708L;

    @DatabaseField(canBeNull = false, useGetSet = false, id = true)
    public Long iddettagliointervento;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public Long idintervento, inizio, fine;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public String tipo, oggetto, descrizione, tecniciintervento, modificato;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public boolean nuovo;

    public DettaglioIntervento() {

	iddettagliointervento = 0L;
	idintervento = 0L;
	inizio = 0L;
	fine = 0L;
	tipo = new String();
	oggetto = new String();
	descrizione = new String();
	try {
	    tecniciintervento = new JSONArray("[]").toString();
	}
	catch (JSONException e) {

	    e.printStackTrace();
	}
	nuovo = false;
	modificato = new String();
    }

    @Override
    public String toString() {

	return String.format("DettaglioIntervento [iddettagliointervento=%s, idintervento=%s, inizio=%s, fine=%s, tipo=%s, oggetto=%s, descrizione=%s, tecniciintervento=%s, modificato=%s, nuovo=%s]",
		iddettagliointervento, idintervento, (new DateTime(inizio).toString(Constants.DATE_PATTERN) + " - " + new DateTime(inizio).toString(Constants.TIME_PATTERN)),
		(new DateTime(fine).toString(Constants.DATE_PATTERN) + " - " + new DateTime(fine).toString(Constants.TIME_PATTERN)), tipo, oggetto, descrizione, tecniciintervento, modificato, nuovo);
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
	result = prime * result + ((modificato == null) ? 0 : modificato.hashCode());
	result = prime * result + (nuovo ? 1231 : 1237);
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
	else if (!descrizione.equals(other.descrizione)) {
	    return false;
	}
	if (fine == null) {
	    if (other.fine != null) {
		return false;
	    }
	}
	else if (!fine.equals(other.fine)) {
	    return false;
	}
	if (iddettagliointervento == null) {
	    if (other.iddettagliointervento != null) {
		return false;
	    }
	}
	else if (!iddettagliointervento.equals(other.iddettagliointervento)) {
	    return false;
	}
	if (idintervento == null) {
	    if (other.idintervento != null) {
		return false;
	    }
	}
	else if (!idintervento.equals(other.idintervento)) {
	    return false;
	}
	if (inizio == null) {
	    if (other.inizio != null) {
		return false;
	    }
	}
	else if (!inizio.equals(other.inizio)) {
	    return false;
	}
	if (modificato == null) {
	    if (other.modificato != null) {
		return false;
	    }
	}
	else if (!modificato.equals(other.modificato)) {
	    return false;
	}
	if (nuovo != other.nuovo) {
	    return false;
	}
	if (oggetto == null) {
	    if (other.oggetto != null) {
		return false;
	    }
	}
	else if (!oggetto.equals(other.oggetto)) {
	    return false;
	}
	if (tecniciintervento == null) {
	    if (other.tecniciintervento != null) {
		return false;
	    }
	}
	else if (!tecniciintervento.equals(other.tecniciintervento)) {
	    return false;
	}
	if (tipo == null) {
	    if (other.tipo != null) {
		return false;
	    }
	}
	else if (!tipo.equals(other.tipo)) {
	    return false;
	}
	return true;
    }
}
