package com.c63.modelo.dao;


import java.sql.Date;
import java.util.List;

import com.c63.modelo.Intereses;

public interface Intereses_IDao{

	public abstract List<Intereses> listarIntereses();

	public abstract String insertarIntereses(
			String fecha_desde, 
			String fecha_hasta, 
			String denominacion, 
			Double porcentaje
			);

	public abstract String updateIntereses( 
			String fecha_desde, 
			String fecha_hasta, 
			String denominacion, 
			Double porcentaje
			);
	
	public abstract String borrarIntereses();

	public abstract Intereses leerIntereses(String fecha_desde);
	
	public abstract Date obtenerSiguienteFechaDesde();

}