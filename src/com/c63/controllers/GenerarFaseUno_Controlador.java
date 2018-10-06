package com.c63.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.c63.comun.FuncionesComunes;
import com.c63.modelo.BancosC63;
import com.c63.modelo.Entidades;
import com.c63.modelo.Recibos;
import com.c63.modelo.dao.BancosC63_Dao;
import com.c63.modelo.dao.BancosC63_IDao;
import com.c63.modelo.dao.Entidades_Dao;
import com.c63.modelo.dao.Entidades_IDao;
import com.c63.modelo.dao.Recibos_Dao;
import com.c63.modelo.dao.Recibos_IDao;

public class GenerarFaseUno_Controlador extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;

	@Wire
	private Window winGenerarFaseUno;
	@Wire
	private Listbox lbCodigoEntidad;
	@Wire
	private Listbox lbBanco;
	@Wire
	private Button btnGenerar;
	@Wire
	private Button btnCancelar;
	private Window padre;

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

	@Listen("onCreate = #winGenerarFaseUno")
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

	@Listen("onClick = #btnGenerar")
	public void generar() {
		try {
			// Comprobamos que haya seleccionado una entidad y un banco
			if (lbCodigoEntidad.getSelectedCount() > 0 && lbBanco.getSelectedCount() > 0) {
				String codEntidad = (String) lbCodigoEntidad.getSelectedItem().getValue();
				String codBanco = (String) lbBanco.getSelectedItem().getValue();

				// Creamos el archivo temporal que contrendra el excel
				File archivo = File.createTempFile("fase_uno", ".txt");
				PrintWriter writer = new PrintWriter(archivo);

				// Consultamos los datos del banco y entidad seleccionadas
				BancosC63_IDao dao = new BancosC63_Dao();
				BancosC63 banco = dao.leerBancoC63(codEntidad, codBanco);
				Entidades_IDao daox = new Entidades_Dao();
				Entidades entidad = daox.leerEntidad(codEntidad);
				// Y el numero de recibos a consultar
				Recibos_IDao reciDao = new Recibos_Dao();
				List<Recibos> recibos = reciDao.listarRecibosEntidad(codEntidad);

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
				// ZONA F1 - ID FASE = 1
				cabecera = cabecera.append('1');
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
				// Para cada recibo, generamos su contenido
				for (Recibos recibo : recibos) {
					StringBuffer detalle = new StringBuffer();
					// ZONA A - COD REGISTRO = 6
					detalle = detalle.append("6");
					// ZONA B - ID DEUDOR
					// ZONA B1 - NIF
					detalle = detalle.append(FuncionesComunes.truncarDcha(recibo.getContribuyente().getCifNif(), 9, ' '));
					// ZONA B2 - NOMBRE / RAZON SOCIAL
					if (FuncionesComunes.esPersonaFisica(recibo.getContribuyente().getCifNif())) {
						// Para personas fisicas los apellidos y nombre van
						// separados por *
						String[] nombre = recibo.getContribuyente().getNombreRazon().split(" ");
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
						detalle = detalle.append(FuncionesComunes.truncarDcha(recibo.getContribuyente().getNombreRazon(), 40, ' '));
					}
					// ZONA B3 - DOMICILIO
					detalle = detalle.append(FuncionesComunes.truncarDcha(recibo.getContribuyente().getDomicilio(), 39, ' '));
					// ZONA B4 - MUNICIPIO
					detalle = detalle.append(FuncionesComunes.truncarDcha(recibo.getContribuyente().getPoblacion(), 12, ' '));
					// ZONA B5 - COD POSTAL
					detalle = detalle.append(FuncionesComunes.truncarIzda(recibo.getContribuyente().getCp(), 5, '0'));
					// ZONA C - IDENTIFICADOR DE LA DEUDA
					detalle = detalle
							.append(FuncionesComunes.truncarDcha(String.valueOf(recibo.getIdRecibo()), 13, ' '));
					// ZONA D - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 15));
					// ZONA E - CODIGO DEUDA / DEUDOR
					detalle = detalle
							.append(FuncionesComunes.truncarDcha(String.valueOf(recibo.getNumRegante()), 8, ' '));
					// ZONA F - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 15));
					// ZONA G - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 1));
					// ZONA H - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 8));
					// ZONA I - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 8));
					// ZONA J - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 111));
					// ZONA K - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 6));
					// ZONA L - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 36));
					// ZONA M - LIBRE
					detalle = detalle.append(StringUtils.leftPad("", 323));

					writer.println(detalle.toString());
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
				// ¿CABECERA?) ???
				finFichero = finFichero
						.append(FuncionesComunes.truncarIzda(String.valueOf(recibos.size() + 2), 8, '0'));
				// ZONA E - LIBRE
				finFichero = finFichero.append(StringUtils.leftPad("", 8));
				// ZONA F
				// ZONA F1 - IMPORTE TOTAL EMBARGAR (FASE 1 CEROS)
				finFichero = finFichero.append(StringUtils.leftPad("", 15, '0'));
				// ZONA F2 - IMPORTE TOTAL RETENIDO (FASE 1 CEROS)
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
						"fase1_" + codEntidad + "_" + codBanco + ".txt");
				// Eliminamos el archivo temporal creado
				archivo.delete();
			} else {
				Messagebox.show("Debe seleccionar una entidad y un banco para poder generar el archivo.");
			}
		} catch (Exception e) {
			Messagebox.show("Se produjo un error al importar el archivo: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Listen("onClick = #btnCancelar")
	public void cancelar() {
		winGenerarFaseUno.detach();
	}

}
