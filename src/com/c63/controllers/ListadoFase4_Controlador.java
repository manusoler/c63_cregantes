package com.c63.controllers;

import java.io.BufferedReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.c63.modelo.Fase4C63;

public class ListadoFase4_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = -3856467933556412779L;
	@Wire
	private Window winListadoFase4;
	@Wire
	private Button btnGenerar;
	@Wire
	private Button btnCancelar;
	@Wire
	private Label lblNombreFichero;
	@Wire
	private Listbox lbFase;
	@Wire
	private Textbox txtNumContri;
	@Wire
	private Textbox txtTotalEmbargar;
	@Wire
	private Textbox txtTotalEmbargado;
	
	private Window padre;
	private Reader reader;
	
	int desktopHeight;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
	}

	@Listen("onClientInfo = #winListadoFase4")
	public void onClientInfo(ClientInfoEvent evt) {
		desktopHeight = evt.getDesktopHeight();
		lbFase.setHeight((desktopHeight - 360) + "px");
	}
	
	@Listen("onUpload = #btnSelTxt")
	public void subirFichero(UploadEvent event) {
		org.zkoss.util.media.Media media = event.getMedia();
		if (!media.isBinary() && media.getFormat().equals("txt")) {
			reader = media.getReaderData();
			lblNombreFichero.setValue(media.getName());
		} else {
			Messagebox.show("El archivo debe ser un archivo de texto válido.");
		}
	}

	@Listen("onCreate = #winListadoFase4")
	public void crearWindow() {
	}

	@Listen("onClick = #btnGenerar")
	public void generar() {
		try {
			if (reader != null) {
				List<Fase4C63> registros = new ArrayList<Fase4C63>();
				BufferedReader br = new BufferedReader(reader);
				String currLine = null;
				double totalEmbargar = 0, totalEmbargado = 0;
				// Leemos el archivo y generamos el listado de registros de la fase 4
				while ((currLine = br.readLine()) != null) {
					// Solo leemos los registros, omitimos cabecera y fin de archivo
					if (currLine.substring(0, 1).equals("6")) {
						Fase4C63 reg = new Fase4C63();
						reg.setCifNif(StringUtils.stripEnd(currLine.substring(1, 10), " "));
						reg.setNombreRazon(StringUtils.stripEnd(currLine.substring(10, 50), " ").replaceAll("\\*", " "));
						double impEmbargar = 0, impEmbargado = 0;
						try {
							impEmbargar = Double.parseDouble(StringUtils.stripEnd(currLine.substring(119, 134), " ")) / 100;
							impEmbargado = Double.parseDouble(StringUtils.stripEnd(currLine.substring(142, 157), " ")) / 100;
						} catch(Exception e) {
							Messagebox.show("Error al leer fichero: " + e.getMessage());
						}
						totalEmbargar += impEmbargar;
						totalEmbargado += impEmbargado;
						reg.setImpEmbargar(impEmbargar);
						reg.setImpEmbargado(impEmbargado);
						registros.add(reg);
					}
				}
				br.close();
				reader.close();
				// Y las cargamos en el listbox
				ListModelList<Fase4C63> listModel = new ListModelList<Fase4C63>(registros);
				lbFase.setModel(listModel);
				if (registros.size() > 0) {
					lbFase.setPageSize(registros.size());
				}
				
				// Ahora cargamos los textbox de totales
				DecimalFormat df = new DecimalFormat("#,###.##");
				txtTotalEmbargado.setValue(df.format(totalEmbargado));
				txtTotalEmbargar.setValue(df.format(totalEmbargar));
				txtNumContri.setValue(String.valueOf(registros.size()));
			} else {
				Messagebox.show("Debe subir el archivo de la fase 4.");
			}
		} catch (Exception e) {
			Messagebox.show("Se produjo un error al importar el archivo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winListadoFase4.detach();
	}
	
}
