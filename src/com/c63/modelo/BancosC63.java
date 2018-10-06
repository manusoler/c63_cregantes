package com.c63.modelo;

public class BancosC63 {

	private String codigoentidad;
	private String c63banco;
	private String c63sucursal;
	private String c63nombre;
	private String c63codine;
	private String c63iban;

	public String getCodigoentidad() {
		return codigoentidad;
	}

	public void setCodigoentidad(String codigoentidad) {
		this.codigoentidad = codigoentidad;
	}

	public String getC63banco() {
		return c63banco;
	}

	public void setC63banco(String c63banco) {
		this.c63banco = c63banco;
	}

	public String getC63sucursal() {
		return c63sucursal;
	}

	public void setC63sucursal(String c63sucursal) {
		this.c63sucursal = c63sucursal;
	}

	public String getC63nombre() {
		return c63nombre;
	}

	public void setC63nombre(String c63nombre) {
		this.c63nombre = c63nombre;
	}

	public String getC63codine() {
		return c63codine;
	}

	public void setC63codine(String c63codine) {
		this.c63codine = c63codine;
	}

	public String getC63iban() {
		return c63iban;
	}

	public void setC63iban(String c63iban) {
		this.c63iban = c63iban;
	}

	public BancosC63(String codigoentidad, String c63banco, String c63sucursal, String c63nombre, String c63codine,
			String c63iban) {
		super();
		this.codigoentidad = codigoentidad;
		this.c63banco = c63banco;
		this.c63sucursal = c63sucursal;
		this.c63nombre = c63nombre;
		this.c63codine = c63codine;
		this.c63iban = c63iban;
	}

	public BancosC63() {
		super();
		this.codigoentidad = null;
		this.c63banco = null;
		this.c63sucursal = null;
		this.c63nombre = null;
		this.c63codine = null;
		this.c63iban = null;
	}

}
