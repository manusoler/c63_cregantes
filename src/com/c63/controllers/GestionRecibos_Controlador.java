package com.c63.controllers;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.comun.FuncionesComunes;
import com.c63.modelo.Contribuyente;
import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class GestionRecibos_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window winGestionRecibos;
	@Wire
	private Groupbox group;
	@Wire
	private Listbox lbCodigoEntidad;
	@Wire
	private Listbox lbEstado;
	@Wire
	private Listbox lbFase;
	@Wire
	private Listbox lbEstadoNotif;
	@Wire
	private Textbox txtId;
	@Wire
	private Textbox txtIdContri;
	@Wire
	private Textbox txtNumRegante;
	@Wire
	private Textbox txtCodObjeto;
	@Wire
	private Textbox txtDomObjeto;
	@Wire
	private Textbox txtConcepto;
	@Wire
	private Textbox txtAno;
	@Wire
	private Textbox txtPeriodo;
	@Wire
	private Textbox txtTextoPeriodo;
	@Wire
	private Datebox fAsamblea;
	@Wire
	private Doublebox txtPrincipal;
	@Wire
	private Doublebox txtRecargo;
	@Wire
	private Doublebox txtIntereses;
	@Wire
	private Doublebox txtCostas;
	@Wire
	private Doublebox txtIngCuenta;
	@Wire
	private Datebox fEstado;
	@Wire
	private Datebox fProvidencia;
	@Wire
	private Textbox txtLoteApremio;
	@Wire
	private Datebox fNotificacion;
	@Wire
	private Datebox fDevolucion;
	@Wire
	private Datebox fEjecutiva;
	@Wire
	private Textbox txtLoteBoletín;
	@Wire
	private Textbox txtLoteNotificacion;
	@Wire
	private Textbox txtObservaciones;
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
	private int idRecibo;
	
	private int idContribuyente;
	private String nifContri;
	private String nombreContri;
	
	

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		idRecibo = execution.getArg().get("idRecibo") != null ? (Integer) execution.getArg().get("idRecibo") : 0;
		idContribuyente = execution.getArg().get("idContribuyente") != null ? (Integer) execution.getArg().get("idContribuyente") : 0;
		nifContri = (String) execution.getArg().get("nifContri");
		nombreContri = (String) execution.getArg().get("nombreContri");
		padre = (Window) execution.getArg().get("padre");
		accion = (String) execution.getArg().get("accion");

		// Obtenemos los codigos de entidad
		List<Entidades> lista = new ArrayList<Entidades>();
		Entidades_IDao dao = new Entidades_Dao();
		lista = dao.listarEntidades();
		lbCodigoEntidad.setModel(new ListModelList<Entidades>(lista));
	}

	@Listen("onCreate = #winGestionRecibos")
	public void onCreate() {
		if ("NUEVO".equals(accion)) {
			pantallaNuevo();
		} else { // DETALLE
			pantallaDetalle();
		}
	}

	public void pantallaDetalle() {
		// Consultamos el proceso
		consultar();
		group.setTitle("Detalle de Recibo " + idRecibo + "  para Contribuyente " + nifContri + " - "+ nombreContri);
		// Y establecemos el estado de los campos
		txtId.setDisabled(true);
		txtIdContri.setDisabled(true);
		txtNumRegante.setDisabled(true);
		txtCodObjeto.setDisabled(true);
		txtDomObjeto.setDisabled(true);
		txtConcepto.setDisabled(true);
		txtAno.setDisabled(true);
		txtPeriodo.setDisabled(true);
		txtTextoPeriodo.setDisabled(true);
		fAsamblea.setDisabled(true);
		txtPrincipal.setDisabled(true);
		txtRecargo.setDisabled(true);
		txtIntereses.setDisabled(true);
		txtCostas.setDisabled(true);
		txtIngCuenta.setDisabled(true);
		fEstado.setDisabled(true);
		fProvidencia.setDisabled(true);
		txtLoteApremio.setDisabled(true);
		fNotificacion.setDisabled(true);
		fDevolucion.setDisabled(true);
		fEjecutiva.setDisabled(true);
		txtLoteBoletín.setDisabled(true);
		txtLoteNotificacion.setDisabled(true);
		txtObservaciones.setDisabled(true);
		lbCodigoEntidad.setDisabled(true);
		lbEstado.setDisabled(true);
		lbFase.setDisabled(true);
		lbEstadoNotif.setDisabled(true);

		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(true);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaModificar() {
		group.setTitle("Modificar Recibo " + idRecibo + "  para Contribuyente " + nifContri + " - "+ nombreContri);

		txtNumRegante.setDisabled(false);
		txtCodObjeto.setDisabled(false);
		txtDomObjeto.setDisabled(false);
		txtConcepto.setDisabled(false);
		txtAno.setDisabled(false);
		txtPeriodo.setDisabled(false);
		txtTextoPeriodo.setDisabled(false);
		fAsamblea.setDisabled(false);
		txtPrincipal.setDisabled(false);
		txtRecargo.setDisabled(false);
		txtIntereses.setDisabled(false);
		txtCostas.setDisabled(false);
		txtIngCuenta.setDisabled(false);
		fEstado.setDisabled(false);
		fProvidencia.setDisabled(false);
		txtLoteApremio.setDisabled(false);
		fNotificacion.setDisabled(false);
		fDevolucion.setDisabled(false);
		fEjecutiva.setDisabled(false);
		txtLoteBoletín.setDisabled(false);
		txtLoteNotificacion.setDisabled(false);
		txtObservaciones.setDisabled(false);
		lbCodigoEntidad.setDisabled(false);
		lbEstado.setDisabled(false);
		lbFase.setDisabled(false);
		lbEstadoNotif.setDisabled(false);

		btnGrabarModificacion.setVisible(true);
		btnModificar.setVisible(false);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaNuevo() {
		group.setTitle("Nuevo Recibo para Contribuyente " + nifContri + " - "+ nombreContri);
		// Establecemos los estados de los campos y botones
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(false);

		lbEstado.setDisabled(true);
		lbFase.setDisabled(true);
		txtId.setDisabled(true);
		txtIdContri.setDisabled(true);
		
		txtIdContri.setValue(String.valueOf(idContribuyente));
		FuncionesComunes.seleccionarValorPorDefecto(lbEstado, "P");
		FuncionesComunes.seleccionarValorPorDefecto(lbFase, "E");
		FuncionesComunes.seleccionarValorPorDefecto(lbEstadoNotif, "0");
	}

	private void consultar() {
		Recibos_IDao dao = new Recibos_Dao();
		Recibos recibo = dao.leerRecibo(idRecibo);
		// Ahora cargamos el proceso en la ventana

		FuncionesComunes.seleccionarValorPorDefecto(lbCodigoEntidad, recibo.getEntidad().getCodigoentidad());
		FuncionesComunes.seleccionarValorPorDefecto(lbEstado, recibo.getEstado());
		FuncionesComunes.seleccionarValorPorDefecto(lbFase, recibo.getFase());
		FuncionesComunes.seleccionarValorPorDefecto(lbEstadoNotif, recibo.getEstadoNotificacion());
		txtId.setValue(String.valueOf(recibo.getIdRecibo()));
		txtIdContri.setValue(String.valueOf(recibo.getContribuyente().getIdContribuyente()));
		txtNumRegante.setValue(recibo.getNumRegante());
		txtCodObjeto.setValue(recibo.getCodObjeto());
		txtDomObjeto.setValue(recibo.getDomicilioObjeto());
		txtConcepto.setValue(recibo.getConcepto());
		txtAno.setValue(String.valueOf(recibo.getAno()));
		txtPeriodo.setValue(String.valueOf(recibo.getPeriodo()));
		txtTextoPeriodo.setValue(recibo.getTextoPeriodo());
		fAsamblea.setValue(recibo.getfAsamblea());
		txtPrincipal.setValue(recibo.getPrincipal());
		txtRecargo.setValue(recibo.getRecargo());
		txtIntereses.setValue(recibo.getIntereses());
		txtCostas.setValue(recibo.getCostas());
		txtIngCuenta.setValue(recibo.getIngresosCuenta());
		fEstado.setValue(recibo.getfEstado());
		fProvidencia.setValue(recibo.getfProvidencia());
		txtLoteApremio.setValue(recibo.getLoteApremio());
		fNotificacion.setValue(recibo.getfNotificacion());
		fDevolucion.setValue(recibo.getfDevolucion());
		fEjecutiva.setValue(recibo.getfEjecutiva());
		txtLoteBoletín.setValue(recibo.getLoteBoletin());
		txtLoteNotificacion.setValue(recibo.getLoteNotificacion());
		txtObservaciones.setValue(recibo.getObservaciones());
		
		idContribuyente = recibo.getContribuyente().getIdContribuyente();
		nifContri = recibo.getContribuyente().getCifNif();
		nombreContri = recibo.getContribuyente().getNombreRazon();
	}

	@Listen("onClick = #btnGrabarNuevo")
	public void nuevo() throws Exception {
		Recibos_IDao dao = new Recibos_Dao();

		Entidades entidad = new Entidades();
		Contribuyente contri = new Contribuyente();

		entidad.setCodigoentidad((String) lbCodigoEntidad.getSelectedItem().getValue());
		contri.setIdContribuyente(Integer.valueOf(txtIdContri.getValue()));

		java.sql.Date dAsamblea = fAsamblea.getValue() != null ? new java.sql.Date(fAsamblea.getValue().getTime()) : null;
		java.sql.Date dEstado = fEstado.getValue() != null ? new java.sql.Date(fEstado.getValue().getTime()) : null;
		java.sql.Date dProvidencia = fProvidencia.getValue() != null ? new java.sql.Date(fProvidencia.getValue().getTime()) : null;
		java.sql.Date dNotificacion = fNotificacion.getValue() != null ? new java.sql.Date(fNotificacion.getValue().getTime()) : null;
		java.sql.Date dDevolucion = fDevolucion.getValue() != null ? new java.sql.Date(fDevolucion.getValue().getTime()) : null;
		java.sql.Date dEjecutiva = fEjecutiva.getValue() != null ? new java.sql.Date(fEjecutiva.getValue().getTime()) : null;
		
		String mensaje = dao
				.insertarRecibo(
						new Recibos(0, txtNumRegante.getValue(),
								txtCodObjeto.getValue(), txtDomObjeto.getValue(), txtConcepto.getValue(), Integer
										.valueOf(txtAno.getValue()),
								Integer.valueOf(txtPeriodo.getValue()), txtTextoPeriodo.getValue(),
								dAsamblea, txtPrincipal.getValue(),
								txtRecargo.getValue(), txtIntereses.getValue(), txtCostas.getValue(),
								txtIngCuenta.getValue(), (String) lbEstado.getSelectedItem().getValue(),
								dEstado, (String) lbFase.getSelectedItem().getValue(),
								dProvidencia, txtLoteApremio.getValue(),
								(String) lbEstadoNotif.getSelectedItem().getValue(),
								dNotificacion, dDevolucion, txtLoteBoletín.getValue(),
								txtObservaciones.getValue(), entidad, contri, dEjecutiva, txtLoteNotificacion.getValue()));
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnGrabarModificacion")
	public void modificar() {
		Recibos_IDao dao = new Recibos_Dao();

		Entidades entidad = new Entidades();
		Contribuyente contri = new Contribuyente();

		entidad.setCodigoentidad((String) lbCodigoEntidad.getSelectedItem().getValue());
		contri.setIdContribuyente(Integer.valueOf(txtIdContri.getValue()));

		java.sql.Date dAsamblea = fAsamblea.getValue() != null ? new java.sql.Date(fAsamblea.getValue().getTime()) : null;
		java.sql.Date dEstado = fEstado.getValue() != null ? new java.sql.Date(fEstado.getValue().getTime()) : null;
		java.sql.Date dProvidencia = fProvidencia.getValue() != null ? new java.sql.Date(fProvidencia.getValue().getTime()) : null;
		java.sql.Date dNotificacion = fNotificacion.getValue() != null ? new java.sql.Date(fNotificacion.getValue().getTime()) : null;
		java.sql.Date dDevolucion = fDevolucion.getValue() != null ? new java.sql.Date(fDevolucion.getValue().getTime()) : null;
		java.sql.Date dEjecutiva = fEjecutiva.getValue() != null ? new java.sql.Date(fEjecutiva.getValue().getTime()) : null;
		
		String mensaje = dao
				.updateRecibo(
						new Recibos(Integer.valueOf(txtId.getValue()), txtNumRegante.getValue(),
								txtCodObjeto.getValue(), txtDomObjeto.getValue(), txtConcepto.getValue(), Integer
										.valueOf(txtAno.getValue()),
								Integer.valueOf(txtPeriodo.getValue()), txtTextoPeriodo.getValue(),
								dAsamblea, txtPrincipal.getValue(),
								txtRecargo.getValue(), txtIntereses.getValue(), txtCostas.getValue(),
								txtIngCuenta.getValue(), (String) lbEstado.getSelectedItem().getValue(),
								dEstado, (String) lbFase.getSelectedItem().getValue(),
								dProvidencia, txtLoteApremio.getValue(),
								(String) lbEstadoNotif.getSelectedItem().getValue(),
								dNotificacion, dDevolucion, txtLoteBoletín.getValue(),
								txtObservaciones.getValue(), entidad, contri, dEjecutiva, txtLoteNotificacion.getValue()));
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnModificar")
	public void btnModificar() {
		pantallaModificar();
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGestionRecibos.detach();
		// Y mandamos evento a la ventana padre para que reaccione
		Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
