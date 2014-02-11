package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.ContentValues;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Interventi")
public class Intervento implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -6145022542420590261L;

	@DatabaseField(canBeNull = true, useGetSet = false, id = true)
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
						idintervento, dataora, cliente, tecnico, numero, tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, firma, modificato,
						saldato, cancellato, chiuso, conflitto, nuovo, costomanodopera, costocomponenti, costoaccessori, importo, totale, iva);
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
		} else if (!cliente.equals(other.cliente))
			return false;
		if (conflitto != other.conflitto)
			return false;
		if (costoaccessori == null) {
			if (other.costoaccessori != null)
				return false;
		} else if (!costoaccessori.equals(other.costoaccessori))
			return false;
		if (costocomponenti == null) {
			if (other.costocomponenti != null)
				return false;
		} else if (!costocomponenti.equals(other.costocomponenti))
			return false;
		if (costomanodopera == null) {
			if (other.costomanodopera != null)
				return false;
		} else if (!costomanodopera.equals(other.costomanodopera))
			return false;
		if (dataora == null) {
			if (other.dataora != null)
				return false;
		} else if (!dataora.equals(other.dataora))
			return false;
		if (firma == null) {
			if (other.firma != null)
				return false;
		} else if (!firma.equals(other.firma))
			return false;
		if (idintervento == null) {
			if (other.idintervento != null)
				return false;
		} else if (!idintervento.equals(other.idintervento))
			return false;
		if (importo == null) {
			if (other.importo != null)
				return false;
		} else if (!importo.equals(other.importo))
			return false;
		if (iva == null) {
			if (other.iva != null)
				return false;
		} else if (!iva.equals(other.iva))
			return false;
		if (modalita == null) {
			if (other.modalita != null)
				return false;
		} else if (!modalita.equals(other.modalita))
			return false;
		if (modificato == null) {
			if (other.modificato != null)
				return false;
		} else if (!modificato.equals(other.modificato))
			return false;
		if (motivo == null) {
			if (other.motivo != null)
				return false;
		} else if (!motivo.equals(other.motivo))
			return false;
		if (nominativo == null) {
			if (other.nominativo != null)
				return false;
		} else if (!nominativo.equals(other.nominativo))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (nuovo != other.nuovo)
			return false;
		if (prodotto == null) {
			if (other.prodotto != null)
				return false;
		} else if (!prodotto.equals(other.prodotto))
			return false;
		if (riffattura == null) {
			if (other.riffattura != null)
				return false;
		} else if (!riffattura.equals(other.riffattura))
			return false;
		if (rifscontrino == null) {
			if (other.rifscontrino != null)
				return false;
		} else if (!rifscontrino.equals(other.rifscontrino))
			return false;
		if (saldato != other.saldato)
			return false;
		if (tecnico == null) {
			if (other.tecnico != null)
				return false;
		} else if (!tecnico.equals(other.tecnico))
			return false;
		if (tipologia == null) {
			if (other.tipologia != null)
				return false;
		} else if (!tipologia.equals(other.tipologia))
			return false;
		if (totale == null) {
			if (other.totale != null)
				return false;
		} else if (!totale.equals(other.totale))
			return false;
		return true;
	}

	public static ContentValues insertSQL(Intervento intervento, boolean sincronizzato) {

		ContentValues values = new ContentValues();

		values.put(InterventoDB.Fields.ID_INTERVENTO, intervento.idintervento);
		values.put(Fields.TYPE, InterventoDB.INTERVENTO_ITEM_TYPE);
		values.put(InterventoDB.Fields.CANCELLATO, intervento.cancellato);
		values.put(InterventoDB.Fields.COSTO_ACCESSORI, intervento.costoaccessori.doubleValue());
		values.put(InterventoDB.Fields.COSTO_COMPONENTI, intervento.costocomponenti.doubleValue());
		values.put(InterventoDB.Fields.COSTO_MANODOPERA, intervento.costomanodopera.doubleValue());
		values.put(InterventoDB.Fields.DATA_ORA, intervento.dataora);
		values.put(InterventoDB.Fields.FIRMA, intervento.firma);
		values.put(InterventoDB.Fields.CLIENTE, intervento.cliente);
		values.put(InterventoDB.Fields.IMPORTO, intervento.importo.doubleValue());
		values.put(InterventoDB.Fields.IVA, intervento.iva.doubleValue());
		values.put(InterventoDB.Fields.MODALITA, intervento.modalita);

		if (sincronizzato)
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_SINCRONIZZATO);
		else
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);

		values.put(InterventoDB.Fields.MOTIVO, intervento.motivo);
		values.put(InterventoDB.Fields.NOMINATIVO, intervento.nominativo);
		values.put(InterventoDB.Fields.NOTE, intervento.note);
		values.put(InterventoDB.Fields.NUMERO_INTERVENTO, intervento.numero);

		if (!sincronizzato)
			values.put(InterventoDB.Fields.NUOVO, Constants.INTERVENTO_NUOVO);

		values.put(InterventoDB.Fields.PRODOTTO, intervento.prodotto);
		values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, intervento.riffattura);
		values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, intervento.rifscontrino);
		values.put(InterventoDB.Fields.SALDATO, intervento.saldato);
		values.put(InterventoDB.Fields.TIPOLOGIA, intervento.tipologia);
		values.put(InterventoDB.Fields.TOTALE, intervento.totale.doubleValue());
		values.put(InterventoDB.Fields.CHIUSO, intervento.chiuso);
		values.put(InterventoDB.Fields.TECNICO, intervento.tecnico);

		return values;
	}

	public static ContentValues updateSQL(Intervento intervento, boolean sincronizzato) {

		ContentValues values = new ContentValues();

		values.put(InterventoDB.Fields.CANCELLATO, intervento.cancellato);
		values.put(InterventoDB.Fields.COSTO_ACCESSORI, intervento.costoaccessori.doubleValue());
		values.put(InterventoDB.Fields.COSTO_COMPONENTI, intervento.costocomponenti.doubleValue());
		values.put(InterventoDB.Fields.COSTO_MANODOPERA, intervento.costomanodopera.doubleValue());
		values.put(InterventoDB.Fields.DATA_ORA, intervento.dataora);
		values.put(InterventoDB.Fields.FIRMA, intervento.firma);
		values.put(InterventoDB.Fields.CLIENTE, intervento.cliente);
		values.put(InterventoDB.Fields.IMPORTO, intervento.importo.doubleValue());
		values.put(InterventoDB.Fields.IVA, intervento.iva.doubleValue());
		values.put(InterventoDB.Fields.MODALITA, intervento.modalita);

		if (sincronizzato)
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_SINCRONIZZATO);
		else
			values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);

		values.put(InterventoDB.Fields.MOTIVO, intervento.motivo);
		values.put(InterventoDB.Fields.NOMINATIVO, intervento.nominativo);
		values.put(InterventoDB.Fields.NOTE, intervento.note);
		values.put(InterventoDB.Fields.NUMERO_INTERVENTO, intervento.numero);
		values.put(InterventoDB.Fields.PRODOTTO, intervento.prodotto);
		values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, intervento.riffattura);
		values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, intervento.rifscontrino);
		values.put(InterventoDB.Fields.SALDATO, intervento.saldato);
		values.put(InterventoDB.Fields.TIPOLOGIA, intervento.tipologia);
		values.put(InterventoDB.Fields.TOTALE, intervento.totale.doubleValue());
		values.put(InterventoDB.Fields.CHIUSO, intervento.chiuso);
		values.put(InterventoDB.Fields.TECNICO, intervento.tecnico);

		return values;
	}

	public static Intervento getFromCursor(Cursor cursor) {

		Intervento intervento = new Intervento();

		intervento.cancellato = (cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CANCELLATO)) == 1 ? true : false);
		intervento.chiuso = (cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CHIUSO)) == 1 ? true : false);
		intervento.conflitto = (cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CONFLITTO)) == 1 ? true : false);
		intervento.costoaccessori = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
		intervento.costocomponenti = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
		intervento.costomanodopera = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
		intervento.dataora = (cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
		intervento.firma = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.FIRMA)));
		intervento.cliente = (cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.CLIENTE)));
		intervento.idintervento = (cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
		intervento.tecnico = (cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.TECNICO)));
		intervento.importo = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IMPORTO))));
		intervento.iva = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IVA))));
		intervento.modalita = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODALITA)));
		intervento.motivo = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MOTIVO)));
		intervento.nominativo = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
		intervento.note = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOTE)));
		intervento.numero = (cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO)));
		intervento.prodotto = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.PRODOTTO)));
		intervento.riffattura = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_FATTURA)));
		intervento.rifscontrino = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_SCONTRINO)));
		intervento.saldato = (cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.SALDATO)) == 1 ? true : false);
		intervento.tipologia = (cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
		intervento.totale = (BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.TOTALE))));

		return intervento;
	}
}
