package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import android.content.ContentValues;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.InterventoDB;

public class Intervento implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6145022542420590261L;
    
    private Long mIdIntervento;
    private String mTipologia, mProdotto, mMotivo, mNominativo, mRifFattura,
	    mRifScontrino, mNote, mModalita, mModificato;
    private String mFirma;
    private boolean mSaldato, mCancellato, mChiuso, mConflitto;
    private Long mDataOra;
    private Long mIdCliente;
    private Long mIdTecnico;
    private BigDecimal mCostoManodopera, mCostoComponenti, mCostoAccessori,
	    mImporto, mTotale;
    private BigDecimal mIva;
    private Long mNumeroIntervento;
    
    public Intervento() {
	
    }
    
    public Intervento(Long mIdIntervento) {
	
	this.mIdIntervento = mIdIntervento;
    }
    
    /**
     * @return the mIdIntervento
     */
    public Long getIdIntervento() {
	
	return mIdIntervento;
    }
    
    /**
     * @param mIdIntervento
     *            the mIdIntervento to set
     */
    public void setIdIntervento(Long mIdIntervento) {
	
	this.mIdIntervento = mIdIntervento;
    }
    
    /**
     * @return the mTipologia
     */
    public String getTipologia() {
	
	return mTipologia;
    }
    
    /**
     * @param mTipologia
     *            the mTipologia to set
     */
    public void setTipologia(String mTipologia) {
	
	this.mTipologia = mTipologia;
    }
    
    /**
     * @return the mProdotto
     */
    public String getProdotto() {
	
	return mProdotto;
    }
    
    /**
     * @param mProdotto
     *            the mProdotto to set
     */
    public void setProdotto(String mProdotto) {
	
	this.mProdotto = mProdotto;
    }
    
    /**
     * @return the mMotivo
     */
    public String getMotivo() {
	
	return mMotivo;
    }
    
    /**
     * @param mMotivo
     *            the mMotivo to set
     */
    public void setMotivo(String mMotivo) {
	
	this.mMotivo = mMotivo;
    }
    
    /**
     * @return the mNominativo
     */
    public String getNominativo() {
	
	return mNominativo;
    }
    
    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setNominativo(String mNominativo) {
	
	this.mNominativo = mNominativo;
    }
    
    /**
     * @return the mRifFattura
     */
    public String getRifFattura() {
	
	return mRifFattura;
    }
    
    /**
     * @param mRifFattura
     *            the mRifFattura to set
     */
    public void setRifFattura(String mRifFattura) {
	
	this.mRifFattura = mRifFattura;
    }
    
    /**
     * @return the mRifScontrino
     */
    public String getRifScontrino() {
	
	return mRifScontrino;
    }
    
    /**
     * @param mRifScontrino
     *            the mRifScontrino to set
     */
    public void setRifScontrino(String mRifScontrino) {
	
	this.mRifScontrino = mRifScontrino;
    }
    
    /**
     * @return the mNote
     */
    public String getNote() {
	
	return mNote;
    }
    
    /**
     * @param mNote
     *            the mNote to set
     */
    public void setNote(String mNote) {
	
	this.mNote = mNote;
    }
    
    /**
     * @return the mFirma
     */
    public String getFirma() {
	
	return mFirma;
    }
    
    /**
     * @param mFirma
     *            the mFirma to set
     */
    public void setFirma(String mFirma) {
	
	this.mFirma = mFirma;
    }
    
    /**
     * @return the mSaldato
     */
    public boolean isSaldato() {
	
	return mSaldato;
    }
    
    /**
     * @param mSaldato
     *            the mSaldato to set
     */
    public void setSaldato(boolean mSaldato) {
	
	this.mSaldato = mSaldato;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean isCancellato() {
	
	return mCancellato;
    }
    
    /**
     * @param mCancellato
     *            the mCancellato to set
     */
    public void setCancellato(boolean mCancellato) {
	
	this.mCancellato = mCancellato;
    }
    
    /**
     * @return the mChiuso
     */
    public boolean isChiuso() {
	
	return mChiuso;
    }
    
    /**
     * @param mChiuso
     *            the mChiuso to set
     */
    public void setChiuso(boolean mChiuso) {
	
	this.mChiuso = mChiuso;
    }
    
    /**
     * @return the mDataOra
     */
    public Long getDataOra() {
	
	return mDataOra;
    }
    
    /**
     * @param mDataOra
     *            the mDataOra to set
     */
    public void setDataOra(Long mDataOra) {
	
	this.mDataOra = mDataOra;
    }
    
    /**
     * @return the mIdCliente
     */
    public Long getIdCliente() {
	
	return mIdCliente;
    }
    
    /**
     * @param mIdCliente
     *            the mIdCliente to set
     */
    public void setIdCliente(Long mIdCliente) {
	
	this.mIdCliente = mIdCliente;
    }
    
    /**
     * @return the mIdTecnico
     */
    public Long getIdTecnico() {
	
	return mIdTecnico;
    }
    
    /**
     * @param mIdTecnico
     *            the mIdTecnico to set
     */
    public void setIdTecnico(Long mIdTecnico) {
	
	this.mIdTecnico = mIdTecnico;
    }
    
    /**
     * @return the mCostoManodopera
     */
    public BigDecimal getCostoManodopera() {
	
	return mCostoManodopera;
    }
    
    /**
     * @param mCostoManodopera
     *            the mCostoManodopera to set
     */
    public void setCostoManodopera(BigDecimal mCostoManodopera) {
	
	this.mCostoManodopera = mCostoManodopera;
    }
    
    /**
     * @return the mCostoComponenti
     */
    public BigDecimal getCostoComponenti() {
	
	return mCostoComponenti;
    }
    
    /**
     * @param mCostoComponenti
     *            the mCostoComponenti to set
     */
    public void setCostoComponenti(BigDecimal mCostoComponenti) {
	
	this.mCostoComponenti = mCostoComponenti;
    }
    
    /**
     * @return the mCostoAccessori
     */
    public BigDecimal getCostoAccessori() {
	
	return mCostoAccessori;
    }
    
    /**
     * @param mCostoAccessori
     *            the mCostoAccessori to set
     */
    public void setCostoAccessori(BigDecimal mCostoAccessori) {
	
	this.mCostoAccessori = mCostoAccessori;
    }
    
    /**
     * @return the mImporto
     */
    public BigDecimal getImporto() {
	
	return mImporto;
    }
    
    /**
     * @param mImporto
     *            the mImporto to set
     */
    public void setImporto(BigDecimal mImporto) {
	
	this.mImporto = mImporto;
    }
    
    /**
     * @return the mTotale
     */
    public BigDecimal getTotale() {
	
	return mTotale;
    }
    
    /**
     * @param mTotale
     *            the mTotale to set
     */
    public void setTotale(BigDecimal mTotale) {
	
	this.mTotale = mTotale;
    }
    
    /**
     * @return the mIva
     */
    public BigDecimal getIva() {
	
	return mIva;
    }
    
    /**
     * @param mIva
     *            the mIva to set
     */
    public void setIva(BigDecimal mIva) {
	
	this.mIva = mIva;
    }
    
    /**
     * @return the mModalita
     */
    public String getModalita() {
	
	return mModalita;
    }
    
    /**
     * @param mModalita
     *            the mModalita to set
     */
    public void setModalita(String mModalita) {
	
	this.mModalita = mModalita;
    }
    
    /**
     * @return the mNumeroIntervento
     */
    public Long getNumeroIntervento() {
	return mNumeroIntervento;
    }
    
    /**
     * @param mNumeroIntervento
     *            the mNumeroIntervento to set
     */
    public void setNumeroIntervento(Long mNumeroIntervento) {
	this.mNumeroIntervento = mNumeroIntervento;
    }
    
    /**
     * @return the mModificato
     */
    public String getModificato() {
	return mModificato;
    }
    
    /**
     * @param mModificato
     *            the mModificato to set
     */
    public void setModificato(String mModificato) {
	this.mModificato = mModificato;
    }
    
    /**
     * @return the mConflitto
     */
    public boolean ismConflitto() {
	return mConflitto;
    }
    
    /**
     * @param mConflitto
     *            the mConflitto to set
     */
    public void setmConflitto(boolean mConflitto) {
	this.mConflitto = mConflitto;
    }
    
    @Override
    public String toString() {
	
	String result = "";
	
	return result;
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
}
