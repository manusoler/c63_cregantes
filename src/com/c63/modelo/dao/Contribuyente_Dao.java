package com.c63.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import com.c63.comun.ConexionBD;
import com.c63.modelo.Contribuyente;

public class Contribuyente_Dao implements Contribuyente_IDao {

	@Override
	public List<Contribuyente> listarContribuyentes(String nif, String nombreRazon) {
		List<Contribuyente> contribuyentes = new ArrayList<Contribuyente>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select * from contribuyentes where 1 = 1 ";
			if(!nif.isEmpty()) {
				sql = sql.concat("and cif_nif LIKE ? ");
			}
			if(!nombreRazon.isEmpty()) {
				sql = sql.concat("and nombre_razon LIKE ? ");
			}
			sql = sql.concat("order by nombre_razon asc");
			PreparedStatement stmt = conn.prepareStatement(sql);
			int pos = 1;
			if(!nif.isEmpty()) {
				stmt.setString(pos++, "%"+nif+"%");
			}
			if(!nombreRazon.isEmpty()) {
				stmt.setString(pos++, "%"+nombreRazon+"%");
			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Contribuyente movi = new Contribuyente();
				movi.setIdContribuyente(rs.getInt("id_contribuyente"));
				movi.setCifNif(rs.getString("cif_nif"));
				movi.setNombreRazon(rs.getString("nombre_razon"));
				movi.setDomicilio(rs.getString("domicilio"));
				movi.setPoblacion(rs.getString("poblacion"));
				movi.setCp(rs.getString("cp"));
				movi.setProvincia(rs.getString("provincia"));
				movi.setTelefono(rs.getString("telefono"));
				movi.setEmail(rs.getString("email"));
				contribuyentes.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarContribuyentes CONTRIBUYENTE_DAO: " + e.getMessage());
			Messagebox.show("Error en listarContribuyentes CONTRIBUYENTE_DAO " + e.getMessage() + " - " + e.toString());
		}
		return contribuyentes;
	}

	@Override
	public int insertarContribuyente(Contribuyente contribuyente) throws Exception{
		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "insert into contribuyentes values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt1 = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			//stmt1.setInt(1, recibo.getIdContribuyente());
			stmt1.setNull(1, Types.INTEGER);
			stmt1.setString(2, contribuyente.getCifNif());
			stmt1.setString(3, contribuyente.getNombreRazon());
			stmt1.setString(4, contribuyente.getDomicilio());
			stmt1.setString(5, contribuyente.getPoblacion());
			stmt1.setString(6, contribuyente.getCp());
			stmt1.setString(7, contribuyente.getProvincia());
			stmt1.setString(8, contribuyente.getTelefono());
			stmt1.setString(9, contribuyente.getEmail());

			int rowCount = stmt1.executeUpdate();
			conn.commit();
			
			// Obtenemos el id generado
			int id = -1;
			ResultSet rs = stmt1.getGeneratedKeys();
	        if (rs.next()){
	            id=rs.getInt(1);
	        }

	        committed = true;
			stmt1.close();
			conn.close();
			return id;
		} catch (Exception e) {
			logger.error("Error en insertarContribuyente CONTRIBUYENTE_DAO: " + e.getMessage());
			throw new Exception("Error en insertarContribuyente CONTRIBUYENTE_DAO " + e.getMessage());
		}
	}

	@Override
	public String updateContribuyente(Contribuyente contribuyente) {
		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update contribuyentes set cif_nif = ?, nombre_razon = ?, domicilio = ?, poblacion = ?, cp = ?, provincia = ?, telefono = ?,"
					+ " email = ? where id_contribuyente = ?";
			
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			
			stmt1.setString(1, contribuyente.getCifNif());
			stmt1.setString(2, contribuyente.getNombreRazon());
			stmt1.setString(3, contribuyente.getDomicilio());
			stmt1.setString(4, contribuyente.getPoblacion());
			stmt1.setString(5, contribuyente.getCp());
			stmt1.setString(6, contribuyente.getProvincia());
			stmt1.setString(7, contribuyente.getTelefono());
			stmt1.setString(8, contribuyente.getEmail());
			stmt1.setInt(9, contribuyente.getIdContribuyente());
			
			int rowCount = stmt1.executeUpdate();
			conn.commit();
			committed = true;
			stmt1.close();
			conn.close();
			return "Contribuyente " + contribuyente.getIdContribuyente() + " Modificado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en updateContribuyentes  CONTRIBUYENTE_DAO: " + e.getMessage());
			return "Error en updateContribuyentes  CONTRIBUYENTE_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public String borrarContribuyente(int idContribuyente) {
		boolean committed = false;
		try {
			String sql = "delete from contribuyentes where id_contribuyente = ?";
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idContribuyente);
			int rowCount = stmt.executeUpdate();
			conn.commit();
			committed = true;
			stmt.close();
			conn.close();
			return "Contribuyente " + idContribuyente + " Borrado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en borrarContribuyente CONTRIBUYENTE_DAO: " + e.getMessage());
			return "Error en borrarContribuyente CONTRIBUYENTE_DAO " + e.getMessage() + " - " + e.toString();
		}

	}

	@Override
	public Contribuyente leerContribuyente(int idContribuyente) {
		Contribuyente movi = null;
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select * "
					+ "from contribuyentes where id_contribuyente = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idContribuyente);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				movi = new Contribuyente();
				movi.setIdContribuyente(rs.getInt("id_contribuyente"));
				movi.setCifNif(rs.getString("cif_nif"));
				movi.setNombreRazon(rs.getString("nombre_razon"));
				movi.setDomicilio(rs.getString("domicilio"));
				movi.setPoblacion(rs.getString("poblacion"));
				movi.setCp(rs.getString("cp"));
				movi.setProvincia(rs.getString("provincia"));
				movi.setTelefono(rs.getString("telefono"));
				movi.setEmail(rs.getString("email"));
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en leerContribuyente CONTRIBUYENTE_DAO " + idContribuyente + " : " + e.getMessage());
			Messagebox.show("Error en leerContribuyente CONTRIBUYENTE_DAO " + idContribuyente + e.getMessage() + " - " + e.toString());
		}
		return movi;
	}
	
	@Override
	public Contribuyente leerContribuyenteNif(String nif) {
		Contribuyente movi = null;
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select * "
					+ "from contribuyentes where cif_nif = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, nif);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				movi = new Contribuyente();
				movi.setCifNif(rs.getString("cif_nif"));
				movi.setNombreRazon(rs.getString("nombre_razon"));
				movi.setDomicilio(rs.getString("domicilio"));
				movi.setPoblacion(rs.getString("poblacion"));
				movi.setCp(rs.getString("cp"));
				movi.setProvincia(rs.getString("provincia"));
				movi.setTelefono(rs.getString("telefono"));
				movi.setEmail(rs.getString("email"));
				movi.setIdContribuyente(rs.getInt("id_contribuyente"));
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en leerContribuyenteNif CONTRIBUYENTE_DAO " + nif + " : " + e.getMessage());
			Messagebox.show("Error en leerContribuyenteNif CONTRIBUYENTE_DAO " + nif + e.getMessage() + " - " + e.toString());
		}
		return movi;
	}

}
