package com.c63.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Messagebox;

import com.c63.comun.ConexionBD;
import com.c63.modelo.Contribuyente;
import com.c63.modelo.Recibos;
import com.c63.modelo.Recibos;

public class Recibos_Dao implements Recibos_IDao {

	@Override
	public List<Recibos> listarRecibos(String codigoentidad, String codObjeto, String anyo, String periodo, String num_regante, String concepto, String nif, String nombrerazon) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ "from recibos, contribuyentes where recibos.id_contribuyente = contribuyentes.id_contribuyente ";
			if(!codigoentidad.isEmpty()) {
				sql = sql.concat("and codigoentidad = ? ");
			}
			if(!codObjeto.isEmpty()) {
				sql = sql.concat("and cod_objeto LIKE ? ");
			}
			if(!anyo.isEmpty()) {
				sql = sql.concat("and ano = ? ");
			}
			if(!periodo.isEmpty()) {
				sql = sql.concat("and periodo = ? ");
			}
			if(!num_regante.isEmpty()) {
				sql = sql.concat("and num_regante = ? ");
			}
			if(!concepto.isEmpty()) {
				sql = sql.concat("and concepto LIKE ? ");
			}
			if(!nif.isEmpty()) {
				sql = sql.concat("and cif_nif LIKE ? ");
			}
			if(!nombrerazon.isEmpty()) {
				sql = sql.concat("and nombre_razon LIKE ? ");
			}
			sql = sql.concat("order by cif_nif asc");
			PreparedStatement stmt = conn.prepareStatement(sql);
			int pos = 1;
			
			if(!codigoentidad.isEmpty()) {
				stmt.setString(pos++, codigoentidad);
			}
			if(!codObjeto.isEmpty()) {
				stmt.setString(pos++, "%"+codObjeto+"%");
			}
			if(!anyo.isEmpty()) {
				stmt.setString(pos++, anyo);
			}
			if(!periodo.isEmpty()) {
				stmt.setString(pos++, periodo);
			}
			if(!num_regante.isEmpty()) {
				stmt.setString(pos++, "%"+num_regante+"%");
			}
			if(!concepto.isEmpty()) {
				stmt.setString(pos++, "%"+concepto+"%");
			}
			if(!nif.isEmpty()) {
				stmt.setString(pos++, "%"+nif+"%");
			}
			if(!nombrerazon.isEmpty()) {
				stmt.setString(pos++, "%"+nombrerazon+"%");
			}
			
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Recibos movi = new Recibos();
				rellenarRecibo(rs, movi);
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibos RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibos RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}

	@Override
	public String insertarRecibo(Recibos recibo) throws Exception{
		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "insert into recibos values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt1 = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			//stmt1.setInt(1, recibo.getIdRecibo());
			stmt1.setNull(1, Types.INTEGER);
			stmt1.setString(2, recibo.getNumRegante());
			stmt1.setString(3, recibo.getCodObjeto());
			stmt1.setString(4, recibo.getDomicilioObjeto());
			stmt1.setString(5, recibo.getConcepto());
			stmt1.setInt(6, recibo.getAno());
			stmt1.setInt(7, recibo.getPeriodo());
			stmt1.setString(8, recibo.getTextoPeriodo());
			stmt1.setDate(9, recibo.getfAsamblea());
			stmt1.setDouble(10, recibo.getPrincipal());
			stmt1.setDouble(11, recibo.getRecargo());
			stmt1.setDouble(12, recibo.getIntereses());
			stmt1.setDouble(13, recibo.getCostas());
			stmt1.setDouble(14, recibo.getIngresosCuenta());
			stmt1.setString(15, recibo.getEstado());
			stmt1.setDate(16, recibo.getfEstado());
			stmt1.setString(17, recibo.getFase());
			stmt1.setDate(18, recibo.getfProvidencia());
			stmt1.setString(19, recibo.getLoteApremio());
			stmt1.setString(20, recibo.getEstadoNotificacion());
			stmt1.setDate(21, recibo.getfNotificacion());
			stmt1.setDate(22, recibo.getfDevolucion());
			stmt1.setString(23, recibo.getLoteBoletin());
			stmt1.setString(24, recibo.getObservaciones());
			stmt1.setString(25, recibo.getEntidad().getCodigoentidad());
			stmt1.setInt(26, recibo.getContribuyente().getIdContribuyente());
			stmt1.setDate(27, recibo.getfEjecutiva());
			stmt1.setString(28, recibo.getLoteNotificacion());

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
			return "Recibo " + id + " Creado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en insertarRecibo RECIBOS_DAO: " + e.getMessage());
			throw new Exception("Error en insertarRecibo RECIBOS_DAO " + e.getMessage());
		}
	}
	
	@Override
	public int[] insertarRecibos(List<Recibos> recibos) throws Exception{
		Contribuyente_IDao contriDao = new Contribuyente_Dao();
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "insert into recibos values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt1 = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			int recuento = 0, recuentoContri = 0;
			for(Recibos recibo : recibos) {
				int idContri = 0;
				// Comprobamos si existe el contribuyente
				Contribuyente contri = null;
				String sql3 = "select * "
						+ "from contribuyentes where cif_nif = ?";

				PreparedStatement stmt3 = conn.prepareStatement(sql3);
				stmt3.setString(1, recibo.getContribuyente().getCifNif());
				ResultSet rs = stmt3.executeQuery();

				while (rs.next()) {
					contri = new Contribuyente();
					contri.setCifNif(rs.getString("cif_nif"));
					contri.setNombreRazon(rs.getString("nombre_razon"));
					contri.setDomicilio(rs.getString("domicilio"));
					contri.setPoblacion(rs.getString("poblacion"));
					contri.setCp(rs.getString("cp"));
					contri.setProvincia(rs.getString("provincia"));
					contri.setTelefono(rs.getString("telefono"));
					contri.setEmail(rs.getString("email"));
					contri.setIdContribuyente(rs.getInt("id_contribuyente"));
				}
				stmt3.close();
				
				if(contri == null) {
					// NO existe, lo insertamos
					String sql2 = "insert into contribuyentes values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
					PreparedStatement stmt2 = conn.prepareStatement(sql2, PreparedStatement.RETURN_GENERATED_KEYS);
					
					//stmt1.setInt(1, recibo.getIdContribuyente());
					stmt2.setNull(1, Types.INTEGER);
					stmt2.setString(2, recibo.getContribuyente().getCifNif());
					stmt2.setString(3, recibo.getContribuyente().getNombreRazon());
					stmt2.setString(4, recibo.getContribuyente().getDomicilio());
					stmt2.setString(5, recibo.getContribuyente().getPoblacion());
					stmt2.setString(6, recibo.getContribuyente().getCp());
					stmt2.setString(7, recibo.getContribuyente().getProvincia());
					stmt2.setString(8, recibo.getContribuyente().getTelefono());
					stmt2.setString(9, recibo.getContribuyente().getEmail());

					stmt2.executeUpdate();
					// Obtenemos el id generado
					idContri = -1;
					ResultSet rs2 = stmt2.getGeneratedKeys();
			        if (rs2.next()){
			        	idContri=rs2.getInt(1);
			        	recuentoContri++;
			        }
					stmt2.close();
				} else {
					idContri = contri.getIdContribuyente();
				}
				
				stmt1.setNull(1, Types.INTEGER);
				stmt1.setString(2, recibo.getNumRegante());
				stmt1.setString(3, recibo.getCodObjeto());
				stmt1.setString(4, recibo.getDomicilioObjeto());
				stmt1.setString(5, recibo.getConcepto());
				stmt1.setInt(6, recibo.getAno());
				stmt1.setInt(7, recibo.getPeriodo());
				stmt1.setString(8, recibo.getTextoPeriodo());
				stmt1.setDate(9, recibo.getfAsamblea());
				stmt1.setDouble(10, recibo.getPrincipal());
				stmt1.setDouble(11, recibo.getRecargo());
				stmt1.setDouble(12, recibo.getIntereses());
				stmt1.setDouble(13, recibo.getCostas());
				stmt1.setDouble(14, recibo.getIngresosCuenta());
				stmt1.setString(15, recibo.getEstado());
				stmt1.setDate(16, recibo.getfEstado());
				stmt1.setString(17, recibo.getFase());
				stmt1.setDate(18, recibo.getfProvidencia());
				stmt1.setString(19, recibo.getLoteApremio());
				stmt1.setString(20, recibo.getEstadoNotificacion());
				stmt1.setDate(21, recibo.getfNotificacion());
				stmt1.setDate(22, recibo.getfDevolucion());
				stmt1.setString(23, recibo.getLoteBoletin());
				stmt1.setString(24, recibo.getObservaciones());
				stmt1.setString(25, recibo.getEntidad().getCodigoentidad());
				stmt1.setInt(26, idContri);
				stmt1.setDate(27, recibo.getfEjecutiva());
				stmt1.setString(28, recibo.getLoteNotificacion());
				if(stmt1.executeUpdate() > 0) {
					recuento++;
				}
			}
			conn.commit();
			stmt1.close();
			conn.close();
			return new int[]{recuento, recuentoContri};
		} catch (Exception e) {
			logger.error("Error en insertarRecibo RECIBOS_DAO: " + e.getMessage());
			throw new Exception("Error en insertarRecibo RECIBOS_DAO: " + e.getMessage());
		}
	}

	@Override
	public String updateRecibo(Recibos recibo) {
		boolean committed = false;
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update recibos set num_regante = ?, cod_objeto = ?, domicilio_objeto = ?, "
					+ "concepto = ?, ano = ?, periodo = ?, texto_periodo = ?, f_asamblea = ?, principal = ?, recargo = ?, intereses = ?, costas = ?, ingresos_cuenta = ?, "
					+ "estado = ?, f_estado = ?, fase = ?, f_providencia = ?, lote_apremio = ?, estado_notificacion = ?, f_notificacion = ?, f_devolucion = ?, lote_boletin = ?, "
					+ "observaciones = ?, f_ejecutiva = ?, lote_notificacion = ? where id_recibo = ?";
			
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			
			stmt1.setString(1, recibo.getNumRegante());
			stmt1.setString(2, recibo.getCodObjeto());
			stmt1.setString(3, recibo.getDomicilioObjeto());
			stmt1.setString(4, recibo.getConcepto());
			stmt1.setInt(5, recibo.getAno());
			stmt1.setInt(6, recibo.getPeriodo());
			stmt1.setString(7, recibo.getTextoPeriodo());
			stmt1.setDate(8, recibo.getfAsamblea());
			stmt1.setDouble(9, recibo.getPrincipal());
			stmt1.setDouble(10, recibo.getRecargo());
			stmt1.setDouble(11, recibo.getIntereses());
			stmt1.setDouble(12, recibo.getCostas());
			stmt1.setDouble(13, recibo.getIngresosCuenta());
			stmt1.setString(14, recibo.getEstado());
			stmt1.setDate(15, recibo.getfEstado());
			stmt1.setString(16, recibo.getFase());
			stmt1.setDate(17, recibo.getfProvidencia());
			stmt1.setString(18, recibo.getLoteApremio());
			stmt1.setString(19, recibo.getEstadoNotificacion());
			stmt1.setDate(20, recibo.getfNotificacion());
			stmt1.setDate(21, recibo.getfDevolucion());
			stmt1.setString(22, recibo.getLoteBoletin());
			stmt1.setString(23, recibo.getObservaciones());
			stmt1.setDate(24, recibo.getfEjecutiva());
			stmt1.setString(25, recibo.getLoteNotificacion());
			stmt1.setInt(26, recibo.getIdRecibo());
			
			int rowCount = stmt1.executeUpdate();
			conn.commit();
			committed = true;
			stmt1.close();
			conn.close();
			return "Recibo " + recibo.getIdRecibo() + " Modificado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en updateRecibos  RECIBOS_DAO: " + e.getMessage());
			return "Error en updateRecibos  RECIBOS_DAO " + e.getMessage() + " - " + e.toString();
		}
	}

	@Override
	public String borrarRecibo(int idRecibo) {
		boolean committed = false;
		try {
			String sql = "delete from recibos where id_recibo = ?";
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idRecibo);
			int rowCount = stmt.executeUpdate();
			conn.commit();
			committed = true;
			stmt.close();
			conn.close();
			return "Recibo " + idRecibo + " Borrado Correctamente.";
		} catch (Exception e) {
			logger.error("Error en borrarRecibo RECIBOS_DAO: " + e.getMessage());
			return "Error en borrarRecibo RECIBOS_DAO " + e.getMessage() + " - " + e.toString();
		}

	}
	
	@Override
	public Recibos calcularImportes(int idRecibo, Date fFinCalculoIntereses) {
		Recibos movi = new Recibos();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select principal, calculoApremio(recibos.id_recibo, ?) as recargo, "
					+ "calculoDemora(recibos.id_recibo, ?) as intereses, "
					+ "costas, ingresos_cuenta from recibos where id_recibo = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDate(1, new java.sql.Date(fFinCalculoIntereses.getTime()));
			stmt.setDate(2, new java.sql.Date(fFinCalculoIntereses.getTime()));
			stmt.setInt(3, idRecibo);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				movi.setPrincipal(rs.getDouble("principal"));
				movi.setRecargo(rs.getDouble("recargo"));
				movi.setIntereses(rs.getDouble("intereses"));
				movi.setCostas(rs.getDouble("costas"));
				movi.setIngresosCuenta(rs.getDouble("ingresos_cuenta"));
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en calcularImportes RECIBOS_DAO " + idRecibo + " : " + e.getMessage());
			Messagebox.show("Error en calcularImportes RECIBOS_DAO " + idRecibo + e.getMessage() + " - " + e.toString());
		}
		return movi;
	}

	@Override
	public Recibos leerRecibo(int idRecibo) {
		Recibos movi = new Recibos();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ "from recibos where id_recibo = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, idRecibo);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				movi.setIdRecibo(rs.getInt("id_recibo"));
				movi.setNumRegante(rs.getString("num_regante"));
				movi.setCodObjeto(rs.getString("cod_objeto"));
				movi.setDomicilioObjeto(rs.getString("domicilio_objeto"));
				movi.setConcepto(rs.getString("concepto"));
				movi.setAno(rs.getInt("ano"));
				movi.setPeriodo(rs.getInt("periodo"));
				movi.setTextoPeriodo(rs.getString("texto_periodo"));
				movi.setfAsamblea(rs.getDate("f_asamblea"));
				movi.setPrincipal(rs.getDouble("principal"));
				movi.setRecargo(rs.getDouble("recargo"));
				movi.setIntereses(rs.getDouble("intereses"));
				movi.setCostas(rs.getDouble("costas"));
				movi.setIngresosCuenta(rs.getDouble("ingresos_cuenta"));
				movi.setEstado(rs.getString("estado"));
				movi.setfEstado(rs.getDate("f_estado"));
				movi.setFase(rs.getString("fase"));
				movi.setfProvidencia(rs.getDate("f_providencia"));
				movi.setLoteApremio(rs.getString("lote_apremio"));
				movi.setEstadoNotificacion(rs.getString("estado_notificacion"));
				movi.setfNotificacion(rs.getDate("f_notificacion"));
				movi.setfDevolucion(rs.getDate("f_devolucion"));
				movi.setLoteBoletin(rs.getString("lote_boletin"));
				movi.setObservaciones(rs.getString("observaciones"));
				movi.setfEjecutiva(rs.getDate("f_ejecutiva"));
				movi.setLoteNotificacion(rs.getString("lote_notificacion"));
				
				Entidades_IDao daoe = new Entidades_Dao();
				Contribuyente_IDao daoc = new Contribuyente_Dao();
				movi.setEntidad(daoe.leerEntidad(rs.getString("codigoentidad")));
				movi.setContribuyente(daoc.leerContribuyente(rs.getInt("id_contribuyente")));
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en leerRecibo RECIBOS_DAO " + idRecibo + " : " + e.getMessage());
			Messagebox.show("Error en leerRecibo RECIBOS_DAO " + idRecibo + e.getMessage() + " - " + e.toString());
		}
		return movi;
	}

	@Override
	public List<Recibos> listarRecibosEntidad(String codigoentidad) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select min(id_recibo) as id_recibo, min(num_regante) as num_regante, min(cod_objeto) as cod_objeto, min(domicilio_objeto) as domicilio_objeto, "
					+ "min(concepto) as concepto, min(ano) as ano, min(periodo) as periodo, min(texto_periodo) as texto_periodo, min(f_asamblea) as f_asamblea, min(principal) as principal, min(calculoApremio(recibos.id_recibo, curdate())) as recargo, min(calculoDemora(recibos.id_recibo, curdate())) as intereses, "
					+ "min(costas) as costas, min(ingresos_cuenta) as ingresos_cuenta, min(estado) as estado, min(f_estado) as f_estado, min(fase) as fase, min(f_providencia) as f_providencia, min(lote_apremio) as lote_apremio, "
					+ "min(estado_notificacion) as estado_notificacion, min(f_notificacion) as f_notificacion, min(f_devolucion) as f_devolucion, min(lote_boletin) as lote_boletin, min(observaciones) as observaciones, "
					+ "min(codigoentidad) as codigoentidad, id_contribuyente, min(f_ejecutiva) as f_ejecutiva, min(lote_notificacion) as lote_notificacion "
					+ "from recibos where codigoentidad = ? group by id_contribuyente";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Recibos movi = new Recibos();
				movi.setIdRecibo(rs.getInt("id_recibo"));
				movi.setNumRegante(rs.getString("num_regante"));
				movi.setCodObjeto(rs.getString("cod_objeto"));
				movi.setDomicilioObjeto(rs.getString("domicilio_objeto"));
				movi.setConcepto(rs.getString("concepto"));
				movi.setAno(rs.getInt("ano"));
				movi.setPeriodo(rs.getInt("periodo"));
				movi.setTextoPeriodo(rs.getString("texto_periodo"));
				movi.setfAsamblea(rs.getDate("f_asamblea"));
				movi.setPrincipal(rs.getDouble("principal"));
				movi.setRecargo(rs.getDouble("recargo"));
				movi.setIntereses(rs.getDouble("intereses"));
				movi.setCostas(rs.getDouble("costas"));
				movi.setIngresosCuenta(rs.getDouble("ingresos_cuenta"));
				movi.setEstado(rs.getString("estado"));
				movi.setfEstado(rs.getDate("f_estado"));
				movi.setFase(rs.getString("fase"));
				movi.setfProvidencia(rs.getDate("f_providencia"));
				movi.setLoteApremio(rs.getString("lote_apremio"));
				movi.setEstadoNotificacion(rs.getString("estado_notificacion"));
				movi.setfNotificacion(rs.getDate("f_notificacion"));
				movi.setfDevolucion(rs.getDate("f_devolucion"));
				movi.setLoteBoletin(rs.getString("lote_boletin"));
				movi.setObservaciones(rs.getString("observaciones"));
				movi.setfEjecutiva(rs.getDate("f_ejecutiva"));
				movi.setLoteNotificacion(rs.getString("lote_notificacion"));
				
				Entidades_IDao daoe = new Entidades_Dao();
				Contribuyente_IDao daoc = new Contribuyente_Dao();
				movi.setEntidad(daoe.leerEntidad(rs.getString("codigoentidad")));
				movi.setContribuyente(daoc.leerContribuyente(rs.getInt("id_contribuyente")));
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibosEntidad RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibosEntidad RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}
	
	@Override
	public List<Recibos> listarRecibosRelacionApremio(String codigoentidad) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ " from recibos, contribuyentes where "
					+ "recibos.id_contribuyente = contribuyentes.id_contribuyente and estado = 'P' "
					+ "and (lote_apremio is NULL or lote_apremio = '') and fase = 'E' and codigoentidad = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Recibos movi = new Recibos();
				rellenarRecibo(rs, movi);
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibosRelacionApremio RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibosRelacionApremio RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}
	
	@Override
	public List<Recibos> listarRecibosParaNotif(String codigoentidad, Date fProvidencia) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ " from recibos, contribuyentes where "
					+ "recibos.id_contribuyente = contribuyentes.id_contribuyente and estado = 'P' "
					+ "and fase = 'A' and ( lote_notificacion is NULL or lote_notificacion LIKE '') and codigoentidad = ? and f_providencia <= ? "
					+ "and ( estado_notificacion is NULL or estado_notificacion = 0)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codigoentidad);
			stmt.setDate(2, new java.sql.Date(fProvidencia.getTime()));
			
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Recibos movi = new Recibos();
				rellenarRecibo(rs, movi);
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibosParaNotif RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibosParaNotif RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}

	@Override
	public int crearLoteApremio(List<Recibos> listaRecibos, String codLote) {
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update recibos set lote_apremio = ? where id_recibo = ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			for(Recibos recibo : listaRecibos) {
				stmt1.setString(1, codLote);
				stmt1.setInt(2, recibo.getIdRecibo());
				int rowCount = stmt1.executeUpdate();
			}
			stmt1.close();
			conn.commit();
			conn.close();
			return listaRecibos.size();
		} catch (Exception e) {
			logger.error("Error en marcarRecibosLoteApremio RECIBOS_DAO: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public List<Recibos> listarRecibosLote(String codLote) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ " from recibos, contribuyentes where "
					+ "recibos.id_contribuyente = contribuyentes.id_contribuyente and estado = 'P' "
					+ " and fase = 'E' and lote_apremio = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codLote);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Recibos movi = new Recibos();
				rellenarRecibo(rs, movi);
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibosLote RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibosLote RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}
	
	@Override
	public List<Recibos> listarRecibosLoteBoletin(String codLote) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ " from recibos, contribuyentes where "
					+ "recibos.id_contribuyente = contribuyentes.id_contribuyente and estado = 'P' "
					+ " and fase = 'A' and estado_notificacion = '2' and lote_boletin = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codLote);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Recibos movi = new Recibos();
				rellenarRecibo(rs, movi);
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibosLoteBoletin RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibosLoteBoletin RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}

	private void rellenarRecibo(ResultSet rs, Recibos movi) throws SQLException {
		movi.setIdRecibo(rs.getInt("id_recibo"));
		movi.setNumRegante(rs.getString("num_regante"));
		movi.setCodObjeto(rs.getString("cod_objeto"));
		movi.setDomicilioObjeto(rs.getString("domicilio_objeto"));
		movi.setConcepto(rs.getString("concepto"));
		movi.setAno(rs.getInt("ano"));
		movi.setPeriodo(rs.getInt("periodo"));
		movi.setTextoPeriodo(rs.getString("texto_periodo"));
		movi.setfAsamblea(rs.getDate("f_asamblea"));
		movi.setPrincipal(rs.getDouble("principal"));
		movi.setRecargo(rs.getDouble("recargo"));
		movi.setIntereses(rs.getDouble("intereses"));
		movi.setCostas(rs.getDouble("costas"));
		movi.setIngresosCuenta(rs.getDouble("ingresos_cuenta"));
		movi.setEstado(rs.getString("estado"));
		movi.setfEstado(rs.getDate("f_estado"));
		movi.setFase(rs.getString("fase"));
		movi.setfProvidencia(rs.getDate("f_providencia"));
		movi.setLoteApremio(rs.getString("lote_apremio"));
		movi.setEstadoNotificacion(rs.getString("estado_notificacion"));
		movi.setfNotificacion(rs.getDate("f_notificacion"));
		movi.setfDevolucion(rs.getDate("f_devolucion"));
		movi.setLoteBoletin(rs.getString("lote_boletin"));
		movi.setObservaciones(rs.getString("observaciones"));
		movi.setfEjecutiva(rs.getDate("f_ejecutiva"));
		movi.setLoteNotificacion(rs.getString("lote_notificacion"));
		
		Entidades_IDao daoe = new Entidades_Dao();
		Contribuyente_IDao daoc = new Contribuyente_Dao();
		movi.setEntidad(daoe.leerEntidad(rs.getString("codigoentidad")));
		movi.setContribuyente(daoc.leerContribuyente(rs.getInt("recibos.id_contribuyente")));
	}

	@Override
	public int marcarLoteApremio(String codLote, Date fProvidencia) {
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update recibos set f_providencia = ?, fase = 'A' where lote_apremio = ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			stmt1.setDate(1, new java.sql.Date(fProvidencia.getTime()));
			stmt1.setString(2, codLote);
			int rowCount = stmt1.executeUpdate();
			stmt1.close();
			conn.commit();
			conn.close();
			return rowCount;
		} catch (Exception e) {
			logger.error("Error en marcarLoteApremio RECIBOS_DAO: " + e.getMessage());
			return 0;
		}
	}
	
	@Override
	public int marcarLoteBoletin(String codLote, Date fNotificacion) {
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "update recibos set f_notificacion = ?, fase = 'X', estado_notificacion = '1' where lote_boletin = ?";
			PreparedStatement stmt1 = conn.prepareStatement(sql);
			stmt1.setDate(1, new java.sql.Date(fNotificacion.getTime()));
			stmt1.setString(2, codLote);
			int rowCount = stmt1.executeUpdate();
			stmt1.close();
			conn.commit();
			conn.close();
			return rowCount;
		} catch (Exception e) {
			logger.error("Error en marcarLoteBoletin RECIBOS_DAO: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public int marcarNotifApremio(int idRecibo, String estado, Date fNotif, String codLoteBoletin) {
		try {
			Connection conn = ConexionBD.conBD_recore();
			conn.setAutoCommit(false);
			String sql = "";
			PreparedStatement stmt1 = null;
			
			if("2".equals(estado)) {
				sql = "update recibos set estado_notificacion = ?, f_devolucion = ?, lote_boletin = ? where id_recibo = ?";
				stmt1 = conn.prepareStatement(sql);
				stmt1.setString(1,estado);
				stmt1.setDate(2, new java.sql.Date(fNotif.getTime()));
				stmt1.setString(3, codLoteBoletin);
				stmt1.setInt(4, idRecibo);
			} else {
				sql = "update recibos set fase = 'X', estado_notificacion = ?, f_notificacion = ? where id_recibo = ?";
				stmt1 = conn.prepareStatement(sql);
				stmt1.setString(1,estado);
				stmt1.setDate(2, new java.sql.Date(fNotif.getTime()));
				stmt1.setInt(3, idRecibo);
			}
			int rowCount = stmt1.executeUpdate();
			stmt1.close();
			conn.commit();
			conn.close();
			return rowCount;
		} catch (Exception e) {
			logger.error("Error en marcarNotifApremio RECIBOS_DAO: " + e.getMessage());
			return 0;
		}
	}

	@Override
	public int crearLoteNotif(List<Recibos> listaRecibos, String codLote) {
	try {
		Connection conn = ConexionBD.conBD_recore();
		conn.setAutoCommit(false);
		String sql = "update recibos set lote_notificacion = ? where id_recibo = ?";
		PreparedStatement stmt1 = conn.prepareStatement(sql);
		for(Recibos recibo : listaRecibos) {
			stmt1.setString(1, codLote);
			stmt1.setInt(2, recibo.getIdRecibo());
			int rowCount = stmt1.executeUpdate();
		}
		stmt1.close();
		conn.commit();
		conn.close();
		return listaRecibos.size();
	} catch (Exception e) {
		logger.error("Error en marcarRecibosLoteNotificacion RECIBOS_DAO: " + e.getMessage());
		return 0;
	}
	}

	@Override
	public List<Recibos> listarRecibosLoteNotif(String codLote) {
		List<Recibos> movimientos = new ArrayList<Recibos>();
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "select recibos.id_recibo, num_regante, cod_objeto, domicilio_objeto, "
					+ "concepto, ano, periodo, texto_periodo, f_asamblea, principal, calculoApremio(recibos.id_recibo, curdate()) as recargo, calculoDemora(recibos.id_recibo, curdate()) as intereses, "
					+ "costas, ingresos_cuenta, estado, f_estado, fase, f_providencia, lote_apremio, "
					+ "estado_notificacion, f_notificacion, f_devolucion, lote_boletin, observaciones, "
					+ "recibos.codigoentidad, recibos.id_contribuyente, f_ejecutiva, lote_notificacion "
					+ " from recibos, contribuyentes where "
					+ "recibos.id_contribuyente = contribuyentes.id_contribuyente and estado = 'P' "
					+ " and fase = 'A' and lote_notificacion = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, codLote);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Recibos movi = new Recibos();
				rellenarRecibo(rs, movi);
				movimientos.add(movi);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en listarRecibosLoteNotificacion RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en listarRecibosLoteNotificacion RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return movimientos;
	}

	@Override
	public double obtenerTotalDeudaContri(String nifContri, String codigoEntidad) {
		double total = 0;
		try {
			Connection conn = ConexionBD.conBD_recore();
			String sql = "SELECT SUM(recibos.principal + calculoApremio(recibos.id_recibo, curdate()) + calculoDemora(recibos.id_recibo, curdate()) + recibos.costas - recibos.ingresos_cuenta) as total FROM contribuyentes, recibos WHERE contribuyentes.id_contribuyente = recibos.id_contribuyente and recibos.estado = 'P' and recibos.fase = 'X' and contribuyentes.cif_nif LIKE ? and recibos.codigoentidad LIKE ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, nifContri);
			stmt.setString(2, codigoEntidad);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				total = rs.getDouble("total");
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			logger.error("Error en obtenerTotalDeudaContri RECIBOS_DAO: " + e.getMessage());
			Messagebox.show("Error en obtenerTotalDeudaContri RECIBOS_DAO " + e.getMessage() + " - " + e.toString());
		}
		return total;
	}
}
