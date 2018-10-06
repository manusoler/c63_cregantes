package com.c63.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.c63.comun.FuncionesComunes;
import com.c63.modelo.BancosC63;
import com.c63.modelo.Entidades;
import com.c63.modelo.Fase2C63;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.BancosC63_Dao;
import com.c63.modelo.dao.BancosC63_IDao;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class GenerarFaseTres_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window winGenerarFaseTres;
	@Wire
	private Listbox lbCodigoEntidad;
	@Wire
	private Listbox lbBanco;
	@Wire
	private Button btnGenerar;
	@Wire
	private Button btnCancelar;
	@Wire
	private Label lblNombreFichero;
	private Window padre;
	private Reader reader;

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

	@Listen("onCreate = #winGenerarFaseTres")
	public void onCreate() {
		lbBanco.setDisabled(true);
	}

	@Listen("onSelect = #lbCodigoEntidad")
	public void onSelectCodigoEntidad() {
		if (lbCodigoEntidad.getSelectedCount() > 0) {
			// Consultamos los bancos para sacarlos en el combo
			BancosC63_IDao daox = new BancosC63_Dao();
			List<BancosC63> lista2 = daox.listarBancosC63Entidad((String) lbCodigoEntidad.getSelectedItem().getValue());
			if (lista2.size() > 0) {
				// Y las cargamos en el listbox
				ListModelList<BancosC63> listModel2 = new ListModelList<BancosC63>(lista2);
				lbBanco.setModel(listModel2);
				lbBanco.setDisabled(false);
			} else {
				Messagebox.show("La entidad seleccionada no posee ningún banco asociado");
				lbBanco.setDisabled(true);
			}
		} else {
			lbBanco.setDisabled(true);
		}
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

	@Listen("onClick = #btnGenerar")
	public void generar() {
		try {
			// Comprobamos que haya seleccionado una entidad y un banco y un
			// archivo subido
			if (lbCodigoEntidad.getSelectedCount() > 0 && lbBanco.getSelectedCount() > 0 && reader != null) {
				Recibos_IDao reciDao = new Recibos_Dao();
				List<Fase2C63> registros = new ArrayList<Fase2C63>();
				String codEntidad = (String) lbCodigoEntidad.getSelectedItem().getValue();
				String codBanco = (String) lbBanco.getSelectedItem().getValue();
				BufferedReader br = new BufferedReader(reader);
				String currLine = null;
				// en primer lugar, leemos el archivo y generamos el listado de
				// registros de la fase 2
				while ((currLine = br.readLine()) != null) {
					// Solo leemos los registros, omitimos cabecera y fin de
					// archivo
					if (currLine.substring(0, 1).equals("6")) {
						Fase2C63 reg = new Fase2C63();
						reg.setCodigoRegistro(StringUtils.stripEnd(currLine.substring(0, 1), " "));
						reg.setNifDeudor(StringUtils.stripEnd(currLine.substring(1, 10), " "));
						reg.setNombreDeudor(StringUtils.stripEnd(currLine.substring(10, 50), " "));
						reg.setDomicilioDeudor(StringUtils.stripEnd(currLine.substring(50, 89), " ").replaceAll("[^A-Za-z0-9* ()\\[\\]]", ""));
						reg.setMunicipio(StringUtils.stripEnd(currLine.substring(89, 101), " "));
						reg.setCodPostal(StringUtils.stripEnd(currLine.substring(101, 106), " "));
						reg.setIdDeuda(StringUtils.stripEnd(currLine.substring(106, 119), " "));
						reg.setCodDeuda(StringUtils.stripEnd(currLine.substring(134, 142), " "));
						reg.setIban1(StringUtils.stripEnd(currLine.substring(174, 198), " "));
						reg.setIban2(StringUtils.stripEnd(currLine.substring(215, 239), " "));
						reg.setIban3(StringUtils.stripEnd(currLine.substring(256, 280), " "));
						reg.setIban4(StringUtils.stripEnd(currLine.substring(297, 321), " "));
						reg.setIban5(StringUtils.stripEnd(currLine.substring(338, 362), " "));
						reg.setIban6(StringUtils.stripEnd(currLine.substring(379, 403), " "));
						reg.setClaveSeguridad1(
								currLine.length() > 427 ? StringUtils.stripEnd(currLine.substring(426, 438), " ") : "");
						reg.setClaveSeguridad2(
								currLine.length() > 439 ? StringUtils.stripEnd(currLine.substring(438, 450), " ") : "");
						reg.setClaveSeguridad3(
								currLine.length() > 451 ? StringUtils.stripEnd(currLine.substring(450, 462), " ") : "");
						reg.setClaveSeguridad4(
								currLine.length() > 463 ? StringUtils.stripEnd(currLine.substring(462, 474), " ") : "");
						reg.setClaveSeguridad5(
								currLine.length() > 475 ? StringUtils.stripEnd(currLine.substring(474, 486), " ") : "");
						reg.setClaveSeguridad6(
								currLine.length() > 487 ? StringUtils.stripEnd(currLine.substring(486, 498), " ") : "");
						
						// Si la deuda no esta insertada, tiene un iban al menos y esta en expediente, la metemos
						Recibos reci = reciDao.leerRecibo(Integer.parseInt(reg.getIdDeuda()));
						if(!existeDeuda(registros, reg.getCodDeuda()) && !reg.getIban1().isEmpty() && reci.getFase().equals("X")) {
							registros.add(reg);
						}
					}
				}
				// Consultamos los datos del banco y entidad seleccionadas
				BancosC63_IDao dao = new BancosC63_Dao();
				BancosC63 banco = dao.leerBancoC63(codEntidad, codBanco);
				Entidades_IDao daox = new Entidades_Dao();
				Entidades entidad = daox.leerEntidad(codEntidad);
				
				// Ahora leemos los registros de la fase 2, y creamos el archivo
				// de la fase 3
				File archivo = File.createTempFile("fase_tres", ".txt");
				PrintWriter writer = new PrintWriter(archivo);

				/*******************************************************
				 * CABECERA
				 *******************************************************/
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				StringBuffer cabecera = new StringBuffer();
				// ZONA A - 4
				cabecera = cabecera.append("4");
				// ZONA B - Libre
				cabecera = cabecera.append(StringUtils.leftPad("", 2));
				// ZONA C - CODIGO NRBE Entidad
				cabecera = cabecera.append(FuncionesComunes.truncarIzda(codBanco, 4, '0'));
				// ZONA D - LIBRE
				cabecera = cabecera.append(StringUtils.leftPad("", 8));
				// ZONA E - LIBRE
				cabecera = cabecera.append(StringUtils.leftPad("", 8));
				// ZONA F
				// ZONA F1 - ID FASE = 3
				cabecera = cabecera.append('3');
				// ZONA F2 - FECHA ACTUAL
				cabecera = cabecera.append(sdf.format(new Date()));
				// ZONA F3 - 00000000
				cabecera = cabecera.append(StringUtils.leftPad("", 8, '0'));
				// ZONA F4 - LIBRE
				cabecera = cabecera.append(StringUtils.leftPad("", 14));
				// ZONA F5 - LIBRE
				cabecera = cabecera.append(StringUtils.leftPad("", 2));
				// ZONA G
				// ZONA G1 - NIF ENTIDAD
				cabecera = cabecera.append(FuncionesComunes.truncarIzda(entidad.getNifentidad(), 9, ' '));
				// ZONA G2 - INE
				cabecera = cabecera.append(FuncionesComunes.truncarIzda(banco.getC63codine(), 6, ' '));
				// ZONA G3 - Nombre
				cabecera = cabecera.append(FuncionesComunes.truncarDcha(entidad.getNombreentidad(), 40, ' '));
				// ZONA H
				// ZONA H1 - Version Cuaderno = 63033
				cabecera = cabecera.append("63033");
				// ZONA H2 - Libre
				cabecera = cabecera.append(StringUtils.leftPad("", 534));

				writer.println(cabecera.toString());

				/*******************************************************
				 * REGISTRO DETALLE
				 *******************************************************/
				DecimalFormat df = new DecimalFormat("0.00");
				double totalEmbargar = 0;
				int totalReg = 0;
				// Para cada recibo, generamos su contenido
				for (Fase2C63 reg : registros) {
					double importe = reciDao.obtenerTotalDeudaContri(reg.getNifDeudor(), codEntidad);
					if(importe > 0) {
						totalEmbargar += importe;
						StringBuffer detalle = new StringBuffer();
						// ZONA A - COD REGISTRO = 6
						detalle = detalle.append("6");
						// ZONA B - ID DEUDOR
						// ZONA B1 - NIF
						detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getNifDeudor(), 9, ' '));
						// ZONA B2 - NOMBRE / RAZON SOCIAL
						if (FuncionesComunes.esPersonaFisica(reg.getNifDeudor())) {
							// Para personas fisicas los apellidos y nombre van
							// separados por *
							String[] nombre = reg.getNombreDeudor().split(" ");
							String nombreCompleto = "";
							for (int i = 0; i < nombre.length; i++) {
								nombreCompleto = nombreCompleto.concat(nombre[i]);
								if (i < 2) {
									nombreCompleto = nombreCompleto.concat("*");
								} else {
									nombreCompleto = nombreCompleto.concat(" ");
								}
							}
							detalle = detalle.append(FuncionesComunes.truncarDcha(nombreCompleto, 40, ' '));
						} else {
							detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getNombreDeudor(), 40, ' '));
						}
						// ZONA B3 - DOMICILIO
						detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getDomicilioDeudor(), 39, ' '));
						// ZONA B4 - MUNICIPIO
						detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getMunicipio(), 12, ' '));
						// ZONA B5 - COD POSTAL
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getCodPostal(), 5, '0'));
						// ZONA C - IDENTIFICADOR DE LA DEUDA
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIdDeuda(), 13, '0'));
						// ZONA D - IMPORTE EMBARGAR
						detalle = detalle.append(FuncionesComunes.truncarIzda(df.format(importe).replace(",", ""), 15, '0'));
						// ZONA E - CODIGO DEUDA / DEUDOR
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getCodDeuda(), 8, '0'));
						// ZONA F - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA G - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 1));
						// ZONA H - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 8));
						// ZONA I - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 8));
						
						// ZONA J - IBAN
						// ZONA J1 - IBAN1
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIban1(), 24, "3058".equals(codBanco) ? '0' : ' '));
						// ZONA J2 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 2));
						// ZONA J3 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA J4 - IBAN2
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIban2(), 24, "3058".equals(codBanco) ? '0' : ' '));
						// ZONA J5 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 2));
						// ZONA J6 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA J7 - IBAN3
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIban3(), 24, "3058".equals(codBanco) ? '0' : ' '));
						// ZONA J8 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 2));
						// ZONA J9 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA J10 - IBAN4
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIban4(), 24, "3058".equals(codBanco) ? '0' : ' '));
						// ZONA J11 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 2));
						// ZONA J12 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA J13 - IBAN5
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIban5(), 24, "3058".equals(codBanco) ? '0' : ' '));
						// ZONA J14 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 2));
						// ZONA J15 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA J16 - IBAN6
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIban6(), 24, "3058".equals(codBanco) ? '0' : ' '));
						// ZONA J17 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 2));
						// ZONA J18 - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 15));
						// ZONA K - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 6));
						// ZONA L - CLAVES SEGURIDAD
						// ZONA L1 - CLAVES SEGURIDAD IBAN1
						detalle = detalle.append(StringUtils.leftPad(reg.getClaveSeguridad1(), 12));
						// ZONA L2 - CLAVES SEGURIDAD IBAN2
						detalle = detalle.append(StringUtils.leftPad(reg.getClaveSeguridad2(), 12));
						// ZONA L3 - CLAVES SEGURIDAD IBAN3
						detalle = detalle.append(StringUtils.leftPad(reg.getClaveSeguridad3(), 12));
						// ZONA L4 - CLAVES SEGURIDAD IBAN4
						detalle = detalle.append(StringUtils.leftPad(reg.getClaveSeguridad4(), 12));
						// ZONA L5 - CLAVES SEGURIDAD IBAN5
						detalle = detalle.append(StringUtils.leftPad(reg.getClaveSeguridad5(), 12));
						// ZONA L6 - CLAVES SEGURIDAD IBAN6
						detalle = detalle.append(StringUtils.leftPad(reg.getClaveSeguridad6(), 12));
						// ZONA M - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 152));
						writer.println(detalle.toString());
	
						/*******
						 * REGISTRO COMPLEMENTARIO DE DETALLE
						 ******/
						/*detalle = new StringBuffer();
						// ZONA A - COD REGISTRO = 7
						detalle = detalle.append("7");
						// ZONA B - ID DEUDOR
						// ZONA B1 - NIF
						detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getNifDeudor(), 9, ' '));
						// ZONA B2 - NOMBRE / RAZON SOCIAL
						if (FuncionesComunes.esPersonaFisica(reg.getNifDeudor())) {
							// Para personas fisicas los apellidos y nombre van
							// separados por *
							String[] nombre = reg.getNombreDeudor().split(" ");
							String nombreCompleto = "";
							for (int i = 0; i < nombre.length; i++) {
								nombreCompleto = nombreCompleto.concat(nombre[i]);
								if (i < 2) {
									nombreCompleto = nombreCompleto.concat("*");
								} else {
									nombreCompleto = nombreCompleto.concat(" ");
								}
							}
							detalle = detalle.append(FuncionesComunes.truncarDcha(nombreCompleto, 40, ' '));
						} else {
							detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getNombreDeudor(), 40, ' '));
						}
						// ZONA B3 - DOMICILIO
						detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getDomicilioDeudor(), 39, ' '));
						// ZONA B4 - MUNICIPIO
						detalle = detalle.append(FuncionesComunes.truncarDcha(reg.getMunicipio(), 12, ' '));
						// ZONA B5 - COD POSTAL
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getCodPostal(), 5, '0'));
						// ZONA C - IDENTIFICADOR DE LA DEUDA
						detalle = detalle.append(FuncionesComunes.truncarIzda(reg.getIdDeuda(), 13, '0'));
						// ZONA D - IMPORTE EMBARGAR
						detalle = detalle.append(FuncionesComunes.truncarIzda(df.format(importe).replace(",", ""), 15, '0'));
						// ZONA E - TEXTO LIBRE
						// ZONA E1 - TEXTO LIBRE 1
						detalle = detalle.append(StringUtils.leftPad("", 50));
						// ZONA E2 - TEXTO LIBRE 2
						detalle = detalle.append(StringUtils.leftPad("", 50));
						// ZONA E3 - TEXTO LIBRE 3
						detalle = detalle.append(StringUtils.leftPad("", 50));
						// ZONA F - LIBRE
						detalle = detalle.append(StringUtils.leftPad("", 366));
						writer.println(detalle.toString());*/
						totalReg++;
					}
				}
				/*******************************************************
				 * FIN FICHERO
				 *******************************************************/
				// Finalmente generamos el registro fin de archivo
				StringBuffer finFichero = new StringBuffer();
				// ZONA A - 8
				finFichero = finFichero.append("8");
				// ZONA B - Libre
				finFichero = finFichero.append(StringUtils.leftPad("", 2));
				// ZONA C - CODIGO NRBE Entidad
				finFichero = finFichero.append(FuncionesComunes.truncarIzda(codBanco, 4, '0'));
				// ZONA D - NUM REGISTROS FICHERO (INCLUIDO EL FIN DE FICHERO Y
				// CABECERA
				finFichero = finFichero
						.append(FuncionesComunes.truncarIzda(String.valueOf(totalReg + 2), 8, '0'));
				// ZONA E - LIBRE
				finFichero = finFichero.append(StringUtils.leftPad("", 8));
				// ZONA F
				// ZONA F1 - IMPORTE TOTAL EMBARGAR
				finFichero = finFichero
						.append(StringUtils.leftPad(df.format(totalEmbargar).replace(",", ""), 15, '0'));
				// ZONA F2 - IMPORTE TOTAL RETENIDO (FASE 3 CEROS)
				finFichero = finFichero.append(StringUtils.leftPad("", 15, '0'));
				// ZONA G
				// ZONA G1 - NIF ENTIDAD
				finFichero = finFichero.append(FuncionesComunes.truncarIzda(entidad.getNifentidad(), 9, ' '));
				// ZONA G2 - INE
				finFichero = finFichero.append(FuncionesComunes.truncarIzda(banco.getC63codine(), 6, ' '));
				// ZONA G3 - Nombre
				finFichero = finFichero.append(FuncionesComunes.truncarDcha(entidad.getNombreentidad(), 40, ' '));
				// ZONA H - LIBRE
				finFichero = finFichero.append(StringUtils.leftPad("", 542));

				writer.print(finFichero.toString());

				// Cerramos el fichero
				writer.close();
				// Hacemos que el archivo se descargue para el usuario
				Filedownload.save(new FileInputStream(archivo), "text/plain",
						"fase3_" + codEntidad + "_" + codBanco + ".txt");
				// Eliminamos el archivo temporal creado
				archivo.delete();
			} else {
				Messagebox.show("Debe seleccionar una entidad, banco y subir el archivo de la fase 2.");
			}
		} catch (Exception e) {
			Messagebox.show("Se produjo un error al importar el archivo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGenerarFaseTres.detach();
	}
	
	private boolean existeDeuda(List<Fase2C63> registros, String codDeuda) {
		for(Fase2C63 reg : registros) {
			if(codDeuda.equals(reg.getCodDeuda())) {
				return true;
			}
		}
		return false;
	}

}
