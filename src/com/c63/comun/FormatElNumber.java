package com.c63.comun;

import java.text.NumberFormat;

public class FormatElNumber {

	public static String formatImporte(double importe) {
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(importe);
	}
}
