package com.c63.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import com.c63.comun.ConexionBD;
import com.c63.modelo.BancosC63;

public class BancosC63_Dao implements BancosC63_IDao {

	@Override
	public List<BancosC63> listarBancosC63() {
		List<BancosC63> movimientos = new ArrayList<BancosC63>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select codigoentidad, c63banco, c63sucursal, c63nombre, "
					+ "c63codine, c63iban from bancosc63 order by codigoentidad ";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				BancosC63 movi = new BancosC63();
				movi.setCodigoentidad(rs.getString("codigoentidad"));
				movi.setC63banco(rs.getString("c63banco"));
				movi.setC63sucursal(rs.getString("c63sucursal"));
				movi.setC63nombre(rs.getString("c63nombre"));
				movi.setC63codine(rs.getString("c63codine"));
				movi.setC63iban(rs.getString("c63iban"));
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarBancosC63 BANCOSC63_DAO: " + e.getMessage());
			Messagebox.show("Error en listarBancosC63 BANCOSC63_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}

	@Override
	public String insertarBancoC63(String codigoentidad, String c63banco, String c63sucursal, String c63nombre,
			String c63codine, String c63iban) {

		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "select count(*) as numbancos from bancosc63 where " + "codigoentidad = ? and c63banco = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			stmt.setString(2, c63banco);

			ResultSet rs = stmt.executeQuery();
			int numBancos = 0;
			while (rs.next()) {
				numBancos = rs.getInt("numbancos");
			}
			stmt.close();
			if (numBancos == 0) {
				sql = "insert into bancosc63 (codigoentidad, c63banco, c63sucursal, c63nombre, c63codine, "
						+ "c63iban) values (?, ?, ?, ?, ?, ? )";
				PreparedStatement stmt1 = conn.prepareStatement(sql);

				stmt1.setString(1, codigoentidad);
				stmt1.setString(2, c63banco);
				stmt1.setString(3, c63sucursal);
				stmt1.setString(4, c63nombre);
				stmt1.setString(5, c63codine);
				stmt1.setString(6, c63iban);
				int rowCount = stmt1.executeUpdate();
				conn.commit();
				committed = true;
				stmt1.close();
				conn.close();
				return "Banco " + c63banco + " Creado Correctamente.";
			} else {
				conn.close();
				return "ERROR ==> BancosC63 " + c63banco + " ya Existe.";
			}
		} catch (Exception e) {
			logger.error("Error en insertarBancoC63 BANCOSC63_DAO: " + e.getMessage());
			return "Error en insertarBancoC63 BANCOSC63_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public String updateBancosC63(String codigoentidad, String c63banco, String c63sucursal, String c63nombre,
			String c63codine, String c63iban) {

		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update bancosc63 set c63sucursal = ?, c63nombre = ?, c63codine = ?, c63iban = ? where "
					+ "codigoentidad = ? and c63banco = ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			stmt1.setString(1, c63sucursal);
			stmt1.setString(2, c63nombre);
			stmt1.setString(3, c63codine);
			stmt1.setString(4, c63iban);
			stmt1.setString(5, codigoentidad);
			stmt1.setString(6, c63banco);
			int rowCount = stmt1.executeUpdate();
			conn.commit();
			committed = true;
			stmt1.close();
			conn.close();
			return "Banco " + c63banco + " Modificado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en updateBancosC63  BANCOSC63_DAO: " + e.getMessage());
			return "Error en updateBancosC63  BANCOSC63_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public String borrarBancoC63(String codigoentidad, String c63banco) {
		boolean committed = false;
		try {
			String sql = "delete from bancosc63 where codigoentidad = ? and c63banco = ?";
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			stmt.setString(2, c63banco);
			int rowCount = stmt.executeUpdate();
			conn.commit();
			committed = true;
			stmt.close();
			conn.close();
			return "Banco " + c63banco + " Borrado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en borrarBancoC63 BANCOSC63_DAO: " + e.getMessage());
			return "Error en borrarBancoC63 BANCOSC63_DAO " + e.getMessage() + " - " + e.toString();
		}

	}

	@Override
	public BancosC63 leerBancoC63(String codigoentidad, String c63banco) {
		BancosC63 movi = new BancosC63();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select codigoentidad, c63banco, c63sucursal, c63nombre, c63codine, c63iban "
					+ "from bancosc63 where codigoentidad = ? and c63banco = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			stmt.setString(2, c63banco);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				movi.setCodigoentidad(rs.getString("codigoentidad"));
				movi.setC63banco(rs.getString("c63banco"));
				movi.setC63sucursal(rs.getString("c63sucursal"));
				movi.setC63nombre(rs.getString("c63nombre"));
				movi.setC63codine(rs.getString("c63codine"));
				movi.setC63iban(rs.getString("c63iban"));
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en leerBancoC63 BANCOSC63_DAO " + c63banco + " : " + e.getMessage());
			Messagebox.show("Error en leerBancoC63 BANCOSC63_DAO " + c63banco + e.getMessage() + " - " + e.toString());
		}
		return movi;
	}

	@Override
	public List<BancosC63> listarBancosC63Entidad(String codigoentidad) {
		List<BancosC63> movimientos = new ArrayList<BancosC63>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select codigoentidad, c63banco, c63sucursal, c63nombre, "
					+ "c63codine, c63iban from bancosc63 where codigoentidad = ? order by c63banco";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				BancosC63 movi = new BancosC63();
				movi.setCodigoentidad(rs.getString("codigoentidad"));
				movi.setC63banco(rs.getString("c63banco"));
				movi.setC63sucursal(rs.getString("c63sucursal"));
				movi.setC63nombre(rs.getString("c63nombre"));
				movi.setC63codine(rs.getString("c63codine"));
				movi.setC63iban(rs.getString("c63iban"));
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarBancosC63Distinct BANCOSC63_DAO: " + e.getMessage());
			Messagebox.show("Error en listarBancosC63Distinct BANCOSC63_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}
}
