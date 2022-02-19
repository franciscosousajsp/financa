package com.example.financia.exception;

public class RegraNegocioExcepion extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegraNegocioExcepion(String msn) {
		super(msn);
	}
}
