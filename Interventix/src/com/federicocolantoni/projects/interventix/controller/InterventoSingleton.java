package com.federicocolantoni.projects.interventix.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.federicocolantoni.projects.interventix.entity.Cliente;
import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;
import com.federicocolantoni.projects.interventix.entity.Intervento;
import com.federicocolantoni.projects.interventix.entity.Utente;

public class InterventoSingleton implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1441259854727900393L;

    public static InterventoSingleton sInstance;

    private Intervento intervento;
    private Utente tecnico;
    private Cliente cliente;
    private List<DettaglioIntervento> listaDettagli;

    private InterventoSingleton() {

	intervento = new Intervento();
	tecnico = new Utente();
	cliente = new Cliente();
	listaDettagli = new ArrayList<DettaglioIntervento>();
    }

    // private static class InterventoSingletonHolder {
    //
    // public static InterventoSingleton sInstance = new InterventoSingleton();
    // }

    public static InterventoSingleton getInstance() {

	return InterventoSingleton.sInstance = new InterventoSingleton();
    }

    protected Object readResolve() {

	return getInstance();
    }

    public static void reset() {

	InterventoSingleton.sInstance = null;
    }

    /**
     * @return the intervento
     */
    public Intervento getIntervento() {

	return intervento;
    }

    /**
     * @param intervento
     *            the intervento to set
     */
    public void setIntervento(Intervento intervento) {

	this.intervento = intervento;
    }

    /**
     * @return the tecnico
     */
    public Utente getTecnico() {

	return tecnico;
    }

    /**
     * @param tecnico
     *            the tecnico to set
     */
    public void setTecnico(Utente tecnico) {

	this.tecnico = tecnico;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {

	return cliente;
    }

    /**
     * @param cliente
     *            the cliente to set
     */
    public void setCliente(Cliente cliente) {

	this.cliente = cliente;
    }

    /**
     * @return the listaDettagli
     */
    public List<DettaglioIntervento> getListaDettagli() {

	return listaDettagli;
    }

    /**
     * @param listaDettagli
     *            the listaDettagli to set
     */
    public void setListaDettagli(List<DettaglioIntervento> listaDettagli) {

	this.listaDettagli = listaDettagli;
    }

    @Override
    public String toString() {

	return String.format("InterventoSingleton [intervento=%s, tecnico=%s, cliente=%s, listaDettagli=%s]", intervento, tecnico, cliente, listaDettagli);
    }

    @Override
    public int hashCode() {

	final int prime = 31;
	int result = 1;
	result = prime * result + (cliente == null ? 0 : cliente.hashCode());
	result = prime * result + (intervento == null ? 0 : intervento.hashCode());
	result = prime * result + (listaDettagli == null ? 0 : listaDettagli.hashCode());
	result = prime * result + (tecnico == null ? 0 : tecnico.hashCode());
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
	if (!(obj instanceof InterventoSingleton)) {
	    return false;
	}
	InterventoSingleton other = (InterventoSingleton) obj;
	if (cliente == null) {
	    if (other.cliente != null) {
		return false;
	    }
	}
	else if (!cliente.equals(other.cliente)) {
	    return false;
	}
	if (intervento == null) {
	    if (other.intervento != null) {
		return false;
	    }
	}
	else if (!intervento.equals(other.intervento)) {
	    return false;
	}
	if (listaDettagli == null) {
	    if (other.listaDettagli != null) {
		return false;
	    }
	}
	else if (!listaDettagli.equals(other.listaDettagli)) {
	    return false;
	}
	if (tecnico == null) {
	    if (other.tecnico != null) {
		return false;
	    }
	}
	else if (!tecnico.equals(other.tecnico)) {
	    return false;
	}
	return true;
    }
}
