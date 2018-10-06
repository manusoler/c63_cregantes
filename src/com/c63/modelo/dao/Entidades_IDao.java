package com.c63.modelo.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.c63.modelo.Entidades;

public interface Entidades_IDao {
	
	final static Logger logger = Logger.getLogger(Entidades_IDao.class);

	public abstract List<Entidades> listarEntidades();

	public abstract String insertarEntidades(String codigoEntidad, String nombreEntidad, String domicilioEntidad,
			String localidadEntidad, String cpostalEntidad, String provinciaEntidad, String telefonoEntidad,
			String emailEntidad, String nifEntidad);

	public abstract String updateEntidades(String codigoEntidad, String nombreEntidad, String domicilioEntidad,
			String localidadEntidad, String cpostalEntidad, String provinciaEntidad, String telefonoEntidad,
			String emailEntidad, String nifEntidad);

	public abstract String borrarEntidades(String codigoEntidad);

	public abstract Entidades leerEntidad(String codigoEntidad);

}