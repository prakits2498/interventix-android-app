package com.federicocolantoni.projects.interventix.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Interventi")
public class Intervento implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6145022542420590261L;

    @DatabaseField(canBeNull = false, useGetSet = false, id = true)
    public Long idintervento;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public Long dataora, cliente, tecnico, numero;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public String tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, firma, modificato;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public boolean saldato, cancellato, chiuso, conflitto, nuovo;

    @DatabaseField(canBeNull = true, useGetSet = false)
    public BigDecimal costomanodopera, costocomponenti, costoaccessori, importo, totale, iva;

    public Intervento() {

	idintervento = 0L;
	tipologia = new String();
	prodotto = new String();
	nominativo = new String();
	riffattura = new String();
	rifscontrino = new String();
	note = new String();
	modalita = new String();
	firma = new String();
	saldato = false;
	cancellato = false;
	chiuso = false;
	conflitto = false;
	nuovo = false;
	dataora = 0L;
	cliente = 0L;
	tecnico = 0L;
	numero = 0L;
	costomanodopera = new BigDecimal(0.0);
	costocomponenti = new BigDecimal(0.0);
	costoaccessori = new BigDecimal(0.0);
	importo = new BigDecimal(0.0);
	totale = new BigDecimal(0.0);
	iva = new BigDecimal(0.0);
    }

    @Override
    public String toString() {

	return String
		.format("Intervento [idintervento=%s, dataora=%s, cliente=%s, tecnico=%s, numero=%s, tipologia=%s, prodotto=%s, motivo=%s, nominativo=%s, riffattura=%s, rifscontrino=%s, note=%s, modalita=%s, firma=%s, modificato=%s, saldato=%s, cancellato=%s, chiuso=%s, conflitto=%s, nuovo=%s, costomanodopera=%s, costocomponenti=%s, costoaccessori=%s, importo=%s, totale=%s, iva=%s]",
			idintervento, dataora, cliente, tecnico, numero, tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, firma.substring(0, 49) + "...", modificato,
			saldato, cancellato, chiuso, conflitto, nuovo, costomanodopera.round(new MathContext(2)).doubleValue(), costocomponenti.round(new MathContext(2)).doubleValue(), costoaccessori
				.round(new MathContext(2)).doubleValue(), importo.round(new MathContext(2)).doubleValue(), totale.doubleValue(), iva.round(new MathContext(2)).doubleValue());
    }

    @Override
    public int hashCode() {

	final int prime = 31;
	int result = 1;
	result = prime * result + (cancellato ? 1231 : 1237);
	result = prime * result + (chiuso ? 1231 : 1237);
	result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
	result = prime * result + (conflitto ? 1231 : 1237);
	result = prime * result + ((costoaccessori == null) ? 0 : costoaccessori.hashCode());
	result = prime * result + ((costocomponenti == null) ? 0 : costocomponenti.hashCode());
	result = prime * result + ((costomanodopera == null) ? 0 : costomanodopera.hashCode());
	result = prime * result + ((dataora == null) ? 0 : dataora.hashCode());
	result = prime * result + ((firma == null) ? 0 : firma.hashCode());
	result = prime * result + ((idintervento == null) ? 0 : idintervento.hashCode());
	result = prime * result + ((importo == null) ? 0 : importo.hashCode());
	result = prime * result + ((iva == null) ? 0 : iva.hashCode());
	result = prime * result + ((modalita == null) ? 0 : modalita.hashCode());
	result = prime * result + ((modificato == null) ? 0 : modificato.hashCode());
	result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
	result = prime * result + ((nominativo == null) ? 0 : nominativo.hashCode());
	result = prime * result + ((note == null) ? 0 : note.hashCode());
	result = prime * result + ((numero == null) ? 0 : numero.hashCode());
	result = prime * result + (nuovo ? 1231 : 1237);
	result = prime * result + ((prodotto == null) ? 0 : prodotto.hashCode());
	result = prime * result + ((riffattura == null) ? 0 : riffattura.hashCode());
	result = prime * result + ((rifscontrino == null) ? 0 : rifscontrino.hashCode());
	result = prime * result + (saldato ? 1231 : 1237);
	result = prime * result + ((tecnico == null) ? 0 : tecnico.hashCode());
	result = prime * result + ((tipologia == null) ? 0 : tipologia.hashCode());
	result = prime * result + ((totale == null) ? 0 : totale.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {

	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof Intervento))
	    return false;
	Intervento other = (Intervento) obj;
	if (cancellato != other.cancellato)
	    return false;
	if (chiuso != other.chiuso)
	    return false;
	if (cliente == null) {
	    if (other.cliente != null)
		return false;
	}
	else if (!cliente.equals(other.cliente))
	    return false;
	if (conflitto != other.conflitto)
	    return false;
	if (costoaccessori == null) {
	    if (other.costoaccessori != null)
		return false;
	}
	else if (!costoaccessori.equals(other.costoaccessori))
	    return false;
	if (costocomponenti == null) {
	    if (other.costocomponenti != null)
		return false;
	}
	else if (!costocomponenti.equals(other.costocomponenti))
	    return false;
	if (costomanodopera == null) {
	    if (other.costomanodopera != null)
		return false;
	}
	else if (!costomanodopera.equals(other.costomanodopera))
	    return false;
	if (dataora == null) {
	    if (other.dataora != null)
		return false;
	}
	else if (!dataora.equals(other.dataora))
	    return false;
	if (firma == null) {
	    if (other.firma != null)
		return false;
	}
	else if (!firma.equals(other.firma))
	    return false;
	if (idintervento == null) {
	    if (other.idintervento != null)
		return false;
	}
	else if (!idintervento.equals(other.idintervento))
	    return false;
	if (importo == null) {
	    if (other.importo != null)
		return false;
	}
	else if (!importo.equals(other.importo))
	    return false;
	if (iva == null) {
	    if (other.iva != null)
		return false;
	}
	else if (!iva.equals(other.iva))
	    return false;
	if (modalita == null) {
	    if (other.modalita != null)
		return false;
	}
	else if (!modalita.equals(other.modalita))
	    return false;
	if (modificato == null) {
	    if (other.modificato != null)
		return false;
	}
	else if (!modificato.equals(other.modificato))
	    return false;
	if (motivo == null) {
	    if (other.motivo != null)
		return false;
	}
	else if (!motivo.equals(other.motivo))
	    return false;
	if (nominativo == null) {
	    if (other.nominativo != null)
		return false;
	}
	else if (!nominativo.equals(other.nominativo))
	    return false;
	if (note == null) {
	    if (other.note != null)
		return false;
	}
	else if (!note.equals(other.note))
	    return false;
	if (numero == null) {
	    if (other.numero != null)
		return false;
	}
	else if (!numero.equals(other.numero))
	    return false;
	if (nuovo != other.nuovo)
	    return false;
	if (prodotto == null) {
	    if (other.prodotto != null)
		return false;
	}
	else if (!prodotto.equals(other.prodotto))
	    return false;
	if (riffattura == null) {
	    if (other.riffattura != null)
		return false;
	}
	else if (!riffattura.equals(other.riffattura))
	    return false;
	if (rifscontrino == null) {
	    if (other.rifscontrino != null)
		return false;
	}
	else if (!rifscontrino.equals(other.rifscontrino))
	    return false;
	if (saldato != other.saldato)
	    return false;
	if (tecnico == null) {
	    if (other.tecnico != null)
		return false;
	}
	else if (!tecnico.equals(other.tecnico))
	    return false;
	if (tipologia == null) {
	    if (other.tipologia != null)
		return false;
	}
	else if (!tipologia.equals(other.tipologia))
	    return false;
	if (totale == null) {
	    if (other.totale != null)
		return false;
	}
	else if (!totale.equals(other.totale))
	    return false;
	return true;
    }
}
