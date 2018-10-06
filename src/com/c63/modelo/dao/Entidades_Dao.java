package com.c63.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import com.c63.comun.ConexionBD;
import com.c63.modelo.Entidades;

public class Entidades_Dao implements Entidades_IDao {
	
	@Override
	public List<Entidades> listarEntidades() {
		List<Entidades> movimientos = new ArrayList<Entidades>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select codigoentidad, nombreentidad, domicilioentidad, localidadentidad, cpostalentidad, "
					+ "provinciaentidad, telefonoentidad, emailentidad, nifentidad from entidades "
					+ "order by nombreentidad ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Entidades movi = new Entidades();
				movi.setCodigoentidad(rs.getString("codigoentidad"));
				movi.setNombreentidad(rs.getString("nombreentidad"));
				movi.setDomicilioentidad(rs.getString("domicilioentidad"));
				movi.setLocalidadentidad(rs.getString("localidadentidad"));
				movi.setCpostalentidad(rs.getString("cpostalentidad"));
				movi.setProvinciaentidad(rs.getString("provinciaentidad"));
				movi.setTelefonoentidad(rs.getString("telefonoentidad"));
				movi.setEmailentidad(rs.getString("emailentidad"));
				movi.setNifentidad(rs.getString("nifentidad"));
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarEntidades ENTIDADES_DAO: " + e.getMessage());
			Messagebox.show("Error en listarEntidades ENTIDADES_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}

	@Override
	public String insertarEntidades(String codigoEntidad, String nombreEntidad, String domicilioEntidad,
			String localidadEntidad, String cpostalEntidad, String provinciaEntidad, String telefonoEntidad,
			String emailEntidad, String nifEntidad) {

		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);

			String sql = "select count(*) as numentidades from entidades where codigoEntidad = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoEntidad);
			ResultSet rs = stmt.executeQuery();
			int numEntidades = 0;
			while (rs.next()) {
				numEntidades = rs.getInt("numentidades");
			}
			stmt.close();
			if (numEntidades == 0) {
				sql = "insert into entidades (codigoentidad, nombreentidad, domicilioentidad, localidadentidad, cpostalentidad, "
						+ "provinciaentidad, telefonoentidad, emailentidad, nifentidad ) values (?, ?, ?, ?, ?, ?, ?, ?, ? )";
				PreparedStatement stmt1 = conn.prepareStatement(sql);
				stmt1.setString(1, codigoEntidad);
				stmt1.setString(2, nombreEntidad);
				stmt1.setString(3, domicilioEntidad);
				stmt1.setString(4, localidadEntidad);
				stmt1.setString(5, cpostalEntidad);
				stmt1.setString(6, provinciaEntidad);
				stmt1.setString(7, telefonoEntidad);
				stmt1.setString(8, emailEntidad);
				stmt1.setString(9, nifEntidad);
				int rowCount = stmt1.executeUpdate();
				conn.commit();
				committed = true;
				stmt1.close();
				conn.close();
				return "Entidad " + codigoEntidad + " Creada Correctamente.";
			} else {
				return "ERROR ==> Entidad " + codigoEntidad + " ya Existe.";
			}
		} catch (Exception e) {
			logger.error("Error en insertarEntidades ENTIDADES_DAO: " + e.getMessage());
			return "Error en insertarEntidades ENTIDADES_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public String updateEntidades(String codigoEntidad, String nombreEntidad, String domicilioEntidad,
			String localidadEntidad, String cpostalEntidad, String provinciaEntidad, String telefonoEntidad,
			String emailEntidad, String nifEntidad) {
		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update entidades set nombreentidad = ?, domicilioentidad = ?, localidadentidad = ?, cpostalentidad = ?, "
					+ "provinciaentidad = ?, telefonoentidad = ?, emailentidad = ?, nifentidad = ? "
					+ "where codigoentidad = ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			stmt1.setString(1, nombreEntidad);
			stmt1.setString(2, domicilioEntidad);
			stmt1.setString(3, localidadEntidad);
			stmt1.setString(4, cpostalEntidad);
			stmt1.setString(5, provinciaEntidad);
			stmt1.setString(6, telefonoEntidad);
			stmt1.setString(7, emailEntidad);
			stmt1.setString(8, nifEntidad);
			stmt1.setString(9, codigoEntidad);
			int rowCount = stmt1.executeUpdate();
			conn.commit();
			committed = true;
			stmt1.close();
			conn.close();
			return "Entidad " + codigoEntidad + " Modificada Correctamente.";

		} catch (Exception e) {
			logger.error("Error en updateEntidades  ENTIDADES_DAO: "+e.getMessage());
			return "Error en updateEntidades  ENTIDADES_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public String borrarEntidades(String codigoEntidad) {
		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "delete from entidades where codigoentidad = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoEntidad);
			int rowCount = stmt.executeUpdate();
			conn.commit();
			committed = true;
			stmt.close();
			conn.close();
			return "Entidad " + codigoEntidad + " Borrada Correctamente.";
		} catch (Exception e) {
			logger.error("Error en borrarEntidad ENTIDADES_DAO: " + e.getMessage());
			return "Error en borrarEntidades  ENTIDADES_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public Entidades leerEntidad(String codigoEntidad) {
		Entidades movi = new Entidades();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select codigoentidad, nombreentidad, domicilioentidad, localidadentidad, "
					+ "cpostalentidad, provinciaentidad, telefonoentidad, emailentidad, nifentidad "
					+ "from entidades where codigoentidad = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoEntidad);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				movi.setCodigoentidad(rs.getString("codigoentidad"));
				movi.setNombreentidad(rs.getString("nombreentidad"));
				movi.setDomicilioentidad(rs.getString("domicilioentidad"));
				movi.setLocalidadentidad(rs.getString("localidadentidad"));
				movi.setCpostalentidad(rs.getString("cpostalentidad"));
				movi.setProvinciaentidad(rs.getString("provinciaentidad"));
				movi.setTelefonoentidad(rs.getString("telefonoentidad"));
				movi.setEmailentidad(rs.getString("emailentidad"));
				movi.setNifentidad(rs.getString("nifentidad"));
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en leerEntidad ENTIDADES_DAO " + codigoEntidad + ": " + e.getMessage());
			Messagebox.show("Error en leerEntidad ENTIDADES_DAO " + codigoEntidad + e.getMessage() + " - " + e.toString());
		}
		return movi;
	}
}
