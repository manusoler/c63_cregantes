package com.c63.modelo.dao;

import java.util.List;

import org.apache.log4j.Logger;

import com.c63.modelo.BancosC63;

public interface BancosC63_IDao {
	
	final static Logger logger = Logger.getLogger(BancosC63_IDao.class);

	public abstract List<BancosC63> listarBancosC63();

	public abstract String insertarBancoC63(String codigoentidad, String c63banco, String c63sucursal, String c63nombre,
			String c63codine, String c63iban);

	public abstract String updateBancosC63(String codigoentidad, String c63banco, String c63sucursal, String c63nombre,
			String c63codine, String c63iban);

	public abstract String borrarBancoC63(String codigoentidad, String c63banco);

	public abstract BancosC63 leerBancoC63(String codigoentidad, String c63banco);
	
	public abstract List<BancosC63> listarBancosC63Entidad(String codigoentidad);

}