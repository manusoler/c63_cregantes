package com.c63.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.comun.FuncionesComunes;
import com.c63.modelo.Contribuyente;
import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Contribuyente_Dao;
import com.c63.modelo.dao.Contribuyente_IDao;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class GestionContribuyentes_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window winGestionContribuyentes;
	@Wire
	private Groupbox group;
	@Wire
	private Textbox txtId;
	@Wire
	private Textbox txtCifNif;
	@Wire
	private Textbox txtNombreRazon;
	@Wire
	private Textbox txtPoblacion;
	@Wire
	private Textbox txtProvincia;
	@Wire
	private Textbox txtDireccion;
	@Wire
	private Textbox txtCP;
	@Wire
	private Textbox txtEmail;
	@Wire
	private Textbox txtTelefono;
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
	private int idContribuyente;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		idContribuyente = execution.getArg().get("idContribuyente") != null
				? (Integer) execution.getArg().get("idContribuyente") : 0;
		padre = (Window) execution.getArg().get("padre");
		accion = (String) execution.getArg().get("accion");
	}

	@Listen("onCreate = #winGestionContribuyentes")
	public void onCreate() {
		if ("NUEVO".equals(accion)) {
			group.setTitle("Nuevo Contribuyente");
			pantallaNuevo();
		} else { // DETALLE
			group.setTitle("Detalle de Contribuyente");
			pantallaDetalle();
		}
	}

	public void pantallaDetalle() {
		// Consultamos el proceso
		consultar();
		// Y establecemos el estado de los campos
		txtId.setDisabled(true);
		txtNombreRazon.setDisabled(true);
		txtCifNif.setDisabled(true);
		txtPoblacion.setDisabled(true);
		txtProvincia.setDisabled(true);
		txtDireccion.setDisabled(true);
		txtCP.setDisabled(true);
		txtTelefono.setDisabled(true);
		txtEmail.setDisabled(true);

		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(true);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaModificar() {
		group.setTitle("Modificar Entidad");

		txtId.setDisabled(true);
		txtNombreRazon.setDisabled(false);
		txtCifNif.setDisabled(false);
		txtPoblacion.setDisabled(false);
		txtProvincia.setDisabled(false);
		txtDireccion.setDisabled(false);
		txtCP.setDisabled(false);
		txtTelefono.setDisabled(false);
		txtEmail.setDisabled(false);

		btnGrabarModificacion.setVisible(true);
		btnModificar.setVisible(false);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaNuevo() {
		// Establecemos los estados de los campos y botones
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(false);

		txtId.setDisabled(true);
	}

	private void consultar() {
		Contribuyente_IDao dao = new Contribuyente_Dao();
		Contribuyente recibo = dao.leerContribuyente(idContribuyente);
		// Ahora cargamos el proceso en la ventana
		txtId.setValue(String.valueOf(recibo.getIdContribuyente()));
		txtNombreRazon.setValue(recibo.getNombreRazon());
		txtCifNif.setValue(recibo.getCifNif());
		txtPoblacion.setValue(recibo.getPoblacion());
		txtProvincia.setValue(recibo.getProvincia());
		txtDireccion.setValue(recibo.getDomicilio());
		txtCP.setValue(recibo.getCp());
		txtTelefono.setValue(recibo.getTelefono());
		txtEmail.setValue(recibo.getEmail());
	}

	@Listen("onClick = #btnGrabarNuevo")
	public void nuevo() throws Exception {
		Contribuyente_IDao dao = new Contribuyente_Dao();
		int id = dao.insertarContribuyente(new Contribuyente(0, txtCifNif.getValue(), txtNombreRazon.getValue(),
				txtDireccion.getValue(), txtPoblacion.getValue(), txtCP.getValue(), txtProvincia.getValue(),
				txtTelefono.getValue(), txtEmail.getValue()));
		cancelar();
		Messagebox.show("Se ha insertado el contribuyente " + id + " correctamente.");
	}

	@Listen("onClick = #btnGrabarModificacion")
	public void modificar() {
		Contribuyente_IDao dao = new Contribuyente_Dao();
		String mensaje = dao.updateContribuyente(new Contribuyente(Integer.valueOf(txtId.getValue()),
				txtCifNif.getValue(), txtNombreRazon.getValue(), txtDireccion.getValue(), txtPoblacion.getValue(),
				txtCP.getValue(), txtProvincia.getValue(), txtTelefono.getValue(), txtEmail.getValue()));
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnModificar")
	public void btnModificar() {
		pantallaModificar();
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGestionContribuyentes.detach();
		// Y mandamos evento a la ventana padre para que reaccione
		Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
