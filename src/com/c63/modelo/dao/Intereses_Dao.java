package com.c63.modelo.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Messagebox;

import com.c63.comun.ConexionBD;
import com.c63.modelo.Intereses;

public class Intereses_Dao implements Intereses_IDao {
	/*
	 * (non-Javadoc)
	 * 
	 */

	@Override
	public List<Intereses> listarIntereses() {

		List<Intereses> movimientos = new ArrayList<Intereses>();

		try {
			String sql = "";
			Connection conn = ConexionBD.conBD_recore();
			sql = "select " + "		fecha_desde, " + "		fecha_hasta,  " + "		denominacion,  "
					+ "		porcentaje " + "from intereses " + "order by fecha_desde ";

			PreparedStatement stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Intereses movi = new Intereses();

				movi.setFecha_desde(rs.getDate("fecha_desde"));
				movi.setFecha_hasta(rs.getDate("fecha_hasta"));
				movi.setDenominacion(rs.getString("denominacion"));
				movi.setPorcentaje(rs.getDouble("porcentaje"));

				movimientos.add(movi);
			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("Error en listarIntereses INTERESES_DAO");
			System.out.println(e.getMessage());
			Messagebox.show("Error en listarIntereses INTERESES_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;

	}

	@Override
	public String insertarIntereses(String fecha_desde, String fecha_hasta, String denominacion, Double porcentaje) {

		boolean committed = false;

		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);

			PreparedStatement stmt1 = conn.prepareStatement("insert into intereses " + " (" + "		fecha_desde, "
					+ "		fecha_hasta,  " + "		denominacion,  " + "		porcentaje " + ") " + " values "
					+ " (?, ?, ?, ? " + "  )");

			stmt1.setString(1, fecha_desde);
			stmt1.setString(2, fecha_hasta);
			stmt1.setString(3, denominacion);
			stmt1.setDouble(4, porcentaje);

			int rowCount = stmt1.executeUpdate();

			conn.commit();
			committed = true;
			stmt1.close();
			conn.close();
			return "Creado Intervalo de Intereses Correctamente.";

		} catch (Exception e) {
			System.out.println("Error en insertarIntereses INTERESES_DAO");
			System.out.println(e.getMessage());
			return "Error en insertarIntereses INTERESES_DAO " + e.getMessage() + " - " + e.toString();
		}

	}

	@Override
	public String updateIntereses(String fecha_desde, String fecha_hasta, String denominacion, Double porcentaje) {

		boolean committed = false;

		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);

			PreparedStatement stmt1 = conn.prepareStatement("update intereses " + " set" + "		fecha_desde = ?, "
					+ "		fecha_hasta = ?,  " + "		denominacion = ?,  " + "		porcentaje = ? " + " where "
					+ "		fecha_desde = ? ");
			stmt1.setString(1, fecha_desde);
			stmt1.setString(2, fecha_hasta);
			stmt1.setString(3, denominacion);
			stmt1.setDouble(4, porcentaje);

			stmt1.setString(5, fecha_desde);
			int rowCount = stmt1.executeUpdate();

			conn.commit();
			committed = true;
			stmt1.close();
			conn.close();
			return "Modificado Intervalo de Intereses Correctamente.";

		} catch (Exception e) {
			System.out.println("Error en updateIntereses  INTERESES_DAO ");
			System.out.println(e.getMessage());
			return "Error en updateIntereses  INTERESES_DAO " + e.getMessage() + " - " + e.toString();
		}

	}

	@Override
	public String borrarIntereses() {

		boolean committed = false;

		try {

			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);

			String sql = "";
			sql = "select max(fecha_desde) fecha_desde " + "from intereses ";

			PreparedStatement stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			Date fecha_desde = null;
			while (rs.next()) {
				fecha_desde = rs.getDate("fecha_desde");
			}

			stmt.close();

			String sqlD = "delete from intereses where fecha_desde = ?";

			PreparedStatement stmtD = conn.prepareStatement(sqlD);
			stmtD.setDate(1, fecha_desde);

			int rowCount = stmtD.executeUpdate();

			stmtD.close();

			conn.commit();
			// committed = true;

			conn.close();
			return "Intervalo de Intereses Borrado Correctamente.";

		} catch (Exception e) {
			System.out.println("Error en borrarIntereses INTERESES_DAO.");
			System.out.println(e.getMessage());

			return "Error en borrarIntereses  INTERESES_DAO " + e.getMessage() + " - " + e.toString();
		}

	}

	@Override
	public Intereses leerIntereses(String fecha_desde) {

		Intereses movi = new Intereses();

		try {

			String sql = "";
			Connection conn = ConexionBD.conBD_recore();
			sql = "select " + "		fecha_desde, " + "		fecha_hasta,  " + "		denominacion,  "
					+ "		porcentaje " + "from intereses " + "where "
					+ "		fecha_desde = date_format(?, '%Y-%m-%d') ";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, fecha_desde);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				movi.setFecha_desde(rs.getDate("fecha_desde"));
				movi.setFecha_hasta(rs.getDate("fecha_hasta"));
				movi.setDenominacion(rs.getString("denominacion"));
				movi.setPorcentaje(rs.getDouble("porcentaje"));

			}

			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("Error en leerIntereses INTERESES_DAO ");
			System.out.println(e.getMessage());
			Messagebox.show("Error en leerIntereses INTERESES_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movi;

	}

	@Override
	public Date obtenerSiguienteFechaDesde() {
		Date fecha = null;
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "SELECT DATE_ADD(MAX(fecha_hasta), INTERVAL 1 DAY) as fecha from intereses";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				fecha = rs.getDate("fecha");
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("Error en obtenerSiguienteFechaDesde INTERESES_DAO ");
			System.out.println(e.getMessage());
			Messagebox.show("Error en obtenerSiguienteFechaDesde INTERESES_DAO " + e.getMessage() + " - " + e.toString());
		}
		return fecha;
	}

}
