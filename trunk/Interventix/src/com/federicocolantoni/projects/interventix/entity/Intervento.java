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
    private Integer mNumeroIntervento;
    
    public Intervento() {
	
    }
    
    public Intervento(Long mIdIntervento) {
	
	this.mIdIntervento = mIdIntervento;
    }
    
    /**
     * @return the mIdIntervento
     */
    public Long getmIdIntervento() {
	
	return mIdIntervento;
    }
    
    /**
     * @param mIdIntervento
     *            the mIdIntervento to set
     */
    public void setmIdIntervento(Long mIdIntervento) {
	
	this.mIdIntervento = mIdIntervento;
    }
    
    /**
     * @return the mTipologia
     */
    public String getmTipologia() {
	
	return mTipologia;
    }
    
    /**
     * @param mTipologia
     *            the mTipologia to set
     */
    public void setmTipologia(String mTipologia) {
	
	this.mTipologia = mTipologia;
    }
    
    /**
     * @return the mProdotto
     */
    public String getmProdotto() {
	
	return mProdotto;
    }
    
    /**
     * @param mProdotto
     *            the mProdotto to set
     */
    public void setmProdotto(String mProdotto) {
	
	this.mProdotto = mProdotto;
    }
    
    /**
     * @return the mMotivo
     */
    public String getmMotivo() {
	
	return mMotivo;
    }
    
    /**
     * @param mMotivo
     *            the mMotivo to set
     */
    public void setmMotivo(String mMotivo) {
	
	this.mMotivo = mMotivo;
    }
    
    /**
     * @return the mNominativo
     */
    public String getmNominativo() {
	
	return mNominativo;
    }
    
    /**
     * @param mNominativo
     *            the mNominativo to set
     */
    public void setmNominativo(String mNominativo) {
	
	this.mNominativo = mNominativo;
    }
    
    /**
     * @return the mRifFattura
     */
    public String getmRifFattura() {
	
	return mRifFattura;
    }
    
    /**
     * @param mRifFattura
     *            the mRifFattura to set
     */
    public void setmRifFattura(String mRifFattura) {
	
	this.mRifFattura = mRifFattura;
    }
    
    /**
     * @return the mRifScontrino
     */
    public String getmRifScontrino() {
	
	return mRifScontrino;
    }
    
    /**
     * @param mRifScontrino
     *            the mRifScontrino to set
     */
    public void setmRifScontrino(String mRifScontrino) {
	
	this.mRifScontrino = mRifScontrino;
    }
    
    /**
     * @return the mNote
     */
    public String getmNote() {
	
	return mNote;
    }
    
    /**
     * @param mNote
     *            the mNote to set
     */
    public void setmNote(String mNote) {
	
	this.mNote = mNote;
    }
    
    /**
     * @return the mFirma
     */
    public String getmFirma() {
	
	return mFirma;
    }
    
    /**
     * @param mFirma
     *            the mFirma to set
     */
    public void setmFirma(String mFirma) {
	
	this.mFirma = mFirma;
    }
    
    /**
     * @return the mSaldato
     */
    public boolean ismSaldato() {
	
	return mSaldato;
    }
    
    /**
     * @param mSaldato
     *            the mSaldato to set
     */
    public void setmSaldato(boolean mSaldato) {
	
	this.mSaldato = mSaldato;
    }
    
    /**
     * @return the mCancellato
     */
    public boolean ismCancellato() {
	
	return mCancellato;
    }
    
    /**
     * @param mCancellato
     *            the mCancellato to set
     */
    public void setmCancellato(boolean mCancellato) {
	
	this.mCancellato = mCancellato;
    }
    
    /**
     * @return the mChiuso
     */
    public boolean ismChiuso() {
	
	return mChiuso;
    }
    
    /**
     * @param mChiuso
     *            the mChiuso to set
     */
    public void setmChiuso(boolean mChiuso) {
	
	this.mChiuso = mChiuso;
    }
    
    /**
     * @return the mDataOra
     */
    public Long getmDataOra() {
	
	return mDataOra;
    }
    
    /**
     * @param mDataOra
     *            the mDataOra to set
     */
    public void setmDataOra(Long mDataOra) {
	
	this.mDataOra = mDataOra;
    }
    
    /**
     * @return the mIdCliente
     */
    public Long getmIdCliente() {
	
	return mIdCliente;
    }
    
    /**
     * @param mIdCliente
     *            the mIdCliente to set
     */
    public void setmIdCliente(Long mIdCliente) {
	
	this.mIdCliente = mIdCliente;
    }
    
    /**
     * @return the mIdTecnico
     */
    public Long getmIdTecnico() {
	
	return mIdTecnico;
    }
    
    /**
     * @param mIdTecnico
     *            the mIdTecnico to set
     */
    public void setmIdTecnico(Long mIdTecnico) {
	
	this.mIdTecnico = mIdTecnico;
    }
    
    /**
     * @return the mCostoManodopera
     */
    public BigDecimal getmCostoManodopera() {
	
	return mCostoManodopera;
    }
    
    /**
     * @param mCostoManodopera
     *            the mCostoManodopera to set
     */
    public void setmCostoManodopera(BigDecimal mCostoManodopera) {
	
	this.mCostoManodopera = mCostoManodopera;
    }
    
    /**
     * @return the mCostoComponenti
     */
    public BigDecimal getmCostoComponenti() {
	
	return mCostoComponenti;
    }
    
    /**
     * @param mCostoComponenti
     *            the mCostoComponenti to set
     */
    public void setmCostoComponenti(BigDecimal mCostoComponenti) {
	
	this.mCostoComponenti = mCostoComponenti;
    }
    
    /**
     * @return the mCostoAccessori
     */
    public BigDecimal getmCostoAccessori() {
	
	return mCostoAccessori;
    }
    
    /**
     * @param mCostoAccessori
     *            the mCostoAccessori to set
     */
    public void setmCostoAccessori(BigDecimal mCostoAccessori) {
	
	this.mCostoAccessori = mCostoAccessori;
    }
    
    /**
     * @return the mImporto
     */
    public BigDecimal getmImporto() {
	
	return mImporto;
    }
    
    /**
     * @param mImporto
     *            the mImporto to set
     */
    public void setmImporto(BigDecimal mImporto) {
	
	this.mImporto = mImporto;
    }
    
    /**
     * @return the mTotale
     */
    public BigDecimal getmTotale() {
	
	return mTotale;
    }
    
    /**
     * @param mTotale
     *            the mTotale to set
     */
    public void setmTotale(BigDecimal mTotale) {
	
	this.mTotale = mTotale;
    }
    
    /**
     * @return the mIva
     */
    public BigDecimal getmIva() {
	
	return mIva;
    }
    
    /**
     * @param mIva
     *            the mIva to set
     */
    public void setmIva(BigDecimal mIva) {
	
	this.mIva = mIva;
    }
    
    /**
     * @return the mModalita
     */
    public String getmModalita() {
	
	return mModalita;
    }
    
    /**
     * @param mModalita
     *            the mModalita to set
     */
    public void setmModalita(String mModalita) {
	
	this.mModalita = mModalita;
    }
    
    /**
     * @return the mNumeroIntervento
     */
    public Integer getmNumeroIntervento() {
	return mNumeroIntervento;
    }
    
    /**
     * @param mNumeroIntervento
     *            the mNumeroIntervento to set
     */
    public void setmNumeroIntervento(Integer mNumeroIntervento) {
	this.mNumeroIntervento = mNumeroIntervento;
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
    
    public static ContentValues insertSQL(Long id, Double costoAccessori, Double costoComponenti, Double costoManodopera,
	    Long dataOra, String firma, Long cliente, Long tecnico, Double importo, Double iva, String modalita, String motivo, String nominativo,
	    String note, Long numeroIntervento, String prodotto, String riferimentoFattura, String riferimentoScontrino,
	    String tipologia, Double totale, Boolean saldato, Boolean chiuso, Boolean cancellato, String modificato) {
	
	ContentValues values = new ContentValues();
	
	values.put(InterventoDB.Fields.ID_INTERVENTO, id);
	values.put(Fields.TYPE, InterventoDB.INTERVENTO_ITEM_TYPE);
	values.put(InterventoDB.Fields.CANCELLATO, cancellato);
	values.put(InterventoDB.Fields.COSTO_ACCESSORI, costoAccessori);
	values.put(InterventoDB.Fields.COSTO_COMPONENTI, costoComponenti);
	values.put(InterventoDB.Fields.COSTO_MANODOPERA, costoManodopera);
	values.put(InterventoDB.Fields.DATA_ORA, dataOra);
	values.put(InterventoDB.Fields.FIRMA, firma);
	values.put(InterventoDB.Fields.CLIENTE, cliente);
	values.put(InterventoDB.Fields.IMPORTO, importo);
	values.put(InterventoDB.Fields.IVA, iva);
	values.put(InterventoDB.Fields.MODALITA, modalita);
	values.put(InterventoDB.Fields.MODIFICATO, modificato);
	values.put(InterventoDB.Fields.MOTIVO, motivo);
	values.put(InterventoDB.Fields.NOMINATIVO, nominativo);
	values.put(InterventoDB.Fields.NOTE, note);
	values.put(InterventoDB.Fields.NUMERO_INTERVENTO, numeroIntervento);
	values.put(InterventoDB.Fields.PRODOTTO, prodotto);
	values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, riferimentoFattura);
	values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, riferimentoScontrino);
	values.put(InterventoDB.Fields.SALDATO, saldato);
	values.put(InterventoDB.Fields.TIPOLOGIA, tipologia);
	values.put(InterventoDB.Fields.TOTALE, totale);
	values.put(InterventoDB.Fields.CHIUSO, chiuso);
	values.put(InterventoDB.Fields.TECNICO, tecnico);
	
	return values;
    }
    
    public static ContentValues updateSQL(Double costoAccessori, Double costoComponenti, Double costoManodopera,
	    Long dataOra, String firma, Long cliente, Long tecnico, Double importo, Double iva, String modalita, String motivo, String nominativo,
	    String note, Long numeroIntervento, String prodotto, String riferimentoFattura, String riferimentoScontrino,
	    String tipologia, Double totale, Boolean saldato, Boolean chiuso, Boolean cancellato, String modificato) {
	
	ContentValues values = new ContentValues();
	
	values.put(InterventoDB.Fields.CANCELLATO, cancellato);
	values.put(InterventoDB.Fields.COSTO_ACCESSORI, costoAccessori);
	values.put(InterventoDB.Fields.COSTO_COMPONENTI, costoComponenti);
	values.put(InterventoDB.Fields.COSTO_MANODOPERA, costoManodopera);
	values.put(InterventoDB.Fields.DATA_ORA, dataOra);
	values.put(InterventoDB.Fields.FIRMA, firma);
	values.put(InterventoDB.Fields.CLIENTE, cliente);
	values.put(InterventoDB.Fields.IMPORTO, importo);
	values.put(InterventoDB.Fields.IVA, iva);
	values.put(InterventoDB.Fields.MODALITA, modalita);
	values.put(InterventoDB.Fields.MODIFICATO, modificato);
	values.put(InterventoDB.Fields.MOTIVO, motivo);
	values.put(InterventoDB.Fields.NOMINATIVO, nominativo);
	values.put(InterventoDB.Fields.NOTE, note);
	values.put(InterventoDB.Fields.NUMERO_INTERVENTO, numeroIntervento);
	values.put(InterventoDB.Fields.PRODOTTO, prodotto);
	values.put(InterventoDB.Fields.RIFERIMENTO_FATTURA, riferimentoFattura);
	values.put(InterventoDB.Fields.RIFERIMENTO_SCONTRINO, riferimentoScontrino);
	values.put(InterventoDB.Fields.SALDATO, saldato);
	values.put(InterventoDB.Fields.TIPOLOGIA, tipologia);
	values.put(InterventoDB.Fields.TOTALE, totale);
	values.put(InterventoDB.Fields.CHIUSO, chiuso);
	values.put(InterventoDB.Fields.TECNICO, tecnico);
	
	return values;
    }
}
