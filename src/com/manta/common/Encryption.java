package com.manta.common;

import org.apache.commons.lang3.StringUtils;

import com.manta.common.InvalidDataException;

/**
 * A class with static methods for various encryption and decryption formulas used in manta.
 */
public class Encryption {

	static final String fromSymbols = "0123456789";
	static final String toSymbols = "mtv9rwxhk1bc7f2j3lgd8nsy045pqz6";

	static final String fromSubIdSymbols = "XMT0123456789";
	static final String toSubIdSymbols = "utv9rc7f2j35pqzmlgd8nswxhk1by046";

	/**
	 * @param mid - mid to encrypt
	 * @return encrypted mid - emid
	 * @throws InvalidDataException if mid contains non-numeric symbols
	 */
	public static String encryptMID(String mid) throws InvalidDataException {
		if (mid.length() == 7) {
			return mid;
		} 
		String encrypted = remapBase(mid, fromSymbols, toSymbols);
		encrypted = StringUtils.leftPad(encrypted, 7, 'm');
		return encrypted;
	}

	/**
	 * @param emid - emid to decryptMID
	 * @return decrypted emid - mid
	 * @throws InvalidDataException if emid contains symbols other than "bcdfghjklmnpqrstvwxyz0123456789"
	 */
	public static String decryptMID(String emid) throws InvalidDataException {
		if (emid.length() == 10)
			return emid;
		String mid = remapBase(emid, toSymbols, fromSymbols);
		mid = StringUtils.leftPad(mid, 10, '0');
		return mid;
	}

	/**
	 * @param activityId - activityId to encrypt
	 * @return encrypted activityId - eActivityId
	 * @throws InvalidDataException if activityId contains symbols non-numeric symbols
	 */
	public static String encryptActivityId(String activityId) throws InvalidDataException {
		String encrypted = remapBase(activityId, fromSymbols, toSymbols);
		encrypted = StringUtils.leftPad(encrypted, 7, 'm');
		return encrypted;
	}

	/**
	 * @param eActivityId - ActivityId to decrypt
	 * @return decrypted eActivityId
	 * @throws InvalidDataException if eActivityId contains symbols other than "bcdfghjklmnpqrstvwxyz0123456789"
	 */
	public static String decryptActivityId(String eActivityId) throws InvalidDataException {
		String activityId = remapBase(eActivityId, toSymbols, fromSymbols);
		return activityId;
	}

	/**
	 * @param subId - SubId to encrypt
	 * @return encrypted - encripted subId
	 * @throws InvalidDataException if subId contains symbols other than "XMT0123456789"
	 */
	public static String encryptSubId(String subId) throws InvalidDataException{
		if (!subId.startsWith("MT"))
			return subId;
		String encrypted = remapBase(subId, fromSubIdSymbols, toSubIdSymbols);
		return encrypted;

	}

	/**
	 * @param eActivityId - SubId to decrypt
	 * @return dsid - decripted subId
	 * @throws InvalidDataException if subId contains symbols other than "bcdfghjklmnpqrstuvwxyz0123456789"
	 */
	public static String decryptSubId(String subId) throws InvalidDataException {
		if (subId.startsWith("MT"))
			return subId;
		String dsid = remapBase(subId, toSubIdSymbols, fromSubIdSymbols);
		return dsid;
	}

	private static String remapBase(String code, String fromSymbols,
			String toSymbols) {
		validate(code, fromSymbols);
		int n_from_symbols = fromSymbols.length();
		double num_val = 0;
		int pos = 0;
		char[] codeArr = code.toCharArray();
		
		for (int i = code.length() - 1; i >= 0; i--) {
			char symbol = codeArr[i];
			int symbolInd = fromSymbols.indexOf(symbol);
			symbolInd++;
			num_val = num_val + (symbolInd - 1) * Math.pow(n_from_symbols, pos);
			pos = pos + 1;
		}
		return expressBase(num_val, toSymbols);
	}

	private static String expressBase(double num_val_orig, String toSymbols) {
		double num_val = num_val_orig;
		int base = toSymbols.length();
		String expression = "";
		while (num_val > 0) {
			int startIndex = (int) (num_val % base);
			if (startIndex < toSymbols.length())
				expression = (toSymbols.substring(startIndex, startIndex + 1))
						+ expression;
			num_val = Math.floor(num_val / base);
		}
		return expression.toString();
	}

	private static void validate(String code, String fromSymbols) {
		if (code == null || code.isEmpty()) {
			throw new InvalidDataException("Code is null or empty");
		} else {
			for (char c : code.toCharArray()) {
				if (fromSymbols.indexOf(c) == -1) {
					throw new InvalidDataException(String.format("Character %c is invalid",c));
					
				}
			}
		}
	}

}
