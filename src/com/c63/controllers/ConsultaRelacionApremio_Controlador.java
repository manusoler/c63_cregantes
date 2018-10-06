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

public class ConsultaRelacionApremio_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaRelacionApremio;
	@Wire
	private Listbox lbCodigoEntidad;
	@Wire
	private Listbox lbRecibos;
	@Wire
	private Label lblSelect;
	@Wire
	private Label lblTotal;
	@Wire
	private Button btnCrearLote;
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

		// Consultamos las entidades para sacarlos en el combo
		Entidades_IDao dao = new Entidades_Dao();
		List<Entidades> lista = dao.listarEntidades();
		// Y las cargamos en el listbox
		ListModelList<Entidades> listModel = new ListModelList<Entidades>(lista);
		lbCodigoEntidad.setModel(listModel);
	}

	@Listen("onClientInfo = #winConsultaRelacionApremio")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbRecibos.setHeight((desktopHeight - 380) + "px");
	}

	@Listen("onCreate = #winConsultaRelacionApremio")
	public void crearWindow() {
	}

	private void listarRecibos() {
		List<Recibos> lista = new ArrayList<Recibos>();
		// Realizamos la busqueda de procesos
		Recibos_IDao dao = new Recibos_Dao();
		if(lbCodigoEntidad.getSelectedCount() > 0) {
		String codigoentidad = (String) lbCodigoEntidad.getSelectedItem().getValue();
		lista = dao.listarRecibosRelacionApremio(codigoentidad);
		// Y las cargamos en el listbox
		ListModelList<Recibos> listModel = new ListModelList<Recibos>(lista);
		listModel.setMultiple(true);
		lbRecibos.setModel(listModel);
		if (lista.size() > 0) {
			lbRecibos.setPageSize(lista.size());
		}
		lblTotal.setValue(String.valueOf(lista.size()));
		selectedEntidad();
		} else {
			Messagebox.show("Debe seleccionar una entidad");
		}
	}

	@Listen("onClick = #btnBuscar")
	public void buscar() {
		listarRecibos();
	}
	
	@Listen("onOK = #winConsultaRelacionApremio")
	public void onOk() {
		listarRecibos();
	}

	@Listen("onSelect = #lbRecibos")
	public void selectedEntidad() {
		if (lbRecibos.getSelectedCount() > 0) {
			btnCrearLote.setDisabled(false);
		} else {
			btnCrearLote.setDisabled(true);
		}
		lblSelect.setValue(String.valueOf(lbRecibos.getSelectedCount()));
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaRelacionApremio.detach();
	}

	@Listen("onCloseChild = #winConsultaRelacionApremio")
	public void onGestionLote() {
		listarRecibos();
	}
	
	@Listen("onClick = #btnCrearLote")
	public void crearLote() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", winConsultaRelacionApremio);
		map.put("numRecibos", lbRecibos.getSelectedCount());
		List<Recibos> lista = new ArrayList<Recibos>();
		for(Listitem item : lbRecibos.getSelectedItems()) {
			lista.add((Recibos)item.getValue());
		}
		map.put("listaRecibos", lista) ;
		Executions.createComponents("pages/crearLoteApremio.zul", null, map);
	}
}
