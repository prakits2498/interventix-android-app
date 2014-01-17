package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.ContentValues;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;

public class Intervento implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6145022542420590261L;
    
    private Long idintervento;
    private String tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, modificato;
    private String firma;
    private boolean saldato, cancellato, chiuso, conflitto;
    private Long dataora;
    private Long cliente;
    private Long tecnico;
    private BigDecimal costomanodopera, costocomponenti, costoaccessori, importo, totale;
    private BigDecimal iva;
    private Long numero;
    
    public Intervento() {
    
    }
    
    public Intervento(Long mIdIntervento) {
    
	this.idintervento = mIdIntervento;
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
    
	this.idintervento = mIdIntervento;
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
    
	this.tipologia = mTipologia;
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
    
	this.prodotto = mProdotto;
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
    
	this.motivo = mMotivo;
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
    
	this.nominativo = mNominativo;
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
    
	this.riffattura = mRifFattura;
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
    
	this.rifscontrino = mRifScontrino;
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
    
	this.note = mNote;
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
    
	this.firma = mFirma;
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
    
	this.saldato = mSaldato;
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
    
	this.cancellato = mCancellato;
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
    
	this.chiuso = mChiuso;
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
    
	this.dataora = mDataOra;
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
    
	this.cliente = mIdCliente;
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
    
	this.tecnico = mIdTecnico;
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
    
	this.costomanodopera = mCostoManodopera;
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
    
	this.costocomponenti = mCostoComponenti;
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
    
	this.costoaccessori = mCostoAccessori;
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
    
	this.importo = mImporto;
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
    
	this.totale = mTotale;
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
    
	this.iva = mIva;
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
    
	this.modalita = mModalita;
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
    
	this.numero = mNumeroIntervento;
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
    
	this.conflitto = mConflitto;
    }
    
    @Override
    public String toString() {
    
	return String.format("Intervento [idintervento=%s, tipologia=%s, prodotto=%s, motivo=%s, nominativo=%s, riffattura=%s, rifscontrino=%s, note=%s, modalita=%s, modificato=%s, firma=%s, saldato=%s, cancellato=%s, chiuso=%s, conflitto=%s, dataora=%s, cliente=%s, tecnico=%s, costomanodopera=%s, costocomponenti=%s, costoaccessori=%s, importo=%s, totale=%s, iva=%s, numero=%s]", idintervento, tipologia, prodotto, motivo, nominativo, riffattura, rifscontrino, note, modalita, modificato, firma, saldato, cancellato, chiuso, conflitto, dataora, cliente, tecnico, costomanodopera, costocomponenti, costoaccessori, importo, totale, iva, numero);
    }
    
    public static ContentValues insertSQL(Intervento intervento) {
    
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
	values.put(InterventoDB.Fields.MODIFICATO, intervento.getModificato());
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
    
    public static ContentValues updateSQL(Intervento intervento) {
    
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
	values.put(InterventoDB.Fields.MODIFICATO, intervento.getModificato());
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
	intervento.setModificato(cursor.getString(cursor.getColumnIndex(InterventoDB.Fields.MODIFICATO)));
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
