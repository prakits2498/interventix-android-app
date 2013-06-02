
package com.federicocolantoni.projects.interventix.intervento;

import java.math.BigDecimal;
import java.util.List;

public class Intervento {

    private Long mIdIntervento;
    private String mTipologia, mProdotto, mMotivo, mNominativo, mRifFattura,
	    mRifScontrino, mNote;
    private byte[] mFirma;
    private boolean mSaldato, mCancellato;
    private Long mDataOra, mInizio, mFine;
    private Long mIdCliente;
    private Long mIdTecnico;
    private BigDecimal mCostoManodopera, mCostoComponenti, mCostoAccessori,
	    mImporto, mTotale;
    private List<DettaglioIntervento> mDettaglioIntervento;
    private BigDecimal mIva, mOre;

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
    public byte[] getmFirma() {

	return mFirma;
    }

    /**
     * @param mFirma
     *            the mFirma to set
     */
    public void setmFirma(byte[] mFirma) {

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
     * @return the mInizio
     */
    public Long getmInizio() {

	return mInizio;
    }

    /**
     * @param mInizio
     *            the mInizio to set
     */
    public void setmInizio(Long mInizio) {

	this.mInizio = mInizio;
    }

    /**
     * @return the mFine
     */
    public Long getmFine() {

	return mFine;
    }

    /**
     * @param mFine
     *            the mFine to set
     */
    public void setmFine(Long mFine) {

	this.mFine = mFine;
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
     * @return the mDettaglioIntervento
     */
    public List<DettaglioIntervento> getmDettaglioIntervento() {

	return mDettaglioIntervento;
    }

    /**
     * @param mDettaglioIntervento
     *            the mDettaglioIntervento to set
     */
    public void setmDettaglioIntervento(
	    List<DettaglioIntervento> mDettaglioIntervento) {

	this.mDettaglioIntervento = mDettaglioIntervento;
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
     * @return the mOre
     */
    public BigDecimal getmOre() {

	return mOre;
    }

    /**
     * @param mOre
     *            the mOre to set
     */
    public void setmOre(BigDecimal mOre) {

	this.mOre = mOre;
    }
}
