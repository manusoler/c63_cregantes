package com.c63.comun;

import java.sql.Connection;
import java.sql.DriverManager;

import org.zkoss.zul.Messagebox;

public class ConexionBD {

	public static Connection conBD_recore() {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String connectString = "jdbc:mysql://localhost/cuaderno63";
			String user = "cuaderno63";
			String password = "pruebac63";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(connectString, user, password);
			return conn;
		} catch (Exception e) {
			System.out.println("Error conexion BD MYSQL RECORE: " + e.getMessage());
			Messagebox.show("Error conexion BD MYSQL RECORE." + e.getMessage());
			return null;
		}
	}

}
