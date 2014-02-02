package com.federicocolantoni.projects.interventix.utils;

import java.io.Serializable;
import java.util.List;

import com.federicocolantoni.projects.interventix.entity.DettaglioIntervento;

public final class ListDetailsIntervento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314993257908959389L;

	private List<DettaglioIntervento> listDetails;

	/**
	 * @return the listDetails
	 */
	public List<DettaglioIntervento> getListDetails() {

		return listDetails;
	}

	/**
	 * @param listDetails
	 *            the listDetails to set
	 */
	public void setListDetails(List<DettaglioIntervento> listDetails) {

		this.listDetails = listDetails;
	}
}
