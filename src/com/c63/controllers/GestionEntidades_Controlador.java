package com.c63.controllers;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Entidades;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;

public class GestionEntidades_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window winGestionEntidades;
	@Wire
	private Groupbox groupGestDelegacion;
	@Wire
	private Textbox txtCodigo;
	@Wire
	private Textbox txtNif;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtProvincia;
	@Wire
	private Textbox txtDireccion;
	@Wire
	private Textbox txtCP;
	@Wire
	private Textbox txtLocalidad;
	@Wire
	private Textbox txtTelefono;
	@Wire
	private Textbox txtEmail;
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
	private String codigoEntidad;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		codigoEntidad = (String) execution.getArg().get("codigoEntidad");
		padre = (Window) execution.getArg().get("padre");
		accion = (String) execution.getArg().get("accion");
		if ("NUEVO".equals(accion)) {
			groupGestDelegacion.setTitle("Nueva Entidad");
			pantallaNuevo();
		} else { // DETALLE
			groupGestDelegacion.setTitle("Detalle de Entidad");
			pantallaDetalle();
		}
	}

	public void pantallaDetalle() {
		// Consultamos el proceso
		consultar();
		// Y establecemos el estado de los campos
		txtCodigo.setDisabled(true);
		txtNif.setReadonly(true);
		txtNombre.setReadonly(true);
		txtProvincia.setReadonly(true);
		txtDireccion.setReadonly(true);
		txtCP.setReadonly(true);
		txtLocalidad.setReadonly(true);
		txtTelefono.setReadonly(true);
		txtEmail.setReadonly(true);
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(true);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaModificar() {
		groupGestDelegacion.setTitle("Modificar Entidad");
		txtCodigo.setDisabled(true);
		txtNif.setReadonly(false);
		txtNombre.setReadonly(false);
		txtProvincia.setReadonly(false);
		txtDireccion.setReadonly(false);
		txtCP.setReadonly(false);
		txtLocalidad.setReadonly(false);
		txtTelefono.setReadonly(false);
		txtEmail.setReadonly(false);
		btnGrabarModificacion.setVisible(true);
		btnModificar.setVisible(false);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaNuevo() {
		// Establecemos los estados de los campos y botones
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(false);
	}

	private void consultar() {
		Entidades_IDao dao = new Entidades_Dao();
		Entidades entidad = dao.leerEntidad(codigoEntidad); // NO DEBERIA
															// PASARSE TIPO Y
															// PROVINCIA???
		// Ahora cargamos el proceso en la ventana
		txtCodigo.setValue(entidad.getCodigoentidad());
		txtNif.setValue(entidad.getNifentidad());
		txtNombre.setValue(entidad.getNifentidad());
		txtProvincia.setValue(entidad.getProvinciaentidad());
		txtDireccion.setValue(entidad.getDomicilioentidad());
		txtCP.setValue(entidad.getCpostalentidad());
		txtLocalidad.setValue(entidad.getLocalidadentidad());
		txtTelefono.setValue(entidad.getTelefonoentidad());
		txtEmail.setValue(entidad.getEmailentidad());
	}

	@Listen("onClick = #btnGrabarNuevo")
	public void nuevo() {
		Entidades_IDao dao = new Entidades_Dao();
		String mensaje = dao.insertarEntidades(txtCodigo.getValue(), txtNombre.getValue(), txtDireccion.getValue(),
				txtLocalidad.getValue(), txtCP.getValue(), txtProvincia.getValue(), txtTelefono.getValue(),
				txtEmail.getValue(), txtNif.getValue());
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnGrabarModificacion")
	public void modificar() {
		Entidades_IDao dao = new Entidades_Dao();
		String mensaje = dao.updateEntidades(txtCodigo.getValue(), txtNombre.getValue(), txtDireccion.getValue(),
				txtLocalidad.getValue(), txtCP.getValue(), txtProvincia.getValue(), txtTelefono.getValue(),
				txtEmail.getValue(), txtNif.getValue());
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnModificar")
	public void btnModificar() {
		pantallaModificar();
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGestionEntidades.detach();
		// Y mandamos evento a la ventana padre para que reaccione
		Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
