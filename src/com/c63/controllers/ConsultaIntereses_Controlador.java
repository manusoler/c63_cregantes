package com.c63.controllers;

import java.text.SimpleDateFormat;
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

import com.c63.modelo.Contribuyente;
import com.c63.modelo.Intereses;
import com.c63.modelo.dao.Contribuyente_Dao;
import com.c63.modelo.dao.Contribuyente_IDao;
import com.c63.modelo.dao.Intereses_Dao;
import com.c63.modelo.dao.Intereses_IDao;

public class ConsultaIntereses_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winConsultaIntereses;
	@Wire
	private Listbox lbIntereses;
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

	@Listen("onClientInfo = #winConsultaIntereses")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbIntereses.setHeight((desktopHeight - 380) + "px");
	}

	@Listen("onCreate = #winConsultaIntereses")
	public void crearWindow() {
		listarIntereses();
	}

	private void listarIntereses() {
		List<Intereses> lista = new ArrayList<Intereses>();
		// Realizamos la busqueda de procesos
		Intereses_IDao dao = new Intereses_Dao();
		lista = dao.listarIntereses();
		// Y las cargamos en el listbox
		ListModelList<Intereses> listModel = new ListModelList<Intereses>(lista);
		lbIntereses.setModel(listModel);
		if (lista.size() > 0) {
			lbIntereses.setPageSize(lista.size());
		}
		selectedIntereses();
	}

	@Listen("onSelect = #lbIntereses")
	public void selectedIntereses() {
		if (lbIntereses.getSelectedCount() > 0) {
			btnDetalle.setDisabled(false);
		} else {
			btnDetalle.setDisabled(true);
		}
	}

	@Listen("onClick = #btnNuevo")
	public void nuevo() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaIntereses);
		mapa.put("accion", "NUEVO");
		Window win = (Window) Executions.createComponents("/pages/gestionIntereses.zul", null, mapa);
		win.doModal();
	}

	@Listen("onClick = #btnDetalle")
	public void detalle() {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("padre", winConsultaIntereses);
		mapa.put("accion", "DETALLE");
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

		mapa.put("fecha_desde", sdf.format(((Intereses) lbIntereses.getSelectedItem().getValue()).getFecha_desde()));
		Window win = (Window) Executions.createComponents("/pages/gestionIntereses.zul", null, mapa);
		win.doModal();
	}
	
	@Listen("onClick = #btnEliminar")
	public void eliminar() {
		Messagebox.show("¿Está seguro de que desea eliminar el último intervalo de interés?", "Eliminar intervalo interés",
				Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							// Lanzamos la eliminacion
							Intereses_IDao dao = new Intereses_Dao();
							Messagebox.show(dao.borrarIntereses());
							// Y se refresca la consulta
							listarIntereses();
						}
					}
				});
	}

	@Listen("onClick = #btnCerrar")
	public void cerrar() {
		winConsultaIntereses.detach();
	}

	@Listen("onCloseChild = #winConsultaIntereses")
	public void onGestionIntereses() {
		listarIntereses();
	}
}
