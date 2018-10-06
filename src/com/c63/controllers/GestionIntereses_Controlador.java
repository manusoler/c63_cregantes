package com.c63.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Intereses;
import com.c63.modelo.dao.Intereses_Dao;
import com.c63.modelo.dao.Intereses_IDao;

public class GestionIntereses_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window winGestionIntereses;
	@Wire
	private Groupbox groupGestIntereses;
	@Wire
	private Datebox txtFechaDesde;
	@Wire
	private Datebox txtFechaHasta;
	@Wire
	private Textbox txtDenominacion;
	@Wire
	private Doublebox txtPorcentaje;
	@Wire
	private Button btnGrabarModificacion;
	@Wire
	private Button btnModificar;
	@Wire
	private Button btnGrabarNuevo;
	@Wire
	private Button btnCancelar;
	private Window padre;
	private String accion;
	private String fecha_desde;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		fecha_desde = (String) execution.getArg().get("fecha_desde");
		padre = (Window) execution.getArg().get("padre");
		accion = (String) execution.getArg().get("accion");
		if ("NUEVO".equals(accion)) {
			groupGestIntereses.setTitle("Nuevo Periodo de Intereses");
			pantallaNuevo();
		} else { // DETALLE
			groupGestIntereses.setTitle("Detalle de Periodo de Intereses");
			pantallaDetalle();
		}
	}

	public void pantallaDetalle() {
		// Consultamos el proceso
		consultar();
		// Y establecemos el estado de los campos
		txtFechaDesde.setDisabled(true);
		txtFechaHasta.setReadonly(true);
		txtDenominacion.setReadonly(true);
		txtPorcentaje.setReadonly(true);
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(true);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaModificar() {
		groupGestIntereses.setTitle("Modificar Periodod e Intereses");
		txtFechaDesde.setDisabled(true);
		txtFechaHasta.setReadonly(false);
		txtDenominacion.setReadonly(false);
		txtPorcentaje.setReadonly(false);
		btnGrabarModificacion.setVisible(true);
		btnModificar.setVisible(false);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaNuevo() throws WrongValueException, ParseException {
		Intereses_IDao dao = new Intereses_Dao();
		java.sql.Date fecha = dao.obtenerSiguienteFechaDesde();
		
		if(fecha == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			txtFechaDesde.setValue(sdf.parse("2015-01-01"));
		} else {
			txtFechaDesde.setValue(new Date(fecha.getTime()));
		}
		// Establecemos los estados de los campos y botones
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(false);
	}

	private void consultar() {
		Intereses_IDao dao = new Intereses_Dao();
		Intereses intereses = dao.leerIntereses(fecha_desde);
		// Ahora cargamos el proceso en la ventana
		txtFechaDesde.setValue(intereses.getFecha_desde());
		txtFechaHasta.setValue(intereses.getFecha_hasta());
		txtDenominacion.setValue(intereses.getDenominacion());
		txtPorcentaje.setValue(intereses.getPorcentaje());

	}

	@Listen("onClick = #btnGrabarNuevo")
	public void nuevo() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Intereses_IDao dao = new Intereses_Dao();
		String mensaje = dao.insertarIntereses(sdf.format(txtFechaDesde.getValue()),
				sdf.format(txtFechaHasta.getValue()), txtDenominacion.getValue(), txtPorcentaje.getValue());
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnGrabarModificacion")
	public void modificar() throws Exception, ParseException {
		Intereses_IDao dao = new Intereses_Dao();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String mensaje = dao.updateIntereses(sdf.format(txtFechaDesde.getValue()), sdf.format(txtFechaHasta.getValue()),
				txtDenominacion.getValue(), txtPorcentaje.getValue());
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnModificar")
	public void btnModificar() {
		pantallaModificar();
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGestionIntereses.detach();
		// Y mandamos evento a la ventana padre para que reaccione
		Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
