package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.ContentValues;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.Constants;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;

public class Intervento implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6145022542420590261L;
    
    private Long idintervento, dataora, cliente, tecnico, numero;
    private String tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, firma;
    private boolean saldato, cancellato, chiuso, conflitto, nuovo;
    private BigDecimal costomanodopera, costocomponenti, costoaccessori, importo, totale, iva;
    
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
    
    public Intervento(Long mIdIntervento) {
    
	idintervento = mIdIntervento;
    }
    
    /**
     * @return the mIdIntervento
     */
    public Long getIdIntervento() {
    
	return idintervento;
    }
    
    /**
     * @param mIdIntervento
     *            the mIdIntervento to set
     */
    public void setIdIntervento(Long mIdIntervento) {
    
	idintervento = mIdIntervento;
    }
    
    /**
     * @return the mTipologia
     */
    public String getTipologia() {
    
	return tipologia;
    }
    
    /**
     * @param mTipologia
     *            the mTipologia to set
     */
    public void setTipologia(String mTipologia) {
    
	tipologia = mTipologia;
    }
    
    /**
     * @return the mProdotto
     */
    public String getProdotto() {
    
	return prodotto;
    }
    
    /**
     * @param mProdotto
     *            the mProdotto to set
     */
    public void setProdotto(String mProdotto) {
    
	prodotto = mProdotto;
    }
    
    /**
     * @return the mMotivo
     */
    public String getMotivo() {
    
	return motivo;
    }
    
    /**
     * @param mMotivo
     *            the mMotivo to set
     */
    public void setMotivo(String mMotivo) {
    
	motivo = mMotivo;
    }
    
    /**
     * @return the mNominativo
     */
    public String getNominativo() {
    
	return nominativo;
    }
    
    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setNominativo(String mNominativo) {
    
	nominativo = mNominativo;
    }
    
    /**
     * @return the mRifFattura
     */
    public String getRifFattura() {
    
	return riffattura;
    }
    
    /**
     * @param mRifFattura
     *            the mRifFattura to set
     */
    public void setRifFattura(String mRifFattura) {
    
	riffattura = mRifFattura;
    }
    
    /**
     * @return the mRifScontrino
     */
    public String getRifScontrino() {
    
	return rifscontrino;
    }
    
    /**
     * @param mRifScontrino
     *            the mRifScontrino to set
     */
    public void setRifScontrino(String mRifScontrino) {
    
	rifscontrino = mRifScontrino;
    }
    
    /**
     * @return the mNote
     */
    public String getNote() {
    
	return note;
    }
    
    /**
     * @param mNote
     *            the mNote to set
     */
    public void setNote(String mNote) {
    
	note = mNote;
    }
    
    /**
     * @return the mFirma
     */
    public String getFirma() {
    
	return firma;
    }
    
    /**
     * @param mFirma
     *            the mFirma to set
     */
    public void setFirma(String mFirma) {
    
	firma = mFirma;
    }
    
    /**
     * @return the mSaldato
     */
    public boolean isSaldato() {
    
	return saldato;
    }
    
    /**
     * @param mSaldato
     *            the mSaldato to set
     */
    public void setSaldato(boolean mSaldato) {
    
	saldato = mSaldato;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean isCancellato() {
    
	return cancellato;
    }
    
    /**
     * @param mCancellato
     *            the mCancellato to set
     */
    public void setCancellato(boolean mCancellato) {
    
	cancellato = mCancellato;
    }
    
    /**
     * @return the mChiuso
     */
    public boolean isChiuso() {
    
	return chiuso;
    }
    
    /**
     * @param mChiuso
     *            the mChiuso to set
     */
    public void setChiuso(boolean mChiuso) {
    
	chiuso = mChiuso;
    }
    
    /**
     * @return the mDataOra
     */
    public Long getDataOra() {
    
	return dataora;
    }
    
    /**
     * @param mDataOra
     *            the mDataOra to set
     */
    public void setDataOra(Long mDataOra) {
    
	dataora = mDataOra;
    }
    
    /**
     * @return the mIdCliente
     */
    public Long getIdCliente() {
    
	return cliente;
    }
    
    /**
     * @param mIdCliente
     *            the mIdCliente to set
     */
    public void setIdCliente(Long mIdCliente) {
    
	cliente = mIdCliente;
    }
    
    /**
     * @return the mIdTecnico
     */
    public Long getIdTecnico() {
    
	return tecnico;
    }
    
    /**
     * @param mIdTecnico
     *            the mIdTecnico to set
     */
    public void setIdTecnico(Long mIdTecnico) {
    
	tecnico = mIdTecnico;
    }
    
    /**
     * @return the mCostoManodopera
     */
    public BigDecimal getCostoManodopera() {
    
	return costomanodopera;
    }
    
    /**
     * @param mCostoManodopera
     *            the mCostoManodopera to set
     */
    public void setCostoManodopera(BigDecimal mCostoManodopera) {
    
	costomanodopera = mCostoManodopera;
    }
    
    /**
     * @return the mCostoComponenti
     */
    public BigDecimal getCostoComponenti() {
    
	return costocomponenti;
    }
    
    /**
     * @param mCostoComponenti
     *            the mCostoComponenti to set
     */
    public void setCostoComponenti(BigDecimal mCostoComponenti) {
    
	costocomponenti = mCostoComponenti;
    }
    
    /**
     * @return the mCostoAccessori
     */
    public BigDecimal getCostoAccessori() {
    
	return costoaccessori;
    }
    
    /**
     * @param mCostoAccessori
     *            the mCostoAccessori to set
     */
    public void setCostoAccessori(BigDecimal mCostoAccessori) {
    
	costoaccessori = mCostoAccessori;
    }
    
    /**
     * @return the mImporto
     */
    public BigDecimal getImporto() {
    
	return importo;
    }
    
    /**
     * @param mImporto
     *            the mImporto to set
     */
    public void setImporto(BigDecimal mImporto) {
    
	importo = mImporto;
    }
    
    /**
     * @return the mTotale
     */
    public BigDecimal getTotale() {
    
	return totale;
    }
    
    /**
     * @param mTotale
     *            the mTotale to set
     */
    public void setTotale(BigDecimal mTotale) {
    
	totale = mTotale;
    }
    
    /**
     * @return the mIva
     */
    public BigDecimal getIva() {
    
	return iva;
    }
    
    /**
     * @param mIva
     *            the mIva to set
     */
    public void setIva(BigDecimal mIva) {
    
	iva = mIva;
    }
    
    /**
     * @return the mModalita
     */
    public String getModalita() {
    
	return modalita;
    }
    
    /**
     * @param mModalita
     *            the mModalita to set
     */
    public void setModalita(String mModalita) {
    
	modalita = mModalita;
    }
    
    /**
     * @return the mNumeroIntervento
     */
    public Long getNumeroIntervento() {
    
	return numero;
    }
    
    /**
     * @param mNumeroIntervento
     *            the mNumeroIntervento to set
     */
    public void setNumeroIntervento(Long mNumeroIntervento) {
    
	numero = mNumeroIntervento;
    }
    
    /**
     * @return the mConflitto
     */
    public boolean isConflitto() {
    
	return conflitto;
    }
    
    /**
     * @param mConflitto
     *            the mConflitto to set
     */
    public void setConflitto(boolean mConflitto) {
    
	conflitto = mConflitto;
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
    
    @Override
    public String toString() {
    
	return String.format("Intervento [idintervento=%s, dataora=%s, cliente=%s, tecnico=%s, numero=%s, tipologia=%s, prodotto=%s, motivo=%s, nominativo=%s, riffattura=%s, rifscontrino=%s, note=%s, modalita=%s, firma=%s, saldato=%s, cancellato=%s, chiuso=%s, conflitto=%s, nuovo=%s, costomanodopera=%s, costocomponenti=%s, costoaccessori=%s, importo=%s, totale=%s, iva=%s]", idintervento, dataora, cliente, tecnico, numero, tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, firma, saldato, cancellato, chiuso, conflitto, nuovo, costomanodopera, costocomponenti, costoaccessori, importo, totale, iva);
    }
    
    @Override
    public int hashCode() {
    
	final int prime = 31;
	int result = 1;
	result = prime * result + (cancellato ? 1231 : 1237);
	result = prime * result + (chiuso ? 1231 : 1237);
	result = prime * result + (cliente == null ? 0 : cliente.hashCode());
	result = prime * result + (conflitto ? 1231 : 1237);
	result = prime * result + (costoaccessori == null ? 0 : costoaccessori.hashCode());
	result = prime * result + (costocomponenti == null ? 0 : costocomponenti.hashCode());
	result = prime * result + (costomanodopera == null ? 0 : costomanodopera.hashCode());
	result = prime * result + (dataora == null ? 0 : dataora.hashCode());
	result = prime * result + (firma == null ? 0 : firma.hashCode());
	result = prime * result + (idintervento == null ? 0 : idintervento.hashCode());
	result = prime * result + (importo == null ? 0 : importo.hashCode());
	result = prime * result + (iva == null ? 0 : iva.hashCode());
	result = prime * result + (modalita == null ? 0 : modalita.hashCode());
	result = prime * result + (motivo == null ? 0 : motivo.hashCode());
	result = prime * result + (nominativo == null ? 0 : nominativo.hashCode());
	result = prime * result + (note == null ? 0 : note.hashCode());
	result = prime * result + (numero == null ? 0 : numero.hashCode());
	result = prime * result + (nuovo ? 1231 : 1237);
	result = prime * result + (prodotto == null ? 0 : prodotto.hashCode());
	result = prime * result + (riffattura == null ? 0 : riffattura.hashCode());
	result = prime * result + (rifscontrino == null ? 0 : rifscontrino.hashCode());
	result = prime * result + (saldato ? 1231 : 1237);
	result = prime * result + (tecnico == null ? 0 : tecnico.hashCode());
	result = prime * result + (tipologia == null ? 0 : tipologia.hashCode());
	result = prime * result + (totale == null ? 0 : totale.hashCode());
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
	else
	    if (!cliente.equals(other.cliente))
		return false;
	if (conflitto != other.conflitto)
	    return false;
	if (costoaccessori == null) {
	    if (other.costoaccessori != null)
		return false;
	}
	else
	    if (!costoaccessori.equals(other.costoaccessori))
		return false;
	if (costocomponenti == null) {
	    if (other.costocomponenti != null)
		return false;
	}
	else
	    if (!costocomponenti.equals(other.costocomponenti))
		return false;
	if (costomanodopera == null) {
	    if (other.costomanodopera != null)
		return false;
	}
	else
	    if (!costomanodopera.equals(other.costomanodopera))
		return false;
	if (dataora == null) {
	    if (other.dataora != null)
		return false;
	}
	else
	    if (!dataora.equals(other.dataora))
		return false;
	if (firma == null) {
	    if (other.firma != null)
		return false;
	}
	else
	    if (!firma.equals(other.firma))
		return false;
	if (idintervento == null) {
	    if (other.idintervento != null)
		return false;
	}
	else
	    if (!idintervento.equals(other.idintervento))
		return false;
	if (importo == null) {
	    if (other.importo != null)
		return false;
	}
	else
	    if (!importo.equals(other.importo))
		return false;
	if (iva == null) {
	    if (other.iva != null)
		return false;
	}
	else
	    if (!iva.equals(other.iva))
		return false;
	if (modalita == null) {
	    if (other.modalita != null)
		return false;
	}
	else
	    if (!modalita.equals(other.modalita))
		return false;
	if (motivo == null) {
	    if (other.motivo != null)
		return false;
	}
	else
	    if (!motivo.equals(other.motivo))
		return false;
	if (nominativo == null) {
	    if (other.nominativo != null)
		return false;
	}
	else
	    if (!nominativo.equals(other.nominativo))
		return false;
	if (note == null) {
	    if (other.note != null)
		return false;
	}
	else
	    if (!note.equals(other.note))
		return false;
	if (numero == null) {
	    if (other.numero != null)
		return false;
	}
	else
	    if (!numero.equals(other.numero))
		return false;
	if (nuovo != other.nuovo)
	    return false;
	if (prodotto == null) {
	    if (other.prodotto != null)
		return false;
	}
	else
	    if (!prodotto.equals(other.prodotto))
		return false;
	if (riffattura == null) {
	    if (other.riffattura != null)
		return false;
	}
	else
	    if (!riffattura.equals(other.riffattura))
		return false;
	if (rifscontrino == null) {
	    if (other.rifscontrino != null)
		return false;
	}
	else
	    if (!rifscontrino.equals(other.rifscontrino))
		return false;
	if (saldato != other.saldato)
	    return false;
	if (tecnico == null) {
	    if (other.tecnico != null)
		return false;
	}
	else
	    if (!tecnico.equals(other.tecnico))
		return false;
	if (tipologia == null) {
	    if (other.tipologia != null)
		return false;
	}
	else
	    if (!tipologia.equals(other.tipologia))
		return false;
	if (totale == null) {
	    if (other.totale != null)
		return false;
	}
	else
	    if (!totale.equals(other.totale))
		return false;
	return true;
    }
    
    public static ContentValues insertSQL(Intervento intervento, boolean sincronizzato) {
    
	ContentValues values = new ContentValues();
	
	values.put(InterventoDB.Fields.ID_INTERVENTO, intervento.getIdIntervento());
	values.put(Fields.TYPE, InterventoDB.INTERVENTO_ITEM_TYPE);
	values.put(InterventoDB.Fields.CANCELLATO, intervento.isCancellato());
	values.put(InterventoDB.Fields.COSTO_ACCESSORI, intervento.getCostoAccessori().doubleValue());
	values.put(InterventoDB.Fields.COSTO_COMPONENTI, intervento.getCostoComponenti().doubleValue());
	values.put(InterventoDB.Fields.COSTO_MANODOPERA, intervento.getCostoManodopera().doubleValue());
	values.put(InterventoDB.Fields.DATA_ORA, intervento.getDataOra());
	values.put(InterventoDB.Fields.FIRMA, intervento.getFirma());
	values.put(InterventoDB.Fields.CLIENTE, intervento.getIdCliente());
	values.put(InterventoDB.Fields.IMPORTO, intervento.getImporto().doubleValue());
	values.put(InterventoDB.Fields.IVA, intervento.getIva().doubleValue());
	values.put(InterventoDB.Fields.MODALITA, intervento.getModalita());
	
	if (!sincronizzato)
	    values.put(InterventoDB.Fields.NUOVO, Constants.INTERVENTO_NUOVO);
	
	if (sincronizzato)
	    values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_SINCRONIZZATO);
	else
	    values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);
	
	values.put(InterventoDB.Fields.MOTIVO, intervento.getMotivo());
	values.put(InterventoDB.Fields.NOMINATIVO, intervento.getNominativo());
	values.put(InterventoDB.Fields.NOTE, intervento.getNote());
	values.put(InterventoDB.Fields.NUMERO_INTERVENTO, intervento.getNumeroIntervento());
	values.put(InterventoDB.Fields.PRODOTTO, intervento.getProdotto());
	values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, intervento.getRifFattura());
	values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, intervento.getRifScontrino());
	values.put(InterventoDB.Fields.SALDATO, intervento.isSaldato());
	values.put(InterventoDB.Fields.TIPOLOGIA, intervento.getTipologia());
	values.put(InterventoDB.Fields.TOTALE, intervento.getTotale().doubleValue());
	values.put(InterventoDB.Fields.CHIUSO, intervento.isChiuso());
	values.put(InterventoDB.Fields.TECNICO, intervento.getIdTecnico());
	
	return values;
    }
    
    public static ContentValues updateSQL(Intervento intervento, boolean sincronizzato) {
    
	ContentValues values = new ContentValues();
	
	values.put(InterventoDB.Fields.CANCELLATO, intervento.isCancellato());
	values.put(InterventoDB.Fields.COSTO_ACCESSORI, intervento.getCostoAccessori().doubleValue());
	values.put(InterventoDB.Fields.COSTO_COMPONENTI, intervento.getCostoComponenti().doubleValue());
	values.put(InterventoDB.Fields.COSTO_MANODOPERA, intervento.getCostoManodopera().doubleValue());
	values.put(InterventoDB.Fields.DATA_ORA, intervento.getDataOra());
	values.put(InterventoDB.Fields.FIRMA, intervento.getFirma());
	values.put(InterventoDB.Fields.CLIENTE, intervento.getIdCliente());
	values.put(InterventoDB.Fields.IMPORTO, intervento.getImporto().doubleValue());
	values.put(InterventoDB.Fields.IVA, intervento.getIva().doubleValue());
	values.put(InterventoDB.Fields.MODALITA, intervento.getModalita());
	
	if (sincronizzato)
	    values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_SINCRONIZZATO);
	else
	    values.put(InterventoDB.Fields.MODIFICATO, Constants.INTERVENTO_MODIFICATO);
	
	values.put(InterventoDB.Fields.MOTIVO, intervento.getMotivo());
	values.put(InterventoDB.Fields.NOMINATIVO, intervento.getNominativo());
	values.put(InterventoDB.Fields.NOTE, intervento.getNote());
	values.put(InterventoDB.Fields.NUMERO_INTERVENTO, intervento.getNumeroIntervento());
	values.put(InterventoDB.Fields.PRODOTTO, intervento.getProdotto());
	values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, intervento.getRifFattura());
	values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, intervento.getRifScontrino());
	values.put(InterventoDB.Fields.SALDATO, intervento.isSaldato());
	values.put(InterventoDB.Fields.TIPOLOGIA, intervento.getTipologia());
	values.put(InterventoDB.Fields.TOTALE, intervento.getTotale().doubleValue());
	values.put(InterventoDB.Fields.CHIUSO, intervento.isChiuso());
	values.put(InterventoDB.Fields.TECNICO, intervento.getIdTecnico());
	
	return values;
    }
    
    public static Intervento getFromCursor(Cursor cursor) {
    
	Intervento intervento = new Intervento();
	
	intervento.setCancellato(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CANCELLATO)) == 1 ? true : false);
	intervento.setChiuso(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CHIUSO)) == 1 ? true : false);
	intervento.setConflitto(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.CONFLITTO)) == 1 ? true : false);
	intervento.setCostoAccessori(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_ACCESSORI))));
	intervento.setCostoComponenti(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_COMPONENTI))));
	intervento.setCostoManodopera(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.COSTO_MANODOPERA))));
	intervento.setDataOra(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.DATA_ORA)));
	intervento.setFirma(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.FIRMA)));
	intervento.setIdCliente(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.CLIENTE)));
	intervento.setIdIntervento(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
	intervento.setIdTecnico(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.TECNICO)));
	intervento.setImporto(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IMPORTO))));
	intervento.setIdIntervento(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.ID_INTERVENTO)));
	intervento.setIva(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.IVA))));
	intervento.setModalita(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODALITA)));
	intervento.setMotivo(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MOTIVO)));
	intervento.setNominativo(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOMINATIVO)));
	intervento.setNote(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.NOTE)));
	intervento.setNumeroIntervento(cursor.getLong(cursor.getColumnIndex(InterventoDB.Fields.NUMERO_INTERVENTO)));
	intervento.setProdotto(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.PRODOTTO)));
	intervento.setRifFattura(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_FATTURA)));
	intervento.setRifScontrino(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.RIFERIMENTO_SCONTRINO)));
	intervento.setSaldato(cursor.getInt(cursor.getColumnIndex(InterventoDB.Fields.SALDATO)) == 1 ? true : false);
	intervento.setTipologia(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.TIPOLOGIA)));
	intervento.setTotale(BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(InterventoDB.Fields.TOTALE))));
	
	return intervento;
    }
}
