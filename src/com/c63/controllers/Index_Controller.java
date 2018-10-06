package com.c63.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.Selectable;

public class Index_Controller extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Window windows_index;
	@Wire
	private Label equipoNet;
	@Wire
	private Label fecha;

	@Listen("onCreate = #windows_index")
	public void inicio() {
		Date fechaDia = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			equipoNet.setValue("HostName: " + localHost.getHostName() + " - " + localHost.getHostAddress());
			fecha.setValue(sdf.format(fechaDia));

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Listen("onClick = #entidades")
	public void entidades() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaEntidades.zul", null, map);
	}

	@Listen("onClick = #bancosC63")
	public void bancosC63() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaBancosc63.zul", null, map);
	}
	
	@Listen("onClick = #intereses")
	public void intereses() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaIntereses.zul", null, map);
	}
	
	@Listen("onClick = #contribuyentes")
	public void contribuyentes() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaContribuyentes.zul", null, map);
	}
	
	@Listen("onClick = #recibos")
	public void recibos() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaRecibos.zul", null, map);
	}
	
	@Listen("onClick = #generarFichero")
	public void generarFichero() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/generarFaseUno.zul", null, map);
	}
	
	@Listen("onClick = #generarFicheroFaseTres")
	public void generarFicheroFaseTres() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/generarFaseTres.zul", null, map);
	}
	
	@Listen("onClick = #listadoFaseCuatro")
	public void listadoFaseCuatro() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/listadoFase4.zul", null, map);
	}
	
	@Listen("onClick = #miRelacionApremio")
	public void relacionApremio() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaRelacionApremio.zul", null, map);
	}
	
	@Listen("onClick = #miMarcarApremio")
	public void marcarApremio() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaLoteApremio.zul", null, map);
	}
	
	@Listen("onClick = #miNotifApremio")
	public void notifApremio() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaNotifApremio.zul", null, map);
	}
	
	@Listen("onClick = #miMarcarNotifApremio")
	public void marcarNotifApremio() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaLoteNotif.zul", null, map);
	}
	
	@Listen("onClick = #miMarcarPublBoletin")
	public void marcarPublBoletin() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("padre", windows_index);
		Executions.createComponents("pages/consultaLoteBoletin.zul", null, map);
	}
	
}
