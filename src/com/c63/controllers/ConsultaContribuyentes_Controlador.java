package com.c63.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.BancosC63;
import com.c63.modelo.Contribuyente;
import com.c63.modelo.dao.BancosC63_Dao;
import com.c63.modelo.dao.BancosC63_IDao;
import com.c63.modelo.dao.Contribuyente_Dao;
import com.c63.modelo.dao.Contribuyente_IDao;

public class ConsultaContribuyentes_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaContribuyentes;
	@Wire
	private Textbox txtNif;
	@Wire
	private Textbox txtNombreRazon;
	@Wire
	private Listbox lbContribuyentes;
	@Wire
	private Button btnNuevo;
	@Wire
	private Button btnNuevoRecibo;
	@Wire
	private Button btnEliminar;
	@Wire
	private Button btnDetalle;
	@Wire
	private Button btnCerrar;
	@Wire
	private Groupbox groupFiltro;
	private Window padre;
	int desktopHeight;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
	}

	@Listen("onClientInfo = #winConsultaContribuyentes")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbContribuyentes.setHeight((desktopHeight - 360) + "px");
	}

	@Listen("onCreate = #winConsultaContribuyentes")
	public void crearWindow() {
		listarContris();
	}

	private void listarContris() {
		List<Contribuyente> lista = new ArrayList<Contribuyente>();
		// Realizamos la busqueda de procesos
		Contribuyente_IDao dao = new Contribuyente_Dao();
		lista = dao.listarContribuyentes(txtNif.getValue(), txtNombreRazon.getValue());
		// Y las cargamos en el listbox
		ListModelList<Contribuyente> listModel = new ListModelList<Contribuyente>(lista);
		lbContribuyentes.setModel(listModel);
		if (lista.size() > 0) {
			lbContribuyentes.setPageSize(lista.size());
		}
		selectedEntidad();
	}

	@Listen("onClick = #btnBuscar")
	public void buscar() {
		listarContris();
	}
	
	@Listen("onOK = #winConsultaContribuyentes")
	public void onOk() {
		listarContris();
	}

	@Listen("onSelect = #lbContribuyentes")
	public void selectedEntidad() {
		if (lbContribuyentes.getSelectedCount() > 0) {
			btnDetalle.setDisabled(false);
			btnEliminar.setDisabled(false);
			btnNuevoRecibo.setDisabled(false);
		} else {
			btnDetalle.setDisabled(true);
			btnEliminar.setDisabled(true);
			btnNuevoRecibo.setDisabled(true);
		}
	}

	@Listen("onClick = #btnNuevo")
	public void nuevo() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaContribuyentes);
		mapa.put("accion", "NUEVO");
		Window win = (Window) Executions.createComponents("/pages/gestionContribuyentes.zul", null, mapa);
		win.doModal();
	}
	
	@Listen("onClick = #btnNuevoRecibo")
	public void nuevoRecibo() {
		Contribuyente contri = (Contribuyente) lbContribuyentes.getSelectedItem().getValue();
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaContribuyentes);
		mapa.put("accion", "NUEVO");
		mapa.put("idContribuyente", contri.getIdContribuyente());
		mapa.put("nifContri", contri.getCifNif());
		mapa.put("nombreContri", contri.getNombreRazon());
		Window win = (Window) Executions.createComponents("/pages/gestionRecibos.zul", null, mapa);
		win.doModal();
	}

	@Listen("onClick = #btnDetalle")
	public void detalle() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaContribuyentes);
		mapa.put("accion", "DETALLE");
		mapa.put("idContribuyente", ((Contribuyente) lbContribuyentes.getSelectedItem().getValue()).getIdContribuyente());
		Window win = (Window) Executions.createComponents("/pages/gestionContribuyentes.zul", null, mapa);
		win.doModal();
	}
	
	@Listen("onClick = #btnEliminar")
	public void eliminar() {
		Messagebox.show("¿Está seguro de que desea eliminar el contribuyente? Se eliminarán todos sus recibos asociados.", "Eliminar Contribuyente",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							// Lanzamos la eliminacion
							Contribuyente_IDao dao = new Contribuyente_Dao();
							Messagebox.show(dao.borrarContribuyente(
									((Contribuyente) lbContribuyentes.getSelectedItem().getValue()).getIdContribuyente()));
							// Y se refresca la consulta
							listarContris();;
						}
					}
				});
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaContribuyentes.detach();
	}

	@Listen("onCloseChild = #winConsultaContribuyentes")
	public void onGestionLote() {
		listarContris();
	}
	
}
