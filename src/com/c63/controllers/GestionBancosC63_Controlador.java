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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.BancosC63;
import com.c63.modelo.Entidades;
import com.c63.modelo.dao.BancosC63_Dao;
import com.c63.modelo.dao.BancosC63_IDao;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;

public class GestionBancosC63_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window winGestionBancosC63;
	@Wire
	private Groupbox groupGestBancoC63;
	@Wire
	private Textbox txtBanco;
	@Wire
	private Textbox txtSucursal;
	@Wire
	private Textbox txtNombre;
	@Wire
	private Textbox txtCodINE;
	@Wire
	private Textbox txtIBAN;
	@Wire
	private Combobox lbEntidad;
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
	private String codigoentidad;
	private String c63banco;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		c63banco = (String) execution.getArg().get("c63banco");
		codigoentidad = (String) execution.getArg().get("codigoentidad");
		padre = (Window) execution.getArg().get("padre");
		accion = (String) execution.getArg().get("accion");
		if ("NUEVO".equals(accion)) {
			groupGestBancoC63.setTitle("Nuevo Banco");
			pantallaNuevo();
		} else { // DETALLE
			groupGestBancoC63.setTitle("Detalle de Banco");
			pantallaDetalle();
		}
	}

	public void pantallaDetalle() {
		// Consultamos el proceso
		consultar();
		// Y establecemos el estado de los campos
		lbEntidad.setDisabled(true);
		txtBanco.setDisabled(true);
		txtSucursal.setReadonly(true);
		txtNombre.setReadonly(true);
		txtCodINE.setReadonly(true);
		txtIBAN.setReadonly(true);
		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(true);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaModificar() {
		groupGestBancoC63.setTitle("Modificar BAnco");
		lbEntidad.setDisabled(true);
		txtBanco.setDisabled(true);
		txtSucursal.setReadonly(false);
		txtNombre.setReadonly(false);
		txtCodINE.setReadonly(false);
		txtIBAN.setReadonly(false);
		btnGrabarModificacion.setVisible(true);
		btnModificar.setVisible(false);
		btnGrabarNuevo.setVisible(false);
	}

	public void pantallaNuevo() {
		// Establecemos los estados de los campos y botones
		List<Entidades> lista = new ArrayList<Entidades>();
		// Realizamos la busqueda de procesos
		Entidades_IDao dao = new Entidades_Dao();
		lista = dao.listarEntidades();
		// Y las cargamos en el listbox
		ListModelList<Entidades> listModel = new ListModelList<Entidades>(lista);
		lbEntidad.setModel(listModel);

		btnGrabarModificacion.setVisible(false);
		btnModificar.setVisible(false);
	}

	private void consultar() {
		BancosC63_IDao dao = new BancosC63_Dao();
		BancosC63 banco = dao.leerBancoC63(codigoentidad, c63banco); //
		// Ahora cargamos el proceso en la ventana
		txtBanco.setValue(banco.getC63banco());
		txtSucursal.setValue(banco.getC63sucursal());
		txtNombre.setValue(banco.getC63nombre());
		txtCodINE.setValue(banco.getC63codine());
		txtIBAN.setValue(banco.getC63iban());
		lbEntidad.setValue(banco.getCodigoentidad());

	}

	@Listen("onClick = #btnGrabarNuevo")
	public void nuevo() {
		Entidades selected = lbEntidad.getSelectedItem().getValue();

		BancosC63_IDao dao = new BancosC63_Dao();
		String mensaje = dao.insertarBancoC63(selected.getCodigoentidad(), txtBanco.getValue(), txtSucursal.getValue(),
				txtNombre.getValue(), txtCodINE.getValue(), txtIBAN.getValue());
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnGrabarModificacion")
	public void modificar() {
		BancosC63_IDao dao = new BancosC63_Dao();
		String mensaje = dao.updateBancosC63(lbEntidad.getValue(), txtBanco.getValue(), txtSucursal.getValue(),
				txtNombre.getValue(), txtCodINE.getValue(), txtIBAN.getValue());
		cancelar();
		Messagebox.show(mensaje);
	}

	@Listen("onClick = #btnModificar")
	public void btnModificar() {
		pantallaModificar();
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGestionBancosC63.detach();
		// Y mandamos evento a la ventana padre para que reaccione
		Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
