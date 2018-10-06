package com.c63.modelo;

import java.sql.Date;

public class Intereses {

	private Date fecha_desde;
	private Date fecha_hasta;
	private String denominacion;
	private Double porcentaje;

	public Date getFecha_desde() {
		return fecha_desde;
	}

	public void setFecha_desde(Date fecha_desde) {
		this.fecha_desde = fecha_desde;
	}

	public Date getFecha_hasta() {
		return fecha_hasta;
	}

	public void setFecha_hasta(Date fecha_hasta) {
		this.fecha_hasta = fecha_hasta;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}

	public Intereses(Date fecha_desde, Date fecha_hasta, String denominacion, Double porcentaje) {
		super();
		this.fecha_desde = fecha_desde;
		this.fecha_hasta = fecha_hasta;
		this.denominacion = denominacion;
		this.porcentaje = porcentaje;
	}

	public Intereses() {
		super();
		this.fecha_desde = null;
		this.fecha_hasta = null;
		this.denominacion = null;
		this.porcentaje = 0.0;
	}

}
