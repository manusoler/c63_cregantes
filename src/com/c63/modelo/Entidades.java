package com.c63.modelo;

public class Entidades {

	private String codigoentidad;
	private String nombreentidad;
	private String domicilioentidad;
	private String localidadentidad;
	private String cpostalentidad;
	private String provinciaentidad;
	private String telefonoentidad;
	private String emailentidad;
	private String nifentidad;

	public String getCodigoentidad() {
		return codigoentidad;
	}

	public void setCodigoentidad(String codigoentidad) {
		this.codigoentidad = codigoentidad;
	}

	public String getNombreentidad() {
		return nombreentidad;
	}

	public void setNombreentidad(String nombreentidad) {
		this.nombreentidad = nombreentidad;
	}

	public String getDomicilioentidad() {
		return domicilioentidad;
	}

	public void setDomicilioentidad(String domicilioentidad) {
		this.domicilioentidad = domicilioentidad;
	}

	public String getLocalidadentidad() {
		return localidadentidad;
	}

	public void setLocalidadentidad(String localidadentidad) {
		this.localidadentidad = localidadentidad;
	}

	public String getCpostalentidad() {
		return cpostalentidad;
	}

	public void setCpostalentidad(String cpostalentidad) {
		this.cpostalentidad = cpostalentidad;
	}

	public String getProvinciaentidad() {
		return provinciaentidad;
	}

	public void setProvinciaentidad(String provinciaentidad) {
		this.provinciaentidad = provinciaentidad;
	}

	public String getTelefonoentidad() {
		return telefonoentidad;
	}

	public void setTelefonoentidad(String telefonoentidad) {
		this.telefonoentidad = telefonoentidad;
	}

	public String getEmailentidad() {
		return emailentidad;
	}

	public void setEmailentidad(String emailentidad) {
		this.emailentidad = emailentidad;
	}

	public String getNifentidad() {
		return nifentidad;
	}

	public void setNifentidad(String nifentidad) {
		this.nifentidad = nifentidad;
	}

	public Entidades(String codigoentidad, String nombreentidad, String domicilioentidad, String localidadentidad,
			String cpostalentidad, String provinciaentidad, String telefonoentidad, String emailentidad,
			String nifentidad) {
		super();
		this.codigoentidad = codigoentidad;
		this.nombreentidad = nombreentidad;
		this.domicilioentidad = domicilioentidad;
		this.localidadentidad = localidadentidad;
		this.cpostalentidad = cpostalentidad;
		this.provinciaentidad = provinciaentidad;
		this.telefonoentidad = telefonoentidad;
		this.emailentidad = emailentidad;
		this.nifentidad = nifentidad;
	}

	public Entidades() {
		super();
		this.codigoentidad = null;
		this.nombreentidad = null;
		this.domicilioentidad = null;
		this.localidadentidad = null;
		this.cpostalentidad = null;
		this.provinciaentidad = null;
		this.telefonoentidad = null;
		this.emailentidad = null;
		this.nifentidad = null;
	}

}
