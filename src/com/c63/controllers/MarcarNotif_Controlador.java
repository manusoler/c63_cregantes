package com.c63.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class MarcarNotif_Controlador extends SelectorComposer<Window> {
	
	final static Logger logger = Logger.getLogger(MarcarNotif_Controlador.class);
	
	@Wire
	private Window winMarcarNotif;
	@Wire
	private Groupbox group;
	@Wire
	private Listbox lbEstado;
	@Wire
	private Datebox fNotif;
	@Wire
	private Textbox txtCodLote;
	@Wire
	private Row lote;
	@Wire
	private Button btnMarcar;
	@Wire
	private Button btnCancelar;
	private Window padre;
	private int idRecibo;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
		idRecibo = ( Integer ) execution.getArg().get("idRecibo");
	}

	@Listen("onCreate = #winMarcarNotif")
	public void onCreate() {
	}
	
	@Listen("onSelect = #lbEstado")
	public void cambiarEstado() {
		lote.setVisible(lbEstado.getSelectedCount() > 0 && "2".equals((String)lbEstado.getSelectedItem().getValue()));
	}

	@Listen("onClick = #btnMarcar")
	public void marcarNotif() {
		// Comprobamos que haya seleccionado un codigo
		if(((String)lbEstado.getSelectedItem().getValue()).isEmpty() || fNotif.getValue() == null) {
			Messagebox.show("Debe seleccionar un estado y establecer una fecha de notificación.");
		} else if("2".equals((String)lbEstado.getSelectedItem().getValue()) && txtCodLote.getValue().isEmpty()) {
			Messagebox.show("Debe establecer un código de lote de boletín.");
		} else {
			Recibos_IDao dao = new Recibos_Dao();
			int result = dao.marcarNotifApremio(idRecibo, (String)lbEstado.getSelectedItem().getValue(), fNotif.getValue(), txtCodLote.getValue());
			if(result > 0) {
				Messagebox.show("Se han marcado el recibo " + idRecibo + " correctamente.");
				cancelar();
			} else {
				Messagebox.show("ERROR. Se ha producido un error al marcar el recibo " + idRecibo);
			}
		}
	}
	
	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winMarcarNotif.detach();
		// Y mandamos evento a la ventana padre para que reaccione
			Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
