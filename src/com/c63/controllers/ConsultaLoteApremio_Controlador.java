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

import com.c63.comun.ConexionBD;
import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class ConsultaLoteApremio_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaLoteApremio;
	@Wire
	private Textbox txtLote;
	@Wire
	private Listbox lbRecibos;
	@Wire
	private Label lblTotal;
	@Wire
	private Button btnMarcarLote;
	@Wire
	private Button btnListadoLote;
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

	@Listen("onClientInfo = #winConsultaLoteApremio")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbRecibos.setHeight((desktopHeight - 340) + "px");
	}

	@Listen("onCreate = #winConsultaLoteApremio")
	public void crearWindow() {
	}

	private void listarRecibos() {
		List<Recibos> lista = new ArrayList<Recibos>();
		// Realizamos la busqueda de procesos
		Recibos_IDao dao = new Recibos_Dao();
		if(!txtLote.getValue().isEmpty()) {
			lista = dao.listarRecibosLote(txtLote.getValue());
			// Y las cargamos en el listbox
			ListModelList<Recibos> listModel = new ListModelList<Recibos>(lista);
			listModel.setMultiple(true);
			lbRecibos.setModel(listModel);
			if (lista.size() > 0) {
				lbRecibos.setPageSize(lista.size());
			}
			btnMarcarLote.setDisabled(lista.size() <= 0);
			btnListadoLote.setDisabled(lista.size() <= 0);
			lblTotal.setValue(String.valueOf(lista.size()));
		} else {
			Messagebox.show("Debe establecer un código de lote");
		}
	}

	@Listen("onClick = #btnBuscar")
	public void buscar() {
		listarRecibos();
	}
	
	@Listen("onOK = #winConsultaLoteApremio")
	public void onOk() {
		listarRecibos();
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaLoteApremio.detach();
	}

	@Listen("onCloseChild = #winConsultaLoteApremio")
	public void onGestionLote() {
		listarRecibos();
	}
	
	@Listen("onClick = #btnMarcarLote")
	public void marcar() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", winConsultaLoteApremio);
		map.put("numRecibos", lbRecibos.getItemCount());
		map.put("codLote", txtLote.getValue()) ;
		Executions.createComponents("pages/marcarLoteApremio.zul", null, map);
	}
	
	@Listen("onClick = #btnListadoLote")
	public void listado() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_LOTE_APREMIO", txtLote.getValue());
		mapa.put("params", params);
		mapa.put("report", "listadoApremio.jasper");
		mapa.put("bbddConn", ConexionBD.conBD_recore());
		Window win = (Window) Executions.createComponents("/pages/listadoPdf.zul", null, mapa);
		win.doModal();
	}
}
