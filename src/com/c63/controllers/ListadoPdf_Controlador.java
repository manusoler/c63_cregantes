package com.c63.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Map;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class ListadoPdf_Controlador extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	@Wire
	private Window listadoPdf;
	@Wire
	private Iframe iframe;
	private byte[] archivo;

	@Listen("onClick = #closeButton")
	public void close() {
		listadoPdf.detach();
	}

	@Override
	public void doAfterCompose(Component window) throws Exception {
		super.doAfterCompose(window);
		try {
			String reportName = (String) Executions.getCurrent().getArg().get("report");
			Connection bbddConn = (Connection) Executions.getCurrent().getArg().get("bbddConn");
			Map<String, Object> parameters = (Map<String, Object>) Executions.getCurrent().getArg().get("params");
			JasperReport report = (JasperReport) JRLoader.loadObjectFromFile(Executions.getCurrent().getDesktop()
			        .getWebApp().getRealPath("/reports/" + reportName));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			// Si no se le pasa un datasource, aunque sea vacío jasper genera
			// pdfs vacíos
			JasperPrint jasperPrint;
			if (bbddConn != null) {
				jasperPrint = JasperFillManager.fillReport(report, parameters, bbddConn);
			} else {
				jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			}
			JasperExportManager.exportReportToPdfStream(jasperPrint, output);
			archivo = output.toByteArray();
			ByteArrayInputStream mediais = new ByteArrayInputStream(archivo);
			iframe.setContent(new AMedia("FirstReport.pdf", "pdf", "application/pdf", mediais));
		} catch (JRException jre) {
			jre.printStackTrace();
		}
	}
}
