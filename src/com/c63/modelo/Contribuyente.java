package com.c63.modelo;

public class Contribuyente {

	private int idContribuyente;
	private String cifNif;
	private String nombreRazon;
	private String domicilio;
	private String poblacion;
	private String cp;
	private String provincia;
	private String telefono;
	private String email;

	public Contribuyente() {
		super();
	}

	public Contribuyente(int idContribuyente, String cifNif, String nombreRazon, String domicilio, String poblacion,
			String cp, String provincia, String telefono, String email) {
		super();
		this.idContribuyente = idContribuyente;
		this.cifNif = cifNif;
		this.nombreRazon = nombreRazon;
		this.domicilio = domicilio;
		this.poblacion = poblacion;
		this.cp = cp;
		this.provincia = provincia;
		this.telefono = telefono;
		this.email = email;
	}

	public int getIdContribuyente() {
		return idContribuyente;
	}

	public void setIdContribuyente(int idContribuyente) {
		this.idContribuyente = idContribuyente;
	}

	public String getCifNif() {
		return cifNif;
	}

	public void setCifNif(String cifNif) {
		this.cifNif = cifNif;
	}

	public String getNombreRazon() {
		return nombreRazon;
	}

	public void setNombreRazon(String nombreRazon) {
		this.nombreRazon = nombreRazon;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
