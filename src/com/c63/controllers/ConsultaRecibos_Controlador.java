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
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class ConsultaRecibos_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaRecibos;
	@Wire
	private Listbox lbCodigoEntidad;
	@Wire
	private Textbox txtNif;
	@Wire
	private Textbox txtNombreRazon;
	@Wire
	private Textbox txtAno;
	@Wire
	private Textbox txtPeriodo;
	@Wire
	private Textbox txtNumRegante;
	@Wire
	private Textbox txtConcepto;
	@Wire
	private Textbox txtCodObjeto;
	@Wire
	private Listbox lbRecibos;
	@Wire
	private Button btnNuevo;
	@Wire
	private Button btnDetalle;
	@Wire
	private Button btnCartaPago;
	@Wire
	private Button btnMarcarNotif;
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

	@Listen("onClientInfo = #winConsultaRecibos")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbRecibos.setHeight((desktopHeight - 380) + "px");
	}

	@Listen("onCreate = #winConsultaRecibos")
	public void crearWindow() {
		listarRecibos();
	}

	private void listarRecibos() {
		List<Recibos> lista = new ArrayList<Recibos>();
		// Realizamos la busqueda de procesos
		Recibos_IDao dao = new Recibos_Dao();
		String codigoentidad = lbCodigoEntidad.getSelectedCount() > 0
				? (String) lbCodigoEntidad.getSelectedItem().getValue() : "";
		lista = dao.listarRecibos(codigoentidad, txtCodObjeto.getValue(), txtAno.getValue(), 
				txtPeriodo.getValue(), txtNumRegante.getValue(), txtConcepto.getValue(), 
				txtNif.getValue(), txtNombreRazon.getValue());
		// Y las cargamos en el listbox
		ListModelList<Recibos> listModel = new ListModelList<Recibos>(lista);
		lbRecibos.setModel(listModel);
		if (lista.size() > 0) {
			lbRecibos.setPageSize(lista.size());
		}
		selectedEntidad();
	}

	@Listen("onClick = #btnBuscar")
	public void buscar() {
		listarRecibos();
	}
	
	@Listen("onOK = #winConsultaRecibos")
	public void onOk() {
		listarRecibos();
	}

	@Listen("onSelect = #lbRecibos")
	public void selectedEntidad() {
		if (lbRecibos.getSelectedCount() > 0) {
			Recibos recibo = (Recibos)lbRecibos.getSelectedItem().getValue();
			btnDetalle.setDisabled(false);
			btnCartaPago.setDisabled(!"P".equals(recibo.getEstado()));
			btnMarcarNotif.setDisabled(!"0".equals(recibo.getEstadoNotificacion()) && recibo.getEstadoNotificacion() != null);
		} else {
			btnDetalle.setDisabled(true);
			btnCartaPago.setDisabled(true);
			btnMarcarNotif.setDisabled(true);
		}
	}

	@Listen("onClick = #btnDetalle")
	public void detalle() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaRecibos);
		mapa.put("accion", "DETALLE");
		mapa.put("idRecibo", ((Recibos) lbRecibos.getSelectedItem().getValue()).getIdRecibo());
		Window win = (Window) Executions.createComponents("/pages/gestionRecibos.zul", null, mapa);
		win.doModal();
	}
	
	@Listen("onClick = #btnCartaPago")
	public void cartaPagp() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaRecibos);
		mapa.put("recibo", (Recibos) lbRecibos.getSelectedItem().getValue());
		Window win = (Window) Executions.createComponents("/pages/generarCartaPago.zul", null, mapa);
		win.doModal();
	}
	
	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaRecibos.detach();
	}

	@Listen("onCloseChild = #winConsultaRecibos")
	public void onGestionLote() {
		listarRecibos();
	}
	
	@Listen("onClick = #importarRecibos")
	public void importarRecibos() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", winConsultaRecibos);
		Executions.createComponents("pages/importarRecibos.zul", null, map);
	}
	
	@Listen("onClick = #btnMarcarNotif")
	public void marcarNotif() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", winConsultaRecibos);
		map.put("idRecibo", ((Recibos)lbRecibos.getSelectedItem().getValue()).getIdRecibo());
		Executions.createComponents("pages/marcarNotif.zul", null, map);
	}
}
