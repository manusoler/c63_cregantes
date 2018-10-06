package com.c63.modelo.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.c63.modelo.Recibos;

public interface Recibos_IDao {
	
	final static Logger logger = Logger.getLogger(Recibos_IDao.class);

	public abstract List<Recibos> listarRecibos(String codigoentidad, String codObjeto, String anyo, String periodo, String num_regante, String concepto, String nif, String nombrerazon);
	
	public abstract List<Recibos> listarRecibosEntidad(String codigoentidad);

	public abstract String insertarRecibo(Recibos recibo) throws Exception;
	
	public abstract int[] insertarRecibos(List<Recibos> recibos) throws Exception;

	public abstract String updateRecibo(Recibos recibo);

	public abstract String borrarRecibo(int idRecibo);

	public abstract Recibos leerRecibo(int idRecibo);
	
	public abstract Recibos calcularImportes(int idRecibo, Date fFinCalculoIntereses);

	public abstract List<Recibos> listarRecibosRelacionApremio(String codigoentidad);
	
	public abstract List<Recibos> listarRecibosParaNotif(String codigoentidad, Date fProvidencia);
	
	public abstract int crearLoteApremio(List<Recibos> listaRecibos, String codLote);
	
	public abstract List<Recibos> listarRecibosLote(String codLote);

	public abstract int marcarLoteApremio(String codLote, Date value);

	public abstract int marcarNotifApremio(int idRecibo, String estado, Date fNotif, String codLoteBoletin);

	public abstract List<Recibos> listarRecibosLoteBoletin(String value);

	public abstract int marcarLoteBoletin(String codLote, Date value);

	public abstract int crearLoteNotif(List<Recibos> listaRecibos, String codLote);

	public abstract List<Recibos> listarRecibosLoteNotif(String codLote);
	
	public abstract double obtenerTotalDeudaContri(String nifContri, String codigoEntidad);

}