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
import com.c63.modelo.dao.BancosC63_Dao;
import com.c63.modelo.dao.BancosC63_IDao;

public class ConsultaBancosC63_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaBancosC63;
	@Wire
	private Listbox lbBancosC63;
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

	@Listen("onClientInfo = #winConsultaBancosC63")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbBancosC63.setHeight((desktopHeight - 380) + "px");
	}

	@Listen("onCreate = #winConsultaBancosC63")
	public void crearWindow() {
		listarBancosC63();
	}

	private void listarBancosC63() {
		List<BancosC63> lista = new ArrayList<BancosC63>();
		// Realizamos la busqueda de procesos
		BancosC63_IDao dao = new BancosC63_Dao();
		lista = dao.listarBancosC63();
		// Y las cargamos en el listbox
		ListModelList<BancosC63> listModel = new ListModelList<BancosC63>(lista);
		lbBancosC63.setModel(listModel);
		if (lista.size() > 0) {
			lbBancosC63.setPageSize(lista.size());
		}
		selectedBancosC63();
	}

	@Listen("onSelect = #lbBancosC63")
	public void selectedBancosC63() {
		if (lbBancosC63.getSelectedCount() > 0) {
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
		mapa.put("padre", winConsultaBancosC63);
		mapa.put("accion", "NUEVO");
		Window win = (Window) Executions.createComponents("/pages/gestionBancosC63.zul", null, mapa);
		win.doModal();
	}

	@Listen("onClick = #btnDetalle")
	public void detalle() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaBancosC63);
		mapa.put("accion", "DETALLE");
		mapa.put("c63banco", ((BancosC63) lbBancosC63.getSelectedItem().getValue()).getC63banco());
		mapa.put("codigoentidad", ((BancosC63) lbBancosC63.getSelectedItem().getValue()).getCodigoentidad());
		Window win = (Window) Executions.createComponents("/pages/gestionBancosC63.zul", null, mapa);
		win.doModal();
	}

	@Listen("onClick = #btnEliminar")
	public void eliminar() {
		Messagebox.show("¿Está seguro de que desea eliminar el banco?", "Eliminar Banco",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							// Lanzamos la eliminacion
							BancosC63_IDao dao = new BancosC63_Dao();
							Messagebox.show(dao.borrarBancoC63(
									((BancosC63) lbBancosC63.getSelectedItem().getValue()).getCodigoentidad(),
									((BancosC63) lbBancosC63.getSelectedItem().getValue()).getC63banco()));
							// Y se refresca la consulta
							listarBancosC63();
						}
					}
				});
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaBancosC63.detach();
	}

	@Listen("onCloseChild = #winConsultaBancosC63")
	public void onGestionLote() {
		listarBancosC63();
	}
}
