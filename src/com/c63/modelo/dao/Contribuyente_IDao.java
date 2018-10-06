package com.c63.modelo.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.c63.modelo.Contribuyente;

public interface Contribuyente_IDao {
	
	final static Logger logger = Logger.getLogger(Contribuyente_IDao.class);

	public abstract List<Contribuyente> listarContribuyentes(String nif, String nombrerazon);
	
	public abstract int insertarContribuyente(Contribuyente contribuyente) throws Exception;

	public abstract String updateContribuyente(Contribuyente contribuyente);

	public abstract String borrarContribuyente(int idContribuyente);

	public abstract Contribuyente leerContribuyente(int idContribuyente);
	
	public abstract Contribuyente leerContribuyenteNif(String nif);

}