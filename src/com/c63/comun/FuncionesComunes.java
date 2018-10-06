package com.c63.comun;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zul.Listbox;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class FuncionesComunes {
	
	final static Logger logger = Logger.getLogger(FuncionesComunes.class);
	
	public static String truncar(String param, int size) {
		if(param != null && param.length() > size) {
			logger.info("Se ha truncado el valor '" + param + "' a '" + param.substring(0, size) + "'");
			return param.substring(0, size);
		} else {
			return param;
		}
	}
	
	public static String truncarIzda(String param, int size, char fill) {
		if(param != null && param.length() > size) {
			logger.info("Se ha truncado el valor '" + param + "' a '" + param.substring(0, size) + "'");
			return param.substring(0, size);
		} else {
			return StringUtils.leftPad(param, size, fill);
		}
	}
	
	public static String truncarDcha(String param, int size, char fill) {
		if(param != null && param.length() > size) {
			logger.info("Se ha truncado el valor '" + param + "' a '" + param.substring(0, size) + "'");
			return param.substring(0, size);
		} else {
			return StringUtils.rightPad(param, size, fill);
		}
	}
	
	/**
	 * Comprueba si el NIF pertenece a una persona fisica.
	 * @param nif
	 * @throws Exception 
	 */
	public static boolean esPersonaFisica(String nif) throws Exception {
		if(nif == null) {
			throw new Exception("NIF nulo");
		}
		char ini = nif.charAt(0);
		if(Character.isDigit(ini) || ini == 'K' || ini == 'L'
			|| ini == 'M' || ini == 'X' || ini == 'Y' || ini == 'Z') {
			return true;
		}
		return false;
	}
	
	public static void seleccionarValorPorDefecto(Listbox listBox, String valor) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.getItemAtIndex(i).getValue().toString().equals(valor)
					|| listBox.getItemAtIndex(i).getLabel().toString().equals(valor)) {
				listBox.setSelectedItem(listBox.getItemAtIndex(i));
			}
		}
	}

	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
		Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
					URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}

	private static final String ALGORITHM = "AES";
	private static final byte[] keyValue = "gestionLotes1234".getBytes();

	public static String encrypt(String valueToEnc) throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encValue = c.doFinal(valueToEnc.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encValue);
		return encryptedValue;
	}

	public static String decrypt(String encryptedValue) throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGORITHM);
		Cipher c = Cipher.getInstance(ALGORITHM);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
}
