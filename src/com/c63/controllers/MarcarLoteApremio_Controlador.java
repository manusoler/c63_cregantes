package com.c63.controllers;

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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class MarcarLoteApremio_Controlador extends SelectorComposer<Window> {
	
	final static Logger logger = Logger.getLogger(MarcarLoteApremio_Controlador.class);
	
	@Wire
	private Window winMarcarLoteApremio;
	@Wire
	private Groupbox group;
	@Wire
	private Datebox fProvidencia;
	@Wire
	private Button btnMarcar;
	@Wire
	private Button btnCancelar;
	private Window padre;
	private String codLote;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
		codLote = ( String ) execution.getArg().get("codLote");
	}

	@Listen("onCreate = #winMarcarLoteApremio")
	public void onCreate() {
	}

	@Listen("onClick = #btnMarcar")
	public void marcarLote() {
		// Comprobamos que haya seleccionado un codigo
		if(fProvidencia.getValue() != null) {
			Recibos_IDao dao = new Recibos_Dao();
			int result = dao.marcarLoteApremio(codLote, fProvidencia.getValue());
			if(result > 0) {
				Messagebox.show("Se han marcado " + result + " recibos en Apremio del Lote " + codLote);
				cancelar();
			} else {
				Messagebox.show("ERROR. Se ha producido un error al marcar los recibos para el Lote Apremio " + codLote);
			}
		}else {
			Messagebox.show("Debe introducir una fecha de providencia");
		}
	}
	
	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winMarcarLoteApremio.detach();
		// Y mandamos evento a la ventana padre para que reaccione
			Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
