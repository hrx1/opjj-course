package hr.fer.zemris.java.hw07.crypto;

import java.util.Objects;

/**
 * Util class provides two public static methods: hextobyte(keyText) and bytetohex(bytearray).
 * 
 * @author Hrvoje
 *
 */
public class CryptoUtil {
	
	
	/**
	 * Method hextobyte(keyText) takes hex-encoded String and returns appropriate byte[]. If string is
	 * not valid (odd-sized, has invalid characters) throws an IllegalArgumentException. For zero-length
	 * string, method must return zero-length byte array. Method must support both uppercase letters and
	 * lowercase letters.
	 * 
	 * For example: hextobyte("01aE22") should return byte[] {1, -82, 34}.
	
	 * @param keyText to convert
	 * @return byte[] of keyText
	 */
	public static byte[] hexToByte(String keyText) {
		Objects.requireNonNull(keyText);
		if (keyText.length() % 2 != 0) 
			throw new IllegalArgumentException("String shouldn't have odd length");
		
		byte[] result = new byte[keyText.length() / 2];
		
		for(int i = 0, j = 0; i < result.length; ++i, j+=2) {
			result[i] = (byte) Integer.parseInt(keyText, j, j+2, 16);
		}

		return result;
	}
	
	
	/**
	 * Method bytetohex(bytearray) takes a byte array and creates its hex-encoding: for each byte of given
	 * array, two characters are returned in string, in big-endian notation. For zero-length array an empty string
	 * must be returned. Method should use lowercase letters for creating encoding.
	 * 
	 * For example: bytetohex(new byte[] {1, -82, 34}) should return "01ae22".
	 * @param byteArray to analyse
	 * @return String of hex values of byte Array
	 */
	public static String byteToHex(byte[] byteArray) {
		Objects.requireNonNull(byteArray);
		
		StringBuilder result = new StringBuilder();
		
		for(int i = 0; i < byteArray.length; ++i) {
			result.append(addLeadingZeroes(Integer.toHexString(byteArray[i] & 0xff)));
		}
		
		return result.toString();
	}
	
	/**
	 * Adds leading zero to hex value in hexString if necessary.
	 * @param hexString to add
	 * @return string with leading zero
	 */
	private static String addLeadingZeroes(String hexString) {
		if(hexString.length() == 2) return hexString;
		if(hexString.length() == 0) return "00";
		else return "0".concat(hexString);
	}
	
}
