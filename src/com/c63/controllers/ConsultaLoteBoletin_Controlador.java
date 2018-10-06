package com.c63.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class ConsultaLoteBoletin_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaLoteBoletin;
	@Wire
	private Textbox txtLote;
	@Wire
	private Listbox lbRecibos;
	@Wire
	private Label lblTotal;
	@Wire
	private Button btnMarcarLote;
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

	@Listen("onClientInfo = #winConsultaLoteBoletin")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbRecibos.setHeight((desktopHeight - 380) + "px");
	}

	@Listen("onCreate = #winConsultaLoteBoletin")
	public void crearWindow() {
	}

	private void listarRecibos() {
		List<Recibos> lista = new ArrayList<Recibos>();
		// Realizamos la busqueda de procesos
		Recibos_IDao dao = new Recibos_Dao();
		if(!txtLote.getValue().isEmpty()) {
			lista = dao.listarRecibosLoteBoletin(txtLote.getValue());
			// Y las cargamos en el listbox
			ListModelList<Recibos> listModel = new ListModelList<Recibos>(lista);
			listModel.setMultiple(true);
			lbRecibos.setModel(listModel);
			if (lista.size() > 0) {
				lbRecibos.setPageSize(lista.size());
			}
			btnMarcarLote.setDisabled(lista.size() <= 0);
			lblTotal.setValue(String.valueOf(lista.size()));
		} else {
			Messagebox.show("Debe establecer un código de lote");
		}
	}

	@Listen("onClick = #btnBuscar")
	public void buscar() {
		listarRecibos();
	}
	
	@Listen("onOK = #winConsultaLoteBoletin")
	public void onOk() {
		listarRecibos();
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaLoteBoletin.detach();
	}

	@Listen("onCloseChild = #winConsultaLoteBoletin")
	public void onGestionLote() {
		listarRecibos();
	}
	
	@Listen("onClick = #btnMarcarLote")
	public void marcar() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", winConsultaLoteBoletin);
		map.put("numRecibos", lbRecibos.getItemCount());
		map.put("codLote", txtLote.getValue()) ;
		Executions.createComponents("pages/marcarLoteBoletin.zul", null, map);
	}
}
