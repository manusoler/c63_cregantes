package com.c63.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.c63.comun.ConexionBD;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class GenerarCartaPago_Controlador extends SelectorComposer<Window> {

	final static Logger logger = Logger.getLogger(GenerarCartaPago_Controlador.class);

	@Wire
	private Window winGenerarCartaPago;
	@Wire
	private Groupbox group;
	@Wire
	private Doublebox txtPrincipal;
	@Wire
	private Doublebox txtRecargo;
	@Wire
	private Doublebox txtIntereses;
	@Wire
	private Doublebox txtCostas;
	@Wire
	private Doublebox txtIngresado;
	@Wire
	private Doublebox txtTotal;
	@Wire
	private Datebox fFinPago;
	@Wire
	private Datebox fCalculoInteres;
	@Wire
	private Button btnGenerar;
	@Wire
	private Button btnCancelar;
	private Window padre;
	private Recibos recibo;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
		recibo = (Recibos) execution.getArg().get("recibo");
	}

	@Listen("onCreate = #winGenerarCartaPago")
	public void onCreate() {
		txtPrincipal.setValue(recibo.getPrincipal());
		txtRecargo.setValue(recibo.getRecargo());
		txtIntereses.setValue(recibo.getIntereses());
		txtCostas.setValue(recibo.getCostas());
		txtIngresado.setValue(recibo.getIngresosCuenta());
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 10);  // number of days to add
		fFinPago.setValue(c.getTime());
		fCalculoInteres.setValue(c.getTime());
		
		txtTotal.setValue(calcularTotal());
	}

	private double calcularTotal() {
		return txtPrincipal.getValue() + txtRecargo.getValue() + 
				txtIntereses.getValue() + txtCostas.getValue() - txtIngresado.getValue();
	}
	
	@Listen("onChange = #fCalculoInteres")
	public void cambioCalculoInteres() {
		Recibos_IDao dao = new Recibos_Dao();
		Recibos rec = dao.calcularImportes(recibo.getIdRecibo(), fCalculoInteres.getValue());
		txtPrincipal.setValue(rec.getPrincipal());
		txtRecargo.setValue(rec.getRecargo());
		txtIntereses.setValue(rec.getIntereses());
		txtCostas.setValue(rec.getCostas());
		txtIngresado.setValue(rec.getIngresosCuenta());
		txtTotal.setValue(calcularTotal());
	}
	
	@Listen("onChange = #txtPrincipal, #txtRecargo, #txtIntereses, #txtCostas, #txtIngresado")
	public void cambiarImporte() {
		txtTotal.setValue(calcularTotal());
	}
	
	@Listen("onClick = #btnGenerar")
	public void generarCartaPago() {
		// Comprobamos que haya seleccionado un codigo
		if (txtPrincipal.getValue() != null && txtRecargo.getValue() != null && 
				txtIntereses.getValue() != null && txtCostas.getValue() != null
				 && txtIngresado.getValue() != null && fFinPago.getValue() != null) {
			Map<String, Object> mapa = new HashMap<String, Object>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("P_RECIBO_ID", recibo.getIdRecibo());
			params.put("P_APREMIO", txtRecargo.getValue());
			params.put("P_INTERESES", txtIntereses.getValue());
			params.put("P_COSTAS", txtCostas.getValue());
			params.put("P_INGRESADO", txtIngresado.getValue());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			params.put("P_FECHA_FIN_PAGO", sdf.format(fFinPago.getValue()));
			mapa.put("params", params);
			mapa.put("report", "cartaPago.jasper");
			mapa.put("bbddConn", ConexionBD.conBD_recore());
			Window win = (Window) Executions.createComponents("/pages/listadoPdf.zul", null, mapa);
			win.doModal();
		} else {
			Messagebox.show("Debe introducir una fecha de notificación");
		}
	}
	
	

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGenerarCartaPago.detach();
		// Y mandamos evento a la ventana padre para que reaccione
		Events.sendEvent(new Event("onCloseChild", padre, null));
	}
}
