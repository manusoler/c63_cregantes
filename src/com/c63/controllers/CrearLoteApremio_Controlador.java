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
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class CrearLoteApremio_Controlador extends SelectorComposer<Window> {
	
	final static Logger logger = Logger.getLogger(CrearLoteApremio_Controlador.class);
	
	@Wire
	private Window winCrearLoteApremio;
	@Wire
	private Groupbox group;
	@Wire
	private Textbox txtCodLote;
	@Wire
	private Button btnCrear;
	@Wire
	private Button btnCancelar;
	private Window padre;
	private List<Recibos> listaRecibos;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
		listaRecibos = ( List<Recibos> ) execution.getArg().get("listaRecibos");
	}

	@Listen("onCreate = #winCrearLoteApremio")
	public void onCreate() {
	}

	@Listen("onClick = #btnCrear")
	public void crearLote() {
		// Comprobamos que haya seleccionado un codigo
		if(!txtCodLote.getValue().isEmpty()) {
			Recibos_IDao dao = new Recibos_Dao();
			int result = dao.crearLoteApremio(listaRecibos, txtCodLote.getValue());
			if(result > 0) {
				Messagebox.show("Se han añadido " + result + " recibos al Lote Apremio " + txtCodLote.getValue());
				cancelar();
			} else {
				Messagebox.show("ERROR. Se ha producido un error al crear el Lote Apremio " + txtCodLote.getValue());
			}
		}else {
			Messagebox.show("Debe introducir un código de lote");
		}
	}
	
	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winCrearLoteApremio.detach();
		// Y mandamos evento a la ventana padre para que reaccione
			Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
