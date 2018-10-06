package com.c63.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.c63.comun.FuncionesComunes;
import com.c63.modelo.Contribuyente;
import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

public class ImportarRecibos_Controlador extends SelectorComposer<Window> {
	
	final static Logger logger = Logger.getLogger(ImportarRecibos_Controlador.class);
	
	@Wire
	private Window winImportarRecibos;
	@Wire
	private Groupbox group;
	@Wire
	private Listbox lbCodigoEntidad;
	@Wire
	private Listbox lbFase;
	@Wire
	private Button btnImportar;
	@Wire
	private Button btnCancelar;
	@Wire
	private Button btnSelExcel;
	@Wire
	private Label lblNombreFichero;
	private Window padre;
	private AMedia tempMedia;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);
		final Execution execution = Executions.getCurrent();
		padre = (Window) execution.getArg().get("padre");
		tempMedia = null;
		// Consultamos las entidades para sacarlos en el combo
		Entidades_IDao dao = new Entidades_Dao();
		List<Entidades> lista = dao.listarEntidades();
		// Y las cargamos en el listbox
		ListModelList<Entidades> listModel = new ListModelList<Entidades>(lista);
		lbCodigoEntidad.setModel(listModel);
	}

	@Listen("onCreate = #winImportarRecibos")
	public void onCreate() {
	}

	@Listen("onUpload = #btnSelExcel")
	public void subirFichero(UploadEvent event) {
		org.zkoss.util.media.Media media = event.getMedia();
		if(media.isBinary() && media.getFormat().equals("xls")) {
			tempMedia = new AMedia(media.getName(), media.getFormat(), media.getContentType(), media.getStreamData());
			lblNombreFichero.setValue(tempMedia.getName());
		} else {
			Messagebox.show("El archivo debe ser un archivo excel válido (versión 2007 o anterior).");
		}
	}

	@Listen("onClick = #btnImportar")
	public void excel() {
		try {
			boolean salir = false;
			// Comprobamos que haya seleccionado una entidad
			if(lbCodigoEntidad.getSelectedCount() > 0 && lbFase.getSelectedCount() > 0) {
				// Abrimos el archivo excel cargado
				Workbook workbook = Workbook.getWorkbook(tempMedia.getStreamData());
				Sheet sheet = workbook.getSheet(0);
				Recibos_IDao dao = new Recibos_Dao();
				
				List<Recibos> recibos = new ArrayList<Recibos>();
				// recorremos todas las filas (la primera es la cabecera)
				for(int i = 4; i < sheet.getRows(); i++) {
					// Y cargamos todos los datos
					String cifNif = sheet.getCell(0,i).getContents().replaceAll("\\.", "").replaceAll("-", "").replaceAll(",", "").trim(); 
					String nombreRazon = FuncionesComunes.truncar(sheet.getCell(1,i).getContents(), 100);
					String domicilio = FuncionesComunes.truncar(sheet.getCell(2,i).getContents(), 100);
					String poblacion = FuncionesComunes.truncar(sheet.getCell(3,i).getContents(), 50);
					String cp = sheet.getCell(4,i).getContents();
					String provincia = sheet.getCell(5,i).getContents();
					String numRegante = sheet.getCell(6,i).getContents().replaceAll("\\.", "").replaceAll("-", "").replaceAll(",", "");
					String domicilioObjeto = sheet.getCell(7,i).getContents();
					String concepto = sheet.getCell(8,i).getContents();
					String ano = sheet.getCell(9,i).getContents();
					String periodo = sheet.getCell(10,i).getContents();
					String textoPeriodo = sheet.getCell(11,i).getContents();
					Date fAsamblea = !sheet.getCell(12,i).getContents().isEmpty() ? ((DateCell)sheet.getCell(12,i)).getDate() : null;
					String principal = sheet.getCell(13,i).getContents();
					String recargo = sheet.getCell(14,i).getContents();
					String codObjeto = sheet.getCell(15,i).getContents();
					String ingresos = sheet.getCell(16,i).getContents();
					Date fProvidencia = !sheet.getCell(17,i).getContents().isEmpty() ? ((DateCell)sheet.getCell(17,i)).getDate() : null;
					Date fEjecutiva = !sheet.getCell(18,i).getContents().isEmpty() ? ((DateCell)sheet.getCell(18,i)).getDate() : null;
					
					if(StringUtils.isEmpty(cifNif) || StringUtils.isEmpty(nombreRazon) || StringUtils.isEmpty(codObjeto)
							|| StringUtils.isEmpty(concepto) || StringUtils.isEmpty(ano) || StringUtils.isEmpty(periodo)
							|| StringUtils.isEmpty(principal)) {
						salir = true;
						Messagebox.show("Error al importar el archivo.\n "
								+ "Existen registros que no poseen CIF, NOMBRE o RAZON, COD. OBJETO, CONCEPTO, AÑO, PERIODO y/o PRINCIPAL.\n"
								+ "Por favor, revise el documento e importelo de nuevo.");
						break;
					}
					
					Contribuyente contri = new Contribuyente();
					contri.setCifNif(cifNif);
					contri.setNombreRazon(nombreRazon);
					contri.setDomicilio(domicilio);
					contri.setPoblacion(poblacion);
					contri.setCp(cp);
					contri.setProvincia(provincia);
					
					Entidades entidad = new Entidades();
					entidad.setCodigoentidad((String)lbCodigoEntidad.getSelectedItem().getValue());
					
					Recibos recibo = new Recibos();
					recibo.setContribuyente(contri);
					recibo.setEntidad(entidad);
					recibo.setTextoPeriodo(textoPeriodo);
					recibo.setNumRegante(numRegante);
					
					recibo.setEstado("P");
					recibo.setFase((String) lbFase.getSelectedItem().getValue());
					try {
						recibo.setCodObjeto(codObjeto);
						recibo.setDomicilioObjeto(domicilioObjeto);
						recibo.setConcepto(concepto);
						recibo.setAno(Integer.parseInt(ano));
						recibo.setPeriodo(Integer.parseInt(periodo));
					} catch (NumberFormatException e) {
						salir = true;
						Messagebox.show("Error al importar el archivo.\n "
								+ "Existen registros que poseen AÑO y/o PERIODO mal formado.\n"
								+ "Por favor, revise el documento e importelo de nuevo.");
						break;
					}
					
					try {
						recibo.setfAsamblea(new java.sql.Date(fAsamblea.getTime()));
					} catch (Exception e) {
						recibo.setfAsamblea(null);
					}
					try {
						recibo.setPrincipal(Double.parseDouble(limpiarImporte(principal)));
					} catch (NumberFormatException e) {
						recibo.setPrincipal(0);
					}
					try {
						recibo.setPrincipal(Double.parseDouble(limpiarImporte(principal)));
					} catch (NumberFormatException e) {
						Messagebox.show("Error al importar el archivo.\n "
								+ "Existen registros que poseen PRINCIPAL mal formado.\n"
								+ "Por favor, revise el documento e importelo de nuevo.");
						break;
					}
					try {
						recibo.setRecargo(Double.parseDouble(limpiarImporte(recargo)));
					} catch (NumberFormatException e) {
						recibo.setRecargo(0);
					}
					try {
						recibo.setIngresosCuenta(Double.parseDouble(limpiarImporte(ingresos)));
					} catch (NumberFormatException e) {
						recibo.setIngresosCuenta(0);
					}
					try {
						recibo.setfProvidencia(new java.sql.Date(fProvidencia.getTime()));
					} catch (Exception e) {
						recibo.setfProvidencia(null);
					}
					
					try {
						recibo.setfEjecutiva(new java.sql.Date(fEjecutiva.getTime()));
					} catch (Exception e) {
						recibo.setfEjecutiva(null);
					}
					recibos.add(recibo);
				}
				
				if(!salir) {
					int[] recuento = dao.insertarRecibos(recibos);
					logger.info("Importación realizada:\n - Total de registros en el archivo: "+(recibos.size())+".\n"
							+ "- Se han insertado " + recuento[0] +" recibos.\n"
							+ "- Se han insertado " + recuento[1] +" contribuyentes.");
					Messagebox.show("Importación realizada:\n - Total de registros en el archivo: "+(recibos.size())+".\n"
							+ "- Se han insertado " + recuento[0] + " recibos.\n"
							+ "- Se han insertado " + recuento[1] +" contribuyentes.");
					Events.sendEvent(padre, new Event("onCloseChild"));
					cancelar();
				}
			}else {
				Messagebox.show("Debe seleccionar una entidad y una fase");
			}
		} catch (Exception e) {
			Messagebox.show("Se produjo un error al importar el archivo: " + e.getMessage());
			logger.error("Se produjo un error al importar el archivo: " + e);
		}
	}
	
	private String limpiarImporte(String importe) {
		return importe.replaceAll(",", ".").replaceAll("€", "").replaceAll("\"", "").trim();
	}
	
	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winImportarRecibos.detach();
	}
}
