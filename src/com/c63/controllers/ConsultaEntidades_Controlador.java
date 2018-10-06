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
import org.zkoss.zul.Window;

import com.c63.modelo.BancosC63;
import com.c63.modelo.Entidades;
import com.c63.modelo.dao.BancosC63_Dao;
import com.c63.modelo.dao.BancosC63_IDao;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;

public class ConsultaEntidades_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaEntidades;
	@Wire
	private Listbox lbEntidades;
	@Wire
	private Button btnNuevo;
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

	@Listen("onClientInfo = #winConsultaEntidades")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbEntidades.setHeight((desktopHeight - 380) + "px");
	}

	@Listen("onCreate = #winConsultaEntidades")
	public void crearWindow() {
		listarEntidades();
	}

	private void listarEntidades() {
		List<Entidades> lista = new ArrayList<Entidades>();
		// Realizamos la busqueda de procesos
		Entidades_IDao dao = new Entidades_Dao();
		lista = dao.listarEntidades();
		// Y las cargamos en el listbox
		ListModelList<Entidades> listModel = new ListModelList<Entidades>(lista);
		lbEntidades.setModel(listModel);
		if (lista.size() > 0) {
			lbEntidades.setPageSize(lista.size());
		}
		selectedEntidad();
	}

	@Listen("onSelect = #lbEntidades")
	public void selectedEntidad() {
		if (lbEntidades.getSelectedCount() > 0) {
			btnEliminar.setDisabled(false);
			btnDetalle.setDisabled(false);
		} else {
			btnEliminar.setDisabled(true);
			btnDetalle.setDisabled(true);
		}
	}

	@Listen("onClick = #btnNuevo")
	public void nuevo() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaEntidades);
		mapa.put("accion", "NUEVO");
		Window win = (Window) Executions.createComponents("/pages/gestionEntidades.zul", null, mapa);
		win.doModal();
	}

	@Listen("onClick = #btnDetalle")
	public void detalle() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaEntidades);
		mapa.put("accion", "DETALLE");
		mapa.put("codigoEntidad", ((Entidades) lbEntidades.getSelectedItem().getValue()).getCodigoentidad());
		Window win = (Window) Executions.createComponents("/pages/gestionEntidades.zul", null, mapa);
		win.doModal();
	}

	@Listen("onClick = #btnEliminar")
	public void eliminar() {
		Messagebox.show("¿Está seguro de que desea eliminar la entidad?", "Eliminar Entidad",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							// Lanzamos la eliminacion
							Entidades_IDao dao = new Entidades_Dao();
							Messagebox.show(dao.borrarEntidades(
									((Entidades) lbEntidades.getSelectedItem().getValue()).getCodigoentidad()));
							// Y se refresca la consulta
							listarEntidades();
							;
						}
					}
				});
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaEntidades.detach();
	}

	@Listen("onCloseChild = #winConsultaEntidades")
	public void onGestionLote() {
		listarEntidades();
	}
}
