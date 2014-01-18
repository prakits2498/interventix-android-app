package com.federicocolantoni.projects.interventix.entity;

import java.io.Serializable;

import android.content.ContentValues;
import android.database.Cursor;

import com.federicocolantoni.projects.interventix.data.InterventixDBContract.ClienteDB;
import com.federicocolantoni.projects.interventix.data.InterventixDBContract.Data.Fields;

public class Cliente implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 6163117197009443000L;

	private Long idcliente;
	private String nominativo, codicefiscale, partitaiva, telefono, fax, email, referente, citta, indirizzo, interno, ufficio, note;
	private boolean cancellato, conflitto;
	private Long revisione;

	public Cliente() {

	}

	public Cliente(Long idCliente) {

		this.idcliente = idCliente;
	}

	/**
	 * @return the mIdCliente
	 */
	public Long getIdCliente() {

		return idcliente;
	}

	/**
	 * @param mIdCliente
	 *            the mIdCliente to set
	 */
	public void setIdCliente(Long mIdCliente) {

		this.idcliente = mIdCliente;
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
	 * @return the mCodiceFiscale
	 */
	public String getCodiceFiscale() {

		return codicefiscale;
	}

	/**
	 * @param mCodiceFiscale
	 *            the mCodiceFiscale to set
	 */
	public void setCodiceFiscale(String mCodiceFiscale) {

		this.codicefiscale = mCodiceFiscale;
	}

	/**
	 * @return the mPartitaIVA
	 */
	public String getPartitaIVA() {

		return partitaiva;
	}

	/**
	 * @param mPartitaIVA
	 *            the mPartitaIVA to set
	 */
	public void setPartitaIVA(String mPartitaIVA) {

		this.partitaiva = mPartitaIVA;
	}

	/**
	 * @return the mTelefono
	 */
	public String getTelefono() {

		return telefono;
	}

	/**
	 * @param mTelefono
	 *            the mTelefono to set
	 */
	public void setTelefono(String mTelefono) {

		this.telefono = mTelefono;
	}

	/**
	 * @return the mFax
	 */
	public String getFax() {

		return fax;
	}

	/**
	 * @param mFax
	 *            the mFax to set
	 */
	public void setFax(String mFax) {

		this.fax = mFax;
	}

	/**
	 * @return the mEmail
	 */
	public String getEmail() {

		return email;
	}

	/**
	 * @param mEmail
	 *            the mEmail to set
	 */
	public void setEmail(String mEmail) {

		this.email = mEmail;
	}

	/**
	 * @return the mReferente
	 */
	public String getReferente() {

		return referente;
	}

	/**
	 * @param mReferente
	 *            the mReferente to set
	 */
	public void setReferente(String mReferente) {

		this.referente = mReferente;
	}

	/**
	 * @return the mCitta
	 */
	public String getCitta() {

		return citta;
	}

	/**
	 * @param mCitta
	 *            the mCitta to set
	 */
	public void setCitta(String mCitta) {

		this.citta = mCitta;
	}

	/**
	 * @return the mIndirizzo
	 */
	public String getIndirizzo() {

		return indirizzo;
	}

	/**
	 * @param mIndirizzo
	 *            the mIndirizzo to set
	 */
	public void setIndirizzo(String mIndirizzo) {

		this.indirizzo = mIndirizzo;
	}

	/**
	 * @return the mInterno
	 */
	public String getInterno() {

		return interno;
	}

	/**
	 * @param mInterno
	 *            the mInterno to set
	 */
	public void setInterno(String mInterno) {

		this.interno = mInterno;
	}

	/**
	 * @return the mUfficio
	 */
	public String getUfficio() {

		return ufficio;
	}

	/**
	 * @param mUfficio
	 *            the mUfficio to set
	 */
	public void setUfficio(String mUfficio) {

		this.ufficio = mUfficio;
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
	 * @return the mCancellato
	 */
	public boolean ismCancellato() {

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
	 * @return the revisione
	 */
	public Long getRevisione() {

		return revisione;
	}

	/**
	 * @param revisione
	 *            the revisione to set
	 */
	public void setRevisione(Long revisione) {

		this.revisione = revisione;
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

		return String
				.format("Cliente [idcliente=%s, nominativo=%s, codicefiscale=%s, partitaiva=%s, telefono=%s, fax=%s, email=%s, referente=%s, citta=%s, indirizzo=%s, interno=%s, ufficio=%s, note=%s, cancellato=%s, conflitto=%s, revisione=%s]",
						idcliente, nominativo, codicefiscale, partitaiva, telefono, fax, email, referente, citta, indirizzo, interno, ufficio, note, cancellato, conflitto,
						revisione);
	}

	public static ContentValues insertSQL(Cliente cliente) {

		ContentValues values = new ContentValues();

		values.put(ClienteDB.Fields.ID_CLIENTE, cliente.getIdCliente());
		values.put(Fields.TYPE, ClienteDB.CLIENTE_ITEM_TYPE);
		values.put(ClienteDB.Fields.CITTA, cliente.getCitta());
		values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getCodiceFiscale());
		values.put(ClienteDB.Fields.CONFLITTO, cliente.isConflitto());
		values.put(ClienteDB.Fields.EMAIL, cliente.getEmail());
		values.put(ClienteDB.Fields.FAX, cliente.getFax());
		values.put(ClienteDB.Fields.INDIRIZZO, cliente.getIndirizzo());
		values.put(ClienteDB.Fields.INTERNO, cliente.getInterno());
		values.put(ClienteDB.Fields.NOMINATIVO, cliente.getNominativo());
		values.put(ClienteDB.Fields.NOTE, cliente.getNote());
		values.put(ClienteDB.Fields.PARTITAIVA, cliente.getPartitaIVA());
		values.put(ClienteDB.Fields.REFERENTE, cliente.getReferente());
		values.put(ClienteDB.Fields.REVISIONE, cliente.getRevisione());
		values.put(ClienteDB.Fields.TELEFONO, cliente.getTelefono());
		values.put(ClienteDB.Fields.UFFICIO, cliente.getUfficio());

		return values;
	}

	public static ContentValues updateSQL(Cliente cliente) {

		ContentValues values = new ContentValues();

		values.put(ClienteDB.Fields.CITTA, cliente.getCitta());
		values.put(ClienteDB.Fields.CODICE_FISCALE, cliente.getCodiceFiscale());
		values.put(ClienteDB.Fields.CONFLITTO, cliente.isConflitto());
		values.put(ClienteDB.Fields.EMAIL, cliente.getEmail());
		values.put(ClienteDB.Fields.FAX, cliente.getFax());
		values.put(ClienteDB.Fields.INDIRIZZO, cliente.getIndirizzo());
		values.put(ClienteDB.Fields.INTERNO, cliente.getInterno());
		values.put(ClienteDB.Fields.NOMINATIVO, cliente.getNominativo());
		values.put(ClienteDB.Fields.NOTE, cliente.getNote());
		values.put(ClienteDB.Fields.PARTITAIVA, cliente.getPartitaIVA());
		values.put(ClienteDB.Fields.REFERENTE, cliente.getReferente());
		values.put(ClienteDB.Fields.REVISIONE, cliente.getRevisione());
		values.put(ClienteDB.Fields.TELEFONO, cliente.getTelefono());
		values.put(ClienteDB.Fields.UFFICIO, cliente.getUfficio());

		return values;
	}

	public static Cliente getFromCursor(Cursor cursor) {

		Cliente cliente = new Cliente();

		cliente.setCancellato(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CANCELLATO)) == 1 ? true : false);
		cliente.setCitta(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CITTA)));
		cliente.setCodiceFiscale(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.CODICE_FISCALE)));
		cliente.setConflitto(cursor.getInt(cursor.getColumnIndex(ClienteDB.Fields.CONFLITTO)) == 1 ? true : false);
		cliente.setEmail(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.EMAIL)));
		cliente.setFax(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.FAX)));
		cliente.setIdCliente(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.ID_CLIENTE)));
		cliente.setIndirizzo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INDIRIZZO)));
		cliente.setInterno(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.INTERNO)));
		cliente.setNominativo(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOMINATIVO)));
		cliente.setNote(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.NOTE)));
		cliente.setPartitaIVA(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.PARTITAIVA)));
		cliente.setReferente(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.REFERENTE)));
		cliente.setRevisione(cursor.getLong(cursor.getColumnIndex(ClienteDB.Fields.REVISIONE)));
		cliente.setTelefono(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.TELEFONO)));
		cliente.setUfficio(cursor.getString(cursor.getColumnIndex(ClienteDB.Fields.UFFICIO)));

		return cliente;
	}
}
