package com.c63.modelo;

import java.sql.Date;

public class Recibos {

	private int idRecibo;
	private String numRegante;
	private String codObjeto;
	private String domicilioObjeto;
	private String concepto;
	private int ano;
	private int periodo;
	private String textoPeriodo;
	private Date fAsamblea;
	private double principal;
	private double recargo;
	private double intereses;
	private double costas;
	private double ingresosCuenta;
	private String estado;
	private Date fEstado;
	private String fase;
	private Date fProvidencia;
	private String loteApremio;
	private String estadoNotificacion;
	private Date fNotificacion;
	private Date fDevolucion;
	private String loteBoletin;
	private String observaciones;
	private Date fEjecutiva;
	private String loteNotificacion;
	
	private Entidades entidad;
	private Contribuyente contribuyente;

	public Recibos() {
		super();
	}
	
	public Recibos(int idRecibo, String numRegante, String codObjeto, String domicilioObjeto, String concepto, int ano,
			int periodo, String textoPeriodo, Date fAsamblea, double principal, double recargo, double intereses,
			double costas, double ingresosCuenta, String estado, Date fEstado, String fase, Date fProvidencia,
			String loteApremio, String estadoNotificacion, Date fNotificacion, Date fDevolucion, String loteBoletin,
			String observaciones, Entidades entidad, Contribuyente contribuyente, Date fEjecutiva, String loteNotificacion) {
		super();
		this.idRecibo = idRecibo;
		this.numRegante = numRegante;
		this.codObjeto = codObjeto;
		this.domicilioObjeto = domicilioObjeto;
		this.concepto = concepto;
		this.ano = ano;
		this.periodo = periodo;
		this.textoPeriodo = textoPeriodo;
		this.fAsamblea = fAsamblea;
		this.principal = principal;
		this.recargo = recargo;
		this.intereses = intereses;
		this.costas = costas;
		this.ingresosCuenta = ingresosCuenta;
		this.estado = estado;
		this.fEstado = fEstado;
		this.fase = fase;
		this.fProvidencia = fProvidencia;
		this.loteApremio = loteApremio;
		this.estadoNotificacion = estadoNotificacion;
		this.fNotificacion = fNotificacion;
		this.fDevolucion = fDevolucion;
		this.loteBoletin = loteBoletin;
		this.observaciones = observaciones;
		this.entidad = entidad;
		this.contribuyente = contribuyente;
		this.fEjecutiva = fEjecutiva;
		this.loteNotificacion = loteNotificacion;
	}

	public String getLoteNotificacion() {
		return loteNotificacion;
	}

	public void setLoteNotificacion(String loteNotificacion) {
		this.loteNotificacion = loteNotificacion;
	}

	public int getIdRecibo() {
		return idRecibo;
	}

	public void setIdRecibo(int idRecibo) {
		this.idRecibo = idRecibo;
	}

	public String getNumRegante() {
		return numRegante;
	}

	public void setNumRegante(String numRegante) {
		this.numRegante = numRegante;
	}

	public String getCodObjeto() {
		return codObjeto;
	}

	public void setCodObjeto(String codObjeto) {
		this.codObjeto = codObjeto;
	}

	public String getDomicilioObjeto() {
		return domicilioObjeto;
	}

	public void setDomicilioObjeto(String domicilioObjeto) {
		this.domicilioObjeto = domicilioObjeto;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getPeriodo() {
		return periodo;
	}

	public void setPeriodo(int periodo) {
		this.periodo = periodo;
	}

	public String getTextoPeriodo() {
		return textoPeriodo;
	}

	public void setTextoPeriodo(String textoPeriodo) {
		this.textoPeriodo = textoPeriodo;
	}

	public Date getfAsamblea() {
		return fAsamblea;
	}

	public void setfAsamblea(Date fAsamblea) {
		this.fAsamblea = fAsamblea;
	}

	public double getPrincipal() {
		return principal;
	}

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getRecargo() {
		return recargo;
	}

	public void setRecargo(double recargo) {
		this.recargo = recargo;
	}

	public double getIntereses() {
		return intereses;
	}

	public void setIntereses(double intereses) {
		this.intereses = intereses;
	}

	public double getCostas() {
		return costas;
	}

	public void setCostas(double costas) {
		this.costas = costas;
	}

	public double getIngresosCuenta() {
		return ingresosCuenta;
	}

	public void setIngresosCuenta(double ingresosCuenta) {
		this.ingresosCuenta = ingresosCuenta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getfEstado() {
		return fEstado;
	}

	public void setfEstado(Date fEstado) {
		this.fEstado = fEstado;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public Date getfProvidencia() {
		return fProvidencia;
	}

	public void setfProvidencia(Date fProvidencia) {
		this.fProvidencia = fProvidencia;
	}

	public String getLoteApremio() {
		return loteApremio;
	}

	public void setLoteApremio(String loteApremio) {
		this.loteApremio = loteApremio;
	}

	public String getEstadoNotificacion() {
		return estadoNotificacion;
	}

	public void setEstadoNotificacion(String estadoNotificacion) {
		this.estadoNotificacion = estadoNotificacion;
	}

	public Date getfNotificacion() {
		return fNotificacion;
	}

	public void setfNotificacion(Date fNotificacion) {
		this.fNotificacion = fNotificacion;
	}

	public Date getfDevolucion() {
		return fDevolucion;
	}

	public void setfDevolucion(Date fDevolucion) {
		this.fDevolucion = fDevolucion;
	}

	public String getLoteBoletin() {
		return loteBoletin;
	}

	public void setLoteBoletin(String loteBoletin) {
		this.loteBoletin = loteBoletin;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Entidades getEntidad() {
		return entidad;
	}

	public void setEntidad(Entidades entidad) {
		this.entidad = entidad;
	}

	public Contribuyente getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Date getfEjecutiva() {
		return fEjecutiva;
	}

	public void setfEjecutiva(Date fEjecutiva) {
		this.fEjecutiva = fEjecutiva;
	}


}
